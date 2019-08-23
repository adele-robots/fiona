#ifndef __Cookie_H
#define __Cookie_H

#include <string>

class Cookie {

public:
	Cookie();
	Cookie(std::string keyValue);
	Cookie(std::string key, std::string value);
	Cookie(const Cookie & other);

	std::string getKey() const;
	std::string getValue() const;

	void setKey(std::string key);
	void setValue(std::string value);
	void setKeyValue(std::string key, std::string value);

	friend std::ostream& operator<< (std::ostream& os, const Cookie& cookie);
	friend std::istream& operator>> (std::istream& is, Cookie& cookie);

private:
	std::string key;
	std::string value;
};

#endif
