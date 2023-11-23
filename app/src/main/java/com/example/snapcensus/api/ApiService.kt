package com.example.snapcensus.api

import com.example.snapcensus.model.GetModel
import com.example.snapcensus.model.ResponseModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("/api/get.php?api_key=87Y78GF78SHFDSHFU")
    fun getJenisKelamin(): Call<GetModel>

    @FormUrlEncoded
    @POST("/api/create_penduduk.php?api_key=87Y78GF78SHFDSHFU")
    fun postData(
        @Field("nik") qKtp: String,
        @Field("nama") qNama: String,
        @Field("tempatlahir") qTempat: String,
        @Field("tanggallahir") qTanggal: String,
        @Field("alamat") qAlamat: String,
        @Field("qRt") qRt: String,
        @Field("qRw") qRw: String,
        @Field("id_cluster") qKelurahan: String,
        @Field("qKecamatan") qKecamatan: String,
        @Field("agama_id") qAgama: String,
        @Field("status_kawin") qStatus: String,
        @Field("pekerjaan_id") qPekerjaan: String,
        @Field("sex") qJk: String
    ): Call<ResponseModel>
}
