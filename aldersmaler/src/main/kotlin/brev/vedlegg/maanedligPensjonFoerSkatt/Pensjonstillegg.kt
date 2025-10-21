package brev.vedlegg.maanedligPensjonFoerSkatt

import brev.felles.bestemtForm
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.AlderspensjonGjeldendeSelectors.erEksportberegnet
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.SaerskiltSatsGjeldendeSelectors.saerskiltSatsErBrukt_safe
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabell
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.brukersSivilstand
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.fullTrygdetid
import no.nav.pensjon.brev.model.alder.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.pensjonstillegg
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class MaanedligPensjonFoerSkattPensjonstillegg(
    val beregnetPensjonPerManedGjeldende: Expression<MaanedligPensjonFoerSkattTabell.AlderspensjonPerManed>,
    val alderspensjonGjeldende: Expression<MaanedligPensjonFoerSkattDto.AlderspensjonGjeldende>,
    val saerskiltSatsGjeldende: Expression<MaanedligPensjonFoerSkattDto.SaerskiltSatsGjeldende?>,
    val regelverkstype: Expression<AlderspensjonRegelverkType>,
): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        showIf(beregnetPensjonPerManedGjeldende.pensjonstillegg.notNull() and regelverkstype.isOneOf(AP2011, AP2016)) {
            //vedleggBelopPt_001
            paragraph {
                text(
                    bokmal { + "Pensjonstillegget " },
                    nynorsk { + "Pensjonstillegget " },
                    english { + "A pension supplement " },
                    BOLD,
                )
                text(
                    bokmal { + "er gitt for å sikre et minste pensjonsnivå til deg som har liten eller ingen opptjening av tilleggspensjon. Størrelsen på minste pensjonsnivå avhenger av sivilstanden din." },
                    nynorsk { + "er gitt for å sikre eit minste pensjonsnivå til deg som har lita eller inga opptening av tilleggspensjon. Storleiken på minste pensjonsnivå avheng av sivilstanden din." },
                    english { + "is granted to ensure a minimum pension level for people who are not eligible for a supplementary pension or only qualify for a small supplementary pension. The minimum pension level depends on your marital status." },
                )
            }

            //vedleggBelopSaerSats_001
            showIf(saerskiltSatsGjeldende.saerskiltSatsErBrukt_safe.ifNull(false)) {
                paragraph {
                    text(
                        bokmal { + "Fordi du forsørger " + beregnetPensjonPerManedGjeldende.brukersSivilstand.bestemtForm()
                                + " din som er over 60 år, er tillegget fastsatt etter en særskilt sats for minste pensjonsnivå." },

                        nynorsk { + "Fordi du forsørgjer " + beregnetPensjonPerManedGjeldende.brukersSivilstand.bestemtForm()
                                + " din som er over 60 år, er tillegget fastsett etter ein særskilt sats for minste pensjonsnivå." },

                        english { + "Because you provide for your " + beregnetPensjonPerManedGjeldende.brukersSivilstand.bestemtForm()
                                + " who is over the age of 60, you are granted the minimum pension level according to a special rate." },
                    )
                }
            }

            showIf(not(beregnetPensjonPerManedGjeldende.fullTrygdetid)) {
                paragraph {
                    showIf(alderspensjonGjeldende.erEksportberegnet) {
                        // vedleggBelopPenTAvkorterRedusertTTEksport_001
                        text(
                            bokmal { + "Pensjonstillegget ditt er beregnet etter antall år med pensjonspoeng på samme måte som grunnpensjonen." },
                            nynorsk { + "Pensjonstillegget ditt er berekna etter talet på år med pensjonspoeng på same måte som grunnpensjonen." },
                            english { + "Your pension supplement is calculated on your number of pension point earning years in the same way as the basic pension." },
                        )
                    }.orShow {
                        text(
                            bokmal { + "Vi har avkortet pensjonstillegget ditt mot trygdetid på samme måte som for grunnpensjonen." },
                            nynorsk { + "Vi har avkorta pensjonstillegget ditt mot trygdetid på same måte som for grunnpensjonen." },
                            english { + "Your pension supplement is calculated on the same period of national insurance cover as the basic pension." },
                        )
                    }
                }
            }

        }
    }

}
