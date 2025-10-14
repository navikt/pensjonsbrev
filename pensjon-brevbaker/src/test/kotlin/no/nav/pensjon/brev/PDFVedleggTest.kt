package no.nav.pensjon.brev

import no.nav.brev.brevbaker.Fixtures
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.template.Language
import org.junit.jupiter.api.Tag
import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.P1Dto
import no.nav.pensjon.brev.api.model.maler.P1Dto.Adresse
import no.nav.pensjon.brev.api.model.maler.P1Dto.AvslaattPensjon
import no.nav.pensjon.brev.api.model.maler.P1Dto.Avslagsbegrunnelse
import no.nav.pensjon.brev.api.model.maler.P1Dto.Epost
import no.nav.pensjon.brev.api.model.maler.P1Dto.GrunnlagInnvilget
import no.nav.pensjon.brev.api.model.maler.P1Dto.InnvilgetPensjon
import no.nav.pensjon.brev.api.model.maler.P1Dto.UtfyllendeInstitusjon
import no.nav.pensjon.brev.api.model.maler.P1Dto.P1Person
import no.nav.pensjon.brev.api.model.maler.P1Dto.Pensjonstype
import no.nav.pensjon.brev.api.model.maler.P1Dto.Postnummer
import no.nav.pensjon.brev.api.model.maler.P1Dto.Poststed
import no.nav.pensjon.brev.api.model.maler.P1Dto.Reduksjonsgrunnlag
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import no.nav.pensjon.brev.maler.SamletMeldingOmPensjonsvedtak
import no.nav.pensjon.brev.pdfvedlegg.PDFVedleggAppenderImpl
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class PDFVedleggTest {

    @Tag(TestTags.MANUAL_TEST)
    @Test
    fun testPdf() {
        val template = SamletMeldingOmPensjonsvedtak.template
        val brevkode = SamletMeldingOmPensjonsvedtak.kode
        val spraak = Language.Bokmal
        if (!template.language.supports(spraak)) {
            println("Mal ${template.letterMetadata.displayTitle} med brevkode ${brevkode.kode()} fins ikke på språk ${spraak.javaClass.simpleName.lowercase()}, tester ikke denne")
            return
        }
        val letter = LetterTestImpl(template, createSamletMeldingOmPensjonsvedtakDto(innvilget = 1, avslag = 6), spraak, Fixtures.felles)

        letter.renderTestPDF("${brevkode.kode()}_${spraak.javaClass.simpleName}", pdfVedleggAppender = PDFVedleggAppenderImpl)
    }
}

fun createSamletMeldingOmPensjonsvedtakDto(innvilget: Int, avslag: Int) =
    SamletMeldingOmPensjonsvedtakDto(
        saksbehandlerValg = EmptyBrevdata,
        pesysData = SamletMeldingOmPensjonsvedtakDto.PesysData(
            sakstype = Sakstype.ALDER,
            vedlegg = createP1Dto(innvilget, avslag)
        )
    )

fun createP1Dto(innvilget: Int, avslag: Int) = P1Dto(
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
    innvilgedePensjoner = (0..<innvilget).map { InnvilgetPensjon(
            institusjon = nay(),
            pensjonstype = Pensjonstype.Alder,
            datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
            bruttobeloep = 1000,
            grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
            reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
            vurderingsperiode = "tjue år",
            adresseNyVurdering = listOf(Adresse(
                adresselinje1 = "Lillevik Torgvei 1",
                adresselinje2 = null,
                adresselinje3 = null,
                landkode = Landkode("FI"),
                postnummer = Postnummer("4321"),
                poststed = Poststed("Lillevik Østre")
            )),
            utbetalingsHyppighet = P1Dto.Utbetalingshyppighet.Maaned12PerAar,
            valuta = "NOK",
            vedtaksdato = "2020-01-01"
        )
    },
    avslaattePensjoner =
        (0..<avslag).map { avslaattPensjon() },
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
        dato = LocalDate.now(),
    )
)

private fun nay(): List<P1Dto.Institusjon> = listOf(
    P1Dto.Institusjon(
        institusjonsid = null,
        institusjonsnavn = "NAY",
        pin = null,
        saksnummer = null,
        land = null,
    )
)

private fun avslaattPensjon() = AvslaattPensjon(
    institusjon = P1Dto.Institusjon(
        institusjonsid = null,
        institusjonsnavn = "NAY 4",
        pin = null,
        saksnummer = null,
        land = null,
    ),
    pensjonstype = Pensjonstype.Etterlatte,
    avslagsbegrunnelse = Avslagsbegrunnelse.OpptjeningsperiodePaaMindreEnnEttAar,
    vurderingsperiode = "en måned",
    adresseNyVurdering = listOf(Adresse(
        adresselinje1 = "Lillevik Torgvei 1",
        adresselinje2 = null,
        adresselinje3 = null,
        landkode = Landkode("FI"),
        postnummer = Postnummer("4321"),
        poststed = Poststed("Lillevik Østre")
    )),
    vedtaksdato = "2020-01-01"
)