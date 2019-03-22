package com.home.smarthomeserver.service


object PasswordValidator {

    private const val PASSWORD_LENGTH = 8

    fun isValid(password: String): List<PasswordResult>? {
        val validationResult = mutableListOf<PasswordResult>()
        if (password.length < PASSWORD_LENGTH) validationResult.add(PasswordValidator.PasswordResult.TOO_SHORT)
        if (!password.contains(Regex("[0-9]+"))) validationResult.add(PasswordValidator.PasswordResult.NO_DIGITS)
        return if(validationResult.isEmpty()) null else validationResult
    }

    enum class PasswordResult {
        TOO_SHORT {
            override fun toString(): String = "Password must be at least 8 characters"
        },
        NO_DIGITS {
            override fun toString(): String = "Password must contain at least 1 digit"
        }
    }

}

