package com.example.githubuser

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val tvName: TextView = findViewById(R.id.tv_name)
        val tvUsername: TextView = findViewById(R.id.tv_username)
        val profilePic: CircleImageView = findViewById(R.id.profile_pic)

        val person = intent.getParcelableExtra<User>(EXTRA_USER) as User
        val name = person.name.toString()
        val username = person.username.toString()
        val image = person.image

        tvName.text = name
        tvUsername.text = username
        Glide.with(this)
            .load(image)
            .into(profilePic)
    }
}