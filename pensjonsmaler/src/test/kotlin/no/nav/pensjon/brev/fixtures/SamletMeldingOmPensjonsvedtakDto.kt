package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Adresse
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.AvslaattPensjon
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Avslagsbegrunnelse
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.GrunnlagInnvilget
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.InnvilgetPensjon
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Institusjon
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.P1Person
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto.Reduksjonsgrunnlag
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import java.time.LocalDate

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
            landkode = "NO",
        ),
        forsikrede = P1Person(
            fornavn = "Lars",
            etternavn = "Holm",
            etternavnVedFoedsel = "Kirkerud",
            foedselsdato = "01.03.1990",
            adresselinje = "Storgata 1",
            poststed = "Lillevik vestre",
            postnummer = "4320",
            landkode = "NO",
        ),
        sakstype = Sakstype.ALDER,
        kravMottattDato = "1.2.1983",
        innvilgedePensjoner = listOf(
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Sakstype.ALDER,
                datoFoersteUtbetaling = "1. januar 2025",
                bruttobeloep = "1000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "FI",
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Sakstype.UFOREP,
                datoFoersteUtbetaling = "31. januar 2020",
                bruttobeloep = "2000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "DK",
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Sakstype.ALDER,
                datoFoersteUtbetaling = "1. januar 2025",
                bruttobeloep = "1000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "FI",
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Sakstype.UFOREP,
                datoFoersteUtbetaling = "31. januar 2020",
                bruttobeloep = "2000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "DK",
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Sakstype.ALDER,
                datoFoersteUtbetaling = "1. januar 2025",
                bruttobeloep = "1000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "FI",
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Sakstype.UFOREP,
                datoFoersteUtbetaling = "31. januar 2020",
                bruttobeloep = "2000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "DK",
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),

            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Sakstype.ALDER,
                datoFoersteUtbetaling = "1. januar 2025",
                bruttobeloep = "1000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "FI",
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Sakstype.UFOREP,
                datoFoersteUtbetaling = "31. januar 2020",
                bruttobeloep = "2000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "DK",
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Sakstype.ALDER,
                datoFoersteUtbetaling = "1. januar 2025",
                bruttobeloep = "1000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "FI",
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Sakstype.UFOREP,
                datoFoersteUtbetaling = "31. januar 2020",
                bruttobeloep = "2000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "DK",
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Sakstype.ALDER,
                datoFoersteUtbetaling = "1. januar 2025",
                bruttobeloep = "1000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "FI",
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Sakstype.UFOREP,
                datoFoersteUtbetaling = "31. januar 2020",
                bruttobeloep = "2000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "DK",
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Sakstype.ALDER,
                datoFoersteUtbetaling = "1. januar 2025",
                bruttobeloep = "1000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = "Eviglang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "FI",
                    postnummer = "4321",
                    poststed = "Lillevik Østre"
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Sakstype.UFOREP,
                datoFoersteUtbetaling = "31. januar 2020",
                bruttobeloep = "2000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "DK",
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            ),
        ),
        avslaattePensjoner =
            (0..<11).map { avslaattPensjon() },
        institusjon = Institusjon(
            navn = "NFP",
            adresselinje = "Lilleviksgrenda",
            poststed = "Lillevik",
            postnummer = "4322",
            landkode = "NO",
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
    pensjonstype = Sakstype.GJENLEV,
    avslagsbegrunnelse = Avslagsbegrunnelse.OpptjeningsperiodePaaMindreEnnEttAar,
    vurderingsperiode = "Mars 2025",
    adresseNyVurdering = Adresse(
        adresselinje1 = "Lillevik Torgvei 1",
        adresselinje2 = null,
        adresselinje3 = null,
        landkode = "FI",
        postnummer = "4321",
        poststed = "Lillevik Østre"
    )
)