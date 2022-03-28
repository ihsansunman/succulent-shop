package school.cactus.succulentshop.product

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.db.db
import school.cactus.succulentshop.product.Resource.Error.*
import school.cactus.succulentshop.product.Resource.Success

class ProductRepository {
    suspend fun fetchProducts(): Flow<Resource<List<ProductItem>>> = flow {
        emit(Resource.Loading<List<ProductItem>>())
        val cachedProducts = db.productDao().getAll()
        if (cachedProducts.isNotEmpty()) {
            emit(Success(cachedProducts.toProductItemList()))
        }

        val response = try {
            emit(Resource.Loading<List<ProductItem>>())
            api.listAllProducts()
        } catch (ex: Exception) {
            null
        }

        when (response?.code()) {
            null -> emit(Failure())
            200 -> {
                val productItems = response.body()!!.toProductItemList()
                emit(Success(productItems))
                db.productDao().insertAll(productItems.toProductEntityList())
            }
            401 -> emit(TokenExpired())
            else -> emit(UnexpectedError())
        }
    }

    suspend fun fetchProductDetail(productId: Int): Flow<Resource<ProductItem>> = flow {
        emit(Resource.Loading<ProductItem>())
        val cachedProduct = db.productDao().getById(productId)

        if (cachedProduct == null) {
            val response = try {
                emit(Resource.Loading<ProductItem>())
                api.getProductById(productId)
            } catch (ex: Exception) {
                null
            }

            emit(
                when (response?.code()) {
                    null -> Failure()
                    200 -> Success(response.body()!!.toProductItem())
                    401 -> TokenExpired()
                    else -> UnexpectedError()
                }
            )
        } else {
            emit(Success(cachedProduct.toProductItem()))
        }
    }

    suspend fun getRelatedProductsById(productId: Int): Flow<Resource<List<ProductItem>>> = flow {
        val response = try {
            emit(Resource.Loading<List<ProductItem>>())
            api.getRelatedProductsById(productId)
        } catch (e: Exception) {
            null
        }

        when (response?.code()) {
            null -> emit(Failure())
            200 -> emit(Success(response.body()?.products!!.toProductItemList()))
            401 -> emit(TokenExpired())
            else -> emit(UnexpectedError())
        }
    }
}
