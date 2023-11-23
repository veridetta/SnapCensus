package com.example.snapcensus.helper

import android.R
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatSpinner
import com.google.android.material.snackbar.Snackbar

fun levenshteinDistance(s1: String, s2: String): Int {
    val dp = Array(s1.length + 1) { IntArray(s2.length + 1) { 0 } }

    for (i in 0..s1.length) {
        for (j in 0..s2.length) {
            when {
                i == 0 -> dp[i][j] = j
                j == 0 -> dp[i][j] = i
                else -> {
                    dp[i][j] = if (s1[i - 1] == s2[j - 1]) {
                        dp[i - 1][j - 1]
                    } else {
                        1 + minOf(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1])
                    }
                }
            }
        }
    }

    return dp[s1.length][s2.length]
}

fun initSpinner(sp: Spinner, genderOptions: Array<String>){
    val adapter = ArrayAdapter(sp.context, R.layout.simple_spinner_item, genderOptions)
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    sp.adapter = adapter
}

fun cekSimilarity(sp: Spinner, genderOptions: Array<String>, userInput: String) {
    val threshold = 2 // Atur threshold sesuai kebutuhan
    var closestMatch: String? = null
    var minDistance = Int.MAX_VALUE

    for (option in genderOptions) {
        val distance = levenshteinDistance(userInput, option)
        if (distance < minDistance && distance <= threshold) {
            minDistance = distance
            closestMatch = option
        }
    }

    if (closestMatch != null) {
        val selectedIndex = genderOptions.indexOf(closestMatch)
        if (selectedIndex != -1) {
            sp.setSelection(selectedIndex)
            println("Kemungkinan yang dipilih adalah: $closestMatch")
        } else {
            println("Tidak dapat menemukan indeks opsi yang cocok.")
        }
    } else {
        println("Tidak ada yang cocok atau tidak ada kesamaan yang mencukupi.")
        sp.setSelection(0)
    }
}

fun showSnackbar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
}