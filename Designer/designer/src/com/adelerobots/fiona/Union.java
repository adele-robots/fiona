package com.adelerobots.fiona;

public class Union {

	String id;
	String outgoing;
	
	public void Union(){
		this.id="id";
		this.outgoing="outg";
		
	}
	
	public void printUnion(){
		System.out.println("Union id: " + this.id);
		System.out.println("Union outgoing to: " + this.outgoing);
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
