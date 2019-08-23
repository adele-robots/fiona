// Global variables
var conteo = 90;
var primer_conteo = true;
var movingLips=false;
var alfa=0.0;
var animationLaunched = false;
var firstMove = false;
var secondMove = false;

function Initialize() {
	/* Auxiliar built-in functions */
	//log("Logged message",1); //1 - INFO, 2 - WARN, 3 - ERROR
	//print("Pregunto a REBECCA, con: are you there?");
	//alert(0.2 + 0.1 == 0.3);
  //usleep(10000000)

	/* ICamera calls */
  //setCameraPositionRequired(component,0.63,166,50);
	//setCameraRotationRequired(component,10.1,4.2,4.3);
	//setCameraParametersRequired(component,false,45, 80,100);

	/* IFaceExpression call */
	//setFaceExpressionRequired(component,"eyes-closed_ShapeKey", 0);

	/* IFlow<char*> call */
	//processDataRequired(component,"hello everybody, how do you do?");

	/* INeck call */
  //rotateHeadRequired(component,0,50);

	/* IEyes call */
	//rotateEyeRequired(component,0,50);

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
	if (movingLips){		
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
	}
/* END ** VoiceStartSpark functionality implemented */


	//checkEyesBlinking()

/* Basic example to check animations */
	conteo = conteo + 0.2;
	if(conteo>100){
		if(!animationLaunched){
			playAnimationRequired(component,"mi-mocap.anim");
			animationLaunched = true;
		}
		else if(!firstMove){
			print("1");
			rotateHeadRequired(component,0,50);
			firstMove = true;
		}
		else if(!secondMove){
			print("2");
			rotateHeadRequired(component,20,0);			
			secondMove = true;
		}
		else{
			print("3");
			rotateHeadRequired(component,0,-50);
			firstMove = false;
			secondMove = false;
		}
		
	}
	else
		setCameraPositionRequired(component,0,150,conteo+30);
/* END ** Basic example to check animations */
}


/* Very basic eyes blinking functionality implemented */
function checkEyesBlinking(){
	var d = new Date();
	print(d.getSeconds());
	if((d.getSeconds())%2==0){
		setFaceExpressionRequired(component,"eyes-closed_ShapeKey", 0);
	}
	else{
		setFaceExpressionRequired(component,"eyes-closed_ShapeKey", 1);		
	}
}
/* END **Very basic eyes blinking functionality implemented */


//IFlow<char*> implementation
function processData(text) {
	print("[JAVASCRIPT] - processData, Rebecca me dice:",text);
  print("y yo le respondo con: hello elvira")
	processDataRequired("hello elvira");
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

