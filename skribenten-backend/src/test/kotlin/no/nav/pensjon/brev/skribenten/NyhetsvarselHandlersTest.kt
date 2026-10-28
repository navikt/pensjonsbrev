package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.skribenten.db.BekreftetNyhetsvarselTable
import no.nav.pensjon.brev.skribenten.db.Transactional
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.NyhetsvarselId
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
    private val hentBekreftedeNyhetsvarslerHandler by lazy { HentBekreftedeNyhetsvarslerHandler(transactional) }
    private val bekreftNyhetsvarselHandler by lazy { BekreftNyhetsvarselHandler(transactional) }

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

    private suspend fun hentBekreftede(navIdent: NavIdent): List<NyhetsvarselId> {
        var bekreftede: List<NyhetsvarselId> = emptyList()
        assertThat(hentBekreftedeNyhetsvarslerHandler(HentBekreftedeNyhetsvarslerHandler.Request(navIdent))).isSuccess<List<NyhetsvarselId>, Nothing> {
            bekreftede = it
        }
        return bekreftede
    }

    private fun bekreftetTidspunkter(navIdent: NavIdent) = transaction(SharedPostgres.database) {
        BekreftetNyhetsvarselTable.selectAll()
            .where { BekreftetNyhetsvarselTable.navIdent eq navIdent }
            .map { it[BekreftetNyhetsvarselTable.bekreftet] }
    }

    @Test
    suspend fun `henter ingen bekreftede nyhetsvarsler for bruker uten bekreftelser`() {
        assertThat(hentBekreftede(nyNavIdent())).isEmpty()
    }

    @Test
    suspend fun `bekrefter nyhetsvarsel og henter det ut igjen`() {
        val navIdent = nyNavIdent()
        val nyhetsvarselId = NyhetsvarselId("attestant-diff")

        assertThat(bekreftNyhetsvarselHandler(BekreftNyhetsvarselHandler.Request(navIdent, nyhetsvarselId))).isSuccess()

        assertThat(hentBekreftede(navIdent)).containsExactly(nyhetsvarselId)
    }

    @Test
    suspend fun `bekrefter flere nyhetsvarsler for samme bruker`() {
        val navIdent = nyNavIdent()
        val diff = NyhetsvarselId("attestant-diff")
        val eblanketter = NyhetsvarselId("brevvelger-eblanketter")

        bekreftNyhetsvarselHandler(BekreftNyhetsvarselHandler.Request(navIdent, diff))
        bekreftNyhetsvarselHandler(BekreftNyhetsvarselHandler.Request(navIdent, eblanketter))

        assertThat(hentBekreftede(navIdent)).containsExactlyInAnyOrder(diff, eblanketter)
    }

    @Test
    suspend fun `aa bekrefte samme nyhetsvarsel flere ganger lagrer det en gang og beholder foerste tidspunkt`() {
        val navIdent = nyNavIdent()
        val nyhetsvarselId = NyhetsvarselId("attestant-diff")

        assertThat(bekreftNyhetsvarselHandler(BekreftNyhetsvarselHandler.Request(navIdent, nyhetsvarselId))).isSuccess()
        val foersteBekreftelse = bekreftetTidspunkter(navIdent).single()

        assertThat(bekreftNyhetsvarselHandler(BekreftNyhetsvarselHandler.Request(navIdent, nyhetsvarselId))).isSuccess()

        assertThat(hentBekreftede(navIdent)).containsExactly(nyhetsvarselId)
        assertThat(bekreftetTidspunkter(navIdent)).containsExactly(foersteBekreftelse)
    }

    @Test
    suspend fun `bekreftelser er knyttet til brukeren som bekreftet`() {
        val bekrefter = nyNavIdent()
        val annenBruker = nyNavIdent()

        bekreftNyhetsvarselHandler(BekreftNyhetsvarselHandler.Request(bekrefter, NyhetsvarselId("attestant-diff")))

        assertThat(hentBekreftede(annenBruker)).isEmpty()
    }
}
