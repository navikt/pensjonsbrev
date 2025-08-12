package no.nav.pensjon.brev.fixtures

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
import no.nav.pensjon.brev.api.model.maler.P1Dto.Institusjon
import no.nav.pensjon.brev.api.model.maler.P1Dto.P1Person
import no.nav.pensjon.brev.api.model.maler.P1Dto.Pensjonstype
import no.nav.pensjon.brev.api.model.maler.P1Dto.Postnummer
import no.nav.pensjon.brev.api.model.maler.P1Dto.Poststed
import no.nav.pensjon.brev.api.model.maler.P1Dto.Reduksjonsgrunnlag
import no.nav.pensjon.brev.api.model.maler.Penger
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import no.nav.pensjon.brev.api.model.maler.Valuta
import no.nav.pensjon.brevbaker.api.model.Telefonnummer
import java.time.LocalDate
import java.time.Month
import java.time.Period

fun createSamletMeldingOmPensjonsvedtakDto() =
    SamletMeldingOmPensjonsvedtakDto(
        saksbehandlerValg = EmptyBrevdata,
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
        kravMottattDato = LocalDate.of(1983, Month.FEBRUARY, 2),
        innvilgedePensjoner = listOf(
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(1000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = Period.of(20, 1, 10),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = Postnummer("4321"),
                    poststed = Poststed("Lillevik Østre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(2000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = Period.of(1, 2, 3),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(1000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = Period.of(20, 1, 10),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = Postnummer("4321"),
                    poststed = Poststed("Lillevik Østre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(2000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = Period.of(1, 2, 3),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(1000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = Period.of(20, 1, 10),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = Postnummer("4321"),
                    poststed = Poststed("Lillevik Østre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(2000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = Period.of(1, 2, 3),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                ),
            ),

            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(1000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode =  Period.of(20, 1, 10),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = Postnummer("4321"),
                    poststed = Poststed("Lillevik Østre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(2000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = Period.of(1, 2, 3),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(1000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode = Period.of(20, 1, 10),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = Postnummer("4321"),
                    poststed = Poststed("Lillevik Østre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(2000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = Period.of(1, 2, 3),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(1000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode =  Period.of(20, 1, 10),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = Postnummer("4321"),
                    poststed = Poststed("Lillevik Østre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(2000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = Period.of(1, 2, 3),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY",
                pensjonstype = Pensjonstype.Alder,
                datoFoersteUtbetaling = LocalDate.of(2025, Month.JANUARY, 1),
                bruttobeloep = Penger(1000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.IHenholdTilNasjonalLovgivning,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvAndreYtelserEllerAnnenInntekt,
                vurderingsperiode =  Period.of(20, 1, 10),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 1",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("FI"),
                    postnummer = Postnummer("4321"),
                    poststed = Poststed("Lillevik Østre")
                ),
            ),
            InnvilgetPensjon(
                institusjon = "NAY2",
                pensjonstype = Pensjonstype.Ufoere,
                datoFoersteUtbetaling = LocalDate.of(2020, Month.JANUARY, 31),
                bruttobeloep = Penger(2000, Valuta("NOK")),
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.PaaGrunnAvOverlappendeGodskrevnePerioder,
                vurderingsperiode = Period.of(1, 2, 3),
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = Landkode("DK"),
                    postnummer = Postnummer("4324"),
                    poststed = Poststed("Lillevik Vestre")
                ),
            ),
        ),
        avslaattePensjoner =
            (0..<11).map { avslaattPensjon() },
        utfyllendeInstitusjon = Institusjon(
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
            underskrift = "Lars Holm, saksbehandler",
        )
    )

private fun avslaattPensjon() = AvslaattPensjon(
    institusjon = "NAY 4",
    pensjonstype = Pensjonstype.Etterlatte,
    avslagsbegrunnelse = Avslagsbegrunnelse.OpptjeningsperiodePaaMindreEnnEttAar,
    vurderingsperiode = Period.of(0, 1, 0),
    adresseNyVurdering = Adresse(
        adresselinje1 = "Lillevik Torgvei 1",
        adresselinje2 = null,
        adresselinje3 = null,
        landkode = Landkode("FI"),
        postnummer = Postnummer("4321"),
        poststed = Poststed("Lillevik Østre")
    )
)