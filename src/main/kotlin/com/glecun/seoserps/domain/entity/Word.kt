package com.glecun.seoserps.domain.entity

data class Word(val word: String, val nb: Int) {
    fun increment(): Word = Word(word, nb + 1)
}
