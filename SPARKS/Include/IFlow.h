/*
 * IFlow.h
 *
 *  Created on: 08/10/2012
 *      Author: guille
 */

#ifndef IFLOW_H_
#define IFLOW_H_

#include "OutgoingImage.h"
#include "Image.h"
#include "AudioWrap.h"
#include "jsoncpp/value.h"

template <class T> class IFlow {
public:
	virtual void processData(T myItem) = 0;
};

#endif /* IFLOW_H_ */
