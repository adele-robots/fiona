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

Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 442, function(sym, e) {
// Show an Element.
//  (sym.$("name") resolves an Edge element name to a DOM
//  element that can be used with jQuery)
sym.$("labels2").show();
// insert code here
});
//Edge binding end

Symbol.bindElementAction(compId, symbolName, "document", "compositionReady", function(sym, e) {
// insert code for compositionReady event here
//$.Edge.getComposition("EDGE-68107672").getStage().play();
});
//Edge binding end

Symbol.bindTimelineAction(compId, symbolName, "Default Timeline", "play", function(sym, e) {
// insert code to be run at timeline play here
});
//Edge binding end













Symbol.bindTriggerAction(compId, symbolName, "Default Timeline", 1499, function(sym, e) {
// insert code here
$('div#out').css({'visibility':'hidden'});
$('div#chipsAnimation').css({'visibility':'visible'});
});
//Edge binding end










})("stage");
//Edge symbol end:'stage'

//=========================================================

//Edge symbol: 'labelsall'
(function(symbolName) {


})("labelsall");
//Edge symbol end:'labelsall'

})(jQuery, AdobeEdge, "EDGE-68107672");