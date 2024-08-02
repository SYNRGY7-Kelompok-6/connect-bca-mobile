package com.team6.connectbca.data.datasource.services

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.team6.connectbca.data.BASE_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private fun provideRetrofitBuilder(context: Context, baseUrl: String): Retrofit {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(provideOkhttpClient(context))
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .build()
}

private fun provideOkhttpClient(context: Context): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(provideHttpLoggingInterceptor())
        .addInterceptor(provideChuckerInterceptor(context))
        .build()
}

private fun provideHttpLoggingInterceptor(): Interceptor {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return httpLoggingInterceptor
}

private fun provideChuckerInterceptor(context: Context): Interceptor {
    return ChuckerInterceptor.Builder(context).build()
}

fun provideLoginService(context: Context) : LoginService {
    return provideRetrofitBuilder(
        context,
        BASE_URL
    ).create(LoginService::class.java)
}

fun provideBankStatementService(context: Context) : BankStatementService {
    return provideRetrofitBuilder(
        context,
        BASE_URL
    ).create(BankStatementService::class.java)
}

fun provideAccountMonthlyService(context: Context) : AccountMonthlyService {
    return provideRetrofitBuilder(
        context,
        BASE_URL
    ).create(AccountMonthlyService::class.java)
}