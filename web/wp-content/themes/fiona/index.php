<?php
/**
 * The main template file.
 *
 * This is the most generic template file in a WordPress theme
 * and one of the two required files for a theme (the other being style.css).
 * It is used to display a page when nothing more specific matches a query.
 * E.g., it puts together the home page when no home.php file exists.
 * Learn more: http://codex.wordpress.org/Template_Hierarchy
 *
 * @package WordPress
 * @subpackage Fiona
 */

get_header(); ?>

<!--Adobe Edge Runtime-->
    <script type="text/javascript" charset="utf-8" src="intro_edgePreload.js"></script>
    <script type="text/javascript" charset="utf-8" src="out_edgePreload.js"></script>
    <script type="text/javascript" charset="utf-8" src="standby_edgePreload.js"></script>
    <script type="text/javascript" charset="utf-8" src="chipsAnimation_edgePreload.js"></script>
<!--Adobe Edge Runtime End-->

	<div id="intro" class="EDGE-491969267" style="visibility:visible;left:-72px;"></div> 
	<div id="out" class="EDGE-68107672" style="visibility:hidden;position: relative;top: -401px;left:-72px;"></div>
	<div id="standby" class="EDGE-492073588" style="visibility:hidden;position: relative;top:-405px;left:-70px;"></div>
	<div id="chipsAnimation" class="EDGE-112784366" style="visibility:hidden;left: -30px;top: -430px;"></div><!--430 30-->
</div>
 
<!-- cargamos de wordpress el contenido de los chips-->

<div id="TituloIcon" class="descbox">
<?php

query_posts('order=asc');
// retrieve one post with an ID of 5
query_posts( 'p=1' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	the_title();
	//the_content( 'Read the full post »' );
endwhile;
?>
</div>  

<div id="TituloBuilding" class="descbox">
<?php

query_posts('order=asc');
// retrieve one post with an ID of 5
query_posts( 'p=2' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	the_title();
	//the_content( 'Read the full post »' );
endwhile;
?>
</div>  

<div id="TituloBooks" class="descbox">
<?php

query_posts('order=asc');
// retrieve one post with an ID of 5
query_posts( 'p=3' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	the_title();
	//the_content( 'Read the full post »' );
endwhile;
?>
</div>  

<div id="TituloTrolley" class="descbox">
<?php

query_posts('order=asc');
// retrieve one post with an ID of 5
query_posts( 'p=4' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	the_title();
	//the_content( 'Read the full post »' );
endwhile;
?>
</div>  

<div id="TituloPiggy" class="descbox">
<?php

query_posts('order=asc');
// retrieve one post with an ID of 5
query_posts( 'p=5' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	the_title();
	//the_content( 'Read the full post »' );
endwhile;
?>
</div>  

<div id="TituloRocket" class="descbox">
<?php

query_posts('order=asc');
// retrieve one post with an ID of 5
query_posts( 'p=6' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	the_title();
	//the_content( 'Read the full post »' );
endwhile;
?>
</div>  



<div id="ContenidoIcon" class="descbox">
<?php

query_posts('order=desc');
// retrieve one post with an ID of 5
query_posts( 'p=1' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	//the_title();
	the_content( 'Read the full post »' );
endwhile;
?>
</div>  

<div id="ContenidoBuilding" class="descbox">
<?php

query_posts('order=desc');
// retrieve one post with an ID of 5
query_posts( 'p=2' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	//the_title();
	the_content( 'Read the full post »' );
endwhile;
?>
</div> 

<div id="ContenidoBooks" class="descbox">
<?php

query_posts('order=desc');
// retrieve one post with an ID of 5
query_posts( 'p=3' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	//the_title();
	the_content( 'Read the full post »' );
endwhile;
?>
</div> 

<div id="ContenidoTrolley" class="descbox">
<?php

query_posts('order=desc');
// retrieve one post with an ID of 5
query_posts( 'p=4' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	//the_title();
	the_content( 'Read the full post »' );
endwhile;
?>
</div> 

<div id="ContenidoPiggy" class="descbox">
<?php

query_posts('order=desc');
// retrieve one post with an ID of 5
query_posts( 'p=5' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	//the_title();
	the_content( 'Read the full post »' );
endwhile;
?>
</div> 

<div id="ContenidoRocket" class="descbox">
<?php

query_posts('order=desc');
// retrieve one post with an ID of 5
query_posts( 'p=6' );

// set $more to 0 in order to only get the first part of the post
global $more;
$more = 0;

// the Loop
while (have_posts()) : the_post();
	//the_title();
	the_content( 'Read the full post »' );
endwhile;
?>
</div> 

<?php get_footer(); ?>