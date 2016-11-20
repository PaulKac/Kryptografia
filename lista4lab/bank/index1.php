<!DOCTYPE html>
<html>
	<head>
		<title>Bank</title>
	</head>
	<body>
		<?php
		if(isset($_GET['pass'])){
			$i = 32;
			$cstrong = TRUE;
			$bytes = openssl_random_pseudo_bytes($i, $cstrong);
			$salt   = bin2hex($bytes);
			$hashed_pass = $salt . hash_hmac('sha512', $salt.$_GET['pass'], 'secretkey');
			echo $hashed_pass;
		}
		?>
	</body>
</html>