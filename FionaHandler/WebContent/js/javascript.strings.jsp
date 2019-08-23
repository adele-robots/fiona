<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import = "java.util.*" %>
<%@ page session="false" %>

function Property(key, value) {
    this.key = key;
    this.value = value;
}

function Properties() {
    this.propertyList = new Array();
    this.addProperty = function addProperty(property) {
        this.propertyList.push(property);
    }
    this.getProperty = function getProperty(key) {
        for (var i = 0 ;i < this.propertyList.length; i++) {
            var property = this.propertyList[i];
            if ( property.key.toUpperCase() == key.toUpperCase()) {
                return (property.value);
            }
        }
        return key + " undefined";
    }
}

var propertiesApp = new Properties();
var propertyApp = null;

function Tapp_(texto) {
    return propertiesApp.getProperty(texto);
}

<%
        ResourceBundle labelsApp = ResourceBundle.getBundle("resources.javascript");
        Enumeration enumerApp = labelsApp.getKeys();
        while ( enumerApp.hasMoreElements()) {
            String key = (String)enumerApp.nextElement();
            String value = labelsApp.getString(key);
        %>
			propertyApp = new Property('<%=key%>','<%=value%>');
			propertiesApp.addProperty(propertyApp);
        <%
        }       
%>
