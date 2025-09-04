package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.letter.Edit
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import kotlin.reflect.KProperty

@JvmName("writeHashTo")
fun Column<Edit.Letter>.writeHashTo(hash: Column<ByteArray>) =
    WithEditLetterHash(this, hash) { WithEditLetterHash.hashBrev(it) }

@JvmName("writeHashToEncrypted")
fun Column<EncryptedByteArray?>.writeHashTo(hash: Column<EncryptedByteArray?>) =
    WithEditLetterHash(this, hash) { EncryptedByteArray(WithEditLetterHash.hashBrev(it)) }

fun Table.hashColumn(name: String): Column<ByteArray> =
    binary(name)

fun Column<ByteArray>.editLetterHash(): ValueClassWrapper<EditLetterHash, ByteArray> =
    wrap({ EditLetterHash(Hex.encodeHexString(it)) }, { Hex.decodeHex(it.hex) })

@JvmName("editLetterHashEncrypted")
fun Column<EncryptedByteArray?>.editLetterHash(): ValueClassWrapper<EditLetterHash?, EncryptedByteArray?> =
    ValueClassWrapper(
        column = this,
        wrap = { it?.bytes?.let { bytes -> Hex.encodeHexString(bytes) }?.let { hex -> EditLetterHash(hex) } },
        unwrap = { it?.hex?.let { hex -> Hex.decodeHex(hex) } ?.let { bytes -> EncryptedByteArray(bytes) } }
    )

@JvmInline
value class EditLetterHash(val hex: String)

class WithEditLetterHash<FROM, TO>(private val letter: Column<FROM>, private val hash: Column<TO>, private val hashFunction: (FROM) -> TO) {

    operator fun <ID : Comparable<ID>> setValue(thisRef: Entity<ID>, property: KProperty<*>, value: FROM) {
        with(thisRef) {
            letter.setValue(thisRef, property, value)
            hash.setValue(thisRef, property, hashFunction(value))
        }
    }

    operator fun <ID : Comparable<ID>> getValue(thisRef: Entity<ID>, property: KProperty<*>): FROM {
        return with(thisRef) {
            letter.getValue(thisRef, property)
        }
    }

    companion object {
        fun <FROM> hashBrev(brev: FROM): ByteArray =
            DigestUtils.sha3_256(databaseObjectMapper.writeValueAsBytes(brev))
                .also { assert(it.size == 32) { "SHA3-256 hash of redigertbrev was longer than 32 bytes: ${it.size}" } }

    }
}