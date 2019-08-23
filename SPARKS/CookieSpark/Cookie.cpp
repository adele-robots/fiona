#include "Cookie.h"

Cookie::Cookie() {
	this->key = "";
	this->value = "";
}

Cookie::Cookie(std::string keyValue) {
	this->key = keyValue.substr(0, keyValue.find('='));
	this->value = keyValue.substr(keyValue.find('=')+1);
}

Cookie::Cookie(std::string key, std::string value) {
	this->key = key;
	this->value = value;
}

Cookie::Cookie(const Cookie & other) {
	this->key = other.key;
	this->value = other.value;
}

std::string Cookie::getKey() const {
	return this->key;
}

std::string Cookie::getValue() const {
	return this->value;
}

void Cookie::setKey(std::string key) {
	this->key = key;
}

void Cookie::setValue(std::string value) {
	this->value = value;
}

void Cookie::setKeyValue(std::string key, std::string value) {
	this->key = key;
	this->value = value;
}

std::ostream& operator<< (std::ostream& os, const Cookie& cookie) {
	os << cookie.key << std::string("=") << cookie.value;
	return os;
}

std::istream& operator>> (std::istream& is, Cookie& cookie) {
	std::string c;
	is >> c;
	if(c.find('=') != std::string::npos) {
		// key=value
		cookie.key = c.substr(0, c.find('='));
		cookie.value = c.substr(c.find('=')+1);
	}
	else {
		// key = value
		cookie.key = c;
		is >> c;
		is >> c;
		cookie.value = c;
	}
	return is;
}
