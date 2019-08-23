/// @file EmailSenderSpark.cpp
/// @brief EmailSenderSpark class implementation.

#include "stdAfx.h"
#include "EmailSenderSpark.h"

#include <stdio.h>
#include <string.h>
#include <CkMailMan.h>
#include <CkEmail.h>
#include <CkString.h>
#include <CkByteData.h>
#include <sys/stat.h>

#define SKIP_PEER_VERIFICATION
#define SKIP_HOSTNAME_VERFICATION

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
	if (!strcmp(componentType, "EmailSenderSpark")) {
			return new EmailSenderSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes EmailSenderSpark component.
void EmailSenderSpark::init(void){
	try{from = getComponentConfiguration()->getString(const_cast<char *>("From"));}catch(Exception &e){from = "";}
	try{to = getComponentConfiguration()->getString(const_cast<char *>("To"));}catch(Exception &e){to = "";}
	try{subject = getComponentConfiguration()->getString(const_cast<char *>("Subject"));}catch(Exception &e){subject = "";}
	cc.clear();
	bbc.clear();
	mailserver = "mail.adelerobots.com";
	headers.clear();
	attachments.clear();

	crm.init();
}

/// Unitializes the EmailSenderSpark component.
void EmailSenderSpark::quit(void){
	crm.quit();
}

void EmailSenderSpark::processData(char *prompt){

	string p(prompt);
	string request;
	if(p.find("[EMAIL] ")!=string::npos) return;
	if(p.find("[REQUEST] ")!=string::npos) return;
	if(p.find("[SENDEMAIL] ")!=string::npos) {
		request = p.substr(12);
	}
	try {
		parse(request);
	} catch(Exception &e){
		LoggerError("Error creating the email: %s", e.msg);
		clearVariables();
		string out("SENDEMAIL ERROR " + string(e.msg));
		myFlow->processData(const_cast<char*>(out.c_str()));
		return;
	}


    //  The mailman object is used for sending and receiving email.
    CkMailMan mailman;

    //  Any string argument automatically begins the 30-day trial.
    bool success;
    success = mailman.UnlockComponent("30-day trial");
    if (success != true) {
        LoggerError("Component unlock failed");
    	clearVariables();
        return;
    }

    //  Set the SMTP server.
    mailman.put_SmtpHost(mailserver.c_str());
    mailman.put_SmtpUsername("alejandro.cividanes@adelerobots.com");
    mailman.put_SmtpPassword("503037");

    //  Create a new email object
    CkEmail email;

    email.put_Subject(subject.c_str());
    email.SetHtmlBody(text.c_str());
    email.put_From(from.c_str());
    email.AddTo(to.c_str(), to.c_str());

    if(!bbc.empty())
    	email.AddBcc(bbc.c_str(), bbc.c_str());

    if(!cc.empty())
    	email.AddBcc(cc.c_str(), cc.c_str());

    //  The last argument indicates the charset to use for the attached text.
    //  The string is converted to this charset and attached to the email as a text file attachment.
    //  The charset can be anything: "utf-8", "iso-8859-1", "shift_JIS", "ansi", etc.
    if(hasAttachment()) {
    	for(unsigned int i = 0; i < attachments.size(); i++) {
    		if(exists(attachments[i]))
    			email.addFileAttachment(attachments[i].c_str());
    		else {
    			string file = crm.getBase64FileFromDocumentName(attachments[i]);
    			if(file == "") {
    				clearVariables();
    				myFlow->processData("SENDEMAIL ERROR NOFILE");
    				return;
    			}
    			CkByteData data;
    			data.append2((void*)file.c_str(), file.size());
    			string name = attachments[i] + ".pdf";
    			email.AddDataAttachment2(name.c_str(), data, "base64");
    		}
    	}
    }

    LoggerInfo("se intenta enviar a %s", to.c_str());
    success = mailman.SendEmail(email);
    if (success != true) {
        LoggerError("%s",mailman.lastErrorText());
        myFlow->processData("SENDEMAIL ERROR MAILMAN");
    }
    else {
        LoggerInfo("Email successfully sent to %s", to.c_str());
        myFlow->processData("SENDEMAIL OK");
    }

	clearVariables();
}

void EmailSenderSpark::parse(string &promt) {
	string str = "from=";
	size_t found = promt.find(str);
	if(found==string::npos)
		try{from = getComponentConfiguration()->getString(const_cast<char *>("From"));}catch(Exception &e){from = "";}
	else {
		from = promt.substr(found+str.length(),promt.find("|",found+str.length())-found-str.length());
		promt.erase(found, str.length() + from.length() );
	}
	if(from.length() == 0)
		throw Exception(const_cast<char *>("NOFROM"));

	str = "to=";
	found = promt.find(str);
	if(found==string::npos)
		try{to = getComponentConfiguration()->getString(const_cast<char *>("To"));}catch(Exception &e){to = "";}
	else {
		to = promt.substr(found+str.length(),promt.find("|",found+str.length())-found-str.length());
		promt.erase(found, str.length() + to.length() );
	}
	if(to.length() == 0)
		throw Exception(const_cast<char *>("NOTO"));
	if(!isEmail(to)) {
		to = crm.getEmailFromName(to);
	}
	if(to.length() == 0)
		throw Exception(const_cast<char *>("NOTO"));

	LoggerInfo("to = %s", to.c_str());

	str = "subject=";
	found = promt.find(str);
	if(found==string::npos)
		try{subject = getComponentConfiguration()->getString(const_cast<char *>("Subject"));}catch(Exception &e){subject = "";}
	else {
		subject = promt.substr(found+str.length(),promt.find("|",found+str.length())-found-str.length());
		promt.erase(found, str.length() + subject.length() );
	}

	string tmp_headers;
	str = "headers=\"";
	found = promt.find(str);
	if(found!=string::npos) {
		tmp_headers = promt.substr(found+str.length(),promt.find("\"",found+str.length())-found-str.length());
		promt.erase(found, str.length() + tmp_headers.length() );
	}

	size_t pos;
	if(!tmp_headers.empty()) {
		do {
			pos = tmp_headers.find(",");
			string head;
			if(pos != string::npos) {
				head = tmp_headers.substr(0,pos);
				tmp_headers = tmp_headers.substr(head.length()+1);
				headers.push_back(head);
			}
			else {
				head = tmp_headers;
				headers.push_back(tmp_headers);
			}
			if(head.substr(0,4).find("Cc: ") != string::npos) {
				cc = head.substr(4);
			}
			if(head.substr(0,5).find("Bbc: ") != string::npos) {
				bbc = head.substr(5);
			}
		}while(pos!=string::npos);
	}

	string tmp_text;
	str = "text=\"";
	found = promt.find(str);
	if(found!=string::npos) {
		tmp_text = promt.substr(found+str.length(),promt.find("\"",found+str.length())-found-str.length());
		promt.erase(found, str.length() + tmp_text.length() );
	}

	/*if(!tmp_text.empty()) {
		do {
			pos = tmp_text.find("\n");
			if(pos != string::npos) {
				string line = tmp_text.substr(0,pos);
				tmp_text = tmp_text.substr(line.length()+1);
				text.push_back(line + BL);
			}
			else
				text.push_back(tmp_text + BL);
		}while(pos!=string::npos);
	}*/
	text = tmp_text;

	string tmp_attachment;
	str = "attachments=\"";
	found = promt.find(str);
	if(found!=string::npos){
		tmp_attachment = promt.substr(found+str.length(),promt.find("\"",found+str.length())-found-str.length());
		promt.erase(found, str.length() + tmp_attachment.length() );
	}

	if(!tmp_attachment.empty()) {
		do {
			pos = tmp_attachment.find(",");
			string attachment;
			if(pos != string::npos) {
				attachment = tmp_attachment.substr(0,pos);
				tmp_attachment = tmp_attachment.substr(attachment.length()+1);
				attachments.push_back(attachment);
			}
			else {
				attachments.push_back(tmp_attachment);
			}
		}while(pos!=string::npos);
	}
}

void EmailSenderSpark::clearVariables() {
	headers.clear();
	text.clear();
	cc.clear();
	bbc.clear();
	attachments.clear();
}

const char * EmailSenderSpark::buildRequest(string from , string to, string subject, vector<pair<string, string> > headers, string text, vector<string> attachments) {
	string request;
	if(!from.empty())
		request.append("from=" + from + "|");
	if(!to.empty())
		request.append("to=" + to + "|");
	if(!subject.empty())
		request.append("subject=" + subject + "|");
	if(!headers.empty()) {
		request = request + "headers=\"";
		for(unsigned int i = 0; i < headers.size(); i++) {
			request = request + headers[i].first + ": " + headers[i].second + ",";
		}
		request = request.substr(0, request.size()-1) + "\"|";
	}
	size_t pos = 0;
	while((pos = text.find(">", pos)) != string::npos){
		// Sustituir
		text.replace(pos, 1, ">\n", 2);
		pos+=2;
	}
	request = request + "text=\"" + text + "\"|";
	if(!attachments.empty()) {
		request = request + "attachments=\"";
		for(unsigned int i = 0; i < attachments.size(); i++) {
			request = request + attachments[i] + ",";
		}
		request = request.substr(0, request.size()-1) + "\"|";
	}
	return request.c_str();
}

bool EmailSenderSpark::hasAttachment() {
	return ! attachments.empty();
}

bool EmailSenderSpark::isEmail(string email) {
	/*
	 * A traditional valid email address can be of the form:
	 *
	 * alejandro.cividanes@adelerobots.com
	 *
	 * 1) search for the @ character, does it appear only once? if yes:
	 * 2) search characters on the left of @: are they from the valid set? if yes:
	 * 3) search characters to the right of @: are they from the valid set? if yes:
	 * 4) does the character . appear at least once to the right of @? if yes:
	 * 5) the email address is valid.
	 *
	 * constexpr std::string valid_set("0123456789abcdefghijklmnopqrstuvwxyz._-");
	 */

	std::string valid_set("0123456789abcdefghijklmnopqrstuvwxyz._-");
	// 1)
	if(email.find('@') == string::npos)
		return false;
	if(email.find('@', email.find('@') + 1) != string::npos)
		return false;
	// 2)
	string nick = email.substr(0, email.find('@'));
	for(size_t i = 0; i < nick.size(); i++) {
		if(valid_set.find(nick[i]) == string::npos)
			return false;
	}
	// 3)
	string domain = email.substr(email.find('@') + 1);
	for(size_t i = 0; i < domain.size(); i++) {
		if(valid_set.find(domain[i]) == string::npos)
			return false;
	}
	// 4)
	if(domain.find('.') == string::npos)
		return false;
	// 5)
	return true;
}

bool EmailSenderSpark::exists (const std::string& name) {
  struct stat buffer;
  return (stat (name.c_str(), &buffer) == 0);
}

bool EmailSenderSpark::isBase64 (const std::string& name) {
	for(string::const_iterator it = name.begin(); it != name.end(); it++) {
		if(!isalnum(*it) && (*it != '+') && (*it != '/') && (*it != '='))
			return false;
	}
	return true;
}
