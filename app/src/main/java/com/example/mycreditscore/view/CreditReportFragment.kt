package com.example.mycreditscore.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycreditscore.databinding.FragmentCreditScoreRecyclerViewBinding
import com.example.mycreditscore.view.adapter.RecyclerViewAdapter

class CreditReportFragment : Fragment() {

    private var creditScoreRecyclerViewBinding: FragmentCreditScoreRecyclerViewBinding? = null
    private var creditScoreResponse: Map<String, Any?>? = null
    private var adapter: RecyclerViewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        creditScoreRecyclerViewBinding = FragmentCreditScoreRecyclerViewBinding.inflate(inflater, container, false)
        val root = creditScoreRecyclerViewBinding?.root

        creditScoreResponse = arguments?.get("creditData") as Map<String, Any?>?
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        creditScoreResponse?.let {
            adapter = RecyclerViewAdapter(it)
            LinearLayoutManager(requireContext())
            creditScoreRecyclerViewBinding?.creditReportRecyclerView?.adapter = adapter
        }
    }
}