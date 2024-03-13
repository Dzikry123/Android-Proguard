package com.example.projectone.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_one.R
import com.example.project_one.databinding.ActivityMainBinding
import com.example.projectone.ui.about.AboutMe
import com.example.core.domain.model.GithubUser
import com.example.core.ui.main.UserAdapter
import com.example.projectone.ui.userdetail.DetailUserActivity
import com.example.core.utils.ResultViewModel
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var tvPowerStatus: TextView


    private val adapter by lazy {
        UserAdapter {
            Intent(this@MainActivity, DetailUserActivity::class.java).apply {
                putExtra("userItems", it)
                startActivity(this)
            }

            Log.d("Intent to Detail", "Data berhasil dikirim: $it")
        }
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getSearchUser(query.toString())
                if (query.isNullOrBlank()) {
                    viewModel.getUser()
                    return true
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewModel.getUser()
                    return true
                }
                return false
            }
        })

        viewModel.resultUser.observe(this) {
            when (it) {
                is ResultViewModel.Success<*> -> {
                    adapter.setData(it.data as MutableList<GithubUser.Item>)
                }

                is ResultViewModel.Error -> {
                    Toast.makeText(this, it.exception.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is ResultViewModel.Loading -> {
                    binding.progressBar.isVisible = it.isLoading
                }
            }
        }

        viewModel.getUser()

        drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.open_nav,
            R.string.close_nav
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // leak canary
        tvPowerStatus = findViewById(R.id.tv_power_status)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_about_me -> {
                val intent = Intent(this, AboutMe::class.java)
                startActivity(intent)
            }

            R.id.nav_fav -> {
                installFavoriteModule()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun installFavoriteModule() {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val moduleFavorite = "favorite"
        if (splitInstallManager.installedModules.contains(moduleFavorite)) {
            moveToFavoriteActivity()
            Toast.makeText(this, "Open module", Toast.LENGTH_SHORT).show()
        } else {
            val request = SplitInstallRequest.newBuilder()
                .addModule(moduleFavorite)
                .build()
            splitInstallManager.startInstall(request)
                .addOnSuccessListener {
                    Toast.makeText(this, "Success installing module", Toast.LENGTH_SHORT).show()
                    moveToFavoriteActivity()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error installing module", Toast.LENGTH_SHORT).show()
                    Log.e("error pindah", "gagal" )
                }
        }
    }

    private fun moveToFavoriteActivity() {
        val uri = Uri.parse("favoriteapp://navigate/favorite")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    // leak canary
    private fun registerBroadCastReceiver() {
        broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    Intent.ACTION_POWER_CONNECTED -> {
                        tvPowerStatus.text = getString(R.string.power_connected)
                    }
                    Intent.ACTION_POWER_DISCONNECTED -> {
                        tvPowerStatus.text = getString(R.string.power_disconnected)
                    }
                }
            }
        }
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(broadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        registerBroadCastReceiver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUser()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }

}



