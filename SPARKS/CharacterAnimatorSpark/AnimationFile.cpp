#include "AnimationFile.h"
#include <boost/tokenizer.hpp>

AnimationFile::AnimationFile() {
	this->output_state = "";
	this->animation = "";
	this->direction = 'D';
	this->speed = 1.0;
	this->stopMin = 0.0;
	this->stopMax = 0.0;
	this->probability = 1.0;
	this->randomHeadMovement = 'N';
}

AnimationFile::AnimationFile(std::string output_state, std::string animation, char direction, float speed, float stopMin, float stopMax, std::string poseIn, std::string poseOut, float probability, char randomHeadMovement) {
	this->output_state = output_state;
	this->animation = animation;
	this->direction = direction;
	this->speed = speed;
	this->stopMin = stopMin;
	this->stopMax = stopMax;
	this->poseIn = poseIn;
	this->poseOut = poseOut;
	this->probability = probability;
	this->randomHeadMovement = randomHeadMovement;
}

AnimationFile::AnimationFile(const AnimationFile & other) {
	this->output_state = other.output_state;
	this->animation = other.animation;
	this->direction = other.direction;
	this->speed = other.speed;
	this->stopMin = other.stopMin;
	this->stopMax = other.stopMax;
	this->poseIn = other.poseIn;
	this->poseOut = other.poseOut;
	this->probability = other.probability;
	this->randomHeadMovement = other.randomHeadMovement;
}

AnimationFile::AnimationFile(std::string & line) {
	boost::char_separator<char> sep("|");
	boost::tokenizer<boost::char_separator<char> > tok(line, sep);
	std::vector<std::string> vec;
	vec.clear();
	for(boost::tokenizer<boost::char_separator<char> >::iterator beg=tok.begin(); beg!=tok.end();++beg){
		std::string s(*beg);
		vec.push_back(trim(s));
	}

	if(vec.size() >= 10) {
		this->output_state			= vec[0];
		this->animation				= vec[1];
		this->direction				= *(vec[2].c_str());
		this->speed					= atof(vec[3].c_str());
		this->stopMin				= atof(vec[4].c_str());
		this->stopMax				= atof(vec[5].c_str());
		this->poseIn				= vec[6];
		this->poseOut				= vec[7];
		this->probability			= atof(vec[8].c_str());
		this->randomHeadMovement	= *(vec[9].c_str());
	}
}

std::string AnimationFile::getOutputState() const {
	return this->output_state;
}

std::string AnimationFile::getAnimation() const {
	return this->animation;
}

char AnimationFile::getDirection() const {
	return this->direction;
}

float AnimationFile::getSpeed() const {
	return this->speed;
}

float AnimationFile::getStopMin() const {
	return this->stopMin;
}

float AnimationFile::getStopMax() const {
	return this->stopMax;
}

std::string AnimationFile::getPoseIn() const {
	return this->poseIn;
}

std::string AnimationFile::getPoseOut() const {
	return this->poseOut;
}

float AnimationFile::getProbability() const {
	return this->probability;
}

char AnimationFile::getRandomHeadMovement() const {
	return this->randomHeadMovement;
}

void AnimationFile::setOutputState(std::string output_state) {
	this->output_state = output_state;
}

void AnimationFile::setAnimation(std::string animation) {
	this->animation = animation;
}

void AnimationFile::setDirection(char direction) {
	this->direction = direction;
}

void AnimationFile::setSpeed(float speed) {
	this->speed = speed;
}

void AnimationFile::setStopMin(float stopMin) {
	this->stopMin = stopMin;
}

void AnimationFile::setStopMax(float stopMax) {
	this->stopMax = stopMax;
}

void AnimationFile::setStop(float stopMin, float stopMax) {
	this->setStopMin(stopMin);
	this->setStopMax(stopMax);
}

void AnimationFile::setPoseIn(std::string poseIn) {
	this->poseIn = poseIn;
}

void AnimationFile::setPoseOut(std::string poseOut) {
	this->poseOut = poseOut;
}

void AnimationFile::setProbability(float probability) {
	this->probability = probability;
}

void AnimationFile::setRandomHeadMovement(char randomHeadMovement) {
	this->randomHeadMovement = randomHeadMovement;
}

void AnimationFile::setAll(std::string output_state, std::string animation, char direction, float speed, float stopMin, float stopMax, std::string poseIn, std::string poseOut, float probability, char randomHeadMovement) {
	this->setOutputState(output_state);
	this->setAnimation(animation);
	this->setDirection(direction);
	this->setSpeed(speed);
	this->setStop(stopMin, stopMax);
	this->setPoseIn(poseIn);
	this->setPoseOut(poseOut);
	this->setProbability(probability);
	this->setRandomHeadMovement(randomHeadMovement);
}

std::istream& operator>> (std::istream& is, AnimationFile& anim) {
	std::string line;
	if(getline(is, line) < 0) {
		std::cout << "Error reading AnimationFile" << std::endl;
		return is;
	}
	boost::char_separator<char> sep("|");
	boost::tokenizer<boost::char_separator<char> > tok(line, sep);
	std::vector<std::string> vec;
	vec.clear();
	for(boost::tokenizer<boost::char_separator<char> >::iterator beg=tok.begin(); beg!=tok.end();++beg){
		std::string s(*beg);
		vec.push_back(trim(s));
	}

	if(vec.size() >= 10) {
		anim.output_state		= vec[0];
		anim.animation			= vec[1];
		anim.direction			= vec[2][0];
		anim.speed				= atof(vec[3].c_str());
		anim.stopMin			= atof(vec[4].c_str());
		anim.stopMax			= atof(vec[5].c_str());
		anim.poseIn				= vec[6];
		anim.poseOut			= vec[7];
		anim.probability		= atof(vec[8].c_str());
		anim.randomHeadMovement	= vec[9][0];
	}
	return is;
}

std::string trim(std::string& str)
{
    size_t firstSp = str.find_first_not_of(' ');
    size_t firstTab = str.find_first_not_of('\t');
    size_t lastSp = str.find_last_not_of(' ');
    size_t lastTab = str.find_last_not_of('\t');
    if(firstSp == firstTab && lastSp == lastTab)
    	return str;
    if(firstTab > firstSp)
    	firstSp = firstTab;
    if(lastTab < lastSp)
    	lastSp = lastTab;
    if(firstSp == std::string::npos || lastSp == std::string::npos)
    	return str;
    std::string ret = str.substr(firstSp, (lastSp-firstSp+1));
    return trim(ret);
}
