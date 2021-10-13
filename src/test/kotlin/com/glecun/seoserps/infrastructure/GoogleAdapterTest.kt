package com.glecun.seoserps.infrastructure

import com.glecun.seoserps.domain.entity.Site
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import io.mockk.mockkStatic
import khttp.responses.Response
import org.json.JSONObject
import org.jsoup.Connection
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.Arrays
import java.util.Arrays.asList
import java.util.Collections
import java.util.Collections.*

internal class GoogleAdapterTest {

    @InjectMockKs
    val googleAdapter = GoogleAdapter("googleKey", "googleCx")

    @Test
    fun should_get_best_sites() {
        val request = "lol"
        val json = javaClass.getResource("/google-search.json")?.readText()
        val response = mockk<Response> { every { jsonObject } returns JSONObject(json) }
        mockkStatic("khttp.KHttp")
        every {
            khttp.get("https://www.googleapis.com/customsearch/v1?key=googleKey&cx=googleCx&cr=countryFR&q=${request}")
        } returns response
        mockkStatic("org.jsoup.Jsoup")
        every {
            Jsoup.connect("https://www.leagueofgraphs.com/").get().text()
        } returns "leagueofgraphs text"
        every {
            Jsoup.connect("https://rhinoshield.eu/collections/collab-league-of-legends").get().text()
        } returns "rhinoshield text"

        val bestSitesForRequest = googleAdapter.getBestSitesForRequest(request)

        assertEquals(
            bestSitesForRequest,
            listOf(
                Site("leagueofgraphs text We track the millions of LoL games played every day to gather champion stats, matchups, builds & summoner rankings, as well as champion stats, popularity, ..."),
                Site("rhinoshield text Official League of Legends phone case collection in partnership with Riot Games. Choose your champion and let the battle for Runeterra begin!")
            )
        )

    }

    @Test
    fun should_get_best_sites_even_if_http_error() {
        val request = "lol"
        val json = """{items:[{snippet:"snippet", link:"http://error.com"}]}"""
        val response = mockk<Response> { every { jsonObject } returns JSONObject(json) }
        mockkStatic("khttp.KHttp")
        every {
            khttp.get("https://www.googleapis.com/customsearch/v1?key=googleKey&cx=googleCx&cr=countryFR&q=${request}")
        } returns response
        mockkStatic("org.jsoup.Jsoup")
        val responseJsoup = mockk<Connection.Response> { every { statusCode() } returns 403 }
        every {
            Jsoup.connect("http://error.com").execute()
        } returns responseJsoup

        val bestSitesForRequest = googleAdapter.getBestSitesForRequest(request)

        assertEquals(
            bestSitesForRequest,
            singletonList(Site("snippet"))
        )
    }
}
