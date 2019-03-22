package com.home.smarthomeserver

import com.home.smarthomeserver.service.PasswordValidator
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun `test short password should return TOO_SHORT enum`() {
        val password = "abc12"

        val result = PasswordValidator.isValid(password)?.get(0) ?: fail("Invalid password accepted")

        assertThat(result).isEqualTo(PasswordValidator.PasswordResult.TOO_SHORT)
    }

    @Test
    fun `test password without digits should return NO_DIGITS enum`() {
        val password = "abcdefgh"

        val result = PasswordValidator.isValid(password)?.get(0) ?: fail("Invalid password accepted")

        assertThat(result).isEqualTo(PasswordValidator.PasswordResult.NO_DIGITS)
    }

    @Test
    fun `test password thats too short and cotains no digits return TOO_SHORT and NO_DIGITS enum`() {
        val password = "ab"

        val result = PasswordValidator.isValid(password) ?: fail("Invalid password accepted")

        assertThat(result).containsAll(listOf(PasswordValidator.PasswordResult.TOO_SHORT, PasswordValidator.PasswordResult.NO_DIGITS))
    }
}