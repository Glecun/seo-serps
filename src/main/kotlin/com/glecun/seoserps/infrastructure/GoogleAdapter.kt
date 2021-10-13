package com.glecun.seoserps.infrastructure

import com.glecun.seoserps.domain.entity.Site
import com.glecun.seoserps.domain.port.GooglePort
import khttp.responses.Response
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class GoogleAdapter(
    @Value("\${google.api.key}") val googleKey: String,
    @Value("\${google.api.cx}") val googleCx: String
) : GooglePort {

    override fun getBestSitesForRequest(request: String): Array<Site> {
        val response = khttp
            .get("https://www.googleapis.com/customsearch/v1?key=${googleKey}&cx=${googleCx}&cr=countryFR&q=${request}")
            .jsonObject

        return emptyArray()
    }


}