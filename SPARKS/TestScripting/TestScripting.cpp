/// @file TestScripting.cpp
/// @brief TestScripting class implementation.

#include "TestScripting.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "TestScripting")) {
			return new TestScripting(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

/// Initializes TestScripting component.
void TestScripting::init(void){
	running = true;
}

/// Unitializes the TestScripting component.
void TestScripting::quit(void){
}
//IApplication
void TestScripting::run(void){
	while (running){
		puts("Running...");
		sleep(3);

		/**
		 * IANIMATION AND IFACEEXPRESSION
		 */

		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IAnimation");
		char BufAnimation[]= "ANIMATION";
		char* play= BufAnimation;
		myAnimation->playAnimation(play);
		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IFaceExpression");
		char BufFaceExpression[]= "FACEEXPRESSION";
		char* setFace=BufFaceExpression;
		myFaceExpression->setFaceExpression(setFace,4);


		/**
		 * IAUDIOQUEUE AND ICAMERA
		 */

		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IAudioQueue");
		myAudioQueue->getStoredAudioSize();
		char BufAudioQueue[]= "AUDIOQUEUE";
		char* audioQueue=BufAudioQueue;
		myAudioQueue->dequeueAudioBuffer(audioQueue,10);
		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: ICamera");
		myCamera->setCameraParameters(true,1.5,2.5,3.5);
		myCamera->setCameraPosition(1.1,2.2,3.3);
		myCamera->setCameraRotation(0.1,0.2,0.3);

		/**
		 * ICONCURRENT AND ICONTROLVOICE
		 */

		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IConcurrent");
		myConcurrent->start();
		myConcurrent->stop();
		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IControlVoice");
		myControlVoice->startSpeaking();
		myControlVoice->startVoice();
		myControlVoice->stopSpeaking();

		/**
		 * IDETECTEDFACEPOSITIONCONSUMER AND IEYES
		 */

		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IDetectedFacePositionConsumer");
		myDetectedFacePositionConsumer->consumeDetectedFacePosition(true,1.9,2.8);
		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IEyes");
		myEyes->rotateEye(9.1,8.2);

		/**
		 * INECK AND ITHREADPROC
		 */

		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: INeck");
		myNeck->rotateHead(9.99,8.88);
		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IThreadProc");
		myThreadProc->process();



		/**
		 * IVOICE AND IWINDOW
		 */

		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IVoice");
		char BufVoice[]= "VOICE";
		char* voiceprompt=BufVoice;
		myVoice->sayThis(voiceprompt);
		myVoice->stopSpeech();
		myVoice->waitEndOfSpeech();
		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IWindows");
		myWindow->getColorDepth();
		myWindow->getWindowDisplay();
		myWindow->hide();
		myWindow->makeCurrentopenGlThread();
		myWindow->openGlSwapBuffers();
		myWindow->show();


		/**
		 * IASYNCFATALERRORHANDLE AND IRENDERIZABLE
		 */

		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IAsyncFatalErrorHandle");
		char BufAsyncFatalErrorHandle[]= "ERRORHANDLE";
		char* handle= BufAsyncFatalErrorHandle;
		myAsyncFatalErrorHandler->handleError(handle);
		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: IRenderizable");
		myRenderizable->getCamaraNode();
		myRenderizable->render();


		/**
		 * FRAMEEVENTSUBSCRIBER AND ICAMERA
		 */
		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: FrameEventSubscriber");
		myFrameEventSubscriber->notifyFrameEvent();
		printf("=========================================================================\n");
		printf("=========================================================================\n");
		puts("Using Interface: ICamera");
		myCamera->setCameraParameters(true,1.5,2.5,3.5);
		myCamera->setCameraPosition(1.1,2.2,3.3);
		myCamera->setCameraRotation(0.1,0.2,0.3);
	}
}

//IAnimation
void  TestScripting::playAnimation(char *animationFileName){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IAnimation!!!\n");
}

//IAsyncFatalErrorHandler
void TestScripting::handleError(char *msg){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IAsyncFatalErrorHandler!!!\n");
}

//IAudioQueue
int TestScripting::getStoredAudioSize(){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IAudioQueue, funcion getStoredAudioSize!!!\n");
	// TODO: Queda pendiende hacer la implementación esto esta puesto para que no proteste
	int a = 1;
	return a;
}

void TestScripting::dequeueAudioBuffer(char *buffer, int size){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IAudioQueue, funcion dequeueAudioBuffer!!!\n");
}

//ICamera
void TestScripting::setCameraPosition(float X,float Y,float Z){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del ICamera, funcion setCameraPosition!!!\n");
}

void TestScripting::setCameraRotation(float X,float Y,float Z){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del ICamera, funcion setCameraRotation!!!\n");
}

void TestScripting::setCameraParameters(bool IsOrtho,float VisionAngle, float nearClippingPlane,float FarClippingPlane){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del ICamera, funcion setCameraParameters!!!\n");
}

//IConcurrent
void TestScripting::start(){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IConcurrent, funcion start!!!\n");
}

void TestScripting::stop(){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IConcurrent, funcion stop!!!\n");
}

//IControlVoice
void TestScripting::startSpeaking(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IControlVoice, funcion startSpeaking!!!\n");
}
void TestScripting::stopSpeaking(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IControlVoice, funcion stopSpeaking!!!\n");
}
void TestScripting::startVoice(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IControlVoice, funcion startVoice!!!\n");
}

//IDetectedFacePosition
void TestScripting::consumeDetectedFacePosition(bool isFaceDetected, double x, double y){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IDetectedFacePosition!!!\n");
}

//IEyes
void TestScripting::rotateEye(float pan,float tilt){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IEyes!!!\n");
}

//IFaceExpression
void TestScripting::setFaceExpression(char *expressionName,float intensity){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IFaceExpression!!!\n");
}

//IFrameEventPublisher
void TestScripting::addFrameEventSubscriber(FrameEventSubscriber *frame){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IFrameEventPublisher!!!\n");
}

//IFrameEventSubscriber
void TestScripting::notifyFrameEvent(){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del FrameEventSubscriber!!!\n");
}

//INeck
void TestScripting::rotateHead(float pan, float tilt){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del INeck!!!\n");
}

//IRenderizable
void TestScripting::render(){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IRenderizable, funcion render!!!\n");
}

H3DRes TestScripting::getCamaraNode(){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IRenderizable, funcion getCamaraNode!!!\n");
	H3DRes h;
	return h;
}

//IThreadProc
void TestScripting::process(){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IThreadProc!!!\n");
}

//IVoice
void TestScripting::sayThis(char *prompt){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IVoice, funcion sayThis!!!\n");
}

void TestScripting::waitEndOfSpeech(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IVoice, funcion waitEndOfSpeech!!!\n");
}

void TestScripting::stopSpeech(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IVoice, funcion stopSpeech!!!\n");
}

//IWindow

Display* TestScripting::getWindowDisplay(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IWindow, funcion getWindowDisplay!!!\n");
	// TODO: Queda pendiende hacer la implementación esto esta puesto para que no proteste
	Display* d;
	return d;
}

void TestScripting::show(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IWindow, funcion show!!!\n");
}

void TestScripting::hide(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IWindow, funcion hide!!!\n");
}

int TestScripting::getColorDepth(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IWindow, funcion getColorDepth!!!\n");
	// TODO: Queda pendiende hacer la implementación esto esta puesto para que no proteste
	int g=3;
	return g;
}

void TestScripting::makeCurrentopenGlThread(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IWindow, funcion makeCurrentopenGlThread!!!\n");
}

void TestScripting::openGlSwapBuffers(void){
	printf("C++ SIDE ===> Y finalmente llegamos a la implementación del IWindow, funcion openGlSwapBuffers!!!\n");
}

