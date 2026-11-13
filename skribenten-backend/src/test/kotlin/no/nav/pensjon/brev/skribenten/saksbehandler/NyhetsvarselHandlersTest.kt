package no.nav.pensjon.brev.skribenten.saksbehandler

import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.db.NyhetsvarselstatusTable
import no.nav.pensjon.brev.skribenten.db.Transactional
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.NyhetsvarselId
import no.nav.pensjon.brev.skribenten.model.Nyhetsvarselmarkering.GJENNOMFOERT_OMVISNINGEN
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
            .let { it[NyhetsvarselstatusTable.settAnnonsen] to it[NyhetsvarselstatusTable.gjennomfoertOmvisningen] }
    }

    @Test
    suspend fun `henter tomme lister for bruker uten nyhetsvarsler`() {
        assertThat(hent(nyNavIdent())).isEqualTo(Api.Nyhetsvarsler(emptyList(), emptyList()))
    }

    @Test
    suspend fun `markerer at annonsen er sett uten at omvisningen er gjennomfoert`() {
        val navIdent = nyNavIdent()

        assertThat(markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, SETT_ANNONSEN))).isSuccess()

        assertThat(hent(navIdent)).isEqualTo(Api.Nyhetsvarsler(settAnnonsen = listOf(diff), gjennomfoertOmvisningen = emptyList()))
    }

    @Test
    suspend fun `markerer at omvisningen er gjennomfoert uten at annonsen er sett`() {
        val navIdent = nyNavIdent()

        assertThat(markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, GJENNOMFOERT_OMVISNINGEN))).isSuccess()

        assertThat(hent(navIdent)).isEqualTo(Api.Nyhetsvarsler(settAnnonsen = emptyList(), gjennomfoertOmvisningen = listOf(diff)))
    }

    @Test
    suspend fun `begge markeringene lagres i samme rad`() {
        val navIdent = nyNavIdent()

        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, SETT_ANNONSEN))
        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, diff, GJENNOMFOERT_OMVISNINGEN))

        assertThat(hent(navIdent)).isEqualTo(Api.Nyhetsvarsler(settAnnonsen = listOf(diff), gjennomfoertOmvisningen = listOf(diff)))
        val (settAnnonsen, gjennomfoertOmvisningen) = rad(navIdent)
        assertThat(settAnnonsen).isNotNull()
        assertThat(gjennomfoertOmvisningen).isNotNull()
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
        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(navIdent, eblanketter, GJENNOMFOERT_OMVISNINGEN))

        val nyhetsvarsler = hent(navIdent)
        assertThat(nyhetsvarsler.settAnnonsen).containsExactlyInAnyOrder(diff, eblanketter)
        assertThat(nyhetsvarsler.gjennomfoertOmvisningen).containsExactly(eblanketter)
    }

    @Test
    suspend fun `nyhetsvarsler er knyttet til brukeren som markerte dem`() {
        markerNyhetsvarselHandler(MarkerNyhetsvarselHandler.Request(nyNavIdent(), diff, GJENNOMFOERT_OMVISNINGEN))

        assertThat(hent(nyNavIdent())).isEqualTo(Api.Nyhetsvarsler(emptyList(), emptyList()))
    }
}
