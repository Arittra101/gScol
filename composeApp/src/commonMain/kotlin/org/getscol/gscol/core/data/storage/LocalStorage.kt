package org.getscol.gscol.core.data.storage

import kotlinx.coroutines.flow.Flow

interface LocalStorage {
    // String operations
    suspend fun setString(key: String, value: String)
    suspend fun getString(key: String): String?

    // Int operations
    suspend fun setInt(key: String, value: Int)
    suspend fun getInt(key: String): Int?

    // Boolean operations
    suspend fun setBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String): Boolean?

    fun getFlowBoolean(key: String): Flow<Boolean>

    fun getFlowInt(key: String): Flow<Int>

    suspend fun setLong(key: String, value: Long)
    suspend fun getLong(key: String): Long?

    // Float operations
    suspend fun setFloat(key: String, value: Float)
    suspend fun getFloat(key: String): Float?

    // Operations
    suspend fun remove(key: String)
    suspend fun clear()
    fun getAll(): Flow<Map<String, Any>>
}