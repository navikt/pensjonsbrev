package no.nav.pensjon.brev.skribenten.db.kryptering

import no.nav.pensjon.brev.skribenten.db.EncryptedByteArray
import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class KrypteringService(private val krypteringsnoekkel: String) {
    companion object {
        private const val ALGORITHM = "AES/GCM/NoPadding"
        private const val FACTORY_INSTANCE = "PBKDF2WithHmacSHA256"
        private const val TAG_LENGTH_BIT = 128
        private const val IV_LENGTH_BYTE = 12
        private const val SALT_LENGTH_BYTE = 16
        private const val ALGORITHM_TYPE = "AES"

        lateinit var instance: KrypteringService

        fun krypter(klartekst: ByteArray) : EncryptedByteArray {
            require(KrypteringService::instance.isInitialized) { "Krypteringservice må være initialisert" }
            return instance.krypter(klartekst)
        }
        fun dekrypter(kryptertMelding: EncryptedByteArray): ByteArray {
            require(KrypteringService::instance.isInitialized) { "Krypteringservice må være initialisert" }
            return instance.dekrypter(kryptertMelding)
        }
    }

    init {
        instance = this
    }

    fun krypter(klartekst: ByteArray): EncryptedByteArray {
        val salt = getRandomNonce(SALT_LENGTH_BYTE)
        val iv = getRandomNonce(IV_LENGTH_BYTE)
        val kryptertMelding = initCipher(Cipher.ENCRYPT_MODE, getSecretKey(salt), iv).doFinal(klartekst)

        return ByteBuffer.allocate(iv.size + salt.size + kryptertMelding.size)
                .put(iv)
                .put(salt)
                .put(kryptertMelding)
                .array()
            .let { EncryptedByteArray(it) }
    }

    fun dekrypter(kryptertMelding: EncryptedByteArray): ByteArray {
        val byteBuffer = ByteBuffer.wrap(kryptertMelding.bytes)

        val iv = ByteArray(IV_LENGTH_BYTE)
        byteBuffer.get(iv)

        val salt = ByteArray(SALT_LENGTH_BYTE)
        byteBuffer.get(salt)

        val encryptedByte = ByteArray(byteBuffer.remaining())
        byteBuffer.get(encryptedByte)

        return initCipher(Cipher.DECRYPT_MODE, getSecretKey(salt), iv).doFinal(encryptedByte)
    }

    private fun initCipher(mode: Int, secretKey: SecretKey, iv: ByteArray) = Cipher.getInstance(ALGORITHM)
        .also { it.init(mode, secretKey, GCMParameterSpec(TAG_LENGTH_BIT, iv)) }

    private fun getRandomNonce(length: Int): ByteArray {
        val nonce = ByteArray(length)
        SecureRandom().nextBytes(nonce)
        return nonce
    }

    private fun getSecretKey(salt: ByteArray) = SecretKeySpec(
        SecretKeyFactory.getInstance(FACTORY_INSTANCE)
            .generateSecret(PBEKeySpec(krypteringsnoekkel.toCharArray(), salt, 65536, 256)).encoded,
        ALGORITHM_TYPE
    )
}