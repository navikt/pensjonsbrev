package no.nav.pensjon.brev.fixtures

import no.nav.brev.Landkode
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
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
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate
import java.time.Month

fun createSamletMeldingOmPensjonsvedtakDto() =
    SamletMeldingOmPensjonsvedtakDto(
        saksbehandlerValg = EmptySaksbehandlerValg,
        pesysData = SamletMeldingOmPensjonsvedtakDto.PesysData(
            sakstype = Sakstype.ALDER,
            vedlegg = createP1Dto()
        )
    )

fun createP1Dto() = P1Dto(
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
        innvilgedePensjoner = listOf(
            InnvilgetPensjon(
                institusjon = nay(),
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloepDesimal = "1000.00",
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
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay2(),
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloepDesimal = "2000",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "ett år og to dager",
                adresseNyVurdering = listOf(Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                )),
                utbetalingsHyppighet = P1Dto.Utbetalingshyppighet.Maaned12PerAar,
                valuta = "NOK",
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay(),
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloepDesimal = "1000.00",
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
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay2(),
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloepDesimal = "2000",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "ett år og to dager",
                adresseNyVurdering = listOf(Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                )),
                utbetalingsHyppighet = P1Dto.Utbetalingshyppighet.Maaned12PerAar,
                valuta = "NOK",
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay(),
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloepDesimal = "1000.00",
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
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay2(),
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloepDesimal = "2000.00",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "ett år og to dager",
                adresseNyVurdering = listOf(Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                )),
                utbetalingsHyppighet = P1Dto.Utbetalingshyppighet.Maaned12PerAar,
                valuta = "NOK",
                vedtaksdato = "2020-01-01",
            ),

            InnvilgetPensjon(
                institusjon = nay(),
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloepDesimal = "1000.00",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode =  "tjue år",
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
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay2(),
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloepDesimal = "2000",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "ett år og to dager",
                adresseNyVurdering = listOf(Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                )),
                utbetalingsHyppighet = P1Dto.Utbetalingshyppighet.Maaned12PerAar,
                valuta = "NOK",
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay(),
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloepDesimal = "1000.00",
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
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay2(),
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloepDesimal = "2000",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "ett år og to dager",
                adresseNyVurdering = listOf(Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                )),
                utbetalingsHyppighet = P1Dto.Utbetalingshyppighet.Maaned12PerAar,
                valuta = "NOK",
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay(),
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloepDesimal = "1000.00",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode =  "tjue år",
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
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay2(),
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloepDesimal = "2000",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "ett år og to dager",
                adresseNyVurdering = listOf(Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                )),
                utbetalingsHyppighet = P1Dto.Utbetalingshyppighet.Maaned12PerAar,
                valuta = "NOK",
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay(),
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloepDesimal = "1000.00",
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode =  "tjue år",
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
                vedtaksdato = "2020-01-01",
            ),
            InnvilgetPensjon(
                institusjon = nay2(),
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloepDesimal = "2000",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = "ett år og to dager",
                adresseNyVurdering = listOf(Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                )),
                utbetalingsHyppighet = P1Dto.Utbetalingshyppighet.Maaned12PerAar,
                valuta = "NOK",
                vedtaksdato = "2020-01-01",
            ),
        ),
        avslaattePensjoner =
            (0..<11).map { avslaattPensjon() },
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

private fun nay2(): List<P1Dto.Institusjon> = listOf(
    P1Dto.Institusjon(
        institusjonsid = null,
        institusjonsnavn = "NAY2",
        pin = null,
        saksnummer = null,
        land = null,
    )
)

private fun avslaattPensjon() = AvslaattPensjon(
    institusjoner = listOf(
        P1Dto.Institusjon(
            institusjonsid = null,
            institusjonsnavn = "NAY 4",
            pin = null,
            saksnummer = null,
            land = null,
        ),
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