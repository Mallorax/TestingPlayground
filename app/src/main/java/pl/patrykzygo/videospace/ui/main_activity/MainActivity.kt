package pl.patrykzygo.videospace.ui.main_activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.patrykzygo.videospace.R
import pl.patrykzygo.videospace.databinding.ActivityMainBinding
import pl.patrykzygo.videospace.factories.DefaultFragmentFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //TODO: A lot of logic here that should be in viewmodel ideally
    // It's just temporary to mess around with few things without VM

    @Inject
    lateinit var fragmentFactory: DefaultFragmentFactory

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfig: AppBarConfiguration
    private val viewModel: MainActivityViewModel by viewModels()
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.drawerNavView.menu.findItem(R.id.login).setOnMenuItemClickListener {
            viewModel.launchAuthEvent()
            true
        }
        setupNavigation()
        subscribeObservers()
        setContentView(binding.root)
    }

    private fun handleIntent() {
        val uri = intent.data
        uri?.let {
            val path = uri.toString()
            Toast.makeText(this, "Path: $path", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStart() {
        super.onStart()
        performLoginCheck()
    }


    private fun setupNavigation() {
        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.user_lists_fragment,
                R.id.search_fragment
            ),
            binding.mainActivityRoot
        )
        binding.drawerNavView.setupWithNavController(navController)
        binding.appBarLayout.toolbar.setupWithNavController(navController, appBarConfig)
    }


    private fun performLoginCheck() {
        val sessionId = readSessionId()
        if (sessionId != null) {
            Snackbar.make(
                binding.root,
                "User logged in with session: $sessionId",
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            Snackbar.make(binding.root, "User logged out", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun subscribeObservers() {
        viewModel.authEvent.observe(this) {
            val tabsBuilder = CustomTabsIntent.Builder()
            val tabsIntent = tabsBuilder.build()
            tabsIntent.launchUrl(this, Uri.parse(it))
        }
    }

    private fun readSessionId(): String? {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return null
        val result = sharedPref.getString("sessionId", null)
        return result
    }

    private fun saveSessionId(sessionId: String) {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("sessionId", sessionId)
            apply()
            Snackbar.make(binding.root, "Session saved", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
}