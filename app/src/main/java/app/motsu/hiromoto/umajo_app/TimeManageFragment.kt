package app.motsu.hiromoto.umajo_app

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.motsu.hiromoto.umajo_app.databinding.FragmentTimeManageBinding
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

const val TAG = "debug"

@RequiresApi(Build.VERSION_CODES.O)
class TimeManageFragment : Fragment() {

    private var _binding: FragmentTimeManageBinding? = null
    private val binding get() = _binding!!
    private val timeItems = MainActivity.timeItemList

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTimeManageBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener{
            TimePickerDialog(requireActivity(), { timePicker, hour, minute ->
                timeItems.add(TimeItem(
                    LocalDateTime.of(LocalDate.now().year,
                        LocalDate.now().month,
                        LocalDate.now().dayOfMonth, hour, minute)))
                binding.timeRecyclerView.adapter!!.notifyDataSetChanged()
            },LocalTime.now().hour,LocalTime.now().minute,true ).show()
        }

        binding.timeRecyclerView.apply {
            adapter = TimeListAdapter(timeItems, requireActivity() as AppCompatActivity)
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        MainActivity.scope.launch{
            // 時間のかかる処理
            while(_binding != null){
                timeItems.forEach{item -> item.calculateTime()}
                binding.timeRecyclerView.adapter!!.notifyDataSetChanged()
                delay(1000)
            }
        }.start()

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

    }
}