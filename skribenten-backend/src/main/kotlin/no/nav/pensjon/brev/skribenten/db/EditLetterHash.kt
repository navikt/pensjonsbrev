package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.letter.Edit
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.columnTransformer
import kotlin.reflect.KProperty

fun Column<Edit.Letter>.writeHashTo(hash: Column<EditLetterHash>) =
    WithEditLetterHash(this, hash)

fun Table.hashColumn(name: String): Column<EditLetterHash> =
    binary(name).transform(columnTransformer({ it.hexBytes }, { EditLetterHash.fromBytes(it) }))


@JvmInline
value class EditLetterHash(private val hex: String) {
    val hexBytes: ByteArray
        get() = Hex.decodeHex(hex)
    companion object {
        fun read(letter: Edit.Letter): EditLetterHash = fromBytes(WithEditLetterHash.hash(letter))
        fun fromBytes(bytes: ByteArray) = EditLetterHash(Hex.encodeHexString(bytes))
        fun fromObject(obj: Any) = fromBytes(databaseObjectMapper.writeValueAsBytes(obj))
    }
}

class WithEditLetterHash<T>(private val letter: Column<T>, private val hash: Column<EditLetterHash>) {

    operator fun <ID : Comparable<ID>> setValue(thisRef: Entity<ID>, property: KProperty<*>, value: T) {
        with(thisRef) {
            letter.setValue(thisRef, property, value)
            hash.setValue(thisRef, property, EditLetterHash.fromBytes(hash(value)))
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
                .also { assert(it.size == 32) { "SHA3-256 hash of redigertbrev was longer than 32 bytes: ${it.size}" } }
    }
}