/*
 * PublisherSpark.h
 *
 *  Created on: 08/10/2012
 *      Author: guille
 */

/// @file PublisherSpark.h
/// @brief Component PublisherSpark main class.


#ifndef __PublisherSpark_H
#define __PublisherSpark_H


#include "Component.h"
#include "IFlow.h"
#include "IPublisher.h"
#include "IEyes.h"
#include "INeck.h"
#include "IDetectedFacePositionConsumer.h"
#include "SubscriberSpark.h"

/// @brief This is the main class for component Publisher.
///
/// 

template <class T> class PublisherSpark :
	public Component,
	public IFlow<T>,
	public IPublisher<T>,
	public IEyes,
	public INeck,
	public IDetectedFacePositionConsumer
{
public:
	PublisherSpark(
			char *instanceName,
			ComponentSystem *cs
	) : Component(instanceName, cs)
	{}

private:
	void initializeRequiredInterfaces() {	
	}

public:
	//Mandatory
	void init(void);
	void quit(void);

	//IFlow<T> implementation
	void processData(T myItem);
	//IPublisher<T> implementation
	void addSubscriber(SubscriberSpark<T> * subscriberSpark);
	//IEyes implementation
	void rotateEye(float pan,float tilt);
	//INeck implementation
	void rotateHead(float pan, float tilt);
	//IDetectedFacePositionConsumer implementation
	void consumeDetectedFacePosition(bool isFaceDetected, double x, double y);


private:
	std::vector< SubscriberSpark<T> *> subscriberArray;

};


/// Initializes Publisher component.
template <class T> void PublisherSpark<T>::init(void){
}

/// Unitializes the Publisher component.
template <class T> void PublisherSpark<T>::quit(void){
}

//IFlow<T> implementation
template <class T> void PublisherSpark<T>::processData(T myItem){
	for (unsigned int i = 0; i < subscriberArray.size(); i++)
		subscriberArray[i]->SubscriberSpark<T>::callProcess(myItem);
}

//IPublisher<T> implementation
template <class T> void PublisherSpark<T>::addSubscriber(SubscriberSpark<T> * subscriberSpark)
{
	//add subscriber to a vector of subscribers
	puts("Suscrito un nuevo SubscriberSpark");
	subscriberArray.push_back(subscriberSpark);
}

//IEyes implementation
template <class T> void PublisherSpark<T>::rotateEye(float pan,float tilt)
{
	for (unsigned int i = 0; i < subscriberArray.size(); i++)
			subscriberArray[i]->SubscriberSpark<T>::callRotateEye(pan,tilt);
}

//INeck implementation
template <class T> void PublisherSpark<T>::rotateHead(float pan,float tilt)
{
	for (unsigned int i = 0; i < subscriberArray.size(); i++)
			subscriberArray[i]->SubscriberSpark<T>::callRotateHead(pan,tilt);
}

//IDetectedFacePositionConsumer implementation
template <class T> void PublisherSpark<T>::consumeDetectedFacePosition(bool isFaceDetected, double x, double y)
{
	for (unsigned int i = 0; i < subscriberArray.size(); i++)
			subscriberArray[i]->SubscriberSpark<T>::callConsumeDetectedFacePosition(isFaceDetected,x,y);
}

#endif

