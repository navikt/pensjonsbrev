package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import no.nav.pensjon.brev.skribenten.Metrics
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MottakerType
import no.nav.pensjon.brev.skribenten.brevredigering.domain.TssId
import no.nav.pensjon.brev.skribenten.model.Distribusjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brev.skribenten.services.FakeSamhandlerService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.entry
import org.junit.jupiter.api.Test

class SendtBrevMetrikkerTest {

    private val samhandlerService =
        FakeSamhandlerService(
        navn = mapOf("80000000003" to "Samhandler uten identtype"),
        typer = mapOf(TssId("80000123456") to "ADVO", TssId("80000999999") to "LE"),
        idTyper = mapOf(
            "80000123456" to "ORG",
            "80000999999" to "FNR",
            "80000000001" to "ORGNR",
            "80000000002" to "UTOR",
        ),
        offentligIder = mapOf(
            "80000123456" to ORGNR,
            "80000999999" to "12345678910",
            "80000000001" to ANNET_ORGNR,
            "80000000002" to "SE556036079301",
        ),
    )

    private fun metrikker(registry: MeterRegistry) = SendtBrevMetrikker(samhandlerService, registry)

    private fun maaling(
        mottakerType: MottakerType? = null,
        tssId: TssId? = null,
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

        metrikker(registry).tellSendtBrev(maaling(mottakerType = MottakerType.SAMHANDLER, tssId = TssId("80000123456")))
            .join()

        assertThat(registry.scrape()).contains(
            """skribenten_brev_sendt_total{adressert_til="IKKE_RELEVANT",avsender_enhet="1234",distribusjon="SENTRALPRINT",id_type="ORG",mottaker="SAMHANDLER",samhandler_type="ADVO"} 1.0"""
        )
    }

    @Test
    suspend fun `samhandlerens id havner aldri i metrikken`() {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

        metrikker(registry).tellSendtBrev(maaling(mottakerType = MottakerType.SAMHANDLER, tssId = TssId("80000123456")))
            .join()

        assertThat(registry.scrape())
            .doesNotContain("80000123456")
            .doesNotContain(ORGNR)
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

    private fun MeterRegistry.idTypeFor(tssId: String?): String =
        find(SendtBrevMetrikker.metricName).counters().singleOrNull()?.id?.getTag("id_type")
            ?: error("Fant ikke én teller for tssId $tssId")

    private fun MeterRegistry.orgBoetter(): Map<String, Double> =
        find(SendtBrevMetrikker.orgMetricName).counters()
            .associate { it.id.getTag("org_bucket")!! to it.count() }

    private suspend fun tellSamhandler(registry: MeterRegistry, tssId: String?) =
        metrikker(registry).tellSendtBrev(maaling(mottakerType = MottakerType.SAMHANDLER, tssId = tssId)).join()

    @Test
    suspend fun `organisasjonsnummer gir id_type ORG og telles i en hash-boette`() {
        val registry = SimpleMeterRegistry()

        tellSamhandler(registry, "80000123456")

        assertThat(registry.idTypeFor("80000123456")).isEqualTo(SendtBrevMetrikker.SamhandlerIdType.ORG.name)
        assertThat(registry.orgBoetter()).containsExactly(entry(SendtBrevMetrikker.orgBoette(ORGNR).toString(), 1.0))
    }

    @Test
    suspend fun `foedselsnummer gir id_type FNR og telles ikke i noen boette`() {
        val registry = SimpleMeterRegistry()

        tellSamhandler(registry, "80000999999")

        assertThat(registry.idTypeFor("80000999999")).isEqualTo(SendtBrevMetrikker.SamhandlerIdType.FNR.name)
        assertThat(registry.orgBoetter()).isEmpty()
    }

    @Test
    suspend fun `ORGNR godtas paa lik linje med ORG`() {
        val registry = SimpleMeterRegistry()

        tellSamhandler(registry, "80000000001")

        assertThat(registry.idTypeFor("80000000001")).isEqualTo(SendtBrevMetrikker.SamhandlerIdType.ORG.name)
        assertThat(registry.orgBoetter()).containsExactly(entry(SendtBrevMetrikker.orgBoette(ANNET_ORGNR).toString(), 1.0))
    }

    @Test
    suspend fun `andre identtyper gir ANNEN og telles ikke i noen boette`() {
        val registry = SimpleMeterRegistry()

        tellSamhandler(registry, "80000000002")

        assertThat(registry.idTypeFor("80000000002")).isEqualTo(SendtBrevMetrikker.SamhandlerIdType.ANNEN.name)
        assertThat(registry.orgBoetter()).isEmpty()
    }

    @Test
    suspend fun `samhandler uten identtype eller type gir UKJENT paa begge labelene`() {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

        tellSamhandler(registry, "80000000003")

        assertThat(registry.scrape()).contains(
            """skribenten_brev_sendt_total{adressert_til="IKKE_RELEVANT",avsender_enhet="1234",distribusjon="SENTRALPRINT",id_type="UKJENT",mottaker="SAMHANDLER",samhandler_type="UKJENT"} 1.0"""
        )
        assertThat(registry.orgBoetter()).isEmpty()
    }

    @Test
    suspend fun `ukjent samhandler gir UKJENT uten boettetelling`() {
        val registry = SimpleMeterRegistry()

        tellSamhandler(registry, "finnes-ikke")

        assertThat(registry.idTypeFor("finnes-ikke")).isEqualTo(SendtBrevMetrikker.UKJENT)
        assertThat(registry.orgBoetter()).isEmpty()
    }

    @Test
    suspend fun `brev til andre enn samhandler er ikke relevant for id_type`() {
        val registry = SimpleMeterRegistry()

        metrikker(registry).tellSendtBrev(maaling()).join()

        assertThat(registry.idTypeFor(null)).isEqualTo(SendtBrevMetrikker.IKKE_RELEVANT)
        assertThat(registry.orgBoetter()).isEmpty()
    }

    @Test
    suspend fun `feilende oppslag gir UKJENT i stedet for aa kaste`() {
        val registry = SimpleMeterRegistry()
        val feilende = object : FakeSamhandlerService() {
            override suspend fun hentSamhandler(idTSSEkstern: String) = throw RuntimeException("TSS er nede")
        }

        SendtBrevMetrikker(feilende, registry)
            .tellSendtBrev(maaling(mottakerType = MottakerType.SAMHANDLER, tssId = "80000123456")).join()

        assertThat(registry.idTypeFor("80000123456")).isEqualTo(SendtBrevMetrikker.UKJENT)
        assertThat(registry.orgBoetter()).isEmpty()
    }

    @Test
    suspend fun `feilet oppslag er UKJENT paa begge samhandlerlabelene og ikke IKKE_RELEVANT`() {
        val registry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        val feilende = object : FakeSamhandlerService() {
            override suspend fun hentSamhandler(idTSSEkstern: String) = throw RuntimeException("TSS er nede")
        }

        SendtBrevMetrikker(feilende, registry)
            .tellSendtBrev(maaling(mottakerType = MottakerType.SAMHANDLER, tssId = "80000123456")).join()

        assertThat(registry.scrape()).contains(
            """skribenten_brev_sendt_total{adressert_til="IKKE_RELEVANT",avsender_enhet="1234",distribusjon="SENTRALPRINT",id_type="UKJENT",mottaker="SAMHANDLER",samhandler_type="UKJENT"} 1.0"""
        )
    }

    @Test
    suspend fun `samme organisasjonsnummer havner alltid i samme boette`() {
        val registry = SimpleMeterRegistry()

        tellSamhandler(registry, "80000123456")
        tellSamhandler(registry, "80000123456")

        assertThat(registry.orgBoetter()).containsExactly(entry(SendtBrevMetrikker.orgBoette(ORGNR).toString(), 2.0))
    }

    // Uten usignert tolkning ville annethvert organisasjonsnummer gitt et negativt boettenummer.
    @Test
    fun `boettenummeret er alltid innenfor antall boetter`() {
        val boetter = (0 until 10_000).map { SendtBrevMetrikker.orgBoette("9${it.toString().padStart(8, '0')}") }

        assertThat(boetter).allMatch { it in 0 until SendtBrevMetrikker.ANTALL_ORG_BOETTER }
        assertThat(boetter.distinct()).hasSize(SendtBrevMetrikker.ANTALL_ORG_BOETTER)
    }

    // Feiler denne, er svaret en ny metrikk med nytt navn: aa oppdatere forventningen flytter alle
    // organisasjonsnumre til nye boetter og gjoer historikken i Prometheus verdiloes.
    @Test
    fun `hashen er frosset`() {
        assertThat(SendtBrevMetrikker.ANTALL_ORG_BOETTER).isEqualTo(128)
        assertThat(SendtBrevMetrikker.orgBoette("987654321")).isEqualTo(30)
        assertThat(SendtBrevMetrikker.orgBoette("912345678")).isEqualTo(106)
    }

    companion object {
        private const val ORGNR = "987654321"
        private const val ANNET_ORGNR = "912345678"
    }
}
