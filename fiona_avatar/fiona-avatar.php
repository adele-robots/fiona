<?php
/*
Plugin Name: Fiona Avatar
Plugin URI: http://URI_Of_Page_Describing_Plugin_and_Updates
Description: This plugin will let you have your personal Fiona Avatar fully integrated with Wordpress
Version: 1.0
Author: ADELE ROBOTS
Author URI: http://www.adelerobots.com
License: GPL2
*/

/*  Copyright 2013  ADELE ROBOTS  (email : info@adelerobots.com)

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License, version 2, as 
    published by the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/


add_action( 'admin_menu', 'fiona_avatar_menu' );

// Here you can check if plugin is configured (e.g. check if some option is set). If not, add new hook. 
// In this example hook is always added.
if(!fiona_avatar_check_plugin_fully_configured())
    add_action( 'admin_notices', 'fiona_avatar_admin_notices' );

register_activation_hook( __FILE__, 'fiona_avatar_activate_plugin' );
add_action('admin_init', 'fiona_avatar_redirect');

/**
 * Register with hook 'wp_enqueue_scripts', which can be used for front end CSS and JavaScript
 */
add_action( 'wp_enqueue_scripts', 'fiona_avatar_add_stylesheet' );

function fiona_avatar_activate_plugin() {
    // work-around to redirect to admin plugin page after plugin activiation
    add_option('fiona_avatar_do_activation_redirect', true);
}

function fiona_avatar_redirect() {
    // redirect to plugin admin page after plugin activation
    if (get_option('fiona_avatar_do_activation_redirect', false)) {
        delete_option('fiona_avatar_do_activation_redirect');
        wp_redirect(admin_url('admin.php?page=avatar-top-level-handle'));
    }
}

/**
 * Enqueue plugin style-file
 */
function fiona_avatar_add_stylesheet() {
    // Respects SSL, Style.css is relative to the current file
    wp_register_style( 'fiona-avatar', plugins_url('css/fiona-avatar.css', __FILE__) );
    wp_enqueue_style( 'fiona-avatar' );
}

function fiona_avatar_initialize() {
	$plugin_dir = basename(dirname(__FILE__));
	load_plugin_textdomain( 'fiona-avatar-text-domain', false, $plugin_dir ) . '/languages';
}


function fiona_avatar_menu() {
	//$hook_suffix = add_options_page( 'Fiona Avatar Options', 'Fiona Avatar', 'manage_options', 'fiona_avatar', 'fiona_avatar_options' );

	// New Top-Level options for the plugin
    $hook_suffix = add_menu_page('Fiona Avatar Settings', 'Fiona Avatar', 'manage_options', 'avatar-top-level-handle', 'fiona_avatar_show_options', plugins_url( 'fiona_avatar/images/favicon.png' ));
	// New sub-level options for the plugin
	//add_submenu_page( 'avatar-top-level-handle', 'Fiona Avatar Settings', 'User options', 'manage_options', 'avatar-submenu-handle', 'show_fiona_avatar_options');

	// Use the hook suffix to compose the hook and register an action executed when plugin's options page is loaded
	add_action( 'load-' . $hook_suffix , 'fiona_avatar_load_function' );

    wp_register_style( 'fiona-avatar-settings', plugins_url('css/fiona-avatar-settings.css', __FILE__) );

    /* Using registered $page handle to hook stylesheet loading */
    add_action( 'admin_print_styles-' . $hook_suffix, 'fiona_avatar_admin_styles' );

}

function fiona_avatar_admin_styles() {
    /*
     * It will be called only on your plugin admin page, enqueue our stylesheet here
     */
    wp_enqueue_style( 'fiona-avatar-settings' );
}


function fiona_avatar_load_function() {
	// Current admin page is the options page for our plugin, so do not display the notice
	// (remove the action responsible for this)
	remove_action( 'admin_notices', 'fiona_avatar_admin_notices' );
}

function fiona_avatar_admin_notices() {
	echo "<div id='notice_not_configured' class='updated fade'><p>" . __("Fiona-Avatar Plugin is not configured yet. Please do it now.","fiona-avatar-text-domain") . "</p></div>\n";
}


// Function to create the Administration Menu for the plugin options
function fiona_avatar_show_options(){
	 //must check that the user has the required capability 
    if (!current_user_can('manage_options'))
    {
      wp_die( __('You do not have sufficient permissions to access this page.') );
    }

    // variables for the field and option names
    // usermail 
    $opt_name = 'mt_favorite_color';
    $hidden_field_name = 'mt_submit_hidden';
    $data_field_names = array(
    						"usermail" => "usermail",
    						"usrid1" => "usrid1",
    						"usrid2" => "usrid2",
    						//"show_pop_up" => "show_pop_up",
    						"avatar_size" => "avatar_size",
    						"allow_camera" => "allow_camera",
                            "button_text" => "button_text"
						);


    foreach ($data_field_names as $key => $value) {
    	// Read in existing option value from database
    	$opt_val[$key] = get_option( $value );

    	// See if the user has posted us some information
    	// If they did, this hidden field will be set to 'Y'    	
    	if( isset($_POST[ $hidden_field_name ]) && $_POST[ $hidden_field_name ] == 'Y' ) {
	        // Read their posted value
	        $opt_val[$key] = $_POST[ $value ];
	        // Save the posted value in the database
	        update_option( $value, $opt_val[$key] );
    	}
    }
    if(!fiona_avatar_check_plugin_fully_configured())
        add_action( 'admin_notices', 'fiona_avatar_admin_notices' );
    else{
        fiona_avatar_load_function();

    }

    if( isset($_POST[ $hidden_field_name ]) && $_POST[ $hidden_field_name ] == 'Y' ) {
        // Put an settings updated message on the screen
        //echo '<div class="updated"><p><strong>'._e('Settings saved.', 'fiona-avatar-text-domain' ).'</strong></p></div>';
        //echo '<div class="updated"><p><strong>Settings saved</strong></p></div>';
        echo '<div class="updated"><p><strong>'.__('Settings saved.', 'fiona-avatar-text-domain' ).'</strong></p></div>';
    }
    

    // Now display the settings editing screen
    echo '<div class="wrap">';

    // header
    echo "<h2>" . __( 'Fiona Avatar Plugin Settings', 'fiona-avatar-text-domain' ) . "</h2>";

    // settings form
	echo '<form name="form1" method="post" action="">';
	echo '<input type="hidden" name="' . $hidden_field_name .'" value="Y">';
    echo '<p>'.__('To configure the Fiona-avatar plugin you must have a FIONA account. Have an account already? Great! If not,','fiona-avatar-text-domain') .
    ' <a href="http://www.sparkingtogether.com/" target="_blank" title="'. __('Sign up for a FIONA account'.'fiona-avatar-text-domain') .'">' . __('sign up here','fiona-avatar-text-domain') . '</a></p><br>';
    echo '<h3>'.__('User Data','fiona-avatar-text-domain').'</h3>';
	// For each option, build the HTML
	foreach ($data_field_names as $key => $value) {
        //if(strcmp(trim($value),"show_pop_up") == 0 || strcmp(trim($value),"allow_camera") == 0){
        if(strcmp(trim($value),"allow_camera") == 0){
            echo '<div class="form_content">';
            echo '<label for="'.$key.'" >'.$value.'</label>';
            echo '<select id="'.$key.'" class="select_form_field" name="' . $value . '" >';
            echo '<option value="true">True</option>';
            if(strcmp(trim($value),"allow_camera") == 0)
                echo '<option value="false" selected="selected">False</option>';
            else
                echo '<option value="false">False</option>';
            echo '</select>';
            echo '<div class="clear"></div>';
            echo '</div>'.'';
        }else{
            if(strcmp(trim($value),"avatar_size") == 0){
                echo '<h3>'.__('Appearance','fiona-avatar-text-domain').'</h3>';
                echo '<div class="form_content">';
                echo '<label for="'.$key.'" >'.$value.'</label>';
                echo '<select id="'.$key.'" class="select_form_field" name="' . $value . '" >';
                echo '<option value="big">Big</option>';
                echo '<option value="small">Small</option>';
                echo '</select>';
                echo '<div class="clear"></div>';
                echo '</div>'.'';
            }elseif (strcmp(trim($value),"button_position") == 0){
                echo '<div class="form_content">';
                echo '<label for="'.$key.'" >'.$value.'</label>';
                echo '<select id="'.$key.'" class="select_form_field" name="' . $value . '" >';
                echo '<option value="middle-left">Middle Left</option>';
                echo '<option value="middle-right">Middle Right</option>';
                echo '</select>';
                echo '<div class="clear"></div>';
                echo '</div>'.'';
            }else{
                echo '<div class="form_content">';
                echo '<label for="'.$key.'">'.$value.'</label>';
                echo '<input class="form_field" id="'.$key.'" type="text" name="'. $value . '" value="' . $opt_val[$key] . '" size="35">';
                echo '<div class="clear"></div>';
                echo '</div>'.'';
            }
        }
	}
    echo '<hr />'.'';

	echo '<p class="submit">';
	//echo '<input type="submit" name="Submit" class="button-primary" value="'.esc_attr_e('Save Changes').'" />';
    echo '<input type="submit" name="Submit" class="button-primary" value="'.__('Save Changes').'" />';
	echo '</p>'.'';

	echo '</form>'.'';
	echo '</div>'.'';
}


function fiona_avatar_init(){
    fiona_avatar_initialize();

	$usermail = get_option('usermail');
	$usrid1 = get_option('usrid1');
	$usrid2 = get_option('usrid2');
	//$show_pop_up = get_option('show_pop_up');
    $show_pop_up = "true";
	$avatar_size = get_option('avatar_size');
	$allow_camera = get_option('allow_camera');
    $button_text = get_option('button_text');
    $button_position = get_option('button_position');



	//header('Content-Type: text/javascript');
	//readfile('http://aio0001.adelerobots.com/js/fiona-embed-1.0.js');

   // embed script

    // DEFAULT VALUES
    /*
    if(!isset($usermail)) $usermail = '';
    if(!isset($usrid1)) $usrid1 = '';
    if(!isset($usrid2)) $usrid2 = '';
    if(!isset($avatar_size)) $avatar_size = '';
    if(!isset($allow_camera)) $allow_camera = '';
    */

    // embed script
    echo '
        <div id="fiona" class="fiona_main" style="display:none;">
        </div>
        <div id="fiona-button">
        <!-- Position bottom-right -->
        <span class="span_wrap">
            <span class="span_wrapped" id="starttalk" onclick="initFiona(this,usermail,usrid2,usrid1)" style="">
               '.$button_text.
            '</span>
        </span>
  		</div>

        <script type="text/javascript"><!--//--><![CDATA[//><!--
            usermail ="' . $usermail . '";
            usrid2 = "' . $usrid2 . '";
            usrid1 = "' . $usrid1 . '";
            avatar_size = "' . $avatar_size . '";//big or small

            show_pop_up = ' .$show_pop_up . '; // true or false

            allow_camera = ' .$allow_camera.'; // true or false

            (function() {
            var fi = document.createElement("script");
             fi.type = "text/javascript";
             fi.async = true;
             fi.src = ("http://192.168.1.104:8080/FionaHandler/js/fiona-embed-1.0.js");
             //fi.src = ("http://fionaresources.sparkingtogether.com/js/fiona-embed-1.0.js");
             var s = document.getElementsByTagName("script")[0];
             s.parentNode.insertBefore(fi, s);
            })();
        //--><!]]></script>
        <script>';
}

add_action( 'wp_footer', 'fiona_avatar_init' );

function fiona_avatar_check_plugin_fully_configured(){
    $usermail = get_option('usermail');
    $usrid1 = get_option('usrid1');
    $usrid2 = get_option('usrid2');
    /*
     * $show_pop_up = get_option('show_pop_up');
    */
    $show_pop_up = "true";
    $avatar_size = get_option('avatar_size');
    $allow_camera = get_option('allow_camera');
    $button_text = get_option('button_text');
    $button_position = get_option('button_position');

    return $usermail != null && $usrid1 != null && $usrid2 != null && $show_pop_up != null && $avatar_size != null && $allow_camera != null
        && $button_text != null;
}


?>