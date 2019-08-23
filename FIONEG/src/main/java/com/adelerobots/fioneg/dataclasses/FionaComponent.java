package com.adelerobots.fioneg.dataclasses;

import java.util.Collection;
import java.util.List;

import com.adelerobots.fioneg.entity.ConfigParamC;

public class FionaComponent {

	private String componentName;
	
	private Collection<String> componentIfaces;
	
	private List<ConfigParamC> componentParams;

	public FionaComponent()
	{
		this.componentName = "noName"; 
		
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public Collection<String> getComponentIfaces() {
		return componentIfaces;
	}

	public void setComponentIfaces(Collection<String> componentIfaces) {
		this.componentIfaces = componentIfaces;
	}

	public List<ConfigParamC> getComponentParams() {
		return componentParams;
	}

	public void setComponentParams(List<ConfigParamC> componentParams) {
		this.componentParams = componentParams;
	}
	
}
