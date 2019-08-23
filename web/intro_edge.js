/**
 * Adobe Edge: symbol definitions
 */
(function($, Edge, compId){
//images folder
var im='images/';

var fonts = {};


var resources = [
];
var symbols = {
"stage": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
         dom: [
         {
            id:'brainworld',
            type:'image',
            rect:[86,338,952,675],
            fill:["rgba(0,0,0,0)",im+"brainworld.png"],
            transform:[[142,-3]]
         },
         {
            id:'stuff',
            type:'image',
            rect:[702,847,641,470],
            fill:["rgba(0,0,0,0)",im+"stuff.png"],
            transform:[[-334,-1313]]
         },
         {
            id:'frontlayer',
            type:'image',
            rect:[743,879,598,234],
            fill:["rgba(0,0,0,0)",im+"frontlayer.png"],
            transform:[[-348,-418]]
         },
         {
            id:'labelsall',
            type:'image',
            rect:[172,140,1236,720],
            fill:["rgba(0,0,0,0)",im+"labelsall.png"],
            transform:[[-90,119]]
         }],
         symbolInstances: [

         ]
      },
   states: {
      "Base State": {
         "${_frontlayer}": [
            ["transform", "translateX", '-272px'],
            ["transform", "translateY", '303px']
         ],
         "${_brainworld}": [
            ["transform", "translateX", '142px'],
            ["transform", "translateY", '706px']
         ],
         "${_labelsall}": [
            ["style", "opacity", '0'],
            ["transform", "translateX", '-90px'],
            ["transform", "translateY", '119px']
         ],
         "${_Stage}": [
            ["color", "background-color", 'rgba(203,230,244,1.00)'],
            ["style", "height", '1024px'],
            ["style", "width", '1436px']
         ],
         "body": [
            ["color", "background-color", 'rgba(0,0,0,0)']
         ],
         "${_stuff}": [
            ["transform", "translateY", '-1313px'],
            ["transform", "translateX", '-259px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1695,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid38", tween: [ "transform", "${_frontlayer}", "translateY", '-418px', { fromValue: '303px'}], position: 0, duration: 750 },
            { id: "eid44", tween: [ "transform", "${_frontlayer}", "translateY", '-378px', { fromValue: '-418px'}], position: 750, duration: 205 },
            { id: "eid47", tween: [ "transform", "${_frontlayer}", "translateY", '-398px', { fromValue: '-378px'}], position: 955, duration: 200 },
            { id: "eid54", tween: [ "transform", "${_frontlayer}", "translateY", '-388px', { fromValue: '-398px'}], position: 1155, duration: 155 },
            { id: "eid59", tween: [ "transform", "${_frontlayer}", "translateY", '-398px', { fromValue: '-388px'}], position: 1310, duration: 115 },
            { id: "eid112", tween: [ "transform", "${_frontlayer}", "translateY", '-402px', { fromValue: '-398px'}], position: 1425, duration: 270 },
            { id: "eid107", tween: [ "transform", "${_stuff}", "translateX", '-259px', { fromValue: '-259px'}], position: 1695, duration: 0 },
            { id: "eid134", tween: [ "style", "${_labelsall}", "opacity", '1', { fromValue: '0'}], position: 500, duration: 1195 },
            { id: "eid21", tween: [ "transform", "${_stuff}", "translateY", '-527px', { fromValue: '-1313px'}], position: 0, duration: 750 },
            { id: "eid42", tween: [ "transform", "${_stuff}", "translateY", '-577px', { fromValue: '-527px'}], position: 750, duration: 205 },
            { id: "eid50", tween: [ "transform", "${_stuff}", "translateY", '-507px', { fromValue: '-577px'}], position: 955, duration: 200 },
            { id: "eid56", tween: [ "transform", "${_stuff}", "translateY", '-527px', { fromValue: '-507px'}], position: 1155, duration: 155 },
            { id: "eid62", tween: [ "transform", "${_stuff}", "translateY", '-505px', { fromValue: '-527px'}], position: 1310, duration: 115 },
            { id: "eid111", tween: [ "transform", "${_stuff}", "translateY", '-509px', { fromValue: '-505px'}], position: 1425, duration: 270 },
            { id: "eid108", tween: [ "transform", "${_frontlayer}", "translateX", '-272px', { fromValue: '-272px'}], position: 1695, duration: 0 },
            { id: "eid121", tween: [ "transform", "${_brainworld}", "translateX", '142px', { fromValue: '142px'}], position: 0, duration: 0 },
            { id: "eid131", tween: [ "transform", "${_brainworld}", "translateX", '142px', { fromValue: '142px'}], position: 955, duration: 0 },
            { id: "eid130", tween: [ "transform", "${_brainworld}", "translateX", '142px', { fromValue: '142px'}], position: 1155, duration: 0 },
            { id: "eid127", tween: [ "transform", "${_brainworld}", "translateX", '142px', { fromValue: '142px'}], position: 1310, duration: 0 },
            { id: "eid123", tween: [ "transform", "${_brainworld}", "translateX", '142px', { fromValue: '142px'}], position: 1425, duration: 0 },
            { id: "eid119", tween: [ "transform", "${_brainworld}", "translateX", '142px', { fromValue: '142px'}], position: 1695, duration: 0 },
            { id: "eid125", tween: [ "transform", "${_brainworld}", "translateY", '-16.316px', { fromValue: '706px'}], position: 0, duration: 750 },
            { id: "eid133", tween: [ "transform", "${_brainworld}", "translateY", '23.69px', { fromValue: '-16.316px'}], position: 750, duration: 205 },
            { id: "eid132", tween: [ "transform", "${_brainworld}", "translateY", '2.673px', { fromValue: '23.69px'}], position: 955, duration: 200 },
            { id: "eid129", tween: [ "transform", "${_brainworld}", "translateY", '3.435px', { fromValue: '2.673px'}], position: 1155, duration: 155 },
            { id: "eid128", tween: [ "transform", "${_brainworld}", "translateY", '4px', { fromValue: '3.435px'}], position: 1310, duration: 115 },
            { id: "eid126", tween: [ "transform", "${_brainworld}", "translateY", '-3px', { fromValue: '4px'}], position: 1425, duration: 270 }         ]
      }
   }
}
};


Edge.registerCompositionDefn(compId, symbols, fonts, resources);

/**
 * Adobe Edge DOM Ready Event Handler
 */
$(window).ready(function() {
     Edge.launchComposition(compId);
});
})(jQuery, AdobeEdge, "EDGE-491969267");
