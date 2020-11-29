package com.example.githubuser

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setUserAccount()
        setUpSectionPagerAdapter()
    }

    fun setUserAccount() {
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

    fun setUpSectionPagerAdapter() {
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

    }
}