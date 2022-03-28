package school.cactus.succulentshop.product

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T> : Resource<T>()
    sealed class Error<T> : Resource<T>() {
        class TokenExpired<T> : Error<T>()
        class Failure<T> : Error<T>()
        class UnexpectedError<T> : Error<T>()
    }
}