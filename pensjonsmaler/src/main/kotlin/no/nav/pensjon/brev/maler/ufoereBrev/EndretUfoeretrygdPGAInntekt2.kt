package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.BarnetilleggFellesbarnSelectors.endringsbelop_safe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.BarnetilleggFellesbarnSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.BarnetilleggSaerkullsbarnSelectors.endringsbelop_safe
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.BarnetilleggSaerkullsbarnSelectors.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.gjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.totalNetto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.uforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.EndretUfoeretrygdPGAInntektDto2Selectors.virkningFom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.GjenlevendetilleggSelectors.belop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.UforetrygdSelectors.endringsbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUfoeretrygdPGAInntekt.UforetrygdSelectors.netto


import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object EndretUfoeretrygdPGAInntekt2 : AutobrevTemplate<EndretUfoeretrygdPGAInntektDto2> {

    // PE_UT_05_100
    override val kode = Pesysbrevkoder.AutoBrev.UT_ENDRET_PGA_INNTEKT_2

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndretUfoeretrygdPGAInntektDto2::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd på grunn av inntekt (automatisk)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        val endretUt = uforetrygd.endringsbelop.notEqualTo(0)
        val endretBt = barnetilleggFellesbarn.endringsbelop_safe.notEqualTo(0).ifNull(false) and
                barnetilleggSaerkullsbarn.endringsbelop_safe.notEqualTo(0).ifNull(false)

        title {
            showIf(endretUt and not(endretBt)) {
                text(
                    Bokmal to "Nav har endret utbetalingen av uføretrygden din",
                    Nynorsk to "Nav har endra utbetalinga av uføretrygda di",
                )
            }.orShowIf(endretUt and endretBt) {
                text(
                    Bokmal to "Nav har endret utbetalingen av uføretrygden din og barnetillegget ditt",
                    Nynorsk to "Nav har endret utbetalingen av uføretrygden din og barnetillegget ditt",
                )
            }.orShow {
                text(
                    Bokmal to "Nav har endret utbetalingen av barnetillegget i uføretrygden din",
                    Nynorsk to "Nav har endret utbetalingen av barnetillegget i uføretrygda di",
                )
            }
        }

        // Vi bruker dette som inntektsplanlegger-brevet
        // TODO: om inntekt er for høy, avsnitt om det
        outline {
            paragraph {
                // TODO: favne bedre - kan komme fra flere steder
                textExpr(
                    Bokmal to "Vi har nå behandlet din melding om endring av inntekt. Ny utbetalingen gjelder fra ".expr() + virkningFom.format() + ". ",
                    Nynorsk to "".expr()
                )
            }
            paragraph {
                // TODO: bruttto er avkorta ikke skatt
                showIf((barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()) and gjenlevendetillegg.notNull()) {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalNetto.format() + " i uføretrygd, barne- og gjenlevendetillegg per måned før skatt. " +
                                "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned. ",
                        Nynorsk to "".expr()
                    )
                }.orShowIf((barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()) and gjenlevendetillegg.isNull()) {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalNetto.format() + " i uføretrygd og barnetillegg per måned før skatt. " +
                                "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned. ",
                        Nynorsk to "".expr()
                    )
                }.orShowIf(not(barnetilleggFellesbarn.notNull() or barnetilleggSaerkullsbarn.notNull()) and gjenlevendetillegg.notNull()) {
                    textExpr(
                        Bokmal to "Du får ".expr() + (totalNetto).format() + " i uføretrygd og gjenlevendetillegg per måned før skatt. " +
                                "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned. ",
                        Nynorsk to "".expr()
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Du får ".expr() + totalNetto.format() + " i uføretrygd per måned før skatt. " +
                                "Uføretrygden blir fortsatt utbetalt senest den 20. hver måned. ",
                        Nynorsk to "".expr()
                    )
                }
            }

            title2 {
                textExpr(
                    Bokmal to "Din månedlige utbetaling fra ".expr() + virkningFom.format(),
                    Nynorsk to "".expr(),
                )
            }

            paragraph {
                table(
                    header = {
                        column(1) {}
                        column(columnSpan = 1, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                            text(
                                Bokmal to "Beløp før skatt per måned",
                                Nynorsk to "",
                            )
                        }
                    }
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "Uføretrygd",
                                Nynorsk to "",
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to uforetrygd.netto.format(),
                                Nynorsk to "".expr(),
                            )
                        }
                    }
                    ifNotNull(barnetilleggSaerkullsbarn) { barnetilleggSaerkullsbarn ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg særkullsbarn",
                                    Nynorsk to "",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to barnetilleggSaerkullsbarn.netto.format(),
                                    Nynorsk to "".expr(),
                                )
                            }
                        }
                    }
                    ifNotNull(barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Barnetillegg fellesbarn",
                                    Nynorsk to "",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to barnetilleggFellesbarn.netto.format(),
                                    Nynorsk to "".expr(),
                                )
                            }
                        }
                    }
                    ifNotNull(gjenlevendetillegg) { gjenlevendetillegg ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Gjenlevendetillegg",
                                    Nynorsk to "",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to gjenlevendetillegg.belop.format(),
                                    Nynorsk to "".expr(),
                                )
                            }
                        }
                    }
                }
            }
        }

        /* outline {
             includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
             includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgPlikterUfoere))
             includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
             includePhrase(Ufoeretrygd.SjekkUtbetalingene)
             includePhrase(Ufoeretrygd.Skattekort)
             includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(brukerBorINorge))
             includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
         }

         includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
         includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe, pe.inkluderopplysningerbruktiberegningen())
         includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    */ }
}
