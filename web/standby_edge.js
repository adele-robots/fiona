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
            id:'brainworldcomplete',
            type:'image',
            rect:[124,172,1167,675],
            fill:["rgba(0,0,0,0)",im+"brainworldcomplete.png"],
            transform:[[102,165]]
         },
         {
            id:'labelsall',
            type:'rect',
            rect:[89,255,0,0]
         },
         {
            id:'icon_jump',
            type:'rect',
            rect:[596,338,0,0]
         },
         {
            id:'redbook',
            type:'image',
            rect:[930,576,138,101],
            fill:["rgba(0,0,0,0)",im+"redbook.png"],
            transform:[[0,4]]
         },
         {
            id:'bluebook_jump',
            type:'rect',
            rect:[955,575,0,0]
         },
         {
            id:'piggybank',
            type:'image',
            rect:[856,602,114,109],
            fill:["rgba(0,0,0,0)",im+"piggybank.png"],
            transform:[[-81,-143]]
         },
         {
            id:'building2',
            type:'image',
            rect:[476,636,171,148],
            fill:["rgba(0,0,0,0)",im+"building2.png"],
            transform:[[-34,-134]]
         },
         {
            id:'nakedbuilding',
            type:'image',
            rect:[441,501,158,151],
            fill:["rgba(0,0,0,0)",im+"nakedbuilding.png"]
         },
         {
            id:'building_move',
            type:'rect',
            rect:[505,559,0,0]
         },
         {
            id:'trolley_move',
            type:'rect',
            rect:[527,664,0,0]
         },
         {
            id:'frontlayer',
            type:'image',
            rect:[743,879,598,234],
            fill:["rgba(0,0,0,0)",im+"frontlayer.png"],
            transform:[[-348,-400]]
         },
         {
            id:'coin_fall',
            type:'rect',
            rect:[828,406,0,0]
         },
         {
            id:'piggylayer',
            type:'image',
            rect:[808,488,30,35],
            fill:["rgba(0,0,0,0)",im+"piggylayer.png"],
            transform:[[11,-25]]
         },
         {
            id:'man_move',
            type:'rect',
            rect:[575,593,0,0]
         },
         {
            id:'rocket_move',
            type:'rect',
            rect:[746,614,0,0]
         },
         {
            id:'bubbles_move',
            type:'rect',
            rect:[730,743,0,0]
         },
         {
            id:'Rectangle3Book',
            type:'rect',
            rect:[286,255,138,110],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[644,313]]
         },
         {
            id:'Rectangle6Building',
            type:'rect',
            rect:[286,255,122,110],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[168,269]]
         },
         {
            id:'Rectangle6',
            type:'rect',
            rect:[286,255,138,88],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[-193,313]]
         },
         {
            id:'Rectangle5',
            type:'rect',
            rect:[286,255,138,88],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[32,634]]
         },
         {
            id:'Rectangle5Trolley',
            type:'rect',
            rect:[286,255,90,140],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[250,410]]
         },
         {
            id:'Rectangle4',
            type:'rect',
            rect:[286,255,152,109],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[873,519]]
         },
         {
            id:'Rectangle4Rocket',
            type:'rect',
            rect:[286,255,90,168],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[443,366]]
         },
         {
            id:'Rectangle3',
            type:'rect',
            rect:[286,255,152,109],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[855,208]]
         },
         {
            id:'Rectangle2',
            type:'rect',
            rect:[286,255,109,89],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[710,46]]
         },
         {
            id:'Rectangle1',
            type:'rect',
            rect:[286,255,148,88],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[5,19]]
         },
         {
            id:'Rectangle1Icon',
            type:'rect',
            rect:[286,255,148,150],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[280,90]]
         },
         {
            id:'Rectangle2Piggy',
            type:'rect',
            rect:[286,255,109,89],
            fill:["rgba(192,192,192,1)"],
            stroke:[0,"rgba(0,0,0,1)","none"],
            transform:[[495,208]]
         }],
         symbolInstances: [
         {
            id:'coin_fall',
            symbolName:'coin_fall'
         },
         {
            id:'man_move',
            symbolName:'man_move'
         },
         {
            id:'bubbles_move',
            symbolName:'bubbles_move'
         },
         {
            id:'rocket_move',
            symbolName:'rocket_move'
         },
         {
            id:'trolley_move',
            symbolName:'trolley_jump'
         },
         {
            id:'labelsall',
            symbolName:'labelsall1'
         },
         {
            id:'bluebook_jump',
            symbolName:'bluebook_jump'
         },
         {
            id:'building_move',
            symbolName:'building_move'
         },
         {
            id:'icon_jump',
            symbolName:'icon_jump'
         }
         ]
      },
   states: {
      "Base State": {
         "${_Rectangle5Trolley}": [
            ["transform", "translateX", '250px'],
            ["style", "height", '140px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '410px'],
            ["style", "width", '90px']
         ],
         "${_Rectangle2}": [
            ["transform", "translateX", '710px'],
            ["style", "height", '89px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '46px'],
            ["style", "width", '109px']
         ],
         "${_Rectangle1Icon}": [
            ["transform", "translateX", '280px'],
            ["style", "height", '150px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '90px'],
            ["style", "width", '148px']
         ],
         "${_Rectangle1}": [
            ["transform", "translateX", '5.803px'],
            ["style", "height", '88px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '19.802px'],
            ["style", "width", '148px']
         ],
         "${_brainworldcomplete}": [
            ["transform", "translateX", '102.157px'],
            ["transform", "translateY", '165.703px']
         ],
         "${_bubbles_move}": [
            ["transform", "translateX", '-2px']
         ],
         "${_Rectangle4}": [
            ["transform", "translateX", '873px'],
            ["style", "height", '109px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '519.999px'],
            ["style", "width", '152px']
         ],
         "${_Rectangle4Rocket}": [
            ["transform", "translateX", '443px'],
            ["style", "height", '168px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '366px'],
            ["style", "width", '90px']
         ],
         "${_frontlayer}": [
            ["transform", "translateX", '-272px'],
            ["transform", "translateY", '-400px']
         ],
         "${_Stage}": [
            ["style", "height", '1024px'],
            ["color", "background-color", 'rgba(203,230,244,1.00)'],
            ["style", "width", '1436px']
         ],
         "${_labelsall}": [
            ["transform", "translateX", '-10.962px'],
            ["transform", "translateY", '5.37px']
         ],
         "${_redbook}": [
            ["transform", "translateY", '4px']
         ],
         "${_building2}": [
            ["transform", "translateX", '-34.999px'],
            ["transform", "translateY", '-134.722px']
         ],
         "body": [
            ["color", "background-color", 'rgba(0,0,0,0)']
         ],
         "${_Rectangle6}": [
            ["transform", "translateX", '-193.998px'],
            ["style", "height", '88px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '313px'],
            ["style", "width", '138px']
         ],
         "${_piggylayer}": [
            ["transform", "translateX", '11.337px'],
            ["transform", "translateY", '-25.834px']
         ],
         "${_Rectangle6Building}": [
            ["transform", "translateX", '168px'],
            ["style", "height", '110px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '269.209px'],
            ["style", "width", '122px']
         ],
         "${_Rectangle2Piggy}": [
            ["transform", "translateX", '495px'],
            ["style", "height", '88px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '208px'],
            ["style", "width", '108px']
         ],
         "${_Rectangle5}": [
            ["transform", "translateX", '32.401px'],
            ["style", "height", '88px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '634.91px'],
            ["style", "width", '138px']
         ],
         "${_piggybank}": [
            ["transform", "translateY", '-143px'],
            ["transform", "translateX", '-81px']
         ],
         "${_Rectangle3}": [
            ["transform", "translateX", '855.454px'],
            ["style", "height", '109px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '208px'],
            ["style", "width", '152px']
         ],
         "${_Rectangle3Book}": [
            ["transform", "translateX", '644px'],
            ["style", "height", '110px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '313px'],
            ["style", "width", '138px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1850.2755234602,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid425", tween: [ "transform", "${_labelsall}", "translateY", '5.37px', { fromValue: '5.37px'}], position: 0, duration: 0 },
            { id: "eid426", tween: [ "transform", "${_labelsall}", "translateX", '-10.962px', { fromValue: '-10.962px'}], position: 0, duration: 0 },
            { id: "eid185", trigger: [ function executeSymbolFunction(e, data) { this._executeSymbolAction(e, data); }, ['play', '${_icon_jump}', [] ], ""], position: 1850.2755234602 }         ]
      }
   }
},
"icon_jump": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      type: 'image',
      id: 'icon',
      rect: [57,395,98,178],
      transform: [[-57,-396]],
      fill: ['rgba(0,0,0,0)','images/icon.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_icon}": [
            ["style", "-webkit-transform-origin", [50,50], {valueTemplate:'@@0@@% @@1@@%'} ],
            ["style", "-moz-transform-origin", [50,50],{valueTemplate:'@@0@@% @@1@@%'}],
            ["style", "-ms-transform-origin", [50,50],{valueTemplate:'@@0@@% @@1@@%'}],
            ["style", "msTransformOrigin", [50,50],{valueTemplate:'@@0@@% @@1@@%'}],
            ["style", "-o-transform-origin", [50,50],{valueTemplate:'@@0@@% @@1@@%'}],
            ["transform", "translateX", '-57.999px'],
            ["transform", "scaleX", '1'],
            ["transform", "translateY", '-395.877px'],
            ["transform", "scaleY", '0.996']
         ],
         "${symbolSelector}": [
            ["style", "height", '177.288px'],
            ["style", "width", '98px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1768.4354077063,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid163", tween: [ "transform", "${_icon}", "translateY", '-388.877px', { fromValue: '-395.877px'}], position: 0, duration: 250 },
            { id: "eid157", tween: [ "transform", "${_icon}", "translateY", '-405.996px', { fromValue: '-388.877px'}], position: 250, duration: 200 },
            { id: "eid124", tween: [ "transform", "${_icon}", "translateY", '-435.999px', { fromValue: '-405.996px'}], position: 450, duration: 100 },
            { id: "eid165", tween: [ "transform", "${_icon}", "translateY", '-445.999px', { fromValue: '-435.999px'}], position: 550, duration: 120 },
            { id: "eid168", tween: [ "transform", "${_icon}", "translateY", '-450.999px', { fromValue: '-445.999px'}], position: 670, duration: 80 },
            { id: "eid170", tween: [ "transform", "${_icon}", "translateY", '-445.999px', { fromValue: '-450.999px'}], position: 750, duration: 95 },
            { id: "eid171", tween: [ "transform", "${_icon}", "translateY", '-435.999px', { fromValue: '-445.999px'}], position: 845, duration: 105 },
            { id: "eid172", tween: [ "transform", "${_icon}", "translateY", '-395.999px', { fromValue: '-435.999px'}], position: 950, duration: 130 },
            { id: "eid173", tween: [ "transform", "${_icon}", "translateY", '-385.969px', { fromValue: '-395.999px'}], position: 1080, duration: 170 },
            { id: "eid175", tween: [ "transform", "${_icon}", "translateY", '-399.87px', { fromValue: '-385.969px'}], position: 1250, duration: 115 },
            { id: "eid177", tween: [ "transform", "${_icon}", "translateY", '-390.679px', { fromValue: '-399.87px'}], position: 1365, duration: 95 },
            { id: "eid179", tween: [ "transform", "${_icon}", "translateY", '-397.45px', { fromValue: '-390.679px'}], position: 1460, duration: 92 },
            { id: "eid181", tween: [ "transform", "${_icon}", "translateY", '-393.578px', { fromValue: '-397.45px'}], position: 1552, duration: 110 },
            { id: "eid183", tween: [ "transform", "${_icon}", "translateY", '-395.512px', { fromValue: '-393.578px'}], position: 1663, duration: 105 },
            { id: "eid161", tween: [ "transform", "${_icon}", "scaleY", '0.887', { fromValue: '0.996'}], position: 0, duration: 250 },
            { id: "eid159", tween: [ "transform", "${_icon}", "scaleY", '1.001', { fromValue: '0.887'}], position: 250, duration: 200 },
            { id: "eid166", tween: [ "transform", "${_icon}", "scaleY", '1', { fromValue: '1.001'}], position: 450, duration: 220 },
            { id: "eid174", tween: [ "transform", "${_icon}", "scaleY", '0.887', { fromValue: '1'}], position: 1080, duration: 170 },
            { id: "eid176", tween: [ "transform", "${_icon}", "scaleY", '1.04', { fromValue: '0.887'}], position: 1250, duration: 115 },
            { id: "eid178", tween: [ "transform", "${_icon}", "scaleY", '0.942', { fromValue: '1.04'}], position: 1365, duration: 95 },
            { id: "eid180", tween: [ "transform", "${_icon}", "scaleY", '1.017', { fromValue: '0.942'}], position: 1460, duration: 92 },
            { id: "eid182", tween: [ "transform", "${_icon}", "scaleY", '0.977', { fromValue: '1.017'}], position: 1552, duration: 110 },
            { id: "eid184", tween: [ "transform", "${_icon}", "scaleY", '0.998', { fromValue: '0.977'}], position: 1663, duration: 105 }         ]
      }
   }
},
"trolley_move": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      type: 'image',
      id: 'trolley22',
      rect: [-47,113,96,114],
      transform: [[46,-114]],
      fill: ['rgba(0,0,0,0)','images/trolley2.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_trolley22}": [
            ["transform", "translateX", '46.296px'],
            ["transform", "translateY", '-114.268px']
         ],
         "${symbolSelector}": [
            ["style", "height", '111px'],
            ["style", "width", '121px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1000,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid244", tween: [ "transform", "${_trolley22}", "translateY", '-144.268px', { fromValue: '-114.268px'}], position: 0, duration: 500 },
            { id: "eid245", tween: [ "transform", "${_trolley22}", "translateY", '-114.268px', { fromValue: '-144.268px'}], position: 500, duration: 500 }         ]
      }
   }
},
"trolley_jump": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      transform: [[353,262]],
      id: 'trolley_shadow2',
      type: 'image',
      rect: [-351,-218,122,67],
      fill: ['rgba(0,0,0,0)','images/trolley_shadow.png']
   },
   {
      id: 'trolley_jump',
      type: 'rect',
      rect: [0,1,0,0]
   }],
   symbolInstances: [
   {
      id: 'trolley_jump',
      symbolName: 'trolley_move'
   }   ]
   },
   states: {
      "Base State": {
         "${_trolley_jump}": [
            ["transform", "translateX", '5.175px'],
            ["transform", "translateY", '-0.803px']
         ],
         "${_trolley_shadow2}": [
            ["transform", "translateX", '353.999px'],
            ["transform", "translateY", '262px']
         ],
         "${symbolSelector}": [
            ["style", "height", '111.006999px'],
            ["style", "width", '123.472998px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 4000,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid251", tween: [ "transform", "${_trolley_jump}", "translateY", '10.196px', { fromValue: '-0.803px'}], position: 0, duration: 2015 },
            { id: "eid260", tween: [ "transform", "${_trolley_jump}", "translateY", '-1px', { fromValue: '10.196px'}], position: 2015, duration: 1985 },
            { id: "eid266", tween: [ "transform", "${_trolley_shadow2}", "translateX", '339.999px', { fromValue: '353.999px'}], position: 0, duration: 2015 },
            { id: "eid283", tween: [ "transform", "${_trolley_shadow2}", "translateX", '354px', { fromValue: '339.999px'}], position: 2015, duration: 1985 },
            { id: "eid268", tween: [ "transform", "${_trolley_shadow2}", "translateY", '274px', { fromValue: '262px'}], position: 0, duration: 2015 },
            { id: "eid282", tween: [ "transform", "${_trolley_shadow2}", "translateY", '262px', { fromValue: '274px'}], position: 2015, duration: 1985 },
            { id: "eid249", tween: [ "transform", "${_trolley_jump}", "translateX", '-13px', { fromValue: '5.175px'}], position: 0, duration: 2015 },
            { id: "eid258", tween: [ "transform", "${_trolley_jump}", "translateX", '5px', { fromValue: '-13px'}], position: 2015, duration: 1985 }         ]
      }
   }
},
"coin_fall": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      rect: [285,19,18,20],
      transform: [[-287,6]],
      id: 'coin',
      type: 'image',
      display: 'none',
      fill: ['rgba(0,0,0,0)','images/coin.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${symbolSelector}": [
            ["style", "height", '19.384615384615px'],
            ["style", "width", '18px']
         ],
         "${_coin}": [
            ["style", "display", 'none'],
            ["transform", "translateY", '-32.878px'],
            ["transform", "translateX", '-282.535px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1580,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid204", tween: [ "style", "${_coin}", "display", 'block', { fromValue: 'none'}], position: 135, duration: 0 },
            { id: "eid209", tween: [ "style", "${_coin}", "display", 'none', { fromValue: 'block'}], position: 955, duration: 0 },
            { id: "eid214", tween: [ "style", "${_coin}", "display", 'block', { fromValue: 'none'}], position: 1040, duration: 0 },
            { id: "eid231", tween: [ "style", "${_coin}", "display", 'block', { fromValue: 'none'}], position: 1580, duration: 0 },
            { id: "eid237", tween: [ "transform", "${_coin}", "translateX", '-279.535px', { fromValue: '-281.535px'}], position: 310, duration: 595 },
            { id: "eid215", tween: [ "transform", "${_coin}", "translateX", '-286px', { fromValue: '-292.884px'}], position: 905, duration: 135 },
            { id: "eid219", tween: [ "transform", "${_coin}", "translateX", '-293px', { fromValue: '-286px'}], position: 1040, duration: 540 },
            { id: "eid211", tween: [ "transform", "${_coin}", "translateY", '46.121px', { fromValue: '-32.878px'}], position: 310, duration: 595 },
            { id: "eid216", tween: [ "transform", "${_coin}", "translateY", '-20px', { fromValue: '46.121px'}], position: 905, duration: 135 },
            { id: "eid220", tween: [ "transform", "${_coin}", "translateY", '46px', { fromValue: '-20px'}], position: 1040, duration: 540 }         ]
      }
   }
},
"bluebook_jump": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      type: 'image',
      id: 'bluebook',
      rect: [-1,0,96,71],
      transform: [[2]],
      fill: ['rgba(0,0,0,0)','images/bluebook.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_bluebook}": [
            ["transform", "translateX", '2px'],
            ["transform", "translateY", '0px']
         ],
         "${symbolSelector}": [
            ["style", "height", '71px'],
            ["style", "width", '96px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 2113,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid288", tween: [ "transform", "${_bluebook}", "translateY", '-56px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid290", tween: [ "transform", "${_bluebook}", "translateY", '0px', { fromValue: '-56px'}], position: 1500, duration: 165 },
            { id: "eid291", tween: [ "transform", "${_bluebook}", "translateY", '-17px', { fromValue: '0px'}], position: 1665, duration: 134 },
            { id: "eid293", tween: [ "transform", "${_bluebook}", "translateY", '0px', { fromValue: '-17px'}], position: 1800, duration: 88 },
            { id: "eid295", tween: [ "transform", "${_bluebook}", "translateY", '-10px', { fromValue: '0px'}], position: 1888, duration: 80 },
            { id: "eid297", tween: [ "transform", "${_bluebook}", "translateY", '0px', { fromValue: '-10px'}], position: 1969, duration: 56 },
            { id: "eid299", tween: [ "transform", "${_bluebook}", "translateY", '-5px', { fromValue: '0px'}], position: 2026, duration: 47 },
            { id: "eid301", tween: [ "transform", "${_bluebook}", "translateY", '0px', { fromValue: '-5px'}], position: 2074, duration: 39 },
            { id: "eid286", tween: [ "transform", "${_bluebook}", "translateX", '2px', { fromValue: '2px'}], position: 0, duration: 0 }         ]
      }
   }
},
"building_move": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      type: 'image',
      id: 'columns',
      rect: [6,41,51,58],
      transform: [[0,-34]],
      fill: ['rgba(0,0,0,0)','images/columns.png']
   },
   {
      type: 'image',
      id: 'columns2',
      rect: [5,6,51,58],
      transform: [[1,1]],
      fill: ['rgba(0,0,0,0)','images/columns.png']
   },
   {
      type: 'image',
      id: 'frieze',
      rect: [0,6,63,42],
      transform: [[0,-6]],
      fill: ['rgba(0,0,0,0)','images/frieze.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_columns2}": [
            ["transform", "translateX", '1px'],
            ["transform", "translateY", '1.763px']
         ],
         "${_frieze}": [
            ["transform", "translateY", '-6px'],
            ["transform", "translateX", '0']
         ],
         "${_columns}": [
            ["transform", "scaleX", '1'],
            ["transform", "scaleY", '1'],
            ["transform", "translateX", '-0.385px'],
            ["transform", "translateY", '-34px']
         ],
         "${symbolSelector}": [
            ["style", "height", '65.763px'],
            ["style", "width", '63px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 2000,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid305", tween: [ "transform", "${_frieze}", "translateY", '-31px', { fromValue: '-6px'}], position: 0, duration: 993 },
            { id: "eid306", tween: [ "transform", "${_frieze}", "translateY", '-6px', { fromValue: '-31px'}], position: 993, duration: 1007 },
            { id: "eid321", tween: [ "transform", "${_columns}", "translateX", '-0.385px', { fromValue: '-0.385px'}], position: 993, duration: 0 },
            { id: "eid320", tween: [ "transform", "${_columns}", "translateY", '-53px', { fromValue: '-34px'}], position: 0, duration: 993 },
            { id: "eid322", tween: [ "transform", "${_columns}", "translateY", '-34px', { fromValue: '-53px'}], position: 993, duration: 1007 }         ]
      }
   }
},
"man_move": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      type: 'image',
      id: 'man',
      rect: [4,17,26,21],
      transform: [[-4,-17],[0,0],[0],[1,1]],
      fill: ['rgba(0,0,0,0)','images/man.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_man}": [
            ["style", "display", 'block'],
            ["transform", "translateX", '-4px'],
            ["transform", "translateY", '-17px']
         ],
         "${symbolSelector}": [
            ["style", "height", '21px'],
            ["style", "width", '26px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1250,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid325", tween: [ "transform", "${_man}", "translateX", '-19px', { fromValue: '-4px'}], position: 0, duration: 500 },
            { id: "eid327", tween: [ "transform", "${_man}", "translateX", '-45px', { fromValue: '-19px'}], position: 500, duration: 500 },
            { id: "eid331", tween: [ "transform", "${_man}", "translateX", '-4px', { fromValue: '-45px'}], position: 1000, duration: 250 },
            { id: "eid329", tween: [ "style", "${_man}", "display", 'none', { fromValue: 'block'}], position: 1000, duration: 0 },
            { id: "eid330", tween: [ "style", "${_man}", "display", 'block', { fromValue: 'none'}], position: 1250, duration: 0 },
            { id: "eid326", tween: [ "transform", "${_man}", "translateY", '-8px', { fromValue: '-17px'}], position: 0, duration: 500 },
            { id: "eid328", tween: [ "transform", "${_man}", "translateY", '-19px', { fromValue: '-8px'}], position: 500, duration: 500 },
            { id: "eid332", tween: [ "transform", "${_man}", "translateY", '-17px', { fromValue: '-19px'}], position: 1000, duration: 250 }         ]
      }
   }
},
"bubbles_move": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      type: 'image',
      transform: [[-31,-83],{},{},[0.0969,0.0969]],
      display: 'none',
      rect: [1,62,92,92],
      id: 'bubble',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[10,-79],{},{},[0.0969,0.0969]],
      display: 'none',
      rect: [1,61,92,92],
      id: 'bubbleCopy',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[-18,-79],{},{},[0.0969,0.0969]],
      display: 'none',
      rect: [1,61,92,92],
      id: 'bubbleCopy2',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[22,-92],{},{},[0.0969,0.0969]],
      display: 'none',
      rect: [1,61,92,92],
      id: 'bubbleCopy3',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[-34,-95],{},{},[0.0969,0.0969]],
      display: 'none',
      rect: [1,61,92,92],
      id: 'bubbleCopy4',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[-30,-78],{},{},[0.1405,0.1405]],
      rect: [1,61,92,92],
      id: 'bubbleCopy5',
      opacity: 0.68493150684932,
      display: 'none',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[-9,-75],{},{},[0.1405,0.1405]],
      rect: [1,61,92,92],
      id: 'bubbleCopy6',
      opacity: 0.68493150684932,
      display: 'none',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[20,-84],{},{},[0.1405,0.1405]],
      rect: [1,61,92,92],
      id: 'bubbleCopy7',
      opacity: 0.68493150684932,
      display: 'none',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[5,-75],{},{},[0.1405,0.1405]],
      rect: [1,61,92,92],
      id: 'bubbleCopy8',
      opacity: 1,
      display: 'none',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[-30,-88],{},{},[0.1405,0.1405]],
      rect: [1,61,92,92],
      id: 'bubbleCopy9',
      opacity: 1,
      display: 'none',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[22,-100],{},{},[0.1405,0.1405]],
      rect: [1,61,92,92],
      id: 'bubbleCopy10',
      opacity: 1,
      display: 'none',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   },
   {
      type: 'image',
      transform: [[-40,-91],{},{},[0.1405,0.1405]],
      rect: [1,61,92,92],
      id: 'bubbleCopy11',
      opacity: 0.68493150684932,
      display: 'none',
      fill: ['rgba(0,0,0,0)','images/bubble.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_bubble}": [
            ["transform", "scaleY", '0.096'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.096'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-83.874px'],
            ["transform", "translateX", '-31.832px']
         ],
         "${_bubbleCopy2}": [
            ["transform", "scaleY", '0.096'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.096'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-79.874px'],
            ["transform", "translateX", '-18.832px']
         ],
         "${_bubbleCopy5}": [
            ["transform", "scaleY", '0.14'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.14'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-78.932px'],
            ["transform", "translateX", '-30.89px']
         ],
         "${_bubbleCopy9}": [
            ["transform", "scaleY", '0.14'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.14'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-88.932px'],
            ["transform", "translateX", '-30.89px']
         ],
         "${_bubbleCopy4}": [
            ["transform", "scaleY", '0.096'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.096'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-95.874px'],
            ["transform", "translateX", '-34.832px']
         ],
         "${_bubbleCopy}": [
            ["transform", "scaleY", '0.096'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.096'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-79.874px'],
            ["transform", "translateX", '10.167px']
         ],
         "${_bubbleCopy7}": [
            ["transform", "scaleY", '0.14'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.14'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-84.932px'],
            ["transform", "translateX", '20.11px']
         ],
         "${_bubbleCopy10}": [
            ["transform", "scaleY", '0.14'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.14'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-100.932px'],
            ["transform", "translateX", '22.11px']
         ],
         "${symbolSelector}": [
            ["style", "height", '37.880002px'],
            ["style", "width", '75.879999px']
         ],
         "${_bubbleCopy8}": [
            ["transform", "scaleY", '0.14'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.14'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-75.932px'],
            ["transform", "translateX", '5.11px']
         ],
         "${_bubbleCopy3}": [
            ["transform", "scaleY", '0.096'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.096'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-92.874px'],
            ["transform", "translateX", '22.167px']
         ],
         "${_bubbleCopy11}": [
            ["transform", "scaleY", '0.14'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.14'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-91.932px'],
            ["transform", "translateX", '-40.89px']
         ],
         "${_bubbleCopy6}": [
            ["transform", "scaleY", '0.14'],
            ["style", "display", 'none'],
            ["transform", "scaleX", '0.14'],
            ["style", "opacity", '1'],
            ["transform", "translateY", '-75.932px'],
            ["transform", "translateX", '-9.89px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1583,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid357", tween: [ "style", "${_bubbleCopy4}", "opacity", '0', { fromValue: '1'}], position: 886, duration: 364 },
            { id: "eid361", tween: [ "style", "${_bubbleCopy8}", "display", 'block', { fromValue: 'none'}], position: 666, duration: 0 },
            { id: "eid347", tween: [ "style", "${_bubbleCopy2}", "opacity", '0', { fromValue: '1'}], position: 500, duration: 500 },
            { id: "eid355", tween: [ "style", "${_bubbleCopy5}", "opacity", '0', { fromValue: '1'}], position: 1000, duration: 424 },
            { id: "eid364", tween: [ "style", "${_bubbleCopy9}", "display", 'block', { fromValue: 'none'}], position: 500, duration: 0 },
            { id: "eid333", tween: [ "style", "${_bubble}", "display", 'block', { fromValue: 'none'}], position: 0, duration: 0 },
            { id: "eid338", tween: [ "style", "${_bubble}", "display", 'none', { fromValue: 'block'}], position: 598, duration: 0 },
            { id: "eid375", tween: [ "style", "${_bubble}", "display", 'block', { fromValue: 'none'}], position: 886, duration: 0 },
            { id: "eid370", tween: [ "style", "${_bubbleCopy11}", "display", 'block', { fromValue: 'none'}], position: 1000, duration: 0 },
            { id: "eid348", tween: [ "style", "${_bubbleCopy3}", "display", 'block', { fromValue: 'none'}], position: 416, duration: 0 },
            { id: "eid380", tween: [ "style", "${_bubbleCopy3}", "display", 'none', { fromValue: 'block'}], position: 886, duration: 0 },
            { id: "eid382", tween: [ "style", "${_bubbleCopy3}", "display", 'block', { fromValue: 'none'}], position: 1250, duration: 0 },
            { id: "eid351", tween: [ "style", "${_bubbleCopy4}", "display", 'block', { fromValue: 'none'}], position: 886, duration: 0 },
            { id: "eid367", tween: [ "style", "${_bubbleCopy10}", "display", 'block', { fromValue: 'none'}], position: 750, duration: 0 },
            { id: "eid336", tween: [ "style", "${_bubble}", "opacity", '0', { fromValue: '1'}], position: 0, duration: 598 },
            { id: "eid376", tween: [ "style", "${_bubble}", "opacity", '1', { fromValue: '0'}], position: 598, duration: 287 },
            { id: "eid378", tween: [ "style", "${_bubble}", "opacity", '0', { fromValue: '1'}], position: 886, duration: 446 },
            { id: "eid366", tween: [ "style", "${_bubbleCopy9}", "opacity", '0', { fromValue: '1'}], position: 500, duration: 832 },
            { id: "eid344", tween: [ "style", "${_bubbleCopy6}", "opacity", '0', { fromValue: '1'}], position: 318, duration: 507 },
            { id: "eid373", tween: [ "style", "${_bubbleCopy11}", "opacity", '0', { fromValue: '1'}], position: 1000, duration: 575 },
            { id: "eid350", tween: [ "style", "${_bubbleCopy3}", "opacity", '0', { fromValue: '1'}], position: 416, duration: 469 },
            { id: "eid383", tween: [ "style", "${_bubbleCopy3}", "opacity", '1', { fromValue: '0'}], position: 886, duration: 364 },
            { id: "eid337", tween: [ "style", "${_bubbleCopy}", "display", 'block', { fromValue: 'none'}], position: 598, duration: 0 },
            { id: "eid341", tween: [ "style", "${_bubbleCopy}", "display", 'none', { fromValue: 'block'}], position: 825, duration: 0 },
            { id: "eid379", tween: [ "style", "${_bubbleCopy}", "display", 'block', { fromValue: 'none'}], position: 1250, duration: 0 },
            { id: "eid342", tween: [ "style", "${_bubbleCopy6}", "display", 'block', { fromValue: 'none'}], position: 318, duration: 0 },
            { id: "eid363", tween: [ "style", "${_bubbleCopy8}", "opacity", '0', { fromValue: '1'}], position: 666, duration: 833 },
            { id: "eid345", tween: [ "style", "${_bubbleCopy2}", "display", 'block', { fromValue: 'none'}], position: 500, duration: 0 },
            { id: "eid369", tween: [ "style", "${_bubbleCopy10}", "opacity", '0', { fromValue: '1'}], position: 750, duration: 750 },
            { id: "eid353", tween: [ "style", "${_bubbleCopy5}", "display", 'block', { fromValue: 'none'}], position: 1000, duration: 0 },
            { id: "eid360", tween: [ "style", "${_bubbleCopy7}", "opacity", '0', { fromValue: '1'}], position: 416, duration: 772 },
            { id: "eid358", tween: [ "style", "${_bubbleCopy7}", "display", 'block', { fromValue: 'none'}], position: 416, duration: 0 },
            { id: "eid340", tween: [ "style", "${_bubbleCopy}", "opacity", '0', { fromValue: '1'}], position: 598, duration: 227 },
            { id: "eid385", tween: [ "style", "${_bubbleCopy}", "opacity", '1', { fromValue: '0'}], position: 825, duration: 424 },
            { id: "eid387", tween: [ "style", "${_bubbleCopy}", "opacity", '0.5', { fromValue: '1'}], position: 1250, duration: 333 }         ]
      }
   }
},
"rocket_move": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      rect: [-28,0,48,168],
      id: 'rocket_alone',
      transform: [[28]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/rocket_alone.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_rocket_alone}": [
            ["transform", "translateX", '28px'],
            ["transform", "translateY", '0px']
         ],
         "${symbolSelector}": [
            ["style", "height", '168px'],
            ["style", "width", '48px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 500,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid396", tween: [ "transform", "${_rocket_alone}", "translateY", '-2px', { fromValue: '0px'}], position: 0, duration: 76 },
            { id: "eid397", tween: [ "transform", "${_rocket_alone}", "translateY", '0px', { fromValue: '-2px'}], position: 76, duration: 71 },
            { id: "eid399", tween: [ "transform", "${_rocket_alone}", "translateY", '-2px', { fromValue: '0px'}], position: 148, duration: 70 },
            { id: "eid403", tween: [ "transform", "${_rocket_alone}", "translateY", '0px', { fromValue: '-2px'}], position: 219, duration: 74 },
            { id: "eid405", tween: [ "transform", "${_rocket_alone}", "translateY", '-2px', { fromValue: '0px'}], position: 293, duration: 55 },
            { id: "eid406", tween: [ "transform", "${_rocket_alone}", "translateY", '0px', { fromValue: '-2px'}], position: 348, duration: 54 },
            { id: "eid408", tween: [ "transform", "${_rocket_alone}", "translateY", '-2px', { fromValue: '0px'}], position: 402, duration: 48 },
            { id: "eid410", tween: [ "transform", "${_rocket_alone}", "translateY", '0px', { fromValue: '-2px'}], position: 450, duration: 50 },
            { id: "eid391", tween: [ "transform", "${_rocket_alone}", "translateX", '30px', { fromValue: '28px'}], position: 0, duration: 47 },
            { id: "eid394", tween: [ "transform", "${_rocket_alone}", "translateX", '26px', { fromValue: '30px'}], position: 47, duration: 74 },
            { id: "eid395", tween: [ "transform", "${_rocket_alone}", "translateX", '30px', { fromValue: '26px'}], position: 122, duration: 46 },
            { id: "eid401", tween: [ "transform", "${_rocket_alone}", "translateX", '26px', { fromValue: '30px'}], position: 168, duration: 50 },
            { id: "eid412", tween: [ "transform", "${_rocket_alone}", "translateX", '30px', { fromValue: '26px'}], position: 219, duration: 40 },
            { id: "eid413", tween: [ "transform", "${_rocket_alone}", "translateX", '26px', { fromValue: '30px'}], position: 259, duration: 44 },
            { id: "eid415", tween: [ "transform", "${_rocket_alone}", "translateX", '30px', { fromValue: '26px'}], position: 303, duration: 56 },
            { id: "eid417", tween: [ "transform", "${_rocket_alone}", "translateX", '26px', { fromValue: '30px'}], position: 359, duration: 43 },
            { id: "eid419", tween: [ "transform", "${_rocket_alone}", "translateX", '30px', { fromValue: '26px'}], position: 402, duration: 40 },
            { id: "eid421", tween: [ "transform", "${_rocket_alone}", "translateX", '26px', { fromValue: '30px'}], position: 442, duration: 37 },
            { id: "eid423", tween: [ "transform", "${_rocket_alone}", "translateX", '28px', { fromValue: '26px'}], position: 479, duration: 21 }         ]
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
      rect: [58,-176,1236,720],
      id: 'labelsall',
      transform: [[-58,178]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/labelsall.png']
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_labelsall}": [
            ["transform", "translateX", '-58px'],
            ["transform", "translateY", '178px']
         ],
         "${symbolSelector}": [
            ["style", "height", '719px'],
            ["style", "width", '1234px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1500,
         autoPlay: false,
         labels: {

         },
         timeline: [
         ]
      }
   }
},
"labelsall1": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      id: 'labelsall',
      type: 'rect',
      rect: [2,1,0,0]
   }],
   symbolInstances: [
   {
      id: 'labelsall',
      symbolName: 'labelsall'
   }   ]
   },
   states: {
      "Base State": {
         "${symbolSelector}": [
            ["style", "height", '721.999992px'],
            ["style", "width", '1234px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1500,
         autoPlay: false,
         labels: {

         },
         timeline: [
         ]
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
})(jQuery, AdobeEdge, "EDGE-492073588");
