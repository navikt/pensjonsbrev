package no.nav.pensjon.brev.skribenten.usecase

import io.ktor.http.*
import no.nav.pensjon.brev.api.model.LetterResponse
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.OpprettBrevPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.initADGroups
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brev.skribenten.services.BrevdataResponse.Data
import no.nav.pensjon.brev.skribenten.services.brev.BrevdataService
import no.nav.pensjon.brev.skribenten.services.brev.RenderService
import no.nav.pensjon.brev.skribenten.usecase.Outcome.Companion.success
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.SignaturImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NavEnhet
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.opentest4j.AssertionFailedError
import java.time.LocalDate

/**
 * TODO: Usikker på om denne foreldreklassen skal være med i det endelige resultatet.
 * Har valgt å gjøre det på denne måten underveis for å understøtte steg-for-steg refaktorering med blanding mellom ny og gammel stil.
 * Felles fixtures tror jeg kommer til å være nyttig uansett, så det er spesielt service-mocks jeg ikke er sikker på.
 * Jeg er også usikker på om sluttresultatet tester helt ned mot database eller ei.
 */
abstract class BrevredigeringTest {

    @BeforeAll
    fun startDbOnce() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }

    @BeforeEach
    fun clearMocks() {
        brevbakerService.renderMarkupKall.clear()
        brevbakerService.renderMarkupResultat = {
            letter.medSignatur(
                saksbehandler = it.signerendeSaksbehandlere?.saksbehandler,
                attestant = it.signerendeSaksbehandlere?.attesterendeSaksbehandler
            )
        }

        brevbakerService.redigerbareMaler[Testbrevkoder.INFORMASJONSBREV] = informasjonsbrev
        brevbakerService.redigerbareMaler[Testbrevkoder.VEDTAKSBREV] = vedtaksbrev
        brevbakerService.redigerbareMaler[Testbrevkoder.VARSELBREV] = varselbrevIVedtakskontekst
        stagePdf(stagetPDF)

        penService.pesysBrevdata = brevdataResponseData
        penService.sendBrevResponse = bestillBrevresponse
    }


    private val navAnsattService = FakeNavansattService(
        harTilgangTilEnhet = mapOf(
            Pair(saksbehandler1Principal.navIdent.id, PRINCIPAL_NAVENHET_ID) to true,
            Pair(saksbehandler2Principal.navIdent.id, PRINCIPAL_NAVENHET_ID) to true
        ),
        navansatte = mapOf(
            saksbehandler1Principal.navIdent.id to saksbehandler1Principal.fullName,
            saksbehandler2Principal.navIdent.id to saksbehandler2Principal.fullName
        )
    )

    protected val brevbakerService = BrevredigeringServiceTest.BrevredigeringFakeBrevbakerService()
    protected val penService = BrevredigeringServiceTest.FakePenService()
    protected val samhandlerService = FakeSamhandlerService(mapOf("samhandler1" to "Sam Handler AS"))

    private val brevredigeringService: BrevredigeringService = BrevredigeringService(
        brevbakerService = brevbakerService,
        navansattService = navAnsattService,
        penService = penService,
        samhandlerService = samhandlerService,
        p1Service = FakeP1Service()
    )
    protected val brevredigeringFacade = BrevredigeringFacade(
        brevbakerService = brevbakerService,
        brevdataService = BrevdataService(penService, samhandlerService),
        navansattService = navAnsattService,
        renderService = RenderService(brevbakerService),
        redigerBrevPolicy = RedigerBrevPolicy(),
        brevreservasjonPolicy = BrevreservasjonPolicy(),
        opprettBrevPolicy = OpprettBrevPolicy(brevbakerService, navAnsattService),
    )

    protected companion object Fixtures {
        init {
            KrypteringService.init("ZBn9yGLDluLZVVGXKZxvnPun3kPQ2ccF")
            initADGroups()
        }

        const val PRINCIPAL_NAVENHET_ID = "Nebuchadnezzar"
        val saksbehandler1Principal = MockPrincipal(NavIdent("Agent Smith"), "Hugo Weaving", setOf(ADGroups.pensjonSaksbehandler))
        val saksbehandler2Principal = MockPrincipal(NavIdent("Morpheus"), "Laurence Fishburne", setOf(ADGroups.pensjonSaksbehandler))
        val attestantPrincipal = MockPrincipal(NavIdent("A12345"), "Peder Ås", mutableSetOf(ADGroups.pensjonSaksbehandler, ADGroups.attestant))

        val sak1 = Pen.SakSelection(
            saksId = 1234L,
            foedselsnr = "12345678910",
            foedselsdato = LocalDate.now().minusYears(42),
            navn = Pen.SakSelection.Navn("a", "b", "c"),
            sakType = Pen.SakType.ALDER,
        )

        val letter = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "red pill"))))
            .medSignatur(saksbehandler = saksbehandler1Principal.fullName, attestant = null)

        val informasjonsbrev = TemplateDescription.Redigerbar(
            name = Testbrevkoder.INFORMASJONSBREV.kode(),
            letterDataClass = "template letter data class",
            languages = listOf(LanguageCode.ENGLISH),
            metadata = LetterMetadata(
                displayTitle = "Et informasjonsbrev",
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            ),
            kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV,
            brevkontekst = TemplateDescription.Brevkontekst.ALLE,
            sakstyper = Sakstype.all,
        )

        val vedtaksbrev = TemplateDescription.Redigerbar(
            name = Testbrevkoder.VEDTAKSBREV.kode(),
            letterDataClass = "template letter data class",
            languages = listOf(LanguageCode.ENGLISH),
            metadata = LetterMetadata(
                displayTitle = "Et vedtaksbrev",
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
            kategori = TemplateDescription.Brevkategori.UFOEREPENSJON,
            brevkontekst = TemplateDescription.Brevkontekst.VEDTAK,
            sakstyper = Sakstype.all,
        )

        private val varselbrevIVedtakskontekst = TemplateDescription.Redigerbar(
            name = Testbrevkoder.VARSELBREV.kode(),
            letterDataClass = "template letter data class",
            languages = listOf(LanguageCode.ENGLISH),
            metadata = LetterMetadata(
                displayTitle = "Et varselbrev",
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            ),
            kategori = TemplateDescription.Brevkategori.VARSEL,
            brevkontekst = TemplateDescription.Brevkontekst.VEDTAK,
            sakstyper = Sakstype.all,
        )

        val stagetPDF = "nesten en pdf".encodeToByteArray()

        val brevdataResponseData = Data(
            felles = Felles(
                dokumentDato = LocalDate.now(),
                saksnummer = sak1.saksId.toString(),
                avsenderEnhet = NavEnhet(
                    nettside = "nav.no",
                    navn = "Nav Familie- og pensjonsytelser Porsgrunn",
                    telefonnummer = Telefonnummer("55553334"),
                ),
                bruker = Bruker(
                    fornavn = "Test",
                    mellomnavn = "\"bruker\"",
                    etternavn = "Testerson",
                    foedselsnummer = Foedselsnummer("01019878910"),
                ),
                signerendeSaksbehandlere = SignerendeSaksbehandlere(
                    saksbehandler = "Ole Saksbehandler",
                    attesterendeSaksbehandler = "Per Attesterende"
                ),
                annenMottakerNavn = null,
            ),
            brevdata = Api.GeneriskBrevdata()
        )

        val bestillBrevresponse = Pen.BestillBrevResponse(123, null)

        fun LetterMarkupImpl.medSignatur(saksbehandler: String?, attestant: String?) =
            copy(
                signatur = (signatur as SignaturImpl).copy(
                    saksbehandlerNavn = saksbehandler,
                    attesterendeSaksbehandlerNavn = attestant
                )
            )
    }

    protected suspend fun opprettBrev(
        principal: UserPrincipal = saksbehandler1Principal,
        reserverForRedigering: Boolean = false,
        mottaker: Dto.Mottaker? = null,
        saksbehandlerValg: SaksbehandlerValg = SaksbehandlerValg().apply { put("valg", true) },
        brevkode: Brevkode.Redigerbart = Testbrevkoder.INFORMASJONSBREV,
        vedtaksId: Long? = null,
        sak: Pen.SakSelection = sak1,
        avsenderEnhetsId: String = PRINCIPAL_NAVENHET_ID,
    ): Outcome<Dto.Brevredigering, BrevredigeringError> = withPrincipal(principal) {
        brevredigeringFacade.opprettBrev(
            OpprettBrevHandler.Request(
                saksId = sak.saksId,
                vedtaksId = vedtaksId,
                brevkode = brevkode,
                spraak = LanguageCode.ENGLISH,
                avsenderEnhetsId = avsenderEnhetsId,
                saksbehandlerValg = saksbehandlerValg,
                reserverForRedigering = reserverForRedigering,
                mottaker = mottaker,
            )
        )
    }

    protected suspend fun oppdaterBrev(
        brevId: Long,
        nyeSaksbehandlerValg: SaksbehandlerValg? = null,
        nyttRedigertbrev: Edit.Letter? = null,
        frigiReservasjon: Boolean = false,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.oppdaterBrev(
            OppdaterBrevHandler.Request(
                brevId = brevId,
                nyeSaksbehandlerValg = nyeSaksbehandlerValg,
                nyttRedigertbrev = nyttRedigertbrev,
                frigiReservasjon = frigiReservasjon
            )
        )
    }

    protected suspend fun attester(
        brev: Dto.Brevredigering,
        attestant: UserPrincipal = attestantPrincipal,
        frigiReservasjon: Boolean = false,
    ) = withPrincipal(attestant) {
        brevredigeringService.attester(
            saksId = brev.info.saksId,
            brevId = brev.info.id,
            frigiReservasjon = frigiReservasjon,
            nyeSaksbehandlerValg = null,
            nyttRedigertbrev = null,
        )
    }

    protected suspend fun veksleKlarStatus(
        brev: Dto.Brevredigering,
        klar: Boolean,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? = withPrincipal(principal) {
        brevredigeringFacade.veksleKlarStatus(
            VeksleKlarStatusHandler.Request(
                brevId = brev.info.id,
                klar = klar
            )
        )
    }

    protected suspend fun arkiverBrev(
        brev: Dto.Brevredigering,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Unit, BrevredigeringError> = withPrincipal(principal) {
        assertThat(hentEllerOpprettPdf(brev)).isNotNull()
        assertThat(veksleKlarStatus(brev, true)).isSuccess()

        penService.sendBrevResponse = Pen.BestillBrevResponse(
            991,
            Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null)
        )
        assertThat(sendBrev(brev)).isNotNull()
        success(Unit)
    }

    /**
     * TODO: Potensielt midlertidig frem til refaktorering er ferdig
     */
    protected suspend fun hentEllerOpprettPdf(brev: Dto.Brevredigering, principal: UserPrincipal = saksbehandler1Principal) =
        withPrincipal(principal) { brevredigeringService.hentEllerOpprettPdf(saksId = brev.info.saksId, brevId = brev.info.id) }

    /**
     * TODO: Potensielt midlertidig frem til refaktorering er ferdig
     */
    protected suspend fun sendBrev(brev: Dto.Brevredigering) =
        brevredigeringService.sendBrev(brev.info.saksId, brev.info.id)

    protected fun stagePdf(pdf: ByteArray) {
        brevbakerService.renderPdfResultat = LetterResponse(
            file = pdf,
            contentType = ContentType.Application.Pdf.toString(),
            letterMetadata = LetterMetadata(
                displayTitle = "En fin tittel",
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
            )
        )
    }

    class ResultFailure(error: BrevredigeringError) :
        AssertionFailedError(null,Outcome.Success::class.java, error::class.java)

    fun <T> Outcome<T, BrevredigeringError>?.resultOrFail(): T = when (this) {
        is Outcome.Success -> value
        is Outcome.Failure -> throw ResultFailure(error)
        null -> throw AssertionError("Resultat var null")
    }
}