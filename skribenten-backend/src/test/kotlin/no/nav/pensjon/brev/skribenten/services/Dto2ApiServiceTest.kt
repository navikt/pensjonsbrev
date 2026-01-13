package no.nav.pensjon.brev.skribenten.services

import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.skribenten.EksempelRedigerbartDto
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant


class Dto2ApiServiceTest {
    private val saksbehandler = NavIdent("Z123")
    private val attestant = NavIdent("A 456")

    private fun lagDto2ApiService(navansattService: NavansattService = FakeNavansattService(), norg2Service: Norg2Service = FakeNorg2Service(), samhandlerService: SamhandlerService = FakeSamhandlerService()): Dto2ApiService =
        Dto2ApiService(
            brevbakerService = FakeBrevbakerService(
                redigerbareMaler = mutableMapOf(Testbrevkoder.TESTBREV to TemplateDescription.Redigerbar(
                    name = Testbrevkoder.TESTBREV.kode(),
                    letterDataClass = EksempelRedigerbartDto::class.java.name,
                    languages = listOf(LanguageCode.BOKMAL),
                    metadata = LetterMetadata(
                        "Redigerbart eksempelbrev",
                        LetterMetadata.Distribusjonstype.VIKTIG,
                        LetterMetadata.Brevtype.INFORMASJONSBREV
                    ),
                    kategori = Pen.Brevkategori.INFORMASJONSBREV,
                    brevkontekst = TemplateDescription.Brevkontekst.ALLE,
                    sakstyper = Sakstype.all,
                ))),
            navansattService = navansattService,
            norg2Service = norg2Service,
            samhandlerService = samhandlerService,
        )

    @Test
    fun `henter navn for opprettetAv og sistredigertAv`(): Unit = runBlocking {
        val opprettetAv = NavIdent("Z100")
        val sistredigertAv = NavIdent("Z101")

        val navansattService = FakeNavansattService(
            navansatte = mapOf(
                saksbehandler.id to "Saksbehandler Saksbehandlersen",
                attestant.id to "Peder AAs",
                opprettetAv.id to "Opprettet Av",
                sistredigertAv.id to "Sist Redigert Av"
            )
        )

        val result = lagDto2ApiService(navansattService).toApi(
            createBrev(
                opprettetAv = opprettetAv,
                sistredigertAv = sistredigertAv
            )
        )

        assertThat(result.opprettetAv).isEqualTo(Api.NavAnsatt(opprettetAv, "Opprettet Av"))
        assertThat(result.sistredigertAv).isEqualTo(Api.NavAnsatt(sistredigertAv, "Sist Redigert Av"))
    }

    @Test
    fun `henter brevtittel fra brevbaker`(): Unit = runBlocking {
        val brev = createBrev()
        assertThat(lagDto2ApiService().toApi(brev).brevtittel).isEqualTo("Redigerbart eksempelbrev")
    }

    @Test
    fun `henter navn paa avsenderEnhet`(): Unit = runBlocking {
        val brev = createBrev(avsenderEnhetId = "1234")
        val norg2Service = FakeNorg2Service(mapOf("1234" to NavEnhet("1234", "En kul enhet")))
        assertThat(lagDto2ApiService(norg2Service = norg2Service).toApi(brev).avsenderEnhet).isEqualTo(NavEnhet("1234", "En kul enhet"))
    }

    @Test
    fun `henter navn paa samhandler mottaker`(): Unit = runBlocking {
        val brev = createBrev(mottaker = Dto.Mottaker.samhandler("tss123"))
        val samhandlerService = FakeSamhandlerService(mapOf("tss123" to "Verdens kuleste samhandler"))
        assertThat(lagDto2ApiService(samhandlerService = samhandlerService).toApi(brev).mottaker).isEqualTo(
            Api.OverstyrtMottaker.Samhandler(
                "tss123",
                "Verdens kuleste samhandler"
            )
        )
    }

    private fun createBrev(
        opprettetAv: NavIdent = saksbehandler,
        sistredigertAv: NavIdent = saksbehandler,
        redigeresAv: NavIdent? = null,
        laastForRedigering: Boolean = false,
        avsenderEnhetId: String? = null,
        mottaker: Dto.Mottaker? = null,
        attestertAv: NavIdent? = null,
    ) = Dto.BrevInfo(
        id = 1,
        saksId = 11,
        vedtaksId = null,
        opprettetAv = opprettetAv,
        opprettet = Instant.now(),
        sistredigertAv = sistredigertAv,
        sistredigert = Instant.now(),
        redigeresAv = redigeresAv,
        sistReservert = Instant.now(),
        brevkode = Testbrevkoder.TESTBREV,
        laastForRedigering = laastForRedigering,
        distribusjonstype = Distribusjonstype.SENTRALPRINT,
        mottaker = mottaker,
        avsenderEnhetId = avsenderEnhetId,
        spraak = LanguageCode.BOKMAL,
        journalpostId = null,
        attestertAv = attestertAv,
        status = Dto.BrevStatus.KLADD
    )
}