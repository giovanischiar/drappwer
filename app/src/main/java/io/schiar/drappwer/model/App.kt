package io.schiar.drappwer.model

data class App(
    val name: String, val packageName: String, val icon: ByteArray, val selected: Boolean = false
) {
    fun switch(): App {
        return App(name = name, packageName = packageName, icon = icon, selected = !selected)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as App

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