package com.adelerobots.fiona;

public class Connector {

	String id;
	String outgoing;

	public void Connector(){
		this.id="id";
		this.outgoing="outg";
		
	}
	
	public void printConnector(){
		System.out.println("Connector id: " + this.id);
		System.out.println("Connector outgoing to: " + this.outgoing);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOutgoing() {
		return outgoing;
	}

	public void setOutgoing(String outgoing) {
		this.outgoing = outgoing;
	}
	
}
