package com.mkyong.analysis.location.mode;

public class ServerLocation {
private String countryCode;
public  String countryName;
private String region;
private String regionName;
public  String city;
private String postalCode;
private String latitude;
private String longitude;
private String areaCode;
@Override
public String toString() {
return city + "," +countryName;
}
	/*public String toString() {
	 	*return city + " " + postalCode + ", " + regionName + " (" + region
	 		*+ "), " + countryName + " (" + countryCode + ") " + latitude
	 			*+ "," + longitude;
	 	}*/
public String getCity() {
return city;
}
public void setCity(String city) {
this.city = city;
}
public String getCountryCode() {
return countryCode;
}
public void setCountryCode(String countryCode) {
this.countryCode = countryCode;
}
public String getCountryName() {
return countryName;
}
public void setCountryName(String countryName) {
this.countryName = countryName;
}
public String getLatitude() {
return latitude;
}
public void setLatitude(String latitude) {
this.latitude = latitude;
}
public String getLongitude() {
return longitude;
}
public void setLongitude(String longitude) {
this.longitude = longitude;
}
public String getPostalCode() {
return postalCode;
}
public void setPostalCode(String postalCode) {
this.postalCode = postalCode;
}
public String getRegion() {
return region;
}
public void setRegion(String region) {
this.region = region;
}
public String getRegionName() {
return regionName;
}
public void setRegionName(String regionName) {
this.regionName = regionName;
}
public String getAreaCode() {
return areaCode;
}
public void setAreaCode(String areaCode) {
this.areaCode = areaCode;
}
}
