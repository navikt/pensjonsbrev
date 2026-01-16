package no.nav.pensjon.brev.skribenten

import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigParseOptions.defaults
import com.typesafe.config.ConfigResolveOptions
import io.ktor.util.collections.ConcurrentSet
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.auth.ADGroup
import no.nav.pensjon.brev.skribenten.auth.ADGroups
import no.nav.pensjon.brev.skribenten.auth.UserAccessToken
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.db.initDatabase
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.usecase.Outcome
import no.nav.pensjon.brevbaker.api.model.Bruker
import no.nav.pensjon.brevbaker.api.model.Felles
import no.nav.pensjon.brevbaker.api.model.NavEnhet
import no.nav.pensjon.brevbaker.api.model.SignerendeSaksbehandlere
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ObjectAssert
import org.testcontainers.postgresql.PostgreSQLContainer
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicBoolean

data class MockPrincipal(override val navIdent: NavIdent, override val fullName: String, val groups: Set<ADGroup> = emptySet()) : UserPrincipal {
    override val accessToken: UserAccessToken
        get() = throw NotImplementedError("Not implemented in mock class")

    override fun isInGroup(groupId: ADGroup) = groups.contains(groupId)
}

fun initADGroups() = ADGroups.init(
ConfigFactory
    .load("application-local", defaults(), ConfigResolveOptions.defaults().setAllowUnresolved(true))
    .getConfig("skribenten")
    .getConfig("groups")
)

object Testbrevkoder {
    val TESTBREV = RedigerbarBrevkode("TESTBREV")
    val INFORMASJONSBREV = RedigerbarBrevkode("INFORMASJONSBREV")
    val VEDTAKSBREV = RedigerbarBrevkode("VEDTAKSBREV")
    val VARSELBREV = RedigerbarBrevkode("VARSELBREV")
}

data class EksempelRedigerbartDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, EksempelRedigerbartDto.PesysData> {
    data class PesysData(
        val pensjonInnvilget: Boolean,
        val datoInnvilget: LocalDate,
        val navneliste: List<String>,
        val datoAvslaatt: LocalDate?,
        val pensjonBeloep: Int?,
    ) : FagsystemBrevdata
}

fun Felles.copy(
    dokumentDato: LocalDate = this.dokumentDato,
    saksnummer: String = this.saksnummer,
    avsenderEnhet: NavEnhet = this.avsenderEnhet,
    bruker: Bruker = this.bruker,
    annenMottakerNavn: String? = this.annenMottakerNavn,
    signerendeSaksbehandlere: SignerendeSaksbehandlere? = this.signerendeSaksbehandlere,
) = Felles(
    dokumentDato = dokumentDato,
    saksnummer = saksnummer,
    avsenderEnhet = avsenderEnhet,
    bruker = bruker,
    annenMottakerNavn = annenMottakerNavn,
    signerendeSaksbehandlere = signerendeSaksbehandlere,
)

inline fun <reified T, E> ObjectAssert<Outcome<T, E>?>.isSuccess(noinline block: ((T) -> Unit)? = null): ObjectAssert<Outcome<T, E>?> {
    isNotNull()
    isInstanceOfSatisfying<Outcome.Success<*>>(Outcome.Success::class.java) { res ->
        assertThat(res.value).isInstanceOfSatisfying<T>(T::class.java) {
            block?.invoke(it)
        }
    }
    return this
}

inline fun <reified ExpectedE : E, T, E> ObjectAssert<Outcome<T, E>?>.isFailure(noinline block: ((E) -> Unit)? = null): ObjectAssert<Outcome<T, E>?> {
    isNotNull()
    isInstanceOfSatisfying<Outcome.Failure<*>>(Outcome.Failure::class.java) { res ->
        assertThat(res.error).isInstanceOfSatisfying<ExpectedE>(ExpectedE::class.java) {
            block?.invoke(it)
        }
    }
    return this
}

object SharedPostgres {
    private val subscriptions = ConcurrentSet<Any>()

    private val container by lazy {
        PostgreSQLContainer("postgres:17-alpine")
            .apply { start() }
    }

    private val initialized = AtomicBoolean(false)

    fun subscribeAndEnsureDatabaseInitialized(subscriber: Any) {
        synchronized(this) {
            if (initialized.compareAndSet(false, true)) {
                val c = container
                initDatabase(jdbcUrl = c.jdbcUrl, username = c.username, password = c.password, maxPoolSize = 20)
            }
            subscriptions.add(subscriber)
        }
    }

    fun cancelSubscription(subscriber: Any) {
        synchronized(this) {
            subscriptions.remove(subscriber)
            if (subscriptions.isEmpty()) {
                try {
                    container.stop()
                } catch (t: Throwable) {
                    println("Could not stop Postgres container: ${t.message}")
                }
                initialized.set(false)
            }

        }
    }
}