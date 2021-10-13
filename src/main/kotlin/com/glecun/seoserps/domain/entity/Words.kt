package com.glecun.seoserps.domain.entity

data class Words (val words: List<Word>) {
    fun regroupByWords(): Words {
        return Words(
            words.fold(emptyList()) { words, word ->
                val wordToUpdate = words.find { it.word == word.word }
                if (wordToUpdate != null) {
                    words.map { if (it == wordToUpdate) it.increment() else it }
                } else {
                    words.plus(word)
                }
            }
        )
    }

    fun sort(): Words = Words(words.sortedByDescending { it.nb })
}
