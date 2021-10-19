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

    fun removeStopwords(stopwords: List<String>): Words = Words(words.filter { !stopwords.contains(it.word) })

    fun sanitizeWords(): Words {
        return Words(words
            .map { Word(
                    it.word
                        .filter { letter -> letter.isLetter() || letter == '\'' }
                        .lowercase(),
                    it.nb
            )}
            .filter { it.word.isNotBlank() }
        )
    }

    fun keepWordsWithAtLeastNbOccurences(nbOccurrencesToKeepWord: Int): Words =
        Words(words.filter { it.nb >= nbOccurrencesToKeepWord })

    fun sort(): Words = Words(words.sortedByDescending { it.nb })
}
