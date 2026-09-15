package no.nav.pensjon.brev.skribenten.common

import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Mottaker
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MottakerType
import no.nav.pensjon.brev.skribenten.db.MottakerTable
import no.nav.pensjon.brev.skribenten.db.kryptering.KrypteringService
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Distribusjon
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.NavIdent
import no.nav.pensjon.brev.skribenten.model.NorskPostnummer
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.vendors.ForUpdateOption
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class UpdateMottakerTest {

    @BeforeAll
    fun startDb() {
        SharedPostgres.subscribeAndEnsureDatabaseInitialized(this)
        KrypteringService.init("ZBn9yGLDluLZVVGXKZxvnPun3kPQ2ccF")
    }

    @AfterAll
    fun kansellerDbAvhengighet() {
        SharedPostgres.cancelSubscription(this)
    }

    private val principal = NavIdent("abc")

    private fun createMottaker(navn: String): BrevId {
        val brevredigeringId = transaction {
            BrevredigeringEntity.opprettBrev(
                saksId = SaksId(456L),
                opprettetAv = principal,
                brevkode = Testbrevkoder.TESTBREV,
                spraak = LanguageCode.BOKMAL,
                avsenderEnhetId = EnhetId("1111"),
                saksbehandlerValg = SaksbehandlervalgMap(),
                distribusjonstype = Distribusjon.SENTRALPRINT,
                redigertBrev = Edit.Letter(
                    Edit.Title(listOf(Edit.ParagraphContent.Text.Literal(null, "a"))),
                    LetterMarkupImpl.SakspartImpl(
                        gjelderNavn = "b",
                        gjelderFoedselsnummer = Foedselsnummer("c"),
                        annenMottakerNavn = null,
                        saksnummer = "d",
                        dokumentDato = LocalDate.now(),
                    ),
                    emptyList(),
                    LetterMarkupImpl.SignaturImpl(
                        hilsenTekst = "f",
                        saksbehandlerNavn = "en signatur",
                        attesterendeSaksbehandlerNavn = "i",
                        navAvsenderEnhet = "j",
                    ),
                    emptySet(),
                ),
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                vedtaksId = null,
                timestamp = Instant.now(),
            ).id.value
        }

        transaction {
            Mottaker.new(brevredigeringId) {
                type = MottakerType.NORSK_ADRESSE
                this.navn = navn
                postnummer = NorskPostnummer("1234")
                poststed = "Lillevik"
                adresselinje1 = "Vei 1"
                manueltAdressertTil = Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT
                // manueltAdressertTilKryptert må settes eksplisitt her (Exposed sitt DAO-lag feiler
                // ved flush av insert av et uinitialisert nullable enum-transform-felt). De andre
                // *Kryptert-feltene lar vi bevisst stå usatt/null, for å simulere en rad fra før
                // krypteringen av mottakeradresse ble innført.
                manueltAdressertTilKryptert = Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT
            }
        }

        return brevredigeringId
    }

    @Test
    fun `updateMottaker krypterer klartekstfelter for eksisterende mottakere`() {
        val brevredigeringId = createMottaker(navn = "Kari Nordmann")

        JobConfig("test-oppdater-mottaker-${brevredigeringId.id}").updateMottaker()

        transaction {
            val mottaker = Mottaker[brevredigeringId]
            assertThat(mottaker.navnKryptert).isEqualTo("Kari Nordmann")
            assertThat(mottaker.postnummerKryptert).isEqualTo(NorskPostnummer("1234"))
            assertThat(mottaker.poststedKryptert).isEqualTo("Lillevik")
            assertThat(mottaker.adresselinje1Kryptert).isEqualTo("Vei 1")
            assertThat(mottaker.manueltAdressertTilKryptert).isEqualTo(Dto.Mottaker.ManueltAdressertTil.IKKE_RELEVANT)
        }
    }

    @Test
    fun `samtidig skriving mens updateMottaker kjorer blir ikke tapt i de krypterte kolonnene`() {
        val brevredigeringId = createMottaker(navn = "Gammelt Navn")

        val writerHarLaastRaden = CountDownLatch(1)
        val jobKanStarte = CountDownLatch(1)
        val executor = Executors.newFixedThreadPool(2)

        try {
            // Simulerer at EndreMottakerHandler alt er i gang med å redigere denne mottakeren (har
            // låst raden, men ikke commitet) idet updateMottaker() starter og når frem til den.
            val writerFuture = executor.submit {
                transaction {
                    MottakerTable
                        .selectAll()
                        .where { MottakerTable.id eq brevredigeringId }
                        .forUpdate(ForUpdateOption.ForUpdate)
                        .single()

                    writerHarLaastRaden.countDown()
                    jobKanStarte.await(2, TimeUnit.SECONDS)
                    // Gi jobben tid til å nå (og bli blokkert av radlåsen på) denne raden før vi
                    // committer den nye verdien.
                    Thread.sleep(500)

                    MottakerTable.update({ MottakerTable.id eq brevredigeringId }) { update ->
                        update[navn] = "Nytt Navn"
                        update[navnKryptert] = "Nytt Navn"
                    }
                }
            }

            assertThat(writerHarLaastRaden.await(2, TimeUnit.SECONDS)).isTrue()

            val jobFuture = executor.submit {
                jobKanStarte.countDown()
                JobConfig("test-oppdater-mottaker-race-${brevredigeringId.id}").updateMottaker()
            }

            writerFuture.get(5, TimeUnit.SECONDS)
            jobFuture.get(10, TimeUnit.SECONDS)
        } finally {
            executor.shutdownNow()
        }

        transaction {
            val mottaker = Mottaker[brevredigeringId]
            assertThat(mottaker.navn).isEqualTo("Nytt Navn")
            assertThat(mottaker.navnKryptert).isEqualTo("Nytt Navn")
        }
    }
}
