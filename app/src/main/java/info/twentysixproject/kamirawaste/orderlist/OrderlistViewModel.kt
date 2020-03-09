package info.twentysixproject.kamirawaste.orderlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderlistViewModel : ViewModel() {
    var orderList= MutableLiveData<List<Orders>>()

    private val _navigateToOrderDetail = MutableLiveData<Orders>()
    val navigateToOrderDetail
        get() = _navigateToOrderDetail

    fun fetchFirestore(){
        val orderFetch : MutableList<Orders> = mutableListOf()

        orderFetch.add(
            Orders(
            "ID0001",
            null,
            "Unknown",
            "Aldo",
            "08114110823",
            "No note",
                ""
            )
        )

        orderFetch.add(
            Orders(
                "ID0001",
                null,
                "Unknown",
                "Aldo",
                "08114110823",
                "No note",
                ""
            )
        )

        orderList.value = orderFetch
    }

    fun getOrderList(): LiveData<List<Orders>>{
        return orderList
    }

    fun onDetailClicked(data: Orders) {
        _navigateToOrderDetail.value = data
    }

    fun onOrderDetailNavigated() {
        _navigateToOrderDetail.value = null
    }

}