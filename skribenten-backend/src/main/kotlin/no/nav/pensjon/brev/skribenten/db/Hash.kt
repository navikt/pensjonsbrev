package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.letter.Edit
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.columnTransformer
import org.jetbrains.exposed.v1.dao.Entity
import kotlin.reflect.KProperty

fun Column<Edit.Letter>.writeHashTo(hash: Column<Hash<Edit.Letter>>) =
    WithHash(this, hash)

fun <T> Table.hashColumn(name: String): Column<Hash<T>> =
    binary(name).transform(columnTransformer(Hash<T>::hexBytes, Hash.Companion::fromBytes))


@JvmInline
value class Hash<T>(private val hex: String) {
    val hexBytes: ByteArray
        get() = Hex.decodeHex(hex)
    companion object {
        fun <T> read(obj: T): Hash<T> = fromBytes(WithHash.hash(obj))
        fun <T> fromBytes(bytes: ByteArray): Hash<T> = Hash(Hex.encodeHexString(bytes))
    }
}

class WithHash<T>(private val letter: Column<T>, private val hash: Column<Hash<T>>) {

    operator fun <ID : Comparable<ID>> setValue(thisRef: Entity<ID>, property: KProperty<*>, value: T) {
        with(thisRef) {
            letter.setValue(thisRef, property, value)
            hash.setValue(thisRef, property, Hash.fromBytes(hash(value)))
        }
    }

    operator fun <ID : Comparable<ID>> getValue(thisRef: Entity<ID>, property: KProperty<*>): T {
        return with(thisRef) {
            letter.getValue(thisRef, property)
        }
    }

    companion object {
        fun <T> hash(obj: T): ByteArray =
            DigestUtils.sha3_256(databaseObjectMapper.writeValueAsBytes(obj))
                .also { assert(it.size == 32) { "SHA3-256 hash of ${obj?.javaClass} was longer than 32 bytes: ${it.size}" } }
    }
}