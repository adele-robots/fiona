package com.adelerobots.fiona;

import java.util.ArrayList;
import java.util.List;

public class MultipleIfaz {
	
	String originSpark;
	
	List<String> destinySparks = new ArrayList<String>();
	
	String ifazMulti;
	
	String ifazPubSub;
	
	String type;
	
	Boolean multi;
	
	public MultipleIfaz(){
		this.originSpark = "unknown";
		this.ifazMulti = "unknown";
		this.type = "unknown";
		this.ifazPubSub = "unknown";
		this.multi = false;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOriginSpark() {
		return originSpark;
	}

	public void setOriginSpark(String originSpark) {
		this.originSpark = originSpark;
	}

	public List<String> getDestinySparks() {
		return destinySparks;
	}

	public void setDestinySparks(List<String> destinySparks) {
		this.destinySparks = destinySparks;
	}

	public String getIfazMulti() {
		return ifazMulti;
	}

	public void setIfazMulti(String ifazMulti) {
		this.ifazMulti = ifazMulti;
	}

	public Boolean getMulti() {
		return multi;
	}

	public void setMulti(Boolean multi) {
		this.multi = multi;
	}

	public String getIfazPubSub() {
		return ifazPubSub;
	}

	public void setIfazPubSub(String ifazPubSub) {
		this.ifazPubSub = ifazPubSub;
	}

}
