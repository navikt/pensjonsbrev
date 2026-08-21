package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.VedtakOmOktFribelopData
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktBunnfradragData.*
import no.nav.pensjon.brev.api.model.maler.legacy.selectors.vedtakOmOktFribelopData.oktFribelopHeleAret
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.ITALIC
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

object OktFribelop {

    data class Outline(val data: Expression<VedtakOmOktFribelopData>) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

            paragraph {
                text(
                    bokmal { +"Fra 1. oktober øker fribeløpet for uføretrygd til 1 ganger folketrygdens grunnbeløp (G). Dette er per i dag 136 549 kroner. Det betyr at du kan ha inntekt på XX kroner, før vi begynner å redusere uføretrygden din. Fribeløpet ble tidligere omtalt som inntektsgrense. Grunnbeløpet (G) justeres i mai hvert år. " },
                    nynorsk { +"Frå 1. oktober aukar fribeløpet for uføretrygd til 1 gonger folketrygdens grunnbeløp (G). Dette er per i dag 136 549 kroner. Det tyder at du kan ha inntekt på XX kroner, før vi byrjar å redusere uføretrygda di. Fribeløpet vart tidlegare omtala som inntektsgrense. Grunnbeløpet (G) vert justert i mai kvart år. " },
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

            title1 {
                text(
                    bokmal { +"Derfor får du høyere fribeløp" },
                    nynorsk { +"Derfor får du høgare fribeløp" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Stortinget har vedtatt en lovendring som trer i kraft fra 1. oktober 2026 med virkning fra 1. januar 2026. Lovendringen sier at fribeløpet øker fra 0,4 til 1 G for de som har hatt uføretrygd i 2 år eller mer, uten økning i uføregraden. " },
                    nynorsk { +"Stortinget har vedteke ei lovendring som trer i kraft frå 1. oktober 2026 med verknad frå 1. januar 2026. Lovendringen seier at fribeløpet aukar frå 0,4 til 1 G for dei som har hatt uføretrygd i 2 år eller meir, utan auke i uføregraden. " },
                )
            }

            showIf(!data.oktFribelopHeleAret) {
                paragraph {
                    text(
                        bokmal { +"" },
                        nynorsk { +"" },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"" },
                        nynorsk { +"" },
                    )
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
}
