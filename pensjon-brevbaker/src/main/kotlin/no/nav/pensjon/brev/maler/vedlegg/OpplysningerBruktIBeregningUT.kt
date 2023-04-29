package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggGjeldendeSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedGjeldendeSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingGjeldendeSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerAvdoedSelectors.harNyttGjenlevendetillegg_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerAvdoedSelectors.norskTrygdetidPeriode1_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerAvdoedSelectors.opptjeningUfoeretrygdAvdoed_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerAvdoedSelectors.trygdetidsdetaljer1
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerAvdoedSelectors.ufoeretrygdGjeldende1
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerAvdoedSelectors.utenlandskTrygdePeriodeAvdoed_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerAvdoedSelectors.yrkesskadeGjeldene1
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.borIUtlandet
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.fraOgMedDatoErNesteAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.opplysningerAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harBarnetilleggInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harEktefelletilleggInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harKravaarsakEndringInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektEtterUfoereGjeldende_beloepIEU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoereBegrunnelse
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektsAvkortingGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.kravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.minsteytelseGjeldende_sats
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.norskTrygdetidPeriode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.opptjeningUfoeretrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygdOrdinaer
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ungUfoerGjeldende_erUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.utenlandskTrygdetidPeriode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harFoerstegangstjenesteOpptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harOmsorgsopptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerGjeldendeSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdGjeldendeSelectors.ufoeretidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.YrkesskadeGjeldendeSelectors.yrkesskadegrad_safe
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
    skalViseSlikBeregnerViGjenlevendetilleggHarNyttTillegg: Boolean,
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
                erUngUfoer = ungUfoerGjeldende_erUnder20Aar.notNull(),
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

                ifNotNull(opplysningerAvdoed) { opplysningerAvdoed ->
                    includePhrase(
                        TabellUfoereOpplysningerAvdoed(
                            beregnetUTPerManedGjeldende = beregnetUTPerManedGjeldende,
                            opplysningerAvdoed = opplysningerAvdoed,
                            trygdetidsdetaljer = opplysningerAvdoed.trygdetidsdetaljer1,
                            ufoeretrygdGjeldende = opplysningerAvdoed.ufoeretrygdGjeldende1,
                            yrkesskadeGjeldende = opplysningerAvdoed.yrkesskadeGjeldene1
                        )
                    )
                }
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

        ifNotNull(yrkesskadeGjeldende.yrkesskadegrad_safe) { yrkesskadegrad ->
            showIf(kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT) and yrkesskadegrad.greaterThan(0)) {
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
                    ifNotNull(opplysningerAvdoed.opptjeningUfoeretrygdAvdoed_safe) { opptjeningAvdoed ->
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
                    trygdetidsdetaljerGjeldende = trygdetidsdetaljerGjeldende,
                    ufoeregrad = ufoeretrygdGjeldende,
                    yrkesskadegrad = yrkesskadeGjeldende,
                    norskTrygdetidPeriode = norskTrygdetidPeriode,
                )
            )

            includePhrase(
                TabellTrygdetiden(
                    norskTrygdetidPeriode = norskTrygdetidPeriode,
                    utenlandskTrygdetidBilateralPeriode = utenlandskTrygdetidPeriode,
                    utenlandskTrygdetidEOSPeriode = utenlandskTrygdetidPeriode
                )
            )
ifNotNull(opplysningerAvdoed.norskTrygdetidPeriode1_safe, opplysningerAvdoed.utenlandskTrygdePeriodeAvdoed_safe ) {
        norskPeriode, utenlandskPeriode ->
    includePhrase(
        TabellTrygdetidenAvdoed(
            norskTrygdetidPeriode = norskPeriode,
            utenlandskTrygdetidEOSPeriode = utenlandskPeriode,
            utenlandskTrygdetidBilateralPeriode = utenlandskPeriode
        )
    )
}

            showIf(kravAarsakType.isOneOf(KravAarsakType.ENDRET_IFU)) {
                includePhrase(SlikHarViFastsattDenNyeInntektsgrensenDin.DenNyeInntektsgrensenDin)
            }

            includePhrase(
                SlikFastsetterViInntektenDinFoerDuBleUfoer(
                    ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                    inntektFoerUfoereBegrunnelse = inntektFoerUfoereBegrunnelse,
                )
            )

            includePhrase(SlikFastsetterViInntektenDinEtterDuBleUfoer)

            includePhrase(
                SlikHarViFastsattKompensasjonsgradenDin(
                    inntektsAvkortingGjeldende = inntektsAvkortingGjeldende,
                    inntektFoerUfoereGjeldende = inntektFoerUfoereGjeldende,
                    kravAarsakType = kravAarsakType,
                    ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                    ufoeretrygdOrdinaer = ufoeretrygdOrdinaer,

                    )
            )

            includePhrase(
                SlikBeregnerViUtbetalingAvUfoeretrygdenNaarInntektenDinEndres(
                    inntektsAvkortingGjeldende = inntektsAvkortingGjeldende,
                    kravAarsakType = kravAarsakType,
                    ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                    ufoeretrygdOrdinaer = ufoeretrygdOrdinaer,
                )
            )

            includePhrase(
                SlikBeregnerViReduksjonAvUfoeretrygden(
                    inntektsAvkortingGjeldende = inntektsAvkortingGjeldende,
                    kravAarsakType = kravAarsakType,
                    ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                    ufoeretrygdOrdinaer = ufoeretrygdOrdinaer,
                )
            )

            includePhrase(
                TabellSlikBlirDinUtbetalingFoerSkatt(
                    inntektsAvkortingGjeldende = inntektsAvkortingGjeldende,
                    kravAarsakType = kravAarsakType,
                    ufoeretrygdGjeldende = ufoeretrygdGjeldende,
                    ufoeretrygdOrdinaer = ufoeretrygdOrdinaer,
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

            if (skalViseSlikBeregnerViGjenlevendetilleggHarNyttTillegg) {
                ifNotNull(opplysningerAvdoed) { gjenlevendetillegg ->
                    includePhrase(
                        HarNyttGjenlevendetillegg(
                            harGjenlevendetillegg = gjenlevendetillegg.notNull(),
                            harNyttGjenlevendetillegg = opplysningerAvdoed.harNyttGjenlevendetillegg_safe.ifNull(
                                then = false
                            )
                        )
                    )
                }
            }

            if (skalViseSlikBeregnerViGjenlevendetillegg) {
                ifNotNull(opplysningerAvdoed) { gjenlevendetillegg ->
                    includePhrase(
                        NotNyttGjenlevendetillegg(
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
                        borIUtlandet = borIUtlandet,
                        harFellesbarn = barnetilleggGjeldende.fellesbarn_safe,
                    )
                )
            }
        }
    }










