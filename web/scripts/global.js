function pausecomp(millis) 
{
	var date = new Date();
	var curDate = null;
	
	do { curDate = new Date(); } 
	while(curDate-date < millis);
}

function chipselection(chip){
	
	if(typeof(anim)==="undefined")
		anim="init";
	
	//ocultamos el de standby
	$('div#standby').css({'visibility': 'hidden'});
	if ($.Edge.getComposition("EDGE-68107672").getStage().isPlaying())
	{
		setTimeout('chipselection('+chip+')',200);
		return;
	}
	else
		if(anim=="out")
		{
				//mostramos los chips
				$('div#out').css({'visibility': 'hidden'});
				$('div#chipsAnimation').css({'visibility': 'visible'});
				//alert('volvemos a la funcin')
				$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").play(0);
				anim="chips";
				setTimeout('chipselection('+chip+')',200);
				return;
		}
	if(anim=="init")
		anim="out";
	
	//ejecutamos el out
	if(anim=="out")
	{
			$('div#out').css({'visibility': 'visible'});
			$.Edge.getComposition("EDGE-68107672").getStage().play();
			setTimeout('chipselection('+chip+')',200);
			return;
	}
	
	if ($.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").isPlaying())
	{
		setTimeout('chipselection('+chip+')',200);
		return;
	}
	
	switch (chip)
	{
		//icon
		case 1:
			$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol("enlarge_icon").play();
			anim="init";
			last_clicked="enlarge_icon";
			break;
		//building
		case 2:
			$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol("enlarge_building").play();
			anim="init";
			last_clicked="enlarge_building";
			break;
		//books
		case 3:
			$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol("enlarge_books").play();
			anim="init";
			last_clicked="enlarge_books";
			break;
		//trolley
		case 4:
			$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol("enlarge_trolley").play();
			anim="init";
			last_clicked="enlarge_trolley";
			break;	
		//piggy
		case 5:
			$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol("enlarge_piggybank").play();
			anim="init";
			last_clicked="enlarge_piggybank";
			break;
		//rocket
		case 6:
			$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol("enlarge_rocket").play();
			anim="init";
			last_clicked="enlarge_rocket";
			break;
	}
}

function littleworldselection(){
	
	//ocultamos el de standby
	if ($.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").isPlaying())
	{
		setTimeout('littleworldselection()',200);
		return;
	}
	else
		{
				$('div#chipsAnimation').css({'visibility': 'hidden'});
				$('div#intro').css({'visibility': 'visible'});
				$.Edge.getComposition("EDGE-491969267").getStage().play();

		}
}

function changechip(clicked){
if(typeof(last_clicked)!="undefined" && last_clicked!=''){
	if($.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol(last_clicked).isPlaying()){
			setTimeout('changechip(\"'+clicked+'\")',200);
			return;
		}
		if (typeof(forward)==="undefined" || forward){
			$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol(last_clicked).playReverse();
			forward = false;
		}
	if($.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol(last_clicked).isPlaying()){
			setTimeout('changechip(\"'+clicked+'\")',200);
			return;
		}
	}
	last_clicked=clicked;
	$.Edge.getComposition("EDGE-112784366").getStage().getSymbol("Entrance").getSymbol(last_clicked).play();
	forward = true;
	
}