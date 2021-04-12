package com.muiezarif.kidsfitness.di.modules

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.muiezarif.kidsfitness.network.api.ApiInterface
import com.muiezarif.kidsfitness.network.api.Urls
import com.muiezarif.kidsfitness.network.repository.Repository
import com.muiezarif.kidsfitness.utils.D
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
object UtilsModule {

    internal val httpLoggingInterceptor: HttpLoggingInterceptor
        @JvmStatic
        @Provides
        @Singleton
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level =
                if (D.IS_DEVELOPMENT_MODE) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
            return interceptor
        }

    @JvmStatic
    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        val builder = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        return builder.setLenient().create()
    }


    @JvmStatic
    @Provides
    @Singleton
    internal fun getRequestHeader(interceptor: HttpLoggingInterceptor): OkHttpClient {

        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .build()
            chain.proceed(request)
        }
            .connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)

        httpClient.addInterceptor(interceptor)
        return httpClient.build()
    }

    @JvmStatic
    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Urls.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @JvmStatic
    @Provides
    @Singleton
    internal fun getApiCallInterface(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @JvmStatic
    @Provides
    @Singleton
    internal fun getRepository(apiCallInterface: ApiInterface): Repository {
        return Repository(apiCallInterface)
    }
}