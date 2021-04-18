package com.example.userconsumer.ui.home

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.userconsumer.R
import com.example.userconsumer.data.datasource.UserDataSource
import com.example.userconsumer.data.model.User
import com.example.userconsumer.data.network.api.GitHubAPI
import com.example.userconsumer.data.repository.UserRepository
import com.example.userconsumer.databinding.FragmentHomeBinding
import com.example.userconsumer.ui.adapter.UserAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    private lateinit var _binding: FragmentHomeBinding
    private val binding: View
        get() = _binding.root

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val githubAPi = GitHubAPI()
        val userDataSource = UserDataSource(requireContext().applicationContext.contentResolver)
        val userRepository = UserRepository(githubAPi, userDataSource)
        val viewModelFactory = HomeViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        _binding.lifecycleOwner = this
        _binding.homeViewModel = viewModel
        return binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.users.observe(viewLifecycleOwner, { users ->
            adapter = UserAdapter(
                users.toMutableList(),
                { user, userAvatarUrl, userLogin ->
                    onItemClickListener(
                        user,
                        userAvatarUrl,
                        userLogin
                    )
                }, { isEmpty -> filteredEmptyResult(isEmpty) }
            )
            adapter.submitList(users)
            adapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            binding.rv_users.adapter = adapter
            binding.rv_users.layoutManager = GridLayoutManager(requireContext(), 3)
        })
    }

    private fun filteredEmptyResult(empty: Boolean) {
        binding.tv_filter_empty.visibility =
            if (empty) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
    }

    private fun onItemClickListener(user: User, userAvatar: CircleImageView, userLogin: TextView) {
        val homeToDetail =
            HomeFragmentDirections.homeToDetail(
                user = user,
                userLogin = user.login
            )
        val extras = FragmentNavigatorExtras(
            (userAvatar to user.avatar_url),
            (userLogin to user.login)
        )

        binding.findNavController().navigate(homeToDetail, extras)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchView = menu.findItem(R.id.search_view).actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
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

    override fun onResume() {
        super.onResume()
        viewModel.loadUsers()
    }
}
