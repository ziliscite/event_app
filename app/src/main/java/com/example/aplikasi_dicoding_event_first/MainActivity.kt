package com.example.aplikasi_dicoding_event_first

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.aplikasi_dicoding_event_first.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_upcoming,
                R.id.navigation_finished,
                R.id.navigation_favorite,
                R.id.navigation_setting
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.detailedFragment) {
                // Slide the bottom nav out to the left with the fragment transition
                // (matching the rest of the view's navigation)
                navView.animate()
                    .translationX(-navView.width.toFloat())  // Moves it to the left
                    .setDuration(75) // Can't use the duration in the integer package, so hard coded it is
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .withEndAction {
                        navView.visibility = View.GONE  // Set it to gone after the animation
                    }
                    .start()
            } else {
                // Bring the bottom nav back into view when returning to a fragment
                navView.visibility = View.VISIBLE
                navView.animate()
                    .translationX(0f)
                    .setDuration(75)
                    .setInterpolator(AccelerateDecelerateInterpolator())
                    .start()
            }
        }
    }

    // Pop pop
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main).navigateUp() || super.onSupportNavigateUp()
    }
}
