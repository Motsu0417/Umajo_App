package app.motsu.hiromoto.umajo_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class WorkListAdapter(private val workItems:ArrayList<WorkItem>,
                      private val rootActivity: AppCompatActivity): RecyclerView.Adapter<WorkListAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val workCheckBox:CheckBox = view.findViewById(R.id.work_item_checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.work_view_item
            , parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.workCheckBox.text = workItems[position].title
        holder.workCheckBox.isChecked = workItems[position].status
        holder.workCheckBox.setOnClickListener{
            workItems[position].status = !workItems[position].status
        }

    }

    override fun getItemCount() = workItems.size
}