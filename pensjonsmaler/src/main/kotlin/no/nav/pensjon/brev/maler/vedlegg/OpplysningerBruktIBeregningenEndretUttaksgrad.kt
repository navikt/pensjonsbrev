package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2025
import no.nav.pensjon.brev.api.model.Beregningsmetode.FOLKETRYGD
import no.nav.pensjon.brev.api.model.Beregningsmetode.NORDISK
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.AlderspensjonSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.andelKap19
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.andelKap20
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.BeregningKap19VedVirkSelectors.forholdstallLevealder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.BeregningKap19VedVirkSelectors.poengAr
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.BeregningKap19VedVirkSelectors.poengAre91
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.BeregningKap19VedVirkSelectors.poengArf92
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.BeregningKap19VedVirkSelectors.sluttpoengtall
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.BeregningKap20VedVirkSelectors.delingstallLevealder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.BrukerSelectors.fodselsdato
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.EndretUttaksgradVedVirkSelectors.garantipensjonsBeholdning
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.EndretUttaksgradVedVirkSelectors.pensjonsbeholdning
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.EndretUttaksgradVedVirkSelectors.restGrunnpensjon
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.EndretUttaksgradVedVirkSelectors.restTilleggspensjon
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.OppfrisketOpptjeningVedVirkSelectors.PGISisteGyldigeOpptjeningsAr_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.OppfrisketOpptjeningVedVirkSelectors.opptjeningTilfortKap20
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.OppfrisketOpptjeningVedVirkSelectors.poengtallSisteGyldigeOpptjeningsAr_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.OppfrisketOpptjeningVedVirkSelectors.sisteGyldigeOpptjeningsAr_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.TrygdetidsdetaljerKap19VedVirkSelectors.beregningsmetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.alderspensjon
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.beregningKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.beregningKap20VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.endretUttaksgradVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.krav
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.oppfrisketOpptjeningVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.trygdetidsdetaljerKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenEndretUttaksgradDtoSelectors.trygdetidsdetaljerKap20VedVirk
import no.nav.pensjon.brev.maler.fraser.common.AntallAarText
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.TableScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.year
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

// V00005 i metaforce
@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningenEndretUttaksgrad =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenEndretUttaksgradDto>(
        // vedleggBeregnTittel_001
        title = newText(
            Bokmal to "Opplysninger brukt i beregningen",
            Nynorsk to "Opplysningar brukte i berekninga",
            English to "Information about your calculation",
        ),
        includeSakspart = false
    ) {
        //  vedleggBeregnInnledn_001
        paragraph {
            text(
                Bokmal to "I dette vedlegget finner du opplysninger om deg og din pensjonsopptjening som vi har brukt i beregningen av pensjonen din. Hvis du mener at opplysningene er feil, må du melde fra til Nav, fordi det kan ha betydning for størrelsen på pensjonen din.",
                Nynorsk to "I dette vedlegget finn du opplysningar om deg og pensjonsoppteninga di som vi har brukt i berekninga av pensjonen din. Dersom du meiner at opplysningane er feil, må du melde frå til Nav, fordi det kan ha noko å seie for storleiken på pensjonen din.",
                English to "This appendix contains information about you and your accumulated pension rights, which we have used to calculate your pension. If you think the information is incorrect, you must notify Nav, as this may affect the size of your pension."
            )
        }
        // vedleggBeregnUttaksgrad_001
        paragraph {
            textExpr(
                Bokmal to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                Nynorsk to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                English to "The rate of your retirement pension is ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " percent."
            )
        }

        showIf(alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)) {
            // vedleggBeregnGradsEndrAP2011_001
            paragraph {
                text(
                    Bokmal to "Når en gradert pensjon skal endres eller oppdateres, skal løpende pensjon først legges tilbake i restpensjonen. Det er årlig pensjon på endringstidspunktet som benyttes. Dette gjøres ved å multiplisere pensjonen med gjeldende forholdstall på endringstidspunktet. Deretter legges beløpet til den restpensjonen du har fra før av. Dette er utgangspunktet for å beregne ny pensjon.",
                    Nynorsk to "Når ein gradert pensjon skal endrast eller oppdaterast, skal løpande pensjon først leggjast tilbake i restpensjonen. Vi bruker årleg pensjon på endringstidspunktet. Dette gjer vi ved å multiplisere pensjonen med gjeldande forholdstal på endringstidspunktet. Deretter blir beløpet lagt til den restpensjonen du har frå før av. Dette er utgangspunktet for å berekne ny pensjon.",
                    English to "When a graded pension is to be changed or updated, the current pension must first be credited back to the remaining pension. It is the annual pension at the time of the change that should be used. This is done by multiplying the pension by the relevant ratio at the time of the change. This figure is then added to the remaining pension that you have from before. This is the starting point for calculation of the new pension."
                )
            }
        }.orShowIf(alderspensjonVedVirk.regelverkType.equalTo(AP2016)) {
            //  vedleggBeregnGradsEndrAP2016_001
            paragraph {
                text(
                    Bokmal to "Når en gradert pensjon skal endres eller oppdateres, skal løpende pensjon først legges tilbake i restpensjonen/beholdningen. Det er årlig pensjon på endringstidspunktet som benyttes. Dette gjøres ved å multiplisere pensjonen med gjeldende forholdstall/delingstall på endringstidspunktet. Deretter legges beløpet til den restpensjonen/beholdningen du har fra før av. Dette er utgangspunktet for å beregne ny pensjon.",
                    Nynorsk to "Når ein gradert pensjon skal endrast eller oppdaterast, skal løpande pensjon først leggjast tilbake i restpensjonen/behaldninga. Vi bruker årleg pensjon på endringstidspunktet. Dette gjer vi ved å multiplisere pensjonen med gjeldande forholdstal/delingstal på endringstidspunktet. Deretter blir beløpet lagt til den restpensjonen/behaldninga du har frå før. Dette er utgangspunktet for å berekne ny pensjon.",
                    English to "When a graded pension is to be changed or updated, the current pension must first be credited back to the remaining pension / your pension capital. It is the annual pension at the time of the change that should be used. This is done by multiplying the pension by the relevant ratio at the time of the change. This figure is then added to the remaining pension / the pension capital that you have from before. This is the starting point for calculation of the new pension."
                )
            }
        }.orShowIf(alderspensjonVedVirk.regelverkType.equalTo(AP2025)) {
            // vedleggBeregnGradsEndrAP2025_001
            paragraph {
                text(
                    Bokmal to "Når en gradert pensjon endres, blir pensjonen du får utbetalt før skatt på endringstidspunktet omregnet til en beholdningsstørrelse. Dette gjøres ved å multiplisere pensjonen med delingstall på endringstidspunktet. Deretter legges beløpet til den beholdningen du har fra før av. Dette er utgangspunktet for å beregne ny pensjon.",
                    Nynorsk to "Når ein gradert pensjon endrast, blir pensjonen du får utbetalt før skatt på endringstidspunktet rekna om til ei behaldning. Dette gjer vi ved å multiplisere pensjonen med gjeldande delingstal på endringstidspunktet. Deretter blir beløpet lagt til den behaldninga du har frå før. Dette er utgangspunktet for å berekne ny pensjon.",
                    English to "When a guaranteed pension is adjusted, the pension you receive before tax at the time of adjustment is converted into a capital balance amount. This is done by multiplying the pension by the divisor at the time of adjustment. This amount is then added to the capital balance amount you had before. This is the basis for calculating the new pension."
                )
            }
        }

        // vedleggBeregnGradsEndrOpptjen_001
        ifNotNull(oppfrisketOpptjeningVedVirk.sisteGyldigeOpptjeningsAr_safe) { sisteGyldigeOpptjeningsaar ->
            ifNotNull(oppfrisketOpptjeningVedVirk.PGISisteGyldigeOpptjeningsAr_safe) { pGISisteGyldigeOpptjeningsaar ->
                paragraph {
                    textExpr(
                        Bokmal to "Fordi du har søkt om endring av uttaksgrad vil ny pensjonsopptjening for inntektsåret ".expr() + sisteGyldigeOpptjeningsaar.format() + " bli lagt til alderspensjonen din.",
                        Nynorsk to "Fordi du har søkt om endring av uttaksgrad vil ny pensjonsopptjening for inntektsåret ".expr() + sisteGyldigeOpptjeningsaar.format() + " bli lagt til alderspensjonen din.",
                        English to "Because you have applied to change your pension withdrawal percentage, your new pension earnings for the income year ".expr() + sisteGyldigeOpptjeningsaar.format() + " will be added to your retirement pension."
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Du har ".expr() + pGISisteGyldigeOpptjeningsaar.format() + " kroner i pensjonsgivende inntekt i dette året. ",
                        Nynorsk to "Du har ".expr() + pGISisteGyldigeOpptjeningsaar.format() + " kroner i pensjonsgivande inntekt i dette året.",
                        English to "Your pensionable income for this year is NOK ".expr() + pGISisteGyldigeOpptjeningsaar.format() + "."
                    )
                }

            }
        }

        showIf(alderspensjonVedVirk.regelverkType.notEqualTo(AP2025)) {
            // vedleggBeregnGradsEndrInfo_001
            paragraph {
                text(
                    Bokmal to "Du kan finne opplysninger om endringer i restpensjon og beholdning ved ny opptjening og regulering i nettjenesten Din pensjon på $DIN_PENSJON_URL.",
                    Nynorsk to "Du kan finne opplysningar om endringar i restpensjon og behaldning ved ny opptening og regulering i nettenesta Din pensjon på $DIN_PENSJON_URL.",
                    English to "You can find information about changes in the remaining pension and the pension capital in connection with new earnings and adjustment in our online service Din pensjon at $DIN_PENSJON_URL."
                )
            }
        }

        showIf(alderspensjonVedVirk.regelverkType.equalTo(AP2016)) {
            //  vedleggBelopAP2016Oversikt_001
            paragraph {
                textExpr(
                    Bokmal to "De som er født i perioden 1954-1962 får en kombinasjon av alderspensjon etter gamle og nye regler i folketrygdloven (kapittel 19 og 20). Fordi du er født i ".expr() + bruker.fodselsdato.year.format()
                            + " får du beregnet " + alderspensjonVedVirk.andelKap19.format() + "/10 av pensjonen etter gamle regler, og " + alderspensjonVedVirk.andelKap20.format() + "/10 etter nye regler.",
                    Nynorsk to "Dei som er fødde i perioden 1954-1962 får ein kombinasjon av alderspensjon etter gamle og nye reglar i folketrygdlova (kapittel 19 og 20). Fordi du er fødd i ".expr() + bruker.fodselsdato.year.format()
                            + " får du rekna ut " + alderspensjonVedVirk.andelKap19.format() + "/10 av pensjonen etter gamle reglar, og " + alderspensjonVedVirk.andelKap20.format() + "/10 etter nye reglar.",
                    English to "]Individuals born between 1954 and 1962 will receive a combination of retirement pension calculated on the basis of both old and new provisions in the National Insurance Act (Chapters 19 and 20). Because you are born in ".expr() +
                            bruker.fodselsdato.year.format() + ", " + alderspensjonVedVirk.andelKap19.format() + "/10 of your pension is calculated on the basis of the old provisions, and " + alderspensjonVedVirk.andelKap20.format() + "/10 is calculated on the basis of new provisions."
                )
            }

            // vedleggBeregnAP2016Kap19_001
            paragraph {
                text(
                    Bokmal to "For den delen av pensjonen din som er beregnet etter regler i kapittel 19 har vi brukt disse opplysningene i beregningen vår:",
                    Nynorsk to "For den delen av pensjonen din som er berekna etter reglar i kapittel 19, har vi brukt desse opplysningane i berekninga vår:",
                    English to "We have used the following information to calculate the part of your pension that comes under the provisions of Chapter 19:"
                )
            }
        }

        showIf(alderspensjonVedVirk.regelverkType.notEqualTo(AP2025)) {
            paragraph {
                table(
                    header = {
                        // vedleggBeregnTabellOverskrift_002
                        column(columnSpan = 2, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT) {
                            textExpr(
                                Bokmal to "Opplysninger brukt i beregningen fra ".expr() + krav.virkDatoFom.format(),
                                Nynorsk to "Opplysninger brukte i berekninga frå ".expr() + krav.virkDatoFom.format(),
                                English to "Information used in the calculation as of ".expr() + krav.virkDatoFom.format()
                            )
                        }
                        column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {}
                    }
                ) {
                    showIf(trygdetidsdetaljerKap19VedVirk.anvendtTT.greaterThan(0) and trygdetidsdetaljerKap19VedVirk.beregningsmetode.isOneOf(FOLKETRYGD, NORDISK)) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Trygdetid",
                                    Nynorsk to "Trygdetid",
                                    English to "National insurance coverage"
                                )
                            }
                            cell {
                                includePhrase(AntallAarText(trygdetidsdetaljerKap19VedVirk.anvendtTT))
                            }
                        }
                    }
                    ifNotNull(oppfrisketOpptjeningVedVirk.sisteGyldigeOpptjeningsAr_safe) {
                        row {
                            cell {
                                text(
                                    Bokmal to "Siste gyldige inntektsår",
                                    Nynorsk to "Siste gyldige inntektsår",
                                    English to "Last valid income year"
                                )
                            }
                            cell {
                                eval(it.format())
                            }
                        }
                    }
                    ifNotNull(oppfrisketOpptjeningVedVirk.PGISisteGyldigeOpptjeningsAr_safe) {
                        showIf(it.greaterThan(0)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Pensjonsgivende inntekt",
                                        Nynorsk to "Pensjonsgivande inntekt",
                                        English to "Pensionable income"
                                    )
                                }
                                cell {
                                    eval(it.format())
                                }
                            }
                        }
                    }
                    ifNotNull(oppfrisketOpptjeningVedVirk.poengtallSisteGyldigeOpptjeningsAr_safe) {
                        // vedleggTabellKap19SistePensjonsPoeng_001
                        showIf(it.greaterThan(0.0)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Pensjonspoeng i siste godskrivingsår",
                                        Nynorsk to "Pensjonspoeng i siste godskrivingsår",
                                        English to "Pension points in the last valid income year"
                                    )
                                }
                                cell {
                                    eval(it.format())
                                }
                            }
                        }
                    }
                    showIf(beregningKap19VedVirk.sluttpoengtall.greaterThan(0) and trygdetidsdetaljerKap19VedVirk.beregningsmetode.isOneOf(FOLKETRYGD, NORDISK)) {
                     // vedleggTabellKap19Sluttpoengtall_001_]
                        row {
                            cell {
                                text(
                                    Bokmal to "Sluttpoengtall",
                                    Nynorsk to "Sluttpoengtal",
                                    English to "Final pension point score"
                                )
                            }
                            cell {
                                eval(beregningKap19VedVirk.sluttpoengtall.format())
                            }
                        }
                    }

                    showIf(beregningKap19VedVirk.poengAr.greaterThan(0) and trygdetidsdetaljerKap19VedVirk.beregningsmetode.equalTo(FOLKETRYGD)) {
                        // vedleggTabellKap19PoengAr_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Antall poengår",
                                    Nynorsk to "Talet på poengår",
                                    English to "Number of pension point earning years"
                                )
                            }
                            cell {
                                includePhrase(AntallAarText(beregningKap19VedVirk.poengAr))
                            }
                        }
                    }

                    showIf(beregningKap19VedVirk.poengArf92.greaterThan(0) and trygdetidsdetaljerKap19VedVirk.beregningsmetode.isOneOf(FOLKETRYGD, NORDISK)) {
                        // vedleggTabellKap19PoengArf92_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Antall år med pensjonsprosent 45",
                                    Nynorsk to "Talet på år med pensjonsprosent 45",
                                    English to "Number of years calculated with pension percentage 45"
                                )
                            }
                            cell {
                                includePhrase(AntallAarText(beregningKap19VedVirk.poengArf92))
                            }
                        }
                    }

                    showIf(beregningKap19VedVirk.poengAre91.greaterThan(0) and trygdetidsdetaljerKap19VedVirk.beregningsmetode.isOneOf(FOLKETRYGD, NORDISK)) {
                        // vedleggTabellKap19PoengAre91_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Antall år med pensjonsprosent 42",
                                    Nynorsk to "Talet på år med pensjonsprosent 42",
                                    English to "Number of years calculated with pension percentage 42"
                                )
                            }
                            cell {
                                includePhrase(AntallAarText(beregningKap19VedVirk.poengAre91))
                            }
                        }
                    }

                    ifNotNull(endretUttaksgradVedVirk.restGrunnpensjon) {
                        //  vedleggTabellRestGP_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Grunnpensjon under opptjening (restgrunnpensjon)",
                                    Nynorsk to "Grunnpensjon under opptening (restgrunnpensjon)",
                                    English to "Remaining basic pension"
                                )
                            }
                            cell {
                                includePhrase(KronerText(it))
                            }
                        }
                    }

                    ifNotNull(endretUttaksgradVedVirk.restTilleggspensjon) {
                        //  vedleggTabellRestTP_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Tilleggspensjon under opptjening (resttilleggspensjon)",
                                    Nynorsk to "Tilleggspensjon under opptening (resttilleggspensjon)",
                                    English to "Remaining supplementary pension"
                                )
                            }
                            cell {
                                includePhrase(KronerText(it))
                            }
                        }
                    }

                    showIf(beregningKap19VedVirk.forholdstallLevealder.greaterThan(0.0)) {
                        // vedleggTabellKap19Forholdstall_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Forholdstall ved uttak",
                                    Nynorsk to "Forholdstal ved uttak",
                                    English to "Ratio at withdrawal"
                                )
                            }
                            cell {
                                eval(beregningKap19VedVirk.forholdstallLevealder.format(scale = 3))
                            }
                        }
                    }

                }
            }
        }

        showIf(alderspensjonVedVirk.regelverkType.equalTo(AP2016)) {
            // vedleggBeregnAP2016Kap20_001
            paragraph {
                text(
                    Bokmal to "For den delen av pensjonen din som er beregnet etter nye regler (kapittel 20) har vi brukt disse opplysningene i beregningen vår:",
                    Nynorsk to "For den delen av pensjonen din som er berekna etter nye reglar (kapittel 20), har vi brukt desse opplysningane i berekninga vår:",
                    English to "We have used the following information to calculate the part of your pension that comes under the new provisions (Chapter 20):"
                )
            }
        }

        showIf(alderspensjonVedVirk.regelverkType.isOneOf(AP2016, AP2025)) {
            paragraph {
                // vedleggBeregnTabellOverskrift_002
                table(
                    header = {
                        column(columnSpan = 2, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.LEFT) {
                            textExpr(
                                Bokmal to "Opplysninger brukt i beregningen fra ".expr() + krav.virkDatoFom.format(),
                                Nynorsk to "Opplysninger brukte i berekninga frå ".expr() + krav.virkDatoFom.format(),
                                English to "Information used in the calculation as of ".expr() + krav.virkDatoFom.format()
                            )
                        }
                        column(alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {}
                    }
                ) {
                    showIf(trygdetidsdetaljerKap20VedVirk.anvendtTT.greaterThan(0)) {
                        // vedleggTabellKap20Trygdetid_001
                        row {
                            cell {
                                textExpr(
                                    Bokmal to "Trygdetid".expr() + ifElse(alderspensjon.regelverkType.equalTo(AP2016), " etter kapittel 20", ""),
                                    Nynorsk to "Trygdetid".expr() + ifElse(alderspensjon.regelverkType.equalTo(AP2016), " etter kapittel 20", ""),
                                    English to "National insurance coverage".expr() + ifElse(alderspensjon.regelverkType.equalTo(AP2016), " pursuant to Chapter 20", "")
                                )
                            }
                            cell {
                                includePhrase(AntallAarText(trygdetidsdetaljerKap20VedVirk.anvendtTT))
                            }
                        }
                    }

                    showIf(oppfrisketOpptjeningVedVirk.opptjeningTilfortKap20.greaterThan(0)) {
                        //  vedleggTabellKap20NyOpptjening_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Ny opptjening",
                                    Nynorsk to "Ny opptening",
                                    English to "New accumulated pension capital"
                                )
                            }
                            cell {
                                includePhrase(KronerText(oppfrisketOpptjeningVedVirk.opptjeningTilfortKap20))
                            }
                        }
                    }

                    showIf(endretUttaksgradVedVirk.pensjonsbeholdning.greaterThan(0) and alderspensjonVedVirk.regelverkType.equalTo(AP2016)) {
                        // tabellBeholdningEtterUttak_001
                        tabellPensjonsbeholdning()
                    }

                    showIf(endretUttaksgradVedVirk.pensjonsbeholdning.greaterThan(0) and alderspensjonVedVirk.regelverkType.equalTo(AP2025)) {
                        // tabellBeholdningEtterUttak_001
                        tabellPensjonsbeholdning()
                    }

                    showIf(endretUttaksgradVedVirk.garantipensjonsBeholdning.greaterThan(0)) {
                        // vedleggTabellGarantiPbeholdningEtterUttak_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Garantipensjonsbeholdning",
                                    Nynorsk to "Garantipensjonsbehaldning",
                                    English to "Guaranteed pension capital"
                                )
                            }
                            cell {
                                includePhrase(KronerText(endretUttaksgradVedVirk.garantipensjonsBeholdning))
                            }
                        }
                    }

                    showIf(beregningKap20VedVirk.delingstallLevealder.greaterThan(0.0) and alderspensjonVedVirk.regelverkType.equalTo(AP2016)) {
                        // vedleggTabellKap20Delingstall_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Delingstall ved levealdersjustering",
                                    Nynorsk to "Delingstal ved levealdersjustering",
                                    English to "Ratio for life expectancy adjustment"
                                )
                            }
                            cell {
                                eval(beregningKap20VedVirk.delingstallLevealder.format())
                            }
                        }
                    }

                    showIf(beregningKap20VedVirk.delingstallLevealder.greaterThan(0.0) and alderspensjonVedVirk.regelverkType.equalTo(AP2025)) {
                        // vedleggTabellKap20DelingstallGradEndr_001
                        row {
                            cell {
                                text(
                                    Bokmal to "Delingstall ved endring",
                                    Nynorsk to "Delingstal ved endring",
                                    English to "Divisor at the time of adjustment"
                                )
                            }
                            cell {
                                eval(beregningKap20VedVirk.delingstallLevealder.format())
                            }
                        }
                    }
                }
            }
        }

        //  infoAPhenvNav.no_001
        paragraph {
            text(
                Bokmal to "På $PENSJON_URL kan du lese mer om regelverket for alderspensjon og hvordan opplysningene vi har lagt til grunn har betydning for beregningen. I nettjenesten Din pensjon på $DIN_PENSJON_URL kan du se hvilke inntekter og opplysninger om opptjening som vi har registrert.",
                Nynorsk to "På $PENSJON_URL kan du lese meir om regelverket for alderspensjon og kva opplysningane vi har lagt til grunn, har å seie for berekninga. I nettenesta Din pensjon på $DIN_PENSJON_URL kan du sjå kva inntekter og opplysningar om opptening vi har registrert.",
                English to "Go to $PENSJON_URL to read more about these regulations that apply to retirement pension and how these affect your calculation. Logon to our online service \"Din pensjon\" at $DIN_PENSJON_URL to see your income and accumulated pension capital."
            )
        }


    }

private fun TableScope<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenEndretUttaksgradDto>.tabellPensjonsbeholdning() {
    row {
        cell {
            text(
                Bokmal to "Pensjonsbeholdning",
                Nynorsk to "Pensjonsbehaldning",
                English to "Accumulated pension capital"
            )
        }
        cell {
            includePhrase(KronerText(endretUttaksgradVedVirk.pensjonsbeholdning))
        }
    }
}