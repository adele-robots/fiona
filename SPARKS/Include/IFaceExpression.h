#ifndef __I_FACE_EXPRESSION_H
#define __I_FACE_EXPRESSION_H


class IFaceExpression {
public:
	virtual void setFaceExpression(char *expressionName,float intensity) = 0;
};


#endif