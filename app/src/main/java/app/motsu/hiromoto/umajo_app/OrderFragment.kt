package app.motsu.hiromoto.umajo_app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.motsu.hiromoto.umajo_app.databinding.FragmentOrderBinding


@RequiresApi(Build.VERSION_CODES.O)
class OrderFragment : Fragment() {

    private var _binding: FragmentOrderBinding? = null
    private val binding get() = _binding!!
    private val items = MainActivity.orderItemList


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOrderBinding.inflate(inflater, container, false)

        binding.orderRecycleView.apply{
            adapter = OrderListAdapter(items, requireActivity() as AppCompatActivity)
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        binding.orderShareButton.setOnClickListener {
            var sendText = ""
            items.forEach {
                if(it.value != 0) sendText += it.toString() + "\n"
            }
            
            if(sendText.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "入力されていません", Toast.LENGTH_SHORT).show()
            }else{
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, sendText.substring(0,sendText.length-1))
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}