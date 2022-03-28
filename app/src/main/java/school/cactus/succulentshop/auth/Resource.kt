package school.cactus.succulentshop.auth

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    sealed class Error<T> : Resource<T>() {
        class Failure<T> : Error<T>()
        class UnexpectedError<T> : Error<T>()
        class ClientError<T>(errorMessage: String) : Resource<T>(message = errorMessage)
    }
}