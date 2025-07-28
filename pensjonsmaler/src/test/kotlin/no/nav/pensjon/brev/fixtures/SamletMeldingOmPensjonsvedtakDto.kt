package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.Penger
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Adresse
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.AvslaattPensjon
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Avslagsbegrunnelse
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.GrunnlagInnvilget
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.InnvilgetPensjon
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Institusjon
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Landkode
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.P1Person
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Pensjonstype
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Reduksjonsgrunnlag
import no.nav.pensjon.brev.api.model.maler.Valuta
import java.math.BigDecimal
import java.time.LocalDate
import java.time.Month

fun createSamletMeldingOmPensjonsvedtakDto() =
    SamletMeldingOmPensjonsvedtakDto(
        innehaver = P1Person(
            fornavn = "Peder",
            etternavn = "Ås",
            etternavnVedFoedsel = "Aas",
            foedselsdato = null,
            adresselinje = "Lillevik Torg",
            poststed = "Lillevik",
            postnummer = "4321",
            landkode = Landkode.NO
        ),
        forsikrede = P1Person(
            fornavn = "Lars",
            etternavn = "Holm",
            etternavnVedFoedsel = "Kirkerud",
            foedselsdato = LocalDate.of(1990, Month.MARCH, 1),
            adresselinje = "Storgata 1",
            poststed = "Lillevik vestre",
            postnummer = "4320",
            landkode = Landkode.NO,
        ),
        sakstype = Sakstype.ALDER,
        kravMottattDato = LocalDate.of(1983, Month.FEBRUARY, 2),
        innvilgedePensjoner = listOf(
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(BigDecimal.valueOf(1000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(BigDecimal.valueOf(2000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(BigDecimal.valueOf(1000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(BigDecimal.valueOf(2000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(BigDecimal.valueOf(1000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(BigDecimal.valueOf(2000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),

            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(BigDecimal.valueOf(1000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(BigDecimal.valueOf(2000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(BigDecimal.valueOf(1000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(BigDecimal.valueOf(2000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(BigDecimal.valueOf(1000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(BigDecimal.valueOf(2000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(BigDecimal.valueOf(1000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(BigDecimal.valueOf(2000), Valuta.NOK),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
        ),
        avslaattePensjoner =
            (0..<11).map { avslaattPensjon() },
        utfyllendeInstitusjon = Institusjon(
            navn = "NFP",
            adresselinje = "Lilleviksgrenda",
            poststed = "Lillevik",
            postnummer = "4322",
            landkode = Landkode.NO,
            institusjonsID = "NFPL1",
            faksnummer = "12134412",
            telefonnummer = "+123 45678901",
            epost = "lars.holm@tøys.nfp.no",
            dato = LocalDate.now(),
            underskrift = "Lars Holm, saksbehandler",
        )
    )

private fun avslaattPensjon() = AvslaattPensjon(
    institusjon = "NAY 4",
    pensjonstype = Pensjonstype.Etterlatte,
    avslagsbegrunnelse = Avslagsbegrunnelse.OpptjeningsperiodePaaMindreEnnEttAar,
    vurderingsperiode = "Mars 2025",
    adresseNyVurdering = Adresse(
        adresselinje1 = "Lillevik Torgvei 1",
        adresselinje2 = null,
        adresselinje3 = null,
        landkode = Landkode("FI"),
        postnummer = "4321",
        poststed = "Lillevik Østre"
    )
)