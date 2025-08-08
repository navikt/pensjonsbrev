package no.nav.pensjon.brev.skribenten.db.kryptering

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64

class KrypteringService(private val krypteringsnoekkel: String) {
    val algorithm: String = "AES/CBC/PKCS5Padding"

    fun krypterData(data: DekryptertByteArray): KryptertByteArray = krypter(hentNoekkel(), data)

    fun dekrypterData(data: KryptertByteArray): DekryptertByteArray = dekrypter(hentNoekkel(), data)

    private val transformation = "AES"

    private fun krypter(noekkel: SecretKey, data: DekryptertByteArray): KryptertByteArray {
        val keydata = noekkel.encoded
        val skeySpec = SecretKeySpec(keydata, 0, keydata.size, transformation)
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(data.byteArray).let { KryptertByteArray(it) }
    }

    private fun dekrypter(noekkel: SecretKey, data: KryptertByteArray): DekryptertByteArray {
        val cipher = Cipher.getInstance(algorithm)
        cipher.init(Cipher.DECRYPT_MODE, noekkel, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(data.byteArray).let { DekryptertByteArray(it) }
    }

    private fun hentNoekkel(): SecretKey {
        if (krypteringsnoekkel.isEmpty()) {
            throw Exception("Kunne ikke finne krypteringsnøkkel. Er ENV \"CRYPTKEY\" satt?")
        }
        val decodedNoekkel = Base64.decode(krypteringsnoekkel)
        return SecretKeySpec(decodedNoekkel, 0, decodedNoekkel.size, transformation)
    }
}

@JvmInline
value class DekryptertByteArray(val byteArray: ByteArray)

@JvmInline
value class KryptertByteArray(val byteArray: ByteArray)