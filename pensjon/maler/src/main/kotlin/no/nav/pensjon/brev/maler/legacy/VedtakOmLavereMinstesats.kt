package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDto
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.avkortetPgaRedusertTrygdetid
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.egenopptjentUforetrygd
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.endringNettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.endringNettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.endringNettoUforetrygdUtenTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.endringReduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.harGradertUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.harMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.hjemmeltekst
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.nettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.nettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.nettoUforetrygdUtenTillegg
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.nyMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.pe
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.reduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.tidligereMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmLavereMinstesatsDtoSelectors.tillegg
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmLavereMinstesats : AutobrevTemplate<VedtakOmLavereMinstesatsDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_LAVERE_MINSTESATS_2026

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av minstesats fom 1. juli 2026",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Vedtaksbrev - Nav endrer uføretrygden din" },
            )
        }
        outline {
            val sumUtOgTillegg = nettoUforetrygdUtenTillegg + nettoBarnetillegg.ifNull(Kroner(0)) + nettoGjenlevendetillegg.ifNull(Kroner(0))

            title1 {
                text(
                    bokmal { +"Dette er dine endringer fra 1. juli 2026" },
                )
            }
            paragraph {
                table(header = {
                    column { text(bokmal { +"Endring" }) }
                    column(alignment = RIGHT) {}
                }) {
                    showIf(this.endringNettoUforetrygdUtenTillegg) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Ny beregnet uføretrygd" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +nettoUforetrygdUtenTillegg.format() },
                                )
                            }
                        }
                        showIf(this.endringNettoBarnetillegg) {
                            row {
                                cell {
                                    text(
                                        bokmal { +"Nytt barnetillegg" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +nettoBarnetillegg.ifNull(Kroner(0)).format() },
                                    )
                                }
                            }
                        }
                        showIf(this.endringNettoGjenlevendetillegg) {
                            row {
                                cell {
                                    text(
                                        bokmal { +"Nytt gjenlevendetillegg" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +nettoGjenlevendetillegg.ifNull(Kroner(0)).format() },
                                    )
                                }
                            }
                        }
                        showIf(this.endringReduksjonsprosent) {
                            row {
                                cell {
                                    text(
                                        bokmal { +"Ny reduksjonsprosent" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +reduksjonsprosent.format() + " prosent" },
                                    )
                                }
                            }
                        }
                        showIf(this.harMinstesats) {
                            row {
                                cell {
                                    text(
                                        bokmal { +"Ny minstesats" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +"2,329 G" },
                                    )
                                }
                            }
                        }
                    }
                }
            }
            paragraph {
                text(bokmal {
                    +"Du får " + sumUtOgTillegg.format() + " i " + tillegg.format(UTOgTilleggMapper)
                    +" per måned før skatt fra 1. juli 2026."
                })
            }
            paragraph { text(bokmal { +"Uføretrygden blir utbetalt senest den 20. hver måned." }) }
            paragraph {
                text(bokmal { +"I vedlegget " })
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(bokmal { +" kan du se hvordan vi har beregnet uføretrygden din." })
            }

            title1 { text(bokmal { +"Derfor endrer vi uføretrygden din" }) }
            paragraph {
                text(
                    bokmal { +"Du får endring i uføretrygden din fordi Stortinget har vedtatt at minstesatsen endres 1. juli 2026.  " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fra 1. juli 2026 endres minstesatsen for gifte/samboende fra 2,379 G (" + tidligereMinstesats.format() + ") til 2,329 G (" + nyMinstesats.format() + ")." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Lovendringen gjelder for deg som fikk uførepensjonen din omregnet til uføretrygd i 2015. Endringen fører til at alle gifte og samboene nå får lik minstesats." },
                )
            }
            paragraph {
                text(
                    bokmal { +"I 2015 ble uførepensjon endret til uføretrygd. For deg som allerede var uføretrygdet, førte det til at ytelsen din ble regnet om etter de nye reglene." },
                )
            }
            showIf(!harMinstesats) {
                paragraph {
                    text(
                        bokmal { +"Siden din egenopptjening er høyere enn minstesatsen (2,329 G), bruker vi din opptjening i beregningen. Dette gir deg en høyere uføretrygd. Din egenopptjening er kroner " + egenopptjentUforetrygd.format() + ". " },
                    )
                }
            }
            showIf(avkortetPgaRedusertTrygdetid) {
                paragraph {
                    text(
                        bokmal { +"Du har avkortet uføretrygd på grunn av redusert trygdetid. Derfor er minstesatsen din lavere enn 2,329 G." },
                    )
                }
            }
            showIf(harGradertUfoeretrygd) {
                paragraph {
                    text(
                        bokmal { +"Denne endringen påvirker også deg som har gradert uføretrygd." },
                    )
                }
            }
            showIf(endringNettoBarnetillegg) {
                title1 { text(bokmal { +"Endring i barnetillegg" }) }
                paragraph {
                    text(
                        bokmal {
                            +"Regelverksendringene fører til at du får en endret utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. " +
                                    "Derfor får du en endret utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er " + nettoBarnetillegg.ifNull(Kroner(0)).format() + "."
                        },
                    )
                }
            }
            showIf(endringNettoGjenlevendetillegg) {
                title1 { text(bokmal { +"Endring i gjenlevendetillegg" }) }
                paragraph {
                    text(
                        bokmal {
                            +"Regelverksendringene fører til at gjenlevendetillegg i uføretrygden din endres. Ny beregning av gjenlevendetillegget (før skatt) er " +
                                    nettoGjenlevendetillegg.ifNull(Kroner(0)).format() + "."
                        },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Vedtaket har vi gjort etter " + hjemmeltekst + "." },
                )
            }

            title1 { text(bokmal { +"Dette kan du gjøre nå" }) }
            paragraph {
                text(bokmal {
                    +"Du har rett til å klage på vedtaket, selv om endringen i uføretrygden din skyldes endringer i lovverket. " +
                            "Mener du vi har feil opplysninger om saken din, kan du også klage på vedtaket."
                })
            }

            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, this.maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, this.pe, this.pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, this.orienteringOmRettigheterUfoere)
    }
}