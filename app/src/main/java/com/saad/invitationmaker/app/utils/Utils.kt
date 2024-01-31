package com.saad.invitationmaker.app.utils

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.saad.invitationmaker.features.home.models.GradientColor
import com.saad.invitationmaker.features.home.models.InvitationCtModel

object Utils {

    val invitationsList = listOf(
        InvitationCtModel(
            0,
            "Birthday Party",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNg6qV56a3aRCUxLdEKgw&usqp=CA",
            GradientColor(
                startColor = Color.parseColor("#d36bff"),
                endColor = Color.parseColor("#e7b6fc")
            )
        ),
        InvitationCtModel(
            1,
            "Wedding Ceremony",
            "https://encrypted-tbn0.gstatic.com/images?q=tUAri4xKAhq0BVDBSxLdEKgw&usqp=CAU",
            GradientColor(
                startColor = Color.parseColor("#02a879"),
                endColor = Color.parseColor("#23fcbf")
            )
        ),
        InvitationCtModel(
            2,
            "Graduation Celebration",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd56a3aRCUAri4xKAhq0BVDBSxLdEKgw&usqp=CAU",
            GradientColor(
                startColor = Color.parseColor("#011470"),
                endColor = Color.parseColor("#92a1e8")
            )
        ),
        InvitationCtModel(
            3,
            "Baby Shower",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:AxKAhq0BVDBSxLdEKgw&usqp=CAU",
            GradientColor(
                startColor = Color.parseColor("#e0a73d"),
                endColor = Color.parseColor("#a6977b")
            )
        ),
        InvitationCtModel(
            4,
            "Anniversary Dinner",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNg6KAhq0BVDBSxLdEKgw&usqp=CAU",
            GradientColor(
                startColor = Color.parseColor("#de3de0"),
                endColor = Color.parseColor("#fadb6b")
            )
        ),
        InvitationCtModel(
            5,
            "Gala",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANdUAri4xKAhq0BVDBSxLdEKgw&usqp=CAU",
            GradientColor(
                startColor = Color.parseColor("#f5560c"),
                endColor = Color.parseColor("#f59467")
            )
        ),
        InvitationCtModel(
            6,
            "Housewarming Party",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn6a3aRCUAri4xKAhq0BVDBSxLdEKgw&usqp=CAU",
            GradientColor(
                startColor = Color.parseColor("#995d42"),
                endColor = Color.parseColor("#9e7e6f")
            )
        ),
        InvitationCtModel(
            7,
            "Ramadan",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNg6qV56a3aRCUAri4LdEKgw&usqp=CAU",
            GradientColor(
                startColor = Color.parseColor("#00BCD4"),
                endColor = Color.parseColor("#4fa6d1")
            )
        ),
        InvitationCtModel(
            8,
            "Brunch",
            "https://encrypted-tbn0.gstatic.com/images?aRCUAri4xKAhq0BVDBSxLdEKgw&usqp=CAU",
            GradientColor(
                startColor = Color.parseColor("#f73e4d"),
                endColor = Color.parseColor("#fc8892")
            )
        ),
        InvitationCtModel(
            9,
            "Retirement Party",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9Ga3aRCUAri4xKAhq0BVDBSxLdEKgw&usqp=CAU",
            GradientColor(
                startColor = Color.parseColor("#571702"),
                endColor = Color.parseColor("#c45029")    // White
            )
        )
    )

    fun log(text: String) {
        Log.d("MyLogs", text)
    }

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}