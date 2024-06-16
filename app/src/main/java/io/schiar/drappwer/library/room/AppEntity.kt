package io.schiar.drappwer.library.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "App")
data class AppEntity(
    val name: String,
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    val icon: ByteArray,
    val selected: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppEntity

        if (name != other.name) return false
        if (packageName != other.packageName) return false
        if (!icon.contentEquals(other.icon)) return false
        return selected == other.selected
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + packageName.hashCode()
        result = 31 * result + icon.contentHashCode()
        result = 31 * result + selected.hashCode()
        return result
    }
}
