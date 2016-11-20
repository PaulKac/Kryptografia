<?php
   require_once('php/session.php');
   require_once("php/page.php");
   $P = new Page();
   echo $P->Begin();
   echo $P->loggedAs();
?>
		<div>
			<table id="historytable" border="1">
				<tr>
					<th>Id</th>
					<th>Na konto</th> 
					<th>Kwota</th>
					<th>Tytuł</th>
				</tr>
				
			<?php
			$result = $db->query("SELECT * FROM history WHERE name = '". $_SESSION['login_user']. "' ORDER BY id DESC");
			while($row = $result->fetch_assoc()) {
				echo "<tr><th>".$row["id"]."</th>"
				."<th>".$row["accountTo"]."</th>"
				."<th>".$row["value"]."</th>"
				."<th>".$row["title"]."</th></tr>\n";
			}
			
			?>
				
			</table>
		</div>
<?php
	echo $P->End();
?>