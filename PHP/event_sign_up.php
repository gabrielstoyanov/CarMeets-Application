<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$eventName = $_POST['eventName'];
$users_attending = $_POST['users_attending'];
$result = mysqli_query($con,"UPDATE Event SET users_attending='$users_attending' 
	WHERE e_name='$eventName'");
echo "success";
mysqli_close($con);

?>