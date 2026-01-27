package no.nav.pensjon.brev.skribenten.usecase

import no.nav.brev.Landkode
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NorskPostnummer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EndreMottakerHandlerTest : BrevredigeringTest() {

    private suspend fun endreMottaker(brevId: Long, mottaker: Dto.Mottaker?, principal: UserPrincipal = saksbehandler1Principal) =
        withPrincipal(principal) {
            brevredigeringFacade.endreMottaker(EndreMottakerHandler.Request(brevId, mottaker))
        }

    @Test
    suspend fun `kan fjerne overstyrt mottaker av brev`() {
        val mottaker = Dto.Mottaker.samhandler("samhandlerId")
        val brev = opprettBrev(mottaker = mottaker).resultOrFail()
        assertThat(brev.info.mottaker).isEqualTo(mottaker)

        val resultat = endreMottaker(brev.info.id, null)
        assertThat(resultat).isSuccess {
            assertThat(it.info.mottaker).isNull()
        }
    }

    @Test
    suspend fun `kan oppdatere mottaker av brev`() {
        val brev = opprettBrev(mottaker = Dto.Mottaker.samhandler("1")).resultOrFail()
        val nyMottaker = Dto.Mottaker.norskAdresse(
            navn = "a",
            postnummer = NorskPostnummer("1234"),
            poststed = "c",
            adresselinje1 = "d",
            adresselinje2 = "e",
            adresselinje3 = "f",
            manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.BRUKER
        )

        val resultat = endreMottaker(brev.info.id, nyMottaker)
        assertThat(resultat).isSuccess {
            assertThat(it.info.mottaker).isEqualTo(nyMottaker)
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isNull()
        }
    }

    @Test
    suspend fun `oppdaterer annenMottakerNavn i redigertBrev når mottaker endres`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(brev.redigertBrev.sakspart.annenMottakerNavn).isNull()

        val (samhandlerId, samhandlerNavn) = samhandlerService.navn.entries.first()
        val nyMottaker = Dto.Mottaker.samhandler(samhandlerId)

        val resultat = endreMottaker(brev.info.id, nyMottaker)
        assertThat(resultat).isSuccess {
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isEqualTo(samhandlerNavn)
        }
    }

    @Test
    suspend fun `kan ikke endre mottaker når brevet er reservert av annen bruker`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        val resultat = endreMottaker(brev.info.id, Dto.Mottaker.samhandler("2"), saksbehandler2Principal)
        assertThat(resultat).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `kan sette annen mottaker for eksisterende brev`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(brev.redigertBrev.sakspart.annenMottakerNavn).isNull()

        val nyMottaker = Dto.Mottaker.utenlandskAdresse(
            navn = "Reci Pient",
            adresselinje1 = "b",
            adresselinje2 = "c",
            adresselinje3 = "d",
            landkode = Landkode("CY"),
            manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.ANNEN
        )

        val resultat = endreMottaker(brev.info.id, nyMottaker)
        assertThat(resultat).isSuccess {
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isEqualTo(nyMottaker.navn)
        }
    }

    @Test
    suspend fun `kan ikke endre mottaker for arkivert brev`() {
        val brev = opprettBrev().resultOrFail()
        arkiverBrev(brev)

        val resultat = endreMottaker(brev.info.id, Dto.Mottaker.samhandler("2"))
        assertThat(resultat).isFailure<RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev, _, _>()
    }

    @Test
    suspend fun `fjerning av mottaker setter også annenMottakerNavn til null`() {
        val mottaker = Dto.Mottaker.norskAdresse(
            navn = "Anon Y. Mouse",
            postnummer = NorskPostnummer("0001"),
            poststed = "Andeby",
            adresselinje1 = "Andebyveien 1",
            adresselinje2 = null,
            adresselinje3 = null,
            manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.ANNEN
        )
        val brev = opprettBrev(mottaker = mottaker).resultOrFail()
        assertThat(brev.redigertBrev.sakspart.annenMottakerNavn).isEqualTo(mottaker.navn)

        val resultat = endreMottaker(brev.info.id, null)
        assertThat(resultat).isSuccess {
            assertThat(it.info.mottaker).isNull()
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isNull()
        }
    }
}