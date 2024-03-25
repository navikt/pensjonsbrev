package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.natpryce.hamkrest.*
import com.natpryce.hamkrest.assertion.assertThat
import com.typesafe.config.ConfigFactory
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class BrevmetadataServiceTest {

/*    // TODO flytt til brevmalServiceTest
    @Test
    fun `brevmetadata setter redigerbarBrevTittel`() = runBlocking {
        val metadata: List<BrevdataDto> = listOf(
            createData("notat", dokumentType = BrevdataDto.DokumentType.N),
            createData(Brevkoder.FRITEKSTBREV_KODE),
        ) + Brevkoder.ikkeRedigerbarBrevtittel.map { createData(it, dokumentType = BrevdataDto.DokumentType.N) }

        val response = mockResponse(metadata).getRedigerbareBrev(PenService.SakType.ALDER, false)

        assertThat(response, anyElement(harKodeOgRedigerbarTittel(Brevkoder.FRITEKSTBREV_KODE, true)))
        assertThat(response, anyElement(harKodeOgRedigerbarTittel("notat", true)))
        assertThat(response, anyElement(harKodeOgRedigerbarTittel(Brevkoder.POSTERINGSGRUNNLAG_KODE, false)))
        assertThat(response, anyElement(harKodeOgRedigerbarTittel(Brevkoder.POSTERINGSGRUNNLAG_VIRK0101_KODE, false)))
        assertThat(response, anyElement(harKodeOgRedigerbarTittel(Brevkoder.POSTERINGSGRUNNLAG_VIRK0102_KODE, false)))
    }

    private fun mockResponse(brev: List<BrevdataDto>) = BrevmetadataService(
        ConfigFactory.parseMap(mapOf("url" to "http://localhost")),
        MockEngine {
            respond(
                content = ByteReadChannel(jacksonObjectMapper().writeValueAsBytes(brev)),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        },
    )*/

    private fun createData(
        brevkode: String,
        redigerbart: Boolean = true,
        dekode: String = "dekode",
        brevkategoriCode: BrevdataDto.BrevkategoriCode = BrevdataDto.BrevkategoriCode.NOTAT,
        dokumentType: BrevdataDto.DokumentType = BrevdataDto.DokumentType.N,
        spraakKodes: List<SpraakKode> = listOf(SpraakKode.NB),
        brevkontekstCode: BrevdataDto.BrevkontekstCode = BrevdataDto.BrevkontekstCode.ALLTID,
        dokumentkategoriCode: BrevdataDto.DokumentkategoriCode = BrevdataDto.DokumentkategoriCode.B,
        brevSystem: BrevdataDto.BrevSystem = BrevdataDto.BrevSystem.DOKSYS,
    ): BrevdataDto = BrevdataDto(
        redigerbart,
        dekode,
        brevkategoriCode,
        dokumentType,
        spraakKodes,
        false,
        null,
        null,
        null,
        brevkontekstCode,
        dokumentkategoriCode,
        false,
        null,
        brevkode,
        brevSystem,
        null
    )

    private fun harKodeOgRedigerbarTittel(kode: String, erRedigerbar: Boolean) =
        has(LetterMetadata::id, equalTo(kode)) and has(LetterMetadata::redigerbarBrevtittel, equalTo(erRedigerbar))
}