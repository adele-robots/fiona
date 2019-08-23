#ifndef __MECA_TRONICA_SERIAL_H
#define __MECA_TRONICA_SERIAL_H

class MecaTronicaSerial
{
public:
	MecaTronicaSerial();	//Constructor
	~MecaTronicaSerial();	//Destructor
	
	/*Funciones para cambiar parámetros de los servos*/
	bool setTarget(unsigned short servo, unsigned short position);	//Envía el comando para mover el servo
	bool setSpeed(unsigned short servo, unsigned short speed);	//Cambia la velocidad de un servo
	bool setAcel(unsigned short servo, unsigned short acel);	//Cambia la aceleracion de un servo
	void setSpeedAcel(unsigned short servo, unsigned short speed, unsigned short acel);	//Cambia la velocidad y la aceleracion de un servo
	void resetConfig();	//Devuelve la velocidad y la aceleración a sus valores por defecto

	/*Funciones para mover servos de distintas formas*/
	void moverServo();	//Mueve un servo preguntando al usuario
	void moverServo(int servo, float grados);	//Mueve un servo
	void moverPANyTILT();	//Mueve PAN y TILT preguntando al usuario
	void moverPANyTILT(float gradosPAN, float gradosTILT);	//Mueve PAN y TILT
	void mover3Servos();	//Mueve tres servos preguntando al usuario
	void mover3Servos(float gradosPAN, float gradosTILT, float gradosROLL);	//Mueve tres servos
	void movTrozos(unsigned short servo, float grados);	//Mueve un servo con distintas aceleraciones
	void moVariab();	//Mueve un servo con distintas aceleraciones preguntando al usuario
	void mover3ServosTrozos();	//Mueve tres servos con distintas aceleraciones preguntando al usuario
	void mover3ServosTrozos(float gradosPAN, float gradosTILT, float gradosROLL);
	void resetPosic();	//Devuelve los servos a su posición inicial
	
	/*Funciones auxiliares*/
	unsigned short eligeServo();	//Para elegir servo
	unsigned short posicServo(unsigned short servo);	//Devuelve la posición actual de un servo
	unsigned short posicion (float grados);	//Convierte los grados en encho de pulso
	bool llego (unsigned short servo, unsigned short objetivo);	//Comprueba si un servo moviéndose alcanzó su objetivo
	void gestos(); //Movimientos de los servos
	void menu(); //Funcion menú para comprobar las demás funciones
};

#endif