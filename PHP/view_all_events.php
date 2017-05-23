<?php

$con=mysqli_connect("localhost", "carmeets", "M@nowar94", "carmeets_cm");

if (mysqli_connect_errno($con))
{
   echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
$result = mysqli_query($con,"SELECT * FROM Event");


while($row=mysqli_fetch_array($result)){
echo $row['e_name'] . "&";
echo $row['e_location'] . "&";
echo $row['e_restrictions'] . "&";
echo $row['e_time'] . "&";
echo $row['e_duration'] . "&";
echo $row['e_createdBy'] . "&";
echo $row['e_creationTime'] . "&";
echo "#";
}

mysqli_close($con);

?>