package com.generalov.lab4.usecases

import com.generalov.lab4.types.InputResult

class Validator {
    companion object {
        fun phoneValidate(phone: String): InputResult {
            val regex = """^\+7\d{10}$""".toRegex()
            return if (phone.length < 12) {
                InputResult.FieldShort
            } else if (!regex.matches(phone)) {
                InputResult.FieldIncorrect
            } else {
                InputResult.Success
            }
        }

        fun passwordValidate(
            password: String,
            confirmPassword: String? = null,
            minLength: Int = 5,
            maxLength: Int = 16
        ): InputResult {
            return if (password != confirmPassword && confirmPassword != null) {
                InputResult.FieldDoNotMatch
            } else if (password.isEmpty()){
                InputResult.FieldEmpty
            } else if (password.length < minLength) {
                InputResult.FieldShort
            } else if (password.length > maxLength) {
                InputResult.FieldLong
            } else {
                InputResult.Success
            }
        }

        fun usernameValidate(
            username: String, minLength: Int = 3,
            maxLength: Int = 20
        ): InputResult {
            return if (username.isEmpty()) {
                InputResult.FieldEmpty
            } else if (username.length < minLength) {
                InputResult.FieldShort
            } else if (username.length > maxLength) {
                InputResult.FieldLong
            } else {
                InputResult.Success
            }
        }
    }

}