package com.glecun.seoserps.domain

import com.glecun.seoserps.domain.entity.Site
import com.glecun.seoserps.domain.entity.Sites
import com.glecun.seoserps.domain.entity.Word
import com.glecun.seoserps.domain.entity.Words
import com.glecun.seoserps.domain.port.GooglePort
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GetBestWordsForSentenceTest {
    val googlePort : GooglePort = mockk()

    @InjectMockKs
    val getBestWordsForSentence = GetBestWordsForSentence(googlePort)

    @Test
    internal fun `should regroup and sort words`() {
        every { googlePort.getBestSitesForRequest("lol") } returns
                Sites(listOf(Site("mdr mdr lol"), Site("mdr kikou lol")))

        val bestWordsForSentence = getBestWordsForSentence("lol")

        assertEquals(
            bestWordsForSentence,
            Words(
                listOf(
                    Word("mdr", 3),
                    Word("lol", 2),
                    Word("kikou", 1)
                )
            )
        )
    }
}
