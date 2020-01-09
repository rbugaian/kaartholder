package dev.demilab.kaartholder.util

import android.util.Log
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class Encryption {

    fun encrypt(
        dataToEncrypt: ByteArray,
        password: CharArray,
        salt: ByteArray,
        iv: ByteArray
    ): HashMap<String, ByteArray> {
        val map = HashMap<String, ByteArray>()

        try {
            // 2
            //PBKDF2 - derive the key from the password, don't use passwords directly
            val pbKeySpec = PBEKeySpec(password, salt, 1324, 256)
            val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
            val keySpec = SecretKeySpec(keyBytes, "AES")

            // 3
            //Create initialization vector for AES
            val ivSpec = IvParameterSpec(iv)

            // 4
            //Encrypt
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encrypted = cipher.doFinal(dataToEncrypt)

            // 5
            map["salt"] = salt
            map["iv"] = iv
            map["encrypted"] = encrypted
        } catch (e: Exception) {
            Log.e("MYAPP", "encryption exception", e)
        }

        return map

    }

    fun decrypt(
        encrypted: ByteArray,
        iv: ByteArray,
        salt: ByteArray,
        password: CharArray
    ): ByteArray? {
        var decrypted: ByteArray? = null
        try {
            // 1
            /*val salt = map["salt"]
            val iv = map["iv"]
            val encrypted = map["encrypted"]*/

            // 2
            //regenerate key from password
            val pbKeySpec = PBEKeySpec(password, salt, 1324, 256)
            val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
            val keySpec = SecretKeySpec(keyBytes, "AES")

            // 3
            //Decrypt
            val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            decrypted = cipher.doFinal(encrypted)
        } catch (e: Exception) {
            Log.e("MYAPP", "decryption exception", e)
        }

        return decrypted
    }
}