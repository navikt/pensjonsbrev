package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.ReverseringLavereMinstesatsDto
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.lopendeYtelse.avkortetPgaRedusertTrygdetid
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.lopendeYtelse.brukersMinstesats
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.lopendeYtelse.harGradertUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.lopendeYtelse.nettoBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.lopendeYtelse.nettoGjenlevendetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.lopendeYtelse.nettoTotal
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.lopendeYtelse.nettoUforetrygd
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.lopendeYtelse.reduksjonsprosent
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.opphortYtelse.opphorsdato
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.etterbetaling
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.hjemmeltekst
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.lopendeYtelse
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.reverseringLavereMinstesatsDto.opphortYtelse
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfore
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference

object ReverseringLavereMinstesats {

    data class Outline(val data: Expression<ReverseringLavereMinstesatsDto>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Vi viser til varselbrev av 2. juli 2026 om reversering av reduksjon i minstesats, og tidligere vedtak om reduksjon i minstesats den 25. mai 2026." },
                    nynorsk { +"Vi viser til varselbrev av 2. juli 2026 om reversering av reduksjon i minstesats, og tidlegare vedtak om reduksjon i minstesats den 25. mai 2026." },
                )
            }

            ifNotNull(data.lopendeYtelse) { lopendeYtelse ->
                title1 {
                    text(
                        bokmal { +"Dette er din uføretrygd fra 1. oktober 2026" },
                        nynorsk { +"Dette er uføretrygda di frå 1. oktober 2026" },
                    )
                }
                paragraph {
                    table(header = {
                        column { text(bokmal { +"Din uføretrygd" }, nynorsk { +"Uføretrygda di" }) }
                        column(alignment = RIGHT) {}
                    }) {
                        row {
                            cell { text(bokmal { +"Uføretrygd" }, nynorsk { +"Uføretrygd" }) }
                            cell { text(bokmal { +lopendeYtelse.nettoUforetrygd.format() }, nynorsk { +lopendeYtelse.nettoUforetrygd.format() }) }
                        }
                        ifNotNull(lopendeYtelse.nettoBarnetillegg) { bt ->
                            row {
                                cell { text(bokmal { +"Barnetillegg" }, nynorsk { +"Barnetillegg" }) }
                                cell { text(bokmal { +bt.format() }, nynorsk { +bt.format() }) }
                            }
                        }
                        ifNotNull(lopendeYtelse.nettoGjenlevendetillegg) { gjt ->
                            row {
                                cell { text(bokmal { +"Gjenlevendetillegg" }, nynorsk { +"Attlevandetillegg" }) }
                                cell { text(bokmal { +gjt.format() }, nynorsk { +gjt.format() }) }
                            }
                        }
                        row {
                            cell { text(bokmal { +"Reduksjonsprosent" }, nynorsk { +"Reduksjonsprosent" }) }
                            cell { text(bokmal { +lopendeYtelse.reduksjonsprosent.format() + " prosent" }, nynorsk { +lopendeYtelse.reduksjonsprosent.format() + " prosent" }) }
                        }
                        row {
                            cell { text(bokmal { +"Din minstesats" }, nynorsk { +"Minstesatsen din" }) }
                            cell { text(bokmal { +lopendeYtelse.brukersMinstesats.format(3) + " G" }, nynorsk { +lopendeYtelse.brukersMinstesats.format(3) + " G" }) }
                        }
                        row {
                            cell { text(bokmal { +"Din etterbetaling" }, nynorsk { +"Etterbetalinga di" }) }
                            cell { text(bokmal { +data.etterbetaling.format() }, nynorsk { +data.etterbetaling.format() }) }
                        }
                    }
                }
                paragraph {
                    list {
                        item {
                            text(
                                bokmal { +"Du får " + lopendeYtelse.nettoTotal.format() + " i " },
                                nynorsk { +"Du får " + lopendeYtelse.nettoTotal.format() + " i " },
                            )
                            showIf(lopendeYtelse.nettoBarnetillegg.isNull() and lopendeYtelse.nettoGjenlevendetillegg.isNull()) {
                                text(
                                    bokmal { +"uføretrygd " },
                                    nynorsk { +"uføretrygd " },
                                )

                            }.orShow {
                                text(
                                    bokmal { +"uføretrygd og tillegg " },
                                    nynorsk { +"uføretrygd og tillegg " },
                                )
                            }
                            text(
                                bokmal { +"per måned før skatt fra 1. oktober 2026." },
                                nynorsk { +"per månad før skatt frå 1. oktober 2026." },
                            )
                        }
                        item {
                            text(
                                bokmal { +"Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                                nynorsk { +"Uføretrygda blir framleis utbetalt seinast den 20. kvar månad." },
                            )
                        }
                        item {
                            text(
                                bokmal { +"I vedlegget " },
                                nynorsk { +"I vedlegget " },
                            )
                            namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                            text(
                                bokmal { +" kan du se hvordan vi har beregnet uføretrygden din." },
                                nynorsk { +" kan du sjå korleis vi har berekna uføretrygda di." },
                            )
                        }
                    }
                }
            }

            title1 {
                text(
                    bokmal { +"Derfor omgjøres loven om redusert minstesats" },
                    nynorsk { +"Derfor gjer vi om lova om redusert minstesats" },
                )
            }
            paragraph {
                text(
                    bokmal { +"I forhandlingene om revidert statsbudsjett i juni, vedtok Stortinget at minstesatsen likevel ikke skal reduseres, og at denne loven trer i kraft 1. oktober 2026. Fra 1. oktober er minstesatsen 2.379 G (324 850 kroner), med tilbakevirkende kraft fra 1. juli 2026." },
                    nynorsk { +"I forhandlingane om revidert statsbudsjett i juni, vedtok Stortinget at minstesatsen likevel ikkje skal reduserast, og at denne lova trer i kraft 1. oktober 2026. Frå 1. oktober er minstesatsen 2.379 G (324 850 kroner), med tilbakeverknad frå 1. juli 2026." },
                )
            }

            ifNotNull(data.opphortYtelse) { opphortYtelse ->
                title1 {
                    text(
                        bokmal { +"Slik påvirkes dine utbetalinger" },
                        nynorsk { +"Slik blir utbetalingane dine påverka" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Fra 1. juli til " + opphortYtelse.opphorsdato.format() + ", har vi brukt den lavere minstesatsen i beregning av uføretrygden din. Når reverseringen nå trer i kraft, skal den ha virkning tilbake i tid fra 1. juli i år. Din uføretrygd opphørte " + opphortYtelse.opphorsdato.format() + ", derfor har du rett til en etterbetaling for perioden på " + data.etterbetaling.format() + ", dette vil komme om få dager." },
                        nynorsk { +"Frå 1. juli til " + opphortYtelse.opphorsdato.format() + ", har vi brukt den lågare minstesatsen i berekninga av uføretrygda di. Når reverseringa no trer i kraft, skal ho ha verknad tilbake i tid frå 1. juli i år. Uføretrygda di opphøyrde " + opphortYtelse.opphorsdato.format() + ", derfor har du rett til ei etterbetaling for perioden på " + data.etterbetaling.format() + ", dette vil komme om få dagar." },
                    )
                }
                title2 {
                    text(
                        bokmal { +"Etteroppgjør" },
                        nynorsk { +"Etteroppgjer" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Selv om din uføretrygd er opphørt, vil det gjøres et etteroppgjør for 2026 for deg, dette gjøres i 2027 når skattemeldingen for 2026 er klar. Dersom du har fått for mye eller for lite utbetalt, vil du motta et brev om dette høsten 2027." },
                        nynorsk { +"Sjølv om uføretrygda di er opphøyrd, vil det gjerast eit etteroppgjer for 2026 for deg, dette blir gjort i 2027 når skattemeldinga for 2026 er klar. Dersom du har fått for mykje eller for lite utbetalt, vil du motta eit brev om dette hausten 2027." },
                    )
                }
            }
                .orShow {
                    ifNotNull(data.lopendeYtelse) { lopendeYtelse ->
                        title1 {
                            text(
                                bokmal { +"Slik påvirkes dine utbetalinger" },
                                nynorsk { +"Slik blir utbetalingane dine påverka" },
                            )
                        }
                        paragraph {
                            text(
                                bokmal { +"Fra 1. juli til 1. oktober i år, har vi brukt den lavere minstesatsen i beregningen av uføretrygden din. Når reverseringen nå trer i kraft, skal den ha virkning tilbake i tid fra 1. juli i år. Derfor har du rett til en etterbetaling på " + data.etterbetaling.format() + ". Etterbetalingen får du sammen med neste utbetaling." },
                                nynorsk { +"Frå 1. juli til 1. oktober i år, har vi brukt den lågare minstesatsen i berekninga av uføretrygda di. Når reverseringa no trer i kraft, skal ho ha verknad tilbake i tid frå 1. juli i år. Derfor har du rett til ei etterbetaling på " + data.etterbetaling.format() + ". Etterbetalinga får du saman med neste utbetaling." },
                            )
                        }
                        showIf(lopendeYtelse.avkortetPgaRedusertTrygdetid) {
                            paragraph {
                                text(
                                    bokmal { +"Du har avkortet uføretrygd på grunn av redusert trygdetid. Din minstesats er " + lopendeYtelse.brukersMinstesats.format(3) + " G." },
                                    nynorsk { +"Du har avkorta uføretrygd på grunn av redusert trygdetid. Minstesatsen din er " + lopendeYtelse.brukersMinstesats.format(3) + " G." },
                                )
                            }
                        }
                        showIf(lopendeYtelse.harGradertUfoeretrygd) {
                            paragraph {
                                text(
                                    bokmal { +"Reverseringen av loven påvirker også deg som har gradert uføretrygd." },
                                    nynorsk { +"Reverseringa av lova påverkar òg deg som har gradert uføretrygd." },
                                )
                            }
                        }
                    }
                }

            title1 {
                text(
                    bokmal { +"Informasjon om etterbetalingen" },
                    nynorsk { +"Informasjon om etterbetalinga" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får ikke renter på etterbetalingen. Informasjon om skattetrekk på etterbetalingen finner du hos Skatteetaten." },
                    nynorsk { +"Du får ikkje renter på etterbetalinga. Informasjon om skattetrekk på etterbetalinga finn du hos Skatteetaten." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Har du gjeld som Skatteetaten krever inn, kan pengene fra etterbetalingen gå til å dekke gjelden. Eksempler på gjeld kan være bidrags- eller feilutbetalingsgjeld hos Nav, og refusjonskrav hos tjenestepensjonsordning." },
                    nynorsk { +"Har du gjeld som Skatteetaten krev inn, kan pengane frå etterbetalinga gå til å dekke gjelda. Døme på gjeld kan vere bidrags- eller feilutbetalingsgjeld hos Nav, og refusjonskrav hos tenestepensjonsordning." },
                )
            }

            ifNotNull(data.lopendeYtelse) { lopendeYtelse ->
                ifNotNull(lopendeYtelse.nettoBarnetillegg) { bt ->
                    title1 {
                        text(
                            bokmal { +"Endring i barnetillegg" },
                            nynorsk { +"Endring i barnetillegg" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Endring i minstesatsen fører til at du får en høyere utbetaling av uføretrygd. Uføretrygden regnes med som inntekt når vi beregner barnetillegg. Derfor får du en lavere utbetaling av barnetillegg. Ny beregning av barnetillegg (før skatt) er " + bt.format() + "." },
                            nynorsk { +"Endring i minstesatsen fører til at du får ei høgare utbetaling av uføretrygd. Uføretrygda blir rekna med som inntekt når vi reknar ut barnetillegg. Derfor får du ei lågare utbetaling av barnetillegg. Ny berekning av barnetillegg (før skatt) er " + bt.format() + "." },
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { +"Vedtaket har vi gjort etter " + data.hjemmeltekst + "." },
                        nynorsk { +"Vedtaket har vi gjort etter " + data.hjemmeltekst + "." },
                    )
                }

                title1 {
                    text(
                        bokmal { +"Du har rett til å klage" },
                        nynorsk { +"Du har rett til å klage" },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen vedtaket har kommet fram til deg. Du finner skjema og informasjon på " + "${Constants.KLAGE_URL}."
                        },
                        nynorsk {
                            +"Om du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen vedtaket har kome fram til deg. Du finn skjema og informasjon på " + "${Constants.KLAGE_URL}."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"I vedlegget " },
                        nynorsk { +"I vedlegget " },
                    )
                    namedReference(vedleggDineRettigheterOgPlikterUfore)
                    text(
                        bokmal { +" får du vite mer om hvordan du går fram for å klage." },
                        nynorsk { +" får du vite meir om korleis du går fram for å klage." },
                    )
                }
                includePhrase(Ufoeretrygd.RettTilInnsyn)
                includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
            }
        }
    }
}