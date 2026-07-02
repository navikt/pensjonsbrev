package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.api.model.maler.*
import no.nav.pensjon.brev.skribenten.*
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.BrevredigeringHandlerTestBase.Fixtures.brevdataResponseData
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.BrevredigeringHandlerTestBase.Fixtures.informasjonsbrev
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.BrevredigeringHandlerTestBase.Fixtures.letter
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.BrevredigeringHandlerTestBase.Fixtures.resultOrFail
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.BrevredigeringHandlerTestBase.Fixtures.saksbehandler1Principal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.*
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.failure
import no.nav.pensjon.brev.skribenten.common.Outcome.Companion.success
import no.nav.pensjon.brev.skribenten.fagsystem.*
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataResponse
import no.nav.pensjon.brev.skribenten.model.*
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brevbaker.api.model.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.core.statements.StatementInterceptor
import org.jetbrains.exposed.v1.jdbc.transactions.*
import org.junit.jupiter.api.*

class ReservertBrevHandlerTest {
    init {
        initADGroups()
    }

    @BeforeAll
    fun startDbOnce() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }

    private val principalEnhet = EnhetId("xxxx")

    private val brevbakerService = object : FakeBrevbakerService(redigerbareMaler = mutableMapOf(Testbrevkoder.INFORMASJONSBREV to informasjonsbrev)) {
        override suspend fun renderMarkup(brevkode: Brevkode.Redigerbart, spraak: LanguageCode, brevdata: RedigerbarBrevdata<*, *>, felles: BrevbakerFelles): LetterMarkupWithDataUsage {
            return LetterMarkupWithDataUsageImpl(letter, emptySet(), LetterMetadata.Brevtype.INFORMASJONSBREV)
        }
    }
    private val brevreservasjonPolicy = BrevreservasjonPolicy()
    private val penClient = object : PenClientStub() {
        override suspend fun hentPesysBrevdata(saksId: SaksId, vedtaksId: VedtaksId?, brevkode: Brevkode.Redigerbart, avsenderEnhetsId: EnhetId): BrevdataResponse.Data = brevdataResponseData
    }
    private val brevmalService = BrevmalService(brevbakerService, penClient, FakeBrevmetadataService())
    private val navansattService = FakeNavansattService(mapOf(saksbehandler1Principal.navIdent to principalEnhet to true))
    private val opprettBrevHandler = OpprettBrevHandlerImpl(
        opprettBrevPolicy = OpprettBrevPolicy(brevmalService, navansattService),
        brevreservasjonPolicy = brevreservasjonPolicy,
        brevmalService = brevmalService,
        brevdataService = BrevdataService(penClient, FakeSamhandlerService()),
        navansattService = navansattService,
    )


    private suspend fun opprettBrev(reserverBrev: Boolean = false) = withPrincipal(saksbehandler1Principal) {
        suspendTransaction(SharedPostgres.database) {
            opprettBrevHandler.invoke(
                OpprettBrevHandlerImpl.Request(
                    saksId = SaksId(1),
                    vedtaksId = VedtaksId(2),
                    brevkode = Testbrevkoder.INFORMASJONSBREV,
                    spraak = LanguageCode.BOKMAL,
                    avsenderEnhetsId = principalEnhet,
                    saksbehandlerValg = Api.GeneriskBrevdata(),
                    reserverForRedigering = reserverBrev,
                    mottaker = null,
                )
            )
        }
    }

    abstract inner class HentBrevStub : ReservertBrevHandler<HentBrevHandler.Request, Dto.Brevredigering>(SharedPostgres.database, brevreservasjonPolicy) {
        override suspend fun execute(request: HentBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError>? =
            success(BrevredigeringEntity[request.brevId].toDto(brevreservasjonPolicy, null))
    }

    @Test
    suspend fun `reserverer brev for handler som krever det`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(brev.info.redigeresAv).isNull()

        val result = withPrincipal(saksbehandler1Principal) {
            object : HentBrevStub() {
                override fun requiresReservasjon(request: HentBrevHandler.Request) = true
            }(HentBrevHandler.Request(brev.info.id))
        }

        assertThat(result).isSuccess { assertThat(it.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent) }
    }

    @Test
    suspend fun `reserverer ikke brev for handler som ikke krever det`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(brev.info.redigeresAv).isNull()

        val result = withPrincipal(saksbehandler1Principal) {
            object : HentBrevStub() {
                override fun requiresReservasjon(request: HentBrevHandler.Request) = false
            }(HentBrevHandler.Request(brev.info.id))
        }

        assertThat(result).isSuccess { assertThat(it.info.redigeresAv).isNull() }
    }

    @Test
    suspend fun `ruller tilbake transaksjon om handler feiler`() {
        val interceptor = DidRollbackInterceptor()
        val brev = opprettBrev().resultOrFail()

        withPrincipal(saksbehandler1Principal) {
            object : HentBrevStub() {
                override suspend fun execute(request: HentBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError> {
                    // Rollback i ytre transaksjon vil også utløse rollback i denne indre transaksjonen.
                    transaction(SharedPostgres.database) {
                        registerInterceptor(interceptor)
                    }
                    return failure(RedigerBrevPolicy.KanIkkeRedigere.LaastBrev)
                }

                override fun requiresReservasjon(request: HentBrevHandler.Request) = true
            }(HentBrevHandler.Request(brev.info.id))
        }

        assertThat(interceptor.didRollback).isTrue()
    }

    @Test
    suspend fun `ruller ikke tilbake transaksjon om handler er vellykket`() {
        val interceptor = DidRollbackInterceptor()
        val brev = opprettBrev().resultOrFail()

        withPrincipal(saksbehandler1Principal) {
            object : ReservertBrevHandler<HentBrevHandler.Request, Dto.Brevredigering>(SharedPostgres.database, brevreservasjonPolicy) {
                override suspend fun execute(request: HentBrevHandler.Request): Outcome<Dto.Brevredigering, BrevredigeringError> {
                    // Rollback i ytre transaksjon vil også utløse rollback i denne indre transaksjonen.
                    transaction {
                        registerInterceptor(interceptor)
                    }
                    return success(BrevredigeringEntity[brev.info.id].toDto(brevreservasjonPolicy, emptySet()))
                }
                override fun requiresReservasjon(request: HentBrevHandler.Request) = true
            }(HentBrevHandler.Request(brev.info.id))
        }

        assertThat(interceptor.didRollback).isFalse()
    }
}

private class DidRollbackInterceptor : StatementInterceptor {
    var didRollback = false
        private set

    override fun afterRollback(transaction: Transaction) {
        didRollback = true
    }
}
