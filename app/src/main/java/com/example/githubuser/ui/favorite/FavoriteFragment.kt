package com.example.githubuser.ui.favorite

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuser.R
import com.example.githubuser.data.db.GitHubUserDatabase
import com.example.githubuser.data.model.User
import com.example.githubuser.data.network.api.GitHubAPI
import com.example.githubuser.data.network.repository.UserRepository
import com.example.githubuser.databinding.FragmentFavoriteBinding
import com.example.githubuser.ui.adapter.UserAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.android.synthetic.main.fragment_favorite.view.*
import java.util.*

class FavoriteFragment : Fragment() {

    private lateinit var viewModel: FavoriteViewModel

    private lateinit var _binding: FragmentFavoriteBinding
    private val binding: View
        get() = _binding.root

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val githubAPi = GitHubAPI()
        val gitHubUserDatabase = GitHubUserDatabase.getDatabase(requireContext().applicationContext)
        val userRepository = UserRepository(githubAPi, gitHubUserDatabase)
        val viewModelFactory = FavoriteViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavoriteViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)
        _binding.lifecycleOwner = this
        _binding.favoriteViewModel = viewModel
        return binding

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllUsers().observe(viewLifecycleOwner, { users ->
            adapter =
                UserAdapter(users.toMutableList()) { user, userAvatar, userLogin ->
                    onItemClicked(user, userAvatar, userLogin)
                }
            setHasOptionsMenu(true)
            adapter.submitList(users)
            adapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            binding.rv_users.adapter = adapter
            binding.rv_users.layoutManager = GridLayoutManager(requireContext(), 3)
            showEmptyIndicator(users.isEmpty())

        })
    }

    private fun showEmptyIndicator(boolean: Boolean) {
        if (boolean) {
            tv_such_empty.visibility = View.VISIBLE
            tv_add_to_fav.visibility = View.VISIBLE
        } else {
            tv_such_empty.visibility = View.INVISIBLE
            tv_add_to_fav.visibility = View.INVISIBLE
        }
    }

    private fun onItemClicked(
        user: User, userAvatar: CircleImageView, userLogin: TextView,
    ) {
        val favoriteToDetail =
            FavoriteFragmentDirections.favoriteToDetailFavorite(
                user = user,
                userLogin = user.login
            )

        val extras = FragmentNavigatorExtras(
            (userAvatar to user.avatar_url),
            (userLogin to user.login)
        )
        binding.findNavController().navigate(favoriteToDetail, extras)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.main_menu, menu)

        menu.findItem(R.id.menu_favorite).isVisible = false
        menu.findItem(R.id.menu_settings).isVisible = false

        val searchView = menu.findItem(R.id.search_view).actionView as SearchView
        searchView.queryHint = requireContext().resources.getString(R.string.search_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                if (!newText.isNullOrEmpty()) {
                    newText.toString().toLowerCase(Locale.getDefault()).trim()
                }
                return true
            }
        })
    }
}