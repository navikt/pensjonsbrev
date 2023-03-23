package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.harBeloepRedusert
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.harTotalNettoUT
import no.nav.pensjon.brev.api.model.vedlegg.GjenlevendetilleggGjeldeneSelectors.harGjenlevendetillegg
import no.nav.pensjon.brev.api.model.vedlegg.GjenlevendetilleggGjeldeneSelectors.harNyttGjenlevendetillegg
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereGjeldendeSelectors.ifuInntekt
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereGjeldendeSelectors.oifuInntekt
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.forventetInntektAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.harForventetInntektLargerThanInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.harInntektsgrenseLargerThanOrEqualToInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.harInntektsgrenseLessThanInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.nettoPerAarReduksjonUT
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.overskytendeInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregningUfoere
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.borIUtlandet
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.fraOgMedDatoErNesteAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.gjenlevendetilleggGjeldene
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harBarnetilleggInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harBrukerKonvertertUP
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harEktefelletilleggInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harKravaarsakEndringInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektEtterUfoereGjeldende_beloepIEU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoereBegrunnelse
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektsAvkortingGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.kravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.minsteytelseGjeldende_sats
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.norskTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.opptjeningAvdoedUfoeretrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.opptjeningUfoeretrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdOrdinaer
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ungUfoerGjeldende_erUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.utenlandskTrygdetidBilateral
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.utenlandskTrygdetidEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.fastsattTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.har40AarFastsattTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harFramtidigTrygdetidEOS
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harFramtidigTrygdetidNorsk
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harLikUfoeregradOgYrkesskadegrad
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harTrygdetidsgrunnlag
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harYrkesskadeOppfylt
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.harDelvisUfoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.harFullUfoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.harUtbetalingsgradLessThanUfoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.ugradertBruttoPerAar
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.fradrag
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harGammelUTBeloepUlikNyUTBeloep
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harNyUTBeloep
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoAkkumulerteBeloepUtbetalt
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoAkkumulertePlussNettoRestAar
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoTilUtbetalingRestenAvAaret
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.ufoeretrygdPlussInntekt
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.textExpr


fun createVedleggOpplysningerBruktIBeregningUT(skalViseMinsteytelse: Boolean, skalViseBarnetillegg: Boolean) =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
        title = newText(
            Bokmal to "Opplysninger om beregningen",
            Nynorsk to "Opplysningar om utrekninga",
            English to "Information about calculations"
        ),
        includeSakspart = false,
    ) {
        val inntektsgrenseErUnderTak =
            inntektsAvkortingGjeldende.inntektsgrenseAar.lessThan(inntektsAvkortingGjeldende.inntektstak)
        val grunnbeloep = beregnetUTPerManedGjeldende.grunnbeloep.format()

        paragraph {
            val virkDatoFom = beregnetUTPerManedGjeldende.virkDatoFom.format()
            textExpr(
                Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkDatoFom + ". Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep + " kroner.",
                Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr() + virkDatoFom + ". Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep + " kroner.",
                English to "Data we have used in the calculations as of ".expr() + virkDatoFom + ". The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbeloep + ".",
            )
        }

        includePhrase(
            TabellUfoereOpplysninger(
                ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                yrkesskadeGjeldende = yrkesskadeGjeldende,
                inntektFoerUfoereGjeldende = inntektFoerUfoereGjeldende,
                inntektsAvkortingGjeldende = inntektsAvkortingGjeldende,
                inntektsgrenseErUnderTak = inntektsgrenseErUnderTak,
                beregnetUTPerManedGjeldende = beregnetUTPerManedGjeldende,
                inntektEtterUfoereGjeldendeBeloep = inntektEtterUfoereGjeldende_beloepIEU,
                ungUfoerGjeldende_erUnder20Aar = ungUfoerGjeldende_erUnder20Aar,
                trygdetidsdetaljerGjeldende = trygdetidsdetaljerGjeldende,
                barnetilleggGjeldende = barnetilleggGjeldende,
            )
        )
        if (skalViseMinsteytelse) {
            val harMinsteytelseSats = minsteytelseGjeldende_sats.ifNull(0.0).greaterThan(0.0)
            showIf(harMinsteytelseSats) {
                includePhrase(
                    OpplysningerOmMinstetillegg(
                        minsteytelseGjeldendeSats = minsteytelseGjeldende_sats,
                        ungUfoerGjeldende_erUnder20Aar = ungUfoerGjeldende_erUnder20Aar,
                        ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                        inntektFoerUfoereGjeldende = inntektFoerUfoereGjeldende,
                        inntektsgrenseErUnderTak = inntektsgrenseErUnderTak,
                    )
                )
            }
        }

        if (skalViseBarnetillegg) {
            ifNotNull(barnetilleggGjeldende) { barnetillegg ->
                includePhrase(
                    OpplysningerOmBarnetillegg(
                        barnetillegg = barnetillegg,
                        sivilstand = sivilstand,
                        anvendtTrygdetid = trygdetidsdetaljerGjeldende.anvendtTT,
                        harYrkesskade = yrkesskadeGjeldende.notNull(),
                        harKravaarsakEndringInntekt = harKravaarsakEndringInntekt,
                        fraOgMedDatoErNesteAar = fraOgMedDatoErNesteAar,
                    )
                )
            }
        }

        showIf(kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)) {
            ifNotNull(opptjeningUfoeretrygd) { opptjening ->
                includePhrase(
                    TabellInntekteneBruktIBeregningen(
                        beregningGjeldendeFraOgMed = beregnetUTPerManedGjeldende.virkDatoFom,
                        harAvdoed = false.expr(),
                        opptjeningUfoeretrygd = opptjening,
                    )
                )
            }

            ifNotNull(opptjeningAvdoedUfoeretrygd) { opptjening ->
                includePhrase(
                    TabellInntekteneBruktIBeregningen(
                        beregningGjeldendeFraOgMed = beregnetUTPerManedGjeldende.virkDatoFom,
                        harAvdoed = true.expr(),
                        opptjeningUfoeretrygd = opptjening,
                    )
                )
            }

            includePhrase(
                SlikFastsetterViInntektenDinFoerDuBleUfoer(
                    harDelvisUfoergrad = ufoeretrygdGjeldende.harDelvisUfoeregrad,
                    inntektFoerUfoereBegrunnelse = inntektFoerUfoereBegrunnelse,
                )
            )

            includePhrase(SlikFastsetterViInntektenDinEtterDuBleUfoer)

            includePhrase(
                TrygdetidenDin(
                    beregnetUTPerManedGjeldende = beregnetUTPerManedGjeldende,
                    fastsattTrygdetid = trygdetidGjeldende.fastsattTrygdetid,
                    har40AarFastsattTrygdetid = trygdetidGjeldende.har40AarFastsattTrygdetid,
                    harFramtidigTrygdetidEOS = trygdetidGjeldende.harFramtidigTrygdetidEOS,
                    harFramtidigTrygdetidNorsk = trygdetidGjeldende.harFramtidigTrygdetidNorsk,
                    harLikUfoeregradOgYrkesskadegrad = trygdetidGjeldende.harLikUfoeregradOgYrkesskadegrad,
                    harTrygdetidsgrunnlag = trygdetidGjeldende.harTrygdetidsgrunnlag,
                    harYrkesskadeOppfylt = trygdetidGjeldende.harYrkesskadeOppfylt,
                )
            )

                includePhrase(
                    TabellTrygdetiden(
                        beregnetUTPerManedGjeldende = beregnetUTPerManedGjeldende,
                        trygdetidsdetaljerGjeldende = trygdetidsdetaljerGjeldende,
                        trygdetidGjeldende = trygdetidGjeldende,
                        norskTrygdetid = norskTrygdetid,
                        utenlandskTrygdetidBilateral = utenlandskTrygdetidBilateral,
                        utenlandskTrygdetidEOS = utenlandskTrygdetidEOS,
                    )
                )
            }


            includePhrase(
                SlikHarViFastsattKompensasjonsgradenDin(
                    harBrukerKonvertertUP = harBrukerKonvertertUP,
                    harDelvisUfoeregrad = ufoeretrygdGjeldende.harDelvisUfoeregrad,
                    harFullUfoeregrad = ufoeretrygdGjeldende.harFullUfoeregrad,
                    harGammelUTBeloepUlikNyUTBeloep = ufoeretrygdOrdinaer.harGammelUTBeloepUlikNyUTBeloep,
                    ifuInntekt = inntektFoerUfoereGjeldende.ifuInntekt,
                    harInntektsgrenseLessThanInntektstak = inntektsAvkortingGjeldende.harInntektsgrenseLessThanInntektstak,
                    kompensasjonsgrad = ufoeretrygdGjeldende.kompensasjonsgrad,
                    kravAarsakType = kravAarsakType,
                    oifuInntekt = inntektFoerUfoereGjeldende.oifuInntekt,
                    ufoeregrad = ufoeretrygdGjeldende.ufoeregrad,
                    ugradertBruttoPerAar = ufoeretrygdGjeldende.ugradertBruttoPerAar,
                )
            )

            includePhrase(
                SlikBeregnerViUtbetalingAvUfoeretrygdenNaarInntektenDinEndres(
                    forventetInntektAar = inntektsAvkortingGjeldende.forventetInntektAar,
                    harGammelUTBeloepUlikNyUTBeloep = ufoeretrygdOrdinaer.harGammelUTBeloepUlikNyUTBeloep,
                    harInntektsgrenseLessThanInntektstak = inntektsAvkortingGjeldende.harInntektsgrenseLessThanInntektstak,
                    harNyUtBeloep = ufoeretrygdOrdinaer.harNyUTBeloep,
                    inntektsgrenseAar = inntektsAvkortingGjeldende.inntektsgrenseAar,
                    kompensasjonsgrad = ufoeretrygdGjeldende.kompensasjonsgrad,
                    kravAarsakType = kravAarsakType,
                    overskytendeInntekt = inntektsAvkortingGjeldende.overskytendeInntekt,
                )
            )

            includePhrase(
                SlikBeregnerViReduksjonAvUfoeretrygden(
                    forventetInntektAar = inntektsAvkortingGjeldende.forventetInntektAar,
                    harGammelUTBeloepUlikNyUTBeloep = ufoeretrygdOrdinaer.harGammelUTBeloepUlikNyUTBeloep,
                    harInntektsgrenseLessThanInntektstak = inntektsAvkortingGjeldende.harInntektsgrenseLessThanInntektstak,
                    harNyUTBeloep = ufoeretrygdOrdinaer.harNyUTBeloep,
                    inntektsgrenseAar = inntektsAvkortingGjeldende.inntektsgrenseAar,
                    kompensasjonsgrad = ufoeretrygdGjeldende.kompensasjonsgrad,
                    kravAarsakType = kravAarsakType,
                    nettoPerAarReduksjonUT = inntektsAvkortingGjeldende.nettoPerAarReduksjonUT,
                    overskytendeInntekt = inntektsAvkortingGjeldende.overskytendeInntekt,
                )
            )

            includePhrase(
                TabellSlikBlirDinUtbetalingFoerSkatt(
                    forventetInntektAar = inntektsAvkortingGjeldende.forventetInntektAar,
                    fradrag = ufoeretrygdOrdinaer.fradrag,
                    harBeloepRedusert = beregningUfoere.harBeloepRedusert,
                    harForventetInntektLargerThanInntektstak = inntektsAvkortingGjeldende.harForventetInntektLargerThanInntektstak,
                    harInntektsgrenseLargerThanOrEqualToInntektstak = inntektsAvkortingGjeldende.harInntektsgrenseLargerThanOrEqualToInntektstak,
                    harInntektsgrenseLessThanInntektstak = inntektsAvkortingGjeldende.harInntektsgrenseLessThanInntektstak,
                    harNyUTBeloep = ufoeretrygdOrdinaer.harNyUTBeloep,
                    harTotalNettoUT = beregningUfoere.harTotalNettoUT,
                    harUtbetalingsgradLessThanUfoeregrad = ufoeretrygdGjeldende.harUtbetalingsgradLessThanUfoeregrad,
                    inntektsgrenseAar = inntektsAvkortingGjeldende.inntektsgrenseAar,
                    inntektstak = inntektsAvkortingGjeldende.inntektstak,
                    kravAarsakType = kravAarsakType,
                    nettoAkkumulerteBeloepUtbetalt = ufoeretrygdOrdinaer.nettoAkkumulerteBeloepUtbetalt,
                    nettoAkkumulertePlussNettoRestAar = ufoeretrygdOrdinaer.nettoAkkumulertePlussNettoRestAar,
                    nettoTilUtbetalingRestenAvAaret = ufoeretrygdOrdinaer.nettoTilUtbetalingRestenAvAaret,
                    ufoeregrad = ufoeretrygdGjeldende.ufoeregrad,
                    ufoeretrygdOrdinaer = ufoeretrygdOrdinaer,
                    ufoeretrygdPlussInntekt = ufoeretrygdOrdinaer.ufoeretrygdPlussInntekt,
                )
            )

            ifNotNull(gjenlevendetilleggGjeldene) { gjenlevendetillegg ->
                includePhrase(
                    SlikBeregnerViGjenlevendetillegg(
                        harGjenlevendetillegg = gjenlevendetillegg.harGjenlevendetillegg,
                        harNyttGjenlevendetillegg = gjenlevendetillegg.harNyttGjenlevendetillegg,
                    )
                )
            }

            ifNotNull(harEktefelletilleggInnvilget) { ektefelletillegg ->
                includePhrase(
                    ForDegSomMottarEktefelletillegg(
                        harEktefelletilleggInnvilget = ektefelletillegg,
                    )
                )
            }

            includePhrase(
                EtteroppgjoerAvUfoeretrygdOgBarnetillegg(
                    sivilstand = sivilstand,
                    harBarnetilleggInnvilget = harBarnetilleggInnvilget.notNull(),
                    harFellesbarn = barnetilleggGjeldende.fellesbarn_safe.notNull(),
                    borIUtlandet = borIUtlandet,
                )
            )
        }









