package com.content.lottoandluck.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import permissionsModule

fun initKoin(
    additionalModules: List<Module> = emptyList(),
): KoinApplication {
    stopKoin()
    val koinApplication = koinApplication {
        modules(
            listOf(
                module { includes(additionalModules) },
                permissionsModule,
            )
        )
        createEagerInstances()
    }
    return startKoin(koinApplication)
}
