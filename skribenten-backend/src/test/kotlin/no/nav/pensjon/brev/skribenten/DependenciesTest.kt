package no.nav.pensjon.brev.skribenten

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.Application
import io.ktor.server.config.HoconApplicationConfig
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.config.mergeWith
import io.ktor.server.plugins.di.DependencyKey
import io.ktor.server.plugins.di.DependencyResolver
import io.ktor.server.plugins.di.MapDependencyResolver
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.di.getBlocking
import io.ktor.server.testing.testApplication
import no.nav.pensjon.brev.skribenten.auth.AzureADSettings
import no.nav.pensjon.brev.skribenten.brevbaker.BrevbakerSettings
import no.nav.pensjon.brev.skribenten.common.CacheSettings
import no.nav.pensjon.brev.skribenten.db.DatabaseSettings
import no.nav.pensjon.brev.skribenten.eksterntApi.ExternalApiSettings
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevmetadataSettings
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenSettings
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorSettings
import no.nav.pensjon.brev.skribenten.services.KrrSettings
import no.nav.pensjon.brev.skribenten.services.LeaderSettings
import no.nav.pensjon.brev.skribenten.services.NaisLeaderService
import no.nav.pensjon.brev.skribenten.services.NavansattSettings
import no.nav.pensjon.brev.skribenten.services.Norg2Settings
import no.nav.pensjon.brev.skribenten.services.PdlSettings
import no.nav.pensjon.brev.skribenten.services.PensjonPersonDataSettings
import no.nav.pensjon.brev.skribenten.services.PensjonRepresentasjonSettings
import no.nav.pensjon.brev.skribenten.services.SafSettings
import no.nav.pensjon.brev.skribenten.services.SamhandlerSettings
import no.nav.pensjon.brev.skribenten.services.SkjermingSettings
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.api.parallel.Isolated
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.testcontainers.postgresql.PostgreSQLContainer

/**
 * Verifiserer at alt [configureDependencies] registrerer faktisk lar seg konstruere.
 *
 * Ktor validerer ved oppstart bare deklarasjoner en rute etterspør med `by app.dependencies`.
 * Registreringer ingen rute etterspør — verken direkte eller transitivt — konstrueres først når en
 * request treffer dem, og feiler da i prod etter grønn oppstart. Denne testen resolver derfor hver
 * eneste registrerte nøkkel.
 *
 * Ktor 3.5.2 har ingen offentlig måte å enumerere deklarasjoner på, så nøklene hentes fra
 * [MapDependencyResolver] sitt private `map`-felt. En håndholdt liste ville råtnet i det øyeblikket
 * noen la til en ny handler uten å oppdatere testen — altså akkurat feilen testen skal fange.
 *
 * Merk at Ktor bruker `IgnoreConflicts` under testmotoren, så duplikate deklarasjoner blir ikke
 * fanget her.
 */
@Isolated
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DependenciesTest {

    private lateinit var postgres: PostgreSQLContainer
    private var preUnleash: FeatureToggleService? = null

    @BeforeAll
    fun setup() {
        preUnleash = Features.toggleService
        postgres = SharedPostgres.createStandaloneContainer().also { it.start() }
        initADGroups()
    }

    @AfterAll
    fun teardown() {
        preUnleash?.let { Features.init(it) }
        postgres.stop()
    }

    private fun databaseConfig() = MapApplicationConfig(
        "skribenten.database.host" to postgres.host,
        "skribenten.database.port" to postgres.getMappedPort(5432).toString(),
        "skribenten.database.name" to postgres.databaseName,
        "skribenten.database.username" to postgres.username,
        "skribenten.database.password" to postgres.password,
        "skribenten.database.maxPoolSize" to "2",
    )

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    @Execution(ExecutionMode.SAME_THREAD)
    fun `alle registrerte avhengigheter kan resolves`(leaderEnabled: Boolean) = testApplication {
        environment {
            config = HoconApplicationConfig(
                ConfigFactory.parseResources("application-test.conf")
                    .withoutPath("skribenten.services.leader")
                    .resolve()
            ).mergeWith(databaseConfig())
            if (leaderEnabled) {
                config = config.mergeWith(MapApplicationConfig("skribenten.services.leader.url" to "http://localhost:9999/fake"))
            }
        }

        lateinit var app: Application
        application { app = this }
        startApplication()

        val registry = app.dependencies
        val config = registry.resolve<SkribentenConfig>()
        assertThat(
            listOf(
                registry.resolve<AzureADSettings>(),
                registry.resolve<DatabaseSettings>(),
                registry.resolve<UnleashSettings>(),
                registry.resolve<CacheSettings>(),
                registry.resolve<CorsSettings>(),
                registry.resolve<PenSettings>(),
                registry.resolve<PdlSettings>(),
                registry.resolve<SafSettings>(),
                registry.resolve<PensjonPersonDataSettings>(),
                registry.resolve<PensjonRepresentasjonSettings>(),
                registry.resolve<KrrSettings>(),
                registry.resolve<BrevbakerSettings>(),
                registry.resolve<BrevmetadataSettings>(),
                registry.resolve<NavansattSettings>(),
                registry.resolve<SamhandlerSettings>(),
                registry.resolve<Norg2Settings>(),
                registry.resolve<ExternalApiSettings>(),
                registry.resolve<LeaderSettings>(),
                registry.resolve<SkjermingSettings>(),
                registry.resolve<FoerstesidegeneratorSettings>(),
            )
        ).allSatisfy { assertThat(it).isSameAs(config) }
        assertThat(config.leader != null).isEqualTo(leaderEnabled)
        assertThat(registry.resolve<NaisLeaderService>().isLeaderElectionEnabled).isEqualTo(leaderEnabled)
        val keys = registry.resolver.registrerteNoekler()

        assertTrue(keys.size >= MINIMUM_ANTALL_DEKLARASJONER) {
            "Fant bare ${keys.size} DI-deklarasjoner, forventet minst $MINIMUM_ANTALL_DEKLARASJONER. " +
                "Sannsynligvis har en Ktor-oppgradering endret internstrukturen i MapDependencyResolver, " +
                "slik at testen ikke lenger ser noe som helst."
        }

        val feil = keys.mapNotNull { key ->
            runCatching { registry.getBlocking<Any?>(key) }.exceptionOrNull()?.let { key to it }
        }
        // Hver deklarasjon ligger i mappet flere ganger (nullbar variant, supertyper osv.), så én
        // ødelagt klasse gir flere oppslag med samme årsak. Vi rapporterer én linje per årsak.
        val unikeFeil = feil.distinctBy { (_, cause) -> cause.message }

        assertTrue(feil.isEmpty()) {
            unikeFeil.joinToString(
                prefix = "Kunne ikke resolve ${feil.size} av ${keys.size} DI-oppslag " +
                    "(${unikeFeil.size} unike årsaker):\n  - ",
                separator = "\n  - ",
            ) { (key, cause) -> "$key: ${cause.message ?: cause::class.simpleName}" }
        }
    }

    private fun DependencyResolver.registrerteNoekler(): List<DependencyKey> {
        val resolver = this as? MapDependencyResolver
            ?: error("Forventet MapDependencyResolver, men fikk ${this::class.qualifiedName}")
        val field = MapDependencyResolver::class.java.getDeclaredField("map").apply { isAccessible = true }

        @Suppress("UNCHECKED_CAST")
        return (field.get(resolver) as Map<DependencyKey, *>).keys.toList()
    }

    private companion object {
        /** Grovt gulv mot at refleksjonen slutter å virke; mappet lå på ~300 da testen ble skrevet. */
        const val MINIMUM_ANTALL_DEKLARASJONER = 200
    }
}
