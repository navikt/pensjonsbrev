@file:Suppress("LocalVariableName")

package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning

import no.nav.pensjon.brev.ufore.api.model.vedlegg.Beregningsmetode
import no.nav.pensjon.brev.ufore.api.model.vedlegg.Kravaarsaktype
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.beregning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.gjenlevendetillegg.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.krav.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.trygdetid.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.trygdetidAvdoed.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.ytelsesgrunnlag.*
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU037V_1
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU037V_2
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU037V_3
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU037V_4
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU038V_1
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU038V_2
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU038V_3
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU038V_4
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU039V_TBU044V_1
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU045V_1
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU046V_1
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU047V
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU500v
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU010V
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_ForDegSomHarRettTilBarnetillegg
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_ForDegSomMottarEktefelletillegg
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_SlikBeregnerViReduksjonenAvUfoeretrygden
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_SlikBeregnerViGjenlevendetilleggetDitt
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_SlikRedusererViBarnetilleggetUtFraInntekt
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_SlikBlirDinUtbetalingFoerSkatt
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU052V_TBU073V_SlikHarViFastsattKompensasjonsgradenDin
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU011V_TBU016V
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU034V_036V
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU080V_TBU027V
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBUxx1V
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBUxx2V
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBUxx4v_og_TBU048V_TBU055V
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU1187
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU1382
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TBU1384
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TrygdetidListeNorTabell
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TrygdetidsListeBilateralTabell
import no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser.TrygdetidsListeEOSTabell
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

/**
 * Ufoere-port av legacy-vedlegget `vedleggOpplysningerBruktIBeregningUTLegacy` (pensjon/maler),
 * basert paa [OpplysningerBruktIBeregningUTLegacyDto] i stedet for PE-objektet PEgruppe10.
 *
 * Innholdet bygges opp gruppevis (trygdetid, inntekt, barnetillegg, etteroppgjoer ...) etter hvert
 * som de tilhoerende frasene migreres. @TemplateModelHelpers genererer selectors for DTO-en.
 */
@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningUTLegacy =
    createAttachment<LangBokmalNynorsk, OpplysningerBruktIBeregningUTLegacyDto>(
        title = {
            text(
                bokmal { +"Opplysninger om beregningen" },
                nynorsk { +"Opplysningar om utrekninga" },
            )
        },
        includeSakspart = false,
    ) {
        title2 {
            text(
                bokmal { +"Opplysninger vi har brukt i beregningen fra " + beregning.beregningVirkningDatoFom.format() },
                nynorsk { +"Opplysningar vi har brukt i berekninga frå " + beregning.beregningVirkningDatoFom.format() },
            )
        }
        paragraph {
            text(
                bokmal {
                    +" Folketrygdens grunnbeløp (G) benyttet i beregningen er " +
                        beregning.grunnbeloep.format() + "."
                },
                nynorsk {
                    +" Folketrygdas grunnbeløp (G) nytta i berekninga er " +
                        beregning.grunnbeloep.format() + "."
                },
            )
        }

        // Brukerens opplysningstabell
        includePhrase(
            TBU010V(
                visningsflagg,
                beregning,
                avkortning,
                ytelsesgrunnlag,
                inntektFoerUfoere,
                trygdetid,
                person,
                yrkesskade,
                barnetilleggFelles,
                barnetilleggSaerkull,
                antallBarnTotalt,
            )
        )

        // Opplysninger om avdøde (gjenlevendetillegg)
        ifNotNull(gjenlevendetillegg) { gt ->
            ifNotNull(trygdetidAvdoed) { avdoed ->
                showIf(visningsflagg.visAvdoedOpplysningerTabell) {
                    includePhrase(TBUxx1V(beregning, gt, avdoed))
                }
            }
        }

        // Slik beregner vi uføretrygden din
        showIf(visningsflagg.visBrukerKonvertertUP) {
            includePhrase(TBUxx2V(visningsflagg))
        }.orShow {
            includePhrase(TBU011V_TBU016V(visningsflagg))
        }

        // For deg som har rett til minsteytelse
        ifNotNull(minsteytelse) { my ->
            showIf(visningsflagg.visMinsteytelse) {
                includePhrase(TBU080V_TBU027V(visningsflagg, my, trygdetid))
            }
        }

        // Generell G-justering + yrkesskade
        includePhrase(TBU034V_036V(visningsflagg))

        // Dette er inntektene vi har brukt i beregningen din
        showIf(visningsflagg.visInntektsseksjon) {
            title1 {
                text(
                    bokmal { +"Dette er inntektene vi har brukt i beregningen din" },
                    nynorsk { +"Dette er inntektene vi har brukt i berekninga di" },
                )
            }
            showIf(beregning.beregningsmetode.equalTo(Beregningsmetode.FOLKETRYGD)) {
                includePhrase(TBU037V_1(beregning.beregningVirkningDatoFom, ytelsesgrunnlag.opptjeningUTListe))
                includePhrase(TBU037V_2(visningsflagg))
            }.orShow {
                includePhrase(TBU038V_1(beregning.beregningVirkningDatoFom, ytelsesgrunnlag.opptjeningUTListe))
                includePhrase(TBU038V_2(visningsflagg))
            }
            ifNotNull(gjenlevendetillegg) { gt ->
                showIf(visningsflagg.visInntektsgrunnlagAvdoed) {
                    includePhrase(TBU037V_3(beregning.beregningVirkningDatoFom, gt.opptjeningUTListeAvdoed))
                    includePhrase(TBU037V_4(visningsflagg))
                }
                showIf(visningsflagg.visInntektsgrunnlagAvdoedUtland) {
                    includePhrase(TBU038V_3(beregning.beregningVirkningDatoFom, gt.opptjeningUTListeAvdoed))
                    includePhrase(TBU038V_4(visningsflagg))
                }
            }
        }

        // Dette er trygdetiden din
        showIf(visningsflagg.visTrygdetid) {
            includePhrase(TBU039V_TBU044V_1(beregning, trygdetid, krav, visningsflagg))

            showIf(visningsflagg.visTrygdetidNorgeTabell) {
                includePhrase(TrygdetidListeNorTabell(trygdetid.perioderNorge))
            }

            showIf(visningsflagg.visTrygdetidEOS) {
                includePhrase(TBU045V_1)
                includePhrase(TrygdetidsListeEOSTabell(trygdetid.perioderEOS))
            }
        }

        showIf(visningsflagg.visTrygdetidBilateral) {
            includePhrase(TBU046V_1)
            includePhrase(TrygdetidsListeBilateralTabell(trygdetid.perioderBilateral))
        }

        showIf(visningsflagg.visRedusertFramtidigTrygdetidTekst) {
            includePhrase(TBU047V)
        }

        ifNotNull(trygdetidAvdoed) { avdoed ->
            showIf(visningsflagg.visTrygdetidAvdoedNorgeTabell) {
                includePhrase(TBU1187(avdoed.perioderNorge))
            }
            showIf(visningsflagg.visTrygdetidAvdoedEOS) {
                includePhrase(TBU1382(avdoed.perioderEOS))
            }
            showIf(visningsflagg.visTrygdetidAvdoedBilateral) {
                includePhrase(TBU1384(avdoed.perioderBilateral))
            }
        }

        // Slik har vi fastsatt den nye inntektsgrensen din
        showIf(krav.kravaarsaktype.equalTo(Kravaarsaktype.ENDRING_IFU)) {
            includePhrase(TBU500v)
        }

        // Slik fastsetter vi inntekten din før/etter at du ble ufør
        showIf(visningsflagg.visFastsetterInntektFoerUfoer) {
            includePhrase(TBUxx4v_og_TBU048V_TBU055V(visningsflagg, beregning))
        }

        // Slik har vi fastsatt kompensasjonsgraden / reduksjon / utbetaling
        includePhrase(TBU052V_TBU073V_SlikHarViFastsattKompensasjonsgradenDin(visningsflagg, beregning, inntektFoerUfoere, avkortning))
        includePhrase(TBU052V_TBU073V_SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres(visningsflagg, avkortning))
        includePhrase(TBU052V_TBU073V_SlikBeregnerViReduksjonenAvUfoeretrygden(visningsflagg, avkortning))
        includePhrase(TBU052V_TBU073V_SlikBlirDinUtbetalingFoerSkatt(visningsflagg, beregning, avkortning))

        // Barnetillegg
        showIf(visningsflagg.tbu601v604v) {
            includePhrase(TBU052V_TBU073V_SlikRedusererViBarnetilleggetUtFraInntekt(visningsflagg, barnetilleggFelles, barnetilleggSaerkull, person))
        }
        includePhrase(TBU052V_TBU073V_ForDegSomHarRettTilBarnetillegg(visningsflagg, person))
        includePhrase(
            TBU052V_TBU073V_SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(
                visningsflagg,
                barnetilleggFelles,
                barnetilleggSaerkull,
                person,
            )
        )

        // Gjenlevendetillegg / ektefelletillegg
        includePhrase(TBU052V_TBU073V_SlikBeregnerViGjenlevendetilleggetDitt(visningsflagg))
        includePhrase(TBU052V_TBU073V_ForDegSomMottarEktefelletillegg(visningsflagg))

        // Etteroppgjør
        showIf(visningsflagg.visEtteroppgjoer) {
            includePhrase(TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg(visningsflagg, barnetilleggFelles, barnetilleggSaerkull, person))
        }
    }
