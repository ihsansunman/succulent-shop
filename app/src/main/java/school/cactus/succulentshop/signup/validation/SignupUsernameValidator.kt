package school.cactus.succulentshop.signup.validation

import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.Validator

class SignupUsernameValidator : Validator {
    override fun validate(field: String) = when {
        field.isEmpty() -> R.string.signup_username_is_required_error
        !field.all { it.isLetterOrDigit() || it == '_' } -> R.string.signup_username_must_not_consist_characters_error
        field.length < 2 -> R.string.signup_username_is_too_short_error
        field.length > 20 -> R.string.signup_username_is_too_long_error
        else -> null
    }
}