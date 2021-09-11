package com.example.mycreditscore.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycreditscore.databinding.FragmentCreditReportBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RecyclerViewAdapter(
    private val report: Map<String, Any?>
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentCreditReportBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val key = report.keys.elementAt(position)
        holder.creditItemLabel.hint = key
        holder.creditItemValue.setText(report[key]?.toString())
    }

    override fun getItemCount(): Int = report.size

    inner class ViewHolder(binding: FragmentCreditReportBinding) : RecyclerView.ViewHolder(binding.root) {
        val creditItemLabel: TextInputLayout = binding.creditScoreLabel
        val creditItemValue: TextInputEditText = binding.creditScoreValue
    }
}