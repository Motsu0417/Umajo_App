package app.motsu.hiromoto.umajo_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import app.motsu.hiromoto.umajo_app.databinding.FragmentWorkManageBinding

class WorkManageFragment : Fragment() {

    private var _binding: FragmentWorkManageBinding? = null
    private val binding get() = _binding!!
    private val workItems = MainActivity.workItemList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWorkManageBinding.inflate(inflater, container, false)

        binding.workRecyclerView.apply{
            adapter =WorkListAdapter(workItems, requireActivity() as AppCompatActivity)
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

    }
}