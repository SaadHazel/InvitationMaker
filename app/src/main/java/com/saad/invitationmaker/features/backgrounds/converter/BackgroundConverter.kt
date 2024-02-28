package com.saad.invitationmaker.features.backgrounds.converter

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.saad.invitationmaker.core.network.models.Hit


class BackgroundConverter {
    @TypeConverter
    fun fromStringList(value: String?): List<Hit>? {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Hit>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toStringList(list: List<Hit>?): String {
        return Gson().toJson(list)
    }
}