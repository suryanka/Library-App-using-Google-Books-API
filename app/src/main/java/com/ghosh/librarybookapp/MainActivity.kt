package com.ghosh.librarybookapp

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.ghosh.librarybookapp.databinding.ActivityMainBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    var bookList= ArrayList<LibraryRvModel>()
    private lateinit var libraryadapter: Libraryadapter
    var permissionCode:Int=1
    lateinit var srcText:String

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        val view=mainBinding.root
        setContentView(view)

        libraryadapter= Libraryadapter(this@MainActivity,bookList)
        mainBinding.recyclerView.adapter=libraryadapter

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.INTERNET)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.INTERNET),permissionCode)
        }

        mainBinding.idSearchButton.setOnClickListener {
            srcText=mainBinding.idEditText.text.toString()
            bookList.clear()
            libraryadapter.notifyDataSetChanged()
            if(srcText!="")
            {
                mainBinding.recyclerView.visibility=View.GONE
                mainBinding.progressBar.visibility=View.VISIBLE
                Handler().postDelayed(object:Runnable{
                    override fun run() {
                        loadRecycler(srcText)
                    }

                },2000)


            }
//            else{
//                bookList.clear()
//                libraryadapter.notifyDataSetChanged()
//            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==permissionCode && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(this@MainActivity,"Permissions Granted", Toast.LENGTH_LONG).show()
        }
        else
        {
            Toast.makeText(this@MainActivity,"Please grant the permissions.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadRecycler(text: String) {
        var url:String="https://www.googleapis.com/books/v1/volumes?q="+text
        val requestQueue:RequestQueue=Volley.newRequestQueue(this@MainActivity)

        val jsonObjectRequest:JsonObjectRequest= JsonObjectRequest(Request.Method.GET, url,null,{response->

            bookList.clear()
            try{
                var booksArray:JSONArray=response.getJSONArray("items")
                for(i in 0..booksArray.length())
                {
                    val bookObj:JSONObject=booksArray.getJSONObject(i)
                    var name:String=bookObj.getJSONObject("volumeInfo").getString("title").toString()
                    var publication:String=bookObj.getJSONObject("volumeInfo").getString("publisher").toString()
                    var date:String=bookObj.getJSONObject("volumeInfo").getString("publishedDate").toString()
                    var pages:String=bookObj.getJSONObject("volumeInfo").getInt("pageCount").toString()
                    var img:String=bookObj.getJSONObject("volumeInfo").getJSONObject("imageLinks")
                        .getString("smallThumbnail").toString()
                    var desc:String=bookObj.getJSONObject("volumeInfo").getString("description").toString()
                    var previewLink:String=bookObj.optJSONObject("volumeInfo").optString("previewLink").toString()
                    var buyLink:String=bookObj.optJSONObject("saleInfo")?.optString("buyLink").toString()

                    bookList.add(LibraryRvModel(img,name,publication,pages,date,desc,previewLink,buyLink))
                }
                libraryadapter.notifyDataSetChanged()
            }
            catch(e:JSONException){
                e.printStackTrace()
            }
        },
        {
            Toast.makeText(this@MainActivity,"Please enter valid search content.",Toast.LENGTH_LONG).show()
        })

        requestQueue.add(jsonObjectRequest)
        mainBinding.progressBar.visibility=View.GONE
        mainBinding.recyclerView.visibility=View.VISIBLE
    }
}