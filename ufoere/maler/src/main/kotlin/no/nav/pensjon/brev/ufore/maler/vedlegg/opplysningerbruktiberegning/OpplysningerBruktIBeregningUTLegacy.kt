@file:Suppress("LocalVariableName")

package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning

import no.nav.pensjon.brev.ufore.api.model.vedlegg.Beregningsmetode
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.beregning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.gjenlevendetillegg.*
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
    }
