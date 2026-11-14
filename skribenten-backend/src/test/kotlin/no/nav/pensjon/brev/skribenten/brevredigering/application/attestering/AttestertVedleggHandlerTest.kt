package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.brev.brevbaker.StableHashCode
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.EndreRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.HentRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.RedigerbartVedleggInfo
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VedleggFinnesIkkeIMal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertVedlegg
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertVedleggMarkup
import no.nav.pensjon.brev.skribenten.brevredigering.domain.blockIds
import no.nav.pensjon.brev.skribenten.brevredigering.domain.foersteLiteralEditedText
import no.nav.pensjon.brev.skribenten.brevredigering.domain.medRedigertTekst
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2
import no.nav.pensjon.brev.skribenten.letter.v2.attachment as attachmentV2
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class AttestertVedleggHandlerTest : BrevredigeringHandlerTestBase() {

    private val vedlegg1 = VedleggId("vedlegg1")

    @BeforeEach
    fun stageV2() = stageMarkupV2()

    private fun malVedlegg(versjon: MarkupVersjon, vararg avsnitt: Pair<Int, String>, tittel: String = "Mal tittel"): VersjonertVedleggMarkup =
        when (versjon) {
            MarkupVersjon.V1 -> VersjonertVedleggMarkup.V1(
                LetterMarkupImpl.AttachmentImpl(
                    title = listOf(LiteralImpl(1, tittel)),
                    blocks = avsnitt.map { (id, tekst) -> ParagraphImpl(id, true, listOf(LiteralImpl(id * 10, tekst))) },
                    includeSakspart = false,
                    stableHashCode = StableHashCode(avsnitt.map { it.first }.hashCode()),
                )
            )
            MarkupVersjon.V2 -> VersjonertVedleggMarkup.V2(
                attachmentV2(
                    *avsnitt.map { (id, tekst) -> MarkupV2.paragraph(id, MarkupV2.literal(id * 10, tekst)) }.toTypedArray(),
                    title = tittel,
                )
            )
        }

    private fun lagretVedlegg(brevId: BrevId): VersjonertVedlegg =
        transaction { BrevredigeringEntity[brevId].hentRedigertVedlegg(vedlegg1)!! }

    private fun stagMal(versjon: MarkupVersjon, vararg avsnitt: Pair<Int, String>, tittel: String = "Mal tittel") {
        brevbakerService.redigerbareVedlegg = when (val mal = malVedlegg(versjon, *avsnitt, tittel = tittel)) {
            is VersjonertVedleggMarkup.V1 -> RedigerbareVedlegg.V1(mapOf(vedlegg1 to mal.markup))
            is VersjonertVedleggMarkup.V2 -> RedigerbareVedlegg.V2(mapOf(vedlegg1 to mal.markup))
        }
    }

    private suspend fun klartVedtaksbrevMedRedigertVedlegg(versjon: MarkupVersjon, redigertTekst: String = "Redigert"): Dto.Brevredigering {
        stagMal(versjon, 1 to "Foerste")
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234), markupVersjon = versjon).resultOrFail()

        val fraMalen = withPrincipal(saksbehandler1Principal) {
            hentRedigertVedlegg(HentRedigertVedleggHandler.Request(brevId = brev.info.id, saksId = sak1.saksId, vedleggId = vedlegg1))
        }.resultOrFail()

        assertThat(
            withPrincipal(saksbehandler1Principal) {
                endreRedigertVedlegg(
                    EndreRedigertVedleggHandler.Request(
                        brevId = brev.info.id,
                        saksId = sak1.saksId,
                        vedleggId = vedlegg1,
                        redigertVedlegg = fraMalen.medRedigertTekst(redigertTekst),
                    )
                )
            }
        ).isSuccess()

        veksleKlarStatus(brev, klar = true).resultOrFail()
        return brev
    }

    private suspend fun hentBrevAttestering(
        brevId: BrevId,
        reserverForRedigering: Boolean,
        attestant: UserPrincipal = attestant1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? = withPrincipal(attestant) {
        hentBrevAttestering(
            HentBrevAttesteringHandler.Request(brevId = brevId, saksId = sak1.saksId, reserverForRedigering = reserverForRedigering)
        )
    }

    private suspend fun hentVedleggForAttestering(
        brevId: BrevId,
        vedleggId: VedleggId = vedlegg1,
        attestant: UserPrincipal = attestant1Principal,
    ): Outcome<VersjonertVedlegg, BrevredigeringError>? = withPrincipal(attestant) {
        hentRedigertVedleggAttestering(
            HentRedigertVedleggAttesteringHandler.Request(brevId = brevId, saksId = sak1.saksId, vedleggId = vedleggId)
        )
    }

    private suspend fun hentVedleggstitlerForAttestering(
        brevId: BrevId,
        attestant: UserPrincipal = attestant1Principal,
    ): Outcome<List<RedigerbartVedleggInfo>, BrevredigeringError>? = withPrincipal(attestant) {
        hentRedigerbareVedleggAttestering(
            HentRedigerbareVedleggAttesteringHandler.Request(brevId = brevId, saksId = sak1.saksId)
        )
    }

    private suspend fun lagreVedleggSomAttestant(
        brevId: BrevId,
        redigertVedlegg: VersjonertVedlegg,
        vedleggId: VedleggId = vedlegg1,
        attestant: UserPrincipal = attestant1Principal,
        frigiReservasjon: Boolean = false,
    ): Outcome<VersjonertVedlegg, BrevredigeringError>? = withPrincipal(attestant) {
        lagreAttestertVedlegg(
            LagreAttestertVedleggHandler.Request(
                brevId = brevId,
                saksId = sak1.saksId,
                vedleggId = vedleggId,
                redigertVedlegg = redigertVedlegg,
                frigiReservasjon = frigiReservasjon,
            )
        )
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `henting av vedlegg for attestering merger ikke inn malendringer`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrevMedRedigertVedlegg(versjon)
        stagMal(versjon, 1 to "Foerste", 2 to "Andre")

        assertThat(hentVedleggForAttestering(brev.info.id)).isSuccess {
            assertThat(it.blockIds()).containsExactly(1)
            assertThat(it.foersteLiteralEditedText()).isEqualTo("Redigert")
        }
        assertThat(lagretVedlegg(brev.info.id).blockIds()).containsExactly(1)
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `henting av vedlegg for attestering faller tilbake til malen naar vedlegget ikke er overstyrt`(versjon: MarkupVersjon) {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234), markupVersjon = versjon).resultOrFail()
        stagMal(versjon, 1 to "Foerste")
        veksleKlarStatus(brev, klar = true).resultOrFail()

        assertThat(hentVedleggForAttestering(brev.info.id)).isSuccess {
            assertThat(it).isEqualTo(malVedlegg(versjon, 1 to "Foerste").toEdit())
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `henting av vedlegg for attestering gir feil naar vedlegget ikke finnes i malen`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrevMedRedigertVedlegg(versjon)

        assertThat(hentVedleggForAttestering(brev.info.id, vedleggId = VedleggId("finnesIkke")))
            .isFailure<VedleggFinnesIkkeIMal, _, _> {
                assertThat(it.vedleggId).isEqualTo(VedleggId("finnesIkke"))
            }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `attestantens lagring merger ikke inn malendringer`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrevMedRedigertVedlegg(versjon)
        val lagret = lagretVedlegg(brev.info.id)
        stagMal(versjon, 1 to "Foerste", 2 to "Andre")

        assertThat(lagreVedleggSomAttestant(brev.info.id, lagret.medRedigertTekst("Attestantens tekst"))).isSuccess {
            assertThat(it.blockIds()).containsExactly(1)
            assertThat(it.foersteLiteralEditedText()).isEqualTo("Attestantens tekst")
        }
        assertThat(lagretVedlegg(brev.info.id).foersteLiteralEditedText()).isEqualTo("Attestantens tekst")
        assertThat(lagretVedlegg(brev.info.id).blockIds()).containsExactly(1)
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `attestanten frigir reservasjonen naar frigiReservasjon er true`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrevMedRedigertVedlegg(versjon)
        val lagret = lagretVedlegg(brev.info.id)

        assertThat(lagreVedleggSomAttestant(brev.info.id, lagret, frigiReservasjon = false)).isSuccess()
        assertThat(hentBrevInfo(brev.info.id).resultOrFail().redigeresAv).isEqualTo(attestant1Principal.navIdent)

        assertThat(lagreVedleggSomAttestant(brev.info.id, lagret, frigiReservasjon = true)).isSuccess()
        assertThat(hentBrevInfo(brev.info.id).resultOrFail().redigeresAv).isNull()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `attestanten kan ikke lagre vedlegg paa et informasjonsbrev`(versjon: MarkupVersjon) {
        stagMal(versjon, 1 to "Foerste")
        val brev = opprettBrev(markupVersjon = versjon).resultOrFail()

        assertThat(lagreVedleggSomAttestant(brev.info.id, malVedlegg(versjon, 1 to "Foerste").toEdit()))
            .isFailure<AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereInformasjonsbrev, _, _>()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `henting av vedleggstitler for attestering merger ikke lagrede vedlegg`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrevMedRedigertVedlegg(versjon)
        stagMal(versjon, 1 to "Foerste", 2 to "Andre", tittel = "Ny maltittel")

        assertThat(hentVedleggstitlerForAttestering(brev.info.id)).isSuccess {
            assertThat(it).hasSize(1)
            assertThat(it.first().tittel).isEqualTo("Mal tittel")
        }
        assertThat(lagretVedlegg(brev.info.id).blockIds()).containsExactly(1)
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `henting av brev for attestering merger ikke lagrede vedlegg`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrevMedRedigertVedlegg(versjon)
        stagMal(versjon, 1 to "Foerste", 2 to "Andre")

        assertThat(hentBrevAttestering(brev.info.id, reserverForRedigering = true)).isSuccess()

        assertThat(lagretVedlegg(brev.info.id).blockIds()).containsExactly(1)
        assertThat(lagretVedlegg(brev.info.id).foersteLiteralEditedText()).isEqualTo("Redigert")
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `attestering merger ikke lagrede vedlegg`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrevMedRedigertVedlegg(versjon)
        stagMal(versjon, 1 to "Foerste", 2 to "Andre")

        assertThat(attester(brev)).isSuccess()

        assertThat(lagretVedlegg(brev.info.id).blockIds()).containsExactly(1)
        assertThat(lagretVedlegg(brev.info.id).foersteLiteralEditedText()).isEqualTo("Redigert")
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `attestering-pdf varsler ikke om utdatert vedlegg`(versjon: MarkupVersjon) {
        val brev = klartVedtaksbrevMedRedigertVedlegg(versjon)
        assertThat(hentEllerOpprettAttesteringPdf(brev)).isSuccess()

        stagMal(versjon, 1 to "Foerste", 2 to "Andre")
        penService.pesysBrevdata = brevdataResponseData.copy(brevdata = Api.GeneriskBrevdata().also { it["a"] = "b" })

        assertThat(hentEllerOpprettAttesteringPdf(brev)).isSuccess {
            assertThat(it.rendretBrevErEndret).isFalse()
        }
    }
}
