package com.example.githubuser

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerview: RecyclerView
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview = findViewById(R.id.recyclerview)
        recyclerview.setHasFixedSize(true)

        shimmerFrameLayout = findViewById(R.id.shimmer_view_container)

        searchUser()
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
        val person = User(user.name, user.username, user.image)
        val goToDetail = Intent(this@MainActivity, DetailActivity::class.java)
        goToDetail.putExtra(DetailActivity.EXTRA_USER, person)
        startActivity(goToDetail)
    }

    private fun searchUser() {
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<SearchView>(R.id.search_view)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { getUser(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun getUser(username: String) {
        Log.d(TAG, "getUser: called")
        recyclerview.visibility = View.GONE
        shimmerFrameLayout.visibility = View.VISIBLE
        shimmerFrameLayout.startShimmer()
        val url = "https://api.github.com/search/users?q=$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token " + BuildConfig.github_api_key)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                Log.d(TAG, "onSuccess: called")
                recyclerview.visibility = View.VISIBLE
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmer()

                val listUser = ArrayList<User>()

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    for (i in 0 until items.length()) {
                        Log.d(TAG, "jsonArray: called")
                        val item = items.getJSONObject(i)
                        val name = item.getString("login")
                        val username = item.getString("login")
                        val image = item.getString("avatar_url")

                        val user = User(name, username, image)
                        listUser.add(user)

                    }
                    showRecyclerList(listUser)

                } catch (e: Exception) {
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                recyclerview.visibility = View.VISIBLE
                shimmerFrameLayout.visibility = View.GONE
                shimmerFrameLayout.stopShimmer()
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