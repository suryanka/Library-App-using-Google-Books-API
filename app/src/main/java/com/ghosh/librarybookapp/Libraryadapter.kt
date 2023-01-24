package com.ghosh.librarybookapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ghosh.librarybookapp.databinding.LibraryRvItemBinding
import com.squareup.picasso.Picasso

class Libraryadapter(var context: Context, var libraryList: ArrayList<LibraryRvModel>):
RecyclerView.Adapter<Libraryadapter.libraryViewHolder>(){

    inner class libraryViewHolder(val adapterBinding: LibraryRvItemBinding):RecyclerView.ViewHolder(adapterBinding.root)
    {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): libraryViewHolder {
        val binding=LibraryRvItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return libraryViewHolder(binding)
    }
    override fun onBindViewHolder(holder: libraryViewHolder,  position: Int) {
        holder.adapterBinding.idName.text=libraryList[position].name
        holder.adapterBinding.idPublication.text=libraryList[position].publication
        holder.adapterBinding.idPages.text=libraryList[position].pages
        holder.adapterBinding.idDate.text=libraryList[position].date

        Picasso.get().load(libraryList[position].img).into(holder.adapterBinding.idImageBook)

        holder.adapterBinding.idCardView.setOnClickListener {

            val intent=Intent(context,ItemActivity::class.java)
            intent.putExtra("name",libraryList[position].name)
            intent.putExtra("publication",libraryList[position].publication)
            intent.putExtra("pages",libraryList[position].pages)
            intent.putExtra("date",libraryList[position].date)
            intent.putExtra("description",libraryList[position].desc)
            intent.putExtra("image",libraryList[position].img)
            intent.putExtra("previewLink",libraryList[position].previewLink)
            intent.putExtra("buyLink",libraryList[position].buyLink)
            context.startActivity(intent)
        }

    }



    override fun getItemCount(): Int {
        return libraryList.size
    }
}