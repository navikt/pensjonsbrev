package no.nav.pensjon.brev.maler.ufore.endring

import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.EndretUforetrygdPGAInntektNesteArDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.barnetilleggFellesbarn.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.barnetilleggSaerkullsbarn.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.fra1GTil04G.dato04G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.gjenlevendetillegg.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.uforetrygd.*
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.barnetilleggFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.barnetilleggFellesbarn.periodisert
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.barnetilleggSaerkullsbarn.periodisert
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.uforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.uforetrygd.okningUforegradVedArskjoring
import no.nav.pensjon.brev.api.model.maler.ufoerApi.endretUtPgaInntekt.selectors.endretUforetrygdPGAInntektNesteArDto.virkningFom
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_UFOERETRYGD_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.INNTEKTSPLANLEGGEREN_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.KONTAKT_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.MELDE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.MINSIDE_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERE_JOBB_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.ufore.fraser.Fribelopsperioder
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

object EndretUforetrygdPGAInntektNesteArFelles {

    data class Outline(val data: Expression<EndretUforetrygdPGAInntektNesteArDto>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            val harBarnetillegg = data.barnetilleggFellesbarn.notNull() or data.barnetilleggSaerkullsbarn.notNull()
            val fellesbarnPeriodisert = data.barnetilleggFellesbarn.safe { periodisert }.ifNull(false)
            val sarkullsbarnPeriodisert = data.barnetilleggSaerkullsbarn.safe { periodisert }.ifNull(false)
            val okningUforegradVedArskjoring = data.uforetrygd.okningUforegradVedArskjoring.ifNull(false)

            title1 {
                text(
                    bokmal { +"Endring i utbetalingen gjelder fra " + data.virkningFom.format() },
                    nynorsk { +"Endring i utbetalinga gjeld frå " + data.virkningFom.format() },
                )
            }
            paragraph {
                table(
                    header = {
                        column(2) {}
                        column(
                            columnSpan = 1,
                            alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
                        ) {
                            text(
                                bokmal { +"Beløp" },
                                nynorsk { +"Beløp" },
                            )
                        }
                    }
                )
                {
                    row {
                        cell {
                            text(
                                bokmal { +"Uføretrygd" },
                                nynorsk { +"Uføretrygd" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.uforetrygd.netto.format() },
                                nynorsk { +data.uforetrygd.netto.format() },
                            )
                        }
                    }
                    ifNotNull(data.barnetilleggSaerkullsbarn) { barnetilleggSB ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Barnetillegg for særkullsbarn" },
                                    nynorsk { +"Barnetillegg for særkullsbarn" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +barnetilleggSB.netto.format() },
                                    nynorsk { +barnetilleggSB.netto.format() }
                                )
                            }
                        }
                    }
                    ifNotNull(data.barnetilleggFellesbarn) { barnetilleggFB ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Barnetillegg for fellesbarn" },
                                    nynorsk { +"Barnetillegg for fellesbarn" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +barnetilleggFB.netto.format() },
                                    nynorsk { +barnetilleggFB.netto.format() }
                                )
                            }
                        }
                    }
                    ifNotNull(data.gjenlevendetillegg) { gjenlevendetillegg ->
                        row {
                            cell {
                                text(
                                    bokmal { +"Gjenlevendetillegg" },
                                    nynorsk { +"Attlevandetillegg" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +gjenlevendetillegg.belop.format() },
                                    nynorsk { +gjenlevendetillegg.belop.format() }
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Sum per måned før skatt" },
                                nynorsk { +"Sum per månad før skatt" },
                                FontType.BOLD
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.totalNetto.format() },
                                nynorsk { +data.totalNetto.format() },
                                FontType.BOLD
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Fribeløp" },
                                nynorsk { +"Fribeløp" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.uforetrygd.fribelop.format() },
                                nynorsk { +data.uforetrygd.fribelop.format() }
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Bunnfradrag" },
                                nynorsk { +"Botnfrådrag" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +data.uforetrygd.bunnfradrag.format() },
                                nynorsk { +data.uforetrygd.bunnfradrag.format() }
                            )
                        }
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Endringen av utbetalingen din i " + data.virkningFom.year.format() + " påvirker ikke utbetalingen din i " + data.virkningFom.year.minus(1).format() },
                    nynorsk { +"Endringa av utbetalinga di i " + data.virkningFom.year.format() + " påverkar ikkje utbetalinga di i " + data.virkningFom.year.minus(1).format() }
                )
            }
            paragraph {
                text(
                    bokmal { +"Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                    nynorsk { +"Uføretrygda blir framleis utbetalt seinast den 20. kvar månad." },
                )
            }
            showIf(data.sokerMottarApIlaAret) {
                paragraph {
                    text(
                        bokmal { +"Fordi du får alderspensjon fra " + data.datoForNormertPensjonsalder.format() + ", er inntekten og bunnfradraget justert ut fra antall måneder du får uføretrygd." },
                        nynorsk { +"Fordi du får alderspensjon frå " + data.datoForNormertPensjonsalder.format() + ", er inntekta og botnfrådraget justert ut frå talet på månader du får uføretrygd." }
                    )
                }
            }.orIfNotNull(data.opphorDato) { dato ->
                paragraph {
                    text(
                        bokmal { +"Fordi uføretrygden din opphører fra " + dato.format() + ", er inntekten og bunnfradraget justert ut fra antall måneder du får uføretrygd. " },
                        nynorsk { +"Fordi uføretrygda di opphøyrer frå " + dato.format() + ", er inntekta og botnfrådraget justert ut frå talet på månader du får uføretrygd." }
                    )
                }
            }

            ifNotNull(data.barnetilleggFellesbarn) { barnetilleggFB ->
                showIf(barnetilleggFB.inntektAnnenForelder.greaterThan(0) and barnetilleggFB.inntektBruker.greaterThan(0)) {
                    title1 {
                        text(
                            bokmal { +"Derfor oppjusterer vi din og annen forelders inntekt" },
                            nynorsk { +"Derfor oppjusterer vi inntekta di og inntekta til den andre forelderen" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Vi oppjusterer din inntekt og annen forelders inntekt for neste år, fordi du ikke har meldt inn inntekt for " + data.virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden per 1. mai " + data.virkningFom.year.minus(
                                    1
                                ).format() + "."
                            },
                            nynorsk {
                                +"Vi oppjusterer inntekta di og inntekta til den andre forelderen for neste år, fordi du ikkje har meldt inn inntekt for " + data.virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + data.virkningFom.year.minus(
                                    1
                                ).format() + "."
                            }
                        )
                    }
                }.orShowIf(barnetilleggFB.inntektAnnenForelder.greaterThan(0)) {
                    title1 {
                        text(
                            bokmal { +"Derfor oppjusterer vi inntekten til annen forelder" },
                            nynorsk { +"Derfor oppjusterer vi inntekta til den andre forelderen" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Vi oppjusterer annen forelders inntekt neste år, fordi du ikke har meldt inn inntekt for " + data.virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden per 1. mai " + data.virkningFom.year.minus(
                                    1
                                ).format() + "."
                            },
                            nynorsk {
                                +"Vi oppjusterer inntekta til den andre forelderen for neste år, fordi du ikkje har meldt inn inntekt for " + data.virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + data.virkningFom.year.minus(
                                    1
                                ).format() + "."
                            }
                        )
                    }
                }.orShow {
                    title1 {
                        text(
                            bokmal { +"Derfor oppjusterer vi inntekten din" },
                            nynorsk { +"Derfor oppjusterer vi inntekta di" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Vi oppjusterer din inntekt neste år, fordi du ikke har meldt inn inntekt for " + data.virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden per 1. mai " + data.virkningFom.year.minus(
                                    1
                                ).format() + "."
                            },
                            nynorsk {
                                +"Vi oppjusterer inntekta di for neste år, fordi du ikkje har meldt inn inntekt for " + data.virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + data.virkningFom.year.minus(
                                    1
                                ).format() + "."
                            }
                        )
                    }
                }
            }.orShow {
                title1 {
                    text(
                        bokmal { +"Derfor oppjusterer vi inntekten din" },
                        nynorsk { +"Derfor oppjusterer vi inntekta di" },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Vi oppjusterer din inntekt neste år, fordi du ikke har meldt inn inntekt for " + data.virkningFom.year.format() + ". Vi tar utgangspunkt i årets inntekt og justerer den ut fra endringer i grunnbeløpet (G) i folketrygden per 1. mai " + data.virkningFom.year.minus(
                                1
                            ).format() + "."
                        },
                        nynorsk {
                            +"Vi oppjusterer inntekta di for neste år, fordi du ikkje har meldt inn inntekt for " + data.virkningFom.year.format() + ". Vi tek utgangspunkt i årsinntekta og justerer ho ut frå endringar i grunnbeløpet (G) i folketrygda per 1. mai " + data.virkningFom.year.minus(
                                1
                            ).format() + "."
                        }
                    )
                }
            }

            showIf(not(okningUforegradVedArskjoring)) {
                paragraph {
                    text(
                        bokmal {
                            +"Fikk du innvilget uføretrygd etter januar " + data.virkningFom.year.minus(1)
                                .format() + ", er inntekten justert opp slik at den gjelder for hele " + data.virkningFom.year.format() + ". "
                        },
                        nynorsk {
                            +"Fekk du innvilga uføretrygd etter januar " + data.virkningFom.year.minus(1)
                                .format() + ", er inntekta justert opp slik at ho gjeld for heile " + data.virkningFom.year.format() + ". "
                        }
                    )
                }
            }
            showIf(okningUforegradVedArskjoring) {
                paragraph {
                    text(
                        bokmal {
                            +"Fordi du har fått økning av uføregraden i løpet av " + data.virkningFom.year.minus(1).format() + ", er det ekstra viktig at du kontrollerer at inntekten vi har brukt i beregningen av uføretrygden er korrekt. " +
                                    "Får du lavere inntekt i " + data.virkningFom.year.format() + ", må du gå inn i Inntektsplanleggeren og sende oss ny forventet inntekt, slik at du får riktig utbetaling av uføretrygd neste år."
                        },
                        nynorsk {
                            +"Fordi du har fått auka uføregraden i løpet av " + data.virkningFom.year.minus(1).format() + ", er det ekstra viktig at du kontrollerer at inntekta vi har brukt i berekninga av uføretrygda er korrekt. " +
                                    "Får du lågare inntekt i " + data.virkningFom.year.format() + ", må du gå inn i Inntektsplanleggaren og sende oss ny forventa inntekt, slik at du får rett utbetaling av uføretrygd neste år. "
                        }
                    )
                }
            }


            title1 {
                showIf(data.uforetrygd.fribelop1G or data.uforetrygd.fribelop04GTil1G) {
                    text(
                        bokmal { +"I " + data.virkningFom.year.format() + " kan du ha høyere inntekt før vi reduserer uføretrygden din" },
                        nynorsk { +"I " + data.virkningFom.year.format() + " kan du ha høgare inntekt før vi reduserer uføretrygda di" }
                    )
                }.orShow {
                    text(
                        bokmal { +"Bunnfradraget ditt endres i " + data.virkningFom.year.format() },
                        nynorsk { +"Botnfrådraget ditt endres i " + data.virkningFom.year.format() }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"I " + data.virkningFom.year.format() + " vil bunnfradraget ditt være " + data.uforetrygd.bunnfradrag.format() + ". Bunnfradraget er så mye inntekt du kan ha før vi begynner å redusere uføretrygden din. Det består av fribeløpet pluss inntekt etter uførhet. " },
                    nynorsk { +"I " + data.virkningFom.year.format() + " vil botnfrådraget ditt vere " + data.uforetrygd.bunnfradrag.format() + ". Botnfrådraget er så mykje inntekt du kan ha før vi byrjar å redusere uføretrygda di. Det består av fribeløpet pluss inntekt etter uførleik. " }
                )
            }

            paragraph {
                showIf(data.uforetrygd.erGradert) {
                    text(
                        bokmal { +"Siden du har gradert uføretrygd, har du fortsatt inntekt etter uførhet. " },
                        nynorsk { +"Sidan du har gradert uføretrygd, har du framleis inntekt etter uførleik. " }
                    )
                }.orShowIf(data.uforetrygd.inntektEtterUforhet.equalTo(0)) {
                    text(
                        bokmal { +"Siden du har 100 prosent uføretrygd, har du 0 kroner i inntekt etter uførhet. " },
                        nynorsk { +"Sidan du har 100 prosent uføretrygd, har du 0 kroner i inntekt etter uførleik. " }
                    )
                }.orShow {
                    text(
                        bokmal { +"Du har 100 prosent uføretrygd, og din inntekt etter uførhet er " + data.uforetrygd.inntektEtterUforhet.format() + ". " },
                        nynorsk { +"Du har 100 prosent uføretrygd, og inntekta di etter uførleik er " + data.uforetrygd.inntektEtterUforhet.format() + ". " }
                    )
                }
            }

            showIf(data.uforetrygd.fribelop1G or data.uforetrygd.fribelop04GTil1G) {
                paragraph {
                    text(
                        bokmal { +"Et høyere bunnfradrag kan gjøre det mer lønnsomt for deg å kombinere jobb og uføretrygd. " },
                        nynorsk { +"Eit høgare botnfrådrag kan gjere det meir lønsamt for deg å kombinere jobb og uføretrygd. " }
                    )
                }
            }

            showIf(data.uforetrygd.fribelop1G) {
                showIf(!data.uforetrygd.fribelop1GDesemberIfjor) {
                    paragraph {
                        showIf(data.uforetrygd.venteperiodeEtterGradsokning) {
                            text(
                                bokmal { +"I januar " + data.virkningFom.year.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til 1 G. " },
                                nynorsk { +"I januar " + data.virkningFom.year.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til 1 G. " }
                            )
                        }.orShow {
                            text(
                                bokmal { +"I januar " + data.virkningFom.year.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt til 1 G. " },
                                nynorsk { +"I januar " + data.virkningFom.year.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt til 1 G. " }
                            )
                        }
                    }
                }
            }.orShowIf(data.uforetrygd.fribelop1GTil04G.notNull() or data.uforetrygd.fribelop04GTil1GTil04G.notNull() or data.uforetrygd.fribelop04GTil1G) {
                title1 {
                    text(
                        bokmal { +"Periodisert fribeløp" },
                        nynorsk { +"Periodisert fribeløp" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Når fribeløpet endres i løpet av kalenderåret, beregnes det årlige bunnfradraget ut fra periodene du har med ulike fribeløp. Ditt fribeløp i " + data.virkningFom.year.format() + " blir " + data.uforetrygd.fribelop.format() + ". Se tabellen under. " },
                        nynorsk { +"Når fribeløpet blir endra i løpet av kalenderåret, blir det årlege botnfrådraget berekna ut frå periodane du har med ulike fribeløp. Fribeløpet ditt i " + data.virkningFom.year.format() + " blir " + data.uforetrygd.fribelop.format() + ". Sjå tabellen under. " }
                    )
                }
                title2 {
                    text(
                        bokmal { +"Slik beregner vi periodisert fribeløp" },
                        nynorsk { +"Slik berekner vi periodisert fribeløp" }
                    )
                }
                paragraph {
                    ifNotNull(data.uforetrygd.fribelop1GTil04G) { fribelop1GTil04G ->
                        text(
                            bokmal { +"Fram til " + fribelop1GTil04G.dato04G.format() + " er fribeløpet ditt 1 G. Da øker din uføregrad, og fribeløpet settes ned til 0,4 G. " },
                            nynorsk { +"Fram til " + fribelop1GTil04G.dato04G.format() + " er fribeløpet ditt 1 G. Då aukar uføregraden din, og fribeløpet blir sett ned til 0,4 G." }
                        )
                        showIf(not(data.sokerMottarApIlaAret or data.opphorDato.notNull())) {
                            text(
                                bokmal { +"Etter to år vil fribeløpet bli satt opp igjen til 1 G. " },
                                nynorsk { +"Etter to år vil fribeløpet bli sett opp igjen til 1 G. " }
                            )
                        }
                    }.orIfNotNull(data.uforetrygd.fribelop04GTil1GTil04G) { fribelop04GTil1GTil04G ->
                        showIf(data.uforetrygd.venteperiodeEtterGradsokning) {
                            text(
                                bokmal { +"Fram til " + data.uforetrygd.datoOkningFribelop.format() + " er fribeløpet ditt 0,4 G. " + data.uforetrygd.datoOkningFribelop.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til 1 G. " + fribelop04GTil1GTil04G.dato04G.format() + " øker din uføregrad, og fribeløpet settes ned igjen til 0,4 G. " },
                                nynorsk { +"Fram til " + data.uforetrygd.datoOkningFribelop.format() + " er fribeløpet ditt 0,4 G. " + data.uforetrygd.datoOkningFribelop.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til 1 G. " + fribelop04GTil1GTil04G.dato04G.format() + " aukar uføregraden din, og fribeløpet blir sett ned igjen til 0,4 G." }
                            )
                        }.orShow {
                            text(
                                bokmal { +"Fram til " + data.uforetrygd.datoOkningFribelop.format() + " er fribeløpet ditt 0,4 G. " + data.uforetrygd.datoOkningFribelop.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt til 1 G. " + fribelop04GTil1GTil04G.dato04G.format() + " øker din uføregrad, og fribeløpet settes ned igjen til 0,4 G." },
                                nynorsk { +"Fram til " + data.uforetrygd.datoOkningFribelop.format() + " er fribeløpet ditt 0,4 G. " + data.uforetrygd.datoOkningFribelop.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt til 1 G. " + fribelop04GTil1GTil04G.dato04G.format() + " aukar uføregraden din, og fribeløpet blir sett ned igjen til 0,4 G." }
                            )
                        }
                        showIf(not(data.sokerMottarApIlaAret or data.opphorDato.notNull())) {
                            text(
                                bokmal { +"Etter to år vil fribeløpet bli satt opp igjen til 1 G. " },
                                nynorsk { +"Etter to år vil fribeløpet bli sett opp igjen til 1 G. " }
                            )
                        }
                    }.orShow {//0,4G -> 1G
                        showIf(data.uforetrygd.venteperiodeEtterGradsokning) {
                            text(
                                bokmal { +"Fram til " + data.uforetrygd.datoOkningFribelop.format() + " er fribeløpet ditt 0,4 G. " + data.uforetrygd.datoOkningFribelop.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til 1 G. " },
                                nynorsk { +"Fram til " + data.uforetrygd.datoOkningFribelop.format() + " er fribeløpet ditt 0,4 G. " + data.uforetrygd.datoOkningFribelop.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til 1 G. " }
                            )
                        }.orShow {
                            text(
                                bokmal { +"Fram til " + data.uforetrygd.datoOkningFribelop.format() + " er fribeløpet ditt 0,4 G. " + data.uforetrygd.datoOkningFribelop.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt til 1 G. " },
                                nynorsk { +"Fram til " + data.uforetrygd.datoOkningFribelop.format() + " er fribeløpet ditt 0,4 G. " + data.uforetrygd.datoOkningFribelop.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt til 1 G. " }
                            )
                        }
                    }
                }

                includePhrase(Fribelopsperioder(data.uforetrygd.fribelopsperioder))

                showIf(data.uforetrygd.fribelop04GTil1G and not(data.sokerMottarApIlaAret or data.opphorDato.notNull())) {
                    paragraph {
                        text(
                            bokmal { +"Neste år: " },
                            nynorsk { +"Neste år: " },
                            FontType.BOLD,
                        )
                        text(
                            bokmal { +"Fra " + data.virkningFom.year.plus(1).format() + " vil du få nytt bunnfradrag med 1 G som fribeløp hele året. " },
                            nynorsk { +"Frå " + data.virkningFom.year.plus(1).format() + " vil du få nytt botnfrådrag med 1 G som fribeløp heile året. " }
                        )
                    }
                }

            }
            title1 {
                text(
                    bokmal { +"Inntekt lagt til grunn for beregning " },
                    nynorsk { +"Inntekt lagt til grunn for berekning " }
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Den årlige inntekten vi vil bruke for deg i beregning av uføretrygd er " + data.uforetrygd.inntektBruktIAvkortning.format() +
                                ". Det gir deg rett til en årlig utbetaling på " + data.totalNettoAr.format() + " i uføretrygd" + ifElse(harBarnetillegg, " og barnetillegg", "") + ". "
                    },
                    nynorsk {
                        +"Den årlege inntekta vi vil bruke for deg i berekninga av uføretrygd er " + data.uforetrygd.inntektBruktIAvkortning.format() +
                                ". Det gir deg rett til ei årleg utbetaling på " + data.totalNettoAr.format() + " i uføretrygd" + ifElse(harBarnetillegg, " og barnetillegg", "") + ". "
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Tjener du mer enn ditt bunnfradrag på " + data.uforetrygd.bunnfradrag.format() +
                                " per år, reduserer vi utbetalingen av uføretrygd. Det er bare den delen av inntekten din som er høyere enn bunnfradraget, som gir lavere utbetaling av uføretrygd. "
                    },
                    nynorsk {
                        +"Dersom du tener meir enn botnfrådraget ditt på " + data.uforetrygd.bunnfradrag.format() +
                                " per år, reduserer vi utbetalinga av uføretrygd. Det er berre den delen av inntekta di som er høgare enn botnfrådraget, som gir lågare utbetaling av uføretrygd. "
                    }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Tjener du mer enn " + data.uforetrygd.inntektstak.format() +
                                " per år, får du ikke utbetalt uføretrygd. Du beholder likevel retten til uføretrygd uten utbetaling, og du kan igjen få utbetaling av uføretrygd hvis inntekten din endrer seg i fremtiden. "
                    },
                    nynorsk {
                        +"Viss du tener meir enn " + data.uforetrygd.inntektstak.format() +
                                " per år, får du ikkje utbetalt uføretrygd. Du beheld likevel retten til uføretrygd utan utbetaling, og du kan igjen få utbetaling av uføretrygd dersom inntekta di seinare skulle endre seg. "
                    }
                )
            }
            ifNotNull(data.barnetilleggFellesbarn) { barnetilleggFellesbarn ->
                showIf(barnetilleggFellesbarn.inntektAnnenForelder.greaterThan(0)) {
                    paragraph {
                        text(
                            bokmal {
                                +"Den årlige inntekten vi vil bruke for annen forelder er " + barnetilleggFellesbarn.inntektAnnenForelder.format() + ". " +
                                        "Dette påvirker bare utbetalingen av barnetillegget. Inntil én ganger folketrygdens grunnbeløp er holdt utenfor den andre forelderens inntekt."
                            },
                            nynorsk {
                                +"Den årlege inntekta vi vil bruke for den andre forelderen er " + barnetilleggFellesbarn.inntektAnnenForelder.format() + ". " +
                                        "Dette påverkar berre utbetalinga av barnetillegget. Inntil éin gong grunnbeløpet i folketrygda blir halde utanfor inntekta til den andre forelderen."
                            }
                        )
                    }
                }
            }
            paragraph {
                ifNotNull(data.barnetilleggFellesbarn) {

                    text(
                        bokmal { +"Forventer du og/eller annen forelder en annen inntekt i " + data.virkningFom.year.format() + ", er det viktig at du melder inn ny forventet inntekt på $INNTEKTSPLANLEGGEREN_URL." },
                        nynorsk { +"Forventar du og/eller den andre forelderen ei anna inntekt i " + data.virkningFom.year.format() + ", er det viktig at du melder inn ny forventa inntekt på $INNTEKTSPLANLEGGEREN_URL." }
                    )
                }.orShow {
                    text(
                        bokmal { +"Forventer du en annen inntekt i " + data.virkningFom.year.format() + ", er det viktig at du melder inn ny forventet inntekt på $INNTEKTSPLANLEGGEREN_URL." },
                        nynorsk { +"Forventar du ei anna inntekt i " + data.virkningFom.year.format() + ", er det viktig at du melder inn ny forventa inntekt på $INNTEKTSPLANLEGGEREN_URL." }
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Hvis du gjør dette, får du en ny beregning og et nytt brev på $DIN_UFOERETRYGD_URL. " },
                    nynorsk { +"Viss du gjer dette, får du ei ny berekning og eit nytt brev på $DIN_UFOERETRYGD_URL. " }
                )
            }
            paragraph {
                text(
                    bokmal { +"På $UFOERE_JOBB_URL finner du mer informasjon, og en informasjonsfilm om hvordan du bruker Inntektsplanleggeren. Trenger du mer veiledning, kan du gjerne kontakte oss: $KONTAKT_URL" },
                    nynorsk { +"På $UFOERE_JOBB_URL finn du meir informasjon, og ein informasjonsfilm om korleis du bruker Inntektsplanleggaren. Treng du meir rettleiing, kan du gjerne kontakte oss: $KONTAKT_URL" }
                )
            }
            showIf(data.gjtEndret) {
                title1 {
                    text(
                        bokmal { +"Ditt gjenlevendetillegg" },
                        nynorsk { +"Attlevandetillegget ditt" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du får gjenlevendetillegg i uføretrygden din. Oppjusteringen av din inntekt påvirker utbetalingen av ditt gjenlevendetillegg. Gjenlevendetillegget endres med samme prosent som uføretrygden." },
                        nynorsk { +"Du får attlevandetillegg i uføretrygda di. Oppjusteringa av inntekta di påverkar utbetalinga av attlevandetillegg. Attlevandetillegget blir endra med same prosent som uføretrygda." }
                    )
                }
            }
            showIf(data.barnetilleggFellesbarn.notNull() or data.barnetilleggSaerkullsbarn.notNull()) {
                title1 {
                    text(
                        bokmal { +"Barnetillegg" },
                        nynorsk { +"Barnetillegg" }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Inntekten din har betydning for hvor mye du får utbetalt i barnetillegg. Uføretrygden din regnes med som inntekt. For fellesbarn bruker vi i tillegg den andre forelderens inntekt når vi beregner størrelsen på barnetillegget. " },
                        nynorsk { +"Inntekta di verkar inn på kor mykje du får utbetalt i barnetillegg. Uføretrygda di blir teken med som inntekt. For fellesbarn bruker vi i tillegg inntekta til den andre forelderen når vi reknar ut storleiken på barnetillegget. " }
                    )
                }

                showIf(
                    (data.btfbEndret and fellesbarnPeriodisert)
                            or (data.btsbEndret and sarkullsbarnPeriodisert)
                            or (harBarnetillegg and data.sokerMottarApIlaAret)
                ) {
                    paragraph {
                        text(
                            bokmal { +"Fordi du har barnetillegg som opphører i løpet av neste år, er inntektene og fribeløpet for neste år justert slik at de kun gjelder for perioden du mottar barnetillegg." },
                            nynorsk { +"Fordi du har barnetillegg som fell bort i løpet av neste år, er inntektene og fribeløpet for neste år justerte slik at dei berre gjeld for perioden du får barnetillegg." }
                        )
                    }
                }

                showIf(data.btfbEndret and data.btsbEndret) {
                    ifNotNull(data.barnetilleggSaerkullsbarn, data.barnetilleggFellesbarn) { barnetilleggSB, barnetilleggFB ->
                        title2 {
                            text(
                                bokmal { +"Dine barnetillegg" },
                                nynorsk { +"Barnetillegga dine" }
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bor sammen med barnets andre forelder. Du får barnetillegg for særkullsbarn når du ikke bor sammen med barnets andre forelder." },
                                nynorsk { +"Du får barnetillegg for særkullsbarn og fellesbarn. Du får barnetillegg for fellesbarn når du bur saman med den andre forelderen til barnet. Du får barnetillegg for særkullsbarn når du ikkje bur saman med den andre forelderen til barnet." }
                            )
                        }
                        paragraph {
                            text(
                                bokmal {
                                    +"Barnetillegg for særkullsbarn er beregnet ut fra din inntekt på " + barnetilleggSB.inntektBruktIAvkortning.format() + ". Barnetillegg for " +
                                            "fellesbarn er i tillegg beregnet ut fra den andre forelderens inntekt på " + barnetilleggFB.inntektAnnenForelder.format() +
                                            ". Du får derfor en utbetaling av barnetillegg på " + barnetilleggSB.netto.plus(barnetilleggFB.netto).format() + " per måned fra neste år."
                                },
                                nynorsk {
                                    +"Barnetillegg for særkullsbarn er rekna ut med utgangspunkt i inntekta di på " + barnetilleggSB.inntektBruktIAvkortning.format() + ". Barnetillegget for " +
                                            "fellesbarn er i tillegg rekna ut frå inntekta til den andre forelderen på " + barnetilleggFB.inntektAnnenForelder.format() +
                                            ". Du får derfor ei utbetaling av barnetillegg på " + barnetilleggSB.netto.plus(barnetilleggFB.netto).format() + " per månad frå neste år."
                                }
                            )
                        }
                    }
                }.orShow {
                    showIf(data.btfbEndret) {
                        ifNotNull(data.barnetilleggFellesbarn) { barnetilleggFB ->
                            title2 {
                                text(
                                    bokmal { +"Barnetillegg for fellesbarn" },
                                    nynorsk { +"Barnetillegg for fellesbarn" }
                                )
                            }
                            paragraph {
                                text(
                                    bokmal {
                                        +"Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. Vi har endret barnetillegget ut fra din personinntekt på " +
                                                barnetilleggFB.inntektBruker.format() + " og personinntekten til barnets andre forelder på " + barnetilleggFB.inntektAnnenForelder.format() +
                                                ". Du får derfor en utbetaling av barnetillegg på " + barnetilleggFB.netto.format() + " per måned fra neste år."
                                    },
                                    nynorsk {
                                        +"Du får barnetillegg for fellesbarn fordi du bur saman med den andre forelderen til barnet. Vi har endra barnetillegget ut frå personinntekta di på " +
                                                barnetilleggFB.inntektBruker.format() + " og personinntekta til den andre forelderen på " + barnetilleggFB.inntektAnnenForelder.format() +
                                                ". Du får derfor ei utbetaling av barnetillegg på " + barnetilleggFB.netto.format() + " per månad frå neste år."
                                    }
                                )
                            }
                        }
                    }
                    showIf(data.btsbEndret) {
                        ifNotNull(data.barnetilleggSaerkullsbarn) { barnetilleggSB ->
                            title2 {
                                text(
                                    bokmal { +"Barnetillegg for særkullsbarn" },
                                    nynorsk { +"Barnetillegg for særkullsbarn" }
                                )
                            }
                            paragraph {
                                text(
                                    bokmal {
                                        +"Du får barnetillegg for særkullsbarn fordi du ikke bor sammen med barnets andre forelder. " +
                                                "Vi har endret barnetillegget ut fra din personinntekt på " + barnetilleggSB.inntektBruktIAvkortning.format() +
                                                ". Du får derfor en utbetaling av barnetillegg på " + barnetilleggSB.netto.format() + " per måned fra neste år."
                                    },
                                    nynorsk {
                                        +"Du får barnetillegg for særkullsbarn fordi du ikkje bur saman med den andre forelderen til barnet. " +
                                                "Vi har endra barnetillegget ut frå personinntekta di på " + barnetilleggSB.inntektBruktIAvkortning.format() +
                                                ". Du får derfor ei utbetaling av barnetillegg på " + barnetilleggSB.netto.format() + " per månad frå neste år."
                                    }
                                )
                            }
                        }
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Du finner fullstendige beregninger i vedlegget «Opplysninger om beregningen»." },
                    nynorsk { +"Du finn fullstendige berekningar i vedlegget «Opplysningar om utrekninga»." }
                )
            }
            paragraph {
                showIf(
                    data.uforetrygd.endringsbelop.notEqualTo(0)
                            and (data.btfbEndret or data.btsbEndret)
                            and data.gjtEndret //UT, BT og GJT
                ) {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12." }
                    )
                }.orShowIf(
                    data.uforetrygd.endringsbelop.notEqualTo(0)
                            and (data.btfbEndret or data.btsbEndret) //UT og BT
                ) {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12." }
                    )
                }.orShowIf(
                    data.uforetrygd.endringsbelop.notEqualTo(0)
                            and data.gjtEndret //UT og GJT
                ) {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-14, 12-18 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12." }
                    )
                }.orShowIf(
                    data.uforetrygd.endringsbelop.notEqualTo(0) //UT
                ) {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12." }
                    )
                }.orShow { //BT
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 12-15 til 12-16 og 22-12." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 12-15 til 12-16 og 22-12." }
                    )
                }

            }
            title1 {
                text(
                    bokmal { +"Du må melde fra om endringer" },
                    nynorsk { +"Du må melde frå om endringar" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Endringer i inntekt og din situasjon kan påvirke hvor mye du får utbetalt fra oss. Derfor er det viktig at du sier ifra så fort det skjer en endring, slik at vi kan beregne riktig utbetaling. " },
                    nynorsk { +"Endringar i inntekt og situasjonen din kan påverke kor mykje du får utbetalt frå oss. Derfor er det viktig at du seier frå så snart det skjer ei endring, slik at vi kan rekne ut rett utbetaling. " }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan melde inn forventet inntekt i Inntektsplanleggeren på $INNTEKTSPLANLEGGEREN_URL" },
                    nynorsk { +"Du kan melde inn forventa inntekt i Inntektsplanleggaren på $INNTEKTSPLANLEGGEREN_URL" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Alle andre endringer kan du melde inn på $MELDE_URL" },
                    nynorsk { +"Alle andre endringar kan meldast inn på $MELDE_URL" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Les mer om dette i vedlegget «Dine rettigheter og plikter»." },
                    nynorsk { +"Les meir om dette i vedlegget «Dine rettar og plikter»." }
                )
            }
            title1 {
                text(
                    bokmal { +"Etteroppgjør" },
                    nynorsk { +"Etteroppgjer" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvert år sjekker vi inntektsopplysningene i skatteoppgjøret ditt for å se om du har fått utbetalt riktig beløp fra oss året før." },
                    nynorsk { +"Kvart år sjekkar vi inntektsopplysningane i skatteoppgjeret ditt for å sjå om du har fått utbetalt rett beløp frå oss året før. " }
                )
            }
            paragraph {
                text(
                    bokmal { +"Viser skatteoppgjøret at du har hatt en annen inntekt enn den inntekten vi brukte da vi beregnet utbetalingene dine, gjør vi en ny beregning. Dette kalles etteroppgjør." },
                    nynorsk { +"Viser skatteoppgjeret at du har hatt ei anna inntekt enn den inntekta vi brukte då vi rekna ut utbetalingane dine, vil vi gjere ei ny berekning. Dette blir kalla etteroppgjer." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du har fått for lite utbetalt, får du en etterbetaling fra oss. Har du fått for mye utbetalt, må du betale tilbake." },
                    nynorsk { +"Dersom du har fått for lite utbetalt, får du ei etterbetaling frå oss. Har du fått for mykje utbetalt, må du betale tilbake." }
                )
            }
            title1 {
                text(
                    bokmal { +"Inntekter som ikke skal gi lavere utbetaling av uføretrygden" },
                    nynorsk { +"Inntekter som ikkje skal gi lågare utbetaling av uføretrygda" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Det gjelder hvis du har fått utbetalt erstatning for inntektstap ved:" },
                    nynorsk { +"Det gjeld dersom du har fått utbetalt erstatning for inntektstap ved:" }
                )
                list {
                    item {
                        text(
                            bokmal { +"Skade " },
                            nynorsk { +"Skade " }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Yrkesskade " },
                            nynorsk { +"Yrkesskade " }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Pasientskade " },
                            nynorsk { +"Pasientskade " }
                        )
                    }
                }
                text(
                    bokmal { +"Skadeerstatningsloven § 3-1, yrkesskadeforsikringsloven § 13, pasientskadeloven § 4 første ledd." },
                    nynorsk { +"Skadeerstatningslova § 3-1, yrkesskadeforsikringslova § 13, pasientskadelova § 4 første ledd." },
                    FontType.ITALIC
                )
            }
            paragraph {
                text(
                    bokmal { +"Dette kan gjelde inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel: " },
                    nynorsk { +"Dette kan gjelde inntekt frå arbeid eller verksemd som vart heilt avslutta før du fekk innvilga uføretrygd, til dømes: " },
                )
                list {
                    item {
                        text(
                            bokmal { +"Utbetalte feriepenger. Opptjeningen må ha skjedd før du fikk innvilget uføretrygd. " },
                            nynorsk { +"Utbetalte feriepengar. Oppteninga må ha skjedd før du fekk innvilga uføretrygd. " }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten " },
                            nynorsk { +"Inntekter frå sal av produksjonsmiddel i samband med opphøyr av verksemda " }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Produksjonstillegg og andre overføringer til gårdbrukere " },
                            nynorsk { +"Produksjonstillegg og andre overføringar til gardsbrukarar " }
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Dette kan også gjelde store etterbetalinger og pengestøtte fra Nav, hvis pengestøtten er pensjonsgivende og etterbetalingen har skjedd i 2024 eller senere." },
                    nynorsk { +"Dette kan også gjelde store etterbetalingar og pengestøtte frå Nav, dersom pengestøtta er pensjonsgjevande og etterbetalinga har skjedd i 2024 eller seinare." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du sender oss dokumentasjon som viser at du har en slik inntekt, kan vi gjøre en ny beregning av uføretrygden din." },
                    nynorsk { +"Dersom du sender oss dokumentasjon som viser at du har slik inntekt, kan vi gjere ei ny berekning av uføretrygda di." },
                )
            }
            ifNotNull(data.barnetilleggFellesbarn) {
                paragraph {
                    text(
                        bokmal { +"Hva kan holdes utenfor personinntekten til den andre forelderen? " },
                        nynorsk { +"Kva kan haldast utanfor personinntekta til den andre forelderen? " },
                    )

                    list {
                        item {
                            text(
                                bokmal { +"Erstatningsoppgjør for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon fra Nav" },
                                nynorsk { +"Erstatningsoppgjer for inntektstap dersom den andre forelderen får uføretrygd eller alderspensjon frå Nav" }
                            )
                        }
                    }
                    text(
                        bokmal { +"Dersom vi mottar dokumentasjon fra deg som bekrefter slik inntekt, kan vi gjøre en ny beregning. " },
                        nynorsk { +"Dersom vi får dokumentasjon frå deg som stadfester slik inntekt, kan vi gjere ei ny berekning. " },
                    )

                }
            }
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))


            title1 {
                text(
                    bokmal { +"Sjekk utbetalingene dine" },
                    nynorsk { +"Sjekk utbetalingane dine" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Du kan se alle utbetalingene du har mottatt på $MINSIDE_URL. Her kan du også endre kontonummeret ditt." },
                    nynorsk { +"Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. Du kan sjå alle utbetalingane du har motteke på $MINSIDE_URL. Her kan du også endre kontonummeret ditt." }
                )
            }
            title1 {
                text(
                    bokmal { +"Skattekort" },
                    nynorsk { +"Skattekort" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til oss, siden vi får skatteopplysningene dine elektronisk fra Skatteetaten. Du bør likevel sjekke at skattekortet ditt er riktig. Skattekortet kan du endre på skatteetaten.no. Du kan også få hjelp av Skatteetaten hvis du har spørsmål om skatt. " },
                    nynorsk { +"Uføretrygd blir skattlagt som lønsinntekt. Du treng ikkje levere skattekortet ditt til oss, då vi får skatteopplysningane dine elektronisk frå Skatteetaten. Du bør likevel sjekke at skattekortet ditt stemmer. Ved behov kan du endre skattekortet på skatteetaten.no. Du kan også få hjelp frå Skatteetaten dersom du har spørsmål om skatt. " }
                )
            }
            showIf(not(data.brukerBorINorge)) {
                title1 {
                    text(
                        bokmal { +"Skatt for deg som bor i utlandet " },
                        nynorsk { +"Skatt for deg som bur i utlandet " }
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på skatteetaten.no. Hvis du er bosatt i utlandet og betaler skatt i et annet land enn Norge, kan du kontakte skattemyndighetene der du bor." },
                        nynorsk { +"Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på skatteetaten.no. Dersom du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur." }
                    )
                }
            }
            includePhrase(Felles.HarDuSpoersmaal(Constants.UFOERETRYGD_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }
    }
}
