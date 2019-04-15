package com.raindrop.cache

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * @name: com.raindrop.cache.WordCache.kt
 * @description Load all word from `word.txt`
 * @author Raindrop
 * @create 2019-04-09
 */
class WordCache {

    companion object {
        // word cache map
        private var wordCache: MutableMap<String, Int> = mutableMapOf()

        fun getWordCache() = wordCache

        // Initialize load `word.txt` and write wordCacheMap
        init {
            var inputStream: InputStream? = null
            var bufferReader: BufferedReader? = null
            try {
                inputStream = this.javaClass.getResourceAsStream("/data/word.txt")
                bufferReader = BufferedReader(InputStreamReader(inputStream))
                var read: String? = null
                while ({ read = bufferReader.readLine(); read }() != null) {
                    wordCache[read!!] = if (wordCache.containsKey(read!!)) wordCache[read!!]!! + 1 else 1
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
                bufferReader?.close()
            }
        }
    }

}