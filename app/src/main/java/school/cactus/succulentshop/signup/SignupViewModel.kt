package school.cactus.succulentshop.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_INDEFINITE
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import kotlinx.coroutines.launch
import school.cactus.succulentshop.R
import school.cactus.succulentshop.auth.AuthRepository
import school.cactus.succulentshop.auth.JwtStore
import school.cactus.succulentshop.auth.Resource
import school.cactus.succulentshop.infra.BaseViewModel
import school.cactus.succulentshop.infra.snackbar.SnackbarAction
import school.cactus.succulentshop.infra.snackbar.SnackbarState
import school.cactus.succulentshop.signup.validation.SignupEmailValidator
import school.cactus.succulentshop.signup.validation.SignupPasswordValidator
import school.cactus.succulentshop.signup.validation.SignupUsernameValidator

class SignupViewModel(
    private val store: JwtStore,
    private val repository: AuthRepository,

    ) : BaseViewModel() {
    private val emailValidator = SignupEmailValidator()
    private val usernameValidator = SignupUsernameValidator()
    private val passwordValidator = SignupPasswordValidator()

    val email = MutableLiveData<String>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _emailErrorMessage = MutableLiveData<Int>()
    val emailErrorMessage: LiveData<Int> = _emailErrorMessage

    private val _usernameErrorMessage = MutableLiveData<Int>()
    val usernameErrorMessage: LiveData<Int> = _usernameErrorMessage

    private val _passwordErrorMessage = MutableLiveData<Int>()
    val passwordErrorMessage: LiveData<Int> = _passwordErrorMessage

    val showKeyboardState = MutableLiveData<Boolean>()

    fun onButtonSignupClick() = viewModelScope.launch {
        showKeyboardState.value = false
        if (isEmailValid() and isUsernameValid() and isPasswordValid()) {
            val result = repository.sendSignupRequest(
                email.value.orEmpty(),
                password.value.orEmpty(),
                username.value.orEmpty(),
            )

            when (result) {
                is Resource.Success -> onSuccess(result.data!!.jwt)
                is Resource.Error.ClientError -> onClientError(result.message!!)
                is Resource.Error.Failure -> onFailure()
                is Resource.Error.UnexpectedError -> onUnexpectedError()
            }
        }
    }

    private fun onUnexpectedError() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.unexpected_error_occurred,
            duration = LENGTH_LONG
        )
    }

    private fun onFailure() {
        _snackbarState.value = SnackbarState(
            errorRes = R.string.check_your_connection,
            duration = LENGTH_INDEFINITE,
            action = SnackbarAction(
                text = R.string.retry,
                action = {
                    onButtonSignupClick()
                }
            )
        )
    }

    private fun onClientError(errorMessage: String) {
        _snackbarState.value = SnackbarState(
            error = errorMessage,
            duration = LENGTH_LONG
        )
    }

    private fun onSuccess(jwt: String) {
        store.saveJwt(jwt)

        val directions = SignupFragmentDirections.actionSignupFragmentToProductListFragment()
        navigation.navigate(directions)
    }

    private fun isPasswordValid(): Boolean {
        _passwordErrorMessage.value = passwordValidator.validate(password.value.orEmpty())
        return _passwordErrorMessage.value == null
    }

    private fun isUsernameValid(): Boolean {
        _usernameErrorMessage.value = usernameValidator.validate(username.value.orEmpty())
        return _usernameErrorMessage.value == null
    }

    private fun isEmailValid(): Boolean {
        _emailErrorMessage.value = emailValidator.validate(email.value.orEmpty())
        return _emailErrorMessage.value == null
    }

    fun onButtonAlreadyHaveAccountClick() {
        navigateToLoginScreen()
    }

    private fun navigateToLoginScreen() {
        val directions = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
        navigation.navigate(directions)
    }
}