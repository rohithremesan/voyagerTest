package com.example.voyager.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.voyager.databinding.RvLayoutBinding
import com.example.voyager.model.Product

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductListViewHolder>() {
    private var productList :List<Product> = listOf()
    fun setProductList(list:List<Product>) {
        this.productList= list.toMutableList() as ArrayList<Product>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RvLayoutBinding.inflate(inflater, parent, false)
        return ProductListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductListViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(productList[position].imageUrl)
            .into(holder.binding.image)
        holder.binding.code.text="# "+productList[position].code
        holder.binding.name.text=productList[position].name
        holder.binding.description.text=productList[position].description
        holder.binding.price.text="₹ "+productList[position].price
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    class ProductListViewHolder(val binding: RvLayoutBinding): RecyclerView.ViewHolder(binding.root) {

    }

}