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



Symbol.bindElementAction(compId, symbolName, "document", "compositionReady", function(sym, e) {
// insert code for compositionReady event here
sym.getSymbol("building_move").play();
sym.getSymbol("trolley_move").play();
sym.getSymbol("bubbles_move").play();

//$.Edge.getComposition('EDGE-492073588').getSymbols("bluebook_jump").play();

loop("bluebook_jump",8000);
loop("coin_fall",6000);
loop("icon_jump",7000);

});
//Edge binding end

Symbol.bindTimelineAction(compId, symbolName, "Default Timeline", "play", function(sym, e) {
// insert code to be run at timeline play here
// insert code here
$('div#standby').getSymbol("labelsall").css({'left':'236px'});
$('div#standby').getSymbol("labelsall").css({'top':'96px'});

});
//Edge binding end









Symbol.bindElementAction(compId, symbolName, "${_icon_jump}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("icon_jump").play();
});
//Edge binding end



Symbol.bindElementAction(compId, symbolName, "${_trolley_move}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("trolley_move").getSymbol("trolley_jump").play();
});
//Edge binding end










Symbol.bindElementAction(compId, symbolName, "${_piggylayer}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("coin_fall").play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_piggybank}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("coin_fall").play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_nakedbuilding}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("man_move").play();
});
//Edge binding end





Symbol.bindElementAction(compId, symbolName, "${_Rectangle1}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(1);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_icon_jump}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(1);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle2}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(5);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_piggylayer}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(5);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_piggybank}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(5);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle3}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(3);
});
//Edge binding end





Symbol.bindElementAction(compId, symbolName, "${_Rectangle4}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(6);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_rocket_move}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("rocket_move").play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_rocket_move}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(6);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_rocket_move}", "mouseout", function(sym, e) {
// insert code for mouse leave here
sym.getSymbol("rocket_move").stop(0);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_trolley_move}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(4);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle5}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(4);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle6}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(2);
});
//Edge binding end



Symbol.bindElementAction(compId, symbolName, "${_building_move}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("man_move").play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_redbook}", "mouseover", function(sym, e) {
// insert code for mouse enter here
alert('redbook mouseover');
sym.getSymbol("bluebook_jump").play();
});
//Edge binding end




Symbol.bindElementAction(compId, symbolName, "${_bluebook_jump}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("bluebook_jump").play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_bluebook_jump}", "click", function(sym, e) {
// insert code for mouse clicks here
alert('bluebook click');
chipselection(3);// insert code for mouse clicks here
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_redbook}", "click", function(sym, e) {
// insert code for mouse clicks here  
chipselection(3);


});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_building_move}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(2);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle3Book}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(3);

});
//Edge binding end


Symbol.bindElementAction(compId, symbolName, "${_Rectangle6Building}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(2);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle6Building}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("man_move").play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle3Book}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("bluebook_jump").play();
});
//Edge binding end








Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 0, function(sym, e) {
// insert code here
$('div#standby').getStage().getSymbol("labelsall").css({'left':'236px'});
$('div#standby').getStage().getSymbol("labelsall").css({'top':'96px'});



});
//Edge binding end




Symbol.bindElementAction(compId, symbolName, "${_Rectangle1Icon}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(1);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle1Icon}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("icon_jump").play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle2Piggy}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(5);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle2Piggy}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("coin_fall").play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle4Rocket}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(6);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle4Rocket}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("rocket_move").play();

});
//Edge binding end


Symbol.bindElementAction(compId, symbolName, "${_Rectangle4Rocket}", "mouseout", function(sym, e) {
// insert code for mouse leave here
sym.getSymbol("rocket_move").stop(0);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle5Trolley}", "click", function(sym, e) {
// insert code for mouse clicks here
chipselection(4);
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_Rectangle5Trolley}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("trolley_move").play();
});
//Edge binding end


})("stage");
//Edge symbol end:'stage'

//=========================================================

//Edge symbol: 'icon_jump'
(function(symbolName) {


















})("icon_jump");
//Edge symbol end:'icon_jump'

//=========================================================

//Edge symbol: 'trolley_move'
(function(symbolName) {
Symbol.bindTimelineAction(compId, symbolName, "Default Timeline", "complete", function(sym, e) {
});
//Edge binding end





















})("trolley_move");
//Edge symbol end:'trolley_move'

//=========================================================

//Edge symbol: 'trolley_jump'
(function(symbolName) {
Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 4000, function(sym, e) {
// insert code here
sym.play();
});
//Edge binding end



















})("trolley_jump");
//Edge symbol end:'trolley_jump'

//=========================================================

//Edge symbol: 'coin_fall'
(function(symbolName) {

















})("coin_fall");
//Edge symbol end:'coin_fall'

//=========================================================

//Edge symbol: 'bluebook_jump'
(function(symbolName) {















})("bluebook_jump");
//Edge symbol end:'bluebook_jump'

//=========================================================

//Edge symbol: 'building_move'
(function(symbolName) {
Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 2000, function(sym, e) {
// insert code here
sym.play();
});
//Edge binding end
















})("building_move");
//Edge symbol end:'building_move'

//=========================================================

//Edge symbol: 'man_move'
(function(symbolName) {















})("man_move");
//Edge symbol end:'man_move'

//=========================================================

//Edge symbol: 'bubbles_move'
(function(symbolName) {
Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 1583, function(sym, e) {
// insert code here
sym.play();
});
//Edge binding end
















})("bubbles_move");
//Edge symbol end:'bubbles_move'

//=========================================================

//Edge symbol: 'rocket_move'
(function(symbolName) {
Symbol.bindElementAction(compId, symbolName, "${_rocket_alone}", "click", function(sym, e) {

});
//Edge binding end

Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 500, function(sym, e) {
// insert code here
sym.play();

});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_rocket_alone}", "mouseover", function(sym, e) {
// insert code for mouse enter here
sym.getSymbol("rocket_move").play();
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "${_rocket_alone}", "mouseout", function(sym, e) {
// insert code for mouse leave here
sym.getSymbol("rocket_move").stop(0);
});
//Edge binding end














})("rocket_move");
//Edge symbol end:'rocket_move'

//=========================================================

//Edge symbol: 'labelsall'
(function(symbolName) {




})("labelsall");
//Edge symbol end:'labelsall'

//=========================================================

//Edge symbol: 'labelsall1'
(function(symbolName) {




})("labelsall1");
//Edge symbol end:'labelsall1'

})(jQuery, AdobeEdge, "EDGE-492073588");


function loop (symbol, time){
	$.Edge.getComposition("EDGE-492073588").getStage().getSymbol(symbol).play();
	setTimeout('loop(\''+symbol+'\','+time+')', time);
}

