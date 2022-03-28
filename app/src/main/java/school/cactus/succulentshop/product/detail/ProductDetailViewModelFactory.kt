package school.cactus.succulentshop.product.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.product.ProductRepository

@Suppress("UNCHECKED_CAST")
class ProductDetailViewModelFactory(
    private val productId: Int,
    private val repository: ProductRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        ProductDetailViewModel(productId, repository) as T
}