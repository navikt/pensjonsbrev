package no.nav.pensjon.brev.skribenten

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.model.NavIdent
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class FavoritterHandlersTest {
    private val hentFavoritterHandler by lazy { HentFavoritterHandler(SharedPostgres.database) }
    private val leggTilFavorittHandler by lazy { LeggTilFavorittHandler(SharedPostgres.database) }
    private val fjernFavorittHandler by lazy { FjernFavorittHandler(SharedPostgres.database) }

    @BeforeAll
    fun initDb() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }

    // Egen NavIdent per test for å unngå at tester påvirker hverandre, siden Favourites-tabellen ikke tømmes mellom tester.
    private val identCounter = AtomicInteger(0)
    private fun nyNavIdent() = NavIdent("Z${identCounter.incrementAndGet()}")

    @Test
    suspend fun `henter ingen favoritter for bruker uten favoritter`() {
        val navIdent = nyNavIdent()

        assertThat(hentFavoritterHandler(HentFavoritterHandler.Request(navIdent))).isSuccess {
            assertThat(it).isEmpty()
        }
    }

    @Test
    suspend fun `legger til favoritt og henter den ut igjen`() {
        val navIdent = nyNavIdent()
        val brevkode = RedigerbarBrevkode("TESTBREV")

        assertThat(leggTilFavorittHandler(LeggTilFavorittHandler.Request(navIdent, brevkode))).isSuccess()

        assertThat(hentFavoritterHandler(HentFavoritterHandler.Request(navIdent))).isSuccess {
            assertThat(it).containsExactly(brevkode)
        }
    }

    @Test
    suspend fun `legger til flere favoritter for samme bruker`() {
        val navIdent = nyNavIdent()
        val brevkode1 = RedigerbarBrevkode("TESTBREV1")
        val brevkode2 = RedigerbarBrevkode("TESTBREV2")

        leggTilFavorittHandler(LeggTilFavorittHandler.Request(navIdent, brevkode1))
        leggTilFavorittHandler(LeggTilFavorittHandler.Request(navIdent, brevkode2))

        assertThat(hentFavoritterHandler(HentFavoritterHandler.Request(navIdent))).isSuccess {
            assertThat(it).containsExactlyInAnyOrder(brevkode1, brevkode2)
        }
    }

    @Test
    suspend fun `favoritter er isolert per bruker`() {
        val navIdent1 = nyNavIdent()
        val navIdent2 = nyNavIdent()
        val brevkode = RedigerbarBrevkode("TESTBREV")

        leggTilFavorittHandler(LeggTilFavorittHandler.Request(navIdent1, brevkode))

        assertThat(hentFavoritterHandler(HentFavoritterHandler.Request(navIdent2))).isSuccess {
            assertThat(it).isEmpty()
        }
    }

    @Test
    suspend fun `fjerner favoritt`() {
        val navIdent = nyNavIdent()
        val brevkode = RedigerbarBrevkode("TESTBREV")

        leggTilFavorittHandler(LeggTilFavorittHandler.Request(navIdent, brevkode))
        assertThat(fjernFavorittHandler(FjernFavorittHandler.Request(navIdent, brevkode))).isSuccess()

        assertThat(hentFavoritterHandler(HentFavoritterHandler.Request(navIdent))).isSuccess {
            assertThat(it).isEmpty()
        }
    }

    @Test
    suspend fun `fjerner kun favoritten som er spesifisert`() {
        val navIdent = nyNavIdent()
        val brevkode1 = RedigerbarBrevkode("TESTBREV1")
        val brevkode2 = RedigerbarBrevkode("TESTBREV2")

        leggTilFavorittHandler(LeggTilFavorittHandler.Request(navIdent, brevkode1))
        leggTilFavorittHandler(LeggTilFavorittHandler.Request(navIdent, brevkode2))

        fjernFavorittHandler(FjernFavorittHandler.Request(navIdent, brevkode1))

        assertThat(hentFavoritterHandler(HentFavoritterHandler.Request(navIdent))).isSuccess {
            assertThat(it).containsExactly(brevkode2)
        }
    }

    @Test
    suspend fun `feiler ikke ved fjerning av favoritt som ikke finnes`() {
        val navIdent = nyNavIdent()
        val brevkode = RedigerbarBrevkode("TESTBREV")

        assertThat(fjernFavorittHandler(FjernFavorittHandler.Request(navIdent, brevkode))).isSuccess()
    }
}
