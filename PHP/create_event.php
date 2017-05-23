<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$e_name = $_POST['e_name'];
$e_location = $_POST['e_location'];
$e_restrictions = $_POST['e_restrictions'];
$e_time = $_POST['e_time'];
$e_duration = $_POST['e_duration'];
$e_createdBy = $_POST['e_createdBy'];
$result = mysqli_query($con,"INSERT INTO Event (e_name, e_location, e_restrictions, e_time, e_duration, e_createdBy, users_attending) 
VALUES ('$e_name', '$e_location', '$e_restrictions', '$e_time', '$e_duration', '$e_createdBy', '$e_createdBy')");
echo "Query Successful!";
mysqli_close($con);

?>