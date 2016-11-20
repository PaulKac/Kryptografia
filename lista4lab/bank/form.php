
<?php
   require_once('php/session.php');
   require_once("php/page.php");
   
   $P = new Page();
   echo $P->Begin();
   echo $P->loggedAs();
?>
		
		<p id="pr"><b >Przelew</b></p><br>
		<form id="form1" action = "confirm.php" method = "post">
			<label>Na rachunek: </label><input id="accnr" type = "text" name = "accountNr" class = "box" /><br>
            <label>Kwota: </label><input type = "number" min="0"  step="0.01" name = "value" class = "box" /><br>
			<label>Tytuł: </label><input type = "text" name = "title" class = "box" /><br>
            <input type = "submit" value = " Dalej "/><br>
		</form>
<?php
	echo $P->End();
?>
	