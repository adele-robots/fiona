#ifndef __AnimationFile_H
#define __AnimationFile_H

#include <string>

std::string trim(std::string& str);

class AnimationFile {

public:
	AnimationFile();
	AnimationFile(std::string output_state, std::string animation, char direction, float speed, float stopMin, float stopMax, std::string poseIn, std::string poseOut, float probability, char randomHeadMovement);
	AnimationFile(const AnimationFile & other);
	AnimationFile(std::string & line);

	std::string getOutputState() const;
	std::string getAnimation() const;
	char getDirection() const;
	float getSpeed() const;
	float getStopMin() const;
	float getStopMax() const;
	std::string getPoseIn() const;
	std::string getPoseOut() const;
	float getProbability() const;
	char getRandomHeadMovement() const;

private:
	void setOutputState(std::string output_state);
	void setAnimation(std::string animation);
	void setDirection(char direction);
	void setSpeed(float speed);
	void setStopMin(float stopMin);
	void setStopMax(float stopMax);
	void setStop(float stopMin, float stopMax);
	void setPoseIn(std::string poseIn);
	void setPoseOut(std::string poseOut);
	void setProbability(float probability);
	void setRandomHeadMovement(char randomHeadMovement);
	void setAll(std::string output_state, std::string animation, char direction, float speed, float stopMin, float stopMax, std::string poseIn, std::string poseOut, float probability, char randomHeadMovement);

public:
	friend std::istream& operator>> (std::istream& is, AnimationFile& anim);

private:
	std::string output_state;
	std::string animation;
	char direction;
	float speed;
	float stopMin;
	float stopMax;
	std::string poseIn;
	std::string poseOut;
	float probability;
	char randomHeadMovement;
};

#endif
