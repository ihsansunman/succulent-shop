package school.cactus.succulentshop.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import school.cactus.succulentshop.product.ProductRepository

@Suppress("UNCHECKED_CAST")
class ProductListViewModelFactory(private val repository: ProductRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ProductListViewModel(repository) as T
}