/**
 * Adobe Edge: symbol definitions
 */
(function($, Edge, compId){
//images folder
var im='images/';

var fonts = {};
   fonts["Miso"]='';


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
            id:'Entrance',
            type:'rect',
            rect:[-1247,-417,0,0]
         }],
         symbolInstances: [
         {
            id:'Entrance',
            symbolName:'Entrance'
         }
         ]
      },
   states: {
      "Base State": {
         "${_Stage}": [
            ["color", "background-color", 'rgba(203,230,244,1.00)'],
            ["style", "height", '1024px'],
            ["style", "width", '1436px']
         ],
         "body": [
            ["color", "background-color", 'rgba(0,0,0,0)']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 440,
         autoPlay: false,
         labels: {

         },
         timeline: [
         ]
      }
   }
},
"Entrance": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      rect: [1774,1253,257,103],
      id: 'littleworld',
      transform: [[58,196]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/littleworld.png']
   },
   {
      id: 'enlarge_icon',
      type: 'rect',
      rect: [-1,-1,0,0]
   },
   {
      id: 'enlarge_trolley',
      type: 'rect',
      rect: [609,309,0,0]
   },
   {
      id: 'enlarge_building',
      type: 'rect',
      rect: [223,130,0,0]
   },
   {
      id: 'enlarge_piggybank',
      type: 'rect',
      rect: [802,407,0,0]
   },
   {
      id: 'enlarge_rocket',
      type: 'rect',
      rect: [996,475,0,0]
   },
   {
      id: 'enlarge_books',
      type: 'rect',
      rect: [416,227,0,0]
   }],
   symbolInstances: [
   {
      id: 'enlarge_icon',
      symbolName: 'enlarge_icon'
   },
   {
      id: 'enlarge_trolley',
      symbolName: 'enlarge_trolley'
   },
   {
      id: 'enlarge_books',
      symbolName: 'enlarge_books'
   },
   {
      id: 'enlarge_piggybank',
      symbolName: 'enlarge_piggybank'
   },
   {
      id: 'enlarge_building',
      symbolName: 'enlarge_building'
   },
   {
      id: 'enlarge_rocket',
      symbolName: 'enlarge_rocket'
   }   ]
   },
   states: {
      "Base State": {
         "${_enlarge_books}": [
            ["transform", "translateX", '0px'],
            ["transform", "translateY", '0px']
         ],
         "${_enlarge_trolley}": [
            ["transform", "translateX", '0px'],
            ["transform", "translateY", '0px']
         ],
         "${_enlarge_rocket}": [
            ["transform", "translateX", '0px'],
            ["transform", "translateY", '0px']
         ],
         "${_enlarge_building}": [
            ["transform", "translateX", '0px'],
            ["transform", "translateY", '0px']
         ],
         "${_enlarge_piggybank}": [
            ["transform", "translateX", '0px'],
            ["transform", "translateY", '0px']
         ],
         "${_littleworld}": [
            ["transform", "translateX", '58px'],
            ["transform", "translateY", '196px']
         ],
         "${_enlarge_icon}": [
            ["transform", "translateX", '0px'],
            ["transform", "translateY", '0px']
         ],
         "${symbolSelector}": [
            ["style", "height", '1550.449978px'],
            ["style", "width", '2086.149978px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 1526.9885438562,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid280", tween: [ "transform", "${_enlarge_trolley}", "translateY", '647px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid293", tween: [ "transform", "${_enlarge_piggybank}", "translateY", '647px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid279", tween: [ "transform", "${_enlarge_trolley}", "translateX", '1358px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid305", tween: [ "transform", "${_enlarge_rocket}", "translateY", '646px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid267", tween: [ "transform", "${_enlarge_books}", "translateX", '1357px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid304", tween: [ "transform", "${_enlarge_rocket}", "translateX", '1357px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid219", tween: [ "transform", "${_littleworld}", "translateY", '86px', { fromValue: '196px'}], position: 0, duration: 1500 },
            { id: "eid253", tween: [ "transform", "${_enlarge_building}", "translateY", '634px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid252", tween: [ "transform", "${_enlarge_building}", "translateX", '1358px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid217", tween: [ "transform", "${_littleworld}", "translateX", '58px', { fromValue: '58px'}], position: 0, duration: 0 },
            { id: "eid215", tween: [ "transform", "${_littleworld}", "translateX", '58px', { fromValue: '58px'}], position: 1500, duration: 0 },
            { id: "eid234", tween: [ "transform", "${_enlarge_icon}", "translateX", '1395px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid292", tween: [ "transform", "${_enlarge_piggybank}", "translateX", '1357px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid866", tween: [ "transform", "${_enlarge_piggybank}", "translateX", '1356px', { fromValue: '1357px'}], position: 1500, duration: 26 },
            { id: "eid235", tween: [ "transform", "${_enlarge_icon}", "translateY", '647px', { fromValue: '0px'}], position: 0, duration: 1500 },
            { id: "eid268", tween: [ "transform", "${_enlarge_books}", "translateY", '647px', { fromValue: '0px'}], position: 0, duration: 1500 }         ]
      }
   }
},
"enlarge_icon": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      rect: [-65,364,149,91],
      id: 'bigshadow',
      transform: [[74,-210]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/bigshadow.png']
   },
   {
      rect: [2214,730,222,260],
      id: 'chipicon',
      transform: [[-876,-114]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/chipicon.png']
   },
   {
      rect: [-147,792,672,520],
      id: 'cornericon',
      transform: {},
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/cornericon.png']
   },
   {
      rect: [816,46,274,28],
      transform: [[48,-30]],
      align: 'auto',
      font: ['Miso',48,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_titulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,26]],
      align: 'auto',
      font: ['Miso',24,'rgba(195,44,185,1.00)','bold','none','normal'],
      id: 'texto_subtitulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,74]],
      align: 'auto',
      font: ['Miso',24,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_contenido',
      type: 'text'
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_texto_subtitulo}": [
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["transform", "translateX", '-96px'],
            ["style", "font-family", 'Miso'],
            ["color", "color", 'rgba(195,44,185,1.00)'],
            ["style", "font-weight", 'bold'],
            ["transform", "translateY", '-29px'],
            ["style", "width", '274px']
         ],
         "${_chipicon}": [
            ["transform", "scaleX", '0.77'],
            ["transform", "scaleY", '0.77'],
            ["transform", "translateY", '-759px'],
            ["transform", "translateX", '-2238px']
         ],
         "${_texto_titulo}": [
            ["style", "width", '274px'],
            ["transform", "translateX", '-96px'],
            ["style", "font-family", 'Miso'],
            ["style", "height", '28px'],
            ["style", "opacity", '0'],
            ["transform", "translateY", '-28px'],
            ["style", "font-size", '36px']
         ],
         "${symbolSelector}": [
            ["style", "height", '198.9px'],
            ["style", "width", '169.83px']
         ],
         "${_bigshadow}": [
            ["transform", "scaleX", '0.77'],
            ["transform", "translateY", '-215.999px'],
            ["transform", "translateX", '74px'],
            ["transform", "scaleY", '0.77']
         ],
         "${_texto_contenido}": [
            ["transform", "translateX", '-96px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-29px'],
            ["style", "width", '274px']
         ],
         "${_cornericon}": [
            ["transform", "translateX", '0'],
            ["transform", "translateY", '137px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 371,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid388", tween: [ "transform", "${_cornericon}", "translateX", '-6px', { fromValue: '0px'}], position: 0, duration: 9 },
            { id: "eid241", tween: [ "transform", "${_chipicon}", "scaleY", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid240", tween: [ "transform", "${_chipicon}", "scaleX", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid326", tween: [ "transform", "${_bigshadow}", "translateY", '-215.999px', { fromValue: '-215.999px'}], position: 0, duration: 0 },
            { id: "eid427", tween: [ "style", "${_texto_subtitulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid433", tween: [ "style", "${_texto_subtitulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid702", tween: [ "transform", "${_texto_contenido}", "translateY", '-29px', { fromValue: '-29px'}], position: 371, duration: 0 },
            { id: "eid311", tween: [ "transform", "${_bigshadow}", "scaleY", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid695", tween: [ "transform", "${_texto_subtitulo}", "translateX", '-96px', { fromValue: '-96px'}], position: 371, duration: 0 },
            { id: "eid696", tween: [ "transform", "${_texto_titulo}", "translateX", '-96px', { fromValue: '-96px'}], position: 371, duration: 0 },
            { id: "eid310", tween: [ "transform", "${_bigshadow}", "scaleX", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid428", tween: [ "style", "${_texto_titulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid434", tween: [ "style", "${_texto_titulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid684", tween: [ "transform", "${_texto_subtitulo}", "translateY", '-29px', { fromValue: '-29px'}], position: 371, duration: 0 },
            { id: "eid426", tween: [ "style", "${_texto_contenido}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid432", tween: [ "style", "${_texto_contenido}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid694", tween: [ "transform", "${_texto_contenido}", "translateX", '-96px', { fromValue: '-96px'}], position: 371, duration: 0 },
            { id: "eid379", tween: [ "transform", "${_cornericon}", "translateY", '-26.913px', { fromValue: '137px'}], position: 0, duration: 9 },
            { id: "eid389", tween: [ "transform", "${_cornericon}", "translateY", '-519px', { fromValue: '-26.913px'}], position: 9, duration: 362 },
            { id: "eid697", tween: [ "transform", "${_texto_titulo}", "translateY", '-28px', { fromValue: '-28px'}], position: 371, duration: 0 }         ]
      }
   }
},
"enlarge_building": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      rect: [-168,243,149,91],
      id: 'bigshadow2',
      transform: [[179,-89]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/bigshadow.png']
   },
   {
      rect: [2147,690,222,260],
      id: 'chipbuilding',
      transform: [[-816,-87],{},{},[0.7657,0.7657]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/chipbuilding.png']
   },
   {
      id: 'cornerbuilding',
      type: 'image',
      rect: [-329,678,672,520],
      fill: ['rgba(0,0,0,0)','images/cornerbuilding.png']
   },
   {
      rect: [816,46,274,28],
      transform: [[48,-30]],
      align: 'auto',
      font: ['Miso',48,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_titulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,26]],
      align: 'auto',
      font: ['Miso',24,'rgba(195,44,185,1.00)','bold','none','normal'],
      id: 'texto_subtitulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,74]],
      align: 'auto',
      font: ['Miso',24,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_contenido',
      type: 'text'
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_texto_contenido}": [
            ["transform", "translateX", '-281px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-145.999px'],
            ["style", "width", '274px']
         ],
         "${_texto_subtitulo}": [
            ["style", "opacity", '0'],
            ["color", "color", 'rgba(195,44,185,1.00)'],
            ["transform", "translateX", '-281px'],
            ["style", "font-weight", 'bold'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-145px'],
            ["style", "width", '274px']
         ],
         "${_cornerbuilding}": [
            ["transform", "translateX", '0'],
            ["transform", "translateY", '132px']
         ],
         "${_texto_titulo}": [
            ["style", "font-size", '36px'],
            ["transform", "translateX", '-281px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-145px'],
            ["style", "width", '274px']
         ],
         "${symbolSelector}": [
            ["style", "height", '198.9px'],
            ["style", "width", '169.83px']
         ],
         "${_chipbuilding}": [
            ["transform", "scaleX", '0.765'],
            ["transform", "scaleY", '0.765'],
            ["transform", "translateY", '-719px'],
            ["transform", "translateX", '-2171px']
         ],
         "${_bigshadow2}": [
            ["transform", "scaleX", '0.77'],
            ["transform", "translateY", '-108.573px'],
            ["transform", "translateX", '179.824px'],
            ["transform", "scaleY", '0.77']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 371,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid318", tween: [ "transform", "${_bigshadow2}", "scaleX", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid248", tween: [ "transform", "${_chipbuilding}", "scaleX", '1', { fromValue: '0.765'}], position: 0, duration: 371 },
            { id: "eid440", tween: [ "style", "${_texto_subtitulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid442", tween: [ "style", "${_texto_subtitulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid382", tween: [ "transform", "${_cornerbuilding}", "translateX", '0', { fromValue: '0'}], position: 0, duration: 0 },
            { id: "eid385", tween: [ "transform", "${_cornerbuilding}", "translateX", '0', { fromValue: '0'}], position: 9, duration: 0 },
            { id: "eid380", tween: [ "transform", "${_cornerbuilding}", "translateX", '0', { fromValue: '0'}], position: 371, duration: 0 },
            { id: "eid384", tween: [ "transform", "${_cornerbuilding}", "translateY", '-2.841px', { fromValue: '132px'}], position: 0, duration: 9 },
            { id: "eid387", tween: [ "transform", "${_cornerbuilding}", "translateY", '-521px', { fromValue: '-2.841px'}], position: 9, duration: 362 },
            { id: "eid249", tween: [ "transform", "${_chipbuilding}", "scaleY", '1', { fromValue: '0.765'}], position: 0, duration: 371 },
            { id: "eid437", tween: [ "style", "${_texto_contenido}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid439", tween: [ "style", "${_texto_contenido}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid443", tween: [ "style", "${_texto_titulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid444", tween: [ "style", "${_texto_titulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid319", tween: [ "transform", "${_bigshadow2}", "scaleY", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid329", tween: [ "transform", "${_bigshadow2}", "translateY", '-108.573px', { fromValue: '-108.573px'}], position: 0, duration: 0 }         ]
      }
   }
},
"enlarge_books": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      id: 'bigshadow3',
      type: 'image',
      rect: [-302,138,149,91],
      fill: ['rgba(0,0,0,0)','images/bigshadow.png']
   },
   {
      rect: [950,535,222,260],
      id: 'chipbooks',
      transform: [[380,80],{},{},[0.7657,0.7657]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/chipbooks.png']
   },
   {
      rect: [-521,684,672,520],
      id: 'cornerbooks',
      transform: [[0,33]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/cornerbooks.png']
   },
   {
      rect: [816,46,274,28],
      transform: [[48,-30]],
      align: 'auto',
      font: ['Miso',48,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_titulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,26]],
      align: 'auto',
      font: ['Miso',24,'rgba(195,44,185,1.00)','bold','none','normal'],
      id: 'texto_subtitulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,74]],
      align: 'auto',
      font: ['Miso',24,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_contenido',
      type: 'text'
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_cornerbooks}": [
            ["transform", "translateY", '33px'],
            ["transform", "translateX", '-5px']
         ],
         "${_texto_contenido}": [
            ["color", "color", 'rgba(77,77,77,1.00)'],
            ["transform", "translateX", '-475.999px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-253px'],
            ["style", "width", '274px']
         ],
         "${symbolSelector}": [
            ["style", "height", '198.9px'],
            ["style", "width", '169.83px']
         ],
         "${_bigshadow3}": [
            ["transform", "scaleX", '0.77'],
            ["transform", "scaleY", '0.77'],
            ["transform", "translateX", '312.888px'],
            ["transform", "translateY", '-15.033px']
         ],
         "${_chipbooks}": [
            ["transform", "scaleX", '0.765'],
            ["transform", "scaleY", '0.765'],
            ["transform", "translateY", '-564px'],
            ["transform", "translateX", '-974px']
         ],
         "${_texto_subtitulo}": [
            ["style", "opacity", '0'],
            ["color", "color", 'rgba(195,44,185,1.00)'],
            ["transform", "translateX", '-475.999px'],
            ["style", "font-weight", 'bold'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-253px'],
            ["style", "width", '274px']
         ],
         "${_texto_titulo}": [
            ["style", "font-size", '36px'],
            ["color", "color", 'rgba(77,77,77,1.00)'],
            ["transform", "translateX", '-476px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-253px'],
            ["style", "width", '274px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 371,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid391", tween: [ "transform", "${_cornerbooks}", "translateY", '-191.448px', { fromValue: '33px'}], position: 0, duration: 8 },
            { id: "eid396", tween: [ "transform", "${_cornerbooks}", "translateY", '-637px', { fromValue: '-191.448px'}], position: 8, duration: 363 },
            { id: "eid336", tween: [ "transform", "${_bigshadow3}", "translateX", '312.888px', { fromValue: '312.888px'}], position: 0, duration: 0 },
            { id: "eid457", tween: [ "style", "${_texto_subtitulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid459", tween: [ "style", "${_texto_subtitulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid859", tween: [ "transform", "${_texto_contenido}", "translateY", '-253px', { fromValue: '-253px'}], position: 371, duration: 0 },
            { id: "eid857", tween: [ "transform", "${_texto_titulo}", "translateY", '-253px', { fromValue: '-253px'}], position: 371, duration: 0 },
            { id: "eid858", tween: [ "transform", "${_texto_subtitulo}", "translateY", '-253px', { fromValue: '-253px'}], position: 371, duration: 0 },
            { id: "eid335", tween: [ "transform", "${_bigshadow3}", "scaleY", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid264", tween: [ "transform", "${_chipbooks}", "scaleY", '1', { fromValue: '0.765'}], position: 0, duration: 371 },
            { id: "eid334", tween: [ "transform", "${_bigshadow3}", "scaleX", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid863", tween: [ "transform", "${_texto_titulo}", "translateX", '-476px', { fromValue: '-476px'}], position: 371, duration: 0 },
            { id: "eid339", tween: [ "transform", "${_bigshadow3}", "translateY", '-15.033px', { fromValue: '-15.033px'}], position: 0, duration: 0 },
            { id: "eid460", tween: [ "style", "${_texto_titulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid461", tween: [ "style", "${_texto_titulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid395", tween: [ "transform", "${_cornerbooks}", "translateX", '-5px', { fromValue: '-5px'}], position: 371, duration: 0 },
            { id: "eid454", tween: [ "style", "${_texto_contenido}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid456", tween: [ "style", "${_texto_contenido}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid865", tween: [ "transform", "${_texto_contenido}", "translateX", '-475.999px', { fromValue: '-475.999px'}], position: 371, duration: 0 },
            { id: "eid263", tween: [ "transform", "${_chipbooks}", "scaleX", '1', { fromValue: '0.765'}], position: 0, duration: 371 },
            { id: "eid864", tween: [ "transform", "${_texto_subtitulo}", "translateX", '-475.999px', { fromValue: '-475.999px'}], position: 371, duration: 0 }         ]
      }
   }
},
"enlarge_trolley": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      id: 'bigshadow4',
      type: 'image',
      rect: [-13,260,149,91],
      fill: ['rgba(0,0,0,0)','images/bigshadow.png']
   },
   {
      rect: [1565,865,222,260],
      id: 'chiptrolley',
      transform: [[-234,-249],{},{},[0.7657,0.7657]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/chiptrolley.png']
   },
   {
      rect: [-693,559,672,520],
      id: 'cornertrolley',
      transform: [[-26,-587]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/cornertrolley.png']
   },
   {
      rect: [816,46,274,28],
      transform: [[48,-30]],
      align: 'auto',
      font: ['Miso',48,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_titulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,26]],
      align: 'auto',
      font: ['Miso',24,'rgba(195,44,185,1.00)','bold','none','normal'],
      id: 'texto_subtitulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,74]],
      align: 'auto',
      font: ['Miso',24,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_contenido',
      type: 'text'
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_texto_contenido}": [
            ["transform", "translateX", '-665px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-336.999px'],
            ["style", "width", '274px']
         ],
         "${_chiptrolley}": [
            ["transform", "scaleX", '0.77'],
            ["transform", "scaleY", '0.77'],
            ["transform", "translateY", '-894px'],
            ["transform", "translateX", '-1589px']
         ],
         "${symbolSelector}": [
            ["style", "height", '198.9px'],
            ["style", "width", '169.83px']
         ],
         "${_bigshadow4}": [
            ["transform", "scaleX", '0.77'],
            ["transform", "scaleY", '0.77'],
            ["transform", "translateX", '22.694px'],
            ["transform", "translateY", '-125.641px']
         ],
         "${_texto_titulo}": [
            ["style", "font-size", '36px'],
            ["transform", "translateX", '-665px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-336.999px'],
            ["style", "width", '274px']
         ],
         "${_texto_subtitulo}": [
            ["style", "opacity", '0'],
            ["color", "color", 'rgba(195,44,185,1.00)'],
            ["transform", "translateX", '-665px'],
            ["style", "font-weight", 'bold'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-336px'],
            ["style", "width", '274px']
         ],
         "${_cornertrolley}": [
            ["transform", "translateY", '53px'],
            ["transform", "translateX", '-26px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 371,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid344", tween: [ "transform", "${_bigshadow4}", "scaleX", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid840", tween: [ "transform", "${_texto_subtitulo}", "translateY", '-336px', { fromValue: '-336px'}], position: 371, duration: 0 },
            { id: "eid346", tween: [ "transform", "${_bigshadow4}", "translateX", '22.694px', { fromValue: '22.694px'}], position: 0, duration: 0 },
            { id: "eid276", tween: [ "transform", "${_chiptrolley}", "scaleY", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid471", tween: [ "style", "${_texto_subtitulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid473", tween: [ "style", "${_texto_subtitulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid842", tween: [ "transform", "${_texto_contenido}", "translateY", '-336.999px', { fromValue: '-336.999px'}], position: 371, duration: 0 },
            { id: "eid275", tween: [ "transform", "${_chiptrolley}", "scaleX", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid849", tween: [ "transform", "${_texto_subtitulo}", "translateX", '-665px', { fromValue: '-665px'}], position: 371, duration: 0 },
            { id: "eid850", tween: [ "transform", "${_texto_titulo}", "translateX", '-665px', { fromValue: '-665px'}], position: 371, duration: 0 },
            { id: "eid345", tween: [ "transform", "${_bigshadow4}", "scaleY", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid474", tween: [ "style", "${_texto_titulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid475", tween: [ "style", "${_texto_titulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid399", tween: [ "transform", "${_cornertrolley}", "translateY", '-170.801px', { fromValue: '53px'}], position: 0, duration: 8 },
            { id: "eid400", tween: [ "transform", "${_cornertrolley}", "translateY", '-587px', { fromValue: '-170.801px'}], position: 8, duration: 363 },
            { id: "eid468", tween: [ "style", "${_texto_contenido}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid470", tween: [ "style", "${_texto_contenido}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid851", tween: [ "transform", "${_texto_contenido}", "translateX", '-665px', { fromValue: '-665px'}], position: 371, duration: 0 },
            { id: "eid351", tween: [ "transform", "${_bigshadow4}", "translateY", '-125.641px', { fromValue: '-125.641px'}], position: 0, duration: 0 },
            { id: "eid841", tween: [ "transform", "${_texto_titulo}", "translateY", '-336.999px', { fromValue: '-336.999px'}], position: 371, duration: 0 }         ]
      }
   }
},
"enlarge_piggybank": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      id: 'bigshadow5',
      type: 'image',
      rect: [-280,165,149,91],
      fill: ['rgba(0,0,0,0)','images/bigshadow.png']
   },
   {
      rect: [662,767,222,260],
      id: 'chippiggybank',
      transform: [[668,-151],{},{},[0.7657,0.7657]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/chippigybank.png']
   },
   {
      rect: [-659,590,672,520],
      id: 'cornerpiggybank',
      transform: [[-252,-76]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/cornerpiggybank.png']
   },
   {
      rect: [816,46,274,28],
      transform: [[48,-30]],
      align: 'auto',
      font: ['Miso',48,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_titulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,26]],
      align: 'auto',
      font: ['Miso',24,'rgba(195,44,185,1.00)','bold','none','normal'],
      id: 'texto_subtitulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,74]],
      align: 'auto',
      font: ['Miso',24,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_contenido',
      type: 'text'
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_texto_contenido}": [
            ["transform", "translateX", '-858.999px'],
            ["style", "opacity", '0.0011370000429451'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-432px'],
            ["style", "width", '274px']
         ],
         "${symbolSelector}": [
            ["style", "height", '198.9px'],
            ["style", "width", '169.83px']
         ],
         "${_texto_titulo}": [
            ["style", "font-size", '36px'],
            ["transform", "translateX", '-858.999px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-432px'],
            ["style", "width", '274px']
         ],
         "${_chippiggybank}": [
            ["transform", "scaleX", '0.77'],
            ["transform", "scaleY", '0.77'],
            ["transform", "translateY", '-796px'],
            ["transform", "translateX", '-686px']
         ],
         "${_bigshadow5}": [
            ["transform", "scaleX", '0.77'],
            ["transform", "scaleY", '1'],
            ["transform", "translateX", '289.898px'],
            ["transform", "translateY", '-34.092px']
         ],
         "${_texto_subtitulo}": [
            ["style", "opacity", '0'],
            ["color", "color", 'rgba(195,44,185,1.00)'],
            ["transform", "translateX", '-858.999px'],
            ["style", "font-weight", 'bold'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-432px'],
            ["style", "width", '274px']
         ],
         "${_cornerpiggybank}": [
            ["transform", "translateX", '-252px'],
            ["transform", "translateY", '-76px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 371,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid291", tween: [ "transform", "${_chippiggybank}", "scaleX", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid485", tween: [ "style", "${_texto_subtitulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid487", tween: [ "style", "${_texto_subtitulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid756", tween: [ "transform", "${_texto_contenido}", "translateY", '-432px', { fromValue: '-432px'}], position: 371, duration: 0 },
            { id: "eid401", tween: [ "transform", "${_cornerpiggybank}", "translateX", '-252px', { fromValue: '-252px'}], position: 0, duration: 0 },
            { id: "eid407", tween: [ "transform", "${_cornerpiggybank}", "translateX", '-252px', { fromValue: '-252px'}], position: 8, duration: 0 },
            { id: "eid403", tween: [ "transform", "${_cornerpiggybank}", "translateX", '-252px', { fromValue: '-252px'}], position: 371, duration: 0 },
            { id: "eid757", tween: [ "transform", "${_texto_titulo}", "translateY", '-432px', { fromValue: '-432px'}], position: 371, duration: 0 },
            { id: "eid376", tween: [ "transform", "${_bigshadow5}", "translateY", '-34.092px', { fromValue: '-34.092px'}], position: 0, duration: 0 },
            { id: "eid288", tween: [ "transform", "${_chippiggybank}", "scaleY", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid355", tween: [ "transform", "${_bigshadow5}", "scaleY", '1', { fromValue: '1'}], position: 0, duration: 0 },
            { id: "eid353", tween: [ "transform", "${_bigshadow5}", "scaleY", '1', { fromValue: '1'}], position: 371, duration: 0 },
            { id: "eid770", tween: [ "transform", "${_texto_subtitulo}", "translateX", '-858.999px', { fromValue: '-858.999px'}], position: 371, duration: 0 },
            { id: "eid769", tween: [ "transform", "${_texto_titulo}", "translateX", '-858.999px', { fromValue: '-858.999px'}], position: 371, duration: 0 },
            { id: "eid758", tween: [ "transform", "${_texto_subtitulo}", "translateY", '-432px', { fromValue: '-432px'}], position: 371, duration: 0 },
            { id: "eid488", tween: [ "style", "${_texto_titulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid489", tween: [ "style", "${_texto_titulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid405", tween: [ "transform", "${_cornerpiggybank}", "translateY", '-329.8px', { fromValue: '-76px'}], position: 0, duration: 8 },
            { id: "eid406", tween: [ "transform", "${_cornerpiggybank}", "translateY", '-716px', { fromValue: '-329.8px'}], position: 8, duration: 363 },
            { id: "eid482", tween: [ "style", "${_texto_contenido}", "opacity", '0.3', { fromValue: '0.0011370000429451'}], position: 0, duration: 250 },
            { id: "eid484", tween: [ "style", "${_texto_contenido}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid768", tween: [ "transform", "${_texto_contenido}", "translateX", '-858.999px', { fromValue: '-858.999px'}], position: 371, duration: 0 },
            { id: "eid362", tween: [ "transform", "${_bigshadow5}", "translateX", '289.898px', { fromValue: '289.898px'}], position: 0, duration: 0 },
            { id: "eid356", tween: [ "transform", "${_bigshadow5}", "scaleX", '1', { fromValue: '0.77'}], position: 0, duration: 371 }         ]
      }
   }
},
"enlarge_rocket": {
   version: "0.1.5",
   build: "0.9.0.113",
   baseState: "Base State",
   initialState: "Base State",
   gpuAccelerate: true,
   content: {
   dom: [
   {
      id: 'bigshadow6',
      type: 'image',
      rect: [-157,210,149,91],
      fill: ['rgba(0,0,0,0)','images/bigshadow.png']
   },
   {
      rect: [750,569,222,260],
      id: 'chiprocket',
      transform: [[580,45],{},{},[0.7657,0.7657]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/chiprocket.png']
   },
   {
      rect: [-1091,498,672,520],
      id: 'cornerrocket',
      transform: [[-16,-56]],
      type: 'image',
      fill: ['rgba(0,0,0,0)','images/cornerrocket.png']
   },
   {
      rect: [816,46,274,28],
      transform: [[48,-30]],
      align: 'auto',
      font: ['Miso',48,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_titulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,26]],
      align: 'auto',
      font: ['Miso',24,'rgba(195,44,185,1.00)','bold','none','normal'],
      id: 'texto_subtitulo',
      type: 'text'
   },
   {
      rect: [816,46,274,28],
      transform: [[61,74]],
      align: 'auto',
      font: ['Miso',24,'rgba(0,0,0,1)','normal','none','normal'],
      id: 'texto_contenido',
      type: 'text'
   }],
   symbolInstances: [
   ]
   },
   states: {
      "Base State": {
         "${_texto_subtitulo}": [
            ["style", "opacity", '0'],
            ["color", "color", 'rgba(195,44,185,1.00)'],
            ["transform", "translateX", '-1051px'],
            ["style", "font-weight", 'bold'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-503.999px'],
            ["style", "width", '274px']
         ],
         "${_chiprocket}": [
            ["transform", "scaleX", '0.765'],
            ["transform", "scaleY", '0.765'],
            ["transform", "translateY", '-598.449px'],
            ["transform", "translateX", '-773.914px']
         ],
         "${symbolSelector}": [
            ["style", "height", '198.9px'],
            ["style", "width", '169.83px']
         ],
         "${_texto_contenido}": [
            ["transform", "translateX", '-1051px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-503.999px'],
            ["style", "width", '274px']
         ],
         "${_bigshadow6}": [
            ["transform", "scaleX", '0.77'],
            ["transform", "scaleY", '0.77'],
            ["transform", "translateX", '169.212px'],
            ["transform", "translateY", '-49.453px']
         ],
         "${_cornerrocket}": [
            ["transform", "translateX", '-16px'],
            ["transform", "translateY", '-56px']
         ],
         "${_texto_titulo}": [
            ["style", "font-size", '36px'],
            ["transform", "translateX", '-1051.999px'],
            ["style", "opacity", '0'],
            ["style", "height", '28px'],
            ["style", "font-family", 'Miso'],
            ["transform", "translateY", '-503px'],
            ["style", "width", '274px']
         ]
      }
   },
   timelines: {
      "Default Timeline": {
         fromState: "Base State",
         toState: "",
         duration: 371,
         autoPlay: false,
         labels: {

         },
         timeline: [
            { id: "eid372", tween: [ "transform", "${_bigshadow6}", "translateX", '169.212px', { fromValue: '169.212px'}], position: 0, duration: 0 },
            { id: "eid499", tween: [ "style", "${_texto_subtitulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid501", tween: [ "style", "${_texto_subtitulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid739", tween: [ "transform", "${_texto_contenido}", "translateY", '-503.999px', { fromValue: '-503.999px'}], position: 371, duration: 0 },
            { id: "eid740", tween: [ "transform", "${_texto_titulo}", "translateY", '-503px', { fromValue: '-503px'}], position: 371, duration: 0 },
            { id: "eid414", tween: [ "transform", "${_cornerrocket}", "translateY", '-191.525px', { fromValue: '-56px'}], position: 0, duration: 9 },
            { id: "eid417", tween: [ "transform", "${_cornerrocket}", "translateY", '-696px', { fromValue: '-191.525px'}], position: 9, duration: 362 },
            { id: "eid371", tween: [ "transform", "${_bigshadow6}", "scaleY", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid374", tween: [ "transform", "${_bigshadow6}", "translateY", '-49.453px', { fromValue: '-49.453px'}], position: 0, duration: 0 },
            { id: "eid300", tween: [ "transform", "${_chiprocket}", "scaleX", '1', { fromValue: '0.765'}], position: 0, duration: 371 },
            { id: "eid726", tween: [ "transform", "${_texto_subtitulo}", "translateX", '-1051px', { fromValue: '-1051px'}], position: 371, duration: 0 },
            { id: "eid725", tween: [ "transform", "${_texto_titulo}", "translateX", '-1051.999px', { fromValue: '-1051.999px'}], position: 371, duration: 0 },
            { id: "eid502", tween: [ "style", "${_texto_titulo}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid503", tween: [ "style", "${_texto_titulo}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid370", tween: [ "transform", "${_bigshadow6}", "scaleX", '1', { fromValue: '0.77'}], position: 0, duration: 371 },
            { id: "eid496", tween: [ "style", "${_texto_contenido}", "opacity", '0.3', { fromValue: '0'}], position: 0, duration: 250 },
            { id: "eid498", tween: [ "style", "${_texto_contenido}", "opacity", '1', { fromValue: '0.3'}], position: 250, duration: 121 },
            { id: "eid412", tween: [ "transform", "${_cornerrocket}", "translateX", '-16px', { fromValue: '-16px'}], position: 0, duration: 0 },
            { id: "eid416", tween: [ "transform", "${_cornerrocket}", "translateX", '-16px', { fromValue: '-16px'}], position: 9, duration: 0 },
            { id: "eid410", tween: [ "transform", "${_cornerrocket}", "translateX", '-16px', { fromValue: '-16px'}], position: 371, duration: 0 },
            { id: "eid724", tween: [ "transform", "${_texto_contenido}", "translateX", '-1051px', { fromValue: '-1051px'}], position: 371, duration: 0 },
            { id: "eid301", tween: [ "transform", "${_chiprocket}", "scaleY", '1', { fromValue: '0.765'}], position: 0, duration: 371 },
            { id: "eid741", tween: [ "transform", "${_texto_subtitulo}", "translateY", '-503.999px', { fromValue: '-503.999px'}], position: 371, duration: 0 }         ]
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
})(jQuery, AdobeEdge, "EDGE-112784366");
