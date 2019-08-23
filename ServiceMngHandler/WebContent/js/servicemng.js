var started = false;
var requiredScript;

var protocol = ('https:' == document.location.protocol ? 'https:' : 'http:');
var reqAddres = protocol + "//192.168.1.104:8080/ServiceMngHandler/ServiceMngHandlerServlet";
var hostAddres = protocol + "//192.168.1.104:8080/ServiceMngHandler/";
//var reqAddres = protocol + "//adele01.treelogic.local:8080/ServiceMngHandler/ServiceMngHandlerServlet";
//var hostAddres = protocol + "//adele01.treelogic.local:8080/ServiceMngHandler/";
var lang = navigator.language || navigator.userLanguage;


(function() {

	function loadScript(url, callback) {

		var script = document.createElement("script")
		script.type = "text/javascript";

		if (script.readyState) { // IE
			script.onreadystatechange = function() {
				if (script.readyState == "loaded"
						|| script.readyState == "complete") {
					script.onreadystatechange = null;
					callback();
				}
			};
		} else { // Others
			script.onload = function() {
				callback();
			};
		}

		script.src = url;
		document.getElementsByTagName("head")[0].appendChild(script);
	}

	loadScript(
			"https://ajax.googleapis.com/ajax/libs/jquery/1.6.1/jquery.min.js",
			function() {
				// Llamada inicial
				getRequiredScript();
			});

})();


function createCORSRequest() {
	var xhr;
	if (window.XMLHttpRequest) {
		xhr = new XMLHttpRequest();
	} else if (typeof XDomainRequest != "undefined") {
		// Otherwise, check if XDomainRequest.
		// XDomainRequest only exists in IE, and is IE's way of making CORS
		// requests.
		xhr = new XDomainRequest();
	} else {
		// Otherwise, CORS is not supported by the browser.
		xhr = null;
	}
	return xhr;
}

var http = createCORSRequest();


function initConnection() {
	alternative = false;
	method = "GET";
	url = reqAddres + "?action=init&user=" + usermail + "&avname=" + avname;
	http.open(method, url);
	http.withCredentials = true;
	http.onloadend = handleResponseInit;
	http.onerror = requestError;
	http.send(null);

}

function handleResponseInit() {
	if (http.readyState == 4 && http.status == 200) {
		var response = http.responseText;
		if (response) {
			if (response != 'error') {
				// Manejar respuesta
				// TODO: Very important manejar la respuesta!!!!
				// requiredScript = devolución por response
				jQuery.getScript(protocol + "//"+response+"/js/fiona-embed-1.0.js");
			} else {
				alert("This avatar is disabled right now. Sorry.");
				showDisabledMessage();
			}
		}
	}
}



function requestError() {
	if (!alternative)
		alert("Woops, there was an error making the request. Try again later, please");	
}


function getRequiredScript() {

	// Llamamos al servlet
	initConnection();
	
}
