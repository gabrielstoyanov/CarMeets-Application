<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$username = $_POST['username'];
$password = $_POST['password'];
$location = $_POST['location'];
$age = $_POST['age'];
$preferences = $_POST['preferences'];
$availability = $_POST['availability'];
$car_info = $_POST['car_info'];
//$picture = $_POST['picture'];
$gender = $_POST['gender'];
$email = $_POST['email'];
$result = mysqli_query($con,"INSERT INTO Users (username, password, location, age, preferences, availability, car_info, gender, email) 
VALUES ('$username', '$password', '$location', '$age', '$preferences', '$availability', '$car_info', '$gender', '$email')");
echo "Query Successful!";
mysqli_close($con);

?>