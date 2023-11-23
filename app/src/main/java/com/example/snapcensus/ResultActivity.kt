package com.example.snapcensus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.snapcensus.api.ApiService
import com.example.snapcensus.helper.cekSimilarity
import com.example.snapcensus.helper.initSpinner
import com.example.snapcensus.helper.showSnackbar
import com.example.snapcensus.model.GetModel
import com.example.snapcensus.model.ResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.regex.Pattern

class ResultActivity : AppCompatActivity() {
    lateinit var konten : RelativeLayout
    lateinit var etNik: EditText
    lateinit var spJk: Spinner
    lateinit var etNama: EditText
    lateinit var etTempat: EditText
    lateinit var etTanggal: EditText
    lateinit var etAlamat: EditText
    lateinit var spRw: Spinner
    lateinit var spRt: Spinner
    lateinit var spKelurahan: Spinner
    lateinit var etKecamatan: EditText
    lateinit var spAgama: Spinner
    lateinit var spStatus: Spinner
    lateinit var spPekerjaan: Spinner
    lateinit var btnSimpan : Button
    lateinit var lyProses : CoordinatorLayout
    var stringMentah =""
    var sKtp = ""
    var sNama = ""
    var sTtl = ""
    var sAlamat = ""
    var sRtRw = ""
    var sKelurahan = ""
    var sKecamatan = ""
    var sAgama = ""
    var sJk = ""
    var sStatus = ""
    var sPekerjaan = ""

    var qKtp=""
    var qNama=""
    var qTempat=""
    var qTanggal=""
    var qAlamat=""
    var qRt=""
    var qRw=""
    var qKelurahan=""
    var qKecamatan=""
    var qAgama=""
    var qStatus=""
    var qPekerjaan=""
    var qJk=""
    var isCamera = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        initView()
        initIntent()
        setView()
        getData()
    }

    private fun initView(){
        konten = findViewById(R.id.contentView)
        etNik = findViewById(R.id.etNik)
        etNama = findViewById(R.id.etNama)
        etTempat = findViewById(R.id.etTempat)
        etTanggal = findViewById(R.id.etTanggal)
        etAlamat = findViewById(R.id.etAlamat)
        spRt = findViewById(R.id.spRt)
        spRw = findViewById(R.id.spRw)
        spKelurahan = findViewById(R.id.spKelurahan)
        etKecamatan = findViewById(R.id.etKecamatan)
        spAgama = findViewById(R.id.spAgama)
        btnSimpan = findViewById(R.id.btnSimpan)
        spStatus = findViewById(R.id.spStatus)
        spJk = findViewById(R.id.spJk)
        spPekerjaan = findViewById(R.id.spPekerjaan)
        lyProses = findViewById(R.id.lyProses)
        lyProses.visibility = CoordinatorLayout.VISIBLE
    }
    private fun initIntent(){
        stringMentah = intent.getStringExtra("text").toString()
        isCamera = intent.getBooleanExtra("isCamera", false)
        if(isCamera){

            val nikRegex = Regex("[0-9]{8,16}")
            sKtp = nikRegex.find(stringMentah)?.value ?: ""
            val statusRegex = Regex("Status Perkawinan\\s*:\\s*(.*)\\s*Pekerjaan")
            sStatus = statusRegex.find(stringMentah)?.groups?.get(1)?.value ?: ""
            //val ttlRegex = Regex("[^,]+, \\d{2}-\\d{2}-\\d{4}")
            val ttlRegex = Regex("(?<=Tempat[\\W]?Tgl[\\W]?Lahir|TempatgLahir)\\s+([^,]+, \\d{2}-\\d{2}-\\d{4})")
            val match = ttlRegex.find(stringMentah)
            sTtl = match?.groupValues?.getOrNull(1) ?: ""
            //sTtl = ttlRegex.find(stringMentah)?.value ?: ""
            val jkRegex = Regex("LAKI[ -]?LAKI|Perempuan", RegexOption.IGNORE_CASE)
            sJk = jkRegex.find(stringMentah)?.value ?: ""
            val rtrwRegex = Regex("\\d{3}[ /]\\d{3}")
            sRtRw = rtrwRegex.find(stringMentah)?.value ?: ""
            val matchNik = nikRegex.find(stringMentah)
            sNama = matchNik?.next()?.value ?: ""
            //sNama = stringMentah.split("\n")[12]
            //sTtl = stringMentah.split("\n")[14]
            //sJk = stringMentah.split("\n")[15]
            sAlamat = stringMentah.split("\n")[13]
            //sRtRw = stringMentah.split("\n")[19]
            sKelurahan = stringMentah.split("\n")[15]
            sKecamatan = stringMentah.split("\n")[16]
            sAgama = stringMentah.split("\n")[17]
            //sStatus = stringMentah.split("\n")[21]
            if(sStatus.contains(":")){
                sStatus = sStatus.split(":")[1]
            }

            sPekerjaan = stringMentah.split("\n")[23]
            if(sPekerjaan.contains(":")){
                sPekerjaan = sPekerjaan.split(":")[1]
            }
        }else{
            val regexNikPattern = "[0-9]{8,16}"
            val nikPattern = Pattern.compile(regexNikPattern)
            val matcherNik = nikPattern.matcher(stringMentah)
            if(matcherNik.find()){
                sKtp =matcherNik.group()
            }
            sNama = stringMentah.split("\n")[12]
            sTtl = stringMentah.split("\n")[13]
            sJk = stringMentah.split("\n")[14]
            sAlamat = stringMentah.split("\n")[15]
            sRtRw = stringMentah.split("\n")[16]
            sKelurahan = stringMentah.split("\n")[17]
            sKecamatan = stringMentah.split("\n")[18]
            sAgama = stringMentah.split("\n")[19]
            sStatus = stringMentah.split("\n")[20]
            if(sStatus.contains(":")){
                sStatus = sStatus.split("Status Perkawinan: ")[1]
            }

            sPekerjaan = stringMentah.split("\n")[24]
            if(sPekerjaan.contains(":")){
                sPekerjaan = sPekerjaan.split(":")[1]
            }
        }

    }

    private fun setView(){
        etNik.setText(sKtp)
        etNama.setText(sNama)
        if(sTtl.contains(",")){
            etTempat.setText(sTtl.split(",")[0])
            etTanggal.setText(sTtl.split(",")[1])
        }else if(sTtl.contains(" ")){
            etTempat.setText(sTtl.split(" ")[0])
            etTanggal.setText(sTtl.split(" ")[1])
        }else{
            etTempat.setText(sTtl)
            etTanggal.setText("2023-10-25")
        }

        etAlamat.setText(sAlamat)
        etKecamatan.setText(sKecamatan)
    }
    fun getData(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://desabulila.com") // Ganti BASE_URL_API_ANDA dengan URL base API Anda
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)
        val call = service.getJenisKelamin()
        //timber url request
        Timber.d("requestnya: ${call.request().url}")
        Log.d("cek data", "requestnya: ${call.request().url}")
        call.enqueue(object : Callback<GetModel> {
            override fun onResponse(
                call: Call<GetModel>,
                response: Response<GetModel>
            ) {
                if (response.isSuccessful) {
                    Log.d("cek data "+response.body()?.jenisKelamin?.size, "onResponse: ${response.body()?.jenisKelamin?.get(0)?.nama}")
                    val jenisKelaminList = response.body()?.jenisKelamin
                    val namaJenisKelaminList = jenisKelaminList?.map { it.nama } ?: listOf()
                    val idJenisKelaminList = jenisKelaminList?.map { it.id } ?: listOf()
                    val agamaList = response.body()?.agama
                    val namaAgamaList = agamaList?.map { it.nama } ?: listOf()
                    val idAgamaList = agamaList?.map { it.id } ?: listOf()
                    val statusList = response.body()?.statusKawin
                    val namaStatusList = statusList?.map { it.nama } ?: listOf()
                    val idStatusList = statusList?.map { it.id } ?: listOf()
                    val pekerjaanList = response.body()?.pekerjaan
                    val namaPekerjaanList = pekerjaanList?.map { it.nama } ?: listOf()
                    val idPekerjaanList = pekerjaanList?.map { it.id } ?: listOf()
                    val dusunList = response.body()?.dusun
                    val namaDusunList = dusunList?.map { it.rt+" "+it.rw+" "+it.dusun } ?: listOf()
                    val idDusunList = dusunList?.map { it.id } ?: listOf()

                    val rtList = mutableListOf<String>()
                    val rwList = mutableListOf<String>()
                    for (i in 1..20){
                        if (i<10){
                            rtList.add("00$i")
                            rwList.add("00$i")
                        }else{
                            rtList.add("0$i")
                            rwList.add("0$i")
                        }
                    }
                    initSpinner(spRt, rtList.toTypedArray())
                    initSpinner(spJk, namaJenisKelaminList.toTypedArray())
                    initSpinner(spAgama, namaAgamaList.toTypedArray())
                    initSpinner(spStatus, namaStatusList.toTypedArray())
                    initSpinner(spPekerjaan, namaPekerjaanList.toTypedArray())
                    initSpinner(spRw, rwList.toTypedArray())
                    initSpinner(spKelurahan, namaDusunList.toTypedArray())
                    if (sRtRw.contains("/")){
                        cekSimilarity(spRt,rtList.toTypedArray(), sRtRw.split("/")[0])
                        cekSimilarity(spRw,rwList.toTypedArray(), sRtRw.split("/")[1])
                        cekSimilarity(spKelurahan,namaDusunList.toTypedArray(), sRtRw.split("/")[0]+" "+
                                sRtRw.split("/")[1]+" "+sKelurahan)
                    }
                    cekSimilarity(spJk,namaJenisKelaminList.toTypedArray(), sJk)
                    cekSimilarity(spAgama,namaAgamaList.toTypedArray(), sAgama)
                    cekSimilarity(spStatus,namaStatusList.toTypedArray(), sStatus)
                    cekSimilarity(spPekerjaan,namaPekerjaanList.toTypedArray(), sPekerjaan)
                    lyProses.visibility = CoordinatorLayout.GONE
                    btnSimpan.setOnClickListener {
                        lyProses.visibility = CoordinatorLayout.VISIBLE
                        qKtp = etNik.text.toString()
                        qNama = etNama.text.toString()
                        qTempat = etTempat.text.toString()
                        //etTanggal = 25-10-1972 diubah ke 1972-10-25
                        val tgl = etTanggal.text.toString().split("-")
                        etTanggal.setText(tgl[2]+"-"+tgl[1]+"-"+tgl[0])
                        qTanggal = etTanggal.text.toString()
                        qAlamat = etAlamat.text.toString()
                        qRt = spRt.selectedItem.toString()
                        qRw = spRw.selectedItem.toString()
                        qKelurahan = idDusunList[namaDusunList.indexOf(spKelurahan.selectedItem.toString())]
                        qKecamatan = etKecamatan.text.toString()
                        qAgama = idAgamaList[namaAgamaList.indexOf(spAgama.selectedItem.toString())]
                        qStatus = idStatusList[namaStatusList.indexOf(spStatus.selectedItem.toString())]
                        qPekerjaan = idPekerjaanList[namaPekerjaanList.indexOf(spPekerjaan.selectedItem.toString())]
                        qJk = idJenisKelaminList[namaJenisKelaminList.indexOf(spJk.selectedItem.toString())]
                        //masukkan log
                        Log.d("ResultActivity", "datanya: $qKtp $qNama $qTempat $qTanggal $qAlamat $qRt $qRw $qKelurahan $qKecamatan $qAgama $qStatus $qPekerjaan $qJk")
                        postData()
                    }
                } else {
                    // Handle jika respons tidak berhasil
                    Timber.d("onResponse: ${response.message()}")
                    Log.d("cek data", "onResponse: ${response.message()}")
                    lyProses.visibility = CoordinatorLayout.GONE
                }
            }

            override fun onFailure(call: Call<GetModel>, t: Throwable) {
                // Handle jika ada kegagalan dalam permintaan jaringan
                Timber.d("onFailure: ${t.message}")
                Log.d("cek data", "onFailure: ${t.message}")
                lyProses.visibility = CoordinatorLayout.GONE

            }
        })
    }


    fun postData(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://desabulila.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)
        // Mengganti dengan nilai-nilai yang diperoleh dari input pengguna
        val call = service.postData(qKtp, qNama, qTempat, qTanggal, qAlamat, qRt, qRw, qKelurahan, qKecamatan, qAgama, qStatus, qPekerjaan, qJk)
        call.enqueue(object : Callback<ResponseModel> {
            override fun onResponse(
                call: Call<ResponseModel>,
                response: Response<ResponseModel>
            ) {
                if (response.isSuccessful) {
                    // Berhasil melakukan permintaan
                    if(response.body()?.kode == 1){
                        // Berhasil mengirim data
                        showSnackbar(konten, response.body()?.pesan.toString())
                        lyProses.visibility = CoordinatorLayout.GONE
                    } else {
                        // Gagal mengirim data
                        showSnackbar(konten, response.body()?.pesan.toString())
                        Log.d("cek data", "onResponse: ${response.body()?.pesan.toString()}")
                        lyProses.visibility = CoordinatorLayout.GONE
                    }
                } else {
                    // Handle respons gagal dari server
                    showSnackbar(konten, response.message())
                    lyProses.visibility = CoordinatorLayout.GONE
                }
            }

            override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                // Handle jika ada kegagalan dalam permintaan jaringan
                showSnackbar(konten, t.message.toString())
                lyProses.visibility = CoordinatorLayout.GONE
            }
        })
    }
}