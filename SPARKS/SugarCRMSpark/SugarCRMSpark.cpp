/**
 * @author Alejandro Cividanes
 * @version 1.0 16/12/2014
 */

#include "SugarCRMSpark.h"
#include "md5.h"
#include <sstream>
#include <fstream>
#include "base64.h"

#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "SugarCRMSpark")) {
			return new SugarCRMSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

void SugarCRMSpark::init(void) {
	username = "j.martin";//getComponentConfiguration()->getString(const_cast<char*>("Username"));
	password = "adelaida";//getComponentConfiguration()->getString(const_cast<char*>("Password"));
	url = "http://crm.adelerobots.com/service/v4_1/rest.php";//getComponentConfiguration()->getString(const_cast<char*>("Url"));
	session.clear();
}

void SugarCRMSpark::quit(void) {
}

void SugarCRMSpark::processData(char* prompt) {
	string nombre(prompt);

	string email = getEmailFromName(nombre);

	/*string email = getBase64FileFromDocumentName(nombre);

	std::ofstream fs("/home/adele/barcelona.pdf", std::fstream::out);
	fs << email;
	fs.close();*/

	myFlowChar->processData(const_cast<char*>(email.c_str()));
}

string SugarCRMSpark::getEmailFromName(string nombre) {
	if(session.empty())
		session = login(username, password);
	string email = getEmail(session, nombre);
	return email;
}

string SugarCRMSpark::getBase64FileFromDocumentName(string nombre) {
	if(session.empty())
		session = login(username, password);
	string id = getIDFile(session, nombre);
	string file = base64_decode(getBase64File(session, id));
	return file;
}

string SugarCRMSpark::login(string user, string pass) {
	stringstream sesion;

	string urlLogin = url + "?method=login&input_type=JSON&response_type=JSON&rest_data=";
	string passwd = md5(pass);

	urlLogin +=
			"{\"user_auth\":{\"user_name\":\"" +
			user +
			"\",\"password\":\"" +
			passwd +
			"\"}}";

	argsLogin = urlLogin.substr(urlLogin.find('?') + 1);

	//LoggerInfo("SugarCRMSpark url = %s", argsLogin.c_str());

	// Send the request
	try
    {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;

		// Set the URL.
		request.setOpt<curlpp::options::Url>(urlLogin);
		request.setOpt<curlpp::options::CookieJar>("/tmp/CRM-cookie");
		request.setOpt<curlpp::options::CookieSession>(true);
		//request.setOpt(new curlpp::options::Verbose(true));

		sesion << request;
    }
	catch(curlpp::LogicError& e )
	{
    	LoggerError("Error making request : %s", e.what());
	}
    catch(curlpp::RuntimeError& e)
    {
    	LoggerError("Error making request : %s", e.what());
    }

	string sess;
	Json::Value APIResponseJSON;

	//LoggerInfo("SugarCRMSpark login response = %s", sesion.str().c_str());

	if(!Json::Reader().parse(sesion.str(), APIResponseJSON))
				LoggerError("SugarCRMSpark::login> Error parsing API reply.");

	sess = APIResponseJSON.get("id", "").asString();

	//LoggerInfo("SugarCRMSpark session = %s", sess.c_str());

	return sess;
}

string SugarCRMSpark::getEmail(string sesion, string nombre) {
	stringstream email;

	string urlEmail = url + "?method=get_entry_list&input_type=JSON&response_type=JSON&rest_data=";
	string first_name;
	string last_name;
	if(nombre.find(' ')!=string::npos) {
		first_name = nombre.substr(0, nombre.find(' '));
		last_name = nombre.substr(nombre.find_last_of(' ') +1);
	}
	else {
		first_name = nombre;
		last_name = "";
	}

	//LoggerInfo("SugarCRMSpark first_name: %s last_name: %s", first_name.c_str(), last_name.c_str());

	urlEmail +=
			"{\"session\":\"" +
			sesion +
			"\",\"module_name\":\"Contacts\",\"query\":\"contacts.first_name like '" +
			first_name +
			"%' and contacts.last_name like '%" +
			last_name +
			"%'\",\"order_by\":\"name\",\"offset\":0,\"select_fields\":[\"email1\"],\"deleted\":0,\"favorites\":false}";

	string argsEmail = urlEmail.substr(urlEmail.find('?') + 1);

	//LoggerInfo("SugarCRMSpark url = %s", urlEmail.c_str());

	// Send the request
	try
    {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;

		// Set the URL.
		request.setOpt<curlpp::options::Url>(url);
		request.setOpt<curlpp::options::Post>(true);
		request.setOpt<curlpp::options::PostFields>(argsEmail);
		request.setOpt<curlpp::options::CookieSession>(true);
		request.setOpt<curlpp::options::CookieFile>("/tmp/CRM-cookie");
		//request.setOpt<curlpp::options::Cookie>("PHPSESSID="+sesion);
		//request.setOpt(new curlpp::options::Verbose(true));

		email << request;
    }
	catch(curlpp::LogicError& e )
	{
    	LoggerError("Error making request : %s", e.what());
	}
    catch(curlpp::RuntimeError& e)
    {
    	LoggerError("Error making request : %s", e.what());
    }

	string mail;
	Json::Value APIResponseJSON;

	string s = email.str();
	//LoggerInfo("SugarCRMSpark email response = %s", s.c_str());

	if(!Json::Reader().parse(email.str(), APIResponseJSON))
				LoggerError("SugarCRMSpark::getEmail> Error parsing API reply.");

	int coincidencias = APIResponseJSON.get("result_count", 0).asInt();
	if(coincidencias == 0) {
		LoggerError("SugarCRMSpark No se han encontrado coincidencias con el nombre %s", nombre.c_str());
		LoggerError("SugarCRMSpark url = %s", urlEmail.c_str());
		return "";
	}
	if(coincidencias > 1) {
		LoggerError("SugarCRMSpark Se han encontrado varias coincidencias para el nombre %s", nombre.c_str());
		return "";
	}

	mail = APIResponseJSON["entry_list"][0u]["name_value_list"]["email1"].get("value","").asString();

	LoggerInfo("SugarCRMSpark email = %s", mail.c_str());

	return mail;
}

string SugarCRMSpark::getIDFile(string sesion, string nombre) {
	stringstream id;

	string urlFile = url + "?method=get_entry_list&input_type=JSON&response_type=JSON&rest_data=";

	urlFile +=
			"{\"session\":\"" +
			sesion +
			"\",\"module_name\":\"Documents\",\"query\":\"documents.document_name like '" +
			nombre +
			"%'\",\"order_by\":\"\",\"offset\":0,\"select_fields\":[\"document_revision_id\",\"filename\"],\"deleted\":0,\"favorites\":false}";

	string argsFile = urlFile.substr(urlFile.find('?') + 1);

	//LoggerInfo("%s", urlFile.c_str());

	// Send the request
	try
    {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;

		// Set the URL.
		request.setOpt<curlpp::options::Url>(url);
		request.setOpt<curlpp::options::Post>(true);
		request.setOpt<curlpp::options::PostFields>(argsFile);
		request.setOpt<curlpp::options::CookieSession>(true);
		request.setOpt<curlpp::options::CookieFile>("/tmp/CRM-cookie");

		id << request;
    }
	catch(curlpp::LogicError& e )
	{
    	LoggerError("Error making request : %s", e.what());
	}
    catch(curlpp::RuntimeError& e)
    {
    	LoggerError("Error making request : %s", e.what());
    }

	string idFile;
	Json::Value APIResponseJSON;

	if(!Json::Reader().parse(id.str(), APIResponseJSON))
				LoggerError("SugarCRMSpark::getIDFile> Error parsing API reply: %s", id.str().c_str());

	int coincidencias = APIResponseJSON.get("result_count", 0).asInt();
	if(coincidencias == 0) {
		LoggerError("SugarCRMSpark No se han encontrado coincidencias con el nombre %s", nombre.c_str());
		LoggerInfo("%s", urlFile.c_str());
		return "";
	}
	if(coincidencias > 1) {
		LoggerError("SugarCRMSpark Se han encontrado varias coincidencias para el nombre %s", nombre.c_str());
		return "";
	}

	idFile = APIResponseJSON["entry_list"][0u]["name_value_list"]["document_revision_id"].get("value","").asString();

	LoggerInfo("SugarCRMSpark Se encontro documento: %s", APIResponseJSON["entry_list"][0u]["name_value_list"]["filename"].get("value","").asString().c_str());

	return idFile;
}

string SugarCRMSpark::getBase64File(string sesion, string id) {
	stringstream fileJson;

	string urlFile = url + "?method=get_document_revision&input_type=JSON&response_type=JSON&rest_data=";

	urlFile +=
			"{\"session\":\"" +
			sesion +
			"\",\"i\":\"" +
			id +
			"\"}";

	string argsFile = urlFile.substr(urlFile.find('?') + 1);

	//LoggerInfo("%s", urlFile.c_str());

	// Send the request
	try
    {
		curlpp::Cleanup cleaner;
		curlpp::Easy request;

		// Set the URL.
		request.setOpt<curlpp::options::Url>(url);
		request.setOpt<curlpp::options::Post>(true);
		request.setOpt<curlpp::options::PostFields>(argsFile);
		request.setOpt<curlpp::options::CookieSession>(true);
		request.setOpt<curlpp::options::CookieFile>("/tmp/CRM-cookie");

		fileJson << request;
    }
	catch(curlpp::LogicError& e )
	{
    	LoggerError("Error making request : %s", e.what());
	}
    catch(curlpp::RuntimeError& e)
    {
    	LoggerError("Error making request : %s", e.what());
    }

	string file64;
	Json::Value APIResponseJSON;

	if(!Json::Reader().parse(fileJson.str(), APIResponseJSON))
				LoggerError("SugarCRMSpark::getBase64File> Error parsing API reply.");

	string coincidencias = APIResponseJSON.get("name", "").asString();
	if(coincidencias == "No Records") {
		LoggerError("SugarCRMSpark No se han encontrado coincidencias con el id %s", id.c_str());
		return "";
	}

	file64 = APIResponseJSON["document_revision"].get("file","").asString();


	size_t pos = 0;
	while((pos = file64.find("\\", pos)) != string::npos){
		// Sustituir
		file64.replace(pos, 1, "", 0);
	}

	return file64;
}
