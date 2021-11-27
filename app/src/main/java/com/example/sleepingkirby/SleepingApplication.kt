package com.example.sleepingkirby

import android.app.Application
import com.example.sleepingkirby.database.dailyevent.DailyEventDatabase
import com.example.sleepingkirby.database.dailyevent.DailyEventRepository
import com.example.sleepingkirby.database.dailytask.DailyTaskDatabase
import com.example.sleepingkirby.database.dailytask.DailyTaskRepository
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob

class SleepingApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    @InternalCoroutinesApi
    val taskDatabase by lazy { DailyTaskDatabase.getDatabase(this, applicationScope) }
    @InternalCoroutinesApi
    val taskRepository by lazy { DailyTaskRepository(taskDatabase.dailyTaskDao()) }

    @InternalCoroutinesApi
    val eventDatabase by lazy { DailyEventDatabase.getDatabase(this, applicationScope) }
    @InternalCoroutinesApi
    val eventRepository by lazy { DailyEventRepository(eventDatabase.dailyEventDao()) }

    init {
        Logger.addLogAdapter(AndroidLogAdapter())
        Logger.d("Logger initialized.")
    }
}