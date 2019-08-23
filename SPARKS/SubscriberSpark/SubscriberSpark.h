/*
 * SubscriberSpark.h
 *
 *  Created on: 08/10/2012
 *      Author: guille
 */

/// @file SubscriberSpark.h
/// @brief Component Subscriber main class.


#ifndef __SubscriberSpark_H
#define __SubscriberSpark_H

#include "Component.h"
#include "IFlow.h"
#include "IPublisher.h"
#include "IEyes.h"
#include "INeck.h"
#include "IDetectedFacePositionConsumer.h"

/// @brief This is the main class for component SubscriberSpark.
///
///

template <class T> class SubscriberSpark :
	public Component
{
public:
	SubscriberSpark(
			char *instanceName,
			ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:
	IFlow<T> *myFlow;
	IPublisher<T> *myPublisher;
	IEyes *myEyes;
	INeck *myNeck;
	IDetectedFacePositionConsumer *myDetectedFace;

	void initializeRequiredInterfaces() {
		requestRequiredInterface<IFlow<T> >(&myFlow);
		requestRequiredInterface<IPublisher<T> >(&myPublisher);
		requestRequiredInterface<IEyes>(&myEyes);
		requestRequiredInterface<INeck>(&myNeck);
		requestRequiredInterface<IDetectedFacePositionConsumer>(&myDetectedFace);
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//Public method to be used by the Publisher to call each Subscriber processData (IFlow) method
    void callProcess(const T &myItem);
    //Public method to be used by the Publisher to call each Subscriber rotateEye (IEyes) method
    void callRotateEye(const float &pan, const float &tilt);
    //Public method to be used by the Publisher to call each Subscriber rotateHead (INeck) method
    void callRotateHead(const float &pan, const float &tilt);
    //Public method to be used by the Publisher to call each Subscriber consumeDetectedFacePosition
    //(IDetectedFacePositionConsumer) method
    void callConsumeDetectedFacePosition(bool isFaceDetected, const double &x, const double &y);
};

/// Initializes Subscriber component.
template <class T> void SubscriberSpark<T>::init(void){
	myPublisher->addSubscriber(this);
}

/// Unitializes the Subscriber component.
template <class T> void SubscriberSpark<T>::quit(void){
}

template <class T> void SubscriberSpark<T>::callProcess(const T &myItem){
	myFlow->processData(myItem);
}

template <class T> void SubscriberSpark<T>::callRotateEye(const float &pan, const float &tilt){
	myEyes->rotateEye(pan,tilt);
}

template <class T> void SubscriberSpark<T>::callRotateHead(const float &pan, const float &tilt){
	myNeck->rotateHead(pan,tilt);
}

template <class T> void SubscriberSpark<T>::callConsumeDetectedFacePosition(bool isFaceDetected, const double &x, const double &y){
	myDetectedFace->consumeDetectedFacePosition(isFaceDetected,x,y);
}

#endif

