/*
 * IPublisher.h
 *
 *  Created on: 08/10/2012
 *      Author: guille
 */

#ifndef IPUBLISHER_H_
#define IPUBLISHER_H_

//#include "SubscriberSpark.h"

//forward declaration to avoid cyclic inclusion
template <class T> class SubscriberSpark;

template <class T> class IPublisher {
public:
	virtual void addSubscriber(SubscriberSpark<T> * subscriberSpark) = 0;
};

#endif /* IPUBLISHER_H_ */
