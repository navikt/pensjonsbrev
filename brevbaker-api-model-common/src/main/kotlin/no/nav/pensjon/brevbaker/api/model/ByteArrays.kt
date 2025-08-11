package no.nav.pensjon.brevbaker.api.model


@JvmInline
value class DekryptertByteArray(val byteArray: ByteArray) {
    fun contentEquals(other: DekryptertByteArray) = byteArray.contentEquals(other.byteArray)
    fun contentHashCode() = byteArray.contentHashCode()
    fun contentToString() = byteArray.contentToString()
    fun somString() = String(byteArray)
    val size: Int
        get() = byteArray.size
}

@JvmInline
value class KryptertByteArray(val byteArray: ByteArray) {
    fun contentEquals(other: KryptertByteArray) = byteArray.contentEquals(other.byteArray)
    fun contentHashCode() = byteArray.contentHashCode()
}