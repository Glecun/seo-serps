package com.glecun.seoserps.infrastructure

import com.glecun.seoserps.domain.entity.Site
import com.glecun.seoserps.domain.entity.Sites
import com.glecun.seoserps.domain.port.GooglePort
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class GoogleAdapter(
    @Value("\${google.api.key}") val googleKey: String,
    @Value("\${google.api.cx}") val googleCx: String
) : GooglePort {

    override fun getBestSitesForRequest(request: String): Sites {
        return Sites(
            (khttp
            .get("https://www.googleapis.com/customsearch/v1?key=${googleKey}&cx=${googleCx}&cr=countryFR&q=${request}")
            .jsonObject["items"] as JSONArray)
            .map {
                val connect = Jsoup.connect(readJsonObject(it, "link"))
                if (connect.execute().statusCode() == 200) {
                    connect.get().text() + " " + readJsonObject(it, "snippet")
                } else {
                    readJsonObject(it, "snippet")
                }
            }.map { Site(it) }
        )
    }

    private fun readJsonObject(it: Any?, name: String) = (it as JSONObject)[name] as String


}
