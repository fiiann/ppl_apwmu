<?php

	require_once('db.php');
	$curl = curl_init();

	$provinsi=$_GET['provinsi'];
	$query = mysqli_query($conn, "SELECT * FROM user WHERE tahun_lulus IS NOT NULL AND provinsi='$provinsi'");

	$result = array();
	while($row = mysqli_fetch_array($query)){
		array_push($result,array(
			'nim'	=> $row['nim'],
			'username'	=> $row['username'],
			'fakultas'	=> $row['fakultas'],
			'jurusan'	=> $row['jurusan'],
			'jenis_kelamin'	=> $row['jenis_kelamin'],
			'warga_negara'	=> $row['warga_negara'],
			'tempat_lahir'	=> $row['tanggal_lahir'],
			'email'	=> $row['email'],
			'alamat'	=> $row['alamat'],
			'telepon'	=> $row['telepon'],
			'pekerjaan'	=> $row['pekerjaan'],
			'foto'	=> $row['foto']
		));
	}
	$result = json_encode($result);
	// echo json_encode(array('result'=>$result));
	echo $result;
	mysqli_close($conn);

 ?>
