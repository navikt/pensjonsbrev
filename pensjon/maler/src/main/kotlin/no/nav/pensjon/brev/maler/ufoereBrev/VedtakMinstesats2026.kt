package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VedtakMinstesats2026Dto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VedtakMinstesats2026DtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VedtakMinstesats2026DtoSelectors.pe
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.inkluderopplysningerbruktiberegningen
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakMinstesats2026  : AutobrevTemplate<VedtakMinstesats2026Dto> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_VEDTAK_MINSTESATS_2026

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av minstesats fom 1. juli 2026",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Vedtaksbrev - Nav endrer uføretrygden din" },
                nynorsk { +"Vedtaksbrev - Nav endrar uføretrygda di" }
            )
        }

        outline {
            title1 {
                text(
                    bokmal { +"Dette er dine endringer fra 1. juli 2026" },
                    nynorsk { +"Dette er dine endringar frå 1. juli 2026" }
                )
            }
            paragraph {
                table ({
                    column {
                        text(
                            bokmal { +"Beskrivelse" },
                            nynorsk { +"Skildring" },
                        )
                    }
                    column {
                        text(
                            bokmal { +"Beløp" },
                            nynorsk { +"Beløp" },
                        )
                    }
                }) {
                    row {
                        cell {
                            text(
                                bokmal { + "Ny beregnet uføretrygd" },
                                nynorsk { + "Ny utrekna uføretrygd" }
                            )
                        }
                        cell {
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Nytt tillegg" },
                                nynorsk { + "Nytt tillegg" }
                            )
                        }
                        cell {
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Ny uføregrad" },
                                nynorsk { + "Ny uføregrad" }
                            )
                        }
                        cell {
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Ny reduksjonsprosent" },
                                nynorsk { + "Ny reduksjonsprosent" }
                            )
                        }
                        cell {
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Nytt bunnfradrag" },
                                nynorsk { + "Nytt bunnfrådrag" }
                            )
                        }
                        cell {
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Nytt inntektstak" },
                                nynorsk { + "Nytt inntektstak" }
                            )
                        }
                        cell {
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Ny inntekt før uførhet (IFU)" },
                                nynorsk { + "Ny inntekt før uførleik (IFU)" }
                            )
                        }
                        cell {
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Ny minstesats" },
                                nynorsk { + "Ny minstesats" }
                            )
                        }
                        cell {
                        }
                    }
                }
            }

            paragraph {
                list {
                    item {
                        text(
                            bokmal { + "Du får 33 940 kroner i uføretrygd og barnetillegg per måned før skatt fra 1. juli 2026." },
                            nynorsk { + "Du får 33 940 kroner i uføretrygd og barnetillegg per månad før skatt frå 1. juli 2026." }
                        )
                    }
                    item {
                        text(
                            bokmal { + "Uføretrygden blir utbetalt senest den 20. hver måned." },
                            nynorsk { + "Uføretrygda blir utbetalt seinast den 20. kvar månad." }
                        )
                    }
                    item {
                        text(
                            bokmal { + "I vedlegget " },
                            nynorsk { + "I vedlegget " }, //“Opplysningar om berekning” kan du sjå korleis vi har rekna ut uføretrygda di." }
                        )
                        namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                        text(
                            bokmal { + " kan du se hvordan vi har beregnet uføretrygden din." },
                            nynorsk { + " kan du sjå korleis vi har rekna ut uføretrygda di." }
                        )
                    }
                }
            }

            title2 {
                text(
                    bokmal { +"Derfor endrer vi uføretrygden din" },
                    nynorsk { +"Derfor endrar vi uføretrygda di" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du får en lavere utbetaling av uføretrygd fordi minstesatsen endres som følge av lovendringer som trer i kraft 1. juli 2026." },
                    nynorsk { + "Du får ei lågare utbetaling av uføretrygd fordi minstesatsen endres som følge av lovendringar som trer i kraft 1. juli 2026." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Fra 1. juli 2026 endres minstesatsen for gifte/samboende fra 2,379 G (XX kroner) til 2,329 G (XXX kroner)." },
                    nynorsk { + "Frå 1. juli 2026 endres minstesatsen for gifte/sambuarar fra 2,379 G (XX kroner) til 2,329 G (XXX kroner)." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Denne lovendringen gjelder for deg fordi du har uføretrygd som er en omregnet uførepensjon og er gift/samboende. Endringen fører til at minstesatsen nå likestilles med minstesatsen som gjelder for uføretrygd." },
                    nynorsk { + "Denne lovendringen gjeld for deg fordi du har uføretrygd som er en omrekna uførepensjon og er gift/sambuarar. Endringen fører til at minstesatsen nå likestilles med minstesatsen som gjelder for uføretrygd." }
                )
            }
            paragraph {
                text(
                    bokmal { + "§ 12-13 andre ledd andre punktum." },
                    nynorsk { + "§ 12-13 andre ledd andre punktum." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Uføretrygd het uførepensjon før 2015 og ble beregnet etter egne regler. Du som hadde uførepensjon før 2015, fikk omregnet uførepensjonen din til uføretrygd fra 2015." },
                    nynorsk { + "Uføretrygd het uførepensjon før 2015 og ble beregna etter egne reglar. Du som hadde uførepensjon før 2015, fikk omregna uførepensjonen din til uføretrygd fra 2015." }
                )
            }

            title2 {
                text(
                    bokmal { + "Endring i barnetillegg" },
                    nynorsk { + "Endring i barnetillegg" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Regelverksendringene fører til at du får en endret utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. Derfor får du en endret utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er X kroner." },
                    nynorsk { + "Regelverksendringane fører til at du får en endret utbetaling av uføretrygd. Uføretrygden reknas med som inntekt når vi berektnar barnetillegg. Derfor får du ei endra utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er X kroner." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Vedtaket har vi gjort etter folketrygdloven §§ 12-8 til 12-16 og 22-12." },
                    nynorsk { + "Vedtaket har vi gjort etter folketrygdloven §§ 12-8 til 12-16 og 22-12." }
                )
            }

            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe, pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfore)
    }
}