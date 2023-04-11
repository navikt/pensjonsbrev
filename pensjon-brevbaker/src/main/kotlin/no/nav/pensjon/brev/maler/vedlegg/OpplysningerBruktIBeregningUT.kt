package no.nav.pensjon.brev.maler.vedlegg


import kotlinx.html.B
import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.harGammelUTBeloepUlikNyUTBeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.harInntektsgrenseLessThanInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.nettoAkkumulertePlussNettoRestAar
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.nettoPerAarReduksjonUT
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.overskytendeInntekt
import no.nav.pensjon.brev.api.model.vedlegg.BeregningUfoereSelectors.ufoeretrygdPlussInntekt
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereGjeldendeSelectors.ifuInntekt
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereGjeldendeSelectors.oifuInntekt
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.forventetInntektAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.harForventetInntektLargerThanInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.harInntektsgrenseLargerThanOrEqualToInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerAvdoedSelectors.harNyttGjenlevendetillegg_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregningUfoere
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.borIUtlandet
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.fraOgMedDatoErNesteAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.opplysningerAvdoed
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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.opptjeningUfoeretrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.opptjeningUfoeretrygdAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdOrdinaer
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ungUfoerGjeldende_erUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.utenlandskTrygdetidBilateral
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.utenlandskTrygdetidEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harFoerstegangstjenesteOpptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harOmsorgsopptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.fastsattTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.har40AarFastsattTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harFramtidigTrygdetidEOS
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harFramtidigTrygdetidNorsk
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harLikUfoeregradOgYrkesskadegrad
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harTrygdetidsgrunnlag
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidGjeldendeSelectors.harYrkesskadeOppfylt
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.harDelvisUfoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.harFullUfoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.harUtbetalingsgradLessThanUfoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.ufoeregrad
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.ufoeretidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.ugradertBruttoPerAar
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.fradrag
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harBeloepRedusert
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harNyUTBeloep
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harTotalNettoUT
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoAkkumulerteBeloepUtbetalt
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.nettoTilUtbetalingRestenAvAaret
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.textExpr


fun createVedleggOpplysningerBruktIBeregningUT(
    skalViseMinsteytelse: Boolean,
    skalViseBarnetillegg: Boolean,
    skalViseAvdoed: Boolean,
    skalViseSlikBeregnerViUfoeretrygdenDin: Boolean,
    skalViseTabellInntekteneBruktIBeregningen: Boolean,
    skalViseTabellInntekteneBruktIBeregningenAvdoed: Boolean,
    skalViseSlikBeregnerViGjenlevendetillegg: Boolean,
    skalViseForDegSomMottarEktefelletillegg: Boolean,
    skalViseEtteroppgjoerAvUfoeretrygdOgBarnetillegg: Boolean,
) =
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
                harMinsteytelse = minsteytelseGjeldende_sats.notNull(),
            )
        )

        if (skalViseAvdoed) {
            val beregningsmetode = trygdetidsdetaljerGjeldende.beregningsmetode
            val harAvdoed = true.expr()
            val harNyttGjenlevendetillegg = opplysningerAvdoed.harNyttGjenlevendetillegg_safe
            showIf(
                harAvdoed and kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)
                        and beregningsmetode.isOneOf(Beregningsmetode.FOLKETRYGD) and harNyttGjenlevendetillegg.ifNull(
                    then = false
                )
            )
            {
                includePhrase(
                    TabellUfoereOpplysningerAvdoed(
                        beregnetUTPerManedGjeldende = beregnetUTPerManedGjeldende,
                        opplysningerAvdoed = opplysningerAvdoed,
                    )
                )
            }
        }

        if (skalViseSlikBeregnerViUfoeretrygdenDin) {
            showIf(kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)) {
                includePhrase(
                    SlikBeregnerViUfoeretrygdenDin(
                        beregningsmetode = trygdetidsdetaljerGjeldende.beregningsmetode,
                        harFoerstegangstjenesteOpptjening = opptjeningUfoeretrygd.harFoerstegangstjenesteOpptjening_safe.ifNull(
                            then = false
                        ),
                        harOmsorgsopptjening = opptjeningUfoeretrygd.harOmsorgsopptjening_safe.ifNull(then = false),
                        ufoeretidspunkt = ufoeretrygdGjeldende.ufoeretidspunkt,
                        virkDatoFom = beregnetUTPerManedGjeldende.virkDatoFom,
                    )
                )
            }
        }

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

        showIf(kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT) and trygdetidGjeldende.harYrkesskadeOppfylt) {
            includePhrase(BeregningAvUfoeretrygdSomSkyldesYrkesskadeEllerYrkessykdom.YrkesskadeEllerYrkessykdom)
        }

        if (skalViseTabellInntekteneBruktIBeregningen) {
            showIf(kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)) {
                ifNotNull(opptjeningUfoeretrygd) { opptjening ->
                    includePhrase(
                        TabellInntekteneBruktIBeregningen(
                            beregningGjeldendeFraOgMed = beregnetUTPerManedGjeldende.virkDatoFom,
                            opptjeningUfoeretrygd = opptjening,
                        )
                    )
                }
            }
        }

        if (skalViseTabellInntekteneBruktIBeregningenAvdoed) {
            val harAvdoed = true.expr()
            showIf(harAvdoed and kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)) {
                ifNotNull(opptjeningUfoeretrygdAvdoed) { opptjeningAvdoed ->
                    includePhrase(
                        TabellInntekteneBruktIBeregningenAvdoed(
                            beregningGjeldendeFraOgMed = beregnetUTPerManedGjeldende.virkDatoFom,
                            opptjeningUfoeretrygdAvdoed = opptjeningAvdoed
                        )
                    )
                }
            }
        }

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

        showIf(kravAarsakType.isOneOf(KravAarsakType.ENDRET_IFU)) {
            includePhrase(SlikHarViFastsattDenNyeInntektsgrensenDin.DenNyeInntektsgrensenDin)
        }

        includePhrase(
            SlikFastsetterViInntektenDinFoerDuBleUfoer(
                harDelvisUfoergrad = ufoeretrygdGjeldende.harDelvisUfoeregrad,
                inntektFoerUfoereBegrunnelse = inntektFoerUfoereBegrunnelse,
            )
        )

        includePhrase(SlikFastsetterViInntektenDinEtterDuBleUfoer)

        includePhrase(
            SlikHarViFastsattKompensasjonsgradenDin(
                harBrukerKonvertertUP = harBrukerKonvertertUP,
                harDelvisUfoeregrad = ufoeretrygdGjeldende.harDelvisUfoeregrad,
                harFullUfoeregrad = ufoeretrygdGjeldende.harFullUfoeregrad,
                harGammelUTBeloepUlikNyUTBeloep = beregningUfoere.harGammelUTBeloepUlikNyUTBeloep,
                ifuInntekt = inntektFoerUfoereGjeldende.ifuInntekt,
                harInntektsgrenseLessThanInntektstak = beregningUfoere.harInntektsgrenseLessThanInntektstak,
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
                harGammelUTBeloepUlikNyUTBeloep = beregningUfoere.harGammelUTBeloepUlikNyUTBeloep,
                harInntektsgrenseLessThanInntektstak = beregningUfoere.harInntektsgrenseLessThanInntektstak,
                harNyUtBeloep = ufoeretrygdOrdinaer.harNyUTBeloep,
                inntektsgrenseAar = inntektsAvkortingGjeldende.inntektsgrenseAar,
                kompensasjonsgrad = ufoeretrygdGjeldende.kompensasjonsgrad,
                kravAarsakType = kravAarsakType,
                overskytendeInntekt = beregningUfoere.overskytendeInntekt,
            )
        )

        includePhrase(
            SlikBeregnerViReduksjonAvUfoeretrygden(
                forventetInntektAar = inntektsAvkortingGjeldende.forventetInntektAar,
                harGammelUTBeloepUlikNyUTBeloep = beregningUfoere.harGammelUTBeloepUlikNyUTBeloep,
                harInntektsgrenseLessThanInntektstak = beregningUfoere.harInntektsgrenseLessThanInntektstak,
                harNyUTBeloep = ufoeretrygdOrdinaer.harNyUTBeloep,
                inntektsgrenseAar = inntektsAvkortingGjeldende.inntektsgrenseAar,
                kompensasjonsgrad = ufoeretrygdGjeldende.kompensasjonsgrad,
                kravAarsakType = kravAarsakType,
                nettoPerAarReduksjonUT = beregningUfoere.nettoPerAarReduksjonUT,
                overskytendeInntekt = beregningUfoere.overskytendeInntekt,
            )
        )

        includePhrase(
            TabellSlikBlirDinUtbetalingFoerSkatt(
                beregningUfoere = beregningUfoere,
                forventetInntektAar = inntektsAvkortingGjeldende.forventetInntektAar,
                fradrag = ufoeretrygdOrdinaer.fradrag,
                harBeloepRedusert = ufoeretrygdOrdinaer.harBeloepRedusert,
                harForventetInntektLargerThanInntektstak = inntektsAvkortingGjeldende.harForventetInntektLargerThanInntektstak,
                harInntektsgrenseLargerThanOrEqualToInntektstak = inntektsAvkortingGjeldende.harInntektsgrenseLargerThanOrEqualToInntektstak,
                harInntektsgrenseLessThanInntektstak = beregningUfoere.harInntektsgrenseLessThanInntektstak,
                harNyUTBeloep = ufoeretrygdOrdinaer.harNyUTBeloep,
                harTotalNettoUT = ufoeretrygdOrdinaer.harTotalNettoUT,
                harUtbetalingsgradLessThanUfoeregrad = ufoeretrygdGjeldende.harUtbetalingsgradLessThanUfoeregrad,
                inntektsgrenseAar = inntektsAvkortingGjeldende.inntektsgrenseAar,
                inntektstak = inntektsAvkortingGjeldende.inntektstak,
                kravAarsakType = kravAarsakType,
                nettoAkkumulerteBeloepUtbetalt = ufoeretrygdOrdinaer.nettoAkkumulerteBeloepUtbetalt,
                nettoAkkumulertePlussNettoRestAar = beregningUfoere.nettoAkkumulertePlussNettoRestAar,
                nettoTilUtbetalingRestenAvAaret = ufoeretrygdOrdinaer.nettoTilUtbetalingRestenAvAaret,
                ufoeregrad = ufoeretrygdGjeldende.ufoeregrad,
                ufoeretrygdOrdinaer = ufoeretrygdOrdinaer,
                ufoeretrygdPlussInntekt = beregningUfoere.ufoeretrygdPlussInntekt,
            )
        )

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

        if (skalViseSlikBeregnerViGjenlevendetillegg) {
            ifNotNull(opplysningerAvdoed) { gjenlevendetillegg ->
                includePhrase(
                    SlikBeregnerViGjenlevendetillegg(
                        harGjenlevendetillegg = gjenlevendetillegg.notNull(),
                        harNyttGjenlevendetillegg = opplysningerAvdoed.harNyttGjenlevendetillegg_safe.ifNull(
                            then = false
                        )
                    )
                )
            }
        }
        if (skalViseForDegSomMottarEktefelletillegg) {
            ifNotNull(harEktefelletilleggInnvilget) { ektefelletillegg ->
                includePhrase(
                    ForDegSomMottarEktefelletillegg(
                        harEktefelletilleggInnvilget = ektefelletillegg,
                    )
                )
            }
        }

        if (skalViseEtteroppgjoerAvUfoeretrygdOgBarnetillegg) {
            includePhrase(
                EtteroppgjoerAvUfoeretrygdOgBarnetillegg(
                    sivilstand = sivilstand,
                    harBarnetilleggInnvilget = harBarnetilleggInnvilget.notNull(),
                    harFellesbarn = barnetilleggGjeldende.fellesbarn_safe.notNull(),
                    borIUtlandet = borIUtlandet,
                )
            )
        }
    }










