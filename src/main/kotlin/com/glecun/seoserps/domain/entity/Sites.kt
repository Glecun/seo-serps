package com.glecun.seoserps.domain.entity

data class Sites (val sites : List<Site>) {
    fun splitToWords(): Words = Words(
        sites
        .flatMap { it.content.split(" ") }
        .map { Word(it, 1) }
    )
}
