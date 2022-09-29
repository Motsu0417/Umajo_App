package app.motsu.hiromoto.umajo_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class OrderListAdapter(private val items:ArrayList<OrderItem>,
                       private val rootActivity: AppCompatActivity): RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val nameTextView:TextView = view.findViewById(R.id.order_item_title_text)
        val valueTextView:TextView = view.findViewById(R.id.order_item_value_text)
        val lotTextView:TextView = view.findViewById(R.id.order_item_lot_text)
        val plusButton: Button = view.findViewById(R.id.order_item_plus_button)
        val minusButton: Button = view.findViewById(R.id.order_item_minus_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.order_view_item, parent, false)
        return ViewHolder(item)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nameTextView.text = items[position].name
        holder.lotTextView.text = items[position].lot
        holder.valueTextView.text = items[position].value.toString()

        holder.plusButton.setOnClickListener {
            items[position].value++
            holder.valueTextView.text = items[position].value.toString()
        }

        holder.minusButton.setOnClickListener {
            items[position].value--
            holder.valueTextView.text = items[position].value.toString()
        }


    }

    override fun getItemCount() = items.size
}