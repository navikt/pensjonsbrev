package no.nav.pensjon.brevbaker.api.model


@JvmInline
value class DekryptertByteArray(val byteArray: ByteArray) {
    fun contentEquals(other: DekryptertByteArray) = byteArray.contentEquals(other.byteArray)
    fun contentHashCode() = byteArray.contentHashCode()
}

@JvmInline
value class KryptertByteArray(val byteArray: ByteArray) {
    fun contentEquals(other: KryptertByteArray) = byteArray.contentEquals(other.byteArray)
    fun contentHashCode() = byteArray.contentHashCode()
}