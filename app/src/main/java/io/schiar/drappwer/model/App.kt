package io.schiar.drappwer.model

data class App(val name: String, val packageName: String, val icon: ByteArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as App

        if (name != other.name) return false
        if (packageName != other.packageName) return false
        return icon.contentEquals(other.icon)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + packageName.hashCode()
        result = 31 * result + icon.contentHashCode()
        return result
    }
}