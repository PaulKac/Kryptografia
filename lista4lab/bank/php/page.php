<?php
	//require_once('session.php');
/* MODEL */

/*$STRONY = [
  ['id'=>'1',  'name'=>"Katedra",      'href'=>"index.php" ],
  ['id'=>'2',  'name'=>"Kadra",        'href'=>"extnwd.php"],
  ['id'=>'3',  'name'=>"Studenci",     'href'=>"qsort.php" ],
  ['id'=>'4',  'name'=>"Aktualności",  'href'=>"msort.php" ]
];*/
 
/* VIEW */

$HEADER =<<<EOT
<!DOCTYPE html>
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="style.css">
		<script src="jquery-3.1.1.min.js"></script>
		
		<title>Bank</title>
	</head>
	<body>
EOT;

$FOOTER = <<<EOT
<!--script src="script.js"></script-->
	</body>
</html>
EOT;

/* CONTROLLER */

class Page{

  public function loggedAs(){
	  if(isset($_SESSION['login_user']))
		return "Zalogowany jako: <span id=\"nick\">" . $_SESSION['login_user'] .  "</span><br>";
		
  }

  public function Begin() {
    global $HEADER;
	header("Cache-Control: no-cache, no-store, must-revalidate"); // HTTP 1.1.
	header("Pragma: no-cache"); // HTTP 1.0.
	header("Expires: 0"); // Proxies.
	return $HEADER . var_dump($_POST); 
  }

  public function End() {
    global $FOOTER;
    return $FOOTER;
  }
}
?>