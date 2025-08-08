package no.nav.pensjon.brev.skribenten.db.kryptering

import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64

class KrypteringService(private val krypteringsnoekkel: String) {

    fun krypterData(data: ByteArray): ByteArray = krypter(hentNoekkel(), data)

    fun dekrypterData(data: ByteArray): ByteArray = dekrypter(hentNoekkel(), data)

    private val transformation = "AES"
    private val provider = "BC"

    private fun krypter(noekkel: SecretKey, data: ByteArray): ByteArray {
        val keydata = noekkel.encoded
        val skeySpec = SecretKeySpec(keydata, 0, keydata.size, transformation)
        val cipher = Cipher.getInstance(transformation, provider)
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(data)
    }

    private fun dekrypter(noekkel: SecretKey, data: ByteArray): ByteArray {
        val cipher = Cipher.getInstance(transformation, provider)
        cipher.init(Cipher.DECRYPT_MODE, noekkel, IvParameterSpec(ByteArray(cipher.blockSize)))
        return cipher.doFinal(data)
    }

    private fun hentNoekkel(): SecretKey {
        if (krypteringsnoekkel.isEmpty()) {
            throw Exception("Kunne ikke finne krypteringsnøkkel. Er ENV \"CRYPTKEY\" satt?")
        }

        val decodedNoekkel = Base64.decode(krypteringsnoekkel)

        return SecretKeySpec(decodedNoekkel, 0, decodedNoekkel.size, transformation)
    }
}