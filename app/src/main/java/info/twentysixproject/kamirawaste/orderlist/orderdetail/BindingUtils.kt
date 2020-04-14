package info.twentysixproject.kamirawaste.orderlist.orderdetail

import android.widget.TextView
import androidx.databinding.BindingAdapter
import info.twentysixproject.kamirawaste.R
import info.twentysixproject.kamirawaste.orderlist.Orders

@BindingAdapter("sleepImage")
fun TextView.setStatusNotes(item: Orders?) {
    item?.let {
        setText(when (item.status) {
            "pending" -> "Pending"
            else -> "Order anda bermasalah"
        })
    }
}