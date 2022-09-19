package app.motsu.hiromoto.umajo_app

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import app.motsu.hiromoto.umajo_app.databinding.ActivityMainBinding
import java.sql.Time
import java.time.LocalDateTime
import java.time.LocalTime

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
        // とりあえず仮で直接入力
        addItem()
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
        public val workItemList = ArrayList<WorkItem>()
    }

    private fun addItem(){
        orderItemList.add(OrderItem("瓶トニックウォーター","本"))
        orderItemList.add(OrderItem("瓶コーラ","本"))
        orderItemList.add(OrderItem("瓶ジンジャー","本"))
        orderItemList.add(OrderItem("アップルジュース","本"))
        orderItemList.add(OrderItem("オレンジジュース","本"))
        orderItemList.add(OrderItem("パイナップルジュース","本"))
        orderItemList.add(OrderItem("グレープフルーツジュース","本"))
        orderItemList.add(OrderItem("マンゴージュース","本"))
        orderItemList.add(OrderItem("トマトジュース","本"))
        orderItemList.add(OrderItem("牛乳","本"))
        orderItemList.add(OrderItem("午後の紅茶 ミルクティー","本"))
        orderItemList.add(OrderItem("午後の紅茶 ストレート","本"))
        orderItemList.add(OrderItem("午後の紅茶 レモンティー","本"))
        orderItemList.add(OrderItem("ジャスミン茶","本"))
        orderItemList.add(OrderItem("緑茶","ケース"))
        orderItemList.add(OrderItem("ウーロン茶","本"))
        orderItemList.add(OrderItem("水","ケース"))
        orderItemList.add(OrderItem("コーヒー","本"))
        orderItemList.add(OrderItem("ピーチネクター","本"))
        orderItemList.add(OrderItem("カルピス原液","本"))
        orderItemList.add(OrderItem("炭酸水","ケース"))
        orderItemList.add(OrderItem("レッドブル","本"))
        orderItemList.add(OrderItem("瓶ノンアルビール","本"))
        orderItemList.add(OrderItem("シロップ 桃","本"))
        orderItemList.add(OrderItem("シロップ 青リンゴ","本"))
        orderItemList.add(OrderItem("シロップ レモン","本"))
        orderItemList.add(OrderItem("シロップ グレープフルーツ","本"))
        orderItemList.add(OrderItem("シロップ ライム","本"))
        orderItemList.add(OrderItem("シロップ 巨峰","本"))
        orderItemList.add(OrderItem("カルロロッシ 白","本"))
        orderItemList.add(OrderItem("カルロロッシ 赤","本"))
        orderItemList.add(OrderItem("クライナー レッドベリーサワー","箱"))
        orderItemList.add(OrderItem("クライナー オリジナル","箱"))
        orderItemList.add(OrderItem("クライナー ユズ","箱"))
        orderItemList.add(OrderItem("クライナー ココナッツ","箱"))
        orderItemList.add(OrderItem("クライナー アナナスサワー","箱"))
        orderItemList.add(OrderItem("いいちこシルエット","本"))
        orderItemList.add(OrderItem("黒霧島 1.8Lパック","本"))
        orderItemList.add(OrderItem("茉莉花","本"))
        orderItemList.add(OrderItem("二階堂25% 900ml","本"))
        orderItemList.add(OrderItem("赤霧島 900ml","本"))
        orderItemList.add(OrderItem("鏡月20% 4L","本"))
        orderItemList.add(OrderItem("かのか25% 4L","本"))
        orderItemList.add(OrderItem("ブラックニッカ 4L","本"))
        orderItemList.add(OrderItem("ウィルキンソン ジン1.8L","本"))
        orderItemList.add(OrderItem("ウィルキンソン ウォッカ 1.8L","本"))
        orderItemList.add(OrderItem("マンゴヤン","本"))
        orderItemList.add(OrderItem("パライソ","本"))
        orderItemList.add(OrderItem("マリブ","本"))
        orderItemList.add(OrderItem("ルジェカシス","本"))
        orderItemList.add(OrderItem("ルジェピーチ","本"))
        orderItemList.add(OrderItem("カンパリ","本"))
        orderItemList.add(OrderItem("カルーア","本"))
        orderItemList.add(OrderItem("ミスティア","本"))
        orderItemList.add(OrderItem("鍛高譚梅酒","本"))
        orderItemList.add(OrderItem("梅酒","本"))
        orderItemList.add(OrderItem("翠","本"))
        orderItemList.add(OrderItem("コカレロ","本"))
        orderItemList.add(OrderItem("テキーラ ゴールド","本"))
        orderItemList.add(OrderItem("ビール 樽10L","本"))
        orderItemList.add(OrderItem("樽ハイプレーン 10L","本"))

        for(i in 0..20){
            workItemList.add(WorkItem("タスク$i"))
        }
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

public class WorkItem{
    var title = ""
    var status = false

    constructor(title:String) {
        this.title = title
    }
}

public class TimeItem{
    private var _startTime: LocalDateTime? = null
    private var _allottedTime: LocalTime? = null
    val startTime get() = _startTime!!
    val allottedTime get() = _allottedTime!!

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(startTime: LocalDateTime) {
        this._startTime = startTime
        _allottedTime = LocalTime.of(1,0)
    }
}