/// @file EmailReaderSpark.cpp
/// @brief EmailReaderSpark class implementation.

#include "EmailReaderSpark.h"
#include <unistd.h>
#include <fstream>

using namespace std;

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "EmailReaderSpark")) {
			return new EmailReaderSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes EmailReaderSpark component.
void EmailReaderSpark::init(void){
	serverIMAP = getComponentConfiguration()->getString(const_cast<char *>("Server_Imap"));
	serverSMTP = getComponentConfiguration()->getString(const_cast<char *>("Server_Smtp"));
	portSMTP = getComponentConfiguration()->getInt(const_cast<char *>("Port_Smtp"));
	user = getComponentConfiguration()->getString(const_cast<char *>("User"));
	pass = getComponentConfiguration()->getString(const_cast<char *>("Password"));
    timePoll = getComponentConfiguration()->getFloat(const_cast<char*>("Time_Polling"));
    imapSearch = getComponentConfiguration()->getString(const_cast<char*>("Imap_Search"));
    mailbox = getComponentConfiguration()->getString(const_cast<char*>("Mailbox"));
    fromEmail = "";
    subject = "";
    char path[1024];
    getComponentConfiguration()->getFilePath(const_cast<char*>("Template"), path, 1024);
    std::ifstream is(path, std::ifstream::in);
    char linea[1024];
    while(is.getline(linea, 1024)) {
    	templateMail.append(linea);
    	templateMail.append("\r\n");
    	memset(linea, '\0', 1024);
    }

    //LoggerInfo("Path: %s\nTemplate:\n%s", path, templateMail.c_str());

    stopWatch.restart();
}

/// Unitializes the EmailReaderSpark component.
void EmailReaderSpark::quit(void){

}

void EmailReaderSpark::processData(char * prompt) {
	string texto(prompt);
	//LoggerInfo(texto.c_str());
	if(texto == "[DO_NOTHING]" || texto == "I don't understand that symbol")
		return;
	string replacedText = "[NO_RESPONSE_FOUND]";
	while(texto.find(replacedText) != string::npos)
		texto = texto.replace(texto.find(replacedText), replacedText.size(), "", 0);
	texto.erase( std::remove(texto.begin(), texto.end(), '\r'), texto.end() );
	if(trim(texto).empty())
		return;

	// sendEmail

    //  The mailman object is used for sending and receiving email.
    CkMailMan mailman;

    //  Any string argument automatically begins the 30-day trial.
    bool success;
    success = mailman.UnlockComponent("30-day trial");
    if (success != true) {
        LoggerError("Component unlock failed");
        return;
    }

    //  Set the SMTP server.
    mailman.put_SmtpHost(serverSMTP.c_str());
    mailman.put_SmtpUsername(user.c_str());
    mailman.put_SmtpPassword(pass.c_str());
    mailman.put_SmtpPort(portSMTP);
    mailman.put_SmtpSsl(true);
   /* mailman.put_StartTLS(true);
    if(! mailman.VerifySmtpConnection())
    	LoggerWarn("VerifySmtpConnection");
    if(! mailman.VerifySmtpLogin())
    	LoggerWarn("VerifySmtpLogin");*/
    mailman.put_RequireSslCertVerify(true);

    //  Create a new email object
    //CkEmail email;

    string copy = templateMail;

    replacedText = "{{ TEXTO }}";
    string body = copy.replace(copy.find(replacedText), replacedText.size(), texto.c_str(), texto.size()) + outcomingEmail->body();

    /*email.put_Subject(("Re: " + subject).c_str());
    email.SetHtmlBody(body.c_str());
    email.put_From(user.c_str());
    email.AddTo(fromEmail.c_str(), fromEmail.c_str());*/
    //outcomingEmail->AppendToBody(body.c_str());
    outcomingEmail->put_Body(body.c_str());
    outcomingEmail->put_From(user.c_str());

    //LoggerInfo("se intenta enviar a %s", fromEmail.c_str());
    success = mailman.SendEmail(*outcomingEmail);
    //success = mailman.SendEmail(email);
    if (success != true) {
        LoggerError("%s",mailman.lastErrorText());
        myFlow->processData(const_cast<char *>("SENDEMAIL ERROR MAILMAN"));
    }
    else {
        LoggerInfo("Email successfully sent to %s", fromEmail.c_str());
        myFlow->processData(const_cast<char *>("SENDEMAIL OK"));
    }

    delete outcomingEmail;
}

std::string EmailReaderSpark::trim(std::string& str)
{
    /*size_t firstSp = str.find_first_not_of(' ');
    size_t firstTab = str.find_first_not_of('\t');
    size_t lastSp = str.find_last_not_of(' ');
    size_t lastTab = str.find_last_not_of('\t');
    if(firstSp == firstTab && lastSp == lastTab)
    	return str;
    if(firstTab > firstSp)
    	firstSp = firstTab;
    if(lastTab < lastSp)
    	lastSp = lastTab;
    std::string ret = str;
    try {
    	ret = str.substr(firstSp, (lastSp-firstSp+1));
    }
    catch(exception & e) {
    	LoggerWarn("EmailReaderSpark::trim %s\nfirstSp: %d\tfirstTab: %d\tlastSp: %d\tlastTab: %d", e.what(), firstSp, firstTab, lastSp, lastTab);
    	return ret;
    }
    return trim(ret);*/
	string strcopy(str);
	strcopy.erase(std::remove(strcopy.begin(), strcopy.end(), ' '), strcopy.end());
	strcopy.erase(std::remove(strcopy.begin(), strcopy.end(), '\t'), strcopy.end());
	return strcopy;
}

void EmailReaderSpark::process() {
	if(stopWatch.elapsedTime() >= timePoll) {
		connectToIMAPServer();
		CkMessageSet *messageSet = 0;
		bool fetchUids = true;

		//  Find emails marked as not already seen:
		//const char *notSeenSearch = "UNSEEN";

		//  Get the set of unseen message UIDs
		messageSet = imap.Search(imapSearch.c_str(),fetchUids);
		if (messageSet == 0 ) {
			LoggerError(imap.lastErrorText());
			return;
		}

		//  Fetch the unseen emails into a bundle object:
		CkEmailBundle *bundle = 0;
		bundle = imap.FetchBundle(*messageSet);
		if (bundle == 0 ) {
			delete messageSet;
			LoggerError(imap.lastErrorText());
			return;
		}

		//  Get each email and send its body.
		//LoggerInfo("%d %s messages", bundle->get_MessageCount(), imapSearch.c_str());
		for (int i = 0; i < bundle->get_MessageCount(); i++) {
			CkEmail *email = 0;
			email = bundle->GetEmail(i);

			fromEmail = email->ck_from();
			subject = email->subject();
			outcomingEmail = email->CreateReply();
			//LoggerInfo("Flags: %s", imap.fetchFlags(i+1, false));

			string body = email->body();

			try {
				if(body.find("<body") != string::npos && body.find("</body>") != string::npos) {
					string::size_type from = body.find(">", body.find("<body")) + 1;
					body = body.substr(from, body.find("</body>") - from);
				}
			}
			catch(exception & e) {
				LoggerWarn("EmailReaderSpark::process %s", e.what());
			}

			myFlow->processData(const_cast<char*>(body.c_str()));

			delete email;
		}

		delete messageSet;
		delete bundle;

		//  Disconnect from the IMAP server.
		imap.Disconnect();

		stopWatch.restart();
	}
	else
		usleep(200000);
}

void EmailReaderSpark::connectToIMAPServer() {


	bool success;

    //  Anything unlocks the component and begins a fully-functional 30-day trial.
    success = imap.UnlockComponent("Anything for 30-day trial");
    if (success != true) {
        ERR(imap.lastErrorText());
    }

    //  Connect to an IMAP server.
    success = imap.Connect(serverIMAP.c_str());
    if (success != true) {
    	ERR(imap.lastErrorText());
    }

    //  Login
    success = imap.Login(user.c_str(), pass.c_str());
    if (success != true) {
    	ERR(imap.lastErrorText());
    }

    //  Select an IMAP mailbox
    success = imap.SelectMailbox(mailbox.c_str());
    if (success != true) {
    	ERR(imap.lastErrorText());
    }
}
