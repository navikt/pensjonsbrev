package no.nav.pensjon.brev.skribenten.vedlegg

import no.nav.brev.BrevLandmodell.Landkode
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.AvslaattPensjon
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.Avslagsbegrunnelse
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.GrunnlagInnvilget
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.InnvilgetPensjon
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.Institusjon
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.P1Person
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.Pensjonstype
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.Postnummer
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.Poststed
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.Reduksjonsgrunnlag
import no.nav.pensjon.brev.skribenten.vedlegg.P1RedigerbarDto.UtfyllendeInstitusjon
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.Bruker
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.NavEnhet
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Telefonnummer
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Month

class P1pdfV2DtoTest {

    @Test
    fun `P1pdfV2Dto genererer forventet antall sider med utfylte felt`() {
        val vedlegg = P1pdfV2Dto.create(createP1RedigerbarDto(), felles)

        assertEquals(8, vedlegg.sider.size)
        assertFalse(vedlegg.sider.map { it.felt }.any { it.isEmpty() })
    }

    private val felles = BrevbakerFelles(
        dokumentDato = LocalDate.now(),
        saksnummer = "12345678",
        avsenderEnhet = NavEnhet(
            nettside = "nav.no",
            navn = "Nav Familie- og pensjonsytelser Porsgrunn",
            telefonnummer = Telefonnummer("55553334"),
        ),
        bruker = Bruker(
            fornavn = "Test",
            mellomnavn = null,
            etternavn = "Testerson",
            foedselsnummer = Foedselsnummer("01019878910"),
        ),
        signerendeSaksbehandlere = SignerendeSaksbehandlere(
            saksbehandler = "Ole Saksbehandler",
            attesterendeSaksbehandler = "Per Attesterende",
        ),
        annenMottakerNavn = null,
    )

    private fun createP1RedigerbarDto() = P1RedigerbarDto(
        innehaver = P1Person(
            fornavn = "Peder",
            etternavn = "Ås",
            etternavnVedFoedsel = "Aas",
            foedselsdato = null,
            adresselinje = "Lillevik Torg",
            poststed = Poststed("Lillevik"),
            postnummer = Postnummer("4321"),
            landkode = Landkode("NO"),
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
        sakstype = Sakstype("ALDER"),
        // 15 innvilgede pensjoner gir 3 sider (chunket 5 om gangen)
        innvilgedePensjoner = (0..<15).map { innvilgetPensjon() },
        // 12 avslåtte pensjoner gir 3 sider (chunket 5 om gangen)
        avslaattePensjoner = (0..<12).map { avslaattPensjon() },
        utfyllendeInstitusjon = UtfyllendeInstitusjon(
            navn = "NFP",
            adresselinje = "Lilleviksgrenda",
            poststed = Poststed("Lillevik"),
            postnummer = Postnummer("4322"),
            landkode = Landkode("NO"),
            institusjonsID = "NFPL1",
            faksnummer = "12134412",
            telefonnummer = Telefonnummer("+123 45678901"),
            epost = P1RedigerbarDto.Epost("lars.holm@nfp.no"),
        ),
    )

    private fun innvilgetPensjon() = InnvilgetPensjon(
        institusjon = Institusjon(
            institusjonsnavn = "NAY",
            pin = "1234",
            saksnummer = "1234",
            datoForVedtak = LocalDate.of(2020, Month.JANUARY, 1),
            land = "NO",
        ),
        pensjonstype = Pensjonstype.Alder,
        datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
        utbetalt = "1000.00",
        grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
        reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
        vurderingsperiode = "tjue år",
        adresseNyVurdering = "Lillevik Torgvei 1, 4321 Lillevik Østre",
    )

    private fun avslaattPensjon() = AvslaattPensjon(
        institusjon = Institusjon(
            institusjonsnavn = "NAY",
            pin = "1234",
            saksnummer = "1234",
            datoForVedtak = LocalDate.of(2020, Month.JANUARY, 1),
            land = "NO",
        ),
        pensjonstype = Pensjonstype.Etterlatte,
        avslagsbegrunnelse = Avslagsbegrunnelse.OpptjeningsperiodePaaMindreEnnEttAar,
        vurderingsperiode = "en måned",
        adresseNyVurdering = "Lillevik Torgvei 1, 4321 Lillevik Østre",
    )
}
