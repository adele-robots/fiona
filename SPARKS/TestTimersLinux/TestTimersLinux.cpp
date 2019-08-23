#include <iostream>
#include <sys/time.h>
#include "StopWatch.h"

using namespace std;

int main()
{
	StopWatch *sW = new StopWatch ();
	
	sW->restart();
	
	int x;
	for (int i=0;i<10000000000;i++)
		x=x+5;
		
	cout<<"Time"<<sW->elapsedTime();
	
}	