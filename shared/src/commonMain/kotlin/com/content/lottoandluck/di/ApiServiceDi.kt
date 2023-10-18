package com.content.lottoandluck.di

import com.content.lottoandluck.data.apiservice.ApiService
import getHttpClient
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.direct
import org.kodein.di.instance

class ApiServiceDi {
    private val apiServiceModule = DI.Module("apiservice") {
        bindSingleton { ApiService(getHttpClient()) }
    }
    val apiServiceDi = DI {
        import(apiServiceModule)
    }
}

object ApiServiceFactory {
    val apiService = ApiServiceDi().apiServiceDi
    val diDirect = apiService.direct
    inline fun <reified T : Any> resolve(): T {
        return diDirect.instance()
    }
}

interface ApiServiceConfigurator {
    fun createApiService(): ApiService
}

class ApiServiceConfiguratorImpl : ApiServiceConfigurator {
    override fun createApiService(): ApiService {
        return ApiServiceFactory.resolve()
    }
}

object ApiServiceConfigFactory {
    fun createApiService(): ApiService {
        return ApiServiceConfiguratorImpl().createApiService()
    }
}