package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.FribelopPeriode
import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktFribelopData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.fribelopPeriode.faktor
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.fribelopPeriode.fom
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.fribelopPeriode.tom
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktFribelopData.*
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.formatMonthYear
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

object OktFribelop {

    data class Outline(val data: Expression<VedtakOmOktFribelopData>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            paragraph {
                text(
                    bokmal { +"Stortinget har vedtatt en lovendring som trer i kraft fra 1. oktober 2026 med virkning fra 1. januar 2026. Lovendringen sier at fribeløpet øker fra 0,4 G til 1 G for de som har hatt uføretrygd i 2 år eller mer, uten økning i uføregraden. " },
                    nynorsk { +"Stortinget har vedteke ei lovendring som trer i kraft frå 1. oktober 2026 med verknad frå 1. januar 2026. Lovendringa seier at fribeløpet aukar frå 0,4 G til 1 G for dei som har hatt uføretrygd i 2 år eller meir, utan auke i uføregraden. " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Denne endringen har bare betydning for deg som har, eller vurderer å ha, inntekt ved siden av uføretrygden. Hvis du ikke har inntekt ved siden av, har fribeløp ikke noe å si for deg. " },
                    nynorsk { +"Denne endringa har berre tyding for deg som har, eller vurderer å ha, inntekt ved sida av uføretrygda. Viss du ikkje har inntekt ved sida av, har fribeløp ikkje noko å seie for deg. " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Har du uføretrygd, kan du jobbe så mye du har mulighet til. Et høyere fribeløp kan føre til at det blir mer lønnsomt for deg å kombinere jobb og uføretrygd. " },
                    nynorsk { +"Har du uføretrygd, kan du jobbe så mykje du har moglegheit til. Eit høgare fribeløp kan føre til at det vert meir lønsamt for deg å kombinere jobb og uføretrygd. " },
                )
            }

            showIf(data.oktFribelopHeleAret) {
                paragraph {
                    text(
                        bokmal { +"Du har hatt uføretrygd i 2 år eller lenger før 1. januar 2026, og derfor øker fribeløpet ditt til 1 G for hele 2026. " },
                        nynorsk { +"Du har hatt uføretrygd i 2 år eller lenger før 1. januar 2026, og derfor aukar fribeløpet ditt til 1 G for heile 2026. " },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Slik beregner vi fribeløp " },
                    nynorsk { +"Slik reknar vi ut fribeløp " },
                )
            }

            showIf(not(data.oktFribelopHeleAret)) {
                paragraph {
                    text(
                        bokmal { +"Fra og med " + data.datoOkningBunnfradrag.format() + " har du hatt uføretrygd i 2 år og fribeløpet skal øke til 1G. " },
                        nynorsk { +"Frå og med " + data.datoOkningBunnfradrag.format() + " har du hatt uføretrygd i 2 år og fribeløpet skal auke til 1G. " },
                    )
                }
                includePhrase(FribelopPerioder(data.fribelopPerioder, data.vektetFribelop, data.bunnfradrag))
                showIf(not(data.normertPensjonsdatoFor2028)) {
                    paragraph {
                        text(
                            bokmal { +"Neste år: " },
                            nynorsk { +"Neste år: " },
                            FontType.BOLD
                        )
                        text(
                            bokmal { +"Fra 2027 vil ditt fribeløp være 1G hele året. " },
                            nynorsk { +"Frå 2027 vil fribeløpet ditt vere 1G heile året. " },
                        )
                    }
                }
            }

            paragraph {
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

            title1 {
                text(
                    bokmal { +"Hva er fribeløp og bunnfradrag?" },
                    nynorsk { +"Kva er fribeløp og botnfrådrag?" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Bunnfradrag er hvor mye inntekt du kan ha før vi begynner å redusere uføretrygden din. Bunnfradraget består av fribeløpet pluss  inntekt etter uførhet. Dette ble tidligere omtaltsom inntektsgrense. " },
                    nynorsk { +"Bunnfrådrag er kor mykje inntekt du kan ha før vi byrjar å redusere uføretrygda di. Bunnfrådraget består av fribeløpet pluss inntekt etter uførleik. Dette vart tidlegare omtalt som inntektsgrense. " },
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
                        +"Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen vedtaket har kommet fram til deg. Du finner skjema og informasjon på " +
                                "${Constants.KLAGE_URL}."
                    },
                    nynorsk {
                        +"Om du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen vedtaket har kome fram til deg. Du finn skjema og informasjon på " +
                                "${Constants.KLAGE_URL}."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"I vedlegget " },
                    nynorsk { +"I vedlegget " },
                )
                namedReference(vedleggDineRettigheterOgPlikterUfoere)
                text(
                    bokmal { +" får du vite mer om hvordan du går fram for å klage." },
                    nynorsk { +" får du vite meir om korleis du går fram for å klage." },
                )
            }
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }

    class FribelopPerioder(private val perioder: Expression<List<FribelopPeriode>>, private val vektetFribelop: Expression<Double>, private val bunnfradrag: Expression<Kroner>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                table(header = {
                    column { text(bokmal { +"Fra" }, nynorsk { +"Frå" }) }
                    column { text(bokmal { +"Til" }, nynorsk { +"Til" }) }
                    column { text(bokmal { +"Fribeløp" }, nynorsk { +"Fribeløp" }) }
                }) {
                    forEach(perioder) { periode ->
                        row {
                            cell {
                                text(
                                    bokmal { +periode.fom.formatMonthYear() },
                                    nynorsk { +"" + periode.fom.formatMonthYear() }
                                )
                            }
                            cell {
                                text(
                                    bokmal { +periode.tom.formatMonthYear() },
                                    nynorsk { +"" + periode.tom.formatMonthYear() }
                                )
                            }
                            cell {
                                text(
                                    bokmal { +periode.faktor.format() + " G" },
                                    nynorsk { +periode.faktor.format() + " G" }
                                )
                            }
                        }
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Når fribeløpet endres i løpet av året, beregnes et gjennomsnitt av periodene du har hatt med ulikt fribeløp. Gjennomsnittlig fribeløp i år blir " + vektetFribelop.format() + " G, som er " + bunnfradrag.format() + "." },
                    nynorsk { +"Når fribeløpet endrar seg i løpet av året, vert det rekna ut eit gjennomsnitt av periodane du har hatt med ulikt fribeløp. Gjennomsnittleg fribeløp i år vert " + vektetFribelop.format() + " G, som er " + bunnfradrag.format() + "." },
                )
            }
        }
    }
}
