<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$username = $_POST['username'];
$password = $_POST['password'];
$result = mysqli_query($con,"SELECT * FROM Users where 
username='$username' and password='$password'");
$row = mysqli_fetch_array($result);
$data = $row['username'];


if($data){
echo $data;
} else {echo "f";}
mysqli_close($con);

?>