package com.example.sleepingkirby.database.definition.event

import com.example.sleepingkirby.R

enum class EventIcon(val id: Int) {
    COFFEE(R.drawable.ic_coffee),
    BOOK(R.drawable.ic_book),
    OTHER(R.drawable.ic_even);

    companion object {
        fun fromId(id: Int): EventIcon {
            values().forEach {
                if (it.id == id) return it
            }
            return OTHER
        }
    }
}