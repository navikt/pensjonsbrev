package no.nav.pensjon.brev.skribenten.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import no.nav.pensjon.brev.skribenten.MockPrincipal
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.updateEditedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.junit.jupiter.api.Test
import java.util.function.Predicate

class HentBrevHandlerTest : BrevredigeringTest() {

    @Test
    suspend fun `kan hente brev uten reservasjon`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val opprettet = opprettBrev(reserverForRedigering = false, saksbehandlerValg = saksbehandlerValg).resultOrFail()

        brevbakerService.renderMarkupKall.clear()

        val hentet = hentBrev(brevId = opprettet.info.id, reserverForRedigering = false)

        assertThat(brevbakerService.renderMarkupKall).isEmpty()
        assertThat(hentet).isSuccess {
            assertThat(it.info.id).isEqualTo(opprettet.info.id)
            assertThat(it.saksbehandlerValg).isEqualTo(saksbehandlerValg)
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev)
        }
    }

    @Test
    suspend fun `kan hente brev med reservasjon`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val opprettet = opprettBrev(reserverForRedigering = false, saksbehandlerValg = saksbehandlerValg).resultOrFail()

        brevbakerService.renderMarkupKall.clear()

        val hentet = hentBrev(brevId = opprettet.info.id, reserverForRedigering = true)

        assertThat(brevbakerService.renderMarkupKall.first()).isEqualTo(Testbrevkoder.INFORMASJONSBREV to LanguageCode.ENGLISH)
        assertThat(brevbakerService.renderMarkupKall.size).isEqualTo(1)

        assertThat(hentet).isSuccess {
            assertThat(it.info.id).isEqualTo(opprettet.info.id)
            assertThat(it.saksbehandlerValg).isEqualTo(saksbehandlerValg)
            assertThat(it.redigertBrev).isEqualTo(letter.toEdit())
            assertThat(it.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }

    @Test
    suspend fun `hent brev med reservasjon merger inn rendret brev`() {
        val opprettet = opprettBrev(reserverForRedigering = false).resultOrFail()
        val freshRender = letter.copy(
            blocks = letter.blocks + ParagraphImpl(2, true, listOf(VariableImpl(21, "ny paragraph")))
        )
        brevbakerService.renderMarkupResultat = { freshRender }

        val hentet = hentBrev(brevId = opprettet.info.id, reserverForRedigering = true)

        assertThat(hentet).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(opprettet.redigertBrev.updateEditedLetter(freshRender))
        }
    }

    @Test
    suspend fun `returnerer null når brev ikke finnes`() {
        val hentet = hentBrev(brevId = 9999, reserverForRedigering = false)

        assertThat(hentet).isNull()
    }

    @Test
    suspend fun `allerede reservert brev kan ikke resereveres for redigering`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        val hentet = hentBrev(
            brevId = brev.info.id,
            reserverForRedigering = true,
            principal = saksbehandler2Principal
        )
        assertThat(hentet).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _> {
            assertThat(it.eksisterende.reservertAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }


    @Test
    suspend fun `kun en som vinner reservasjon av et brev`() {
        val brev = opprettBrev().resultOrFail()

        brevbakerService.renderMarkupResultat = {
            delay(100)
            letter
        }

        val hentBrev = coroutineScope {
            (0..10).map {
                async(Dispatchers.IO) {
                    hentBrev(
                        principal = MockPrincipal(NavIdent("id-$it"), "saksbehandler-id-$it"),
                        brevId = brev.info.id,
                        reserverForRedigering = true,
                    )
                }
            }
        }
        val awaited = hentBrev.awaitAll().filterIsInstance<Outcome<Dto.Brevredigering, BrevredigeringError>>()
        assertThat(awaited).hasSize(hentBrev.size)

//        val redigeresFaktiskAv = transaction { BrevredigeringEntity[brev.info.id].redigeresAv }!!

        assertThat(awaited).areExactly(1, condition("Vellykkede hentBrev med reservasjon") { it.isSuccess })
        assertThat(awaited).areExactly(
            awaited.size - 1,
            condition("Feilende hentBrev med reservasjon") { it.isFailure },
        )
//        assertThat(awaited).allMatch {
//            it.isFailure || it.getOrNull()?.info?.redigeresAv == redigeresFaktiskAv
//        }
    }

    private fun <T> condition(description: String, predicate: Predicate<T>): Condition<T> =
        Condition(predicate, description)
}
