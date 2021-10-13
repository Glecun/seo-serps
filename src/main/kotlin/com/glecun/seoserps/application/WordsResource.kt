package com.glecun.seoserps.application

import com.glecun.seoserps.domain.GetBestWordsForSentence
import com.glecun.seoserps.domain.entity.Words
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
class WordsResource @Autowired constructor(val getBestWordsForSentence: GetBestWordsForSentence) {

    @GetMapping("/words")
    fun getWords(@RequestParam sentence: String): Array<Words> = getBestWordsForSentence(sentence)

}