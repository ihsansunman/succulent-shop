package school.cactus.succulentshop.signup.validation

import school.cactus.succulentshop.R
import school.cactus.succulentshop.validation.Validator

class SignupEmailValidator : Validator {
    override fun validate(field: String) = when {
        field.isEmpty() -> R.string.signup_email_is_required_error
        field.all { it != '@' } -> R.string.signup_email_is_invalid_error
        field.all { it != '.' } -> R.string.signup_email_is_invalid_error
        field.substringAfter('@').length < 5 -> R.string.signup_email_too_short_error
        field.substringAfter('@').length > 50 -> R.string.signup_email_too_long_error
        else -> null
    }
}