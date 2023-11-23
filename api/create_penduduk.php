<?php
include 'connect.php';
$key = '87Y78GF78SHFDSHFU';
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $api_key = $_GET['api_key'];
    if ($api_key != $key) {
        echo json_encode(array('kode' => 999, 'pesan' => 'Api Key tidak valid'));
        exit;
    }
    $nik = $_POST['nik']; //number 16
    $nama = $_POST['nama']; //text
    $sex = $_POST['sex']; //1 , 2 
    $tempatlahir = $_POST['tempatlahir']; //text
    $tanggallahir = $_POST['tanggallahir'];  //1972-10-25
    $tanggallahir = str_replace(' ', '', $tanggallahir);
    //ubah ke format tanggal
    $tanggallahir = date('Y-m-d', strtotime($tanggallahir));
    $agama_id = $_POST['agama_id']; //1,2,3,4,5,6,7
    $pekerjaan_id = $_POST['pekerjaan_id']; //1 - 89
    $status_kawin = $_POST['status_kawin']; // 1,2,3,4
    $alamat = $_POST['alamat']; //text dipakai untuk alamat_sekarang & sebelumnya
    $id_cluster = $_POST['id_cluster']; //1 - 89
    //$created_at agar seperti ini 2023-11-22 02:01:44
    $created_at = date('Y-m-d H:i:s');
    //10 angka random untuktag_id_card
    $tag_id_card = rand(1000000000, 9999999999);
    
    $query = "INSERT INTO `tweb_penduduk` ( `nama`, `nik`, `id_kk`, `kk_level`, `id_rtm`, `rtm_level`, `sex`, `tempatlahir`, `tanggallahir`, `agama_id`, `pendidikan_kk_id`, `pendidikan_sedang_id`, `pekerjaan_id`, `status_kawin`, `warganegara_id`, `dokumen_pasport`, `dokumen_kitas`, `ayah_nik`, `ibu_nik`, `nama_ayah`, `nama_ibu`, `foto`, `golongan_darah_id`, `id_cluster`, `status`, `alamat_sebelumnya`, `alamat_sekarang`, `status_dasar`, `hamil`, `cacat_id`, `sakit_menahun_id`, `akta_lahir`, `akta_perkawinan`, `tanggalperkawinan`, `akta_perceraian`, `tanggalperceraian`, `cara_kb_id`, `telepon`, `tanggal_akhir_paspor`, `no_kk_sebelumnya`, `ktp_el`, `status_rekam`, `waktu_lahir`, `tempat_dilahirkan`, `jenis_kelahiran`, `kelahiran_anak_ke`, `penolong_kelahiran`, `berat_lahir`, `panjang_lahir`, `tag_id_card`, `created_at`, `created_by`, `updated_at`, `updated_by`, `id_asuransi`, `no_asuransi`, `email`, `email_token`, `email_tgl_kadaluarsa`, `email_tgl_verifikasi`, `telegram`, `telegram_token`, `telegram_tgl_kadaluarsa`, `telegram_tgl_verifikasi`, `bahasa_id`, `ket`, `negara_asal`, `tempat_cetak_ktp`, `tanggal_cetak_ktp`, `suku`, `bpjs_ketenagakerjaan`, `hubung_warga`) VALUES
    ('$nama', '$nik', 0, 1, NULL, NULL, 1, '$tempatlahir',  '$tanggallahir', $agama_id, 1, 1, $pekerjaan_id, $status_kawin, 1, '', NULL, NULL, NULL, NULL, NULL, NULL, 1, $id_cluster, 1, '$alamat', '$alamat', 1, NULL, 7, 14, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 3, '00:00', 2, 1, 1, 1, 0, '0', '$tag_id_card', '$created_at', 1, '$created_at', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, 'Email');";
   $exeQuery = mysqli_query($conn, $query);
    //ambil id terakhir
    $id = mysqli_insert_id($conn);
    $query2="INSERT INTO `log_penduduk` (`id`, `id_pend`, `kode_peristiwa`, `meninggal_di`, `jam_mati`, `sebab`, `penolong_mati`, `akta_mati`, `alamat_tujuan`, `tgl_lapor`, `tgl_peristiwa`, `catatan`, `no_kk`, `nama_kk`, `ref_pindah`, `created_at`, `created_by`, `updated_at`, `updated_by`, `maksud_tujuan_kedatangan`) VALUES (NULL, '$id', '1', NULL, NULL, NULL, NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, NULL, NULL, '1', CURRENT_TIMESTAMP, 1, CURRENT_TIMESTAMP, NULL, NULL);";
    $exeQuery2 = mysqli_query($conn, $query2);
    //check apakah berhasil
    // $exeQuery = false;
    // $exeQuery2 =false;
    if($exeQuery && $exeQuery2){
        echo json_encode(array('kode' => 1, 'pesan' => 'Berhasil menambahkan data'));
    }else{
        if($exeQuery){
            $query3="DELETE FROM `tweb_penduduk` WHERE `tweb_penduduk`.`id` = $id";
            $exeQuery3 = mysqli_query($conn, $query3);
        }
        echo json_encode(array('kode' => 2, 'pesan' => 'Data gagal ditambahkan '.mysqli_error($conn).' query '.$query));
    }
    
} else {
    echo json_encode(array('kode' => 101, 'pesan' => 'Request tidak valid'));
}
?>