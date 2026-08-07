package no.nav.brev.brevbaker.jackson

import no.nav.brev.brevbaker.markup.dsl.*
import no.nav.brev.brevbaker.markup.dsl.extended.*
import com.fasterxml.jackson.databind.module.SimpleModule
import no.nav.brev.brevbaker.markup.Attachment
import no.nav.pensjon.brev.api.model.BestillRedigertBrevRequestV2
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BestillRedigertBrevRequestV2Test {

    data class TestSaksbehandlerValg(val begrunnelse: String) : SaksbehandlerValgBrevdata
    data class TestPesysData(val belop: Int) : FagsystemBrevdata
    data class TestBrevdata(
        override val saksbehandlerValg: TestSaksbehandlerValg,
        override val pesysData: TestPesysData,
    ) : RedigerbarBrevdata<TestSaksbehandlerValg, TestPesysData>

    private val mapper = internalObjectMapper().registerModule(
        SimpleModule().apply {
            addAbstractTypeMapping(Brevkode.Redigerbart::class.java, RedigerbarBrevkode::class.java)
            addAbstractTypeMapping(RedigerbarBrevdata::class.java, TestBrevdata::class.java)
            addAbstractTypeMapping(SaksbehandlerValgBrevdata::class.java, TestSaksbehandlerValg::class.java)
            addAbstractTypeMapping(FagsystemBrevdata::class.java, TestPesysData::class.java)
        }
    )

    private fun request() = BestillRedigertBrevRequestV2(
        kode = RedigerbarBrevkode("TEST_BREV"),
        letterData = TestBrevdata(TestSaksbehandlerValg("fordi"), TestPesysData(1234)),
        felles = felles(),
        language = LanguageCode.BOKMAL,
        letterMarkup = MarkupGoldenFixture.letter(),
        alltidValgbareVedlegg = emptyList(),
        redigerteVedlegg = mapOf(BrevbakerType.VedleggId("vedlegg-1") to vedlegg()),
    )

    @Test
    fun `hele forespoerselen gaar rundt med den interne mapperen`() {
        val original = request()

        val json = mapper.writeValueAsString(original)
        val lest = mapper.readValue(json, object : com.fasterxml.jackson.core.type.TypeReference<BestillRedigertBrevRequestV2<Brevkode.Redigerbart>>() {})

        assertEquals(original, lest)
    }

    @Test
    fun `markup og api-model-common ligger side om side i samme payload`() {
        val json = mapper.readTree(mapper.writeValueAsString(request()))

        assertEquals("TEST_BREV", json.get("kode").textValue())
        assertEquals("1337123", json.get("felles").get("saksnummer").textValue())
        assertEquals(1234, json.get("letterData").get("pesysData").get("belop").intValue())
        assertEquals("TITLE2", json.get("letterMarkup").get("blocks").get(0).get("type").textValue())
    }

    private fun vedlegg(): Attachment {
        var next = 2000
        fun id() = next++
        return attachmentExtended(inkluderSaksinformasjon = false) {
            title1 { text(id(), "Vedleggstittel") }
            outline { paragraph(id()) { text(id(), "Vedleggstekst") } }
        }
    }

    private fun felles() = BrevbakerFelles(
        dokumentDato = LocalDate.of(2020, 1, 1),
        saksnummer = "1337123",
        avsenderEnhet = BrevbakerFelles.NavEnhet(
            nettside = "nav.no",
            navn = "Nav Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = BrevbakerType.Telefonnummer("55553334"),
        ),
        bruker = BrevbakerFelles.Bruker(
            fornavn = "Test",
            mellomnavn = null,
            etternavn = "Testerson",
            foedselsnummer = BrevbakerType.Foedselsnummer("01019878910"),
        ),
        signerendeSaksbehandlere = null,
        annenMottakerNavn = null,
    )
}
