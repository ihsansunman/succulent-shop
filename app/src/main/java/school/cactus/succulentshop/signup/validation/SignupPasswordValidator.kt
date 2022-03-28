package school.cactus.succulentshop.signup.validation

import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.Validator

class SignupPasswordValidator : Validator {
    override fun validate(field: String) = when {
        field.isEmpty() -> R.string.signup_password_is_required_error
        field.length < 7 -> R.string.signup_password_is_too_short_error
        field.length > 40 -> R.string.signup_password_is_too_long_error
        !field.any { it.isDigit() } -> R.string.signup_password_must_contain_characters_error
        !field.any { it.isLowerCase() } -> R.string.signup_password_must_contain_characters_error
        !field.any { it.isUpperCase() } -> R.string.signup_password_must_contain_characters_error
        !field.any { !it.isLetterOrDigit() } -> R.string.signup_password_must_contain_characters_error
        else -> null
    }
}