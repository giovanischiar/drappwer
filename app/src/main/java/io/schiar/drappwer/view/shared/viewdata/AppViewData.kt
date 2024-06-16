package io.schiar.drappwer.view.shared.viewdata

data class AppViewData(
    val name: String,
    val icon: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppViewData

        if (name != other.name) return false
        return icon.contentEquals(other.icon)
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + icon.contentHashCode()
        return result
    }
}
