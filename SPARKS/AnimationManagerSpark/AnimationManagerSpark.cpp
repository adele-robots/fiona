/*
 * AnimationManagerSpark.cpp
 *
 *  Created on: 06/09/2013
 *      Author: guille
 */

/// @file AnimationManagerSpark.cpp
/// @brief AnimationManagerSpark class implementation.


//#include "stdAfx.h"
#include "AnimationManagerSpark.h"
#include "simplexnoise.h"


#ifdef _WIN32
#else
extern "C"
Component *createComponent(
		char *componentInstanceName,
		char *componentType,
		ComponentSystem *componentSystem
	)
{
	if (!strcmp(componentType, "AnimationManagerSpark")) {
			return new AnimationManagerSpark(componentInstanceName, componentSystem);
		}
	else {
			return NULL;
		}
}
#endif

float xoff = 2.0;
float xincrement = 0.01/4;
float yoff = 9.0;
float yincrement = 0.01;
float zoff = 0.0;
float zincrement = 0.01;

float noiseNeckRotY;


timer_t firstTimerID;


void AnimationManagerSpark::timerHandler( int sig, siginfo_t *si, void *uc)
{
	LoggerInfo("AnimationManagerSpark - timerHandler | señal recogida pero no del timer");
    //timer_t *tidp;
    //tidp = si->si_value.sival_ptr;

//    if ( si->si_value.sival_ptr == &firstTimerID ){
//    	LoggerInfo("AnimationManagerSpark - timerHandler | señal recogida del timer");
//    }else{
//    	LoggerInfo("AnimationManagerSpark - timerHandler | señal recogida pero no del timer");
//    }
//    else if ( *tidp == secondTimerID )
//        secondCB(sig, si, uc);
//    else if ( *tidp == thirdTimerID )
//        thirdCB(sig, si, uc);
}


int AnimationManagerSpark::makeTimer( char *name, timer_t *timerID, int expireMS, int intervalMS )
{
	LoggerInfo("AnimationManagerSpark - makeTimer 1");
	struct sigevent         te;
	struct itimerspec       its;
	struct sigaction        sa;
	int                     sigNo = SIGRTMIN;
	/* Set up signal handler. */
	sa.sa_flags = SA_SIGINFO;
	sa.sa_sigaction = timerHandler;
	sigemptyset(&sa.sa_mask);
	if (sigaction(sigNo, &sa, NULL) == -1)
	{
		//fprintf(stderr, "%s: Failed to setup signal handling for %s.\n", PROG, name);
		return(-1);
	}
	/* Set and enable alarm */
	te.sigev_notify = SIGEV_SIGNAL;
	te.sigev_signo = sigNo;
	LoggerInfo("AnimationManagerSpark - makeTimer 2");
	te.sigev_value.sival_ptr = timerID;
	LoggerInfo("AnimationManagerSpark - makeTimer 3");
	timer_create(CLOCK_REALTIME, &te, timerID);
	LoggerInfo("AnimationManagerSpark - makeTimer 4");
	its.it_interval.tv_sec = 0;
	its.it_interval.tv_nsec = intervalMS * 1000000;
	its.it_value.tv_sec = 0;
	its.it_value.tv_nsec = expireMS * 1000000;
	LoggerInfo("AnimationManagerSpark - makeTimer 5");
	timer_settime(*timerID, 0, &its, NULL);
	return(0);
}



float AnimationManagerSpark::nextVal (float curr, float min, float max, float *pStep) {
	//LoggerInfo("AnimationManagerSpark::nextVal| step: %f",*pStep);
    // Handle situations where you want to turn around.
    if ((curr == min) || (curr == max)) {
        *pStep = -(*pStep);
        return curr + *pStep;
    }

    // Handle situation where you would exceed your bounds (just
    //   go to the bound).
    if (curr + *pStep < min) return min;
    if (curr + *pStep > max) return max;

    // Otherwise, just move within the bounds.
    return curr + *pStep;
}

/// Initializes AnimationManagerSpark component.
void AnimationManagerSpark::init(void){
	// Initialize random numbers
	timeval tv;
	gettimeofday(&tv, 0);
	// use second XOR microsecond precision XOR PID as seed
	srand((tv.tv_sec ^ tv.tv_usec) ^ getpid());

	mini = 0.0;
	maxi = 1.0;
	curr = mini;
	step = -0.1;

	maxNostrilsFlare = 0.5;
	currentNostrilsFlare = mini;
	stepNostrilsFlare = -0.025;

	maxOpenMouth = 1.0;
	currentOpenMouth = mini;
	stepOpenMouth = -0.1;

	maxSad = 1.0;
	currentSad = mini;
	stepSad = -0.1;

	currentSurprised = mini;

}

/// Unitializes the AnimationManagerSpark component.
void AnimationManagerSpark::quit(void){
}

//IFlow<char*> implementation
void AnimationManagerSpark::processData(char * prompt) {
	LoggerInfo("AnimationManagerSpark::processData | TEXTO: %s",prompt);
	string promptStr(prompt);
	if (promptStr.find("happy") != std::string::npos) {
		LoggerInfo("AnimationManagerSpark::processData | FOUND HAPPY!");
		isHappy = true;
		expressionEnded = false;
	}
	if (promptStr.find("openmouth") != std::string::npos) {
		LoggerInfo("AnimationManagerSpark::processData | FOUND open mouth!!");
		openMouth = true;
		openMouthExpressionEnded = false;
	}
	if (promptStr.find("sad") != std::string::npos) {
		LoggerInfo("AnimationManagerSpark::processData | SAD open mouth!!");
		isSad = true;
		sadExpressionEnded = false;
	}
	if (promptStr.find("surprised") != std::string::npos) {
		LoggerInfo("AnimationManagerSpark::processData | SURPRISED found!!");
		isSurprised = true;
		surprisedEnded = false;
	}

}

void AnimationManagerSpark::blink(){
	//timeCounterBlink.restart();
	//while
	LoggerInfo("AnimationManagerSpark::blink!");

	myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesClosedR"),0.5);
	myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesClosedL"),0.5);
	usleep(200000);
	myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesClosedR"),1);
	myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesClosedL"),1);
	usleep(200000);
	myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesClosedR"),0);
	myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesClosedL"),0);

}

//IThreadProc implementation
void AnimationManagerSpark::process() {
	// Random number between [2,10]
	randomBlinkTime = 2 + rand()%(11-2);
	LoggerInfo("randomBlinkTime:%d",randomBlinkTime);
	usleep(randomBlinkTime*1000000);
	//blink();

	static bool firstTime = true;
	if(firstTime){
		firstTime = false;
		//int rc;
		//rc = makeTimer("First Timer", &firstTimerID, 40, 40);
//		struct sigevent         te;
//		struct itimerspec       its;
//		struct sigaction        sa;
//		int                     sigNo = SIGRTMIN;
//		/* Set up signal handler. */
//		sa.sa_flags = SA_SIGINFO;
//		sa.sa_sigaction = timerHandler;
//		sigemptyset(&sa.sa_mask);
//		if (sigaction(sigNo, &sa, NULL) == -1)
//		{
//			//fprintf(stderr, "%s: Failed to setup signal handling for %s.\n", PROG, name);
//			//return(-1);
//		}
//		/* Set and enable alarm */
//		te.sigev_notify = SIGEV_SIGNAL;
//		te.sigev_signo = sigNo;
//		LoggerInfo("AnimationManagerSpark - makeTimer 2");
//		te.sigev_value.sival_ptr = &firstTimerID;
//		LoggerInfo("AnimationManagerSpark - makeTimer 3");
//		timer_create(CLOCK_REALTIME, &te, &firstTimerID);
//		LoggerInfo("AnimationManagerSpark - makeTimer 4");
//		its.it_interval.tv_sec = 0;
//		its.it_interval.tv_nsec = 40 * 1000000;
//		its.it_value.tv_sec = 0;
//		its.it_value.tv_nsec = 40 * 1000000;
//		LoggerInfo("AnimationManagerSpark - makeTimer 5");
//		timer_settime(firstTimerID, 0, &its, NULL);
	}

}

//FrameEventSubscriber implementation
void AnimationManagerSpark::notifyFrameEvent(){
	currentNostrilsFlare = nextVal(currentNostrilsFlare,mini,maxNostrilsFlare,&stepNostrilsFlare);
	LoggerInfo("AnimationManagerSpark::notifyFrameEvent| currentNostrilsFlare -> %f",currentNostrilsFlare);
	//myFaceExpression->setFaceExpression(const_cast<char *>("PHMNostrilsFlare"),currentNostrilsFlare);
	if(isHappy){
		//static bool expressionEnded = false;
		if(curr == 0.0f && expressionEnded){
			LoggerInfo("AnimationManagerSpark::notifyFrameEvent| Smile_Close_14 HAPPY EXPRESSION ENDED");
			isHappy = false;
			expressionEnded = false;
		}
		else if(curr < 1.0f){
			happyFreezes = HAPPY_ITERATIONS;
			curr = nextVal(curr,mini,maxi,&step);
			LoggerInfo("AnimationManagerSpark::notifyFrameEvent| currentHappy: %f",curr);
			//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmile-Genesis"),curr);
			if(round == 0){
				myFaceExpression->setFaceExpression(const_cast<char *>("Sympathy_08"),curr);
				//myFaceExpression->setFaceExpression(const_cast<char *>("Pain_04"),curr);
			}
			else if(round == 1)//Smile_Close_09
				myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_07"),curr);
			else if(round == 2)//Sympathy_08
				myFaceExpression->setFaceExpression(const_cast<char *>("Pain_04"),curr);
			else if(round == 3)
				myFaceExpression->setFaceExpression(const_cast<char *>("Attention_09"),curr);
		}else{
			if(happyFreezes > 0){
				LoggerInfo("HAPPY FREEZES %d TIMES",happyFreezes);
				//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmile-Genesis"),curr);
				if(round == 0){
					myFaceExpression->setFaceExpression(const_cast<char *>("Sympathy_08"),curr);
					//myFaceExpression->setFaceExpression(const_cast<char *>("Pain_04"),curr);
				}
				else if(round == 1)//Smile_Close_09
					myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_07"),curr);
				else if(round == 2)//Sympathy_08
					myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_01"),curr);
				else if(round == 3)
					myFaceExpression->setFaceExpression(const_cast<char *>("Attention_09"),curr);
				happyFreezes--;
			}else{
				expressionEnded = true;
				curr = nextVal(curr,mini,maxi,&step);
				//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmile-Genesis"),curr);
				if(round == 0){
					myFaceExpression->setFaceExpression(const_cast<char *>("Sympathy_08"),curr);
					//myFaceExpression->setFaceExpression(const_cast<char *>("Pain_04"),curr);
				}
				else if(round == 1)//Smile_Close_09
					myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_07"),curr);
				else if(round == 2)//Sympathy_08
					myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_01"),curr);
				else if(round == 3)
					myFaceExpression->setFaceExpression(const_cast<char *>("Attention_09"),curr);
			}
		}
	}

	if(openMouth){
			//static bool expressionEnded = false;
			if(currentOpenMouth == 0.0f && openMouthExpressionEnded){
				LoggerInfo("AnimationManagerSpark::notifyFrameEvent| Smile_Close_15 OPEN MOUTH WIDE ended!");
				openMouth = false;
				openMouthExpressionEnded = false;
			}
			else if(currentOpenMouth < 1.0f){
				openMouthFreezes = OPENMOUTH_ITERATIONS;
				currentOpenMouth = nextVal(currentOpenMouth,mini,maxOpenMouth,&stepOpenMouth);
				LoggerInfo("AnimationManagerSpark::notifyFrameEvent| currentOpenMouth: %f",currentOpenMouth);
				//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthOpen"),currentOpenMouth);
	//			myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthFrown"),currentOpenMouth);
	//			myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesHeight"),-currentOpenMouth);
				if(round == 0){
					myFaceExpression->setFaceExpression(const_cast<char *>("Contempt_05"),currentOpenMouth);
					//myFaceExpression->setFaceExpression(const_cast<char *>("Regret_08"),currentOpenMouth);
				}
				else if(round == 1)
					myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_11"),currentOpenMouth);
				else if(round == 2)//Sympathy_11
					myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_01"),currentOpenMouth);
				else if(round == 3)
					myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_01"),currentOpenMouth);
			}else{
				if(openMouthFreezes > 0){
					LoggerInfo("OPEN MOUTH FREEZES %d TIMES",openMouthFreezes);
					//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthOpen"),currentOpenMouth);
	//				myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthFrown"),currentOpenMouth);
	//				myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesHeight"),-currentOpenMouth);
					if(round == 0){
						myFaceExpression->setFaceExpression(const_cast<char *>("Contempt_05"),currentOpenMouth);
						//myFaceExpression->setFaceExpression(const_cast<char *>("Regret_08"),currentOpenMouth);
					}
					else if(round == 1)
						myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_11"),currentOpenMouth);
					else if(round == 2)
						myFaceExpression->setFaceExpression(const_cast<char *>("Surprise_06"),currentOpenMouth);
					else if(round == 3)
						myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_01"),currentOpenMouth);
					openMouthFreezes--;
				}else{
					openMouthExpressionEnded = true;
					currentOpenMouth = nextVal(currentOpenMouth,mini,maxOpenMouth,&stepOpenMouth);
					//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthOpen"),currentOpenMouth);
	//				myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthFrown"),currentOpenMouth);
	//				myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesHeight"),-currentOpenMouth);
					if(round == 0){
						myFaceExpression->setFaceExpression(const_cast<char *>("Contempt_05"),currentOpenMouth);
						//myFaceExpression->setFaceExpression(const_cast<char *>("Regret_08"),currentOpenMouth);
					}
					else if(round == 1)
						myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_11"),currentOpenMouth);
					else if(round == 2)
						myFaceExpression->setFaceExpression(const_cast<char *>("Surprise_06"),currentOpenMouth);
					else if(round == 3)
						myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_01"),currentOpenMouth);
				}
			}
		}

	if(isSad){
		//static bool expressionEnded = false;
		if(currentSad == 0.0f && sadExpressionEnded){
			LoggerInfo("AnimationManagerSpark::notifyFrameEvent| Sympathy_01 sad EXPRESSION ENDED");
			isSad = false;
			sadExpressionEnded = false;
		}
		else if(currentSad < 1.0f){
			sadFreezes = HAPPY_ITERATIONS;
			currentSad = nextVal(currentSad,mini,maxSad,&stepSad);
			LoggerInfo("AnimationManagerSpark::notifyFrameEvent| currentSad: %f",currentSad);
			//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmile-Genesis"),curr);
			if(round == 0)//Smile_Close_06
				myFaceExpression->setFaceExpression(const_cast<char *>("Regret_08"),currentSad);
			else if(round == 1)
				myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_14"),currentSad);
			else if(round == 2)
				myFaceExpression->setFaceExpression(const_cast<char *>("Attention_01"),currentSad);
			else if(round == 3)
				myFaceExpression->setFaceExpression(const_cast<char *>("Confusion_01"),currentSad);
		}else{
			if(sadFreezes > 0){
				LoggerInfo("SAD FREEZES %d TIMES",sadFreezes);
				//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmile-Genesis"),curr);
				if(round == 0)//Smile_Close_06
					myFaceExpression->setFaceExpression(const_cast<char *>("Regret_08"),currentSad);
				else if(round == 1)
					myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_14"),currentSad);
				else if(round == 2)
					myFaceExpression->setFaceExpression(const_cast<char *>("Attention_01"),currentSad);
				else if(round == 3)
					myFaceExpression->setFaceExpression(const_cast<char *>("Confusion_01"),currentSad);
				sadFreezes--;
			}else{
				sadExpressionEnded = true;
				currentSad = nextVal(currentSad,mini,maxSad,&stepSad);
				//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmile-Genesis"),curr);
				if(round == 0)//Smile_Close_06
					myFaceExpression->setFaceExpression(const_cast<char *>("Regret_08"),currentSad);
				else if(round == 1)
					myFaceExpression->setFaceExpression(const_cast<char *>("Smile_Close_14"),currentSad);
				else if(round == 2)
					myFaceExpression->setFaceExpression(const_cast<char *>("Attention_01"),currentSad);
				else if(round == 3)
					myFaceExpression->setFaceExpression(const_cast<char *>("Confusion_01"),currentSad);
			}
		}
	}

	if(isSurprised){
			//static bool expressionEnded = false;
			if(currentSurprised == 0.0f && surprisedEnded){
				round++;
				LoggerInfo("AnimationManagerSpark::notifyFrameEvent| Surprised EXPRESSION ENDED | ROUND ++ !!!! toca round: %d", round);
				isSurprised = false;
				surprisedEnded = false;
			}
			else if(currentSurprised < 1.0f){
				surprisedFreezes = HAPPY_ITERATIONS;
				currentSurprised = nextVal(currentSurprised,mini,maxi,&step);
				LoggerInfo("AnimationManagerSpark::notifyFrameEvent| currentSurprised: %f",currentSurprised);
				//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmile-Genesis"),curr);
				if(round == 0)//Smile_Close_07
					myFaceExpression->setFaceExpression(const_cast<char *>("Contempt_10"),currentSurprised);
				else if(round == 1)
					myFaceExpression->setFaceExpression(const_cast<char *>("Sympathy_04"),currentSurprised);
				else if(round == 2)
					myFaceExpression->setFaceExpression(const_cast<char *>("Attention_03"),currentSurprised);
				else if(round == 3)
					myFaceExpression->setFaceExpression(const_cast<char *>("Confusion_03"),currentSurprised);
			}else{
				if(surprisedFreezes > 0){
					LoggerInfo("SURPRISED FREEZES %d TIMES",surprisedFreezes);
					//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmile-Genesis"),curr);
					if(round == 0)//Smile_Close_07
						myFaceExpression->setFaceExpression(const_cast<char *>("Contempt_10"),currentSurprised);
					else if(round == 1)
						myFaceExpression->setFaceExpression(const_cast<char *>("Sympathy_04"),currentSurprised);
					else if(round == 2)
						myFaceExpression->setFaceExpression(const_cast<char *>("Attention_03"),currentSurprised);
					else if(round == 3)
						myFaceExpression->setFaceExpression(const_cast<char *>("Confusion_03"),currentSurprised);
					surprisedFreezes--;
				}else{
					surprisedEnded = true;
					currentSurprised = nextVal(currentSurprised,mini,maxi,&step);
					//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmile-Genesis"),curr);
					if(round == 0)//Smile_Close_07
						myFaceExpression->setFaceExpression(const_cast<char *>("Contempt_10"),currentSurprised);
					else if(round == 1)
						myFaceExpression->setFaceExpression(const_cast<char *>("Sympathy_04"),currentSurprised);
					else if(round == 2)
						myFaceExpression->setFaceExpression(const_cast<char *>("Attention_03"),currentSurprised);
					else if(round == 3)
						myFaceExpression->setFaceExpression(const_cast<char *>("Confusion_03"),currentSurprised);
				}
			}
		}



	xoff += xincrement;
	yoff += yincrement;
	zoff += zincrement;
	//LoggerInfo("AnimationManagerSpark::notifyFrameEvent| noisez: %f",zoff);
	static bool firstTime = true;
	if(zoff >= .05f && firstTime){
		firstTime = false;
		LoggerInfo("playAnimation 1");
		myAnimation->playAnimation(const_cast<char *>("poses_idle5_3_2_3_1_3"));

	}
	if(zoff >= 0.1f && zoff <=1.0f){
		//myFaceExpression->setFaceExpression(const_cast<char *>("PHMSmileSimpleL"),zoff);
		//myFaceExpression->setFaceExpression(const_cast<char *>("PHMSmileSimpleR"),zoff);
		//myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesClosedR"),zoff);
		//myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesClosedL"),zoff);

		//myFaceExpression->setFaceExpression(const_cast<char *>("PHMEyesClosedR"),zoff);
		//myFaceExpression->setFaceExpression(const_cast<char *>("PHMBrowUpR"),zoff);
		//LoggerInfo("Expression intensity set on: %f",zoff);
		//myFaceExpression->setFaceExpression(const_cast<char *>("PHMMouthSmileOpen"),zoff);


	}
	// Use 2D as 1D, passing 'y' variable always as 0
	// Scaled the return value to [-10,10]
//	noiseRotX = scaled_raw_noise_2d(-3.0,0.5,xoff,0);
//	noiseRotY = scaled_raw_noise_2d(-10.0,10.0,yoff,0);
//	noiseRotZ = scaled_raw_noise_2d(-2.0,2.0,zoff,0);

	// Octaves, persistence, scale, loBound, hiBound, x, y
	noiseRotX = scaled_octave_noise_2d(4,0.25,1,-3.5,-0.5,xoff,0);
	noiseRotY = scaled_octave_noise_2d(4,0.5,1,-20.0,20.0,yoff,0);
	noiseRotZ = scaled_octave_noise_2d(4,0.25,1,-0.5,0.5,zoff,0);

	//noiseNeckRotX = scaled_raw_noise_2d(-10.0,2.0,xoff*4,0);
	noiseNeckRotX = scaled_octave_noise_2d(4,0.25,1,-10.0,-2.0,xoff*4,0);
	// Se pone el -1, para tener un valor contrario al 'noiseRotY', para que siempre esté mirando de frente
	// Es mejor no poner exactamente el mismo valor en negativo, porque entonces estaría siempre mirando de
	// frente pero sin ninguna variación, sin mover el cuello, y se movería el cuerpo mientras está la cabeza quieta
	noiseNeckRotY = -1*scaled_octave_noise_2d(2,0.75,1,-20.0,20.0,yoff,0);


//	noiseEyeRotX = scaled_raw_noise_2d(-8.0,1.0,xoff,0);
//	noiseEyeRotY = scaled_raw_noise_2d(-5.0,5.0,yoff/2,0);
	noiseEyeRotX = scaled_octave_noise_2d(4,0.25,1,-0.5,0.5,xoff,0);
	noiseEyeRotY = scaled_octave_noise_2d(4,0.25,1,-2.0,2.0,yoff/2,0);

	noiseRShoulderRotX = scaled_octave_noise_2d(2,0.5,1,-2.0,2.0,zoff/8,0);
	noiseRShoulderRotY = scaled_octave_noise_2d(2,0.5,1,-1.0,15.0,zoff/8,0);
	noiseRShoulderRotZ = scaled_octave_noise_2d(2,0.5,1,64.0,75.0,yoff/16,0);

	noiseLShoulderRotX = scaled_octave_noise_2d(2,0.5,1,-2.0,2.0,-zoff/8,0);
	noiseLShoulderRotY = scaled_octave_noise_2d(2,0.5,1,-15.0,1.0,-zoff/8,0);
	noiseLShoulderRotZ = scaled_octave_noise_2d(2,0.5,1,-75.0,-64.0,zoff/16,0);

	noiseRForeArmRotX = scaled_octave_noise_2d(2,0.5,1,-5.0,5.0,(zoff+29)/8,0);
	noiseRForeArmRotY = scaled_octave_noise_2d(2,0.25,1,-5.0,100.0,zoff/8,0);
	noiseRForeArmRotZ = scaled_octave_noise_2d(2,0.5,1,-0.5,0.5,-zoff/16,0);

	// A mayor persistencia, mayores cambios en los valores y más rápidos (abarca primero muchos de los valores del rango definido)
	// A mayor número de octavas, cambios más bruscos, con menor número, la transición entre distintos valores es más suave
	noiseLForeArmRotX = scaled_octave_noise_2d(2,0.5,1,-5.0,5.0,zoff/8,0);
	noiseLForeArmRotY = scaled_octave_noise_2d(2,0.5,1,-80.0,5.0,(yoff+80)/8,0);
	noiseLForeArmRotZ = scaled_octave_noise_2d(2,0.5,1,-0.5,0.5,yoff/8,0);


	//myJoint->rotateJointPart(const_cast<char *>("chest"),noiseRotX,noiseRotY,noiseRotZ);

	//myJoint->rotateJointPart(const_cast<char *>("neck"),noiseNeckRotX,noiseNeckRotY,-noiseRotZ);

//	myJoint->rotateJointPart(const_cast<char *>("rShldr"),noiseRShoulderRotX,noiseRShoulderRotY,noiseRShoulderRotZ);
//	myJoint->rotateJointPart(const_cast<char *>("lShldr"),noiseLShoulderRotX,noiseLShoulderRotY,noiseLShoulderRotZ);
//	myJoint->rotateJointPart(const_cast<char *>("rForeArm"),noiseRForeArmRotX,noiseRForeArmRotY,noiseRForeArmRotZ);
//	myJoint->rotateJointPart(const_cast<char *>("lForeArm"),noiseLForeArmRotX,noiseLForeArmRotY,noiseLForeArmRotZ);

	myJoint->rotateJointPart(const_cast<char *>("lEye"),noiseEyeRotX,noiseEyeRotY,0);
	myJoint->rotateJointPart(const_cast<char *>("rEye"),noiseEyeRotX,noiseEyeRotY,0);
	//myJoint->rotateJointPart(const_cast<char *>("lEye"),-3.0,noiseEyeRotY*4,0);
	//myJoint->rotateJointPart(const_cast<char *>("rEye"),-3.0,noiseEyeRotY*4,0);
}


