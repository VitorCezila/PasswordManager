package com.cezila.passwordmanager.domain.use_case

import com.cezila.passwordmanager.domain.data.repository.FakeStorePasswordRepositoryImpl
import com.cezila.passwordmanager.domain.model.Password
import com.cezila.passwordmanager.domain.repository.StorePasswordRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdatePasswordUseCaseTest {

    private lateinit var repository: StorePasswordRepository
    private lateinit var updatePasswordUseCase: UpdatePasswordUseCase

    @Before
    fun setup() {
        repository = FakeStorePasswordRepositoryImpl()
        updatePasswordUseCase = UpdatePasswordUseCase(repository)
    }

    @Test
    fun `updatePassword should update the password successfully`() = runTest {
        val originalPassword = Password(
            id = 1,
            title = "Example",
            login = "user@example.com",
            url = "https://example.com",
            encryptedPassword = "original_encrypted_password"
        )

        repository.insertPassword(originalPassword)

        val updatedPassword = originalPassword.copy(
            title = "Updated Example",
            login = "updated_user@example.com",
            url = "https://updated-example.com",
            encryptedPassword = "updated_encrypted_password"
        )

        updatePasswordUseCase(updatedPassword)

        val retrievedPassword = repository.getPasswordById(1)
        assertEquals(updatedPassword, retrievedPassword)
    }

    @Test
    fun `updatePassword should do nothing for non-existent password`() = runTest {
        val updatedPassword = Password(
            id = 1,
            title = "Updated Example",
            login = "updated_user@example.com",
            url = "https://updated-example.com",
            encryptedPassword = "updated_encrypted_password"
        )

        updatePasswordUseCase(updatedPassword)
        val passwords = repository.getPasswords()
        assertEquals(0, passwords.size)
    }
}