function initialiser() {
	var map = new google.maps.Map(document.getElementById('map'), {
    zoom: 17,
    center: {lat: 48.41981498066815, lng: -71.05101943016047 }
  });

  map.addListener('click', function(event) {
    placerMarqueur(event.latLng, map);
  });
}

function placerMarqueur(latLng, map) {
  var marqueur = new google.maps.Marker({
    	position: latLng,
    	map: map
  	});
	
	map.panTo(latLng);
	alertPosition(latLng);
	
	marqueur.setDraggable(true);
	google.maps.event.addListener(marqueur, 'dragend', function(event) {
		alertPosition(event.latLng);
	});
	
}

function alertPosition(latLng){
	alert("Position du marqueur : "+latLng);
}