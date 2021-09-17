package com.example.myapplication.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myapplication.domain.model.WordPackage

/**
 * Entity пакета в базе данныхх
 *
 */
@Entity(tableName = "word_package")
data class WordPackageEntity(
    @ColumnInfo(name = "word_package_name") var name: String,
    @ColumnInfo(name = "word_package_front_language") var frontLanguage: String,
    @ColumnInfo(name = "word_package_back_language") var backLanguage: String

) {
    @PrimaryKey(autoGenerate = true)
    var wordPackageId: Long = 0

    /**
     * Метод, приводящий данное Entity к модели домена
     */
    fun convertToDomain(): WordPackage =
        WordPackage(wordPackageId, name, frontLanguage, backLanguage, mutableListOf())
}