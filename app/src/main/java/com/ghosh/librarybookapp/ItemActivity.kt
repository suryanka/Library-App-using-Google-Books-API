package com.ghosh.librarybookapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.MovementMethod
import android.text.method.ScrollingMovementMethod
import android.widget.Toast
import com.ghosh.librarybookapp.databinding.ActivityItemBinding
import com.squareup.picasso.Picasso

class ItemActivity : AppCompatActivity() {
    lateinit var itemBinding: ActivityItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        itemBinding=ActivityItemBinding.inflate(layoutInflater)
        val view=itemBinding.root
        setContentView(view)

        var name: String? = intent.getStringExtra("name")
        var publication: String? = intent.getStringExtra("publication")
        var date: String? = intent.getStringExtra("date")
        var pages: String? = intent.getStringExtra("pages")
        var desc: String? = intent.getStringExtra("description")
        var img:String? = intent.getStringExtra("image")
        var previewLink:String?= intent.getStringExtra("previewLink")
        var buyLink:String?= intent.getStringExtra("buyLink")


        itemBinding.idItemName.text=name
        itemBinding.idItemPublication.text=publication
        itemBinding.idItemPages.text=pages
        itemBinding.idDate.text=date
        itemBinding.tvDesc.text=desc
        Picasso.get().load(img).into(itemBinding.idItemImageBook)

        itemBinding.tvDesc.movementMethod=ScrollingMovementMethod()
        itemBinding.tvDesc.isScrollbarFadingEnabled=false

        itemBinding.idButtonBuy.setOnClickListener{
            if(buyLink!!.isEmpty())
            {
                Toast.makeText(this@ItemActivity,"This book is not available for buying",Toast.LENGTH_LONG).show()
            }
            else
            {
                val uri:Uri=Uri.parse(buyLink)
                var intent=Intent(Intent.ACTION_VIEW,uri)
                startActivity(intent)
            }
        }
        itemBinding.idButtonPreview.setOnClickListener{
            if(!previewLink!!.isEmpty())
            {
                val uri:Uri=Uri.parse(previewLink)
                var intent=Intent(Intent.ACTION_VIEW,uri)
                startActivity(intent)
            }
            else {
                Toast.makeText(
                    this@ItemActivity,
                    "This book is not available for previewing",
                    Toast.LENGTH_LONG
                ).show()
            }
        }



    }
}