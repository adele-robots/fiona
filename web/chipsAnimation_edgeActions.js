/***********************
* Adobe Edge Composition Actions
*
* Edit this file with caution, being careful to preserve 
* function signatures and comments starting with 'Edge' to maintain the 
* ability to interact with these actions from within Adobe Edge
*
***********************/
(function($, Edge, compId){
var Composition = Edge.Composition, Symbol = Edge.Symbol; // aliases for commonly used Edge classes

//Edge symbol: 'stage'
(function(symbolName) {

Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 440, function(sym, e) {
// Show an Element.
//  (sym.$("name") resolves an Edge element name to a DOM
//  element that can be used with jQuery)
sym.$("labels2").show();
// insert code here
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "document", "compositionReady", function(sym, e) {
// insert code for compositionReady event here
last_clicked=""
});
//Edge binding end

Symbol.bindTimelineAction(compId, symbolName, "Default Timeline", "play", function(sym, e) {
// insert code to be run at timeline play here
});
//Edge binding end
























})("stage");
//Edge symbol end:'stage'

//=========================================================

//Edge symbol: 'Entrance'
(function(symbolName) {
Symbol.bindElementAction(compId, symbolName, "${_enlarge_icon}", "click", function(sym, e) {
// insert code for mouse clicks here
if(last_clicked!=undefined && last_clicked!=null && last_clicked!='')
	sym.getSymbol(last_clicked).playReverse();
last_clicked="enlarge_icon";
sym.getSymbol(last_clicked).play();
});
//Edge binding end


Symbol.bindElementAction(compId, symbolName, "${_enlarge_building}", "click", function(sym, e) {
// insert code for mouse clicks here
if(last_clicked!=undefined && last_clicked!=null && last_clicked!='')
	sym.getSymbol(last_clicked).playReverse();
last_clicked="enlarge_building";
sym.getSymbol(last_clicked).play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_enlarge_books}", "click", function(sym, e) {
// insert code for mouse clicks here
if(last_clicked!=undefined && last_clicked!=null && last_clicked!='')
	sym.getSymbol(last_clicked).playReverse();
last_clicked="enlarge_books";
sym.getSymbol(last_clicked).play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_enlarge_trolley}", "click", function(sym, e) {
// insert code for mouse clicks here
if(last_clicked!=undefined && last_clicked!=null && last_clicked!='')
	sym.getSymbol(last_clicked).playReverse();
last_clicked="enlarge_trolley";
sym.getSymbol(last_clicked).play();// insert code for mouse clicks here
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_enlarge_piggybank}", "click", function(sym, e) {
// insert code for mouse clicks here
if(last_clicked!=undefined && last_clicked!=null && last_clicked!='')
	sym.getSymbol(last_clicked).playReverse();
last_clicked="enlarge_piggybank";
sym.getSymbol(last_clicked).play();

});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_enlarge_rocket}", "click", function(sym, e) {
// insert code for mouse clicks here
if(last_clicked!=undefined && last_clicked!=null && last_clicked!='')
	sym.getSymbol(last_clicked).playReverse();
last_clicked="enlarge_rocket";
sym.getSymbol(last_clicked).play();

});
//Edge binding end






Symbol.bindElementAction(compId, symbolName, "${_littleworld}", "click", function(sym, e) {
// insert code for mouse clicks here
if(last_clicked!=undefined && last_clicked!=null && last_clicked!='')
	sym.getSymbol(last_clicked).playReverse();
last_clicked="";
$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").playReverse();
littleworldselection();
//$.Edge.getComposition("EDGE-491969267").getStage().play();
});
//Edge binding end

Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 1500, function(sym, e) {

$('div#chipsAnimation_Entrance_enlarge_icon_texto_titulo').html(document.getElementById("TituloIcon").innerHTML);
$('div#chipsAnimation_Entrance_enlarge_icon_texto_subtitulo').html(document.getElementById("ContenidoIcon").innerHTML.split("|")[0]);
$('div#chipsAnimation_Entrance_enlarge_icon_texto_contenido').html(document.getElementById("ContenidoIcon").innerHTML.split("|")[1]);

$('div#chipsAnimation_Entrance_enlarge_building_texto_titulo').html(document.getElementById("TituloBuilding").innerHTML);
$('div#chipsAnimation_Entrance_enlarge_building_texto_subtitulo').html(document.getElementById("ContenidoBuilding").innerHTML.split("|")[0]);
$('div#chipsAnimation_Entrance_enlarge_building_texto_contenido').html(document.getElementById("ContenidoBuilding").innerHTML.split("|")[1]);

$('div#chipsAnimation_Entrance_enlarge_books_texto_titulo').html(document.getElementById("TituloBooks").innerHTML);
$('div#chipsAnimation_Entrance_enlarge_books_texto_subtitulo').html(document.getElementById("ContenidoBooks").innerHTML.split("|")[0]);
$('div#chipsAnimation_Entrance_enlarge_books_texto_contenido').html(document.getElementById("ContenidoBooks").innerHTML.split("|")[1]);

$('div#chipsAnimation_Entrance_enlarge_trolley_texto_titulo').html(document.getElementById("TituloTrolley").innerHTML);
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_subtitulo').html(document.getElementById("ContenidoTrolley").innerHTML.split("|")[0]);
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_contenido').html(document.getElementById("ContenidoTrolley").innerHTML.split("|")[1]);

$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_titulo').html(document.getElementById("TituloPiggy").innerHTML);
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_subtitulo').html(document.getElementById("ContenidoPiggy").innerHTML.split("|")[0]);
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_contenido').html(document.getElementById("ContenidoPiggy").innerHTML.split("|")[1]);

$('div#chipsAnimation_Entrance_enlarge_rocket_texto_titulo').html(document.getElementById("TituloRocket").innerHTML);
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_subtitulo').html(document.getElementById("ContenidoRocket").innerHTML.split("|")[0]);
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_contenido').html(document.getElementById("ContenidoRocket").innerHTML.split("|")[1]);





//Estilos titulos y contenido
$('div#chipsAnimation_Entrance_enlarge_icon_texto_titulo').css({'color': '#4d4d4d'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_titulo').css({'font-size': '48px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_titulo').css({'font-weight': '600'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_titulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_titulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_titulo').css({'top': '61px'});


$('div#chipsAnimation_Entrance_enlarge_building_texto_titulo').css({'color': '#4d4d4d'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_titulo').css({'font-size': '48px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_titulo').css({'font-weight': '600'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_titulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_titulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_titulo').css({'top': '61px'});

$('div#chipsAnimation_Entrance_enlarge_books_texto_titulo').css({'color': '#4d4d4d'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_titulo').css({'font-size': '48px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_titulo').css({'font-weight': '600'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_titulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_titulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_titulo').css({'line-height': '50px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_titulo').css({'top': '70px'});

$('div#chipsAnimation_Entrance_enlarge_trolley_texto_titulo').css({'color': '#4d4d4d'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_titulo').css({'font-size': '48px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_titulo').css({'font-weight': '600'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_titulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_titulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_titulo').css({'line-height': '50px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_titulo').css({'top': '70px'});

$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_titulo').css({'color': '#4d4d4d'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_titulo').css({'font-size': '48px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_titulo').css({'font-weight': '600'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_titulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_titulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_titulo').css({'top': '61px'});

$('div#chipsAnimation_Entrance_enlarge_rocket_texto_titulo').css({'color': '#4d4d4d'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_titulo').css({'font-size': '48px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_titulo').css({'font-weight': '600'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_titulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_titulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_titulo').css({'line-height': '50px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_titulo').css({'top': '70px'});


$('div#chipsAnimation_Entrance_enlarge_icon_texto_subtitulo').css({'color': '#E30066'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_subtitulo').css({'font-size': '16px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_subtitulo').css({'line-height': '29px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_subtitulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_subtitulo').css({'top': '135px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_subtitulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_subtitulo').css({'font-weight': '600'});


$('div#chipsAnimation_Entrance_enlarge_building_texto_subtitulo').css({'color': '#E30066'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_subtitulo').css({'font-size': '16px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_subtitulo').css({'line-height': '29px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_subtitulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_subtitulo').css({'top': '135px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_subtitulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_subtitulo').css({'font-weight': '600'});

$('div#chipsAnimation_Entrance_enlarge_books_texto_subtitulo').css({'color': '#E30066'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_subtitulo').css({'font-size': '16px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_subtitulo').css({'line-height': '29px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_subtitulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_subtitulo').css({'top': '175px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_subtitulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_subtitulo').css({'font-weight': '600'});

$('div#chipsAnimation_Entrance_enlarge_trolley_texto_subtitulo').css({'color': '#E30066'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_subtitulo').css({'font-size': '16px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_subtitulo').css({'line-height': '29px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_subtitulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_subtitulo').css({'top': '175px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_subtitulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_subtitulo').css({'font-weight': '600'});

$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_subtitulo').css({'color': '#E30066'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_subtitulo').css({'font-size': '16px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_subtitulo').css({'line-height': '29px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_subtitulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_subtitulo').css({'top': '135px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_subtitulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_subtitulo').css({'font-weight': '600'});

$('div#chipsAnimation_Entrance_enlarge_rocket_texto_subtitulo').css({'color': '#E30066'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_subtitulo').css({'font-size': '16px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_subtitulo').css({'line-height': '29px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_subtitulo').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_subtitulo').css({'top': '175px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_subtitulo').css({'font-family': 'Dosis'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_subtitulo').css({'font-weight': '600'});

$('div#chipsAnimation_Entrance_enlarge_icon_texto_contenido').css({'color': '#e107a'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_contenido').css({'font-size': '22px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_contenido').css({'line-height': '25px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_contenido').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_contenido').css({'top': '205px'});
$('div#chipsAnimation_Entrance_enlarge_icon_texto_contenido').css({'font-family': 'Dosis'});

$('div#chipsAnimation_Entrance_enlarge_building_texto_contenido').css({'color': '#e107a'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_contenido').css({'font-size': '22px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_contenido').css({'line-height': '25px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_contenido').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_contenido').css({'top': '175px'});
$('div#chipsAnimation_Entrance_enlarge_building_texto_contenido').css({'font-family': 'Dosis'});

$('div#chipsAnimation_Entrance_enlarge_books_texto_contenido').css({'color': '#e107a'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_contenido').css({'font-size': '22px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_contenido').css({'line-height': '25px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_contenido').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_contenido').css({'top': '215px'});
$('div#chipsAnimation_Entrance_enlarge_books_texto_contenido').css({'font-family': 'Dosis'});

$('div#chipsAnimation_Entrance_enlarge_trolley_texto_contenido').css({'color': '#e107a'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_contenido').css({'font-size': '22px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_contenido').css({'line-height': '25px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_contenido').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_contenido').css({'top': '215px'});
$('div#chipsAnimation_Entrance_enlarge_trolley_texto_contenido').css({'font-family': 'Dosis'});

$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_contenido').css({'color': '#e107a'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_contenido').css({'font-size': '22px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_contenido').css({'line-height': '25px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_contenido').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_contenido').css({'top': '175px'});
$('div#chipsAnimation_Entrance_enlarge_piggybank_texto_contenido').css({'font-family': 'Dosis'});

$('div#chipsAnimation_Entrance_enlarge_rocket_texto_contenido').css({'color': '#e107a'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_contenido').css({'font-size': '22px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_contenido').css({'line-height': '25px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_contenido').css({'width': '400px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_contenido').css({'top': '235px'});
$('div#chipsAnimation_Entrance_enlarge_rocket_texto_contenido').css({'font-family': 'Dosis'});



});
//Edge binding end







})("Entrance");
//Edge symbol end:'Entrance'

//=========================================================

//Edge symbol: 'enlarge_icon'
(function(symbolName) {












})("enlarge_icon");
//Edge symbol end:'enlarge_icon'

//=========================================================

//Edge symbol: 'enlarge_building'
(function(symbolName) {











})("enlarge_building");
//Edge symbol end:'enlarge_building'

//=========================================================

//Edge symbol: 'enlarge_books'
(function(symbolName) {











})("enlarge_books");
//Edge symbol end:'enlarge_books'

//=========================================================

//Edge symbol: 'enlarge_trolley'
(function(symbolName) {











})("enlarge_trolley");
//Edge symbol end:'enlarge_trolley'

//=========================================================

//Edge symbol: 'enlarge_piggybank'
(function(symbolName) {











})("enlarge_piggybank");
//Edge symbol end:'enlarge_piggybank'

//=========================================================

//Edge symbol: 'enlarge_rocket'
(function(symbolName) {











})("enlarge_rocket");
//Edge symbol end:'enlarge_rocket'

})(jQuery, AdobeEdge, "EDGE-112784366");