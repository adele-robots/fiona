var directionsDisplay;
var directionsService;
var map;

function displayMap(lat, lon,markerTitle) {
	if(typeof markerTitle != "undefined"){
		markerTitle = "" + markerTitle + "";
		markerTitle = markerTitle.replace(/\//g,"");
	}
	// Crear un div con el mapa
	if (jQuery("#map_canvas").length > 0) {
		closeMapDialog();
	}

	var divMap = jQuery("<div id='map_canvas' class='mapa closable'></div>");
	jQuery("body").append(divMap);

	initialize(lat, lon,markerTitle);
}

function displayMapCalcRoute(lat, lon, start, end) {
	// Crear un div con el mapa
	if (jQuery("#map_canvas").length > 0) {
		closeMapDialog();
	}

	var divMap = jQuery("<div id='map_canvas' class='mapa closable'></div>");
	jQuery("body").append(divMap);

	initializeCalcRoute(lat, lon, start, end);
}

function initialize(lat, lon, markerTitle) {
	// create the map
	var latLon = new google.maps.LatLng(lat, lon);
	var myOptions = {
		zoom : 14,
		center : latLon,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var div = jQuery("#map_canvas")[0];
	map = new google.maps.Map(div, myOptions);

	var dialogOptions = {
		maxHeight : 300,
		minHeight : 300,
		maxWidth : 390,
		minWidth : 390,
		position : 'right',
		resizable : false,
		show : {
			effect : 'drop',
			direction : 'up'
		},
		close : closeMapDialog
	};
	jQuery("#map_canvas").dialog(dialogOptions);
	jQuery(".ui-widget-header")
			.css(
					"background",
					"url('"
							+ hostAddres
							+ "img/black_bar_thick.png') repeat-x scroll 50% 50% #CCCCCC");

	var center = map.getCenter();
	google.maps.event.trigger(map, 'resize');
	map.setCenter(center);
	
	// Añadir marcador en el centro
	var markerCenter = new google.maps.Marker({
	      position: latLon,
	      map: map,
	      title: (markerTitle || "A")
	});
}

function initializeCalcRoute(lat, lon, start, end) {
	directionsService = new google.maps.DirectionsService();
	directionsDisplay = new google.maps.DirectionsRenderer();

	// create the map

	var myOptions = {
		zoom : 14,
		center : new google.maps.LatLng(lat, lon),
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	var div = jQuery("#map_canvas")[0];
	map = new google.maps.Map(div, myOptions);
	directionsDisplay.setMap(map);
	var dialogOptions = {
		maxHeight : 300,
		minHeight : 300,
		maxWidth : 390,
		minWidth : 390,
		position : 'right',
		resizable : false,
		show : {
			effect : 'drop',
			direction : 'up'
		},
		close : closeMapDialog
	};
	jQuery("#map_canvas").dialog(dialogOptions);
	jQuery(".ui-widget-header")
			.css(
					"background",
					"url('"
							+ hostAddres
							+ "img/black_bar_thick.png') repeat-x scroll 50% 50% #CCCCCC");
	calcRoute("Moreda de Aller", "Oviedo");
	var center = map.getCenter();
	google.maps.event.trigger(map, 'resize');
	map.setCenter(center);

}

function calcRoute(start, end) {
	var start = "Moreda de Aller";
	var end = "Oviedo";
	var request = {
		origin : start,
		destination : end,
		travelMode : google.maps.TravelMode.DRIVING
	};
	directionsService.route(request, function(result, status) {
		if (status == google.maps.DirectionsStatus.OK) {
			directionsDisplay.setDirections(result);
		}
	});
}

function closeMapDialog() {
	jQuery("#map_canvas").dialog("destroy");
	jQuery("#map_canvas").remove();
}