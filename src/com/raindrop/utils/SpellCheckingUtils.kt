package com.raindrop.utils

import com.raindrop.cache.WordCache
import com.raindrop.model.WordStorage

/**
 * @name: com.raindrop.utils.SpellCheckingUtils.kt
 * @description Work spell check tool
 * @author Raindrop
 * @create 2019-04-09
 */
open class SpellCheckingUtils {

    companion object {
        private val wordMap = WordCache.getWordCache()
        fun wordSpellIsCorrect(word: String): Boolean {
            return WordCache.getWordCache().containsKey(word)
        }

        fun wordCorrectSingle(word: String): String {
            return wordCorrectList(word, 1).first()
        }

        fun wordCorrectList(word: String, limit: Int): MutableList<String> {
            if (wordMap.containsKey(word.toLowerCase())) {
                return mutableListOf(word)
            }

            var words: MutableList<String> = getRelatedWord(word)
            var wordStorages = mutableListOf<WordStorage>()
            for (word in words) {
                if (wordMap.containsKey(word)) {
                    var wordStorage = WordStorage(word, wordMap[word]!!)
                    wordStorages.add(wordStorage)
                }
            }
            if (wordStorages.size > 0) {
                return getCandidateList(wordStorages, limit)
            }

            for (word in words) {
                for (s in getRelatedWord(word)) {
                    if (wordMap.containsKey(s)) {
                        var wordStorage = WordStorage(word, wordMap.get(word) ?: 0)
                        wordStorages.add(wordStorage)
                    }
                }
            }
            if (wordStorages.size > 0) {
                return getCandidateList(wordStorages, limit)
            }
            return mutableListOf(word)
        }

        private fun getRelatedWord(word: String): MutableList<String> {
            var words = mutableListOf<String>()
            for (i in word.indices) {
                words.add(word.substring(0, i) + word.substring(i + 1))
            }
            for (i in (0..word.length - 2)) {
                words.add(word.substring(0, i) + word.substring(i + 1, i + 2) + word.substring(i, i + 1) + word.substring(i + 2))
            }
            for (i in word.indices) {
                for (c in 'a'..'z') {
                    words.add(word.substring(0, i) + c.toString() + word.substring(i + 1))
                }
            }
            for (i in word.indices) {
                for (c in 'a'..'z') {
                    words.add(word.substring(0, i) + c.toString() + word.substring(i))
                }
            }
            return words
        }

        private fun getCandidateList(data: MutableList<WordStorage>, limit: Int): MutableList<String> {
            var result = mutableListOf<String>()
            //1.排序
            data.sort()
            var index = limit
            if (index > data.size) {
                index = data.size
            }
            for (d in data) {
                if (result.size >= index) {
                    break
                }
                if (result.contains(d.getWord())) {
                    continue
                }
                result.add(d.getWord()!!)
            }
            return result
        }
    }

}
