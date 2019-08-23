
var backgroundCookie = $.cookie('backgroundImage');
var backgroundImageId = $.cookie('backgroundImageId');
var backgroundIndex = $.cookie('backgroundIndex');

function getBackground() {
//   if (!backgroundCookie) {
    var arrayBackground = new Array();

    
        arrayBackground[0] = "/images/bg.jpg";
          
    if (keywordGeoFoundIndex > 0) {
      backgroundIndex = keywordGeoFoundIndex;
    }
    else {
      var randomizer = Math.floor(Math.random() * arrayBackground.length);
      backgroundIndex = randomizer;
    }
    
   
    backgroundIndexCookie = $.cookie('backgroundIndex',backgroundIndex, { path: '/' });
    
    backgroundImage = arrayBackground[backgroundIndex];
    backgroundCookie = $.cookie('backgroundImage',backgroundImage, { path: '/' });    
      
    $.backstretch(backgroundImage);
    
//  } else {
//    $.backstretch(backgroundCookie);    
//  };  
}
getBackground();


function getBackgroundCalloutId() {
  if (!backgroundImageId) {
    var arrayBackgroundId = new Array();
    
        arrayBackgroundId[0] = "bg-show-1220";
      
        arrayBackgroundId[1] = "bg-show-1221";
      
        arrayBackgroundId[2] = "bg-show-1222";
      
        arrayBackgroundId[3] = "bg-show-1223";
      
        arrayBackgroundId[4] = "bg-show-1224";
      
        arrayBackgroundId[5] = "bg-show-1225";
      
    backgroundImageId = arrayBackgroundId[backgroundIndex];
    backgroundImageIdCookie = $.cookie('backgroundImageId',backgroundImageId, { path: '/' });  
  };
}
getBackgroundCalloutId();


$(document).ready(function() {
  if ($('body').hasClass('home')) {
    $('.pulsate').effect("pulsate", { times:1500 }, 1500);                
  };
  
  $('#camera-icon').click(function() {
    return false;
  });
  
  $('#camera-icon').mouseenter(function(){
    $('#'+backgroundImageId+'-callout').fadeIn(400);
  });
  
  $('.bg-callout').mouseleave(function(){
    $('#'+backgroundImageId+'-callout').fadeOut(400);
  });
});

