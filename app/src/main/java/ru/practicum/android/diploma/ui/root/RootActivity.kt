package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private val binding: ActivityRootBinding by lazy { ActivityRootBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        // Пример использования access token для HeadHunter API
        networkRequestExample(accessToken = BuildConfig.API_ACCESS_TOKEN)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.search_fragment -> {
                    changeBottomNavigationVisibility(true)
                }

                R.id.favorites_fragment -> {
                    changeBottomNavigationVisibility(true)
                }

                R.id.development_team_fragment -> {
                    changeBottomNavigationVisibility(true)
                }

                else -> {
                    changeBottomNavigationVisibility(false)
                }
            }
        }
    }

    private fun networkRequestExample(accessToken: String) {
        // ...

    }

    private fun changeBottomNavigationVisibility(isVisible: Boolean) {
        binding.bottomNavigationView.isVisible = isVisible
        binding.separator.isVisible = isVisible
    }
}
