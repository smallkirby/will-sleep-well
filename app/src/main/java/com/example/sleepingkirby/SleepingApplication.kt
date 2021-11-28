package com.example.sleepingkirby

import android.app.Application
import com.example.sleepingkirby.database.definition.DailyDefinitionDatabase
import com.example.sleepingkirby.database.definition.DailyDefinitionRepository
import com.example.sleepingkirby.database.log.DailyLogDataBase
import com.example.sleepingkirby.database.log.DailyLogRepository
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob

class SleepingApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    @InternalCoroutinesApi
    val definitionDatabase by lazy { DailyDefinitionDatabase.getDatabase(this, applicationScope) }
    @InternalCoroutinesApi
    val definitionRepository by lazy { DailyDefinitionRepository(definitionDatabase.dailyDefinitionDao()) }

    @InternalCoroutinesApi
    val logDatabase by lazy { DailyLogDataBase.getDatabase(this, applicationScope) }
    @InternalCoroutinesApi
    val logRespository by lazy { DailyLogRepository(logDatabase.dailyLogDao()) }


    init {
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d("Logger initialized.")
    }
}