var r5appserver;
var r5server;
var flashvarscam;
var flashvarsrcv;
var paramscam;
var paramsrcv;
var attributesrcv;
var randomRoom;
var sessionNew = true;
var dupTabAttempt = false;
var fionaDivInicial, divInicialParent;
var idCallingElement;
var started = false;
var usermail, usrid2, usrid1;
var onclickValue;
var isAccesible;
var protocol=('https:' == document.location.protocol ? 'https:' : 'http:');
//var reqAddres = protocol + "//aio0001.adelerobots.com/FionaHandler/FionaEmbedServlet";
//var hostAddres = protocol + "//aio0001.adelerobots.com/";
//var red5Addres = "rtmp:\/\/aio0001.adelerobots.com\/FionaRed5\/";
var reqAddres = protocol + "//192.168.1.225/FionaHandler/FionaEmbedServlet";
var hostAddres = protocol + "//192.168.1.225/";
var red5Addres = "rtmp:\/\/192.168.1.225\/FionaRed5\/";
var alternative = false;
var lang = navigator.language || navigator.userLanguage;
var mousePosX = 640/2;
var mousePosY = 480/2;
var deltaFactor = 0;
var buttonText = "Send";
var buttonStyle = "";

if (typeof btnText != "undefined")
	buttonText = btnText;
	
if (typeof btnStyle != "undefined")
	buttonStyle = btnStyle;


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
			protocol + "//ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js",
			function() {
				jq = $.noConflict(true);
				// Preparación de objetos para red5
				userscope = "avatar";
				usercamscope = "usercam";

				// Dar valor a la dirección del red5
				r5appserver = red5Addres;
				r5server = r5appserver;

				//console.log(r5server);

				flashvarscam = {
					stream : usercamscope,
					width : '200',
					height : '130', // size de la imagen
					windowwidth : '214',
					windowheight : '160',
					streamtype : 'live',
					silencelevel : '0',
					server : r5server,
					username : usermail,
					password : usrid2
				};
				flashvarsrcv = {
					stream : userscope,
					width : avawidth,
					height : avaheight,
					server : r5server,
					buffertime : '0',
					username : usermail,
					password : usrid2,
					defaultport : '1935',
					rtmpticon : 'true'
				};
				paramscam = {
					wmode : 'transparent',
					allowScriptAccess : 'sameDomain',
					bgcolor : '#000',
					quality : 'high'
				};
				paramsrcv = {
					wmode : 'transparent',
					allowScriptAccess : 'sameDomain',
					bgcolor : '#000',
					quality : 'high'
				};
				attributesrcv = {
					id : 'avatarimg',
					name : 'avatarimg'
				};

				// FIN Preparación de objetos para red5

				var cssId = 'fionaCss'; // you could encode the css
				// path itself to generate
				// id..
				if (!document.getElementById(cssId)) {
					if (document.createStyleSheet) {
						document.createStyleSheet(hostAddres
								+ 'css/fiona-embed-1.0_ie.css');
						//document
								//.createStyleSheet(protocol + '//ajax.googleapis.com/ajax/libs/jqueryui/1.8/themes/base/jquery-ui.css');
						document.createStyleSheet(hostAddres+'js/jquery/ui-css/jquery-ui.css');


					} else {
						var head = document.getElementsByTagName('head')[0];
						var link = document.createElement('link');
						link.id = cssId;
						link.rel = 'stylesheet';
						link.type = 'text/css';
						link.href = hostAddres + 'css/fiona-embed-1.0.css';
						link.media = 'all';
						head.appendChild(link);

						//TODO:if exists
						var userLink = document.createElement('link');
                       	                        userLink.rel = 'stylesheet';
               	                                userLink.type = 'text/css';
						userLink.href= hostAddres + 'css/' + usrid1 + '.css';
						userLink.media = 'all';
						head.appendChild(userLink);

						var linkCssUi = document.createElement('link');
						linkCssUi.rel = 'stylesheet';
						linkCssUi.type = 'text/css';
						//linkCssUi.href = protocol + '//ajax.googleapis.com/ajax/libs/jqueryui/1.10.2/themes/smoothness/jquery-ui.css';
						linkCssUi.href = hostAddres+'js/jquery/ui-css/jquery-ui.css';
						head.appendChild(linkCssUi);
					}
				}

				if (!(/MSIE (\d+\.\d+);/.test(navigator.userAgent))) {

					loadScript(hostAddres + "js/jquery/jquery.shiftenter_noConflict.js",
							function() {

							});
				}

				loadScript(
						protocol + "//ajax.googleapis.com/ajax/libs/jqueryui/1.8/jquery-ui.min.js",
						//protocol + "//ajax.googleapis.com/ajax/libs/jqueryui/1.6/jquery-ui.min.js",	
						function() {

						});

				loadScript(hostAddres + "js/swfobject.js", function() {

				});

				loadScript(hostAddres + "js/jquery/jquery.frame.animation_noConflict.js",
						function() {

				});

				loadScript(protocol + "//maps.googleapis.com/maps/api/js?sensor=false&callback=initializedMaps",
						function() {

				});
				
				//loadScript(protocol + "//maps.gstatic.com/intl/es_es/mapfiles/api-3/13/11/main.js",
				//		function() {

				//});
				
				loadScript(hostAddres + "js/fiona-maps_noConflict.js", function() {

				});
				
				loadScript(hostAddres + "js/fiona-video_noConflict.js", function() {

				});
				loadScript(hostAddres + "js/jquery/jquery-ui.min.js",function() {
                                });

                                loadScript(hostAddres + "js/swfobject.js", function() {
                                });

				if(typeof mouse != "undefined" && mouse == true) {
					loadScript(hostAddres + "js/jquery.mousewheel.js", function() {
					//console.log("jquery.mousewheel.js loaded!");
	                                });
				};
			});

})();

// document.onkeydown = function endOnRefresh(e) {
//    
// if( typeof e != 'undefined' ) {
// if( (e.keyCode == 116) ) {
// if(started && !dupTabAttempt){
// alert("Yo are about to close or refresh the page!!!");
// endConnection();
// }
// }
// }
// };

// window.onunload = function() {
// if(started && !dupTabAttempt){
// alert("Yo are about to close or refresh the page!!!");
// endConnection();
// }
// };

/*
 * Problema: Si hacemos esto invalidamos desde una pestaña toda la sesión al
 * cerrar, cuando lo que queremos es hacer esto al cerrar el navegador
 */
window.onbeforeunload = function() {
	if (started && !dupTabAttempt) {
		endConnection();
	}
};

if (avatar_size == "big") {
	avawidth = 640;
	avaheight = 480;
} else if (avatar_size == "small") {
	// buscarse la vida pa cambiar estilos
	avawidth = 320;
	avaheight = 240;
}

var id = [ 99999, 99999 ];
// Para controlar los timer
var poll, expTime, timeoutID, textReceivePoll;

function initializedMaps(){
        //console.log("Cargados los mapas de Google");
}

function createRequestObject() {
	var req;
	if (window.XMLHttpRequest) {
		req = new XMLHttpRequest();
	} else if (window.ActiveXObject) {
		// For IE 5+
		req = new ActiveXObject("Microsoft.XMLHTTP");
	} else {
	}
	return req;
}

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

if (avatar_size == "big") {
	var attributescam = {
		id : 'camsenderbig',
		name : 'camsenderbig'
	};
}
if (avatar_size == "small") {
	var attributescam = {
		id : 'camsendersmall',
		name : 'camsendersmall'
	};
}

function startPolling() {
	poll = setInterval("sendPoll()", 5000);
}
function sendPoll() {
	method = "GET";
	url = reqAddres + "?action=sigcheck&user=" + usermail + "&av=" + avatarId;
	http.open(method, url);
	http.withCredentials = true;
	//http.onloadend = handleResponsePoll;
	http.onreadystatechange = handleResponsePoll;
	http.onerror = requestError;
	http.send(null);
}
function handleResponsePoll() {
	if (http.readyState == 4 && http.status == 200) {
		var response = http.responseText;
		if (response) {
			if( response.search("exit") != -1 ) {
                                endConnection();
                                closeDialog();
                        }
		}
	}
}

var httpTextReceive = createCORSRequest();

function startTextReceive() {
	textReceivePoll = setInterval("sendTextReceive()", 500);
}
function sendTextReceive() {
	method = "GET";
	if(typeof mouse != "undefined" && mouse != true) {
		 url = reqAddres + "?action=textreceive?t="+(new Date()).getTime();
	} else {
		url = reqAddres + "?action=textreceive&px="+mousePosX+"&py="+mousePosY+"&df="+deltaFactor+"&t="+(new Date()).getTime();

		// Reseteo el valor de la rueda del ratón

		deltaFactor = 0;
	}
	httpTextReceive.open(method, url);
	httpTextReceive.withCredentials = true;	
	httpTextReceive.onreadystatechange = handleResponseTextReceive;
	httpTextReceive.onerror = requestError;
	httpTextReceive.send(null);
}

function reemplaza(text) {
	text = text.replace(/"/g, "'");
	return text;
}
var convert = function(convert) {
	return jq("<span />", {
		html : convert
	}).text();
};

function handleResponseTextReceive() {
	if (httpTextReceive.readyState == 4 && httpTextReceive.status == 200) {
		var response = httpTextReceive.responseText;
		if (response) {
			if (response == "error calling chat") {

			} else if (response != "&lt;nothing&gt;") { 
				jq('#avatarResponse').empty();
				var text = convert(response);
				//jq('#avatarResponse').append(reemplaza(text));
				jq('#avatarResponse').append(text);
				jq("textarea[name='chatForm:chatboxarea']:first").focus();
				clickAutoButtons();
			}
		}
	}
}

var httpMouseButton = createCORSRequest();

function sendMouseButton(buttonInfo) {
       // console.log("Texto: "+buttonInfo);
        method = "GET";
        url = reqAddres + "?action=mousebutton&info="+buttonInfo;
        httpMouseButton.open(method, url);
        httpMouseButton.withCredentials = true;
        httpMouseButton.onreadystatechange = handleResponseMouseButton;
        httpMouseButton.onerror = requestError;
        httpMouseButton.send(null);
}

function handleResponseMouseButton() {
        if (httpMouseButton.readyState == 4 && httpMouseButton.status == 200) {
                var response = httpMouseButton.responseText;
                if (response) {
                        if (response == "Error calling 'SendMessagesToQueue'") {
				//console.log("Error calling 'SendMessagesToQueue'");
                        } 
                }
        }
}

function textsend() {
	if (id[1] != 0 && id[2] != 0) {
		var text = jq('textarea#chatboxarea').val();
		if (text != '') {
			method = "GET";
			//console.log("Texto antes de escapar: "+text);
			url = reqAddres + "?action=textsend&textosend=" + escape(text) + "&t=" + (new Date()).getTime();
			//console.log("Texto despues de escapar: "+escape(text));
			http.open(method, url);
			http.withCredentials = true;
			//http.onloadend = handleResponseChatSend;
			http.onreadystatechange = handleResponseChatSend;
			http.onerror = requestError;
			http.send(null);
		} else {
			jq("textarea[name='chatForm:chatboxarea']:first").focus();
		}
	} else {
		alert("no hay proceso corriendo");
	}
	jq('textarea#chatboxarea').val("");
}
function handleResponseChatSend() {
	if (http.readyState == 4 && http.status == 200) {
		var response = http.responseText;
		if (response) {
			if (response == "error calling chat") {

			} else {
				jq('#avatarResponse').empty();
				jq('#avatarResponse').append(response);
				jq("textarea[name='chatForm:chatboxarea']:first").focus();
			}
		}
	}
}

function checkChat() {
	method = "GET";
	url = reqAddres + "?action=checkchat&user=" + usermail + "&av=" + avatarId;
	http.open(method, url);
	http.withCredentials = true;
	//http.onloadend = handleResponseChatCheck;
	http.onreadystatechange = handleResponseChatCheck;
	http.onerror = requestError;
	http.send(null);

}
function handleResponseChatCheck() {
	if (http.readyState == 4 && http.status == 200) {
		var response = http.responseText;
		if (response) {
			if (response == "true") {
				jq('#fiona').append(
						'<div id="chatbox" class="fiona_chatbox"> </div>'); // sólo
				// si
				// tiene
				// el
				// chat
				// en
				// el
				// xml...
				jq('#chatbox')
						.append(
								'<form id="chatForm"><textarea id="chatboxarea" name="chatForm:chatboxarea" ></textarea></form>');
				jq('#chatForm')
						.append(
								'<input type="button" id="fiona_send_button" name="chatForm:send" style="' + buttonStyle + '" value="'+ buttonText + '" onclick="textsend();"/>');
				if ((/MSIE (\d+\.\d+);/.test(navigator.userAgent))) {
					jq("#chatboxarea").keyup(function(event) {
						if (event.keyCode == 13) {
							jq("#fiona_send_button").click();
						}
					});
				}
				if (avatar_size == "big") {
					jq('#fiona').append(
							'<div id="avatarResponse" class="big"> </div>');
					if (show_pop_up == true) {
						jq('#avatarResponse').addClass('pop_up');
					}

					if (allow_camera == false) {
						jq("#avatarResponse").css("width", "633px");
						//jq("#chatboxarea").css("width", "90%");
						jq("#chatboxarea").css("width", "84%");
						jq('#fiona_send_button').addClass("no_camera");
					} else {
						jq(".fiona_chatbox").css("width", "390px");
						jq('#avatarResponse').addClass('allow_camera');
					}
				} else {
					jq('#fiona').append(
							'<div id="avatarResponse" class="small"> </div>');

					jq('#chatboxarea').addClass("small");
					if (show_pop_up == true) {
						jq('#avatarResponse').addClass('pop_up');
					}
					if (allow_camera == false) {
						jq("#avatarResponse").css("width", "311px");
						jq('#fiona_send_button').addClass("no_camera");
					} else {
						jq('#avatarResponse').addClass('allow_camera');
					}
				}
				setFocusOnChatTA();
				startTextReceive();
			}
		}
	}
}

function getId() {
	method = "GET";
	url = reqAddres + "?action=getid&user=" + usrid1;
	http.open(method, url);
	http.withCredentials = true;
	//http.onloadend = handleResponseGetId;
	http.onreadystatechange = handleResponseGetId;
	http.onerror = requestError;
	http.send(null);

}
function handleResponseGetId() {
	if (http.readyState == 4 && http.status == 200) {
		var response = http.responseText;
		if (response) {
			if (response != null) {
				id = response.split(" ");
				//expTime = setInterval("expiration()", 180000);
				expTime = setInterval("expiration()", 300000);
				checkChat();
				startPolling();
			} else {
				getId();
			}
		}
	}
}

function initConnection() {
	alternative = false;
	method = "GET";
	url = reqAddres + "?action=init&user=" + usermail + "&room=" + randomRoom + "&av=" + avatarId;
	http.open(method, url);
	http.withCredentials = true;
	//http.onloadend = handleResponseInit;
	http.onreadystatechange = handleResponseInit;
	http.onerror = requestError;
	http.send(null);

}

function handleResponseInit() {
	if (http.readyState == 4 && http.status == 200) {
		var response = http.responseText;
		if (response) {
			if (response != 'error' && response.split(" ")[0] != 'DISABLED') {
				if (response.split(" ")[1] != 99991) {
					if (response == 'session_exists') {
						sessionNew = false;
						dupTabAttempt = true;
						expiration();
						return;
					}
					if (response != "true" && response != "false") {
						var timeExpiration = 300000;
						if(response.split(" ")[2] != 'null' && response.split(" ")[2] != '')
							timeExpiration = response.split(" ")[2] * 1000;
						//expTime = setInterval("expiration()", 180000);
						expTime = setInterval("expiration()", timeExpiration);
						checkChat();
						startPolling();
					}
				} else if (response.split(" ")[1] == 99991) {
					avatarinuse();
				} else {
					getId();
				}
			} else {
				alert("This avatar is disabled right now. Sorry.");
				showDisabledMessage();
			}
		}
	}
}

function endConnection() {
	method = "GET";
	url = reqAddres + "?action=endConnection";
	if(! (typeof resource === 'undefined')) {
        	url = url + "&resource=" + resource;
        }
	http.open(method, url);
	http.withCredentials = true;
	//http.onloadend = handleResponseEnd;
	http.onreadystatechange = handleResponseEnd;
	http.onerror = requestError;
	http.send(null);

}

function handleResponseEnd() {
	if (http.readyState == 4 && http.status == 200) {
		var response = http.responseText;
		if (response) {
			if (response == 'session_invalidated') {
				sessionNew = true;
				dupTabAttempt = false;
				started = false;
			}
		}
	}
}

var httpTron = createCORSRequest();
function checkTron() {
        method = "GET";
        url = reqAddres + "?action=checkTron&user=" + usermail + "&av=" + avatarId + "&voice=" + voice + "&stream=" + r5server + randomRoom;
        httpTron.open(method, url,false);
        //httpTron.withCredentials = true;
        httpTron.onloadend = handleResponseTron;
        httpTron.onerror = requestError;
        httpTron.send(null);

}

function handleResponseTron() {
        if (httpTron.readyState == 4 && httpTron.status == 200) {
                var response = httpTron.responseText;
                /*if (response) {
                        if (response == '0') {
                                troninuse();
                                TronOK = false;
                                timeoutIDtron = setTimeout("initFiona(this,usermail,usrid2,usrid1,avatarId)", 5000);
                        }
                        else if(response == 'no_entity') {
                                // TODO: mostrar hay un error. no reintentar
                                troninuse();
                                TronOK = false;
                        }
                        else {
                                resource = response;
                                TronOK = true;
                        }
                }*/
                resource = response;
                TronOK = true; // avatar always starts, no matter Tron state
        }
}

function requestError() {
	if(!alternative)
		alert("Woops, there was an error making the request. Try again later, please");
	// Limpiar cookies del navegador para volver a intentar llamada al servidor
	setCookieExpired("JSESSIONID", "", "/FionaHandler", null, null);
}

function avatarinuse() {
	// chapar flashplayers y calcar foto
	swfobject.removeSWF('usercam');
	swfobject.removeSWF('avatarstream');
	jq('#players').remove();
	jq('#chatbox').remove();
	jq('#botones').remove();
	jq('#fiona').append(
			"<img src='" + hostAddres + 'FionaHandler/scriptletuserimages/' + usrid1
					+ '/ScriptletImages/' + "busy_icon.png'></img>");	
	// parar timer
	clearInterval(expTime);
	clearInterval(poll);
	clearInterval(textReceivePoll);
	if (show_pop_up == false) {
		// Esperamos 10 segundos y reseteamos el div fiona principal
		timeoutID = setTimeout("resetFionaDiv()", 10000);
	}
}

function expiration() {
	swfobject.removeSWF('usercam');
	swfobject.removeSWF('avatarstream');
	jq('#avatarResponse').remove();
	jq('#players').remove();
	jq('#chatbox').remove();
	jq('#botones').remove();
	jq('#fiona').removeClass('fiona_main');
	jq('#fiona').addClass('fiona_main_end');
	if (show_pop_up == true) {
		jq('#fiona').dialog("option", "minWidth", 685);
		jq('#fiona').dialog("option", "maxWidth", 685);
		jq('#fiona').dialog("option", "width", 685);
		jq('#fiona').dialog("option", "position", "center");
	}

	if (!dupTabAttempt)
		jq('#fiona').append(
				'<img id="flameBoxImage" src="' + hostAddres
						+ 'FionaHandler/scriptletuserimages/' + usrid1 + '/ScriptletImages/'
						+ 'the-end.png" alt="Powered by FIONA"/>');
	else
		jq('#fiona').append(
				'<img id="flameBoxImage" src="' + hostAddres
						+ 'FionaHandler/scriptletuserimages/' + usrid1 + '/ScriptletImages/'
						+ 'omnipresente.png" alt="Powered by FIONA"/>');	
	// parar timer
	clearInterval(expTime);
	clearInterval(poll);
	clearInterval(textReceivePoll);
	if (show_pop_up == false) {
		// Esperamos 10 segundos y reseteamos el div fiona principal
		timeoutID = setTimeout("resetFionaDiv()", 10000);
	}
	if (started && !dupTabAttempt) {
		endConnection();
	} else {
		// resetFionaDiv();
		started = false;
		sessionNew = true;
		dupTabAttempt = false;
	}
	// Cerrar los dialog de los mapas
	closeMapDialog();
	// Cerrar los dialog de los videos
	closeVideoDialog();
}

function closeDialog() {
	expiration();
	// if(started && !dupTabAttempt)
	resetFionaDiv();
	// Cerrar los dialog de los mapas
	closeMapDialog();
	// Cerrar los dialog de los videos
	closeVideoDialog();
}

function resetFionaDiv() {
	if (show_pop_up == true) {
		// Eliminamos la funcionalidad del dialog
		jq('#fiona').dialog("destroy");
		// Eliminamos el div actual
		jq('#fiona').remove();
		// Concatenamos al padre inicial
		// el contenido del div principal antes
		// de iniciar el avatar
		jq(divInicialParent).append(fionaDivInicial);
	} else {
		// Reemplazamos el contenido actual del div
		// con el que tenía antes de iniciar el
		// avatar
		jq('#fiona').replaceWith(fionaDivInicial);
	}
	// Habilitamos el elemento desde el que se llamó
	// el inicio del avatar
	if (onclickValue != "undefined") {
		jq("#" + idCallingElement).attr("onclick", onclickValue);
	} else {
		jq("#" + idCallingElement).attr("onclick",
				"initFiona(this,usermail,usrid2,usrid1,avatarid,barimg)");
	}
	// Restauramos el div principal a su estado inicial
	if (timeoutID != null)
		clearTimeout(timeoutID);
}

function initFiona(element, mail, id2, id1,ida,idc) {

	if (element != null) {
		started = true;
		usermail = mail;
		usrid2 = id2;
		usrid1 = id1;
		avatarId = ida || "";
		idc = barimg || "img/black_bar_thick.png";
		randomRoom = randomString(32);
		flashvarscam.server = r5server + randomRoom;
		flashvarsrcv.server = r5server + randomRoom;
		flashvarscam.username = usermail;
		flashvarscam.password = usrid2;
		flashvarsrcv.username = usermail;
		flashvarsrcv.password = usrid2;

                if((! (typeof tron === 'undefined')) && (tron)) {
                        checkTron();
                }
                if((! (typeof TronOK === 'undefined')) && ( !TronOK )) {
                        return;
                }

		fionaDivInicial = jq("#fiona").clone();
		divInicialParent = jq("#fiona").parent();

		initConnection();

		if (sessionNew == true) {
			// Desabilitamos el elemento desde el que se llamó al método
			// para evitar que vuelva a llamarse
			idCallingElement = element.id;
			onclickValue = jq("#" + idCallingElement).attr("onclick");
			jq("#" + idCallingElement).removeAttr("onclick");
			jq('#fiona').append(
					'<div id="players" class="fiona_players"> </div>');
			jq('#fiona').append(
					'<div id="botones" class="fiona_buttons"> </div>');

			if (avatar_size == "big") {
				jq("#players").addClass("big");
				jq('#players').append(
						'<div id="avatarplayer" class="avatarbig"> </div>');
			} else if (avatar_size == "small") {
				jq("#players").addClass("small");
				jq('#players').append(
						'<div id="avatarplayer" class="avatarsmall"> </div>');
			}

			jq('#players').append('<div id="usercam"> </div>');

			if (show_pop_up == true) {

				if (avatar_size == "big") {
					// Diálogo que al cerrar, llame a expiration
					var dialogOptions = {
						/*
						 * maxHeight: 650, minHeight: 650,
						 */
						maxWidth : 690,
						minWidth : 690,
						position : 'center',
						resizable : false,
						show : {
							effect : 'drop',
							direction : 'up'
						},
						close : closeDialog
					};
				} else {
					// Diálogo que al cerrar, llame a expiration
					var dialogOptions = {
						maxHeight : 600,
						minHeight : 600,
						maxWidth : 690,
						minWidth : 690,
						position : 'center',
						resizable : false,
						show : {
							effect : 'drop',
							direction : 'up'
						},
						close : closeDialog
					};
				}
				jq("#fiona").dialog(dialogOptions);
				jq(".ui-widget-header")
						.css(
								"background",
								"url('"
										+ hostAddres
										+ idc + "') repeat-x scroll 50% 50% #CCCCCC");
			}else{
				// show_pop_up = false
				jq("#fiona").removeAttr("style");
			}

			initFlame();
			var t = setInterval(
					function() {
						if (typeof swfobject != "undefined") {
							if (avatar_size == "big") {
								swfobject.embedSWF(hostAddres
										+ 'swf/receivevideo.swf',
										'avatarplayer', '640', '500', '9.0.0',
										false, flashvarsrcv, paramsrcv,
										attributesrcv,
										avatarstream_onLoadCallkback);
								if (allow_camera == true) {
									swfobject.embedSWF(hostAddres
											+ 'swf/vidpublish.swf', 'usercam',
											'220', '160', '9.0.0', false,
											flashvarscam, paramscam,
											attributescam);
									if (show_pop_up == true) {
										jq('#camsenderbig').addClass(
												'pop_up');
									}
								}
							} else if (avatar_size == "small") {
								swfobject.embedSWF(hostAddres
										+ 'swf/receivevideo.swf',
										'avatarplayer', '330', '250', '9.0.0',
										false, flashvarsrcv, paramsrcv,
										attributesrcv,
										avatarstream_onLoadCallkback);
								if (allow_camera == true)
									swfobject.embedSWF(hostAddres
											+ 'swf/vidpublish.swf', 'usercam',
											'220', '160', '9.0.0', false,
											flashvarscam, paramscam,
											attributescam);
							}

							clearInterval(t);
							t = null;
							// Resize
							if (avatar_size == "small") {
								jq(".fiona_main").css("width", "320px");

								if (show_pop_up == true) {
									jq(".fiona_main")
											.css("height", "402px");

									jq('#fiona').dialog("option", "height",
											402);
									jq('#fiona').dialog("option",
											"maxHeight", 402);
									jq('#fiona').dialog("option",
											"minHeight", 402);
									if (allow_camera == true) {
										jq('#fiona').dialog("option",
												"minWidth", 521);
										jq('#fiona').dialog("option",
												"width", 521);
									} else {
										jq('#fiona').dialog("option",
												"minWidth", 358);
										jq('#fiona').dialog("option",
												"width", 358);

									}

									jq('#fiona').css("height", "402px");
								}
							}
							if (show_pop_up == true)
								jq("#fiona").dialog("option", "position",
										"center");
						}
					}, 5500);
		} else {
		}
	}

if (typeof mouse != "undefined" && mouse == true){
	$("#fiona #players").mousemove(function(evt) {
	     //           var parentOffset = $(this).parent().offset();
	     //           var relX = e.pageX - parentOffset.left;
	     //           var relY = e.pageY - parentOffset.top;
	     //           console.log("Pos abs| x: "+e.pageX+" , y: "+e.pageY);
	     //           console.log("Pos abs (client)| x: "+e.clientX+" , y: "+e.clientY);
	     //           console.log("Pos rel| x: "+relX+" , y: "+relY);
	
	//Mucho más preciso lo siguiente
	//var element = document.getElementById('players');  //replace elementId with your element's Id.
	//	var element = this;
	//    var rect = element.getBoundingClientRect();
	    var rect = this.getBoundingClientRect();
	    var scrollTop = document.documentElement.scrollTop?
	                    document.documentElement.scrollTop:document.body.scrollTop;
	    var scrollLeft = document.documentElement.scrollLeft?                   
	                    document.documentElement.scrollLeft:document.body.scrollLeft;
	    var elementLeft = rect.left+scrollLeft;  
	    var elementTop = rect.top+scrollTop;
	
	        if (document.all){ //detects using IE   
	            mousePosX = event.clientX+scrollLeft-elementLeft; //event not evt because of IE
	            mousePosY = event.clientY+scrollTop-elementTop;
	        }
	        else{
	            mousePosX = evt.pageX-elementLeft;
	            mousePosY = evt.pageY-elementTop;
		    //evt.preventDefault();
		    //console.log('WHICH BUTTON: ' + evt.which );
	    }
	        });
	
		$("#fiona #players").mouseleave(function(event) {
	        	mousePosX = 640/2;
	        	mousePosY = 480/2;
			//console.log("Mouse leaves player..");
	    	});
	//	$("#fiona #players").on("click", function (event) {
	//		console.log('WHICH BUTTON: ' + event.which );	 
	//	});
		$("#fiona #players").on("mousedown", function(e) {   
			if( e.which <= 2 ) {
	        		e.preventDefault();
	      			//console.log("DOWN | Button clicked: " + e.which);
				sendMouseButton("BUTTON DOWN "+e.which);
			}
		});
		$("#fiona #players").on("mouseup", function(e) {
	                if( e.which <= 2 ) {
	                        e.preventDefault();
	                        //console.log("UP | Button clicked: " + e.which);
				sendMouseButton("BUTTON UP "+e.which);
	                }
	        });
		$("#fiona #players").mousewheel(function(event, delta, deltaX, deltaY) {
			var o;
			if (delta > 0){
	                	o = ' up (' + delta + ')';
				deltaFactor+=event.deltaFactor;
			}
	                else if (delta < 0){
	                        o = ' down (' + delta + ')';
				deltaFactor-=event.deltaFactor;
			}
	
	                if (event.deltaY > 0)
	                        o += ' north (' + event.deltaY + ')';
	                else if (event.deltaY < 0)
		                o += ' south (' + event.deltaY + ')';
	
	                if (event.deltaX > 0)
	                        o += ' east (' + event.deltaX + ')';
	                else if (event.deltaX < 0)
	                        o += ' west (' + event.deltaX + ')';
	
	                o += ' deltaFactor (' + event.deltaFactor + ')';
			//console.log(o);
	
	                return false; // prevent default
		});
	}
}

function setFocusOnChatTA() {
	jq("textarea[name='chatForm:chatboxarea']:first").focus();
	if (!(/MSIE (\d+\.\d+);/.test(navigator.userAgent))) {
		jq("textarea[name='chatForm:chatboxarea']").shiftenter({
			hint : '',
			metaKey : 'shift',
			submitCallback : function($el) {
				var submitButton = jq("input[name='chatForm:send']");
				submitButton.click();
			}
		});
	}
}

function showAvatarFlameBox(numRepeat, msdelay) {
	jq("#flameBoxAnim").frameAnimation({
		hoverMode : false,
		repeat : numRepeat,
		delay : msdelay
	});
}

function avatarstream_onLoadCallkback(event) {
	if (event.success) {
	jq('#flameBoxAnim').hide();
	jq('#flameBox').hide();
	jq('#flameBoxWrap').hide();
		showAvatarFlameBox(-1, 80);
	} else {
		showAlternativeContent();
	}
}

function initFlame() {

	var flamebox = jq('<div id="flameBoxWrap" class="bgflame">'
			+ '<div id="flameBox" class="bgflame-inner">'
			+ '<img id="flameBoxImage" src="'
			+ hostAddres
			+ 'img/flame.png" alt="Powered by FIONA"/>'
			+ '<a id="flameBoxAnim" href="#" style="display:none;background:url('
			+ hostAddres + 'img/flame2.png) no-repeat 0px -5700px;"></a>'
			+ '</div>' + '</div>');
	jq('#players').append(flamebox);
	if (avatar_size == "small")
		jq('#flameBoxWrap').addClass("small");
	jq('#flameBoxWrap').show();
	jq("#flameBoxImage").hide();
	// Mostrar enlace para animacion
	jq('#flameBoxAnim').show();
	// Iniciar animacion
	showAvatarFlameBox(-1, 100);
	if (show_pop_up == true)
		jq("#fiona").dialog("option", "position","center");
}

function showDisabledMessage() {
	// Borramos los elementos necesarios
	// para mostrar el avatar
	swfobject.removeSWF('usercam');
	swfobject.removeSWF('avatarstream');
	jq('#players').remove();
	jq('#botones').remove();
	// Mostramos mensaje al usuario indicando que
	// el avatar está desactivado
	var html = "<p>This avatar is disabled right now. We are sorry.</p>"
	jq('#fiona').append(html);
}

function randomString(length) {
	var result = '';
	var chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
	for ( var i = length; i > 0; --i)
		result += chars[Math.round(Math.random() * (chars.length - 1))];
	return result;
}

function setCookieExpired(name, value, path, domain, secure) {
	// set time, it's in milliseconds
	var cookie_date = new Date();

	/*
	 * if the expires variable is set, make the correct expires time, the
	 * current script below will set it for x number of days, to make it for
	 * hours, delete * 24, for minutes, delete * 60 * 24
	 */

	var expires_date = new Date(cookie_date.getTime() - 1);

	document.cookie = name + "=" + escape(value)
			+ (";expires=" + expires_date.toGMTString())
			+ ((path) ? ";path=" + path : "")
			+ ((domain) ? ";domain=" + domain : "")
			+ ((secure) ? ";secure" : "");
}

function browserUnsupported() {
	var unsupport = false;
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) {
		// if (/Firefox[\/\s](\d+\.\d+)/.test(navigator.userAgent)) {
		var dialogOptions = {
			/*
			 * maxHeight: 650, minHeight: 650,
			 */
			maxWidth : 690,
			minWidth : 690,
			position : 'center',
			resizable : false,
			close : closeDialog
		};
		jq("#fiona").dialog(dialogOptions);
		jq(".ui-widget-header")
				.css(
						"background",
						"url('"
								+ hostAddres
								+ idc + "') repeat-x scroll 50% 50% #CCCCCC");
		// chapar flashplayers y calcar foto
		swfobject.removeSWF('usercam');
		swfobject.removeSWF('avatarstream');
		jq('#players').remove();
		jq('#chatbox').remove();
		jq('#botones').remove();
		jq('#fiona').append(
				"<img src='" + hostAddres + 'FionaHandler/scriptletuserimages/' + usrid1
						+ '/ScriptletImages/' + "ie-virus.png'></img>");
		// parar timer
		clearInterval(expTime);
		clearInterval(poll);
		clearInterval(textReceivePoll);
		if (show_pop_up == false) {
			// Esperamos 10 segundos y reseteamos el div fiona principal
			timeoutID = setTimeout("resetFionaDiv()", 10000);
		}
		unsupport = true;
	}
	return unsupport;
}

function showAlternativeContent(){
        alternative = true;
        swfobject.removeSWF('usercam');
        swfobject.removeSWF('avatarstream');
        jq('#avatarResponse').remove();
        jq('#players').remove();
        jq('#chatbox').remove();
        jq('#botones').remove();
        clearInterval(expTime);
        clearInterval(poll);
	clearInterval(textReceivePoll);
	// Se comprueba el idioma
        if(lang.indexOf("es") != -1){
	        jq('#fiona').append('<p>Necesita Adobe Flash Player para hacer uso del servicio.</p><p><a href="' + protocol + '//www.adobe.com/go/getflashplayer"><img src="' + protocol + '//www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>');
	}else{
		jq('#fiona').append('<p>Adobe Flash Player is required.</p><p><a href="' + protocol + '//www.adobe.com/go/getflashplayer"><img src="' + protocol + '//www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>');
	}
}

function showInfoRTMPT(){
	// Se vacía el contenido, por si se vuelve a mostrar sin haber sido cerrado previamente
	jq(" #rtmpt_dialog ").empty();
	// Se comprueba el idioma
	if(lang.indexOf("es") != -1){
		jq( "#rtmpt_dialog" ).attr( 'title','Conexi\u00f3n limitada');
		jq( "#rtmpt_dialog" ).append("<p>Si est\u00e1s viendo un peque\u00f1o icono sobre la imagen, es porque est\u00e1s navegando a trav\u00e9s de un firewall/proxy. En este caso se experimentar\u00e1 un peor comportamiento del asistente virtual. Contacte con su administrador de sistemas para poder disfrutar del servicio  a pleno rendimiento.</p>");
	}else{
		jq( "#rtmpt_dialog" ).attr( 'title','Connection behind firewall/proxy');
		jq( "#rtmpt_dialog" ).append("<p>If you are watching an overlay icon is because we are streaming the avatar using the RTMPT protocol, which is significantly slower and more error-prone. This situation often occurs when you are behind a firewall/proxy. Contact with your IT system administrator for a better perfomance.</p>");
	}
	jq( "#rtmpt_dialog" ).dialog();
	jq( "#rtmpt_dialog" ).dialog("option", "close", closeInfoRTMPT);
	// Para que quede siempre por encima
	jq( "#rtmpt_dialog" ).dialog( "moveToTop" );	
}

function closeInfoRTMPT(){
	jq( "#rtmpt_dialog" ).empty();
}

function fromFlash(msg){
        //console.log("From receivevideo2.swf -> "+msg);
}

function crearFormPaypal(params) {
	var firstName = jq("#spanFirstName").html();
	var lastName = jq("#spanLastName").html();
	var address1 = jq("#spanAddress1").html();
	var address2 = jq("#spanAddress2").html();
	var city = jq("#spanCity").html();
	var state = jq("#spanState").html();
	var zip = jq("#spanZip").html();
	var lc = jq("#spanLc").html();
	var email = jq("#spanEmail").html();
	var nightPhoneA = jq("#spanNightPhoneA").html();
	var nightPhoneB = jq("#spanNightPhoneB").html();
	var nightPhoneC = jq("#spanNightPhoneC").html();

	var html = "<div style='display:none;'><form id='formPayPal' action='https://www.sandbox.paypal.com/cgi-bin/webscr' method='post' target='blank'>";
	html = html + "<input type='hidden' name='cmd' value='_s-xclick'>";
	html = html
			+ "<input type='hidden' name='hosted_button_id' value='UMANWWTN8FDF6'>";

	if (firstName != null && firstName != "undefined")
		html = html + "<input type='hidden' name='first_name' value='"
				+ firstName + "'>";
	if (lastName != null && lastName != "undefined")
		html = html + "<input type='hidden' name='last_name' value='"
				+ lastName + "'>";
	if (address1 != null && address1 != "undefined")
		html = html + "<input type='hidden' name='address1' value='" + address1
				+ "'>";
	if (address2 != null && address2 != "undefined")
		html = html + "<input type='hidden' name='address2' value='" + address2
				+ "'>";
	if (city != null && city != "undefined")
		html = html + "<input type='hidden' name='city' value='" + city + "'>";
	if (state != null && state != "undefined")
		html = html + "<input type='hidden' name='state' value='" + state
				+ "'>";
	if (zip != null && zip != "undefined")
		html = html + "<input type='hidden' name='zip' value='" + zip + "'>";
	if (lc != null && lc != "undefined")
		html = html + "<input type='hidden' name='lc' value='" + lc + "'>";
	if (email != null && email != "undefined")
		html = html + "<input type='hidden' name='email' value='" + email
				+ "'>";
	if (nightPhoneA != null && nightPhoneA != "undefined")
		html = html + "<input type='hidden' name='night_phone_a' value='"
				+ nightPhoneA + "'>";
	if (nightPhoneB != null && nightPhoneB != "undefined")
		html = html + "<input type='hidden' name='night_phone_b' value='"
				+ nightPhoneB + "'>";
	if (nightPhoneC != null && nightPhoneC != "undefined")
		html = html + "<input type='hidden' name='night_phone_c' value='"
				+ nightPhoneC + "'>";

	html = html
			+ "<input id='imgPayPal' type='image' src='http://www.theclientrelationfactory.com/wp-content/uploads/2013/06/buy_now_btn1.png' border='0' name='submit' alt='PayPal - The safer, easier way to pay online!'>";
	html = html
			+ "<img alt='' border='0' src='https://www.sandbox.paypal.com/es_XC/i/scr/pixel.gif' width='1' height='1'>";
	html = html + "</form></div>";

	// jq('#avatarResponse').empty();
	jq("#avatarResponse").append(html);
	jq("#imgPayPal").trigger('click');

}

function clickAutoButtons() {
	jq(".autoClickable").trigger('click');
}

function repregunta(value){
	jq("textarea#chatboxarea").val(value);
	var submitButton = jq("#fiona_send_button");
	submitButton.trigger('click');
}

function crearFormTCRF(params) {        
        var email = jq("#spanEmail").html();
        var period = jq("#spanPeriod").html();
        

        var html = "<div style='display:none;'><form id='formPayPal' action='http://fiona.ticserver.com/mySite/Paypal/SetExpressCheckout.php' method='post' target='blank'>";        
       
        if (email != null && email != "undefined")
                html = html + "<input type='hidden' name='email' value='" + email
                                + "'>";
        if (period != null && period != "undefined")
                html = html + "<input type='hidden' name='period' value='"
                                + period + "'>";
        

        html = html
                        + "<input id='imgPayPal' type='submit' src='http://www.theclientrelationfactory.com/wp-content/uploads/2013/06/buy_now_btn1.png' border='0'  alt='PayPal - The safer, easier way to pay online!'>";
        
        html = html + "</form></div>";
        
        jq("#avatarResponse").append(html);
        jq("#imgPayPal").trigger('click');

}


