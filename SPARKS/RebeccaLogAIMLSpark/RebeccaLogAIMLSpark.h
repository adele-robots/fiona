/*
 * RebeccaLogAIMLSpark.h
 *
 *  Created on: 17/01/2013
 *      Author: guille
 */

/// @file RebeccaLogAIMLSpark.h
/// @brief Component RebeccaLogAIMLSpark main class.


#ifndef __RebeccaLogAIMLSpark_H
#define __RebeccaLogAIMLSpark_H

#include <sys/syscall.h>
#include <cstdlib>
#include "Component.h"
#include "RebeccaAIMLSpark.h"

/* LOG4CPLUS Headers */
#include <log4cplus/logger.h>
#include <log4cplus/fileappender.h>
#include <log4cplus/layout.h>
#include <log4cplus/ndc.h>
#include <log4cplus/helpers/loglog.h>

/* UUID Headers*/
//#include <boost/uuid/uuid.hpp>            // uuid class
//#include <boost/uuid/uuid_generators.hpp> // generators
//#include <boost/uuid/uuid_io.hpp>         // streaming operators etc.
//#include <boost/lexical_cast.hpp>

using namespace log4cplus;
using namespace log4cplus::helpers;

#define DEBUG 0

#ifndef CERR
#define CERR(msg) if(DEBUG) {std::cerr << __FILE__ << ":" << std::dec << __LINE__ << ":" <<__FUNCTION__ << "(): " << msg;}
#endif

#ifndef COUT
#define COUT std::cout << __FILE__ << ":" << std::dec << __LINE__ << " : "
#endif

/// @brief This is the main class for component RebeccaLogAIMLSpark.
///
/// 

class RebeccaLogAIMLSpark :
	public RebeccaAIMLSpark

{
public:
		RebeccaLogAIMLSpark(
				char *instanceName,
				ComponentSystem *cs
		) : RebeccaAIMLSpark(instanceName, cs)
		{
			CERR("RebeccaLogAIMLSpark constructor" << endl);
		}
		virtual ~RebeccaLogAIMLSpark() {};

public:
	//Mandatory
	void init(void);

	//IFlow implementation
	void processData(char *);
	
	
private:
	//Put class attributes here
	Logger myLogger;
	
};

#endif
