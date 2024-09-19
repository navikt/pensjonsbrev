package no.nav.pensjon.brev.skribenten.services

import io.ktor.server.application.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Distribusjonstype
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Instant

class ApiServiceTest {
    private val saksbehandler = NavIdent("Z123")

    private val navansattService = mockk<NavansattService>()
    private val norg2Service = mockk<Norg2Service>()
    private val samhandlerService = mockk<SamhandlerService>()
    private val apiService = ApiService(
        brevbakerService = mockk {
            coEvery { getRedigerbarTemplate(any(), eq(Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID)) } returns TemplateDescription(
                name = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID.name,
                letterDataClass = InformasjonOmSaksbehandlingstidDto::class.java.name,
                languages = listOf(LanguageCode.BOKMAL),
                metadata = LetterMetadata(
                    "Informasjon om saksbehandlingstid",
                    false,
                    LetterMetadata.Distribusjonstype.VIKTIG,
                    LetterMetadata.Brevtype.INFORMASJONSBREV
                ),
                kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV,
            )
        },
        navansattService = navansattService,
        norg2Service = norg2Service,
        samhandlerService = samhandlerService,
    )

    private val mockCall = mockk<ApplicationCall>()

    @BeforeEach
    fun stage() {
        stageAnsatt(saksbehandler, "Saksbehandler", "Saksbehandlersen")
    }

    @Test
    fun `status er kladd om brev ikke er laast og ikke redigeres`(): Unit = runBlocking {
        val brev = createBrev(redigeresAv = null, laastForRedigering = false)
        assertThat(apiService.toApi(mockCall, brev).status).isEqualTo(Api.BrevStatus.Kladd)
    }

    @Test
    fun `status er Klar om brev er laast`(): Unit = runBlocking {
        val brev = createBrev(laastForRedigering = true)
        assertThat(apiService.toApi(mockCall, brev).status).isEqualTo(Api.BrevStatus.Klar)
    }

    @Test
    fun `status er UnderRedigering om brev ikke er laast og er reservert for redigering`(): Unit = runBlocking {
        val redigeresAv = NavIdent("Z99")
        val brev = createBrev(redigeresAv = redigeresAv, laastForRedigering = false)
        stageAnsatt(redigeresAv, "Annen", "Saksbehandler")
        assertThat(apiService.toApi(mockCall, brev).status).isEqualTo(
            Api.BrevStatus.UnderRedigering(
                Api.NavAnsatt(
                    redigeresAv ,
                    "Annen Saksbehandler"
                )
            )
        )
    }

    @Test
    fun `henter navn for opprettetAv og sistredigertAv`(): Unit = runBlocking {
        val opprettetAv = NavIdent("Z100")
        val sistredigertAv = NavIdent("Z101")
        stageAnsatt(opprettetAv, "Opprettet", "Av")
        stageAnsatt(sistredigertAv, "Sist Redigert", "Av")

        val result = apiService.toApi(mockCall, createBrev(opprettetAv = opprettetAv, sistredigertAv = sistredigertAv))

        assertThat(result.opprettetAv).isEqualTo(Api.NavAnsatt(opprettetAv, "Opprettet Av"))
        assertThat(result.sistredigertAv).isEqualTo(Api.NavAnsatt(sistredigertAv, "Sist Redigert Av"))
    }

    @Test
    fun `henter brevtittel fra brevbaker`(): Unit = runBlocking {
        val brev = createBrev()
        assertThat(apiService.toApi(mockCall, brev).brevtittel).isEqualTo("Informasjon om saksbehandlingstid")
    }

    @Test
    fun `henter navn på avsenderEnhet`(): Unit = runBlocking {
        val brev = createBrev(avsenderEnhetId = "1234")
        coEvery { norg2Service.getEnhet(any(), eq("1234")) } returns NAVEnhet("1234", "En kul enhet")
        assertThat(apiService.toApi(mockCall, brev).avsenderEnhet).isEqualTo(NAVEnhet("1234", "En kul enhet"))
    }

    @Test
    fun `henter navn paa samhandler mottaker`(): Unit = runBlocking {
        val brev = createBrev(mottaker = Dto.Mottaker.samhandler("tss123"))
        coEvery { samhandlerService.hentSamhandlerNavn(any(), eq("tss123")) } returns "Verdens kuleste samhandler"
        assertThat(apiService.toApi(mockCall, brev).mottaker).isEqualTo(Api.OverstyrtMottaker.Samhandler("tss123", "Verdens kuleste samhandler"))
    }

    private fun createBrev(
        opprettetAv: NavIdent = saksbehandler,
        sistredigertAv: NavIdent = saksbehandler,
        redigeresAv: NavIdent? = null,
        laastForRedigering: Boolean = false,
        avsenderEnhetId: String? = null,
        mottaker: Dto.Mottaker? = null,
    ) = Dto.BrevInfo(
        id = 1,
        saksId = 11,
        opprettetAv = opprettetAv,
        opprettet = Instant.now(),
        sistredigertAv = sistredigertAv,
        sistredigert = Instant.now(),
        redigeresAv = redigeresAv,
        sistReservert = Instant.now(),
        brevkode = Brevkode.Redigerbar.INFORMASJON_OM_SAKSBEHANDLINGSTID,
        laastForRedigering = laastForRedigering,
        distribusjonstype = Distribusjonstype.SENTRALPRINT,
        mottaker = mottaker,
        avsenderEnhetId = avsenderEnhetId,
        spraak = LanguageCode.BOKMAL,
        signaturSignerende = "Z 123",
    )

    private fun stageAnsatt(id: NavIdent, fornavn: String, etternavn: String) {
        coEvery { navansattService.hentNavansatt(any(), eq(id.id)) } returns Navansatt(emptyList(), "$fornavn $etternavn", fornavn, etternavn)
    }
}