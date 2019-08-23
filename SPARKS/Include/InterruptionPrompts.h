/// @file InterruptionPrompts.h
/// @brief InterruptionPrompts class declaration.


#ifndef __INTERRUPTION_PROMPTS_H
#define __INTERRUPTION_PROMPTS_H

#include <vector>


/// \brief Loads the interruptions file and stores and recover them.
///
/// This class parses an \e interruption \e file, and stores a series of
/// prompts, intended to be prepended randomly to the normal prompts
/// of the Voice User Interface.

class InterruptionPrompts
{
	std::vector<char *> interruptionPrompts;

public:
	char *getPrompt(int index) { return interruptionPrompts[index]; }
	void loadInterruptions(void);
	int getInterrupitonPromptNumber(void);
	void print(void);
};


#endif
