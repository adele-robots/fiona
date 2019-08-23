/// @file XMLParser.cpp
/// @brief XMLParser class implementation.


#ifndef __XMLParser_H
#define __XMLParser_H

#include <vector>
#include "pugixml.hpp"


/// @brief The class XMLParser is bla bla.
///
/// Details (long description) bla bla
/// bla bla.

class XMLParser {
public:
	void parseXmlFile(char *xmlFilePath);
	pugi::xpath_node_set xpathQuery(char *query);


private:
	std::vector<ptrdiff_t> offset_data;
	bool build_offset_data(const char* file);
	std::pair<int, int> get_location(ptrdiff_t offset);

private:
	pugi::xml_document xmlDocument;
};


#endif
