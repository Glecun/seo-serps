package com.glecun.seoserps.domain.port

import com.glecun.seoserps.domain.entity.Sites

interface GooglePort {

    fun getBestSitesForRequest(request: String) : Sites
}
