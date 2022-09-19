package app.motsu.hiromoto.umajo_app

import android.app.TimePickerDialog
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeListAdapter(private val timeItems:ArrayList<TimeItem>,
                      private val rootActivity: AppCompatActivity): RecyclerView.Adapter<TimeListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val startTimeView:TextView = view.findViewById(R.id.time_item_start_textView)
        val endTimeView:TextView = view.findViewById(R.id.time_item_end_textView)
        val elapsedTimeView:TextView = view.findViewById(R.id.time_item_elapsed_textView)
        val leftTimeView:TextView = view.findViewById(R.id.time_item_left_textView)
        val moreTimeButton:Button = view.findViewById(R.id.time_item_more_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.work_view_item
            , parent, false)
        return ViewHolder(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.startTimeView.text = timeItems[position].startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        holder.endTimeView.text = (timeItems[position].startTime + timeItems[position].allottedTime).format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        var hour = 0
        var minute = 0
        holder.moreTimeButton.setOnClickListener {
            TimePickerDialog(rootActivity, { timePicker, h, m ->
//                timePicker.hour = 0
//                timePicker.minute = 0

                hour = h
                minute = m
            },0,0,true ).show()
            timeItems.add(TimeItem(LocalDateTime.of(2022,LocalDate.now().month,LocalDate.now().dayOfMonth, hour, minute)))

        }
    }

    override fun getItemCount() = timeItems.size
}

@RequiresApi(Build.VERSION_CODES.O)
private operator fun LocalDateTime.plus(time: LocalTime): LocalDateTime {
    return this.plusHours(time.hour.toLong()).plusMinutes(time.minute.toLong())
}
