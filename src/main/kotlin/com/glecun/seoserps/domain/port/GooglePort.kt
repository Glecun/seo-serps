package com.glecun.seoserps.domain.port

import com.glecun.seoserps.domain.entity.Site

interface GooglePort {

    fun getBestSitesForRequest(request: String) : Array<Site>
}