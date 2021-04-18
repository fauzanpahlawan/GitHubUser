package com.example.userconsumer.ui.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userconsumer.R
import com.example.userconsumer.data.datasource.UserDataSource
import com.example.userconsumer.data.network.api.GitHubAPI
import com.example.userconsumer.data.repository.UserRepository
import com.example.userconsumer.databinding.FragmentFollowersBinding
import com.example.userconsumer.helper.ResourcesHelper
import com.example.userconsumer.ui.adapter.FollowAdapter
import com.example.userconsumer.ui.util.showSnackBar
import kotlinx.android.synthetic.main.fragment_followers.view.*

class FollowFragment : Fragment() {

    companion object {
        const val ARG_INDEX = "section_number"
        const val ARG_USER_LOGIN = "userLogin"

        fun newInstance(position: Int, userLogin: String): FollowFragment {
            val fragment = FollowFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_INDEX, position)
            bundle.putString(ARG_USER_LOGIN, userLogin)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: FollowViewModel

    private lateinit var _binding: FragmentFollowersBinding
    private val binding: View
        get() = _binding.root

    private lateinit var followerAdapter: FollowAdapter
    private lateinit var followingAdapter: FollowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var userLogin: String? = null

        if (arguments != null) {
            userLogin = arguments?.getString(ARG_USER_LOGIN) as String
        }

        val githubAPi = GitHubAPI()
        val userDataSource = UserDataSource(requireContext().applicationContext.contentResolver)
        val userRepository = UserRepository(githubAPi, userDataSource)
        val resourcesHelper = ResourcesHelper(requireContext())
        val viewModelFactory =
            FollowViewModelFactory(userRepository, userLogin.toString(), resourcesHelper)
        viewModel =
            ViewModelProvider(
                parentFragment?.viewModelStore as ViewModelStore,
                viewModelFactory
            ).get(FollowViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_followers, container, false)
        _binding.lifecycleOwner = this
        _binding.followViewModel = viewModel
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            when (arguments?.getInt(ARG_INDEX, 0) as Int) {
                0 -> {
                    viewModel.followers.observe(
                        parentFragment?.viewLifecycleOwner as LifecycleOwner,
                        { followers ->
                            followerAdapter = FollowAdapter(followers.toMutableList())
                            followerAdapter.stateRestorationPolicy =
                                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                            binding.rv_followers.adapter = followerAdapter
                            binding.rv_followers.layoutManager =
                                GridLayoutManager(requireContext(), 3)
                        })
                    viewModel.errorMessage.observe(
                        parentFragment?.viewLifecycleOwner as LifecycleOwner, { message ->
                            binding.fragment_followers_layout.showSnackBar(message)
                        })
                }
                1 -> {
                    viewModel.following.observe(
                        parentFragment?.viewLifecycleOwner as LifecycleOwner,
                        { following ->
                            followingAdapter = FollowAdapter(following.toMutableList())
                            followingAdapter.stateRestorationPolicy =
                                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                            binding.rv_followers.adapter = followingAdapter
                            binding.rv_followers.layoutManager =
                                GridLayoutManager(requireContext(), 3)
                        })
                    viewModel.errorMessage.observe(
                        parentFragment?.viewLifecycleOwner as LifecycleOwner, { message ->
                            binding.fragment_followers_layout.showSnackBar(message)
                        })
                }
            }
        }
    }
}