package app.motsu.hiromoto.umajo_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import app.motsu.hiromoto.umajo_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }
        
        binding.orderButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragmentContainerView, OrderFragment(), "Order")
            }.commit()
            setButtonActive(false)
        }

        binding.timeButton.setOnClickListener{
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragmentContainerView, TimeManageFragment(), "Time")
            }.commit()
            setButtonActive(false)
        }

        binding.workButton.setOnClickListener{
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragmentContainerView, WorkManageFragment(), "Work")
            }.commit()
            setButtonActive(false)
        }

        for(i in 0..10){
            orderItemList.add(OrderItem("item$i","本"))
        }
    }
    
    private fun setButtonActive(boolean: Boolean){
        binding.orderButton.isVisible = boolean
        binding.timeButton.isVisible = boolean
        binding.workButton.isVisible = boolean
    }

    override fun onBackPressed() {
        supportFragmentManager.beginTransaction().apply{
            for(fragment in supportFragmentManager.fragments)
            remove(fragment)
        }.commit()
        setButtonActive(true)
        if(supportFragmentManager.fragments.size == 0){
            super.onBackPressed()
        }
    }

    companion object{
        public val orderItemList = ArrayList<OrderItem>()
    }
}

public class OrderItem{
    var name = ""
    var lot = ""
    var value = 0

    constructor(name: String , lot:String) {
        this.name = name
        this.lot = lot
    }

    override fun toString(): String {
        return "$name $value $lot"
    }

}