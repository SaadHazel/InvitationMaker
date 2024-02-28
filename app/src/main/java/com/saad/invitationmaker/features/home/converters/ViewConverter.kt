package com.saad.invitationmaker.features.home.converters

import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.saad.invitationmaker.features.home.models.AllViews


class ViewsConverter {
    @TypeConverter
    fun fromStringList(value: String?): List<AllViews>? {
        if (value == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<AllViews>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toStringList(list: List<AllViews>?): String {
        return Gson().toJson(list)
    }
}
