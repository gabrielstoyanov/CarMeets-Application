<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$make = $_POST['make'];

$result = mysqli_query($con,"SELECT * FROM Cars WHERE Make='$make'");
while($row = mysqli_fetch_array($result)){
echo $data = $row['Model'];}
mysqli_close($con);

?>