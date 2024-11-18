package com.example.androidcookbook.auth

import com.example.androidcookbook.data.modules.CookbookBEModule
import com.example.androidcookbook.data.repositories.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class],
    replaces = [CookbookBEModule::class])
@Module
class FakeAuthRepositoryModule {
    @Singleton
    @Provides
    fun provideFakeAuthRepository(): AuthRepository {
        return AuthRepository(authService = TimeoutExceptionAuthService())
    }
}