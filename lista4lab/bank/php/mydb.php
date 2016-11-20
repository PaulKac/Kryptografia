<?php

/*.
    require_module 'standard';
    require_module 'mysqli';
.*/

/**
* @return mixed
*/

function myDb() {
	$mysqli = new mysqli('localhost', 'root', 'pass', 'bank');
	if ($mysqli->connect_errno) {
		echo "Bkad: nie uda³o siê nawi¹zanie po³¹czenie z MySQL: \n";  //Nie w kodzie produkcyjnym
		echo "Numer bledu : " . $mysqli->connect_errno . "\n";
		echo "Blad: " . $mysqli->connect_error . "\n";
		return false;
	} else {
		$mysqli->set_charset("utf8");
		return $mysqli;
	}
}

/**
* @param mysqli $sqli
* @param string $q query typu select
* @return string[int]
*/

function myDbSelect($mysqli, $q){
    /*. string[int] .*/ 
	$rows   = array();
	$result = $mysqli -> query($q);
	if (!$result) {
		echo "Blad zapytania: " . $q . "<br>\n";  //Nie w kodzie produkcyjnym
		echo "Numer bledu : " . $mysqli->errno . "<br>\n";
		echo "Blad: " . $mysqli->error . "\n";			
	} else {
		while ($row = $result->fetch_assoc()) {
			$rows[] = $row;
		}
	}
	return $rows;
}

$db = myDB();

function doPayment($username, $accountTo, $value, $title){
	$db = myDB();
	$take = $db->query("SELECT money FROM accounts WHERE name = '$username'");
	$give = $db->query("SELECT money FROM accounts WHERE accountNr = '$accountTo'");
	$takerow = $take->fetch_assoc();
	$giverow = $give->fetch_assoc();
	$newtakeval = $takerow["money"] - $value;
	$newgiveval = $giverow["money"] + $value;
	
	$db->query("UPDATE accounts SET money='$newtakeval' WHERE name = '$username'");
	$db->query("UPDATE accounts SET money='$newgiveval' WHERE accountNr = '$accountTo'");
	
	if ($db->query("INSERT INTO history (id, name, accountTo, value, title) VALUES (NULL, '$username', '$accountTo', '$value', '$title')")
	 === TRUE) {
    echo "New record created successfully";
	} else {
		echo "Error: "  . "<br>" . $db->error;
	}
	//unset($_SESSION['showUpdated']);
}

?>