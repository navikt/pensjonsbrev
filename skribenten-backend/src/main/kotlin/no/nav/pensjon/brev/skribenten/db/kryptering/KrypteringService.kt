package no.nav.pensjon.brev.skribenten.db.kryptering

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
    }

    fun krypter(klartekst: DekryptertByteArray): KryptertByteArray {
        val salt = getRandomNonce(SALT_LENGTH_BYTE)
        val iv = getRandomNonce(IV_LENGTH_BYTE)
        val kryptertMelding = initCipher(Cipher.ENCRYPT_MODE, getSecretKey(salt), iv).doFinal(klartekst.byteArray)

        return KryptertByteArray(
            ByteBuffer.allocate(iv.size + salt.size + kryptertMelding.size)
                .put(iv)
                .put(salt)
                .put(kryptertMelding)
                .array()
        )
    }

    fun dekrypter(kryptertMelding: KryptertByteArray): DekryptertByteArray {
        val byteBuffer = ByteBuffer.wrap(kryptertMelding.byteArray)

        val iv = ByteArray(IV_LENGTH_BYTE)
        byteBuffer.get(iv)

        val salt = ByteArray(SALT_LENGTH_BYTE)
        byteBuffer.get(salt)

        val encryptedByte = ByteArray(byteBuffer.remaining())
        byteBuffer.get(encryptedByte)

        return DekryptertByteArray(initCipher(Cipher.DECRYPT_MODE, getSecretKey(salt), iv).doFinal(encryptedByte))
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

@JvmInline
value class DekryptertByteArray(val byteArray: ByteArray)

@JvmInline
value class KryptertByteArray(val byteArray: ByteArray)