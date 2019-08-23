
#ifndef __EnumParser_H
#define __EnumParser_H

#include <stdexcept>

template <typename T>
class EnumParser
{
		std::map<std::string, T> enumMap;
public:
		EnumParser(){};

		T ParseSomeEnum(const std::string &value)
		{
			typename std::map <std::string, T>::const_iterator iValue = enumMap.find(value);
			if(iValue == enumMap.end())
				throw std::runtime_error("Error accessing enum map");
			return iValue->second;
		}
};

#endif
