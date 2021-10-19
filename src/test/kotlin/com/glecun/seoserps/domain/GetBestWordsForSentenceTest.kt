package com.glecun.seoserps.domain

import com.glecun.seoserps.domain.entity.Site
import com.glecun.seoserps.domain.entity.Sites
import com.glecun.seoserps.domain.entity.Word
import com.glecun.seoserps.domain.entity.Words
import com.glecun.seoserps.domain.port.GooglePort
import com.glecun.seoserps.domain.port.StopwordsPort
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GetBestWordsForSentenceTest {
    private val googlePort : GooglePort = mockk()
    private val stopwordsPort : StopwordsPort = mockk()

    @InjectMockKs
    val getBestWordsForSentence = GetBestWordsForSentence(googlePort, stopwordsPort)

    @Test
    internal fun `should regroup and sort words`() {
        every { googlePort.getBestSitesForRequest("lol") } returns
                Sites(listOf(Site("mdr mdr lol"), Site("mdr kikou lol")))
        every { stopwordsPort.getStopwords() } returns emptyList()

        val bestWordsForSentence = getBestWordsForSentence("lol", 0)

        assertEquals(
            Words(
                listOf(
                    Word("mdr", 3),
                    Word("lol", 2),
                    Word("kikou", 1)
                )
            ),
            bestWordsForSentence
        )
    }

    @Test
    internal fun `should remove stopwords`() {
        every { googlePort.getBestSitesForRequest("lol") } returns
                Sites(listOf(Site("devenir un cats"), Site("become a cats")))

        every { stopwordsPort.getStopwords() } returns listOf("un", "a")

        val bestWordsForSentence = getBestWordsForSentence("lol", 0)

        assertEquals(
            Words(
                listOf(
                    Word("cats", 2),
                    Word("devenir", 1),
                    Word("become", 1)
                )
            ),
            bestWordsForSentence
        )
    }

    @Test
    internal fun `should sanitize words`() {
        every { googlePort.getBestSitesForRequest("lol") } returns
                Sites(listOf(Site("LoL, M'dr taTa. :")))

        every { stopwordsPort.getStopwords() } returns emptyList()

        val bestWordsForSentence = getBestWordsForSentence("lol", 0)

        assertEquals(
            Words(
                listOf(
                    Word("lol", 1),
                    Word("m'dr", 1),
                    Word("tata", 1)
                )
            ),
            bestWordsForSentence
        )
    }

    @Test
    internal fun `should keep words with multiple occurrences`() {
        every { googlePort.getBestSitesForRequest("lol") } returns
                Sites(listOf(Site("lol toto"), Site("lol tata")))
        every { stopwordsPort.getStopwords() } returns emptyList()

        val bestWordsForSentence = getBestWordsForSentence("lol", 2)

        assertEquals(
            Words(listOf(Word("lol", 2))),
            bestWordsForSentence
        )
    }
}
