/// @file XMLParser.cpp
/// @brief XMLParser class implementation.

// Third party libraries are linked explicitly once in the project.
// #pragma comment(lib, "thirdPartyLib.lib")

#include "stdAfx.h"
#include <algorithm>
#include "ErrorHandling.h"
#include "Logger.h"
#include "XMLParser.h"



/// @brief The class XMLParser is bla bla.
///
/// Details (long description) bla bla
/// bla bla.



void XMLParser::parseXmlFile(char *xmlFilePath) {
    pugi::xml_parse_result result = xmlDocument.load_file(xmlFilePath);

	if (!result) {
		if (result.status == pugi::status_file_not_found) {
			ERR("File not found: %s", xmlFilePath);
		}
		else {
			bool ok = build_offset_data(xmlFilePath);
			if (!ok) {
				ERR("Error calculating line numbers of file '%s'", xmlFilePath);
			}

			ERR(
				"Error parsing file '%s', line %d, col: %d: %s",
				xmlFilePath,
				get_location(result.offset).first,
				get_location(result.offset).second,
				result.description()
			);
		}
	}
}




pugi::xpath_node_set XMLParser::xpathQuery(char *query) {
	pugi::xpath_node_set nodeSet;
	try {
		nodeSet = xmlDocument.select_nodes(query);
	}
	catch (const pugi::xpath_exception& e) {
		ERR("Select failed: %s", e.what());
	}

	return nodeSet;
}




bool XMLParser::build_offset_data(const char* file)
{
    FILE* f = fopen(file, "rb");
    if (!f) return false;

    ptrdiff_t offset = 0;

    char buffer[1024];
    size_t size;

    while ((size = fread(buffer, 1, sizeof(buffer), f)) > 0)
    {
        for (size_t i = 0; i < size; ++i)
            if (buffer[i] == '\n')
                offset_data.push_back(offset + i);

        offset += size;
    }

    fclose(f);

    return true;
}

std::pair<int, int> XMLParser::get_location(ptrdiff_t offset)
{
    std::vector<ptrdiff_t>::const_iterator it = std::lower_bound(offset_data.begin(), offset_data.end(), offset);
    size_t index = it - offset_data.begin();

    return std::make_pair(1 + index, index == 0 ? offset : offset - offset_data[index - 1]);
}
