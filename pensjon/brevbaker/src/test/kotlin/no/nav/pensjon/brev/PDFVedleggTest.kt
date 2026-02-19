package no.nav.pensjon.brev

import no.nav.brev.Landkode
import no.nav.brev.brevbaker.FellesFactory
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.brev.brevbaker.vilkaarligDato
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.P1RedigerbarDto
import no.nav.pensjon.brev.api.model.maler.P1RedigerbarDto.*
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakV2Dto
import no.nav.pensjon.brev.maler.SamletMeldingOmPensjonsvedtakV2
import no.nav.pensjon.brev.pdfvedlegg.PDFVedleggAppenderImpl
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.BrevWrappers.Telefonnummer
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class PDFVedleggTest {

    @Tag(TestTags.INTEGRATION_TEST)
    @Test
    fun testPdf() {
        val template = SamletMeldingOmPensjonsvedtakV2.template
        val brevkode = SamletMeldingOmPensjonsvedtakV2.kode
        val spraak = Language.Bokmal
        if (!template.language.supports(spraak)) {
            println("Mal ${template.letterMetadata.displayTitle} med brevkode ${brevkode.kode()} fins ikke på språk ${spraak.javaClass.simpleName.lowercase()}, tester ikke denne")
            return
        }
        val letter = LetterTestImpl(
            template,
            createSamletMeldingOmPensjonsvedtakV2UtenVedleggDto(),
            spraak,
            FellesFactory.felles
        )

        letter.renderTestPDF(
            "${brevkode.kode()}_${spraak.javaClass.simpleName}",
            pdfVedleggAppender = PDFVedleggAppenderImpl
        )
    }
}

fun createSamletMeldingOmPensjonsvedtakV2UtenVedleggDto() =
    SamletMeldingOmPensjonsvedtakV2Dto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = SamletMeldingOmPensjonsvedtakV2Dto.PesysData(
            sakstype = Sakstype.ALDER,
            p1Vedlegg = null
        ),
    )


fun createSamletMeldingOmPensjonsvedtakV2Dto() =
    SamletMeldingOmPensjonsvedtakV2Dto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = SamletMeldingOmPensjonsvedtakV2Dto.PesysData(
            sakstype = Sakstype.ALDER,
            p1Vedlegg = createP1VedleggDto(),
        ),
    )

private val ADRESSE_EKSEMPEL = "Lillevik Torgvei 1\n4321\nLillevik Østre\nDanmark"

fun createP1VedleggDto() = P1RedigerbarDto(
    innehaver = P1Person(
        fornavn = "Peder",
        etternavn = "Ås",
        etternavnVedFoedsel = "Aas",
        foedselsdato = null,
        adresselinje = "Lillevik Torg",
        poststed = Poststed("Lillevik"),
        postnummer = Postnummer("4321"),
        landkode = Landkode("NO")
    ),
    forsikrede = P1Person(
        fornavn = "Lars",
        etternavn = "Holm",
        etternavnVedFoedsel = "Kirkerud",
        foedselsdato = LocalDate.of(1990, Month.MARCH, 1),
        adresselinje = "Storgata 1",
        poststed = Poststed("Lillevik vestre"),
        postnummer = Postnummer("4320"),
        landkode = Landkode("NO"),
    ),
    sakstype = Sakstype.ALDER,
    innvilgedePensjoner = (0..<6).map { innvilgetPensjon },
    avslaattePensjoner = (0..<6).map { avslaattPensjon },
    utfyllendeInstitusjon = UtfyllendeInstitusjon(
        navn = "NFP",
        adresselinje = "Lilleviksgrenda",
        poststed = Poststed("Lillevik"),
        postnummer = Postnummer("4322"),
        landkode = Landkode("NO"),
        institusjonsID = "NFPL1",
        faksnummer = "12134412",
        telefonnummer = Telefonnummer("+123 45678901"),
        epost = Epost("lars.holm@tøys.nfp.no"),
    )
)

private val nay =
    Institusjon(
        institusjonsnavn = "NAY",
        pin = "1234",
        saksnummer = "1234",
        datoForVedtak = LocalDate.of(2020, Month.JANUARY, 1),
        land = "Norge"
    )


private val innvilgetPensjon = InnvilgetPensjon(
    institusjon = nay,
    pensjonstype = Pensjonstype.Etterlatte,
    vurderingsperiode = "en måned",
    datoFoersteUtbetaling = vilkaarligDato,
    utbetalt = "1000 Kroner fra en dato",
    grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
    reduksjonsgrunnlag = null,
    adresseNyVurdering = ADRESSE_EKSEMPEL,
)


private val avslaattPensjon = AvslaattPensjon(
    institusjon = nay,
    pensjonstype = Pensjonstype.Etterlatte,
    avslagsbegrunnelse = Avslagsbegrunnelse.OpptjeningsperiodePaaMindreEnnEttAar,
    vurderingsperiode = "en måned",
    adresseNyVurdering = ADRESSE_EKSEMPEL,
)