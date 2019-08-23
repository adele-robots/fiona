// Global variables
var conteo = 90;
var primer_conteo = false;
var segundo_conteo = false;
var movingLips=false;
var alfa=0.0;
var animationLaunched = false;
var firstMove = false;
var secondMove = false;
var ojos_abiertos = true;
var tiempo_cerrados = new Date();
var aleatorio= Math.floor((Math.random()*60)+30);
var abriendo = false;
var abriendo_nivel = 0;
var cerrando = false;
var cerrando_nivel = 0;
var centro_a_oeste = false;
var oeste_a_centro = false;
var centro_a_este = false;
var este_a_centro = false;
var centro_a_norte = false;
var norte_a_centro = false;
var centro_a_sur = false;
var sur_a_centro = false;
var contador_rotacion_x = 0;
var contador_rotacion_y = 0;
var aleatorio_giro= Math.floor((Math.random()*6)+2);

function Initialize() {
	/* Auxiliar built-in functions */
	//log("Logged message",1); //1 - INFO, 2 - WARN, 3 - ERROR

	/* ICamera calls */
	//setCameraPositionRequired(component,0.63,166,50);
	//setCameraRotationRequired(component,10.1,4.2,4.3);
	//setCameraParametersRequired(component,false,45, 80,100);

	/* IFaceExpression call */
	//setFaceExpressionRequired(component,"eyes-closed_ShapeKey", 0);


	/* IFlow<char*> call */
	//processDataRequired(component,"hello everybody, how do you do?");

	/* INeck call */
   rotateHeadRequired(component,0,0);
  

	/* IEyes call */
	rotateEyeRequired(component,0,-10);
	

	/* IAnimation call */
	//playAnimationRequired(component,"mi-mocap.anim");

	/* IControlVoice calls */
	//startSpeakingRequired(component);
	//stopSpeakingRequired(component);
	//startVoiceRequired(component);

}

// FrameEventSubscriber implementation example. This method is called from 
// Character3DSpark with every new rendered frame
function notifyFrameEvent(){

/* VoiceStartSpark functionality implemented */
	/*if (movingLips){		
		setFaceExpressionRequired(component,"vis-eh_ShapeKey",alfa);
		if (alfa == 0.0){
			alfa=0.5;
		}
		else if (alfa == 0.5){
			alfa=1;
		}
		else if (alfa == 1){
			alfa=0;
		}
	}
	else{
		setFaceExpressionRequired(component,"vis-eh_ShapeKey",0.0);
	}*/
/* END ** VoiceStartSpark functionality implemented */

  //manageNostrils()
	//checkEyesBlinking()
	
	moveNeck();
	checkEyes();
	
	conteo = conteo + 1;
	if(conteo>100){
		conteo = 0;
	}
	/*if(conteo>350){
		if(!primer_conteo){
			//processDataRequired(component,"hello everybody, how do you do?");
			//primer_conteo = true;
		}
	}	
*/
/* Basic example to check animations */
	//conteo = conteo + 0.2;

		/*if(!animationLaunched){
			playAnimationRequired(component,"mi-mocap.anim");
			animationLaunched = true;
		}
		else 
   if(conteo>100){
		 if(!firstMove){
				print("1");
				rotateHeadRequired(component,0,50);
				firstMove = true;
				conteo = 0;
			}
			else if(!secondMove){
				print("2");
				rotateHeadRequired(component,20,0);			
				secondMove = true;
				conteo = 0;
			}
			else{
				print("3");
				rotateHeadRequired(component,0,-50);
				firstMove = false;
				secondMove = false;
				conteo = 0;
			}
		}
	*}
	else
		setCameraPositionRequired(component,0,150,conteo+30);
*/
/* END ** Basic example to check animations */
}
/********************************************************************************************************************/
/****************************************************** BLINK *******************************************************/
/********************************************************************************************************************/

/***********Basic eye blinking functionality with conteo **********/
function checkEyesBlinking()
{
	
	if (conteo % aleatorio == 0)
	{
		/*setFaceExpressionRequired(component,"BLINK", 0.8);*/
		cerrando = true;
		abriendo_nivel = 0.8;
	}
	if(cerrando)
	{
		setFaceExpressionRequired(component,"BLINK", cerrando_nivel);
		cerrando_nivel = cerrando_nivel+0.2;
	}
	if(cerrando_nivel == 0.8)
	{
		abriendo = true;
		cerrando = false;
	}
	if(abriendo)
	{
		  abriendo_nivel = abriendo_nivel-0.2;
			setFaceExpressionRequired(component,"BLINK", abriendo_nivel);
	}
	if(abriendo_nivel == 0)
	{
		abriendo = false;
		aleatorio= Math.floor((Math.random()*60)+30);
	}
	
}
/********  END ***Basic eye blinking functionality with conteo **********/

/***********Basic eye blinking functionality with sleep ***********
function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 10e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}
var aleatorio= Math.floor((Math.random()*7)+4);
function checkEyesBlinking()
{
	var d = new Date();
	print(d.getSeconds());
	
	if((d.getSeconds())%aleatorio==0){
		setFaceExpressionRequired(component,"BLINK", 0.8);
		sleep(1000);
		setFaceExpressionRequired(component,"BLINK", 0);
		setFaceExpressionRequired(component,"BLINK", 0.8);
		aleatorio= Math.floor((Math.random()*7)+4);
	}
	else{
		setFaceExpressionRequired(component,"BLINK", 0);
				
	}

}
/*********** END Basic eye blinking functionality with sleep ***********

/********************************************************************************************************************/
/****************************************************** NECK *******************************************************/
/********************************************************************************************************************/

function moveNeck ()
{
		if(!centro_a_oeste && !oeste_a_centro && !centro_a_este && !este_a_centro && !centro_a_norte &&	!norte_a_centro && !centro_a_sur && !sur_a_centro)
		{
	
			if(conteo%4 == 0)
			{
				centro_a_oeste = true;
				//ramdom de hasta donde gira
			   aleatorio_giro= Math.floor((Math.random()*6)+4);
			}
			if(conteo%4 == 1)
			{
				centro_a_este = true;
				//ramdom de hasta donde gira
			  aleatorio_giro= Math.floor((Math.random()*6)+4);
			}
			if(conteo%4 == 2)
			{
				centro_a_norte = true;
				//ramdom de hasta donde gira
				aleatorio_giro= Math.floor((Math.random()*2)+1);
			}
			if(conteo%4 == 3)
			{
				centro_a_sur = true;
				//ramdom de hasta donde gira
				aleatorio_giro= Math.floor((Math.random()*2)+1);
			}
	
			
		}
		// movimiento ligero hacia la izquierda
		if(centro_a_oeste)
		{
			contador_rotacion_x = contador_rotacion_x + 1;
			rotateHeadRequired(component,contador_rotacion_x/5,0);	
			if(contador_rotacion_x/5 == aleatorio_giro)
			{
				//secondMove = true;
				centro_a_oeste = false;
				oeste_a_centro = true;
			}
			
		}
		//vuelta al centro desde la izquierda
		if(oeste_a_centro)
		{
			contador_rotacion_x= contador_rotacion_x - 1;
			rotateHeadRequired(component,contador_rotacion_x/5,0);	
			if(contador_rotacion_x/5 == 0)
			{
				centro_a_oeste = false;
				oeste_a_centro = false;
			}
			
		}
		//movimiento ligero hacia la derecha
		if(centro_a_este)
		{
			contador_rotacion_x = contador_rotacion_x - 1;
			rotateHeadRequired(component,contador_rotacion_x/5,0);	
			if(contador_rotacion_x/5 == - aleatorio_giro)
			{
				centro_a_este = false;
				este_a_centro = true;
			}
			
		}
		//vuelta al centro desde la derecha
		if(este_a_centro)
		{
			contador_rotacion_x = contador_rotacion_x + 1;
			rotateHeadRequired(component,contador_rotacion_x/5,0);	
			if(contador_rotacion_x/5 == 0)
			{
				centro_a_este = false;
				este_a_centro = false;
			}
			
		}
		
		//movimiento ligero hacia abajo
		if(centro_a_sur)
		{
			contador_rotacion_y = contador_rotacion_y - 1;
			rotateHeadRequired(component,0,contador_rotacion_y/5);	
			if(contador_rotacion_y/5 == - aleatorio_giro)
			{
				centro_a_sur = false;
				sur_a_centro = true;
			}
			
		}
		//vuelta al centro desde abajo
		if(sur_a_centro)
		{
			contador_rotacion_y = contador_rotacion_y + 1;
			rotateHeadRequired(component,0,contador_rotacion_y/5);	
			if(contador_rotacion_y/5 == 0)
			{
				centro_a_sur = false;
				sur_a_centro = false;
			}
			
		}
			//movimiento ligero hacia arriba
		if(centro_a_norte)
		{
			contador_rotacion_y = contador_rotacion_y + 1;
			rotateHeadRequired(component,0,contador_rotacion_y/5);	
			if(contador_rotacion_y/5 == aleatorio_giro)
			{
				centro_a_norte = false;
				norte_a_centro = true;
			}
			
		}
		//vuelta al centro desde arriba
		if(norte_a_centro)
		{
			contador_rotacion_y = contador_rotacion_y - 1;
			rotateHeadRequired(component,0,contador_rotacion_y/5);	
			if(contador_rotacion_y/5 == 0)
			{
				centro_a_norte = false;
				norte_a_centro = false;
			}
			
		}
	
}

/********************************************************************************************************************/
/****************************************************** EYES *******************************************************/
/********************************************************************************************************************/

function checkEyes()
{
	/* IEyes call */
	// aumnetar x gira ojos hacia la derecha de Ada, disminuir a la izquierda
	//aumentar y sube los ojos
	//rotateEyeRequired(component,x,y);
	rotateEyeRequired(component, - (contador_rotacion_x/5),-10);
	
	
}
/********************************************************************************************************************/
/****************************************************** NOSTRILS *******************************************************/
/********************************************************************************************************************/

function manageNostrils()
{
	var d = new Date();
	print(d.getSeconds());
	if((d.getSeconds())%3==0){
		setFaceExpressionRequired(component,"NARIZ", 0);
	
	}
	else{
		setFaceExpressionRequired(component,"NARIZ", 0.8);
		
	}
}

/* Very basic eyes blinking functionality implemented
function checkEyesBlinking(){
        var d = new Date();
        print(d.getSeconds());
        

        if(((d.getSeconds())%6==0) && ojos_abiertos){
                setFaceExpressionRequired(component,"BLINK", 0.8);
                ojos_abiertos = false;
                tiempo_cerrados = new Date();

        }else{

        var tiempo_ahora = new Date();
        	if(!ojos_abiertos )*///(tiempo_ahora.getSeconds()-tiempo_cerrados.getSeconds())>0.5 )
        /*	{
               	 setFaceExpressionRequired(component,"BLINK", 0);
               	 ojos_abiertos=true;
               	 tiempo_cerrados=0;
        	}
	}
}
END **Very basic eyes blinking functionality implemented */


//IFlow<char*> implementation
function processData(text) {
	print("[JAVASCRIPT] - processData, TEXT:",text);
	//processDataRequired("hello elvira");
}

//IControlVoice implementation example
function startSpeaking(){
	print("[JAVASCRIPT] - startSpeaking");
	processDataRequired(component,"hello everybody");
}

function stopSpeaking(){
	//print("[JAVASCRIPT] - stopSpeaking");
	movingLips=false;
}

function startVoice(){
	//print("[JAVASCRIPT] - startVoice");
	movingLips=true;
}

//IFaceExpression implementation example
function setFaceExpression(expressionName, intensity) {
	print("setFaceExpression en Javascript !!","\n");
  print("y expressionName es: ", expressionName);

}

//ICamera implementation example
function setCameraPosition(X,Y,Z){
	print("[JAVASCRIPT] - setCameraPosition!!","\n");
  print("y las coordenadas son: ", X,",",Y,",",Z);	
}

function setCameraRotation( X, Y, Z){
	print("[JAVASCRIPT] - setCameraRotation!!","\n");
  print("y las coordenadas son: ", X,",",Y,",",Z);	
}
	
function setCameraParameters(IsOrtho,VisionAngle,nearClippingPlane,FarClippingPlane){
	print("[JAVASCRIPT] - setCameraParameters!!","\n");	
}


Initialize();

