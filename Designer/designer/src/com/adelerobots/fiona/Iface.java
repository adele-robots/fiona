package com.adelerobots.fiona;

//OJO, este objeto representa los elementos de interface de la GUI de composición
public class Iface {
	
	//Id
	String id;
	
	//Nombre
	String name;
		
	//outgoing/target
	String outgoing;
	
	//es proveida o requerida	
	boolean isProvided;
	
	//si ya se asignó a un FionaIface
	boolean isFilled;
	
	public Iface()
	{
		this.id="id";
		this.name="name";
		this.outgoing="outgoing";
		this.isProvided=false;
		
	}
	
	public void printIface()
	{
		System.out.println("Nombre del interfaz: " + this.name);
		System.out.println("Id del interfaz: " + this.id);
		System.out.println("Conexión de salida: " + this.outgoing);
		if (isProvided) System.out.println("Interfaz Provided: sí");
		else System.out.println("Interfaz Provided: no");
		
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(String outgoing) {
		this.outgoing = outgoing;
	}

	public boolean isProvided() {
		return isProvided;
	}

	public void setProvided(boolean isProvided) {
		this.isProvided = isProvided;
	}

	public boolean isFilled() {
		return isFilled;
	}

	public void setFilled(boolean isFilled) {
		this.isFilled = isFilled;
	}

}	


