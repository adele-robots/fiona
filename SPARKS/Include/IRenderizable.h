#ifndef __I_RENDERIZABLE_H
#define __I_RENDERIZABLE_H

#include "Horde3D.h"

class IRenderizable {
public:
	virtual void render(void) = 0;
	virtual H3DRes getCamaraNode() = 0;
};


#endif
