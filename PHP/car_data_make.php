<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$result = mysqli_query($con,"SELECT * FROM Cars");
while($row = mysqli_fetch_array($result)){
echo $data = $row['Make'] . "&";}
mysqli_close($con);

?>