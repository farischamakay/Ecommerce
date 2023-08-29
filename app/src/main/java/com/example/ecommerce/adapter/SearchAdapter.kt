package com.example.ecommerce.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommerce.databinding.ItemListSearchBinding

class SearchAdapter(private var results: List<String?>, private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = results[position]
        item?.let { holder.bind(it) }

    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun updateData(newData: List<String?>) {
        results = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: ItemListSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.searchResult.text = item
            binding.imgNext.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}