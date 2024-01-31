package com.saad.invitationmaker.app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.saad.invitationmaker.R
import com.saad.invitationmaker.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        /*
                val appBarConfiguration = AppBarConfiguration(
                    setOf(
                        R.id.mainFragment, R.id.backgroundFragment, R.id.myCreationFragment
                    )
                )
                setupActionBarWithNavController(navController, appBarConfiguration)*/

        binding.bottomNavigation.setupWithNavController(navController)
    }
}