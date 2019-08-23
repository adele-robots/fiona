function Initialize() {
log(component,"JS SIDE ====> llamada log ..");
}

// IANIMATION AND IFACEEXPRESSION 
function playAnimation(animationFileName) {
	log(component,"JS SIDE ====> llamada log - playAnimation");
	setFaceExpressionRequired(component, "happy", 0.5);
}

function setFaceExpression() {
	log(component,"JS SIDE ===> llamada log - setFaceExpression Provided");
	playAnimationRequired(component,"prueba");
}

//AUDIOQUEUE AND DE ICAMERA 
function getStoredAudioSize(){
	log(component,"JS SIDE ===> llamada log - dequeueAudioBuffer Provided");
	setCameraPositionRequired(component,3.1,3.2,3.3);
	setCameraRotationRequired(component,4.1,4.2,4.3);
	setCameraParametersRequired(component,false,5.1, 5.2,5.3);	
}

function dequeueAudioBuffer(buffer, size){
	log(component,"JS SIDE ===> llamada log - dequeueAudioBuffer Provided");
	setCameraPositionRequired(component,6.1,6.2,6.3);
	setCameraRotationRequired(component,7.1,7.2,7.3);
	setCameraParametersRequired(component,false,8.1, 8.2,8.3);	
}

function setCameraPosition(X,Y,Z){
	log(component,"JS SIDE ===> llamada log - setCameraPosition Provided");
	getStoredAudioSizeRequired(component);
	dequeueAudioBufferRequired(component,"CameraPosition", 50);
}

function setCameraRotation( X, Y, Z){
	log(component,"JS SIDE ===> llamada log - setCameraRotation Provided");
	getStoredAudioSizeRequired(component)
	dequeueAudioBufferRequired(component,"CameraRotation", 60);
}
	
function setCameraParameters(IsOrtho,VisionAngle,nearClippingPlane,FarClippingPlane){
	log(component,"JS SIDE ===> llamada log - setCameraParameters Provided");
	getStoredAudioSizeRequired(component)
	dequeueAudioBufferRequired(component,"CameraParameters", 70);
}

// ICONCURRENT AND ICONTROLVOICE 
function start(){
	log(component,"JS SIDE ===> llamada log - start Provided");
	startSpeakingRequired(component);
	stopSpeakingRequired(component);
	startVoiceRequired(component);	
}

function stop(){
	log(component,"JS SIDE ===> llamada log - stop Provided");
	startSpeakingRequired(component);
	stopSpeakingRequired(component);
	startVoiceRequired(component);	
}

function startSpeaking(){
	log(component,"JS SIDE ===> llamada log - startSpeaking Provided");
	startRequired(component);
	stopRequired(component);
}

function stopSpeaking(){
	log(component,"JS SIDE ===> llamada log - stopSpeaking Provided");
	startRequired(component);
	stopRequired(component);
}

function startVoice(){
	log(component,"JS SIDE ===> llamada log - startVoice Provided");
	startRequired(component);
	stopRequired(component);
}

//IDETECTEDFACEPOSITIONCONSUMER AND IEYES 
function consumeDetectedFacePosition(isFaceDetected, x, y){
	log(component,"JS SIDE ===> llamada log - consumeDetectedFacePosition Provided");
	rotateEyeRequired(component,3.3,4.4);
}

function rotateEye(pan,tilt){
	log(component,"JS SIDE ===> llamada log - rotateEye Provided");
	consumeDetectedFacePositionRequired(component,false,1.1,2.2);
}

//INECK AND ITHREADPROC 

function rotateHead(pan,tilt){
	log(component,"JS SIDE ===> llamada log - rotateHead Provided");
	processRequired(component);
}

function process(){
	log(component,"JS SIDE ===> llamada log - process Provided");
	rotateHeadRequired(component,1.11,2.22);
}

//IVOICE AND IWINDOWS 

function sayThis(prompt){
	log(component,"JS SIDE ===> llamada log - sayThis Provided");
	getWindowDisplayRequired(component);
	showRequired(component);
	hideRequired(component);
	getColorDepthRequired(component);
	makeCurrentopenGlThreadRequired(component);
	openGlSwapBuffersRequired(component);
}

function waitEndOfSpeech(){
	log(component,"JS SIDE ===> llamada log - waitEndOfSpeech Provided");
	getWindowDisplayRequired(component);
	showRequired(component);
	hideRequired(component);
	getColorDepthRequired(component);
	makeCurrentopenGlThreadRequired(component);
	openGlSwapBuffersRequired(component);	
}

function stopSpeech(){
	log(component,"JS SIDE ===> llamada log - stopSpeech Provided");
	getWindowDisplayRequired(component);
	showRequired(component);
	hideRequired(component);
	getColorDepthRequired(component);
	makeCurrentopenGlThreadRequired(component);
	openGlSwapBuffersRequired(component);
}

function getWindowDisplay(){
	log(component,"JS SIDE ===> llamada log - getWindowDisplay Provided");
	sayThisRequired(component, "prompt-getWindowDisplay");
	waitEndOfSpeechRequired(component);
	stopSpeechRequired(component);
}

function show(){
	log(component,"JS SIDE ===> llamada log - show Provided");
	sayThisRequired(component, "prompt-show");
	waitEndOfSpeechRequired(component);
	stopSpeechRequired(component);	
}

function hide(){
	log(component,"JS SIDE ===> llamada log - hide Provided");
	sayThisRequired(component, "prompt-hide");
	waitEndOfSpeechRequired(component);
	stopSpeechRequired(component);	
}

function getColorDepth(){
	log(component,"JS SIDE ===> llamada log - getColorDepth Provided");
	sayThisRequired(component, "prompt-getColorDepth");
	waitEndOfSpeechRequired(component);
	stopSpeechRequired(component);	
}

function makeCurrentopenGlThread(){
	log(component,"JS SIDE ===> llamada log - makeCurrentopenGlThread Provided");
	sayThisRequired(component, "prompt-makeCurrentopenGlThread");
	waitEndOfSpeechRequired(component);
	stopSpeechRequired(component);	
}

function openGlSwapBuffers(){
	log(component,"JS SIDE ===> llamada log - openGlSwapBuffers Provided");
	sayThisRequired(component, "prompt-openGlSwapBuffers");
	waitEndOfSpeechRequired(component);
	stopSpeechRequired(component);	
}

//IASYNCFATALERRORHANDLER AND IRENDERIZABLE
function render(){
	log(component,"JS SIDE ===> llamada log - render Provided");
	handleErrorRequired(component,"HOLA");
}

function getCamaraNode(){
	log(component,"JS SIDE ===> llamada log - getCamaraNode Provided");
	handleErrorRequired(component,"ADIOS");
}

function handleError(msg){
	log(component,"JS SIDE ===> llamada log - handleError Provided.");
	renderRequired(component);
	getCamaraNodeRequired(component);
}


//FRAMEEVENTSUBSCRIBER AND ICAMERA

function notifyFrameEvent(){
	log(component,"JS SIDE ===> llamada log - notifyFrameEvent Provided");
	setCameraPositionRequired(component,6.1,6.2,6.3);
	setCameraRotationRequired(component,7.1,7.2,7.3);
	setCameraParametersRequired(component,false,8.1, 8.2,8.3);	
	
}

function setCameraPosition(X,Y,Z){
	log(component,"JS SIDE ===> llamada log - setCameraPosition Provided");
	notifyFrameEventRequired(component);
}

function setCameraRotation( X, Y, Z){
	log(component,"JS SIDE ===> llamada log - setCameraRotation Provided");
	notifyFrameEventRequired(component);
}
	
function setCameraParameters(IsOrtho,VisionAngle,nearClippingPlane,FarClippingPlane){
	log(component,"JS SIDE ===> llamada log - setCameraParameters Provided");
	notifyFrameEventRequired(component);
}

Initialize();

