#ifndef __I_DETECTED_FACE_POSITION_CONSUMER_H
#define __I_DETECTED_FACE_POSITION_CONSUMER_H


/// The class processa stream of face positions, one per frame.
/// 0 <= x <= 1, 0 <= y <= 1
/// if isFaceDetected == true, then (x, y) are meaningless.

class IDetectedFacePositionConsumer {
public:
	virtual void consumeDetectedFacePosition(bool isFaceDetected, double x, double y) = 0;
};


#endif
