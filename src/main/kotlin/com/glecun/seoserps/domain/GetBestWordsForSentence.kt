package com.glecun.seoserps.domain

import com.glecun.seoserps.domain.entity.Word
import com.glecun.seoserps.domain.entity.Words
import com.glecun.seoserps.domain.port.GooglePort
import com.glecun.seoserps.domain.port.StopwordsPort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetBestWordsForSentence @Autowired constructor(
    val googlePort: GooglePort,
    val stopwordsPort: StopwordsPort,
    ){
    operator fun invoke(sentence: String, nbOccurrencesToKeepWord: Int = 5): Words {
        return googlePort.getBestSitesForRequest(sentence)
            .splitToWords()
            .sanitizeWords()
            .removeStopwords(stopwordsPort.getStopwords())
            .regroupByWords()
            .keepWordsWithAtLeastNbOccurences(nbOccurrencesToKeepWord)
            .sort()
    }
}
