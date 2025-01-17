package com.example.android.politicalpreparedness.election


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetTextI18n")
@BindingAdapter("displayDate")
fun displayDate(textView: TextView, date: String?) {
    var dateString = "Unknown time"
    date?.let {
        if (date.isNotEmpty()) {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                dateFormat.parse(date)?.let {
                    dateString = it.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    textView.text = dateString
}

@SuppressLint("SetTextI18n")
@BindingAdapter("toggleTitle")
fun toggleTitle(button: Button, status: Boolean) {
    val title =
        button.context.getString(if (status) R.string.unfollow_election else R.string.follow_election)
    button.text = title
}

@BindingAdapter("onClickVoterInfoLink")
fun onClickVoterInfoLink(textView: TextView, url: String?) {
    textView.setOnClickListener {
        if (!url.isNullOrEmpty()) {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            textView.context.startActivity(browserIntent)
        } else {
            Toast.makeText(
                textView.context,
                textView.context.getString(R.string.link_invalid),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}