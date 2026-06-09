package no.nav.pensjon.brev.fixtures.legacy

import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Avdoed
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Beregning
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Beregningsmetode
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Bruker
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.PesysData
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.PoengAar
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Poengrekke
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Poengtallstype
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.Sluttpoengtall
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.TrygdetidScalars
import no.nav.pensjon.brev.api.model.maler.legacy.OpplysningerOmBeregningenGPUtlandDto.YrkesskadeBeregning
import no.nav.pensjon.brev.api.model.vedlegg.Trygdetid
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Foedselsnummer
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

fun createOpplysningerOmBeregningGPUtlandDto() =
    OpplysningerOmBeregningenGPUtlandDto(
        pesysData = PesysData(
            virkDatoFom = LocalDate.of(2026, 1, 1),
            bruker = Bruker(
                flyktning = true,
                forventetInntekt = Kroner(250_000),
                samboer3_2 = true,
                ektefelleMottarPensjon = false,
                ektefelleInntektOver2g = false,
            ),
            avdoed = Avdoed(
                fnr = Foedselsnummer("01015012345"),
                flyktning = false,
                doedsdato = LocalDate.of(2024, 6, 15),
                doedsfallSkyldesYrkesskade = false,
                skadetidspunktYrkesskade = null,
                ungUfoer = false,
                avtaleland = "Spania",
                ttNordiskAar = 0,
                ttNordiskMnd = 0,
                trygdetidsgrunnlagNorge = listOf(
                    Trygdetid(
                        fom = LocalDate.of(1980, 1, 1),
                        tom = LocalDate.of(2010, 12, 31),
                        land = "Norge",
                    ),
                ),
                trygdetidsgrunnlagEos = listOf(
                    Trygdetid(
                        land = "Spania",
                        fom = LocalDate.of(2011, 1, 1),
                        tom = LocalDate.of(2020, 12, 31),
                    ),
                ),
                trygdetidsgrunnlagBilateral = emptyList(),
            ),
            beregning = Beregning(
                tpInnvilget = true,
                beregningsmetode = Beregningsmetode.EOS,
                ttAnvBest = 30,
                ttAnv = 30,
                yug = 0,
                sluttpoengtall = Sluttpoengtall(
                    sptUtenOk = 4.5,
                    optMedOk = 0.0,
                    sptUtenOkEos = 4.5,
                    optMedOkEos = 0.0,
                    sptUtenOkNordisk = 0.0,
                    optMedOkNordisk = 0.0,
                    sptUtenOkMinusOptMedOkAvdoed = 0.0,
                    sptUtenOkEosMinusOptMedOkEos2 = Kroner(0),
                    sptUtenOkNordiskMinusOptMedOkNordisk2 = Kroner(0),
                ),
                poengrekke = Poengrekke(
                    populert = true,
                    poengaarUtenOk = 30,
                    poengaarUtenOkF92 = 0,
                    poengaarUtenOkE91 = 0,
                    poengaarUtenOkFaktiskeNorge = 20,
                    poengaarUtenOkFaktiskeEos = 10,
                    poengaarUtenOkFaktiskeNorden = 0,
                    poengaarUtenOkTeoretiskEos = 30,
                    framtidigPoengtall = 0,
                    framtidigPoengaarNordenBrutto = 0,
                    framtidigPoengaarNordenNetto = 0,
                    poengaarTellerEos = 20,
                    poengaarNevnerEos = 30,
                    poengaarUtenOkFaktiskNorgePlusEos2 = 30,
                    poengaarUtenOkFaktiskNorgePlusFramtidigPoengaarNordenNetto2 = 30,
                    poengaarUtenOkFaktiskNorgePlusFaktiskeNorden2 = 20,
                    aar = listOf(
                        PoengAar(
                            aarstall = Year(1990),
                            pensjonsgivendeInntekt = Kroner(250_000),
                            grunnbeloepVeiet = Kroner(60_000),
                            pensjonspoeng = 4.5,
                            poengtallstype = Poengtallstype.ANNET,
                        ),
                        PoengAar(
                            aarstall = Year(1991),
                            pensjonsgivendeInntekt = Kroner(180_000),
                            grunnbeloepVeiet = Kroner(62_000),
                            pensjonspoeng = 3.0,
                            poengtallstype = Poengtallstype.OMSORGSPOENG_J,
                        ),
                    ),
                ),
                yrke = YrkesskadeBeregning(
                    sluttpoengtallYrke = 0.0,
                    poengaarYrke = 0,
                    poengaarYrkeF92 = 0,
                    poengaarYrkeE91 = 0,
                ),
                trygdetid = TrygdetidScalars(
                    faTTNorge = 360,
                    faTTEos = 120,
                    ttNordisk = 0,
                    framtidigTTEos = 0,
                    framtidigTTNorsk = 0,
                    ttTellerEos = 360,
                    ttNevnerEos = 480,
                    ttTellerNordisk = 0,
                    ttNevnerNordisk = 0,
                    faTTBilateral = 0,
                    framtidigTTAvtaleland = 0,
                    ttTellerBilateral = 0,
                    ttNevnerBilateral = 0,
                    faTTNorgePlusFaTTEos = 480,
                    faTTNorgePlusFaTTBilateral = 0,
                    faTTNorgePlusFaTTA10Netto = 0,
                ),
            ),
        ),
    )

