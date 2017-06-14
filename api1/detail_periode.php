<?php

	require_once('db.php');
	$curl = curl_init();

	$periode=$_GET['periode'];
	$query = mysqli_query($conn, "SELECT * FROM user WHERE periode='$periode'");

	$result = array();
	while($row = mysqli_fetch_array($query)){
		array_push($result,array(
			'periode'	=> $row['periode'],
			'username'	=> $row['username'],
			'nim'	=> $row['nim'],
			'judul_ta'	=> $row['judul_ta']
			));
	}
	$result = json_encode($result);
	// echo json_encode(array('result'=>$result));
	echo $result;
	mysqli_close($conn);

 ?>
