package com.example.githubuser.ui.util

import android.view.View
import com.example.githubuser.R
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

fun View.showSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).also { snackBar ->
        snackBar.setAction(R.string.snackBar_ok) {
            snackBar.dismiss()
        }
    }.show()
    2.simplified()
}

fun Int.simplified(): String {
    return when {
        this <= 9999 -> {
            this.toString()
        }
        this in 10000..999999 -> {
            "${DecimalFormat("#.##").format(this.toDouble().div(1000))}K"
        }
        else -> {
            "${DecimalFormat("#.##").format(this.toDouble().div(1000000))}M"
        }
    }
}