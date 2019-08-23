#ifndef __G711_H
#define __G711_H

int search(	int val, short *table, int size);

unsigned char linear2alaw( int pcm_val);

int alaw2linear(unsigned char a_val);

unsigned char linear2ulaw(int pcm_val);

short ulaw2linear(unsigned char u_val);

unsigned char alaw2ulaw(unsigned char aval);

unsigned char ulaw2alaw(unsigned char uval);

#endif
