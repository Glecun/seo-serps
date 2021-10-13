package com.glecun.seoserps.application

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class WordsResource {

    @GetMapping("/words")
    fun getWords(): String {
        return ""
    }

}