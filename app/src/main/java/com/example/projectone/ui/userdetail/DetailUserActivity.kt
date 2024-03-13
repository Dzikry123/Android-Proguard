package com.example.projectone.ui.userdetail

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import coil.load
import coil.transform.CircleCropTransformation
import com.example.project_one.R
import com.example.project_one.databinding.ActivityDetailUserBinding
import com.example.core.domain.model.GithubDetailUser
import com.example.core.domain.model.GithubUser
import com.example.core.ui.detailUser.provider.DetailUserActivityCallback
import com.example.projectone.ui.userdetail.fragment.UserFollower
import com.example.core.utils.ResultViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailUserActivity : AppCompatActivity(),DetailUserActivityCallback {
    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailUserViewModel>()

    private var isFavorite = false
    private var username: String = ""

    private lateinit var broadcastReceiver: BroadcastReceiver
    private lateinit var tvPowerStatus: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val item = intent.getParcelableExtra<GithubUser.Item>("userItems")
         username = item?.login ?: "User Not Found"

        Log.d("Intent berhasil", "Data berhasil diterima: $item")

        viewModel.getUser(username)

        viewModel.resultDetailUser.observe(this) { result ->
            when (result) {
                is ResultViewModel.Success<*> -> {
                    val data = result.data as GithubDetailUser
                    val user = data
                    binding.imageUser.load(user.avatar_url) {
                        transformations(CircleCropTransformation())
                    }
                    binding.fullNames.text = user.name
                    binding.username.text = user.login
                    binding.followerCount.text = user.followers.toString()
                    binding.followingCount.text = user.following.toString()
                    binding.repositoryCount.text = user.public_repos.toString()

                    Log.d("Detail User", "Detail User bisa di GET: $item")
                }

                is ResultViewModel.Error -> {
                    Toast.makeText(this, result.exception.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("Intent Error", "Data gagal di GET")

                }
                is ResultViewModel.Loading -> {
                    binding.progressBar.isVisible = result.isLoading
                    Log.d("Loading Detail", "${result.isLoading}")
                }
                else -> {}
            }
        }

        viewModel.getUser(username)
        val fragments = mutableListOf<Fragment>(
            UserFollower.newInstance(UserFollower.FOLLOWERS),
            UserFollower.newInstance(UserFollower.FOLLOWING)
        )
        val titleTabFragments = mutableListOf(
            getString(R.string.follower_tab), getString(R.string.following_tab)
        )
        val adapter = DetailUserAdapter(this, fragments)
        binding.viewPager.adapter = adapter


        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titleTabFragments[position]
        }.attach()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    viewModel.getFollowerUser(username)
                } else {
                    viewModel.getFollowingUser(username)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })


        viewModel.getFollowerUser(username)

        viewModel.resultFavoriteAdd.observe(this) {
            updateFavoriteButtonColor(true)
        }
        viewModel.resultFavoriteDelete.observe(this) {
            updateFavoriteButtonColor(false)
        }

        viewModel.findById(item?.id ?: 0) {
            viewModel.viewModelScope.launch {
                updateFavoriteButtonColor(true )
            }
        }

        binding.favoriteButton.setOnClickListener {
            val titleAdd = getString(R.string.notif_title_add)
            val titleDelete = getString(R.string.notif_title_delete)
            val addMessage = getString(R.string.notif_add_message)
            val deleteMessage = getString(R.string.notif_delete_message)
            val userId = item
            if (userId != null) {
                viewModel.viewModelScope.launch {
                    if (viewModel.isFavorite.value == true) {
                        Log.d("DetailUserActivity", "isFavorite: $isFavorite")
                        viewModel.delete(userId)
//                        viewModel.setFavorite(isFavorite)
                        updateFavoriteButtonColor(isFavorite)
                        sendNotif(titleDelete, deleteMessage)
                        Log.d("DetailUserActivity", "User dengan ID $userId dihapus dari favorit.")
                    } else {
                        viewModel.insert(userId)
//                        viewModel.setFavorite(!isFavorite)
                        updateFavoriteButtonColor(!isFavorite)
                        Log.d("DetailUserActivity", "User dengan ID $userId ditambah ke favorit.")
                        sendNotif(titleAdd, addMessage)
                    }
                }
            }

        }

        // Notifications Message


        if (Build.VERSION.SDK_INT >= 33) {
            permitReqNotif.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // leak canary
        tvPowerStatus = findViewById(R.id.tv_power_status)

    }


    // Notification Utility
    private fun sendNotif(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.fav_pink)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSubText(getString(R.string.notif_subtittle))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            builder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private val permitReqNotif =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "github_channel_01"
        private const val CHANNEL_NAME = "github list user"
    }

    private fun updateFavoriteButtonColor(isFavorite: Boolean) {
        val colorRes = if (isFavorite) R.color.colorFav else android.R.color.white
        val color = ContextCompat.getColor(this, colorRes)
        binding.favoriteButton.setColorFilter(color)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onTabSelected(position: Int) {
        if (position == 0) {
            viewModel.getFollowerUser(username)
        } else {
            viewModel.getFollowingUser(username)
        }
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

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastReceiver)
    }


}


