package com.example.userconsumer.helper

import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.userconsumer.R
import de.hdodenhof.circleimageview.CircleImageView
import java.text.DecimalFormat

@BindingAdapter("avatar")
fun loadAvatar(view: CircleImageView, url: String?) {
    GlideApp.with(view)
        .load(url)
        .placeholder(R.drawable.ic_loading)
        .into(view)
}

@BindingAdapter("textValue", "textAlternative")
fun makeText(view: TextView, textValue: String?, textAlternative: String?) {
    if (textValue == null || textValue.isEmpty()) {
        view.text = textAlternative
    } else {
        view.text = textValue
    }
}

@BindingAdapter("isFav")
fun setBackground(view: ImageButton, isFav: Boolean) {
    if (isFav) {
        view.setBackgroundResource(R.drawable.ic_favorite_48)
    } else {
        view.setBackgroundResource(R.drawable.ic_favorite_border_48)
    }
}

@BindingAdapter(
    "numberSimplified",
    "numberLabelThousands",
    "numberLabelMillions",
    "numberProperties"
)
fun formatNumber(
    view: TextView,
    numberSimplified: Int,
    numberLabelThousands: String,
    numberLabelMillions: String,
    numberProperties: String
) {
    when (numberSimplified) {
        in 0..9999 -> {
            val formatted = "$numberSimplified $numberProperties"
            view.text = formatted
        }
        in 10000..999999 -> {
            val formatted = "${
                DecimalFormat("#.##").format(
                    numberSimplified.toDouble().div(1000)
                )
            }$numberLabelThousands $numberProperties"
            view.text = formatted
        }
        in 1000000..Int.MAX_VALUE -> {
            val formatted =
                "${
                    DecimalFormat("#.##").format(
                        numberSimplified.toDouble().div(1000000)
                    )
                }$numberLabelMillions $numberProperties"
            view.text = formatted
        }
    }
}