package com.adelerobots.fiona;

import java.util.Vector;

public class FionaComponent {
	
	//id
	String id;
	
	//nombre
	String name;
	
	//para instanciación de componentes
	String instanceName;
	
	//Path o identificador del archivo de configuración
	String configPath;
	
	//Lista de interfaces provided
	Vector <String> requiredIfaces;
	
	//Índice de multi-instanciación, 0 si no hay multi instanciación, 1 es el primero... 2...
	Integer instanceNumber;
	
	//Lista de interfaces required
	Vector <String> providedIfaces;
	
	Vector <String> outgoingIds;
	
	public FionaComponent(){
		this.id="_id";
		this.name= "name";
		this.configPath = "configPath";
		this.requiredIfaces = new Vector<String>();
		this.providedIfaces = new Vector<String>();
		this.outgoingIds = new Vector<String>();
		this.instanceNumber = 0;
	}
	
	public void addProvided(String iface){
		this.providedIfaces.add(iface);
		
	}
	
	//devuelve
	public void printComponent()
	{
		System.out.println("Nombre del componente: " + this.name);
		System.out.println("Id del componente: " + this.id);
		System.out.println("Path al fichero de configuración: " + this.configPath);
		System.out.println("Required Interfaces: ");
		System.out.println(requiredIfaces.toString());
		System.out.println("Provided Interfaces: ");
		System.out.println(providedIfaces.toString());
		System.out.println("Outgoing Id's: " + outgoingIds.toString());
		
		
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

	public String getConfigPath() {
		return configPath;
	}

	public void setConfigPath(String configPath) {
		this.configPath = configPath;
	}

	public Vector<String> getRequiredIfaces() {
		return requiredIfaces;
	}

	public void setRequiredIfaces(Vector<String> requiredIfaces) {
		this.requiredIfaces = requiredIfaces;
	}
	
	public void addToRequiredIfaces(String requiredIfaces) {
		this.requiredIfaces.add(requiredIfaces);
	}

	public Vector<String> getProvidedIfaces() {
		return providedIfaces;
	}

	public void setProvidedIfaces(Vector<String> providedIfaces) {
		this.providedIfaces = providedIfaces;
	}
	
	public void addToProvidedIfaces(String providedIfaces) {
		this.providedIfaces.add(providedIfaces);
	}

	public Vector<String> getOutgoingIds() {
		return outgoingIds;
	}

	public void setOutgoingIds(Vector<String> outgoingIds) {
		this.outgoingIds = outgoingIds;
	}
	
	public void addToOutgoingIds(String outgoingId) {
		this.outgoingIds.add(outgoingId);
	}

	public Integer getInstanceNumber() {
		return instanceNumber;
	}

	public void setInstanceNumber(Integer instanceNumber) {
		this.instanceNumber = instanceNumber;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

}
