package no.nav.pensjon.brev.fixtures

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.AvviksResultat
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk
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
        barnetillegg = null,
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