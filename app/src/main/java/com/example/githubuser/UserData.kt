package com.example.githubuser

/**
 * Created by pengalatdite on 11/24/2020.
 */
object UserData {
    private val names = arrayOf(
        "Ahmad Dahlan",
        "Ahmad Yani",
        "Sutomo",
        "Gatot Soebroto",
        "Ki Hadjar Dewantarai",
        "Mohammad Hatta",
        "Soedirman",
        "Soekarno",
        "Soepomo",
        "Tan Malaka"
    )

    private val usernames = arrayOf(
        "Ahmad Dahlan",
        "Ahmad Yani",
        "Sutomo",
        "Gatot Soebroto",
        "Ki Hadjar Dewantarai",
        "Mohammad Hatta",
        "Soedirman",
        "Soekarno",
        "Soepomo",
        "Tan Malaka"
    )

    private val images = intArrayOf(
        R.drawable.ahmad_dahlan,
        R.drawable.ahmad_yani,
        R.drawable.bung_tomo,
        R.drawable.gatot_subroto,
        R.drawable.ki_hadjar_dewantara,
        R.drawable.mohammad_hatta,
        R.drawable.sudirman,
        R.drawable.sukarno,
        R.drawable.supomo,
        R.drawable.tan_malaka
    )

    val listData: ArrayList<User>
        get() {
            val list = arrayListOf<User>()
            for (position in names.indices) {
                val user = User(names[position], usernames[position], images[position].toString())
                list.add(user)
            }
            return list
        }

}