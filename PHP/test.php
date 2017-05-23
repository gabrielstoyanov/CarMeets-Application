<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$id = $_GET['id'];
$result = mysqli_query($con,"SELECT * FROM Users WHERE user_ID=$id");
$row=mysqli_fetch_array($result);
echo $row['availability'];
mysqli_close($con);

?>