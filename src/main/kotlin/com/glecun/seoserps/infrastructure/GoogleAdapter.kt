package com.glecun.seoserps.infrastructure

import com.glecun.seoserps.domain.entity.Site
import com.glecun.seoserps.domain.entity.Sites
import com.glecun.seoserps.domain.port.GooglePort
import mu.KotlinLogging
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository
import java.net.URLEncoder
import javax.net.ssl.SSLException

private val logger = KotlinLogging.logger { }

@Repository
class GoogleAdapter(
    @Value("\${google.api.key}") val googleKey: String,
    @Value("\${google.api.cx}") val googleCx: String
) : GooglePort {

    override fun getBestSitesForRequest(request: String): Sites {
        return Sites(
            (khttp
            .get("https://www.googleapis.com/customsearch/v1?key=${googleKey}&cx=${googleCx}&cr=countryFR&q=${URLEncoder.encode(request, "utf-8")}")
            .jsonObject["items"] as JSONArray)
            .map {
                val url = readJsonObject(it, "link")
                retriesIfFails(3, url) {
                    val connect = Jsoup.connect(url).ignoreContentType(true).ignoreHttpErrors(true)
                    val response = connect.execute()
                    if (response.statusCode() == 200 && response.contentType() != "application/pdf") {
                        connect.get().text() + " " + readJsonObject(it, "snippet")
                    } else {
                        readJsonObject(it, "snippet")
                    }
                }
            }.map { Site(it) }
        )
    }

    private fun readJsonObject(it: Any?, name: String) = (it as JSONObject)[name] as String

    private fun retriesIfFails(maxTries: Int, url: String, fn: () -> String): String {
        var count = 0;
        while (true) {
            try {
                return fn()
            } catch (ex : SSLException) {
                if (++count == maxTries) {
                    logger.error(ex) { "Failed after $maxTries retries on: $url"}
                    return ""
                }
            }
        }
    }

}
