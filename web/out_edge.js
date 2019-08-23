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
            rect:[84,445,952,675],
            fill:["rgba(0,0,0,0)",im+"brainworld.png"],
            transform:[[144,-109]]
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
            id:'labelsall2',
            type:'rect',
            rect:[82,256,0,0]
         }],
         symbolInstances: [
         {
            id:'labelsall2',
            symbolName:'labelsall'
         }
         ]
      },
   states: {
      "Base State": {
         "${_frontlayer}": [
            ["transform", "translateX", '-272px'],
            ["transform", "translateY", '-402px']
         ],
         "${_brainworld}": [
            ["transform", "translateY", '-112px'],
            ["transform", "translateX", '144px']
         ],
         "${_labelsall2}": [
            ["style", "opacity", '1'],
            ["transform", "translateX", '-0.64px'],
            ["transform", "translateY", '2.12px']
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
            ["transform", "translateY", '-509px'],
            ["transform", "translateX", '-259px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1499,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid126", tween: [ "transform", "${_frontlayer}", "translateY", '-335.013px', { fromValue: '-402px'}], position: 250, duration: 578 },
            { id: "eid133", tween: [ "transform", "${_frontlayer}", "translateY", '-54.203px', { fromValue: '-335.013px'}], position: 828, duration: 317 },
            { id: "eid136", tween: [ "transform", "${_frontlayer}", "translateY", '303px', { fromValue: '-54.203px'}], position: 1146, duration: 353 },
            { id: "eid170", tween: [ "transform", "${_labelsall2}", "translateX", '-0.64px', { fromValue: '-0.64px'}], position: 0, duration: 0 },
            { id: "eid163", tween: [ "style", "${_labelsall2}", "opacity", '0', { fromValue: '1'}], position: 250, duration: 1249 },
            { id: "eid171", tween: [ "transform", "${_brainworld}", "translateY", '-109px', { fromValue: '-112px'}], position: 0, duration: 250 },
            { id: "eid152", tween: [ "transform", "${_brainworld}", "translateY", '58px', { fromValue: '-109px'}], position: 250, duration: 578 },
            { id: "eid157", tween: [ "transform", "${_brainworld}", "translateY", '294px', { fromValue: '58px'}], position: 828, duration: 317 },
            { id: "eid160", tween: [ "transform", "${_brainworld}", "translateY", '596px', { fromValue: '294px'}], position: 1145, duration: 354 },
            { id: "eid148", tween: [ "transform", "${_brainworld}", "translateX", '144px', { fromValue: '144px'}], position: 250, duration: 0 },
            { id: "eid150", tween: [ "transform", "${_brainworld}", "translateX", '144px', { fromValue: '144px'}], position: 828, duration: 0 },
            { id: "eid153", tween: [ "transform", "${_brainworld}", "translateX", '144px', { fromValue: '144px'}], position: 1145, duration: 0 },
            { id: "eid158", tween: [ "transform", "${_brainworld}", "translateX", '144px', { fromValue: '144px'}], position: 1499, duration: 0 },
            { id: "eid129", tween: [ "transform", "${_stuff}", "translateY", '-692.414px', { fromValue: '-509px'}], position: 250, duration: 578 },
            { id: "eid134", tween: [ "transform", "${_stuff}", "translateY", '-965.553px', { fromValue: '-692.414px'}], position: 828, duration: 317 },
            { id: "eid137", tween: [ "transform", "${_stuff}", "translateY", '-1313px', { fromValue: '-965.553px'}], position: 1146, duration: 353 }         ]
      }
   }
},
"labelsall": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      transform: [[-154,163]],
      id: 'labelsall',
      type: 'image',
      rect: [155,-162,1236,720],
      fill: ['rgba(0,0,0,0)','images/labelsall.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_labelsall}": [
            ["style", "opacity", '1'],
            ["transform", "translateX", '-154px'],
            ["transform", "translateY", '163px']
         ],
         "${symbolSelector}": [
            ["style", "height", '720px'],
            ["style", "width", '1235px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1499,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid147", tween: [ "style", "${_labelsall}", "opacity", '0', { fromValue: '1'}], position: 0, duration: 1499 }         ]
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
})(jQuery, AdobeEdge, "EDGE-68107672");
