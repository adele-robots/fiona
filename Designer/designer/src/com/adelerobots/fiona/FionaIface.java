package com.adelerobots.fiona;

public class FionaIface {

	String ifaceName;
	String providedBy;
	String requiredBy;
	String conId;
	
	public FionaIface(){
		this.ifaceName = "ifacename";
		this.providedBy = "unknown";
		this.requiredBy = "unknown";
		this.conId = "unknown";
	}

	public String getIfaceName() {
		return ifaceName;
	}

	public void setIfaceName(String ifaceName) {
		this.ifaceName = ifaceName;
	}

	public String getProvidedBy() {
		return providedBy;
	}

	public void setProvidedBy(String providedBy) {
		this.providedBy = providedBy;
	}

	public String getRequiredBy() {
		return requiredBy;
	}

	public void setRequiredBy(String requiredBy) {
		this.requiredBy = requiredBy;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}
	
}
