package com.team6.connectbca.data.datasource.services

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.team6.connectbca.data.BASE_JS_URL
import com.team6.connectbca.data.BASE_URL
import com.team6.connectbca.data.datasource.interfaces.auth.AuthLocalDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun provideRetrofitBuilder(
    context: Context,
    baseUrl: String,
    authLocalDataSource: AuthLocalDataSource? = null,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(provideOkhttpClient(context, authLocalDataSource))
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()
}

private fun provideOkhttpClient(
    context: Context,
    authLocalDataSource: AuthLocalDataSource? = null,
): OkHttpClient {
    val okHttpClient = if (authLocalDataSource != null) {
        OkHttpClient.Builder()
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor(provideChuckerInterceptor(context))
            .addInterceptor(provideHttpHeaderInterceptor(context, authLocalDataSource))
            .build()
    } else {
        OkHttpClient.Builder()
            .addInterceptor(provideHttpLoggingInterceptor())
            .addInterceptor(provideChuckerInterceptor(context))
            .build()
    }
    return okHttpClient
}

private fun provideHttpHeaderInterceptor(
    context: Context,
    authLocalDataSource: AuthLocalDataSource,
): Interceptor {
    return Interceptor { chain ->
        val token = runBlocking { authLocalDataSource.getToken() }
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(request)
    }
}

private fun provideHttpLoggingInterceptor(): Interceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}

private fun provideChuckerInterceptor(context: Context): Interceptor {
    return ChuckerInterceptor.Builder(context).build()
}

fun provideLoginService(context: Context): LoginService {
    return provideRetrofitBuilder(
        context,
        BASE_URL
    ).create(LoginService::class.java)
}

fun provideBankStatementService(
    context: Context,
    authLocalDataSource: AuthLocalDataSource,
): BankStatementService {
    return provideRetrofitBuilder(
        context,
        BASE_URL,
        authLocalDataSource
    ).create(BankStatementService::class.java)
}

fun provideAccountMonthlyService(
    context: Context,
    authLocalDataSource: AuthLocalDataSource
) : AccountMonthlyService {
    return provideRetrofitBuilder(
        context,
        BASE_URL,
        authLocalDataSource
    ).create(AccountMonthlyService::class.java)
}

fun provideQrisService(
    context: Context, 
    authLocalDataSource: AuthLocalDataSource
): QrisService {
    return provideRetrofitBuilder(
        context,
        BASE_JS_URL,
        authLocalDataSource
    ).create(QrisService::class.java)
}

fun provideTransferService(
    context: Context,
    authLocalDataSource: AuthLocalDataSource
): TransferService {
    return provideRetrofitBuilder(
        context,
        BASE_URL,
        authLocalDataSource
    ).create(TransferService::class.java)
}

fun provideSavedAccountService(
    context: Context,
    authLocalDataSource: AuthLocalDataSource
): SavedAccountService {
    return provideRetrofitBuilder(
        context,
        BASE_URL,
        authLocalDataSource
    ).create(SavedAccountService::class.java)
}

fun providePinService(context: Context,authLocalDataSource: AuthLocalDataSource): PinService {
    return provideRetrofitBuilder(
        context,
        BASE_URL,
        authLocalDataSource
    ).create(PinService::class.java)
}

fun provideUserService(
    context: Context,
    authLocalDataSource: AuthLocalDataSource
): UserService {
    return provideRetrofitBuilder(
        context,
        BASE_URL,
        authLocalDataSource
    ).create(UserService::class.java)
}