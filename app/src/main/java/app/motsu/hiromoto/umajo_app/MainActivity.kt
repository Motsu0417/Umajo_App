package app.motsu.hiromoto.umajo_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.view.isVisible
import app.motsu.hiromoto.umajo_app.databinding.ActivityMainBinding
import kotlinx.coroutines.MainScope
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

const val CHANNEL_ID = "UMAJO_APP"
const val COUNT_ID_KEY = "COUNT_ID"
const val ORDER_MENU_URL = "https://script.google.com/macros/s/AKfycbwMzzgdIMOqxObncjT5xx4qyNFNzjS_mUiZF6Y7KqqIkyNu3ikzHWXtp9ymKLQgUjbL/exec"

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding

    private fun createNotificationChannel() {

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
            .build()

        val uri = Uri.parse("android.resource://$packageName/${R.raw.notify_horse}")

        val name = "UMA_NOTIFICATION_CHANNEL"
        val descriptionText = "ウマ女アプリ専用通知チャンネル"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
            setSound(uri, audioAttributes)
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        createNotificationChannel()

        getSharedPreferences(CHANNEL_ID, Context.MODE_PRIVATE).apply {
            idCount = getInt(COUNT_ID_KEY, 0)
        }

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

        binding.mainTestButton.setOnClickListener {
            // 通知タップ時の遷移先を設定
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

            //通知オブジェクトの作成
            var builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("お時間確認！")
                .setContentText("お席のお時間が近づいてきました！確認してください")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            //通知の実施
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, builder.build())
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

    override fun onDestroy() {
        getSharedPreferences(CHANNEL_ID, Context.MODE_PRIVATE).edit().apply {
            putInt(COUNT_ID_KEY, idCount)
        }.apply()
        timeItemList.forEach {
            if(it.notificationFlag){
                val intent = Intent(this, TimeNotificationService::class.java)
                intent.putExtra("NotiTime", it.endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")))
                startService(intent)
                it.notificationFlag = false
            }
        }

        super.onDestroy()
    }

    companion object{
        val orderItemList = ArrayList<OrderItem>()
        val workItemList = ArrayList<WorkItem>()
        val timeItemList = ArrayList<TimeItem>()
        val scope = MainScope()
        var idCount = 0
    }

    private fun addItem(){
        // 非同期のためのスレッド作成
        val thread = Thread{
            // OkHttpのクライアント
            val client = OkHttpClient()
            // Http通信のリクエスト
            val request = Request.Builder().apply {
                url(ORDER_MENU_URL)
            }.build()
            // リクエストを表示
            Log.d(TAG, "addItem: \n$request")
            try {
                // 実際に通信する部分
                val response = client.newCall(request).execute()
                // 取得したテキストデータを代入（stringを2回取り出そうとすると不明のErrorになる）
                val orderDataStr:String = response.body!!.string()
                Log.d(TAG, "addItem: \n$request\n${orderDataStr}")
                // テキストのデータを分割
                /**
                 * データの形は、
                 * "商品名:単位,商品名:単位"
                 * の文字列になっているので、
                 * それぞれ[,]毎に分割した後[:]毎に分割し、アイテムに追加している
                 */
                orderDataStr.split(",").forEach{
                    if(!it.isNullOrBlank()) {
                        val itemData = it.split(":")
                        orderItemList.add(OrderItem(itemData[0], itemData[1]))
                    }
                }
            }catch (e:Exception){
                Log.e(TAG, "addItem: ", e)
            }
        }
        thread.start()

        for(i in 0..20){
            workItemList.add(WorkItem("タスク$i"))
        }
    }
}

class OrderItem{
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

class WorkItem{
    var title = ""
    var status = false

    constructor(title:String) {
        this.title = title
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class TimeItem{
    // 開始時間
    private var _startTime: LocalDateTime? = null
    val startTime get() = _startTime!!
    // 持ち時間
    private var _allottedTime: LocalTime? = null
    val allottedTime get() = _allottedTime!!
    //終了予定時間
    private var _endTime: LocalDateTime? = null
    val endTime get() = _endTime!!
    // 経過時間
    private var _elapsedTime:LocalTime? = null
    val elapsedTime get() = _elapsedTime!!
    // 残り時間
    private var _leftTime:LocalTime? = null
    val leftTime get() = _leftTime!!
    // 通知送っているかどうか
    var notificationFlag = true
    // uuid
    private var _uuid: String? = null
    val uuid get() = _uuid!!

    constructor(startTime: LocalDateTime) {
        this._startTime = startTime
        _allottedTime = LocalTime.of(1,0)
        _uuid = this.startTime.format(DateTimeFormatter.ofPattern("yyyymmdd")) + String.format("%2d", MainActivity.idCount++)
        calculateTime()
    }

    fun addTime(time:LocalTime): TimeItem {
        _allottedTime = allottedTime
            .plusSeconds(time.toSecond())
        calculateTime()
        Log.d(TAG, "addTime: $_allottedTime")
        return this
    }


    fun calculateTime(){
        _endTime = startTime.plusSeconds(allottedTime.toSecond())

        _elapsedTime = LocalDateTime.now()
            .minusYears(startTime.year.toLong())
            .minusMonths(startTime.monthValue.toLong())
            .minusDays(startTime.dayOfMonth.toLong())
            .minusSeconds(startTime.toLocalTime().toSecond()).toLocalTime()

        _leftTime = if(isOver()) {
            elapsedTime.minusSeconds(allottedTime.toSecond())
        } else allottedTime.minusSeconds(elapsedTime.toSecond())
    }


    fun isOver():Boolean{
        return if(_endTime != null) endTime < LocalDateTime.now() else false
    }

    fun isAlmost():Boolean{
        return if(_endTime != null) endTime < LocalDateTime.now().plusMinutes(10) else false
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun LocalTime.toSecond(): Long {
    return ((this.hour * 60 + this.minute) * 60 + this.second).toLong()
}
