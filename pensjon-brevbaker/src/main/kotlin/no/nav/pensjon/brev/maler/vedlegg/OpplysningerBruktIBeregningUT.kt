package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.Beregningsmetode
import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.BarnetilleggSelectors.fellesbarn_safe
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedSelectors.brukerErFlyktning
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereSelectors.inntektFoerUfoereBegrunnelse
import no.nav.pensjon.brev.api.model.vedlegg.InntektFoerUfoereSelectors.opptjeningUfoeretrygd_safe
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingSelectors.forventetInntektAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.InntektsAvkortingSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.bilateralTrygdePerioder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.eosTrygdePerioder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.erFlyktning
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.erUngUfoer
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.foedselsnummer
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.harNyttGjenlevendetillegg_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.norskTrygdetidPerioder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.opptjeningUfoeretrygd_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.trygdetidsdetaljer
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTAvdoedSelectors.yrkesskade
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.avdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.barnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.beregnetUTPerManed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.borIUtlandet
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.fraOgMedDatoErNesteAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harEktefelletilleggInnvilget
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.harKravaarsakEndringInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektEtterUfoereBeloepIEU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektFoerUfoere
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.inntektsAvkorting
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.kravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.minsteytelseSats
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.trygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.ungUfoerErUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDtoSelectors.yrkesskade
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harFoerstegangstjenesteOpptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpptjeningUfoeretrygdSelectors.harOmsorgsopptjening_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidSelectors.bilateralTrygdePerioder
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidSelectors.eosTrygdePerioder
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidSelectors.norskTrygdetidPerioder
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidSelectors.trygdetidsdetaljer
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.fastsattNorskTrygdetid_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerAvdoedSelectors.harBoddArbeidUtland
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerSelectors.faktiskTTEOS_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerSelectors.fastsattNorskTrygdetid_safe
import no.nav.pensjon.brev.api.model.vedlegg.TrygdetidsdetaljerSelectors.harBoddArbeidUtland
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harGammelUTBeloepUlikNyUTBeloep
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdOrdinaerSelectors.harNyUTBeloep
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdSelectors.ufoeretidspunkt
import no.nav.pensjon.brev.api.model.vedlegg.UfoeretrygdSelectors.ufoeretrygdOrdinaer
import no.nav.pensjon.brev.api.model.vedlegg.YrkesskadeSelectors.yrkesskadegrad_safe
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.textExpr


fun createVedleggOpplysningerBruktIBeregningUT(
    skalViseAvdoed: Boolean,
    skalViseBarnetillegg: Boolean,
    skalViseDinUtbetalingFoerSkatt: Boolean,
    skalViseEtteroppgjoerAvUfoeretrygdOgBarnetillegg: Boolean,
    skalViseForDegSomMottarEktefelletillegg: Boolean,
    skalViseInntektEtterUfoer: Boolean,
    skalViseInntektFoerUfoer: Boolean,
    skalViseKompensasjonsgrad: Boolean,
    skalViseMinsteytelse: Boolean,
    skalViseReduksjonAvUfoeretrygden: Boolean,
    skalViseSlikBeregnerViGjenlevendetillegg: Boolean,
    skalViseSlikBeregnerViGjenlevendetilleggHarNyttTillegg: Boolean,
    skalViseSlikBeregnerViUfoeretrygdenDin: Boolean,
    skalViseTabellInntekteneBruktIBeregningen: Boolean,
    skalViseTrygdetidenDin: Boolean,
    skalViseUtbetalingAvUTNaarInntektEndres: Boolean,
    skalViseYrkesskadeEllerYrkessykdom: Boolean,
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
            inntektsAvkorting.inntektsgrenseAar.lessThan(inntektsAvkorting.inntektstak)
        val grunnbeloep = beregnetUTPerManed.grunnbeloep.format()
        val harMinsteytelse = minsteytelseSats.notNull()

        paragraph {
            val virkDatoFom = beregnetUTPerManed.virkDatoFom.format()
            textExpr(
                Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkDatoFom + ". Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbeloep + " kroner.",
                Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr() + virkDatoFom + ". Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbeloep + " kroner.",
                English to "Data we have used in the calculations as of ".expr() + virkDatoFom + ". The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbeloep + ".",
            )
        }

        includePhrase(
            TabellUfoereOpplysninger(
                barnetillegg = barnetillegg,
                beregnetUTPerManed = beregnetUTPerManed,
                erUngUfoer = ungUfoerErUnder20Aar.notNull(),
                harMinsteytelse = harMinsteytelse,
                inntektEtterUfoereBeloep = inntektEtterUfoereBeloepIEU,
                inntektFoerUfoere = inntektFoerUfoere,
                inntektsAvkorting = inntektsAvkorting,
                inntektsgrenseErUnderTak = inntektsgrenseErUnderTak,
                trygdetidsdetaljer = trygdetid.trygdetidsdetaljer,
                ufoeretrygd = ufoeretrygd,
                yrkesskade = yrkesskade,
            )
        )

        if (skalViseAvdoed) {
            val beregningsmetode = trygdetid.trygdetidsdetaljer.beregningsmetode
            val harAvdoed = true.expr()
            val harNyttGjenlevendetillegg = avdoed.harNyttGjenlevendetillegg_safe
            showIf(
                harAvdoed and kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)
                        and beregningsmetode.isOneOf(Beregningsmetode.FOLKETRYGD) and harNyttGjenlevendetillegg.ifNull(
                    then = false
                )
            ) {
                ifNotNull(avdoed) { opplysningerAvdoed ->
                    includePhrase(
                        TabellUfoereOpplysningerAvdoed(
                            avdoed = opplysningerAvdoed,
                            beregnetUTPerManed = beregnetUTPerManed,
                            erFlyktning = opplysningerAvdoed.erFlyktning,
                            erUngUfoer = opplysningerAvdoed.erUngUfoer,
                            foedselsnummer = opplysningerAvdoed.foedselsnummer,
                            trygdetidsdetaljer = opplysningerAvdoed.trygdetidsdetaljer,
                            ufoeretrygd = opplysningerAvdoed.ufoeretrygd,
                            yrkesskade = opplysningerAvdoed.yrkesskade,
                        )
                    )
                }
            }
        }

        if (skalViseSlikBeregnerViUfoeretrygdenDin) {
            showIf(kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)) {
                includePhrase(
                    SlikBeregnerViUfoeretrygdenDin(
                        beregningsmetode = trygdetid.trygdetidsdetaljer.beregningsmetode,
                        harFoerstegangstjenesteOpptjening = inntektFoerUfoere.opptjeningUfoeretrygd_safe.harFoerstegangstjenesteOpptjening_safe.ifNull(
                            then = false
                        ),
                        harOmsorgsopptjening = inntektFoerUfoere.opptjeningUfoeretrygd_safe.harOmsorgsopptjening_safe.ifNull(
                            then = false
                        ),
                        ufoeretidspunkt = ufoeretrygd.ufoeretidspunkt,
                        virkDatoFom = beregnetUTPerManed.virkDatoFom,
                    )
                )
            }
        }

        if (skalViseMinsteytelse) {
            ifNotNull(minsteytelseSats) {minsteytelseSats ->
                includePhrase(
                    OpplysningerOmMinstetillegg(
                        harMinsteytelse = harMinsteytelse,
                        inntektFoerUfoere = inntektFoerUfoere,
                        inntektsgrenseErUnderTak = inntektsgrenseErUnderTak,
                        minsteytelseSats = minsteytelseSats,
                        ufoeretrygd = ufoeretrygd,
                        ungUfoerErUnder20Aar = ungUfoerErUnder20Aar.ifNull(false),
                    )
                )
            }
        }

        showIf(kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)) {

            if (skalViseYrkesskadeEllerYrkessykdom) {
                ifNotNull(yrkesskade.yrkesskadegrad_safe) { yrkesskadegrad ->
                    showIf(yrkesskadegrad.greaterThan(0)) {
                        includePhrase(BeregningAvUfoeretrygdSomSkyldesYrkesskadeEllerYrkessykdom)
                    }
                }

                if (skalViseTabellInntekteneBruktIBeregningen) {
                    showIf(
                        trygdetid.trygdetidsdetaljer.beregningsmetode.isOneOf(
                            Beregningsmetode.FOLKETRYGD
                        )
                    ) {
                        includePhrase(
                            TabellInntekteneBruktIBeregningen(
                                opptjeningUfoeretrygd = inntektFoerUfoere.opptjeningUfoeretrygd_safe,
                            )
                        )
                    }
                }
            }

            if (skalViseAvdoed) {
                val beregningsmetode = trygdetid.trygdetidsdetaljer.beregningsmetode
                val harAvdoed = true.expr()
                val harNyttGjenlevendetillegg = avdoed.harNyttGjenlevendetillegg_safe
                showIf(
                    harAvdoed and beregningsmetode.isOneOf(Beregningsmetode.FOLKETRYGD) and
                            harNyttGjenlevendetillegg.ifNull(
                                then = false
                            )
                ) {
                    ifNotNull(avdoed.opptjeningUfoeretrygd_safe) { opptjeningAvdoed ->
                        includePhrase(
                            TabellInntekteneBruktIBeregningenAvdoed(
                                opptjeningUfoeretrygd = opptjeningAvdoed
                            )
                        )
                    }
                }
            }

            if (skalViseTrygdetidenDin) {
                includePhrase(
                    TrygdetidenDin(
                        beregnetUTPerManed = beregnetUTPerManed,
                        trygdetidsdetaljer = trygdetid.trygdetidsdetaljer,
                        ufoeregrad = ufoeretrygd,
                        yrkesskadegrad = yrkesskade,
                        norskTrygdetidPerioder = trygdetid.norskTrygdetidPerioder,
                    )
                )

                includePhrase(
                    TabellTrygdetiden(
                        anvendtTT = trygdetid.trygdetidsdetaljer.anvendtTT,
                        brukerErFlyktning = beregnetUTPerManed.brukerErFlyktning,
                        bilateralTrygdetidPerioder = trygdetid.bilateralTrygdePerioder,
                        eosTrygdetidPerioder = trygdetid.eosTrygdePerioder,
                        faktiskTTEOS = trygdetid.trygdetidsdetaljer.faktiskTTEOS_safe,
                        fastsattNorskTrygdetid = trygdetid.trygdetidsdetaljer.fastsattNorskTrygdetid_safe,
                        harBoddArbeidUtland = trygdetid.trygdetidsdetaljer.harBoddArbeidUtland,
                        kravAarsakType = kravAarsakType,
                        norskTrygdetidPerioder = trygdetid.norskTrygdetidPerioder,
                    )
                )
            }

            if (skalViseAvdoed) {
                val beregningsmetode = trygdetid.trygdetidsdetaljer.beregningsmetode
                val harAvdoed = true.expr()
                val harNyttGjenlevendetillegg = avdoed.harNyttGjenlevendetillegg_safe
                showIf(
                    harAvdoed and beregningsmetode.isOneOf(Beregningsmetode.FOLKETRYGD) and
                            harNyttGjenlevendetillegg.ifNull(
                                then = false
                            )
                ) {
                    ifNotNull(
                        avdoed
                    ) { periode ->
                        includePhrase(
                            TabellTrygdetidenAvdoed(
                                bilateralTrygdetidPerioder = periode.bilateralTrygdePerioder,
                                eosTrygdetidPerioder = periode.eosTrygdePerioder,
                                norskTrygdetidPerioder = periode.norskTrygdetidPerioder,
                                fastsattNorskTrygdetid = periode.trygdetidsdetaljer.fastsattNorskTrygdetid_safe,
                                harBoddArbeidUtland = periode.trygdetidsdetaljer.harBoddArbeidUtland,
                            )
                        )
                    }
                }
            }

            showIf(kravAarsakType.isOneOf(KravAarsakType.ENDRET_IFU)) {
                includePhrase(SlikHarViFastsattDenNyeInntektsgrensenDin.DenNyeInntektsgrensenDin)
            }

            showIf(kravAarsakType.isNotAnyOf(KravAarsakType.ENDRET_IFU)) {
                if (skalViseInntektFoerUfoer) {
                    includePhrase(
                        SlikFastsetterViInntektenDinFoerDuBleUfoer(
                            inntektFoerUfoereBegrunnelse = inntektFoerUfoere.inntektFoerUfoereBegrunnelse,
                            kravAarsakType = kravAarsakType,
                            harMinsteytelse = harMinsteytelse,
                            ufoeretrygd = ufoeretrygd,
                        )
                    )
                }

                if (skalViseInntektEtterUfoer) {
                    includePhrase(
                        SlikFastsetterViInntektenDinEtterDuBleUfoer(
                            harMinsteytelse = harMinsteytelse,
                            inntektFoerUfoereBegrunnelse = inntektFoerUfoere.inntektFoerUfoereBegrunnelse,
                            kravAarsakType = kravAarsakType,
                            ufoeretrygd = ufoeretrygd,
                        )
                    )
                }
            }

            if (skalViseKompensasjonsgrad) {
                showIf(
                    (
                            kravAarsakType.isOneOf(KravAarsakType.ENDRET_INNTEKT)) and ufoeretrygd.ufoeretrygdOrdinaer.harGammelUTBeloepUlikNyUTBeloep
                ) {
                    includePhrase(
                        SlikHarViFastsattKompensasjonsgradenDin(
                            inntektsAvkorting = inntektsAvkorting,
                            inntektFoerUfoere = inntektFoerUfoere,
                            kravAarsakType = kravAarsakType,
                            ufoeretrygd = ufoeretrygd,
                            ufoeretrygdOrdinaer = ufoeretrygd.ufoeretrygdOrdinaer,
                        )
                    )
                }
            }

            if (skalViseUtbetalingAvUTNaarInntektEndres) {
                showIf(
                    (kravAarsakType.isOneOf(KravAarsakType.ENDRET_INNTEKT))
                            and ufoeretrygd.ufoeretrygdOrdinaer.harGammelUTBeloepUlikNyUTBeloep and inntektsgrenseErUnderTak
                ) {
                    includePhrase(
                        SlikBeregnerViUtbetalingAvUfoeretrygdenNaarInntektenDinEndres(
                            inntektsAvkorting = inntektsAvkorting,
                            ufoeretrygd = ufoeretrygd,
                            ufoeretrygdOrdinaer = ufoeretrygd.ufoeretrygdOrdinaer,
                        )
                    )
                }

                if (skalViseReduksjonAvUfoeretrygden) {
                    showIf(
                        (kravAarsakType.isOneOf(KravAarsakType.ENDRET_INNTEKT)) and ufoeretrygd.ufoeretrygdOrdinaer.harGammelUTBeloepUlikNyUTBeloep
                                and inntektsAvkorting.forventetInntektAar.greaterThanOrEqual(inntektsAvkorting.inntektsgrenseAar)
                                and inntektsgrenseErUnderTak and ufoeretrygd.ufoeretrygdOrdinaer.harNyUTBeloep
                    ) {
                        includePhrase(
                            SlikBeregnerViReduksjonAvUfoeretrygden(
                                ufoeretrygd = ufoeretrygd,
                                ufoeretrygdOrdinaer = ufoeretrygd.ufoeretrygdOrdinaer,
                            )
                        )
                    }

                    if (skalViseDinUtbetalingFoerSkatt) {
                        includePhrase(
                            TabellSlikBlirDinUtbetalingFoerSkatt(
                                inntektsAvkorting = inntektsAvkorting,
                                ufoeretrygd = ufoeretrygd,
                                ufoeretrygdOrdinaer = ufoeretrygd.ufoeretrygdOrdinaer,
                            )
                        )
                    }
                }
            }
        }// END of kravAarsakType.isNotAnyOf(KravAarsakType.BT)

        if (skalViseBarnetillegg) {
            ifNotNull(barnetillegg) { barnetillegg ->
                includePhrase(
                    OpplysningerOmBarnetillegg(
                        barnetillegg = barnetillegg,
                        sivilstand = sivilstand,
                        anvendtTrygdetid = trygdetid.trygdetidsdetaljer.anvendtTT,
                        harYrkesskade = yrkesskade.notNull(),
                        harKravaarsakEndringInntekt = harKravaarsakEndringInntekt,
                        fraOgMedDatoErNesteAar = fraOgMedDatoErNesteAar,
                    )
                )
            }
        }

        showIf(kravAarsakType.isNotAnyOf(KravAarsakType.SOKNAD_BT)) {

            if (skalViseSlikBeregnerViGjenlevendetilleggHarNyttTillegg) {
                ifNotNull(avdoed) { gjenlevendetillegg ->
                    includePhrase(
                        HarNyttGjenlevendetillegg(
                            harGjenlevendetillegg = gjenlevendetillegg.notNull(),
                            harNyttGjenlevendetillegg = avdoed.harNyttGjenlevendetillegg_safe.ifNull(
                                then = false
                            )
                        )
                    )
                }
            }

            if (skalViseSlikBeregnerViGjenlevendetillegg) {
                ifNotNull(avdoed) { gjenlevendetillegg ->
                    includePhrase(
                        NotNyttGjenlevendetillegg(
                            harGjenlevendetillegg = gjenlevendetillegg.notNull(),
                            harNyttGjenlevendetillegg = avdoed.harNyttGjenlevendetillegg_safe.ifNull(
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
                        harBarnetilleggInnvilget = barnetillegg.notNull(),
                        borIUtlandet = borIUtlandet,
                        harFellesbarn = barnetillegg.fellesbarn_safe,
                    )
                )
            }
        }
    }











