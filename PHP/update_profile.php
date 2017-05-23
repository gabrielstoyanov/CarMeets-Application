<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$username = $_POST['username'];
$location = $_POST['location'];
$age = $_POST['age'];
$preferences = $_POST['preferences'];
$availability = $_POST['availability'];
$car_info = $_POST['car_info'];
$gender = $_POST['gender'];
$email = $_POST['email'];
$result = mysqli_query($con,"UPDATE Users
SET location='$location', age='$age', preferences='$preferences', availability='$availability', car_info='$car_info', gender='$gender', email='$email' 
WHERE username='$username'");


echo "s";
mysqli_close($con);

?>