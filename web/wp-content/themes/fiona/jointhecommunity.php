<?php
/**
 * Template Name: Join the community Template
 * Description: This page give us two distinct paths from the home page. Either create an account or let us know that you need to know more before creating an account.
 *
 * Based on the time to spent on the page and number of video views we will know 
 * how much more likely someone its to create an account after viewing the video and how effective 
 * our video is in aiding in conversions.
 * 
 * When visitors clicks on the "Join" button we want them to go from Visitor to User as quickly as possible.
 *
 * @package WordPress
 * @subpackage Fiona
 */

if ( isset($_REQUEST['learn-more']) ) {
	get_template_part("index");//render this template to show predefined index with EDGE chips
	exit; //end script to do that pseudo redirect
}


class JtcUser {
	public $id;
	public $name;
	public $surname;
	public $mail;
	public $mailId;
	public $passwd;
	public $entity;
	public $entityName;
	public $account;
	public $accountName;
	public $status;
	public $creditcard;
	public $creditcard_exp;
	public $signupcode;
	public $username;
	public $displayname;
	public $fullname;
	public $avatarumd5;
	public $role;
	public $roleName;
	
	static function add($user){
		$result = self::initResult();
		if (!defined('FAWNA_SOAP_CONNECTOR_URI') || !filter_var(FAWNA_SOAP_CONNECTOR_URI, FILTER_VALIDATE_URL, FILTER_FLAG_SCHEME_REQUIRED | FILTER_FLAG_HOST_REQUIRED | FILTER_FLAG_PATH_REQUIRED) ) {
			array_push($result->messages, __( 'SOAP connector URI need to be configured for invoking SN029001 service.' ) );
			return $result;
		}
		//Invoke "SN029001 - Add usuarios" service using SOAP
		try {
			$client_opts = array(
				'trace' => true 
			);
			$client = @new SoapClient(FAWNA_SOAP_CONNECTOR_URI.'/SN029001?wsdl', $client_opts);
			
			$sn_params = array();
			$sn_params['compania']           = null;
			$sn_params['SN029001C001']       = array('pos' => 0, 'tipo' => 'CHAR', '_' => $user->name    );				//Name
			if (isset($user->surname))          //Optional
				$sn_params['SN029001C002']   = array('pos' => 1, 'tipo' => 'CHAR', '_' => $user->surname );				//Surname
			$sn_params['SN029001C003']       = array('pos' => 2, 'tipo' => 'CHAR', '_' => $user->mail );				//Email
			$sn_params['SN029001C004']       = array('pos' => 3, 'tipo' => 'CHAR', '_' => $user->passwd );				//Password
			if (isset($user->entity))           //Optional
				$sn_params['SN029001C005']   = array('pos' => 4, 'tipo' => 'PIC',  '_' => $user->entity );				//Entity Id
			if (isset($user->account))          //Optional
				$sn_params['SN029001C006']   = array('pos' => 5, 'tipo' => 'PIC',  '_' => $user->account );				//Account Type Id
			if (isset($user->creditcard)) 	     //Optional
				$sn_params['SN029001C007']   = array('pos' => 6, 'tipo' => 'PIC',  '_' => $user->creditcard );			//Creditcard number
			if (isset($user->creditcard_exp))  //Optional
				$sn_params['SN029001C008']   = array('pos' => 7, 'tipo' => 'CHAR', '_' => $user->creditcard_exp );		//Creditcard expiration
			$sn_params['SN029001C009']       = array('pos' => 8, 'tipo' => 'CHAR', '_' => $user->username );			//Nickname/username
			if (isset($user->role))            //Optional
				$sn_params['SN029001C010']   = array('pos' => 9, 'tipo' => 'PIC',  '_' => $user->role );				//Role Id
			
			$response = $client->ServicioNegocio(array(
				'datosEntrada'     => $sn_params
			));
			self::bindResult(& $result, $response);
			if ($result->model) $result->model = $result->model['FIONEGN001'];//filter by that context name
			$result->model = self::singlearray($result->model);
		} catch (SoapFault $fault) {
			array_push($result->messages, $fault);
		}
		// Kill the link to Soap client
		if (isset($client)) unset($client);
		return $result;
	}
	
	static function get($mode, $val){
		$result = self::initResult();
		if (!defined('FAWNA_SOAP_CONNECTOR_URI') || !filter_var(FAWNA_SOAP_CONNECTOR_URI, FILTER_VALIDATE_URL, FILTER_FLAG_SCHEME_REQUIRED | FILTER_FLAG_HOST_REQUIRED | FILTER_FLAG_PATH_REQUIRED) ) {
			array_push($result->messages, __( 'SOAP connector URI need to be configured for invoking SN029003 service.' ) );
			return $result;
		}
		//Invoke "SN029003 - Detalle usuarios" service using SOAP
		try {
			$client_opts = array(
				'trace' => true 
			);
			$client = @new SoapClient(FAWNA_SOAP_CONNECTOR_URI.'/SN029003?wsdl', $client_opts);
			
			$sn_params = array();
			$sn_params['compania']           = null;
			$sn_params['SN029003C001']       = array('pos' => 0, 'tipo' => 'CHAR', '_' => $mode );
			if ( in_array($mode, array('L','E','N')) ) {
				$sn_params['SN029003C002']   = array('pos' => 1, 'tipo' => 'CHAR', '_' => $val  );
			} else if ( in_array($mode, array('I')) ) {
				$sn_params['SN029003C003']   = array('pos' => 2, 'tipo' => 'PIC',  '_' => $val  );
			}
			$response = $client->ServicioNegocio(array(
				'datosEntrada'     => $sn_params
			));
			self::bindResult(& $result, $response);
			if ($result->model) $result->model = $result->model['FIONEGN001'];//filter by that context name
			$result->model = self::singlearray($result->model);
		} catch (SoapFault $fault) {
			array_push($result->messages, $fault);
		}
		// Kill the link to Soap client
		if (isset($client)) unset($client);
		
		return $result;
	}
	
	/**
	 * Unwrap single results from array
	 * @param mixed $obj
	 */
	static function singlearray($obj=null) {
		if (is_array($obj)) {
			if ( count($obj) == 0 ) return null;//on empty, null.
			return reset( $obj );//first element
		}
		return $obj;
	}
	protected static function bindResult(& $result, $soap) {
		if (!is_object($result)) {
			$result = self::initResult();
		}
		//Handle errors returned into SOAP Response Body
		if (array_key_exists('error', get_object_vars($soap->out)) && !empty($soap->out->error)) {
			array_push($result->messages, $soap->out->error);
		} else if (array_key_exists('contexto', get_object_vars($soap->out)) && !empty($soap->out->contexto)) {
			if (!is_array($soap->out->contexto)) { //Ensure array
				$field = $soap->out->contexto;
				$soap->out->contexto = array();
				array_push($soap->out->contexto, $field);
			}
			//group by context name and map to JtcUser model
			$map = array();
			foreach ($soap->out->contexto as $i=>$v) {
				if (!isset($map[$v->nombre])) $map[$v->nombre] = array();
				array_push($map[$v->nombre], self::map($v));
			}
			$result->model = $map;
		}
		return $result;
	}
	
	protected static function map($node) {
		if ($node->nombre == 'FIONEGN001') { //JtcUser context name
			$bean = new JtcUser();
			self::mapIntern($node, & $bean, array(
					'FIONEG001010' => 'id',
					'FIONEG001020' => 'name', 
					'FIONEG001030' => 'surname', 
					'FIONEG001040' => 'mail', 
					'FIONEG001041' => 'mailId', 
					'FIONEG001050' => 'passwd', 
					'FIONEG001060' => 'entity', 
					'FIONEG001061' => 'entityName', 
					'FIONEG001070' => 'account', 
					'FIONEG001071' => 'accountName', 
					'FIONEG001080' => 'status', 
					'FIONEG001090' => 'creditcard', 
					'FIONEG001100' => 'creditcard_exp', 
					'FIONEG001110' => 'signupcode', 
					'FIONEG001120' => 'username', 
					'FIONEG001121' => 'fullname', 
					'FIONEG001122' => 'displayname', 
					'FIONEG001130' => 'avatarumd5', 
					'FIONEG001140' => 'role', 
					'FIONEG001141' => 'roleName', 
				), array(
					
				));
			return $bean;
		} else { //TODO Handle other context/registry name...
			return $node;
		}
	}
	
	protected static function mapIntern($node, & $bean, $simpleMappings, $complexMappings) {
		//Fill value for simple mappings
		$property = 'campoContexto';
		if ( array_key_exists($property, get_object_vars($node)) ) {
			if (!is_array($node->$property)) { //Ensure array
				$field = $node->$property;
				$node->$property = array();
				array_push($node->$property, $field);
			}
			foreach ($node->$property as $i=>$v) { //fill mapped property with that simple value
				if ( array_key_exists($simpleMappings[$v->nombre], get_object_vars($bean)) ) {
					$bean->$simpleMappings[$v->nombre] = $v->_;
				}
			}
		}
		
		//Fill value for complex mappings (context/registry)
		$property = 'campoContextoRegistro';
		if ( array_key_exists($property, get_object_vars($node)) ) {
			if (!is_array($node->$property)) { //Ensure array
				$field = $node->$property;
				$node->$property = array();
				array_push($node->$property, $field);
			}
			foreach ($node->$property as $i=>$v) { //fill mapped property with that complex value
				if ( array_key_exists($complexMappings[$v->nombre], get_object_vars($bean)) ) {
					//TODO check campoContextoRegistro is a sequence of Registro so array or bean??
					$bean->$complexMappings[$v->nombre] = self::map($v);
				}
			}
		}
	}
	
	static function initResult() {
		$result = new stdClass;
		$result->messages = array();
		$result->model = null;
		return $result;
	}
}

function jtc_add_user() {
	//Populate
	$user->username = new JtcUser;
	$user->name   = isset($_POST['jtc-name']) ? sanitize_text_field( $_POST['jtc-name'] ) : null;
	$user->mail   = isset($_POST['jtc-mail']) ? sanitize_text_field( $_POST['jtc-mail'] ) : null;
	$user->passwd = wp_generate_password(10, true, false); //generate random password
	$user->username = isset($user->mail) ? preg_replace("/[@\.]/i", "", $user->mail) : null;
	//Validate
	$result = JtcUser::initResult();
	if (empty($user->name)) { 
		array_push($result->messages, __( 'Your name is a required field.' ) );
	}
	if (empty($user->mail)) {
		array_push($result->messages, __( 'Your e-mail address is a required field.' ) );
	} else if (!is_email($user->mail)) { 
		array_push($result->messages, __( 'You must provide a valid e-mail address.' ) );
	}
	//invoke service
	if (empty($result->messages)) {
		//test autogenerated username to avoid duplicated
		$seed = 1;
		$username = $user->username;
		do {
			$test = JtcUser::get('N', $username); //search by nickname
			if ( (is_array($test->model) && empty($test->model)) || is_null($test->model) ) break;
			$username = $user->username . sprintf("%02d", $seed++);
		} while (true);
		$user->username = $username;
		//insert
		$result = JtcUser::add($user);
	}
	return $result;
}

$jtc_added_user;
$jtc_success;
$jtc_success = true;
if ( isset($_REQUEST['join']) ) {
	if ( (isset($_REQUEST['_wpnonce']) ? wp_verify_nonce($_REQUEST['_wpnonce'], 'jointhecommunity') : false) ) {
		$jtc_added_user = jtc_add_user();
	}
	//wp_nonce_ays('jointhecommunity');
	//die('Security check');
}

wp_enqueue_script('jquery');
if ( ! function_exists( 'fionatwentyeleven_jointhecommunity_header' ) ) :
function fionatwentyeleven_jointhecommunity_header() {
?>
<style type="text/css">
/**
 *  =Clearfix hack
 */
.clearfix:before {
content:"";
display:table;
}
.clearfix:after {
clear:both;
content:".";
display:block;
font-size:0;
height:0;
visibility:hidden;
}
.clearfix { *zoom:1; }	/** For IE 6/7 only. Include this rule to trigger hasLayout and contain floats. */
div.jointhecommunity > #content {
margin-top: 190px;
width:auto;
position:static;
}
div.jointhecommunity > #content * { 
font-family: inherit;
}
/* Forms */
input,
select,
button {
font-size: 15px;
height: 22px;
line-height: 1.4em;
padding: 2px;
margin:2px;
}
div.jointhecommunity .field label {
font-size: 25px;
position: absolute !important;
clip: rect(1px 1px 1px 1px);
clip: rect(1px, 1px, 1px, 1px);
}
div#primary.jointhecommunity {
margin:auto 11em;
}
div#primary.jointhecommunity p {
margin-bottom: 0.4em;
line-height: 1.2em;
font-size: 1em;
padding-bottom:0;
}
div.jointhecommunity > #content {
text-align: left;
}
div.jointhecommunity > #content > .intro {
font-size: 1.5em;
margin-bottom: 1em;
}
div.jointhecommunity > #content > .messages {
text-align:center;
font-size: 1.5em;
font-weight: 600;
}
div.jointhecommunity > #content > .messages.errors {
color:darkred;
}
div.jointhecommunity > #content > .messages.success {
color:darkblue;
}
div.jointhecommunity > #content > .join-post {
background: #1c8cd4 url('<?php echo get_template_directory_uri(); ?>/images/jointhecommunity-signup-bg.png') no-repeat 101% 150%;
border: 2px solid rgba(0,0,0,0.2);
margin-top: .8em;
padding:0.8em;
font-size: 3.8em;
line-height: 1em;
font-weight: 600;
height: 180px;
color: #c8f8f8;
-webkit-text-shadow: 2px 2px 4px #1c304d;
-moz-text-shadow: 2px 2px 4px #1c304d;
text-shadow: 2px 2px 4px #1c304d;
-webkit-border-radius: 10px;
-moz-border-radius: 10px;
border-radius: 10px;
-moz-box-shadow: 4px 4px 8px 0px rgba(0,0,0,0.4);
-webkit-box-shadow: 4px 4px 8px 0px rgba(0,0,0,0.4);
box-shadow: 4px 4px 8px 0px rgba(0,0,0,0.4);
}
div.jointhecommunity > #content > .join-post p, 
div.jointhecommunity > #content > .learnmore-post p {
font-weight: 600;
}
div.jointhecommunity > #content > .learnmore-post {
margin:.8em; word-spacing:.2em;
font-size: 3.8em;
line-height: 1.3em;
font-weight: 600;
-webkit-text-shadow: 2px 2px 2px rgba(0,0,0,0.3);
-moz-text-shadow: 2px 2px 2px rgba(0,0,0,0.3);
text-shadow: 2px 2px 2px rgba(0,0,0,0.3);
color:#20a0e0;
}
div.jointhecommunity > #content > .learnmore-post p {
line-height: 1.3em;
}
div.jointhecommunity .join-post input.input {
color:#fff;
font-weight: 800;
font-size: 27px;
height: auto;
border: 2px solid #58b8d8;
background:rgba(255, 255, 255, 0.5) none no-repeat 3px 50%;
-moz-box-shadow: 4px 4px 8px 0px rgba(0,0,0,0.5);
-webkit-box-shadow: 4px 4px 8px 0px rgba(0,0,0,0.5);
box-shadow: 4px 4px 8px 0px rgba(0,0,0,0.5);
-webkit-text-shadow: 2px 2px 4px rgba(0,0,0,0.4);
-moz-text-shadow: 2px 2px 4px rgba(0,0,0,0.4);
text-shadow: 2px 2px 2px rgba(0,0,0,0.2);
width: 300px;
padding-left:20px;
margin-bottom:10px;
}
input::-webkit-input-placeholder{color:#fff; font-style:italic;}
input::-moz-placeholder{color:#fff; font-style:italic;}
input:-moz-placeholder{color:#fff; font-style:italic;}
input:-ms-input-placeholder{color:#fff; font-style:italic;}
div.jointhecommunity .join-post input.input:focus{
border-color: #1878b8;
}
div.jointhecommunity .join-post input.mail {
/*background-image: url('<?php echo get_template_directory_uri(); ?>/images/login_mail.png');*/
}

div.jointhecommunity .jtc-button {
font-weight: 600;
color: white;
font-size: .7em; word-spacing: normal; letter-spacing: normal;
height: auto;
line-height: 1.1em;
padding: 0px;
margin:0.5em auto 0;
background: rgb(40,152,206); /* Old browsers */
background: -moz-linear-gradient(top,  rgba(40,152,206,1) 0%, rgba(28,132,202,1) 100%); /* FF3.6+ */
background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(40,152,206,1)), color-stop(100%,rgba(28,132,202,1))); /* Chrome,Safari4+ */
background: -webkit-linear-gradient(top,  rgba(40,152,206,1) 0%,rgba(28,132,202,1) 100%); /* Chrome10+,Safari5.1+ */
background: -o-linear-gradient(top,  rgba(40,152,206,1) 0%,rgba(28,132,202,1) 100%); /* Opera 11.10+ */
background: -ms-linear-gradient(top,  rgba(40,152,206,1) 0%,rgba(28,132,202,1) 100%); /* IE10+ */
background: linear-gradient(to bottom,  rgba(40,152,206,1) 0%,rgba(28,132,202,1) 100%); /* W3C */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#2898d8', endColorstr='#1c84d4',GradientType=0 ); /* IE6-9 */
border: 2px solid #58b8d8;
-webkit-text-shadow: 2px 2px 4px rgba(0,0,0,0.4);
-moz-text-shadow: 2px 2px 4px rgba(0,0,0,0.4);
text-shadow: 2px 2px 4px rgba(0,0,0,0.4);
-moz-border-radius: 4px;
border-radius: 4px;
-moz-box-shadow: 4px 4px 8px 0px rgba(0,0,0,0.5);
-webkit-box-shadow: 4px 4px 8px 0px rgba(0,0,0,0.5);
box-shadow: 4px 4px 8px 0px rgba(0,0,0,0.5);
cursor: pointer;
height: auto;
padding: 0px 5px 5px;
vertical-align: bottom;
width: 190px;
text-align:center;
}
div.jointhecommunity .jtc-button:hover {
background: rgb(28,132,202); /* Old browsers */
background: -moz-linear-gradient(top,  rgba(28,132,202,1) 0%, rgba(40,152,206,1) 100%); /* FF3.6+ */
background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(28,132,202,1)), color-stop(100%,rgba(40,152,206,1))); /* Chrome,Safari4+ */
background: -webkit-linear-gradient(top,  rgba(28,132,202,1) 0%,rgba(40,152,206,1) 100%); /* Chrome10+,Safari5.1+ */
background: -o-linear-gradient(top,  rgba(28,132,202,1) 0%,rgba(40,152,206,1) 100%); /* Opera 11.10+ */
background: -ms-linear-gradient(top,  rgba(28,132,202,1) 0%,rgba(40,152,206,1) 100%); /* IE10+ */
background: linear-gradient(to bottom,  rgba(28,132,202,1) 0%,rgba(40,152,206,1) 100%); /* W3C */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#1c84d4', endColorstr='#2898d8',GradientType=0 ); /* IE6-9 */
border-color: #1878b8;
}
div.jointhecommunity a.jtc-button {
display: block;
text-decoration: none;
}
</style>
<?php 
}
add_action( 'wp_head', 'fionatwentyeleven_jointhecommunity_header' );
endif; // ends check for fionatwentyeleven_jointhecommunity_header()


get_header(); ?>

<div id="primary" class="jointhecommunity clearfix">
	<div id="content" role="main" class="clearfix">

		<?php
		while ( have_posts() ) : the_post(); 
			/**
			 * We are using a heading by rendering the_content
			 * If we have content for this page, let's display it.
			 */
			if ( '' != get_the_content() )
				get_template_part( 'content', 'intro' );
		endwhile;
		?>

		<?php 
		global $jtc_added_user;
		global $jtc_success;
		
		if (isset($jtc_added_user)) {
			$jtc_success = empty($jtc_added_user->messages);
			if ($jtc_success) {
				echo sprintf(
					'<div class="messages success">
						<p><strong>%s</strong>, ' . __( 'thanks for joining Fiona.' ) . '</p>
					</div>', 
					$jtc_added_user->model->fullname);
			} else {
				echo '<div class="messages errors">';
				foreach ( $jtc_added_user->messages as $msg) {
					echo '<p>' . $msg . '</p>';
				}
				echo '</div>';
			}
		}
		?>
		<section class="join-post clearfix">
			<div style="float:left; width:48%;">
				<p><?php echo __( 'Join the community.' ); ?></p>
				<p><?php echo __( 'Lets work together to create an artificial mind.' ); ?><p>
			</div>
			<form action="?join" method="post" style="float:right; width:45%; text-align:right; margin-right:1em;" autocomplete="off">
				<?php wp_nonce_field( 'jointhecommunity', '_wpnonce', false ); ?>
				<div class="field">
					<label for="name" class="assistive-text"><?php echo esc_html( 'Name' ); ?></label>
					<input type="text" name="jtc-name" id="jtc-name" class="input" value="<?php echo esc_attr(isset($_POST['jtc-name']) && !$jtc_success ? $_POST['jtc-name']:''); ?>" size="20" placeholder="<?php echo esc_attr( 'Name' ); ?>" />
				</div>
				<div class="field">
					<label for="name" class="assistive-text"><?php echo esc_html( 'Email' ); ?></label>
					<input type="text" name="jtc-mail" id="jtc-mail" class="input mail" value="<?php echo esc_attr(isset($_POST['jtc-mail']) && !$jtc_success ? $_POST['jtc-mail']:''); ?>" size="20" placeholder="<?php echo esc_attr( 'Email' ); ?>" />
				</div>
				<input type="submit" name="submit" value="<?php echo esc_attr( 'Join' ); ?>" class="jtc-button">
			</form>
		</section>
		
		<section class="learnmore-post clearfix">
			<div class="learnmore-video" style="float:left; width:47%;">
				<iframe width="100%" height="280" src="//www.youtube.com/embed/yciPYWthnOg" frameborder="0" allowfullscreen=""></iframe>
			</div>
			<div class="learnmore-desc" style="float:right; width:47%;">
				<p><?php echo __( 'You can use Fiona to create a virtual avatar that fits your needs.' ); ?></p>
				<p style="text-align:center; margin-top:.5em"><a class="jtc-button" style="margin:auto;" href="?learn-more"><?php echo __( 'Learn More' ); ?></a></p>
			</div>
		</section>

	</div><!-- #content -->
</div><!-- #primary -->

<?php get_footer(); ?>