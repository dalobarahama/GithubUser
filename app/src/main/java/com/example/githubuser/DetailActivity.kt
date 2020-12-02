package com.example.githubuser

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import de.hdodenhof.circleimageview.CircleImageView

class DetailActivity : AppCompatActivity() {

    lateinit var name: String

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        setUserAccount()
    }

    private fun setUserAccount() {
        val tvName: TextView = findViewById(R.id.tv_name)
        val tvUsername: TextView = findViewById(R.id.tv_username)
        val profilePic: CircleImageView = findViewById(R.id.profile_pic)

        val person = intent.getParcelableExtra<User>(EXTRA_USER) as User

        tvName.text = person.name.toString()
        tvUsername.text = person.username.toString()
        Glide.with(this)
            .load(person.image)
            .into(profilePic)

        setUpSectionPagerAdapter(person)
    }

    private fun setUpSectionPagerAdapter(user: User) {
        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        val sectionsPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = user.username
        viewPager.adapter = sectionsPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

    }
}