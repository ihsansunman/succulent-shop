package school.cactus.succulentshop.api.signup

data class RegisterRequest(
    val email: String,
    val password: String,
    val username: String,
)