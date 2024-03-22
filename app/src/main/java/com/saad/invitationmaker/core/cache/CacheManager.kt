package com.saad.invitationmaker.core.cache

class CacheManager<T> {
    private val cache = HashMap<String, T>()
    fun put(key: String, value: T) {
        cache[key] = value
    }


    fun get(key: String): T? {
        return cache[key]
    }
}