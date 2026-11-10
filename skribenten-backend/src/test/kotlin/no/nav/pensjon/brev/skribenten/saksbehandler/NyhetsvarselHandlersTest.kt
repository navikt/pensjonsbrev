package no.nav.pensjon.brev.skribenten.saksbehandler

import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.db.NyhetsvarselstatusTable
import no.nav.pensjon.brev.skribenten.db.Transactional
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.NyhetsvarselId
import no.nav.pensjon.brev.skribenten.model.Nyhetsvarselmarkering.GJENNOMFOERT_TUREN
import no.nav.pensjon.brev.skribenten.model.Nyhetsvarselmarkering.SETT_ANNONSEN
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class NyhetsvarselHandlersTest {
    private val transactional by lazy { Transactional(SharedPostgres.database) }
    private val hentNyhetsvarslerHandler by lazy { HentNyhetsvarslerHandler(transactional) }
    private val markerNyhetsvarselHandler by lazy { MarkerNyhetsvarselHandler(transactional) }

    @BeforeAll
    fun initDb() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }

    private val identCounter = AtomicInteger(0)
    private fun nyNavIdent() = NavIdent("H${identCounter.incrementAndGet()}")
    private val diff = NyhetsvarselId("diff-toggles")

    private suspend fun hent(navIdent: NavIdent): Api.Nyhetsvarsler {
        var nyhetsvarsler = Api.Nyhetsvarsler(emptyList(), emptyList())
        assertThat(hentNyhetsvarslerHandler(HentNyhetsvarslerHandler.Request(navIdent))).isSuccess<Api.Nyhetsvarsler, Nothing> {
            nyhetsvarsler = it
        }
        return nyhetsvarsler
    }

    private fun rad(navIdent: NavIdent) = transaction(SharedPostgres.database) {
        NyhetsvarselstatusTable.selectAll()
            .where { NyhetsvarselstatusTable.navIdent eq navIdent }
            .single()
            .let { it[NyhetsvarselstatusTable.settAnnonsen] to it[NyhetsvarselstatusTable.gjennomfoertTuren] }
    }

    @Test
    suspend fun `henter tomme lister for bruker uten nyhetsvarsler`() {
        assertThat(hent(nyNavIdent())).isEqualTo(Api.Nyhetsvarsler(emptyList(), emptyList()))
    }

    @Test
    suspend fun `markerer at annonsen er sett uten at turen er gjennomfoert`() {
        val navIdent = nyNavIdent()

        assertThat(markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, SETT_ANNONSEN))).isSuccess()

        assertThat(hent(navIdent)).isEqualTo(Api.Nyhetsvarsler(settAnnonsen = listOf(diff), gjennomfoertTuren = emptyList()))
    }

    @Test
    suspend fun `markerer at turen er gjennomfoert uten at annonsen er sett`() {
        val navIdent = nyNavIdent()

        assertThat(markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, GJENNOMFOERT_TUREN))).isSuccess()

        assertThat(hent(navIdent)).isEqualTo(Api.Nyhetsvarsler(settAnnonsen = emptyList(), gjennomfoertTuren = listOf(diff)))
    }

    @Test
    suspend fun `begge markeringene lagres i samme rad`() {
        val navIdent = nyNavIdent()

        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, SETT_ANNONSEN))
        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, GJENNOMFOERT_TUREN))

        assertThat(hent(navIdent)).isEqualTo(Api.Nyhetsvarsler(settAnnonsen = listOf(diff), gjennomfoertTuren = listOf(diff)))
        val (settAnnonsen, gjennomfoertTuren) = rad(navIdent)
        assertThat(settAnnonsen).isNotNull()
        assertThat(gjennomfoertTuren).isNotNull()
    }

    @Test
    suspend fun `aa markere paa nytt beholder foerste tidspunkt og roerer ikke den andre markeringen`() {
        val navIdent = nyNavIdent()
        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, SETT_ANNONSEN))
        val (foersteSett, _) = rad(navIdent)

        assertThat(markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, SETT_ANNONSEN))).isSuccess()

        assertThat(rad(navIdent)).isEqualTo(foersteSett to null)
    }

    @Test
    suspend fun `henter flere nyhetsvarsler for samme bruker`() {
        val navIdent = nyNavIdent()
        val eblanketter = NyhetsvarselId("brevvelger-eblanketter")

        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, SETT_ANNONSEN))
        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, eblanketter, SETT_ANNONSEN))
        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, eblanketter, GJENNOMFOERT_TUREN))

        val nyhetsvarsler = hent(navIdent)
        assertThat(nyhetsvarsler.settAnnonsen).containsExactlyInAnyOrder(diff, eblanketter)
        assertThat(nyhetsvarsler.gjennomfoertTuren).containsExactly(eblanketter)
    }

    @Test
    suspend fun `nyhetsvarsler er knyttet til brukeren som markerte dem`() {
        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(nyNavIdent(), diff, GJENNOMFOERT_TUREN))

        assertThat(hent(nyNavIdent())).isEqualTo(Api.Nyhetsvarsler(emptyList(), emptyList()))
    }
}
