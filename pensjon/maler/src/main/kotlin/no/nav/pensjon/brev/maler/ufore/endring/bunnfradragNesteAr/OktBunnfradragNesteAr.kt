package no.nav.pensjon.brev.maler.ufore.endring.bunnfradragNesteAr

import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.OktBunnfradragNesteArDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.periodisertBarnetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.bunnfradrag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.datoForNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.fribelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.fribelopUtvikling
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.fribelopsperioder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.gradertUforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.grunnbelop
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.hjemler
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.inntektEtterUforhet
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.nettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.nettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.nettoUforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.endringBarnetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.endringGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.endringUforetrygd
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.opphorsdato
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.sokerMottarApIlaAret
import no.nav.pensjon.brev.api.model.maler.ufoerApi.bunnfradragNesteAr.selectors.oktBunnfradragNesteArDto.vedtakVirkFom
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_04G_TIL_1G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_04G_TIL_1G_TIL_04G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_1G_FRA_JAN
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.FribelopUtviklingKategori.FRIBELOP_1G_TIL_04G
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopUtvikling.fribelopsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopUtvikling.gradsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopUtvikling.kategori
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsokning._2arEtterGradsokning
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.fribelopsokning.dato
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.gradsokning.dato
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.maler.fraser.common.Constants.UFOERETRYGD_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.HjemmelFormatter
import no.nav.pensjon.brev.maler.ufore.fraser.Fribelopsperioder
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.maler.ufore.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.year
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

object OktBunnfradragNesteAr {

    data class Outline(val data: Expression<OktBunnfradragNesteArDto>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            with(data) {
                val harUforetrygdEllerTilleggUtbetalt = nettoUforetrygd
                    .plus(nettoBarnetillegg.ifNull(Kroner(0)))
                    .plus(nettoGjenlevendetillegg.ifNull(Kroner(0)))
                    .greaterThan(0)

                val endringUforetrygdOgTillegg = endringUforetrygd.plus(endringBarnetillegg).plus(endringGjenlevendetillegg)

                paragraph {
                    text(
                        bokmal { +"I " + vedtakVirkFom.year.format() + " vil bunnfradraget ditt være " + bunnfradrag.format() + ". Dette har kun betydning for deg som skal ha inntekt ved siden av uføretrygden." },
                        nynorsk { +"I " + vedtakVirkFom.year.format() + " vil botnfrådraget ditt vere " + bunnfradrag.format() + ". Dette har berre betydning for deg som skal ha inntekt ved sida av uføretrygda." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Bunnfradraget er så mye inntekt du kan ha før vi begynner å redusere uføretrygden din. Det består av fribeløpet pluss eventuell inntekt etter uførhet." },
                        nynorsk { +"Botnfrådraget er så mykje inntekt du kan ha før vi byrjar å redusere uføretrygda di. Det består av fribeløpet pluss eventuell inntekt etter uførleik." },
                    )
                }
                showIf(gradertUforetrygd) {
                    paragraph {
                        text(
                            bokmal { +"Siden du har gradert uføretrygd har du fortsatt inntekt etter uførhet." },
                            nynorsk { +"Sidan du har gradert uføretrygd har du framleis inntekt etter uførleik." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +ifElse(inntektEtterUforhet.equalTo(0), "Siden du har 100 prosent uføretrygd har du 0 kroner i inntekt etter uførhet. ".expr(), "Du har 100 prosent uføretrygd, og din inntekt etter uførhet er ".expr() + inntektEtterUforhet.format() + ".") },
                            nynorsk { +ifElse(inntektEtterUforhet.equalTo(0), "Sidan du har 100 prosent uføretrygd har du 0 kroner i inntekt etter uførleik. ".expr(), "Du har 100 prosent uføretrygd, og di inntekt etter uførleik er ".expr() + inntektEtterUforhet.format() + ".") },
                        )
                    }
                }
                showIf(fribelopUtvikling.kategori.isOneOf(FRIBELOP_1G_FRA_JAN, FRIBELOP_04G_TIL_1G)) {
                    paragraph {
                        text(
                            bokmal { +"Et høyere bunnfradrag kan gjøre det mer lønnsomt for deg å kombinere jobb og uføretrygd. " },
                            nynorsk { +"Eit høgare botnfrådrag kan gjere det meir lønsamt for deg å kombinere jobb og uføretrygd. " },
                        )
                    }
                }
                showIf(sokerMottarApIlaAret) {
                    paragraph {
                        text(
                            bokmal { +"Fordi du får alderspensjon fra " + datoForNormertPensjonsalder.format() + ", er inntekten og bunnfradraget justert ut fra antall måneder du får uføretrygd." },
                            nynorsk { +"Fordi du får alderspensjon frå " + datoForNormertPensjonsalder.format() + ", er inntekta og botnfrådraget justert ut frå talet på månader du får uføretrygd." }
                        )
                    }
                }.orIfNotNull(opphorsdato) { dato ->
                    paragraph {
                        text(
                            bokmal { +"Fordi uføretrygden din opphører fra " + dato.format() + ", er inntekten og bunnfradraget justert ut fra antall måneder du får uføretrygd. " },
                            nynorsk { +"Fordi uføretrygda di opphøyrer frå " + dato.format() + ", er inntekta og botnfrådraget justert ut frå talet på månader du får uføretrygd." }
                        )
                    }
                }.orShowIf(periodisertBarnetillegg) {
                    paragraph {
                        text(
                            bokmal { +"Fordi du har barnetillegg som opphører i løpet av neste år, er inntekten og bunnfradraget for neste år justert slik at de kun gjelder for perioden du mottar barnetillegg." },
                            nynorsk { +"Fordi du har barnetillegg som fell bort i løpet av neste år, er inntekten og bunnfradraget for neste år justerte slik at dei berre gjeld for perioden du får barnetillegg." }
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Din beregning for " + vedtakVirkFom.year.format() },
                        nynorsk { +"Di berekning for " + vedtakVirkFom.year.format() },
                        FontType.BOLD,
                    )
                }
                paragraph {
                    table(header = {
                        column { text(bokmal { +"Beregning" }, nynorsk { +"Berekning" }) }
                        column(alignment = RIGHT) { text(bokmal { +"Beløp" }, nynorsk { +"Beløp" }) }
                    }) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Uføretrygd" },
                                    nynorsk { +"Uføretrygd" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +nettoUforetrygd.format() },
                                    nynorsk { +nettoUforetrygd.format() },
                                )
                            }
                        }
                        ifNotNull(nettoBarnetillegg) { nettoBt ->
                            row {
                                cell {
                                    text(
                                        bokmal { +"Barnetillegg" },
                                        nynorsk { +"Barnetillegg" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +nettoBt.format() },
                                        nynorsk { +nettoBt.format() },
                                    )
                                }
                            }
                        }
                        ifNotNull(nettoGjenlevendetillegg) { nettoGjt ->
                            row {
                                cell {
                                    text(
                                        bokmal { +"Gjenlevendetillegg" },
                                        nynorsk { +"Gjenlevendetillegg" },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +nettoGjt.format() },
                                        nynorsk { +nettoGjt.format() },
                                    )
                                }
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
                                    bokmal { +bunnfradrag.format() },
                                    nynorsk { +bunnfradrag.format() },
                                )
                            }
                        }
                        row {
                            cell {
                                text(
                                    bokmal { +"Fribeløp" },
                                    nynorsk { +"Fribeløp" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +fribelop.format() },
                                    nynorsk { +fribelop.format() },
                                )
                            }
                        }
                        showIf(endringUforetrygdOgTillegg.notEqualTo(0)) {
                            row {
                                cell {
                                    showIf(endringUforetrygdOgTillegg.greaterThanOrEqual(0)) {
                                        text(
                                            bokmal { +"Månedlig økning " },
                                            nynorsk { +"Månadleg auke " },
                                        )
                                    }.orShow {
                                        text(
                                            bokmal { +"Månedlig reduksjon " },
                                            nynorsk { +"Månadleg reduksjon " },
                                        )
                                    }
                                    text(
                                        bokmal { +"i uføretrygd" + ifElse(nettoBarnetillegg.isNull(), "", " og barnetillegg") },
                                        nynorsk { +"i uføretrygd" + ifElse(nettoBarnetillegg.isNull(), "", " og barnetillegg") },
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +endringUforetrygdOgTillegg.format() },
                                        nynorsk { +endringUforetrygdOgTillegg.format() },
                                    )
                                }
                            }
                        }
                    }
                }
                showIf(harUforetrygdEllerTilleggUtbetalt) {
                    paragraph {
                        text(
                            bokmal { +"Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                            nynorsk { +"Uføretrygda blir framleis utbetalt seinast den 20. kvar månad." },
                        )
                    }
                }
                includePhrase(Ufoeretrygd.MeldeFraOmEndringer(vedleggDineRettigheterOgPlikterUfoere))
                title1 {
                    text(
                        bokmal { +"Slik beregner vi fribeløp og bunnfradrag" },
                        nynorsk { +"Slik bereknar vi fribeløp og botnfrådrag" },
                    )
                }
                showIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_1G_FRA_JAN)) {
                    paragraph {
                        ifNotNull(fribelopUtvikling.fribelopsokning) { fribelopsokning ->
                            showIf(fribelopsokning._2arEtterGradsokning) {
                                text(
                                    bokmal { +"I januar " + vedtakVirkFom.year.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til en ganger folketrygdens grunnbeløp (G). " },
                                    nynorsk { +"I januar " + vedtakVirkFom.year.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet i folketrygda (G). " }
                                )
                            }.orShow {
                                text(
                                    bokmal { +"I januar " + vedtakVirkFom.year.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt til en ganger folketrygdens grunnbeløp (G). " },
                                    nynorsk { +"I januar " + vedtakVirkFom.year.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet i folketrygda (G). " }
                                )
                            }
                        }
                    }
                    includePhrase(GrunnbelopFrase(grunnbelop))
                }.orShowIf(fribelopUtvikling.kategori.isOneOf(FRIBELOP_1G_TIL_04G, FRIBELOP_04G_TIL_1G_TIL_04G, FRIBELOP_04G_TIL_1G)) {
                    paragraph {
                        text(
                            bokmal { +"Når fribeløpet endres i løpet av kalenderåret, beregnes det årlige bunnfradraget ut fra periodene du har med ulike fribeløp. Ditt fribeløp i " + vedtakVirkFom.year.format() + " blir " + fribelop.format() + ". Se tabellen under. " },
                            nynorsk { +"Når fribeløpet blir endra i løpet av kalenderåret, blir det årlege botnfrådraget berekna ut frå periodane du har med ulike fribeløp. Fribeløpet ditt i " + vedtakVirkFom.year.format() + " blir " + fribelop.format() + ". Sjå tabellen under. " }
                        )
                    }
                    title2 {
                        text(
                            bokmal { +"Slik beregner vi periodisert fribeløp:" },
                            nynorsk { +"Slik berekner vi periodisert fribeløp:" }
                        )
                    }
                    paragraph {
                        showIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_1G_TIL_04G)) {
                            ifNotNull(fribelopUtvikling.gradsokning) { gradsokning ->
                                text(
                                    bokmal { +"Fram til " + gradsokning.dato.format() + " er fribeløpet ditt en ganger folketrygdens grunnbeløp (G). Da øker din uføregrad, og fribeløpet settes ned til 40 prosent av grunnbeløpet. " },
                                    nynorsk { +"Fram til " + gradsokning.dato.format() + " er fribeløpet ditt ein gong grunnbeløpet i folketrygda (G). Då aukar uføregraden din, og fribeløpet blir sett ned til 40 prosent av grunnbeløpet. " }
                                )
                                showIf(not(sokerMottarApIlaAret or opphorsdato.notNull())) {
                                    text(
                                        bokmal { +"Etter to år vil fribeløpet bli satt opp igjen til en ganger grunnbeløpet. " },
                                        nynorsk { +"Etter to år vil fribeløpet bli sett opp igjen til en gong grunnbeløpet. " }
                                    )
                                }
                            }
                        }.orShowIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_04G_TIL_1G_TIL_04G)) {
                            ifNotNull(fribelopUtvikling.gradsokning, fribelopUtvikling.fribelopsokning) { gradsokning, fribelopsokning ->
                                showIf(fribelopsokning._2arEtterGradsokning) {
                                    text(
                                        bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av folketrygdens grunnbeløp (G). " + fribelopsokning.dato.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til en ganger grunnbeløpet. " + gradsokning.dato.format() + " øker din uføregrad, og fribeløpet settes ned igjen til 40 prosent av grunnbeløpet. " },
                                        nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av grunnbeløpet i folketrygda (G). " + fribelopsokning.dato.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet. " + gradsokning.dato.format() + " aukar uføregraden din, og fribeløpet blir sett ned igjen til 40 prosent av grunnbeløpet. " }
                                    )
                                }.orShow {
                                    text(
                                        bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av folketrygdens grunnbeløp (G). " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor øker fribeløpet ditt til en ganger grunnbeløpet. " + gradsokning.dato.format() + " øker din uføregrad, og fribeløpet settes ned igjen til 40 prosent av grunnbeløpet. " },
                                        nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av grunnbeløpet i folketrygda (G). " + fribelopsokning.dato.format() + " har du hatt uføretrygd i to år. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet. " + gradsokning.dato.format() + " aukar uføregraden din, og fribeløpet blir sett ned igjen til 40 prosent av grunnbeløpet. " }
                                    )
                                }
                                showIf(not(sokerMottarApIlaAret or opphorsdato.notNull())) {
                                    text(
                                        bokmal { +"Etter to år vil fribeløpet bli satt opp igjen til en ganger grunnbeløpet. " },
                                        nynorsk { +"Etter to år vil fribeløpet bli sett opp igjen til ein gong grunnbeløpet. " }
                                    )
                                }
                            }
                        }.orShowIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_04G_TIL_1G)) {
                            ifNotNull(fribelopUtvikling.fribelopsokning) { fribelopsokning ->
                                showIf(fribelopsokning._2arEtterGradsokning) {
                                    text(
                                        bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av folketrygdens grunnbeløp (G). " + fribelopsokning.dato.format() + " er det to år siden din uføregrad økte. Derfor øker fribeløpet ditt til en ganger grunnbeløpet. " },
                                        nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av grunnbeløpet i folketrygda (G). " + fribelopsokning.dato.format() + " er det to år sidan uføregraden din auka. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet. " }
                                    )
                                }.orShow {
                                    text(
                                        bokmal { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av folketrygdens grunnbeløp (G). " + fribelopsokning.dato.format() + " har du hatt uføretrygd i 2 år. Derfor øker fribeløpet ditt til en ganger grunnbeløpet. " },
                                        nynorsk { +"Fram til " + fribelopsokning.dato.format() + " er fribeløpet ditt 40 prosent av grunnbeløpet i folketrygda (G). " + fribelopsokning.dato.format() + " har du hatt uføretrygd i 2 år. Derfor aukar fribeløpet ditt til ein gong grunnbeløpet. " }
                                    )
                                }
                            }
                        }
                    }

                    includePhrase(GrunnbelopFrase(grunnbelop))
                    includePhrase(Fribelopsperioder(fribelopsperioder))

                    showIf(fribelopUtvikling.kategori.equalTo(FRIBELOP_04G_TIL_1G) and not(sokerMottarApIlaAret or opphorsdato.notNull())) {
                        paragraph {
                            text(
                                bokmal { +"Neste år: " },
                                nynorsk { +"Neste år: " },
                                FontType.BOLD,
                            )
                            text(
                                bokmal { +"Fra " + vedtakVirkFom.year.plus(1).format() + " vil du få nytt bunnfradrag med 1 G som fribeløp hele året. " },
                                nynorsk { +"Frå " + vedtakVirkFom.year.plus(1).format() + " vil du få nytt botnfrådrag med 1 G som fribeløp heile året. " }
                            )
                        }
                    }
                }

                showIf(endringBarnetillegg.lessThan(0)) {
                    showIf(endringUforetrygd.greaterThan(0)) {
                        title1 {
                            text(
                                bokmal { +"Endring i barnetillegg" },
                                nynorsk { +"Endring i barnetillegg" },
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Fribeløpsendringen fører til at du får en " },
                                nynorsk { +"Fribeløpsendringa fører til at du får ei " },
                            )
                            text(
                                bokmal { +"høyere" },
                                nynorsk { +"høgare" },
                                FontType.BOLD,
                            )
                            text(
                                bokmal { +" utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. Derfor får du en lavere utbetaling av barnetillegg. Barnetillegg (før skatt) er derfor nå " + nettoBarnetillegg.ifNull(Kroner(0)).format() + "." },
                                nynorsk { +" utbetaling av uføretrygd. Uføretrygda blir rekna med som inntekt når vi bereknar barnetillegg. Difor får du ei lågare utbetaling av barnetillegg. Barnetillegg (før skatt) er difor no " + nettoBarnetillegg.ifNull(Kroner(0)).format() + "." },
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Hvert år i januar justerer vi barnetillegget ditt ut fra uføretrygden din for det nye kalenderåret." },
                                nynorsk { +"Kvart år i januar justerer vi barnetillegget ditt ut frå uføretrygda di for det nye kalenderåret." },
                            )
                        }
                    }.orShowIf(endringUforetrygd.equalTo(0)) {
                        title1 {
                            text(
                                bokmal { +"Endring i barnetillegg" },
                                nynorsk { +"Endring i barnetillegg" },
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Fribeløpsendringen fører til at barnetillegg for fellesbarn endres. Begge foreldres inntekt og uføretrygd regnes med når vi beregner barnetillegg for felles barn. Derfor får du en lavere utbetaling av barnetillegg." },
                                nynorsk { +"Fribeløpsendringa fører til at barnetillegg for fellesbarn blir endra. Begge foreldra si inntekt og uføretrygd blir rekna med når vi bereknar barnetillegg for felles barn. Difor får du ei lågare utbetaling av barnetillegg." },
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Barnetillegg (før skatt) er derfor nå " + nettoBarnetillegg.ifNull(Kroner(0)).format() + "." },
                                nynorsk { +"Barnetillegg (før skatt) er difor no " + nettoBarnetillegg.ifNull(Kroner(0)).format() + "." },
                            )
                        }
                    }
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter folketrygdloven " + hjemler.format(HjemmelFormatter(true)) + "." },
                        nynorsk { +"Vedtaket er gjort etter folketrygdlova " + hjemler.format(HjemmelFormatter(true)) + "." },
                    )
                }
                includePhrase(Ufoeretrygd.RettTilAKlage)
                includePhrase(Ufoeretrygd.RettTilInnsyn)
                includePhrase(Felles.HarDuSpoersmaal(UFOERETRYGD_URL, NAV_KONTAKTSENTER_TELEFON))
            }
        }
    }
}

data class GrunnbelopFrase(val grunnbelop: Expression<Kroner>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            text(
                bokmal { +"Per nå er folketrygdens grunnbeløp " + grunnbelop.format() + ". Grunnbeløpet justeres i mai hvert år." },
                nynorsk { +"Per no er grunnbeløpet i folketrygda " + grunnbelop.format() + ". Grunnbeløpet blir justert i mai kvart år." }
            )
        }
}