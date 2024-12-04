package com.example.androidcookbook.data.modules

import com.example.androidcookbook.data.network.AiGenService
import com.example.androidcookbook.data.network.AuthService
import com.example.androidcookbook.data.network.NewsfeedService
import com.example.androidcookbook.data.network.PostService
import com.example.androidcookbook.data.network.UserService
import com.example.androidcookbook.data.providers.AccessTokenProvider
import com.example.androidcookbook.data.repositories.AiGenRepository
import com.example.androidcookbook.data.repositories.AuthRepository
import com.example.androidcookbook.data.repositories.NewsfeedRepository
import com.example.androidcookbook.data.repositories.PostRepository
import com.example.androidcookbook.data.repositories.UserRepository
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CookbookBEModule {

    private const val COOKBOOK_BE = "https://cookbookbe.onrender.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CookbookRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class CookbookAccessToken

//    private val accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjMxLCJ1c2VybmFtZSI6ImhvYXByaTEyMyIsInJvbGVzIjpudWxsLCJpYXQiOjE3MzI5MjM3MTUsImV4cCI6MTczMjkyNzMxNX0.-49F7heQjEnZRKL14sViM_ETE0A67YhPI2Z8vEz0FQg"

    @Provides
    @Singleton
    fun provideOkHttpClient(accessTokenProvider: AccessTokenProvider) = OkHttpClient.Builder()
        .addInterceptor{ chain ->
            val token = accessTokenProvider.accessToken.value
            val request = chain.request().newBuilder()
                .apply {
                    if (token.isNotEmpty()) {
                        addHeader("Authorization", "Bearer $token") // Attach token
                    }
                }
                .build()
            chain.proceed(request)
        }
        .addInterceptor(loggingInterceptor)
        .build()

    @CookbookRetrofit
    @Provides
    @Singleton
    fun provideCookBE(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(COOKBOOK_BE)
        .client(client)
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

    @Provides
    @Singleton
    fun provideAiGenService(@CookbookRetrofit retrofit: Retrofit): AiGenService =
        retrofit.create(AiGenService::class.java)

    @Provides
    @Singleton
    fun provideAiGenRepository(aiGenService: AiGenService): AiGenRepository =
        AiGenRepository(aiGenService)

    @Provides
    @Singleton
    fun provideNewsfeedService(@CookbookRetrofit retrofit: Retrofit): NewsfeedService =
        retrofit.create(NewsfeedService::class.java)

    @Provides
    @Singleton
    fun provideNewsfeedRepository(newsfeedService: NewsfeedService): NewsfeedRepository =
        NewsfeedRepository(newsfeedService)
}
