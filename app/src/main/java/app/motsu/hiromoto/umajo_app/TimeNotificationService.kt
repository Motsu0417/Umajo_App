package app.motsu.hiromoto.umajo_app

import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class TimeNotificationService : IntentService("TimeNotificationService") {

    override fun onHandleIntent(intent: Intent?) {
        if(intent == null) {
            stopSelf()
            return
        }
        val targetTimeStr = intent.getStringExtra("NotiTime")
        if(targetTimeStr.isNullOrBlank()) {
            stopSelf()
            return
        }
        val targetStrArray = targetTimeStr.split("-")
        val year = targetStrArray[0].toInt()
        val month = targetStrArray[1].toInt()
        val day = targetStrArray[2].toInt()
        val hour = targetStrArray[3].toInt()
        val min = targetStrArray[4].toInt()
        val sec = targetStrArray[5].toInt()
        val targetTime = LocalDateTime.of(year, month, day, hour, min, sec).minusSeconds(600)

        Log.d(TAG, "onHandleIntent: 通知がタスクに入りました, ${targetTime}")
        
        while(LocalDateTime.now() < targetTime){
            Thread.sleep(10000)
            Log.d(TAG, "onHandleIntent: 待機しています ${targetTime}")
        }

        sendNotification()
        stopSelf()
    }

    private fun sendNotification() {
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