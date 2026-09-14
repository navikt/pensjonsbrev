package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import io.micrometer.core.instrument.MeterRegistry
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.skribenten.Metrics
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MottakerType
import no.nav.pensjon.brev.skribenten.model.Distribusjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.FakeSamhandlerService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SendtBrevMetrikkerTest {

    private val samhandlerService = FakeSamhandlerService(typer = mapOf("80000123456" to "ADVO", "80000999999" to "LE"))

    private fun metrikker(registry: MeterRegistry) = SendtBrevMetrikker(samhandlerService, registry)

    private fun maaling(
        mottakerType: MottakerType? = null,
        tssId: String? = null,
        manueltAdressertTil: Dto.Mottaker.ManueltAdressertTil? = null,
        avsenderEnhet: String = "1234",
    ) = SendtBrevMetrikker.SendtBrevMaaling(
        mottakerType = mottakerType,
        tssId = tssId,
        manueltAdressertTil = manueltAdressertTil,
        distribusjonstype = Distribusjon.SENTRALPRINT,
        avsenderEnhet = EnhetId(avsenderEnhet),
    )

    @Test
    suspend fun `eksponeres i prometheus-format med alle labels`() {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

        metrikker(registry).tellSendtBrev(maaling(mottakerType = MottakerType.SAMHANDLER, tssId = "80000123456")).join()

        assertThat(registry.scrape()).contains(
            """skribenten_brev_sendt_total{adressert_til="IKKE_RELEVANT",avsender_enhet="1234",distribusjon="SENTRALPRINT",mottaker="SAMHANDLER",samhandler_type="ADVO"} 1.0"""
        )
    }

    @Test
    suspend fun `samhandlerens id havner aldri i metrikken`() {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

        metrikker(registry).tellSendtBrev(maaling(mottakerType = MottakerType.SAMHANDLER, tssId = "80000123456")).join()

        assertThat(registry.scrape()).doesNotContain("80000123456")
    }

    @Test
    suspend fun `avsenderenheter over taket forkastes i stedet for aa blaase opp kardinaliteten`() {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        registry.config().meterFilter(Metrics.avsenderEnhetFilter())
        val metrikker = metrikker(registry)
        val jobber = mutableListOf<Job>()

        repeat(Metrics.maksAntallAvsenderEnheter + 10) {
            jobber += metrikker.tellSendtBrev(maaling(avsenderEnhet = (1000 + it).toString()))
        }
        jobber.joinAll()

        val antallEnheter = registry.find(SendtBrevMetrikker.metricName).counters()
            .map { it.id.getTag("avsender_enhet") }.distinct().size
        assertThat(antallEnheter).isEqualTo(Metrics.maksAntallAvsenderEnheter)
    }
}
