package no.nav.pensjon.brev.skribenten.brevredigering.application.redigering
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase

import no.nav.brev.BrevLandmodell.Landkode
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Adresselinje
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Navn
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Poststed
import no.nav.pensjon.brev.skribenten.brevredigering.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.TssId
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NorskPostnummer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class EndreMottakerHandlerTest : BrevredigeringHandlerTestBase() {

    private suspend fun endreMottaker(brevId: BrevId, mottaker: Dto.Mottaker?, principal: UserPrincipal = saksbehandler1Principal) =
        withPrincipal(principal) {
            endreMottaker(EndreMottakerHandler.Request(brevId, sak1.saksId, mottaker))
        }

    @Test
    suspend fun `kan fjerne overstyrt mottaker av brev`() {
        val mottaker = Dto.Mottaker.samhandler(TssId("samhandlerId"))
        val brev = opprettBrev(mottaker = mottaker).resultOrFail()
        assertThat(brev.info.mottaker).isEqualTo(mottaker)

        val resultat = endreMottaker(brev.info.id, null)
        assertThat(resultat).isSuccess {
            assertThat(it.mottaker).isNull()
        }
    }

    @Test
    suspend fun `kan oppdatere mottaker av brev`() {
        val brev = opprettBrev(mottaker = Dto.Mottaker.samhandler(TssId("1"))).resultOrFail()
        val nyMottaker = Dto.Mottaker.norskAdresse(
            navn = Navn("a"),
            postnummer = NorskPostnummer("1234"),
            poststed = Poststed("c"),
            adresselinje1 = Adresselinje("d"),
            adresselinje2 = Adresselinje("e"),
            adresselinje3 = Adresselinje("f"),
            manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.BRUKER
        )

        val resultat = endreMottaker(brev.info.id, nyMottaker)
        assertThat(resultat).isSuccess {
            assertThat(it.mottaker).isEqualTo(nyMottaker)
        }
        assertThat(hentBrev(brev.info.id)).isSuccess {
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isNull()
        }
    }

    @Test
    suspend fun `oppdaterer annenMottakerNavn i redigertBrev når mottaker endres`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(brev.redigertBrev.sakspart.annenMottakerNavn).isNull()

        val (samhandlerId, samhandlerNavn) = samhandlerService.navn.entries.first()
        val nyMottaker = Dto.Mottaker.samhandler(samhandlerId)

        assertThat(endreMottaker(brev.info.id, nyMottaker)).isSuccess()
        assertThat(hentBrev(brev.info.id)).isSuccess {
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isEqualTo(samhandlerNavn)
        }
    }

    @Test
    suspend fun `annenMottakerNavn i redigertBrev kommer fra pesysData om det ikke er overstyrt`() {
        penService.pesysBrevdata = brevdataResponseData.copy(felles = brevdataResponseData.felles.medAnnenMottakerNavn("Pesys Mottaker"))

        val brev = opprettBrev()
        assertThat(brev).isSuccess {
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isEqualTo("Pesys Mottaker")
        }
    }

    @Test
    suspend fun `annenMottakerNavn i redigertBrev som kommer fra pesysData blir overstyrt`() {
        penService.pesysBrevdata = brevdataResponseData.copy(felles = brevdataResponseData.felles.medAnnenMottakerNavn("Pesys Mottaker"))

        val brev = opprettBrev().resultOrFail()
        assertThat(brev.redigertBrev.sakspart.annenMottakerNavn).isEqualTo("Pesys Mottaker")

        val (samhandlerId, samhandlerNavn) = samhandlerService.navn.entries.first()
        val nyMottaker = Dto.Mottaker.samhandler(samhandlerId)

        assertThat(endreMottaker(brev.info.id, nyMottaker)).isSuccess()
        assertThat(hentBrev(brev.info.id)).isSuccess {
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isEqualTo(samhandlerNavn)
        }
    }

    @Test
    suspend fun `kan ikke endre mottaker når brevet er reservert av annen bruker`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        val resultat = endreMottaker(brev.info.id, Dto.Mottaker.samhandler(TssId("2")), saksbehandler2Principal)
        assertThat(resultat).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `kan sette annen mottaker for eksisterende brev`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(brev.redigertBrev.sakspart.annenMottakerNavn).isNull()

        val nyMottaker = Dto.Mottaker.utenlandskAdresse(
            navn = Navn("Reci Pient"),
            adresselinje1 = Adresselinje("b"),
            adresselinje2 = Adresselinje("c"),
            adresselinje3 = Adresselinje("d"),
            landkode = Landkode("CY"),
            manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.ANNEN
        )

        assertThat(endreMottaker(brev.info.id, nyMottaker)).isSuccess()
        assertThat(hentBrev(brev.info.id)).isSuccess {
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isEqualTo(nyMottaker.navn?.value)
        }
    }

    @Test
    suspend fun `kan ikke endre mottaker for arkivert brev`() {
        val brev = opprettBrev().resultOrFail()
        arkiverBrev(brev)

        val resultat = endreMottaker(brev.info.id, Dto.Mottaker.samhandler(TssId("2")))
        assertThat(resultat).isFailure<RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev, _, _>()
    }

    @Test
    suspend fun `fjerning av mottaker setter også annenMottakerNavn til null`() {
        val mottaker = Dto.Mottaker.norskAdresse(
            navn = Navn("Anon Y. Mouse"),
            postnummer = NorskPostnummer("0001"),
            poststed = Poststed("Andeby"),
            adresselinje1 = Adresselinje("Andebyveien 1"),
            adresselinje2 = null,
            adresselinje3 = null,
            manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.ANNEN
        )
        val brev = opprettBrev(mottaker = mottaker).resultOrFail()
        assertThat(brev.redigertBrev.sakspart.annenMottakerNavn).isEqualTo(mottaker.navn?.value)

        assertThat(endreMottaker(brev.info.id, null)).isSuccess {
            assertThat(it.mottaker).isNull()
        }
        assertThat(hentBrev(brev.info.id)).isSuccess {
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isNull()
        }
    }
}