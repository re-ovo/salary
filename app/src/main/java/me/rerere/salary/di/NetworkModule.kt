package me.rerere.salary.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.rerere.salary.api.SalaryAPI
import me.rerere.salary.sharedPreferenceOf
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request()
            val sharedPreferences = sharedPreferenceOf("jwt")
            if (sharedPreferences.getString("token", "").isNullOrEmpty()) {
                chain.proceed(request)
            } else {
                chain.proceed(
                    request.newBuilder()
                        .addHeader(
                            "Authorization",
                            "Bearer ${sharedPreferences.getString("token", "")}"
                        )
                        .build()
                )
            }
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("http://10.0.2.2")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideSalaryAPI(retrofit: Retrofit): SalaryAPI = retrofit.create(SalaryAPI::class.java)
}