package school.cactus.succulentshop.product.detail

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
import school.cactus.succulentshop.product.Resource.Error.*
import school.cactus.succulentshop.product.Resource.Loading
import school.cactus.succulentshop.product.Resource.Success
import school.cactus.succulentshop.product.list.ProductListFragmentDirections

class ProductDetailViewModel(
    private val productId: Int,
    private val repository: ProductRepository
) : BaseViewModel() {

    private val _product = MutableLiveData<ProductItem>()
    val product: LiveData<ProductItem> = _product

    private val _relatedProducts = MutableLiveData<List<ProductItem>>()
    val relatedProducts: LiveData<List<ProductItem>> = _relatedProducts

    val decoration = RelatedProductDecoration()
    val adapter = RelatedProductAdapter()
    val itemClickListener: (ProductItem) -> Unit = {
        val directions = ProductDetailFragmentDirections.actionProductDetailFragmentSelf(it.id)
        navigation.navigate(directions)
    }

    init {
        fetchProduct()
        fetchRelatedProductsById()
    }

    private fun fetchProduct() = viewModelScope.launch {
        repository.fetchProductDetail(productId).collect {
            when (it) {
                is Success -> onSuccess(it.data!!)
                is TokenExpired -> onTokenExpired()
                is UnexpectedError -> onUnexpectedError()
                is Failure -> onFailure()
                is Loading -> _isLoading.value = true
            }
        }
    }

    private fun fetchRelatedProductsById() = viewModelScope.launch {
        repository.getRelatedProductsById(productId).collect {
            when (it) {
                is Success -> onSuccess(it.data!!)
                is TokenExpired -> onTokenExpired()
                is UnexpectedError -> onUnexpectedError()
                is Failure -> onFailure()
                is Loading -> _isLoading.value = true
            }
        }
    }

    private fun onSuccess(product: ProductItem) {
        _product.value = product
    }

    private fun onSuccess(products: List<ProductItem>) {
        _isLoading.value = false
        _relatedProducts.value = products
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
                    fetchProduct()
                    fetchRelatedProductsById()
                }
            )
        )
    }

    private fun navigateToLogin() {
        val directions = ProductListFragmentDirections.tokenExpired()
        navigation.navigate(directions)
    }
}