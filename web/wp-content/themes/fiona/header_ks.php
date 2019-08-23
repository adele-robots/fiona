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

?>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en"> 
<head> 
  <title>.:: Fiona ::.:: Sparking Together ::.</title> 
 
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/> 
  <meta http-equiv="description" content=""/> 
  
  <!-- Stylesheets --> 
  <link charset="utf-8" href="/css/main.css" media="screen" rel="Stylesheet" title="Adele" type="text/css"/> 
  
  <!-- Shortcut Icons --> 
  <link rel="shortcut icon" href="/favicon.ico" type="image/x-icon"/> 
 
  <!-- RSS Feed --> 
  <link rel="alternate" title="Fiona RSS Feed" href="/rss-pr" type="application/rss+xml" /> 
 
  <!-- Scripts --> 
  <script src="/scripts/jquery-1.4.2.min.js" type="text/javascript"></script>  
  <script src="/scripts/jquery.backstretch.min.js" type="text/javascript"></script>  
  <script src="/scripts/jquery-ui-1.8.4.custom.min.js" type="text/javascript"></script>  
  <script src="/scripts/jquery.corner.js" type="text/javascript"></script>  
  <script src="/scripts/jquery.column.list.js" type="text/javascript"></script>  
  <script src="/scripts/jquery.cookie.js" type="text/javascript"></script>  
  <script src="/scripts/jquery.cycle.all.min.js" type="text/javascript"></script>  
  <script src="/scripts/jquery.touchwipe.min.js" type="text/javascript"></script> 
  <script src="/scripts/jqresize.js" type="text/javascript"></script> 
 
	<script src="/scripts/global.js" type="text/javascript"></script>

<?php

/* Always have wp_head() just before the closing </head>
 * tag of your theme, or you will break many plugins, which
 * generally use this hook to add elements to <head> such
 * as styles, scripts, and meta tags.
 */
wp_head();
?>
</head>





<body class="<?php echo "xhtml no-js " . join( ' ', get_body_class() ); ?>">
<script type="text/javascript">//<![CDATA[
document.body.className = document.body.className.replace('no-js', 'js');
// ]]></script>
<div id="wrapper"> 

	<div id="header"> 
		<div id="header_bg"><img id="header_bg_img" src="/images/header.png"></img></div>
			<div id="click" onclick="javascript:window.location.href='/'" style="position:absolute;height:100px;width:170px;top:10px;left:30px;z-index:5;cursor: pointer;"></div>
		<div id="iframe_bg">
		<iframe id="iframe" src="/login.html" scrolling="no" style="position:absolute;right:0px;width:900px;height:65px;"></iframe>
		</div>
			
				<a href="http://twitter.com/Sparking2gether" target="_top"><img src="/images/twitter.png" style="padding-left:10px"/></a>
				<a href="http://www.facebook.com/pages/Sparking-Together/180513272042574" target="_top"><img src="/images/facebook.png" style="padding-left:10px"/></a>
				
			</div>
	</div><!-- end header --> 

	<div id="main">