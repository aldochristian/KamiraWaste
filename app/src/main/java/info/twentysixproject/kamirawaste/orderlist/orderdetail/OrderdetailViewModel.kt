package info.twentysixproject.kamirawaste.orderlist.orderdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.twentysixproject.kamirawaste.orderlist.Orders

class OrderdetailViewModel : ViewModel() {
    //get ID
    var orderDetail= MutableLiveData<List<Orders>>()

    //Load the detail
    fun fetchFirestore(idOrder:String){
        val orderFetch : MutableList<Orders> = mutableListOf()

        orderFetch.add(
            Orders(
                idOrder,
                null,
                "Unknown",
                "Aldo",
                "08114110823",
                "No note",
                ""
            )
        )
        orderDetail.value = orderFetch
    }

    fun getOrderDetail(): LiveData<List<Orders>> {
        return orderDetail
    }

}