<?php
$host = "localhost";
$username = "root";
$password = "";
$dbname = "opensid";

// Membuat koneksi
$conn = mysqli_connect($host, $username, $password, $dbname);

// Cek koneksi
if (!$conn) {
    die("Koneksi gagal: " . mysqli_connect_error());
}
?>