package com.mkyong.analysis.location.mode;
import java.io.File;
import java.io.IOException;


import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import com.mkyong.analysis.location.mode.ServerLocation;
 
public class GetLocation {
 
  public static ServerLocation getLocation(String ipAddress) {
 
	File file = new File(
	    "lib/GeoLiteCity.dat");
	return getLocation(ipAddress, file.getAbsoluteFile());
 
  }
 
  public static ServerLocation getLocation(String ipAddress, File file) {
 
	ServerLocation serverLocation = null;
 
	try {
 
	serverLocation = new ServerLocation();
 
	LookupService lookup = new LookupService(file,LookupService.GEOIP_MEMORY_CACHE);
	Location locationServices = lookup.getLocation(ipAddress);
	
	if(locationServices == null) {
		serverLocation.setCountryCode(null);
		serverLocation.setCountryName(null);
		serverLocation.setRegion(null);
		serverLocation.setRegionName(null);
		serverLocation.setCity(null);
		serverLocation.setPostalCode(null);
		serverLocation.setLatitude(null);
		serverLocation.setLongitude(null);
		serverLocation.setAreaCode(null);
	}
	else {
		serverLocation.setCountryCode(locationServices.countryCode);
		serverLocation.setCountryName(locationServices.countryName);
		serverLocation.setRegion(locationServices.region);
		serverLocation.setRegionName(regionName.regionNameByCode(
	             locationServices.countryCode, locationServices.region));
		serverLocation.setCity(locationServices.city);
		serverLocation.setPostalCode(locationServices.postalCode);
		serverLocation.setLatitude(String.valueOf(locationServices.latitude));
		serverLocation.setLongitude(String.valueOf(locationServices.longitude));
		serverLocation.setAreaCode(String.valueOf(locationServices.area_code));
	}
 
	} catch (IOException e) {
		//System.err.println(e.getMessage());
	}
 
	return serverLocation;
 
  }
}