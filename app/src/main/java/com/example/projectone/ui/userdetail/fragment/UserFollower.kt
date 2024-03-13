package com.example.projectone.ui.userdetail.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_one.databinding.FragmentUserFollowerBinding
import com.example.core.ui.main.UserAdapter
import com.example.core.domain.model.GithubUser
import com.example.projectone.ui.userdetail.DetailUserViewModel
import com.example.core.utils.ResultViewModel

class UserFollower : Fragment() {
    private var binding : FragmentUserFollowerBinding? = null
    private val adapter by lazy {
        UserAdapter{

        }
    }

    private val viewModel by activityViewModels<DetailUserViewModel>()
    var type = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserFollowerBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.userFollower?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@UserFollower.adapter
        }

        when (type) {
            FOLLOWERS -> {
                viewModel.resultFollowersUser.observe(viewLifecycleOwner) { state ->
                    if (state != null) {
                        manageFollowerUser(state)
                    }
                }
            }

            FOLLOWING -> {
                viewModel.resultFollowingUser.observe(viewLifecycleOwner) { state ->
                    if (state != null) {
                        manageFollowerUser(state)
                    }
                }
            }
        }

    }

    private fun manageFollowerUser(state: ResultViewModel) {
        when(state) {
            is ResultViewModel.Success<*> -> {
                adapter.setData(state.data as MutableList<GithubUser.Item>)
            }
            is ResultViewModel.Error -> {
                Toast.makeText(requireActivity(), state.exception.message.toString(), Toast.LENGTH_SHORT).show()
            }
            is ResultViewModel.Loading -> {
                binding?.progressBar?.isVisible = state.isLoading
            }
        }
    }

    companion object {
        const val FOLLOWERS = 100
        const val FOLLOWING = 101
        fun newInstance(type: Int) = UserFollower()
            .apply {
                this.type = type
            }
    }
}

