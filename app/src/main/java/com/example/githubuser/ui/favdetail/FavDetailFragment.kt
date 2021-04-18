package com.example.githubuser.ui.favdetail

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RemoteViews
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.githubuser.R
import com.example.githubuser.data.db.GitHubUserDatabase
import com.example.githubuser.data.network.api.GitHubAPI
import com.example.githubuser.data.network.repository.UserRepository
import com.example.githubuser.databinding.FavDetailFragmentBinding
import com.example.githubuser.ui.adapter.FollowPagerAdapter
import com.example.githubuser.ui.util.showSnackBar
import com.example.githubuser.ui.util.simplified
import com.example.githubuser.widget.FavUsersWidget
import com.example.githubuser.widget.FavWidgetService
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fav_detail_fragment.view.*
import kotlinx.android.synthetic.main.fragment_user_detail.view.follow_tabs
import kotlinx.android.synthetic.main.fragment_user_detail.view.follow_view_pager

class FavDetailFragment : Fragment() {

    private lateinit var viewModelFactory: FavDetailViewModelFactory
    private lateinit var viewModel: FavDetailViewModel

    private lateinit var _binding: FavDetailFragmentBinding
    private val binding: View
        get() = _binding.root

    private val args: FavDetailFragmentArgs by navArgs()

    private lateinit var followPagerAdapter: FollowPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)

        val githubAPi = GitHubAPI()
        val gitHubUserDatabase = GitHubUserDatabase.getDatabase(requireContext().applicationContext)
        val userRepository = UserRepository(githubAPi, gitHubUserDatabase)
        viewModelFactory =
            FavDetailViewModelFactory(userRepository, args.user)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fav_detail_fragment, container, false)
        _binding.lifecycleOwner = this
        _binding.favDetailViewModel = viewModel
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

        viewModel.insertedUser.observe(viewLifecycleOwner, { inserted ->
            if (inserted) {
                binding.fav_detail_fragment_layout.showSnackBar(
                    requireContext().getString(
                        R.string.add_favorite,
                        args.userLogin
                    )
                )
            } else {
                binding.fav_detail_fragment_layout.showSnackBar(
                    requireContext().getString(
                        R.string.remove_favorite,
                        args.userLogin
                    )
                )
            }
        })

        viewModel.fUser.observe(viewLifecycleOwner, { user ->
            if (user != null) {
                viewModel.setIsFavorite(true)
            } else {
                viewModel.setIsFavorite(false)
            }
            updateWidget(requireContext().applicationContext)
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.isUserFav()
    }

    private fun updateWidget(mContext: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(mContext)
        val ids = ComponentName(mContext.applicationContext, FavUsersWidget::class.java)

        val serviceIntent = Intent(mContext.applicationContext, FavWidgetService::class.java)
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ids)
        serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

        val views = RemoteViews(mContext.packageName, R.layout.fav_users_widget)
        views.setRemoteAdapter(R.id.stack_view_widget, serviceIntent)
        views.setEmptyView(R.id.stack_view_widget, R.id.tv_empty_view_widget)

        appWidgetManager.updateAppWidget(ids, views)
    }
}