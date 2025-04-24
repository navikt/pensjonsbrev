package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.GarantipensjonSatsType
import no.nav.pensjon.brev.maler.fraser.common.AntallAarText
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.beregningVirkDatoFom
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.garantipensjonInnvilget
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.nettoUtbetaltPerManed
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.beholdningForForsteUttak
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.BeregningKap20VedVirkSelectors.delingstallLevealder
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.GarantipensjonVedVirkSelectors.satsType_safe
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.TrygdetidsdetaljerKap20VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.VilkaarsVedtakSelectors.avslattGarantipensjon
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.beregningKap20VedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.garantipensjonVedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.trygdetidsdetaljerKap20VedVirk
import no.nav.pensjon.brev.maler.vedlegg.OpplysningerBruktIBeregningenAlderAP2025DtoSelectors.vilkarsVedtak
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.PlainTextOnlyPhrase
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.PlainTextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class OpplysningerBruktIBeregningenAlderAP2025Dto(
    val alderspensjonVedVirk: AlderspensjonVedVirk,
    val beregnetPensjonPerManedVedVirk: BeregnetPensjonPerManedVedVirk,
    val beregningKap20VedVirk: BeregningKap20VedVirk,
    val vilkarsVedtak: VilkaarsVedtak,
    val garantipensjonVedVirk: GarantipensjonVedVirk?,
    val trygdetidsdetaljerKap20VedVirk: TrygdetidsdetaljerKap20VedVirk,
) {

    data class TrygdetidsdetaljerKap20VedVirk(
        val anvendtTT: Int,
    )

    data class GarantipensjonVedVirk(
        val delingstalletVed67Ar: Double,
        val satsType: GarantipensjonSatsType,
    )

    data class VilkaarsVedtak(
        val avslattGarantipensjon: Boolean,
    )

    data class BeregningKap20VedVirk(
        val beholdningForForsteUttak: Kroner,
        val delingstallLevealder: Double,
    )

    data class AlderspensjonVedVirk(
        val uttaksgrad: Int,
        val beregningVirkDatoFom: LocalDate,
        val garantipensjonInnvilget: Boolean,
        val nettoUtbetaltPerManed: Kroner,
    )

    data class BeregnetPensjonPerManedVedVirk(
        val virkDatoFom: LocalDate,
    )
}

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningenAlderAP2025 =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenAlderAP2025Dto>(
        //vedleggBeregnAP2025Tittel_001
        title = newText(
            Bokmal to "Slik har vi beregnet pensjonen din",
            Nynorsk to "Slik har vi berekna pensjonen din",
            English to "This is how we have calculated your pension",
        ),
        includeSakspart = false
    ) {
        //vedleggBeregnInnlednAP2025_001
        paragraph {
            text(
                Bokmal to "I dette vedlegget finner du opplysninger som vi har brukt for å regne ut alderspensjonen din.",
                Nynorsk to "I dette vedlegget finn du opplysningar som vi har brukt for å rekne ut alderspensjonen din.",
                English to "In this attachment, you will find information that we have used to calculate your retirement pension.",
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du mener at opplysningene er feil, må du melde fra til Nav, fordi det kan ha betydning for størrelsen på pensjonen din.",
                Nynorsk to "Viss du meiner at opplysningane er feil, må du melde frå til Nav, fordi det kan ha noko å seie for storleiken på pensjonen din.",
                English to "If you believe any information is incorrect, you must notify Nav, as it may affect the amount of your pension.",
            )
        }
        //vedleggBeregnUttaksgrad_001
        paragraph {
            textExpr(
                Bokmal to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                Nynorsk to "Uttaksgraden for alderspensjonen din er ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " prosent.",
                English to "The rate of your retirement pension is ".expr() + alderspensjonVedVirk.uttaksgrad.format() + " percent.",
            )
        }
        paragraph {
            table({
                //vedleggBeregnTabellOverskrift_001
                column(columnSpan = 4) {
                    textExpr(
                        Bokmal to "Opplysninger brukt i beregningen per ".expr() + alderspensjonVedVirk.beregningVirkDatoFom.format(),
                        Nynorsk to "Opplysningar brukte i berekninga frå ".expr() + alderspensjonVedVirk.beregningVirkDatoFom.format(),
                        English to "Information used to calculate as of ".expr() + alderspensjonVedVirk.beregningVirkDatoFom.format(),
                    )
                }
                column(alignment = RIGHT) { }
            }) {
                row {
                    //tabellBeholdningForForsteUttak_002
                    cell {
                        text(
                            Bokmal to "Pensjonsbeholdning ved uttak",
                            Nynorsk to "Pensjonsbehaldning ved uttak",
                            English to "Accumulated pension capital before initial withdrawal",
                        )
                    }
                    cell { includePhrase(KronerText(beregningKap20VedVirk.beholdningForForsteUttak)) }
                }

                //vedleggTabellKap20Delingstall _002
                row {
                    cell {
                        text(
                            Bokmal to "Delingstall ved uttak",
                            Nynorsk to "Delingstal ved uttak",
                            English to "Life expectancy adjustment divisor at withdrawal",
                        )
                    }
                    cell { eval(beregningKap20VedVirk.delingstallLevealder.format()) }
                }

                //vedleggTabellKap20Trygdetid_001
                showIf(not(vilkarsVedtak.avslattGarantipensjon)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Trygdetid",
                                Nynorsk to "Trygdetid",
                                English to "National insurance coverage",
                            )
                        }
                        cell { AntallAarText(trygdetidsdetaljerKap20VedVirk.anvendtTT) }
                    }
                }

                //vedleggTabellKap20SatsGarP_001
                showIf(
                    alderspensjonVedVirk.garantipensjonInnvilget
                            and alderspensjonVedVirk.nettoUtbetaltPerManed.greaterThan(0)
                ) {
                    row {
                        cell {
                            val hoysats = garantipensjonVedVirk.satsType_safe.equalTo(GarantipensjonSatsType.HOY)
                            val ordinaer = garantipensjonVedVirk.satsType_safe.equalTo(GarantipensjonSatsType.ORDINAER)
                            text(
                                Bokmal to "Sats for garantipensjon",
                                Nynorsk to "Sats for garantipensjon",
                                English to "Guaranteed pension rate",
                            )

                            showIf(hoysats) {
                                text(
                                    Bokmal to " (høy sats)",
                                    Nynorsk to " (høg sats)",
                                    English to " (high rate)",
                                )
                            }.orShowIf(ordinaer) {
                                text(
                                    Bokmal to " (ordinær sats)",
                                    Nynorsk to " (ordinær sats)",
                                    English to " (ordinary rate)",
                                )
                            }

                        }
                        cell {

                        }
                    }
                }

            }
        }
    }