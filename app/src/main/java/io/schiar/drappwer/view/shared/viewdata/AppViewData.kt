package io.schiar.drappwer.view.shared.viewdata

data class AppViewData(
    val name: String,
    val icon: ByteArray,
    val selected: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppViewData

        if (name != other.name) return false
        if (!icon.contentEquals(other.icon)) return false
        return selected == other.selected
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon.contentHashCode()
        result = 31 * result + selected.hashCode()
        return result
    }
}
