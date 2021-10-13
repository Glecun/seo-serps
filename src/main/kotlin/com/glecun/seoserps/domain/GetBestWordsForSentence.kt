package com.glecun.seoserps.domain

import com.glecun.seoserps.domain.entity.Words
import com.glecun.seoserps.domain.port.GooglePort
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GetBestWordsForSentence @Autowired constructor(val googlePort: GooglePort){
    operator fun invoke(sentence: String): Array<Words> {
        googlePort.getBestSitesForRequest(sentence)
        return emptyArray()
    }
}