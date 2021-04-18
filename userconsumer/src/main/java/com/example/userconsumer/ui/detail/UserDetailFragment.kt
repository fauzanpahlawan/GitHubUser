package com.example.userconsumer.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.example.userconsumer.R
import com.example.userconsumer.data.datasource.UserDataSource
import com.example.userconsumer.data.network.api.GitHubAPI
import com.example.userconsumer.data.repository.UserRepository
import com.example.userconsumer.databinding.FragmentUserDetailBinding
import com.example.userconsumer.ui.adapter.FollowPagerAdapter
import com.example.userconsumer.ui.util.showSnackBar
import com.example.userconsumer.ui.util.simplified
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_user_detail.view.*

class UserDetailFragment : Fragment() {

    private lateinit var viewModelFactory: UserDetailViewModelFactory
    private lateinit var viewModel: UserDetailViewModel

    private lateinit var _binding: FragmentUserDetailBinding
    private val binding: View
        get() = _binding.root

    private val args: UserDetailFragmentArgs by navArgs()

    private lateinit var followPagerAdapter: FollowPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        val githubAPi = GitHubAPI()
        val userDataSource = UserDataSource(requireContext().applicationContext.contentResolver)
        val userRepository = UserRepository(githubAPi, userDataSource)
        viewModelFactory =
            UserDetailViewModelFactory(
                userRepository,
                args.user
            )
        viewModel = ViewModelProvider(this, viewModelFactory).get(UserDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_user_detail, container, false)
        _binding.lifecycleOwner = this
        _binding.userDetailViewModel = viewModel
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(viewLifecycleOwner, { user ->
            followPagerAdapter = FollowPagerAdapter(
                this,
                user.login
            )
            binding.follow_view_pager.adapter = followPagerAdapter
            TabLayoutMediator(binding.follow_tabs, binding.follow_view_pager) { tab, position ->
                when (position) {
                    0 -> tab.text =
                        requireContext().resources.getString(
                            R.string.tab_1,
                            user.followers.simplified()
                        )
                    1 -> tab.text =
                        requireContext().resources.getString(
                            R.string.tab_2,
                            user.following.simplified()
                        )
                }
            }.attach()
        })

        viewModel.isFavorite.observe(viewLifecycleOwner, { isFav ->
            binding.btn_fav.setOnClickListener {
                if (isFav) {
                    viewModel.removeFromFavorite(args.user)
                    binding.user_detail_fragment_layout.showSnackBar(
                        requireContext().getString(
                            R.string.remove_favorite,
                            args.user.login
                        )
                    )
                    binding.findNavController().navigate(R.id.detailToHome)
                }
            }
        })
    }
}