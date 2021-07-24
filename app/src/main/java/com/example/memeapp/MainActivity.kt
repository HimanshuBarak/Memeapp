package com.example.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Intents.Insert.ACTION
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentimageurl:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getmeme()
    }

    private fun getmeme(){
        //by default the progress bar should be visible
        progressbar.visibility = View.VISIBLE
        textview.visibility = View.GONE
        //instaniate the request queue
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        //defining the request
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                 currentimageurl = response.getString("url")
                //loading the image in the url in our imageview
                Glide.with(this).load(currentimageurl).listener(object:RequestListener <Drawable>{
                    //if image fails to load
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbar.visibility = View.GONE
                        textview.visibility = View.VISIBLE
                        return false
                    }
                    //when image loads
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressbar.visibility = View.GONE
                        return false
                    }
                }).into(memeimage);
            },
            { error ->
                // TODO: Handle error
            }

        )
        //adding the request to the queue for processing
       Mysingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun nextmeme(view: View) {

        getmeme()
    }
    fun sharememe(view: View) {
        //creting an intent to share the meme
       val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Checkout this meme I got from reddit $currentimageurl")

        val chooser = Intent.createChooser(intent,"Share using..")
        startActivity(intent)

    }
}