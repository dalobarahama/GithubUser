package com.example.githubuser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerview: RecyclerView
    private var list: ArrayList<User> = arrayListOf()
    private lateinit var progressbar: ProgressBar

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressbar = findViewById(R.id.progress_bar)

        recyclerview = findViewById(R.id.recyclerview)
        recyclerview.setHasFixedSize(true)

        getUser()
//        list.addAll(UserData.listData)

    }

    private fun showRecyclerList(list: ArrayList<User>) {
        recyclerview.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        recyclerview.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(user: User) {
        Toast.makeText(this, "Kamu memilih " + user.name, Toast.LENGTH_SHORT).show()

        val person = User(user.name, user.username, user.image)
        val goToDetail = Intent(this@MainActivity, DetailActivity::class.java)
        goToDetail.putExtra(DetailActivity.EXTRA_USER, person)
        startActivity(goToDetail)
    }

    private fun getUser() {
        Log.d(TAG, "getUser: called")
        progressbar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                Log.d(TAG, "onSuccess: called")
                progressbar.visibility = View.INVISIBLE

                val listUser = ArrayList<User>()

                val result = String(responseBody!!)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    for (i in 0 until jsonArray.length()) {
                        Log.d(TAG, "jsonArray: called")
                        val jsonObject = jsonArray.getJSONObject(i)
                        val name = jsonObject.getString("login")
                        val username = jsonObject.getString("organizations_url")
                        val image = jsonObject.getString("avatar_url")
                        val user = User(name, username, image.toInt())
                        listUser.add(user)

                        Toast.makeText(this@MainActivity, name, Toast.LENGTH_SHORT).show()
                    }
                    showRecyclerList(listUser)

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                progressbar.visibility = View.INVISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Forbidden"
                    404 -> "$statusCode: Not Found"
                    else -> "$statusCode: ${error?.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT)
            }
        })
    }

}