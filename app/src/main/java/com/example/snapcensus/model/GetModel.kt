package com.example.snapcensus.model

data class GetModel(
    val jenisKelamin: List<JenisKelamin>,
    val agama: List<Agama>,
    val pekerjaan: List<Pekerjaan>,
    val statusKawin: List<StatusKawin>,
    val golonganDarah: List<GolonganDarah>,
    val dusun: List<Dusun>
)

data class JenisKelamin(
    val id: String,
    val nama: String
)

data class Agama(
    val id: String,
    val nama: String
)

data class Pekerjaan(
    val id: String,
    val nama: String
)

data class StatusKawin(
    val id: String,
    val nama: String
)

data class GolonganDarah(
    val id: String,
    val nama: String
)

data class Dusun(
    val id: String,
    val rt: String,
    val rw: String,
    val dusun: String,
    val idKepala: String // Jika perlu tambahan field, sesuaikan di sini
    // Tambahkan properti lain sesuai kebutuhan
)
