package com.filmapp.util

import android.content.Context
import com.filmapp.model.Film
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object HistoryManager {
    private const val PREF_NAME = "history_pref"
    private const val KEY_HISTORY = "history"
    private const val MAX_HISTORY = 20

    fun add(context: Context, film: Film) {
        val list = getAll(context).toMutableList()
        list.removeAll { it.id == film.id }
        list.add(0, film)
        if (list.size > MAX_HISTORY) list.removeAt(list.lastIndex)
        save(context, list)
    }

    fun getAll(context: Context): List<Film> {
        val json = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_HISTORY, null) ?: return emptyList()
        return runCatching { Json.decodeFromString<List<Film>>(json) }.getOrDefault(emptyList())
    }

    fun clear(context: Context) = save(context, emptyList())

    private fun save(context: Context, list: List<Film>) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_HISTORY, Json.encodeToString(list)).apply()
    }
}
