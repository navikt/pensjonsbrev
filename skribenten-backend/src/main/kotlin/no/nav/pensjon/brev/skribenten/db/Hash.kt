package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.letter.Edit
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.columnTransformer
import kotlin.reflect.KProperty

fun Column<Edit.Letter>.writeHashTo(hash: Column<Hash>) =
    WithHash(this, hash)

fun Table.hashColumn(name: String): Column<Hash> =
    binary(name).transform(columnTransformer({ it.hexBytes }, { Hash.fromBytes(it) }))


@JvmInline
value class Hash(private val hex: String) {
    val hexBytes: ByteArray
        get() = Hex.decodeHex(hex)
    companion object {
        fun <T> read(obj: T): Hash = fromBytes(WithHash.hash(obj))
        fun fromBytes(bytes: ByteArray) = Hash(Hex.encodeHexString(bytes))
    }
}

class WithHash<T>(private val letter: Column<T>, private val hash: Column<Hash>) {

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