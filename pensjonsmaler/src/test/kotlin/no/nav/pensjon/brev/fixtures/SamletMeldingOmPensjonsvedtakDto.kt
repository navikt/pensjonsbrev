package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.maler.Adresse
import no.nav.pensjon.brev.api.model.maler.AvslaattPensjon
import no.nav.pensjon.brev.api.model.maler.Avslagsbegrunnelse
import no.nav.pensjon.brev.api.model.maler.GrunnlagInnvilget
import no.nav.pensjon.brev.api.model.maler.InnvilgetPensjon
import no.nav.pensjon.brev.api.model.maler.Institution
import no.nav.pensjon.brev.api.model.maler.P1Person
import no.nav.pensjon.brev.api.model.maler.Reduksjonsgrunnlag
import no.nav.pensjon.brev.api.model.maler.SamletMeldingOmPensjonsvedtakDto
import java.time.LocalDate

fun createSamletMeldingOmPensjonsvedtakDto() =
    SamletMeldingOmPensjonsvedtakDto(
        holder = P1Person(
            fornavn = "Peder",
            etternavn = "Ås",
            etternavnVedFoedsel = "Aas",
            dateOfBirth = null,
            adresselinje = "Lillevik Torg",
            poststed = "Lillevik",
            postnummer = "4321",
            landkode = "NO",
        ),
        insuredPerson = P1Person(
            fornavn = "Lars",
            etternavn = "Holm",
            etternavnVedFoedsel = "Kirkerud",
            dateOfBirth = "01.03.1990",
            adresselinje = "Storgata 1",
            poststed = "Lillevik vestre",
            postnummer = "4320",
            landkode = "NO",
        ),
        sakstype = Sakstype.ALDER,
        innvilgedePensjoner = listOf(
            InnvilgetPensjon(
                institusjon = "NAY",
                type = Sakstype.ALDER,
                datoFoersteUtbetaling = "1. januar 2025",
                bruttobeloep = "1000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.NationalLegislation,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.InViewOfAnotherBenefitOrIncome,
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
                type = Sakstype.UFOREP,
                datoFoersteUtbetaling = "31. januar 2020",
                bruttobeloep = "2000 NOK",
                grunnlagInnvilget = GrunnlagInnvilget.ProRata,
                reduksjonsgrunnlag = Reduksjonsgrunnlag.InViewOfOverlappingOfCreditedPeriods,
                vurderingsperiode = "Ikke så lang",
                adresseNyVurdering = Adresse(
                    adresselinje1 = "Lillevik Torgvei 2",
                    adresselinje2 = null,
                    adresselinje3 = null,
                    landkode = "DK",
                    postnummer = "4324",
                    poststed = "Lillevik Vestre"
                ),
            )
        ),
        avslaattePensjoner =
            (0..<11).map { avslaattPensjon() },
        institution = Institution(
            name = "NFP",
            street = "Lilleviksgrenda",
            town = "Lillevik",
            postcode = "4322",
            countryCode = "NO",
            institutionID = "NFPL1",
            officeFax = "12134412",
            officePhone = "+123 45678901",
            email = "lars.holm@tøys.nfp.no",
            date = LocalDate.now(),
            signature = "Lars Holm, saksbehandler",
        )
    )

private fun avslaattPensjon() = AvslaattPensjon(
    institusjon = "NAY 4",
    type = Sakstype.GJENLEV,
    avslagsbegrunnelse = Avslagsbegrunnelse.InsurancePeriodsLessThanOneYear,
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