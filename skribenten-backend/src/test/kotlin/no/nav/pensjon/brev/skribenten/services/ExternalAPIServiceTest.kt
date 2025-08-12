package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.ConfigValueFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class ExternalAPIServiceTest {

    private val skribentenWebUrl = "https://our-cool-url"
    val saksId = 1L
    val brevDto = Dto.BrevInfo(
        id = 2L,
        saksId = saksId,
        vedtaksId = null,
        opprettetAv = NavIdent("Sakson"),
        opprettet = Instant.now(),
        sistredigertAv = NavIdent("Sakson"),
        sistredigert = Instant.now(),
        redigeresAv = null,
        sistReservert = null,
        brevkode = Testbrevkoder.INFORMASJONSBREV,
        laastForRedigering = false,
        distribusjonstype = Distribusjonstype.SENTRALPRINT,
        mottaker = null,
        avsenderEnhetId = "0001",
        spraak = LanguageCode.BOKMAL,
        journalpostId = null,
        attestertAv = null,
        status = Dto.BrevStatus.KLADD
    )
    val brevmal = TemplateDescription.Redigerbar(
        name = Testbrevkoder.INFORMASJONSBREV.kode(),
        letterDataClass = "a.class",
        languages = listOf(),
        metadata = LetterMetadata(
            "Informasjonsbrev",
            false,
            LetterMetadata.Distribusjonstype.ANNET,
            LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
        kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV,
        brevkontekst = TemplateDescription.Brevkontekst.SAK,
        sakstyper = emptySet(),
    )
    private val externalAPIService = ExternalAPIService(
        config = ConfigValueFactory.fromMap(mapOf("skribentenWebUrl" to skribentenWebUrl)).toConfig(),
        brevredigeringService = mockk<BrevredigeringService> {
            coEvery { hentBrevForAlleSaker(eq(setOf(saksId))) } returns listOf(brevDto)
        },
        brevbakerService = mockk<BrevbakerService> {
            coEvery { getRedigerbarTemplate(Testbrevkoder.INFORMASJONSBREV) } returns brevmal
        },
    )


    @Test
    fun `legger til url for aa aapne brev i skribenten`(): Unit = runBlocking {
        val brev = externalAPIService.hentAlleBrevForSaker(setOf(saksId)).single()
        assertThat(brev.url).startsWith(skribentenWebUrl).endsWith("/${brev.id}")
    }
}