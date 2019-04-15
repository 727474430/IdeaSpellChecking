package com.raindrop.utils

import java.security.MessageDigest

/**
 * @name: com.raindrop.utils.StringUtil.kt
 * @description String tool
 * @author Raindrop
 * @create 2019-04-09
 */
private val hexDigits = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
)

/**
 * Generator 32 bit md5
 * @return md5
 */
fun String.md5(): String {
    val md5Digest = with(MessageDigest.getInstance("MD5")) {
        update(toByteArray(Charsets.UTF_8))
        digest()
    }

    val result = CharArray(md5Digest.size * 2)
    md5Digest.forEachIndexed { index, byte ->
        result[index * 2] = hexDigits[byte.toInt() ushr 4 and 0xf]
        result[index * 2 + 1] = hexDigits[byte.toInt() and 0xf]
    }

    return String(result)
}

/**
 * Split string by camel word
 * @return new string combination
 */
fun String.splitByCamelWord(): String {
    var sb = StringBuilder()
    for (index in this.indices) {
        var char = this[index]
        if (char in 'A'..'Z') {
            sb.append(" ")
        }
        sb.append(char)
    }
    return sb.toString()
}