package com.raindrop.model

/**
 * @name: com.raindrop.model.WordStorage.kt
 * @description Word storage entity
 * @author Raindrop
 * @create 2019-04-09
 */
class WordStorage: Comparable<WordStorage> {

    /*word key*/
    private var word: String? = null
    /*word count*/
    private var count: Int? = null

    constructor(word: String, count: Int) {
        this.word = word
        this.count = count
    }

    override fun compareTo(other: WordStorage): Int {
        return other.count!! - this.count!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WordStorage

        if (word != other.word) return false
        if (count != other.count) return false
        return true
    }

    override fun hashCode(): Int {
        var result = word?.hashCode() ?: 0
        result = 31 * result + (count ?: 0)
        return result
    }

    override fun toString(): String {
        return "WordStorage(word=$word, count=$count)"
    }

    fun getWord(): String? {
        return this.word
    }

    fun getCount(): Int? {
        return this.count
    }

}