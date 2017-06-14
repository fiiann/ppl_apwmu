<?php

	require_once('db.php');
	$curl = curl_init();

		$query = mysqli_query($conn, "SELECT * FROM tahun_wisuda");

	$result = array();
	while($row = mysqli_fetch_array($query)){
		array_push($result,array(
			'tahun'	=> $row['tahun']
			));
	}
	$result = json_encode($result);
	// echo json_encode(array('result'=>$result));
	echo $result;
	mysqli_close($conn);

 ?>
