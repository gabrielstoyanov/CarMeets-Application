<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}

$username = $_POST['username'];
$result = mysqli_query($con,"SELECT * FROM Users WHERE username = '$username'");
while($row = mysqli_fetch_array($result)){
echo $data = $row['location'] . "&" . $row['age'] . "&" . $row['preferences'] . "&" . $row['car_info'] . "&" . $row['gender'] . "&" . $row['register_date'] . "&" . $row['age'] . "&" . $row['availability'];}
mysqli_close($con);

?>