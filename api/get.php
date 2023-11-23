<?php
include 'connect.php';
$key = '87Y78GF78SHFDSHFU';
if ($_SERVER['REQUEST_METHOD'] == 'GET') {
    $api_key = $_GET['api_key'];
    if ($api_key != $key) {
        echo json_encode(array('kode' => 999, 'pesan' => 'Api Key tidak valid'));
        exit;
    }
    if ($api_key != $key) {
        echo json_encode(array('kode' => 999, 'pesan' => 'Api Key tidak valid'));
        exit;
    }
    $array = array();

    $queries = array(
        "jenisKelamin" => "select * from tweb_penduduk_sex",
        "agama" => "select * from tweb_penduduk_agama",
        "pekerjaan" => "select * from tweb_penduduk_pekerjaan",
        "statusKawin" => "select * from tweb_penduduk_kawin",
        "golongan_darah" => "select * from tweb_golongan_darah",
        "dusun" => "select * from tweb_wil_clusterdesa"
    );

    foreach ($queries as $key => $query) {
        $result = mysqli_query($conn, $query);
        if ($result === false) {
            echo "Error in query: " . mysqli_error($conn);
            exit;
        }
        while ($row = mysqli_fetch_array($result)) {
            $array[$key][] = $row;
        }
    }

    echo json_encode($array);
}
?>