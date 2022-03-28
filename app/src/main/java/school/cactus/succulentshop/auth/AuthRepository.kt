package school.cactus.succulentshop.auth

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import school.cactus.succulentshop.api.GenericErrorResponse
import school.cactus.succulentshop.api.api
import school.cactus.succulentshop.api.login.LoginRequest
import school.cactus.succulentshop.api.login.LoginResponse
import school.cactus.succulentshop.api.signup.RegisterRequest
import school.cactus.succulentshop.api.signup.RegisterResponse

class AuthRepository {
    suspend fun sendLoginRequest(
        identifier: String,
        password: String
    ): Resource<LoginResponse> {
        val request = LoginRequest(identifier, password)

        val response = try {
            api.login(request)
        } catch (ex: Exception) {
            null
        }

        return when (response?.code()) {
            null -> Resource.Error.Failure<LoginResponse>()
            200 -> Resource.Success(LoginResponse(response.body()!!.jwt))
            in 400..499 -> Resource.Error.ClientError<LoginResponse>(
                response.errorBody()!!.errorMessage()
            )
            else -> Resource.Error.UnexpectedError()
        }
    }

    suspend fun sendSignupRequest(
        email: String,
        password: String,
        userName: String,
    ): Resource<RegisterResponse> {
        val request = RegisterRequest(email, password, userName)

        val response = try {
            api.signup(request)
        } catch (e: Exception) {
            null
        }

        return when (response?.code()) {
            null -> Resource.Error.Failure<RegisterResponse>()
            200 -> Resource.Success(RegisterResponse(response.body()!!.jwt))
            in 400..499 -> Resource.Error.ClientError<RegisterResponse>(
                response.errorBody()!!.errorMessage()
            )
            else -> Resource.Error.UnexpectedError()
        }
    }

    private fun ResponseBody.errorMessage(): String {
        val errorBody = string()
        val gson: Gson = GsonBuilder().create()
        val loginErrorResponse = gson.fromJson(errorBody, GenericErrorResponse::class.java)
        return loginErrorResponse.message[0].messages[0].message
    }
}