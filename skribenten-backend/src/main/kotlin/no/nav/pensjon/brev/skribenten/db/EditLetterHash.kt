package no.nav.pensjon.brev.skribenten.db

import no.nav.pensjon.brev.skribenten.letter.Edit
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import kotlin.reflect.KProperty

fun Column<Edit.Letter>.writeHashTo(hash: Column<ByteArray>) =
    WithEditLetterHash(this, hash)

fun Table.hashColumn(name: String): Column<ByteArray> =
    binary(name, 32)

fun Column<ByteArray>.editLetterHash(): ValueClassWrapper<EditLetterHash, ByteArray> =
    wrap({ EditLetterHash(Hex.encodeHexString(it)) }, { Hex.decodeHex(it.hex) })

@JvmInline
value class EditLetterHash(val hex: String)

class WithEditLetterHash(private val letter: Column<Edit.Letter>, private val hash: Column<ByteArray>) {

    operator fun <ID : Comparable<ID>> setValue(thisRef: Entity<ID>, property: KProperty<*>, value: Edit.Letter) {
        with(thisRef) {
            letter.setValue(thisRef, property, value)
            hash.setValue(thisRef, property, hashBrev(value))
        }
    }

    operator fun <ID : Comparable<ID>> getValue(thisRef: Entity<ID>, property: KProperty<*>): Edit.Letter {
        return with(thisRef) {
            letter.getValue(thisRef, property)
        }
    }

    companion object {
        fun hashBrev(brev: Edit.Letter): ByteArray =
            DigestUtils.sha3_256(databaseObjectMapper.writeValueAsBytes(brev))
                .also { assert(it.size == 32) { "SHA3-256 hash of redigertbrev was longer than 32 bytes: ${it.size}" } }

    }
}