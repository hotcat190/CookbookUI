package com.example.androidcookbook.data.modules

import com.example.androidcookbook.data.network.AuthService
import com.example.androidcookbook.data.network.PostService
import com.example.androidcookbook.data.network.UserService
import com.example.androidcookbook.data.repositories.AuthRepository
import com.example.androidcookbook.data.repositories.PostRepository
import com.example.androidcookbook.data.repositories.UserRepository
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CookbookBEModule {

    private const val COOKBOOK_BE = "https://cookbookbe.onrender.com/"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CookbookRetrofit

    @CookbookRetrofit
    @Provides
    @Singleton
    fun provideCookBE(): Retrofit = Retrofit.Builder()
        .baseUrl(COOKBOOK_BE)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideAuthService(@CookbookRetrofit retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(authService: AuthService): AuthRepository =
        AuthRepository(authService)

    @Provides
    @Singleton
    fun provideUserService(@CookbookRetrofit retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(userService: UserService) =
        UserRepository(userService)

    @Provides
    @Singleton
    fun providePostService(@CookbookRetrofit retrofit: Retrofit): PostService =
        retrofit.create(PostService::class.java)

    @Provides
    @Singleton
    fun providePostRepository(postService: PostService) =
        PostRepository(postService)
}
