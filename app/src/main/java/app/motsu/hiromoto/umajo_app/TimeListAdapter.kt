package app.motsu.hiromoto.umajo_app

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.coroutines.coroutineContext

class TimeListAdapter(private val timeItems:ArrayList<TimeItem>,
                      private val rootActivity: AppCompatActivity): RecyclerView.Adapter<TimeListAdapter.ViewHolder>() {

    private val views = ArrayList<ViewHolder>()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val startTimeView:TextView = view.findViewById(R.id.time_item_start_textView)
        val endTimeView:TextView = view.findViewById(R.id.time_item_end_textView)
        val elapsedTimeView:TextView = view.findViewById(R.id.time_item_elapsed_textView)
        val leftTimeView:TextView = view.findViewById(R.id.time_item_left_textView)
        val moreTimeButton:Button = view.findViewById(R.id.time_item_more_button)
        var myItem:TimeItem? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.time_view_item
            , parent, false)
        return ViewHolder(item)
    }


    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = timeItems[position]
        views.add(holder)
        holder.myItem = item
        holder.startTimeView.text = item.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        holder.endTimeView.text = item.endTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        holder.elapsedTimeView.text = item.elapsedTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        holder.leftTimeView.text = item.leftTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        if(item.isOver()){
            holder.leftTimeView.setTextColor(Color.RED)
            holder.leftTimeView.text = item.leftTime.format(DateTimeFormatter.ofPattern("-HH:mm:ss"))
        }else if(item.isAlmost()){
            holder.leftTimeView.setTextColor(Color.argb(255,255, 201, 8))
            if(item.notificationFlag){
                item.notificationFlag = false
                // 通知タップ時の遷移先を設定
                val intent = Intent(rootActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent: PendingIntent = PendingIntent.getActivity(rootActivity, 0, intent, 0)

                //通知オブジェクトの作成
                var builder = NotificationCompat.Builder(rootActivity, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("お時間確認！")
                    .setContentText("お席のお時間が近づいてきました！確認してください")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)

                //通知の実施
                val notificationManager: NotificationManager =
                    rootActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(0, builder.build())
            }
        }else{
            holder.leftTimeView.setTextColor(Color.GRAY)
        }

        holder.moreTimeButton.setOnClickListener {
            TimePickerDialog(rootActivity, { timePicker, h, m ->
                timeItems[position].addTime(LocalTime.of(h,m))
            },0,30,true ).show()
        }

        holder.itemView.setOnLongClickListener {
            timeItems.removeAt(position)

            notifyDataSetChanged()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount() = timeItems.size
}

