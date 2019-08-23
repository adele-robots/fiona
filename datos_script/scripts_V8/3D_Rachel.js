/*************************************************** JS PERLIN NOISE *****************************************************************************/
// Ported from Stefan Gustavson's java implementation
// http://staffwww.itn.liu.se/~stegu/simplexnoise/simplexnoise.pdf
// Read Stefan's excellent paper for details on how this code works.
//
// Sean McCullough banksean@gmail.com
 
/**
 * You can pass in a random number generator object if you like.
 * It is assumed to have a random() method.
 */
var SimplexNoise = function(r) {
	if (r == undefined) r = Math;
  this.grad3 = [[1,1,0],[-1,1,0],[1,-1,0],[-1,-1,0], 
                                 [1,0,1],[-1,0,1],[1,0,-1],[-1,0,-1], 
                                 [0,1,1],[0,-1,1],[0,1,-1],[0,-1,-1]]; 
  this.p = [];
  for (var i=0; i<256; i++) {
	  this.p[i] = Math.floor(r.random()*256);
  }
  // To remove the need for index wrapping, double the permutation table length 
  this.perm = []; 
  for(var i=0; i<512; i++) {
		this.perm[i]=this.p[i & 255];
	} 
 
  // A lookup table to traverse the simplex around a given point in 4D. 
  // Details can be found where this table is used, in the 4D noise method. 
  this.simplex = [ 
    [0,1,2,3],[0,1,3,2],[0,0,0,0],[0,2,3,1],[0,0,0,0],[0,0,0,0],[0,0,0,0],[1,2,3,0], 
    [0,2,1,3],[0,0,0,0],[0,3,1,2],[0,3,2,1],[0,0,0,0],[0,0,0,0],[0,0,0,0],[1,3,2,0], 
    [0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0], 
    [1,2,0,3],[0,0,0,0],[1,3,0,2],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,3,0,1],[2,3,1,0], 
    [1,0,2,3],[1,0,3,2],[0,0,0,0],[0,0,0,0],[0,0,0,0],[2,0,3,1],[0,0,0,0],[2,1,3,0], 
    [0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0],[0,0,0,0], 
    [2,0,1,3],[0,0,0,0],[0,0,0,0],[0,0,0,0],[3,0,1,2],[3,0,2,1],[0,0,0,0],[3,1,2,0], 
    [2,1,0,3],[0,0,0,0],[0,0,0,0],[0,0,0,0],[3,1,0,2],[0,0,0,0],[3,2,0,1],[3,2,1,0]]; 
};
 
SimplexNoise.prototype.dot = function(g, x, y) { 
	return g[0]*x + g[1]*y;
};
 
SimplexNoise.prototype.noise = function(xin, yin) { 
  var n0, n1, n2; // Noise contributions from the three corners 
  // Skew the input space to determine which simplex cell we're in 
  var F2 = 0.5*(Math.sqrt(3.0)-1.0); 
  var s = (xin+yin)*F2; // Hairy factor for 2D 
  var i = Math.floor(xin+s); 
  var j = Math.floor(yin+s); 
  var G2 = (3.0-Math.sqrt(3.0))/6.0; 
  var t = (i+j)*G2; 
  var X0 = i-t; // Unskew the cell origin back to (x,y) space 
  var Y0 = j-t; 
  var x0 = xin-X0; // The x,y distances from the cell origin 
  var y0 = yin-Y0; 
  // For the 2D case, the simplex shape is an equilateral triangle. 
  // Determine which simplex we are in. 
  var i1, j1; // Offsets for second (middle) corner of simplex in (i,j) coords 
  if(x0>y0) {i1=1; j1=0;} // lower triangle, XY order: (0,0)->(1,0)->(1,1) 
  else {i1=0; j1=1;}      // upper triangle, YX order: (0,0)->(0,1)->(1,1) 
  // A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and 
  // a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where 
  // c = (3-sqrt(3))/6 
  var x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed coords 
  var y1 = y0 - j1 + G2; 
  var x2 = x0 - 1.0 + 2.0 * G2; // Offsets for last corner in (x,y) unskewed coords 
  var y2 = y0 - 1.0 + 2.0 * G2; 
  // Work out the hashed gradient indices of the three simplex corners 
  var ii = i & 255; 
  var jj = j & 255; 
  var gi0 = this.perm[ii+this.perm[jj]] % 12; 
  var gi1 = this.perm[ii+i1+this.perm[jj+j1]] % 12; 
  var gi2 = this.perm[ii+1+this.perm[jj+1]] % 12; 

  // Calculate the contribution from the three corners 
  var t0 = 0.5 - x0*x0-y0*y0; 
  if(t0<0) n0 = 0.0; 
  else { 
    t0 *= t0; 
    n0 = t0 * t0 * this.dot(this.grad3[gi0], x0, y0);  // (x,y) of grad3 used for 2D gradient 
  } 
  var t1 = 0.5 - x1*x1-y1*y1; 
  if(t1<0) n1 = 0.0; 
  else { 
    t1 *= t1; 
    n1 = t1 * t1 * this.dot(this.grad3[gi1], x1, y1); 
  }
  var t2 = 0.5 - x2*x2-y2*y2; 
  if(t2<0) n2 = 0.0; 
  else { 
    t2 *= t2; 
    n2 = t2 * t2 * this.dot(this.grad3[gi2], x2, y2); 
  } 
  // Add contributions from each corner to get the final noise value. 
  // The result is scaled to return values in the interval [-1,1]. 
  return 70.0 * (n0 + n1 + n2); 
};
 
// 3D simplex noise 
SimplexNoise.prototype.noise3d = function(xin, yin, zin) { 
  var n0, n1, n2, n3; // Noise contributions from the four corners 
  // Skew the input space to determine which simplex cell we're in 
  var F3 = 1.0/3.0; 
  var s = (xin+yin+zin)*F3; // Very nice and simple skew factor for 3D 
  var i = Math.floor(xin+s); 
  var j = Math.floor(yin+s); 
  var k = Math.floor(zin+s); 
  var G3 = 1.0/6.0; // Very nice and simple unskew factor, too 
  var t = (i+j+k)*G3; 
  var X0 = i-t; // Unskew the cell origin back to (x,y,z) space 
  var Y0 = j-t; 
  var Z0 = k-t; 
  var x0 = xin-X0; // The x,y,z distances from the cell origin 
  var y0 = yin-Y0; 
  var z0 = zin-Z0; 
  // For the 3D case, the simplex shape is a slightly irregular tetrahedron. 
  // Determine which simplex we are in. 
  var i1, j1, k1; // Offsets for second corner of simplex in (i,j,k) coords 
  var i2, j2, k2; // Offsets for third corner of simplex in (i,j,k) coords 
  if(x0>=y0) { 
    if(y0>=z0) 
      { i1=1; j1=0; k1=0; i2=1; j2=1; k2=0; } // X Y Z order 
      else if(x0>=z0) { i1=1; j1=0; k1=0; i2=1; j2=0; k2=1; } // X Z Y order 
      else { i1=0; j1=0; k1=1; i2=1; j2=0; k2=1; } // Z X Y order 
    } 
  else { // x0<y0 
    if(y0<z0) { i1=0; j1=0; k1=1; i2=0; j2=1; k2=1; } // Z Y X order 
    else if(x0<z0) { i1=0; j1=1; k1=0; i2=0; j2=1; k2=1; } // Y Z X order 
    else { i1=0; j1=1; k1=0; i2=1; j2=1; k2=0; } // Y X Z order 
  } 
  // A step of (1,0,0) in (i,j,k) means a step of (1-c,-c,-c) in (x,y,z), 
  // a step of (0,1,0) in (i,j,k) means a step of (-c,1-c,-c) in (x,y,z), and 
  // a step of (0,0,1) in (i,j,k) means a step of (-c,-c,1-c) in (x,y,z), where 
  // c = 1/6.
  var x1 = x0 - i1 + G3; // Offsets for second corner in (x,y,z) coords 
  var y1 = y0 - j1 + G3; 
  var z1 = z0 - k1 + G3; 
  var x2 = x0 - i2 + 2.0*G3; // Offsets for third corner in (x,y,z) coords 
  var y2 = y0 - j2 + 2.0*G3; 
  var z2 = z0 - k2 + 2.0*G3; 
  var x3 = x0 - 1.0 + 3.0*G3; // Offsets for last corner in (x,y,z) coords 
  var y3 = y0 - 1.0 + 3.0*G3; 
  var z3 = z0 - 1.0 + 3.0*G3; 
  // Work out the hashed gradient indices of the four simplex corners 
  var ii = i & 255; 
  var jj = j & 255; 
  var kk = k & 255; 
  var gi0 = this.perm[ii+this.perm[jj+this.perm[kk]]] % 12; 
  var gi1 = this.perm[ii+i1+this.perm[jj+j1+this.perm[kk+k1]]] % 12; 
  var gi2 = this.perm[ii+i2+this.perm[jj+j2+this.perm[kk+k2]]] % 12; 
  var gi3 = this.perm[ii+1+this.perm[jj+1+this.perm[kk+1]]] % 12; 
  // Calculate the contribution from the four corners 
  var t0 = 0.6 - x0*x0 - y0*y0 - z0*z0; 
  if(t0<0) n0 = 0.0; 
  else { 
    t0 *= t0; 
    n0 = t0 * t0 * this.dot(this.grad3[gi0], x0, y0, z0); 
  }
  var t1 = 0.6 - x1*x1 - y1*y1 - z1*z1; 
  if(t1<0) n1 = 0.0; 
  else { 
    t1 *= t1; 
    n1 = t1 * t1 * this.dot(this.grad3[gi1], x1, y1, z1); 
  } 
  var t2 = 0.6 - x2*x2 - y2*y2 - z2*z2; 
  if(t2<0) n2 = 0.0; 
  else { 
    t2 *= t2; 
    n2 = t2 * t2 * this.dot(this.grad3[gi2], x2, y2, z2); 
  } 
  var t3 = 0.6 - x3*x3 - y3*y3 - z3*z3; 
  if(t3<0) n3 = 0.0; 
  else { 
    t3 *= t3; 
    n3 = t3 * t3 * this.dot(this.grad3[gi3], x3, y3, z3); 
  } 
  // Add contributions from each corner to get the final noise value. 
  // The result is scaled to stay just inside [-1,1] 
  return 32.0*(n0 + n1 + n2 + n3); 
};

/**************************************************************** END PERLIN NOISE *******************************************************************/

// Global variables
var conteo = 90;
var primer_conteo = false;
var segundo_conteo = false;
var movingLips=false;
var alfa=0.0;
var animationLaunched = false;
var firstMove = false;
var secondMove = false;
var ojos_abiertos = true;
var tiempo_cerrados = new Date();
var aleatorio= Math.floor((Math.random()*60)+30);
var abriendo = false;
var abriendo_nivel = 0;
var cerrando = false;
var cerrando_nivel = 0;
var centro_a_oeste = false;
var oeste_a_centro = false;
var centro_a_este = false;
var este_a_centro = false;
var centro_a_norte = false;
var norte_a_centro = false;
var centro_a_sur = false;
var sur_a_centro = false;
var contador_rotacion_x = 0;
var contador_rotacion_y = 0;
var aleatorio_giro= Math.floor((Math.random()*6)+2);


var pending_tasks = new Array();

function execute_order_rotate(type_t, start_x, end_x, start_y, end_y, time)
{
	
	var initial_time = Date.now();
	var registro = new Array(type_t, start_x, end_x, start_y, end_y, time, initial_time);
	pending_tasks.push ( registro );
	
	
}

function execute_order_expression(type_t, target_t, start, end, time)
{
	
	var initial_time = Date.now();
	var registro = new Array(type_t, target_t, start, end, time, initial_time);
	pending_tasks.push ( registro );
	
	
}

function process(registro, pos)
{
	//En funcion de la orden que va a ser ejecutada se procesa el registro
	
	if(registro[0] == "setFaceExpressionRequired")
	{
		print("El registro de 0 es setFaceExpressionRequired");
		//calculamos el cambio que tiene que hacerse en el morph
		
		//En primer lugar calculamos el cambio por milisegundo
		//(end-start)/time
		var change = ((registro[3]-registro[2])/registro[4])
		print("Hay que cambiar por milisegundo change", change);
	
		//Calculamos los milisegundos transcurridos desde que se efectuo la orden
		var lapsed = (Date.now() - registro[5]);
		print("Desde que se ordenó la tarea ha pasado ", lapsed," milisegundos");
		
		//Si ha transcurrido el tiempo total de la orden, la ejecutamos poniendo el valor final y eliminamos la tarea
		if(lapsed >= registro[4])
		{
			setFaceExpressionRequired(component,registro[1], registro[3]);
			pending_tasks.splice( pos, 1 );
			
		}
		else
		{
			//sino ha trancurrido modificamos solo la parte proporcional
			print("vamos a ejecutar esto: setFaceExpressionRequired(component, ", registro[1], ",", registro[2]+(change*lapsed));
			setFaceExpressionRequired(component, registro[1], registro[2] + (change * lapsed));
		}
		
	}else
		
	if (registro[0] == "rotateHeadRequired")
	{
		
		//En primer lugar calculamos el cambio por milisegundo tanto de la x como de la y
		//(end-start)*time
		
		var change_x = ((registro[2]-registro[1])/registro[5])
		var change_y = ((registro[4]-registro[3])/registro[5])
		
		//Calculamos los milisegundos transcurridos desde que se efectuo la orden
		var lapsed = (Date.now() - registro[6]);
				
		//Si ha transcurrido el tiempo total de la orden, la ejecutamos poniendo el valor final y eliminamos la tarea
		if(lapsed >= registro[5])
		{
			rotateHeadRequired(component,registro[2], registro[4]);
			pending_tasks.splice( pos, 1 );
			
		}
		else
		{
			//sino ha trancurrido modificamos solo la parte proporcional
			rotateHeadRequired(component, registro[1]+(change_x*lapsed), registro[3]+(change_y*lapsed));
		}
	}
	
}

function check_pending_tasks ()
{

	log("[JAVASCRIPT] - Entra en check_pending_tasks",1);
	
	//Recorremos el array de tareas
	for (pos=0;pos<pending_tasks.length;pos++)
	{
	
		print("Tarea ",pos, " leida");
		var registro = pending_tasks[pos]
		process(registro,pos);
		
	}
}

function Initialize() {
	
	print("Vamos a hacer la llamada a Simplex");
	//print("Resultado de Simplex: ",SimplexNoise.prototype.noise (2,3));

	//execute_order_expression("setFaceExpressionRequired", "PHMMouthOpenWide", "0", "1", "30000")
	//execute_order_rotate("rotateHeadRequired", "0", "180", "0", "0", "30000")
	//execute_order("setFaceExpressionRequired", "PHMEyesClosedR", "0", "1", "1");
	//execute_order("setFaceExpressionRequired", "PHMEyesClosedL", "0", "1", "1");
	
	/* Auxiliar built-in functions */
	//log("Logged message",1); //1 - INFO, 2 - WARN, 3 - ERROR

	/* ICamera calls */
	//setCameraPositionRequired(component,0.63,166,50);
	//setCameraRotationRequired(component,10.1,4.2,4.3);
	//setCameraParametersRequired(component,false,45, 80,100);

	/* IFaceExpression call */
	//setFaceExpressionRequired(component,"PHMMouthOpenWide", 1);


	/* IFlow<char*> call */
	//processDataRequired(component,"hello everybody, how do you do?");
//PHMSmileSimpleL" "PHMNostrilsFlare" "PHMNoseWrinkle" "PHMNoseWidth" "PHMNoseHeight PHMMouthHeight"

/*setFaceExpressionRequired(component,"PHMNoseHeight", 0);
setFaceExpressionRequired(component,"PHMNoseWidth", -0.9);
setFaceExpressionRequired(component,"Front_style_01", 1);
setFaceExpressionRequired(component,"FBMBasicFemale", 1);
*/

	/* INeck call */
   rotateHeadRequired(component,0,0);
  

	/* IEyes call */
//	rotateEyeRequired(component,0,-10);
	

	/* IAnimation call */
	//playAnimationRequired(component,"mi-mocap.anim");

	/* IControlVoice calls */
	//startSpeakingRequired(component);
	//stopSpeakingRequired(component);
	//startVoiceRequired(component);

}

/**************************************************************************************************************************************************/
/******************************************************* NATURAL RANDOM MOVEMENT *************************************************************************/
/**************************************************************************************************************************************************/
var r1=0, r2=0, r3=0, r4=0
// Velocidad de los movimientos aleatorios
var inc_x = 0.005
var inc_y = 0.004
// Hasta donde se realizan los movimientos
var random_head_movement_strenght_x = 0.2
var random_head_movement_strenght_y = 0.07
var pPan, pTilt
var	snx = new SimplexNoise();
var	sny = new SimplexNoise();

function updateHeadRamdomMovements()
{
	
	var rx, ry


	rx = snx.noise(r1,r2)
	ry = sny.noise(r3,r4)
	
	r1 += inc_x;
	r2 += inc_x;
	r3 += inc_y;
	r4 += inc_y;


	pPan = random_head_movement_strenght_x * 180 * rx / 3.1416;
	pTilt = random_head_movement_strenght_y * 180 * ry / 3.1416;
	
	print('pPan: ', pPan)
	print('pTilt: ', pTilt)
	
}

/**************************************************************************************************************************************************/
/******************************************************* END NATURAL MOVEMENT *********************************************************************/
/**************************************************************************************************************************************************/





// FrameEventSubscriber implementation example. This method is called from 
// Character3DSpark with every new rendered frame
function notifyFrameEvent(){

updateHeadRamdomMovements()
rotateHeadRequired(component,pPan,pTilt)

setFaceExpressionRequired(component,"PHMSmileSimpleL", 0.5);
setFaceExpressionRequired(component,"PHMSmileSimpleR", 0.5);

/* VoiceStartSpark functionality implemented */
	/*if (movingLips){		
		setFaceExpressionRequired(component,"vis-eh_ShapeKey",alfa);
		if (alfa == 0.0){
			alfa=0.5;
		}
		else if (alfa == 0.5){
			alfa=1;
		}
		else if (alfa == 1){
			alfa=0;
		}
	}
	else{
		setFaceExpressionRequired(component,"vis-eh_ShapeKey",0.0);
	}*/
/* END ** VoiceStartSpark functionality implemented */

check_pending_tasks ()

//  	manageNostrils()
	checkEyesBlinking()
	
//	moveNeck();
//	checkEyes();
	
	conteo = conteo + 1;
	if(conteo>100){
		conteo = 0;
	}
	/*if(conteo>350){
		if(!primer_conteo){
			//processDataRequired(component,"hello everybody, how do you do?");
			//primer_conteo = true;
		}
	}	
*/
/* Basic example to check animations */
	//conteo = conteo + 0.2;

		/*if(!animationLaunched){
			playAnimationRequired(component,"mi-mocap.anim");
			animationLaunched = true;
		}
		else 
   if(conteo>100){
		 if(!firstMove){
				print("1");
				rotateHeadRequired(component,0,50);
				firstMove = true;
				conteo = 0;
			}
			else if(!secondMove){
				print("2");
				rotateHeadRequired(component,20,0);			
				secondMove = true;
				conteo = 0;
			}
			else{
				print("3");
				rotateHeadRequired(component,0,-50);
				firstMove = false;
				secondMove = false;
				conteo = 0;
			}
		}
	*}
	else
		setCameraPositionRequired(component,0,150,conteo+30);
*/
/* END ** Basic example to check animations */
}
/********************************************************************************************************************/
/****************************************************** BLINK *******************************************************/
/********************************************************************************************************************/

/***********Basic eye blinking functionality with conteo **********/
function checkEyesBlinking()
{
	
	if (conteo % aleatorio == 0)
	{
		/*setFaceExpressionRequired(component,"BLINK", 0.8);*/
		cerrando = true;
		abriendo_nivel = 0.8;
	}
	if(cerrando)
	
{
		setFaceExpressionRequired(component,"PHMEyesClosedL", cerrando_nivel);
		setFaceExpressionRequired(component,"PHMEyesClosedR", cerrando_nivel);
		cerrando_nivel = cerrando_nivel+0.2;
	}
	if(cerrando_nivel == 0.8)
	{
		abriendo = true;
		cerrando = false;
	}
	if(abriendo)
	{
		setFaceExpressionRequired(component,"PHMEyesClosedL", abriendo_nivel);
                setFaceExpressionRequired(component,"PHMEyesClosedR", abriendo_nivel);

		  abriendo_nivel = abriendo_nivel-0.2;
	}
	if(abriendo_nivel == 0)
	{
		abriendo = false;
		aleatorio= Math.floor((Math.random()*60)+30);
	}
	
}
/********  END ***Basic eye blinking functionality with conteo **********/

/***********Basic eye blinking functionality with sleep ***********
function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 10e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}
var aleatorio= Math.floor((Math.random()*7)+4);
function checkEyesBlinking()
{
	var d = new Date();
	print(d.getSeconds());
	
	if((d.getSeconds())%aleatorio==0){
		setFaceExpressionRequired(component,"BLINK", 0.8);
		sleep(1000);
		setFaceExpressionRequired(component,"BLINK", 0);
		setFaceExpressionRequired(component,"BLINK", 0.8);
		aleatorio= Math.floor((Math.random()*7)+4);
	}
	else{
		setFaceExpressionRequired(component,"BLINK", 0);
				
	}

}
/*********** END Basic eye blinking functionality with sleep ***********

/********************************************************************************************************************/
/****************************************************** NECK *******************************************************/
/********************************************************************************************************************/

function moveNeck ()
{

		if(!centro_a_oeste && !oeste_a_centro && !centro_a_este && !este_a_centro && !centro_a_norte &&	!norte_a_centro && !centro_a_sur && !sur_a_centro)
		{
	
			if(conteo%4 == 0)
			{
				centro_a_oeste = true;
				//ramdom de hasta donde gira
			   aleatorio_giro= Math.floor((Math.random()*6)+4);
			}
			if(conteo%4 == 1)
			{
				centro_a_este = true;
				//ramdom de hasta donde gira
			  aleatorio_giro= Math.floor((Math.random()*6)+4);
			}
			if(conteo%4 == 2)
			{
				centro_a_norte = true;
				//ramdom de hasta donde gira
				aleatorio_giro= Math.floor((Math.random()*2)+1);
			}
			if(conteo%4 == 3)
			{
				centro_a_sur = true;
				//ramdom de hasta donde gira
				aleatorio_giro= Math.floor((Math.random()*2)+1);
			}
	
			
		}
		// movimiento ligero hacia la izquierda
		if(centro_a_oeste)
		{
			contador_rotacion_x = contador_rotacion_x + 1;
			rotateHeadRequired(component,contador_rotacion_x/5,0);	
			if(contador_rotacion_x/5 == aleatorio_giro)
			{
				//secondMove = true;
				centro_a_oeste = false;
				oeste_a_centro = true;
			}
			
		}
		//vuelta al centro desde la izquierda
		if(oeste_a_centro)
		{
			contador_rotacion_x= contador_rotacion_x - 1;
			rotateHeadRequired(component,contador_rotacion_x/5,0);	
			if(contador_rotacion_x/5 == 0)
			{
				centro_a_oeste = false;
				oeste_a_centro = false;
			}
			
		}
		//movimiento ligero hacia la derecha
		if(centro_a_este)
		{
			contador_rotacion_x = contador_rotacion_x - 1;
			rotateHeadRequired(component,contador_rotacion_x/5,0);	
			if(contador_rotacion_x/5 == - aleatorio_giro)
			{
				centro_a_este = false;
				este_a_centro = true;
			}
			
		}
		//vuelta al centro desde la derecha
		if(este_a_centro)
		{
			contador_rotacion_x = contador_rotacion_x + 1;
			rotateHeadRequired(component,contador_rotacion_x/5,0);	
			if(contador_rotacion_x/5 == 0)
			{
				centro_a_este = false;
				este_a_centro = false;
			}
			
		}
		
		//movimiento ligero hacia abajo
		if(centro_a_sur)
		{
			contador_rotacion_y = contador_rotacion_y - 1;
			rotateHeadRequired(component,0,contador_rotacion_y/5);	
			if(contador_rotacion_y/5 == - aleatorio_giro)
			{
				centro_a_sur = false;
				sur_a_centro = true;
			}
			
		}
		//vuelta al centro desde abajo
		if(sur_a_centro)
		{
			contador_rotacion_y = contador_rotacion_y + 1;
			rotateHeadRequired(component,0,contador_rotacion_y/5);	
			if(contador_rotacion_y/5 == 0)
			{
				centro_a_sur = false;
				sur_a_centro = false;
			}
			
		}
			//movimiento ligero hacia arriba
		if(centro_a_norte)
		{
			contador_rotacion_y = contador_rotacion_y + 1;
			rotateHeadRequired(component,0,contador_rotacion_y/5);	
			if(contador_rotacion_y/5 == aleatorio_giro)
			{
				centro_a_norte = false;
				norte_a_centro = true;
			}
			
		}
		//vuelta al centro desde arriba
		if(norte_a_centro)
		{
			contador_rotacion_y = contador_rotacion_y - 1;
			rotateHeadRequired(component,0,contador_rotacion_y/5);	
			if(contador_rotacion_y/5 == 0)
			{
				centro_a_norte = false;
				norte_a_centro = false;
			}
			
		}
	
}

/********************************************************************************************************************/
/****************************************************** EYES *******************************************************/
/********************************************************************************************************************/

function checkEyes()
{
	/* IEyes call */
	// aumnetar x gira ojos hacia la derecha de Ada, disminuir a la izquierda
	//aumentar y sube los ojos
	//rotateEyeRequired(component,x,y);
	rotateEyeRequired(component, - (contador_rotacion_x/5),-10);
	
	
}
/********************************************************************************************************************/
/****************************************************** NOSTRILS *******************************************************/
/********************************************************************************************************************/

function manageNostrils()
{
	var d = new Date();
	print(d.getSeconds());
	if((d.getSeconds())%3==0){
		setFaceExpressionRequired(component,"PHMNostrilsFlare", 0);
	
	}
	else{
		setFaceExpressionRequired(component,"PHMNostrilsFlare", 0.8);
		
	}
}

/* Very basic eyes blinking functionality implemented
function checkEyesBlinking(){
        var d = new Date();
        print(d.getSeconds());
        

        if(((d.getSeconds())%6==0) && ojos_abiertos){
                setFaceExpressionRequired(component,"BLINK", 0.8);
                ojos_abiertos = false;
                tiempo_cerrados = new Date();

        }else{

        var tiempo_ahora = new Date();
        	if(!ojos_abiertos )*///(tiempo_ahora.getSeconds()-tiempo_cerrados.getSeconds())>0.5 )
        /*	{
               	 setFaceExpressionRequired(component,"BLINK", 0);
               	 ojos_abiertos=true;
               	 tiempo_cerrados=0;
        	}
	}
}
END **Very basic eyes blinking functionality implemented */


//IFlow<char*> implementation
function processData(text) {
	print("[JAVASCRIPT] - processData, TEXT:",text);
	//processDataRequired("hello elvira");
}

//IControlVoice implementation example
function startSpeaking(){
	print("[JAVASCRIPT] - startSpeaking");
	processDataRequired(component,"hello everybody");
}

function stopSpeaking(){
	//print("[JAVASCRIPT] - stopSpeaking");
	movingLips=false;
}

function startVoice(){
	//print("[JAVASCRIPT] - startVoice");
	movingLips=true;
}

//IFaceExpression implementation example
function setFaceExpression(expressionName, intensity) {
	print("setFaceExpression en Javascript !!","\n");
  print("y expressionName es: ", expressionName);

}

//ICamera implementation example
function setCameraPosition(X,Y,Z){
	print("[JAVASCRIPT] - setCameraPosition!!","\n");
  print("y las coordenadas son: ", X,",",Y,",",Z);	
}

function setCameraRotation( X, Y, Z){
	print("[JAVASCRIPT] - setCameraRotation!!","\n");
  print("y las coordenadas son: ", X,",",Y,",",Z);	
}
	
function setCameraParameters(IsOrtho,VisionAngle,nearClippingPlane,FarClippingPlane){
	print("[JAVASCRIPT] - setCameraParameters!!","\n");	
}


Initialize();

