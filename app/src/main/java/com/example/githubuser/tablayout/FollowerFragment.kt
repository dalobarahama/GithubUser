package com.example.githubuser.tablayout

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.BuildConfig
import com.example.githubuser.ListUserAdapter
import com.example.githubuser.R
import com.example.githubuser.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowerFragment : Fragment() {

    private lateinit var recyclerview: RecyclerView
    private lateinit var progressbar: ProgressBar
    private lateinit var listUserAdapter: ListUserAdapter

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressbar = view.findViewById(R.id.progress_bar_follower)

        recyclerview = view.findViewById(R.id.progress_bar_follower)
        recyclerview.setHasFixedSize(true)

        val username = arguments?.getString(ARG_USERNAME)

        getUser(username)
    }

    private fun getUser(username: String?) {
        Log.d("Following Fragment", "getUser: called")
        progressbar.visibility = View.VISIBLE
        val url = "https://api.github.com/users/$username/followers"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token " + BuildConfig.github_api_key)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                Log.d("Following Fragment", "onSuccess: called")
                progressbar.visibility = View.INVISIBLE

                val listUser = ArrayList<User>()
                val result = String(responseBody)
                try {
                    val response = JSONArray(result)

                    for (i in 0 until response.length()) {
                        Log.d("Following Fragment", "jsonArray: called")
                        val item = response.getJSONObject(i)
                        val name = item.getString("login")
                        val username = item.getString("login")
                        val image = item.getString("avatar_url")
                        val user = User(name, username, image)
                        listUser.add(user)
                    }
                    showRecyclerList(listUser)

                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                progressbar.visibility = View.VISIBLE
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode: Bad Request"
                    403 -> "$statusCode: Forbidden"
                    404 -> "$statusCode: Not Found"
                    else -> "$statusCode: ${error?.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT)
            }
        })
    }

    private fun showRecyclerList(list: ArrayList<User>) {
        recyclerview.layoutManager = LinearLayoutManager(context)
        listUserAdapter = ListUserAdapter(list)
        recyclerview.adapter = listUserAdapter

        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {

            }
        })
    }

}