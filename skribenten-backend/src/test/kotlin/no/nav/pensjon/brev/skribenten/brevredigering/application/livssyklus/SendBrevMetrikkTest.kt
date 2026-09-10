package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.job
import kotlinx.coroutines.joinAll
import no.nav.brev.BrevLandmodell.Landkode
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.Distribusjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NorskPostnummer
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.FinnSamhandlerRequestDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.FinnSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerAdresseResponseDto
import no.nav.pensjon.brev.skribenten.routes.samhandler.dto.HentSamhandlerResponseDto
import no.nav.pensjon.brev.skribenten.services.SamhandlerService
import no.nav.pensjon.brev.skribenten.services.notYetStubbed
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

// Testbasen bruker per_class-livssyklus: eget registry per test, og sekvensiell kjøring fordi
// arkivert-testen midlertidig endrer delt tilstand i penService.
@Execution(ExecutionMode.SAME_THREAD)
class SendBrevMetrikkTest : BrevredigeringHandlerTestBase() {

    private class Maalinger(val registry: SimpleMeterRegistry, val metrikker: SendtBrevMetrikker, val scope: CoroutineScope) {
        suspend fun ventPaaTelling() = scope.coroutineContext.job.children.toList().joinAll()
    }

    private fun nyeMaalinger(samhandler: SamhandlerService = samhandlerService): Maalinger {
        val registry = SimpleMeterRegistry()
        val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        return Maalinger(registry, SendtBrevMetrikker(samhandler, registry, scope), scope)
    }

    private fun SimpleMeterRegistry.antallSendt(
        mottaker: String,
        samhandlerType: String = SendtBrevMetrikker.IKKE_RELEVANT,
        adressertTil: String = SendtBrevMetrikker.IKKE_RELEVANT,
        distribusjon: Distribusjon = Distribusjon.SENTRALPRINT,
    ): Double =
        find(SendtBrevMetrikker.metricName)
            .tag("mottaker", mottaker)
            .tag("samhandler_type", samhandlerType)
            .tag("adressert_til", adressertTil)
            .tag("distribusjon", distribusjon.name)
            .tag("avsender_enhet", PRINCIPAL_NAVENHET_ID.value)
            .counter()?.count() ?: 0.0

    private fun handlerMed(maalinger: Maalinger) =
        SendBrevHandler(brevtilgang, brevService, brevmalService, maalinger.metrikker)

    private suspend fun sendKlartBrev(
        maalinger: Maalinger,
        mottaker: Dto.Mottaker? = null,
        distribusjonstype: Distribusjon = Distribusjon.SENTRALPRINT,
    ) {
        val brev = opprettBrev(mottaker = mottaker).resultOrFail()
        if (distribusjonstype != Distribusjon.SENTRALPRINT) {
            assertThat(endreDistribusjonstype(brev.info.id, distribusjonstype)).isSuccess()
        }
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(veksleKlarStatus(brev, true)).isSuccess()

        assertThat(
            withPrincipal(saksbehandler1Principal) {
                handlerMed(maalinger)(SendBrevHandler.Request(brevId = brev.info.id, saksId = sak1.saksId))
            }
        ).isSuccess()

        maalinger.ventPaaTelling()
    }

    @Test
    suspend fun `brev uten overstyrt mottaker telles som sendt til bruker`() {
        val maalinger = nyeMaalinger()

        sendKlartBrev(maalinger)

        assertThat(maalinger.registry.antallSendt(mottaker = SendtBrevMetrikker.BRUKER)).isEqualTo(1.0)
    }

    @Test
    suspend fun `brev til samhandler telles med samhandlertype`() {
        val maalinger = nyeMaalinger()

        sendKlartBrev(maalinger, Dto.Mottaker.samhandler(SAMHANDLER_TSS_ID))

        assertThat(maalinger.registry.antallSendt(mottaker = "SAMHANDLER", samhandlerType = SAMHANDLER_TYPE)).isEqualTo(1.0)
    }

    @Test
    suspend fun `samhandler uten kjent type telles som ukjent`() {
        val maalinger = nyeMaalinger()

        sendKlartBrev(maalinger, Dto.Mottaker.samhandler("ukjent-tssid"))

        assertThat(maalinger.registry.antallSendt(mottaker = "SAMHANDLER", samhandlerType = SendtBrevMetrikker.UKJENT)).isEqualTo(1.0)
    }

    @Test
    suspend fun `feil ved oppslag av samhandlertype stopper ikke sendingen`() {
        val feilendeSamhandlerService = object : SamhandlerService {
            override suspend fun hentSamhandlerType(idTSSEkstern: String): String = throw RuntimeException("TSS er nede")
            override suspend fun hentSamhandlerNavn(idTSSEkstern: String): String? = null
            override suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto = notYetStubbed()
            override suspend fun hentSamhandler(idTSSEkstern: String): HentSamhandlerResponseDto = notYetStubbed()
            override suspend fun hentSamhandlerAdresse(idTSSEkstern: String): HentSamhandlerAdresseResponseDto = notYetStubbed()
        }
        val maalinger = nyeMaalinger(feilendeSamhandlerService)

        sendKlartBrev(maalinger, Dto.Mottaker.samhandler(SAMHANDLER_TSS_ID))

        assertThat(maalinger.registry.antallSendt(mottaker = "SAMHANDLER", samhandlerType = SendtBrevMetrikker.UKJENT)).isEqualTo(1.0)
    }

    @Test
    suspend fun `manuell norsk adresse telles med hvem den er adressert til`() {
        val maalinger = nyeMaalinger()

        sendKlartBrev(
            maalinger,
            Dto.Mottaker.norskAdresse(
                navn = "Anon Y. Mouse",
                postnummer = NorskPostnummer("0001"),
                poststed = "Andeby",
                adresselinje1 = "Andebyveien 1",
                adresselinje2 = null,
                adresselinje3 = null,
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.ANNEN,
            )
        )

        assertThat(maalinger.registry.antallSendt(mottaker = "NORSK_ADRESSE", adressertTil = "ANNEN")).isEqualTo(1.0)
    }

    @Test
    suspend fun `manuell utenlandsk adresse skilles fra norsk adresse`() {
        val maalinger = nyeMaalinger()

        sendKlartBrev(
            maalinger,
            Dto.Mottaker.utenlandskAdresse(
                navn = "Reci Pient",
                adresselinje1 = "Nikosiaveien 1",
                adresselinje2 = null,
                adresselinje3 = null,
                landkode = Landkode("CY"),
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.BRUKER,
            )
        )

        assertThat(maalinger.registry.antallSendt(mottaker = "UTENLANDSK_ADRESSE", adressertTil = "BRUKER")).isEqualTo(1.0)
        assertThat(maalinger.registry.antallSendt(mottaker = "NORSK_ADRESSE", adressertTil = "BRUKER")).isEqualTo(0.0)
    }

    @Test
    suspend fun `lokalprint telles med egen distribusjonstype`() {
        val maalinger = nyeMaalinger()

        sendKlartBrev(maalinger, distribusjonstype = Distribusjon.LOKALPRINT)

        assertThat(maalinger.registry.antallSendt(mottaker = SendtBrevMetrikker.BRUKER, distribusjon = Distribusjon.LOKALPRINT)).isEqualTo(1.0)
    }

    @Test
    suspend fun `brev som feiler under distribusjon telles ikke som sendt`() {
        val maalinger = nyeMaalinger()
        val brev = opprettBrev().resultOrFail()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(veksleKlarStatus(brev, true)).isSuccess()

        penService.sendBrevResponse = Pen.BestillBrevResponse(
            bestillBrevresponse.journalpostId,
            Pen.BestillBrevResponse.Error(null, "Distribuering feilet", null),
        )
        try {
            assertThat(
                withPrincipal(saksbehandler1Principal) {
                    handlerMed(maalinger)(SendBrevHandler.Request(brevId = brev.info.id, saksId = sak1.saksId))
                }
            ).isSuccess()
        } finally {
            penService.sendBrevResponse = bestillBrevresponse
        }
        maalinger.ventPaaTelling()

        assertThat(maalinger.registry.antallSendt(mottaker = SendtBrevMetrikker.BRUKER)).isEqualTo(0.0)
    }

    @Test
    suspend fun `samhandleroppslaget beholder saksbehandlerens principal etter at requesten er besvart`() {
        var identIOppslag: String? = null
        val principalKrevendeSamhandlerService = object : SamhandlerService {
            override suspend fun hentSamhandlerType(idTSSEkstern: String): String {
                identIOppslag = PrincipalInContext.require().navIdent.id
                return SAMHANDLER_TYPE
            }

            override suspend fun hentSamhandlerNavn(idTSSEkstern: String): String? = null
            override suspend fun finnSamhandler(requestDto: FinnSamhandlerRequestDto): FinnSamhandlerResponseDto = notYetStubbed()
            override suspend fun hentSamhandler(idTSSEkstern: String): HentSamhandlerResponseDto = notYetStubbed()
            override suspend fun hentSamhandlerAdresse(idTSSEkstern: String): HentSamhandlerAdresseResponseDto = notYetStubbed()
        }
        val maalinger = nyeMaalinger(principalKrevendeSamhandlerService)

        sendKlartBrev(maalinger, Dto.Mottaker.samhandler(SAMHANDLER_TSS_ID))

        assertThat(identIOppslag).isEqualTo(saksbehandler1Principal.navIdent.id)
        assertThat(maalinger.registry.antallSendt(mottaker = "SAMHANDLER", samhandlerType = SAMHANDLER_TYPE)).isEqualTo(1.0)
    }
}
