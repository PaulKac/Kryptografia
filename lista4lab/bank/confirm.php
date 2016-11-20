<?php
   require_once('php/session.php');
   require_once("php/mydb.php");
   require_once("php/page.php");
   
   if($_SERVER["REQUEST_METHOD"] == "POST") {
	   
      $accountNr = $_POST['accountNr'];
      $value = $_POST['value'];
	  $title = $_POST['title'];
	  
   }
   
   $P = new Page();
   echo $P->Begin();
   echo $P->loggedAs();

?>

<form id="confirmform" action = "sent.php" method = "post">
	<label>Na rachunek: </label><input id="accnr" type="text" name="accountNr" class="box" value="<?php echo $accountNr; ?>" readonly /><br>
	<label>Kwota: </label><input type="number"  step="0.01" name="value" class="box" value="<?php echo $value; ?>"  readonly /><br>
	<label>Tytuł: </label><input type="text" name="title" class="box" value="<?php echo $title; ?>"  readonly /><br>
	<input type = "submit" value = " Wyślij "/><br />
</form>

<?php
	echo $P->End();
?>