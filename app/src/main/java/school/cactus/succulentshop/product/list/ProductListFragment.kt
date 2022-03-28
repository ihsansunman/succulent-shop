package school.cactus.succulentshop.product.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.databinding.FragmentProductListBinding
import school.cactus.succulentshop.infra.BaseFragment
import school.cactus.succulentshop.product.ProductRepository

class ProductListFragment : BaseFragment() {
    private var _binding: FragmentProductListBinding? = null

    private val binding get() = _binding!!

    override val viewModel: ProductListViewModel by viewModels {
        ProductListViewModelFactory(ProductRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().title = getString(R.string.app_name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_option, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemMenuOptionLogout -> {
                logOut()
            }
        }
        return true
    }

    private fun logOut() {
        JwtStore(requireContext()).deleteJwt()
        viewModel.navigateToLogin()
    }
}