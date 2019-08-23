/**
* Función que calcula el tamaño de la ventana
*/
window.size = function(){
   var w = 0;
   var h = 0;
   //IE
   if(!window.innerWidth){
      //strict mode
      if(!(document.documentElement.clientWidth == 0)){
         w = document.documentElement.clientWidth;
         h = document.documentElement.clientHeight;
      }
      //quirks mode
      else{
         w = document.body.clientWidth;
         h = document.body.clientHeight;
      }
   }
   //w3c
   else{
      w = window.innerWidth;
      h = window.innerHeight;
   }
   return {width:w,height:h};
}

/**
* Función que calcula el centro de la ventana actual o de un rectángulo de
* las dimensiones que se le pasen, devuelve el vértice superior izquierdo
*/
window.center = function(){
   var hWnd = (arguments[0] != null) ? arguments[0] : {width:0,height:0,divide:2};
   var _x = 0;
   var _y = 0;
   var offsetX = 0;
   var offsetY = 0;
   //IE
   if(!window.pageYOffset){
      //strict mode
      if(!(document.documentElement.scrollTop == 0)){
         offsetY = document.documentElement.scrollTop;
         offsetX = document.documentElement.scrollLeft;
      }
      //quirks mode
      else{
         offsetY = document.body.scrollTop;
         offsetX = document.body.scrollLeft;
      }
   }
   //w3c
   else{
      offsetX = window.pageXOffset;
      offsetY = window.pageYOffset;
   }
   
   if(hWnd.divide==1)
   {
	 	_x = ((this.size().width-hWnd.width)/2)+offsetX;
	    _y = ((this.size().height-hWnd.height)/2)+offsetY;
	   return{x:_x,y:_y};
   }
   else
   {
	   _x = offsetX;
	    _y = offsetY;
	   return{x:_x,y:_y};
   }
}

/** funcion que oculta todos los campos select de una pagina */
function ocultarSelect() {
	var i;
	//Para poder acceder a los elementos que se encuenta en el iframe
	ventana = document.body; 
	//Recorremos todos los campos que sean del tipo select
	sel = ventana.document.getElementsByTagName("SELECT")
	for(i=0;i<sel.length;i++)
	{
		// se muestra todos los combos
		sel[i].style.display = "none";
	}
}


/** funcion que muestra todos los campos select de una pagina */
function mostrarSelect() {
	var i;
	
	//Para poder acceder a los elementos que se encuenta en el iframe
	ventana = document.body; 
	//Recorremos todos los campos que sean del tipo select
	sel = ventana.document.getElementsByTagName("SELECT")
	for(i=0;i<sel.length;i++)
	{
		sel[i].style.display = "block";
	}
}


/** funcion que abre la ventana modal de espera */
function openLightBox() {
	if(navigator.appName.indexOf("Explorer") > -1 && navigator.appVersion.split(";")[1].indexOf("MSIE 6.")>-1)
 	{ 
 		document.getElementsByTagName("html")[0].style.overflow = "hidden";

 		ocultarSelect();

	   document.getElementById('lightbox_fondo').style.width=document.documentElement.clientWidth+'px';
	   document.getElementById('lightbox_fondo').style.height=document.documentElement.clientHeight+'px';
	   document.getElementById('lightbox_fondo').style.position='absolute';

	   document.getElementById('lightbox_contenedor').style.width='285px';
	   document.getElementById('lightbox_contenedor').style.height='103px';
	   document.getElementById('lightbox_contenedor').style.position='absolute';
	
		var punto = window.center({width:0,height:0,divide:0});
		var miCapa = document.getElementById('lightbox_fondo');

        miCapa.style.top = punto.y + "px";
        miCapa.style.left = punto.x + "px";
        
        var punto2 = window.center({width:285,height:103,divide:1});
		var miCapa2 = document.getElementById('lightbox_contenedor');

        miCapa2.style.top = punto2.y + "px";
        miCapa2.style.left = punto2.x + "px";
 	}
 	if(navigator.appName.indexOf("Explorer") > -1)
 	{
 		document.getElementById('lightBox_imagen').className='lightbox_noVisible';
 	}
	document.getElementById('lightbox_panel').className='lightbox_visible';
}
	
/** funcion que cierra la ventana modal de espera */
function closeLightBox() {
	document.getElementById('lightbox_panel').className='lightbox_noVisible';
	if(navigator.appName.indexOf("Explorer") > -1 && navigator.appVersion.split(";")[1].indexOf("MSIE 6.")>-1)
 	{ 
 		document.getElementsByTagName("html")[0].style.overflow = "scroll";            
        mostrarSelect();    
 	}
}
