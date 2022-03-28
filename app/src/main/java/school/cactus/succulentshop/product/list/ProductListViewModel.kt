package school.cactus.succulentshop.product.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import school.cactus.succulentshop.R
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.product.ProductItem
import school.cactus.succulentshop.product.ProductRepository
import school.cactus.succulentshop.product.Resource

class ProductListViewModel(private val repository: ProductRepository) : BaseViewModel() {
    private val _products = MutableLiveData<List<ProductItem>>()

    val products: LiveData<List<ProductItem>> = _products

    val itemClickListener: (ProductItem) -> Unit = {
        val action = ProductListFragmentDirections.openProductDetail(it.id)
        navigation.navigate(action)
    }

    val adapter = ProductAdapter()
    val decoration = ProductDecoration()

    init {
        fetchProducts()
    }

    private fun fetchProducts() = viewModelScope.launch {
        repository.fetchProducts().collect {
            when (it) {
                is Resource.Success -> onSuccess(it.data!!)
                is Resource.Error.TokenExpired -> onTokenExpired()
                is Resource.Error.UnexpectedError -> onUnexpectedError()
                is Resource.Error.Failure -> onFailure()
                is Resource.Loading -> _isLoading.value = true
            }
        }
    }

    private fun onSuccess(products: List<ProductItem>) {
        _isLoading.value = false
        _products.postValue(products)
    }

    private fun onTokenExpired() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.your_session_is_expired,
            duration = Snackbar.LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.log_in,
                action = {
                    navigateToLogin()
                }
            )
        )
    }

    private fun onUnexpectedError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.unexpected_error_occurred,
            duration = Snackbar.LENGTH_LONG,
        )
    }

    private fun onFailure() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.check_your_connection,
            duration = Snackbar.LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.retry,
                action = {
                    fetchProducts()
                }
            )
        )
    }

    fun navigateToLogin() {
        val directions = ProductListFragmentDirections.tokenExpired()
        navigation.navigate(directions)
    }
}