package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.*
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.Barnetillegg.Saerkullsbarn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

fun createForhaandsvarselEtteroppgjoerUfoeretrygdDto() =
    ForhaandsvarselEtteroppgjoerUfoeretrygdDto(
        erNyttEtteroppgjoer = false,
        harTjentOver80prosentAvOIFU = false,
        kanSoekeOmNyInntektsgrense = false,
        oppjustertInntektFoerUfoerhet = Kroner(0),
        opplysningerOmEtteroppgjoeretUfoeretrygd = Fixtures.create(),
        orienteringOmRettigheterUfoere = Fixtures.create(),
    )

fun createForhaandsvarselEtteroppgjoerUfoeretrygdDtoOpplysningerOmEtteroppgjoret() =
    OpplysningerOmEtteroppgjoeretDto(
        barnetillegg = Barnetillegg(
            felles = null,
            saerkull = Saerkullsbarn(
                fribeloep = Kroner(10),
                resultat = AvviksResultat(Kroner(10), Kroner(8), Kroner(-2), false),
                harSamletInntektOverInntektstak = false,
                samletInntekt = Kroner(10),
                inntektstakSamletInntekt = Kroner(12),
            ),
            personinntekt = InntektOgFratrekk(Inntekt(emptyList(), Kroner(0)), Fratrekk(emptyList(), Kroner(0))),
            mindreEnn40AarTrygdetid = true,
            totaltResultat = AvviksResultat(Kroner(10), Kroner(8), Kroner(-2), false),
        ),
        harGjenlevendeTillegg = false,
        harFaattForMye = true,
        pensjonsgivendeInntekt = InntektOgFratrekk(
            inntekt = Inntekt(emptyList(), Kroner(0)),
            fratrekk = Fratrekk(emptyList(), Kroner(0)),
        ),
        pensjonsgivendeInntektBruktIBeregningen = Kroner(400000),
        periode = Year(2022),
        totaltAvvik = Kroner(50000),
        ufoeretrygd = AvviksResultat(
            skulleFaatt = Kroner(100000),
            fikk = Kroner(150000),
            avvik = Kroner(50000),
            harFaattForMye = false,
        ),
    )