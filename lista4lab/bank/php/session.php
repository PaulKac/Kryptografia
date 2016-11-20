<?php
   require_once('mydb.php');
   session_start();
   
   $user_check = $_SESSION['login_user'];
   
   $ses_sql = mysqli_query($db,"select name from accounts where name = '$user_check' ");
   
   $row = mysqli_fetch_array($ses_sql,MYSQLI_ASSOC);
   
   $login_session = $row['name'];
   
   if(!isset($_SESSION['login_user'])){
      header("location:index.php");
   }
?>