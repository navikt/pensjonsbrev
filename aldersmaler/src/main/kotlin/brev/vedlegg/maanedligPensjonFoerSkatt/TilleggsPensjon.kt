package brev.vedlegg.maanedligPensjonFoerSkatt

import no.nav.pensjon.brev.api.model.maler.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.maler.vedlegg.MaanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.erRedusert
import no.nav.pensjon.brev.api.model.maler.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.kombinertMedAvdod
import no.nav.pensjon.brev.api.model.maler.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaUngUfore
import no.nav.pensjon.brev.api.model.maler.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaUngUforeAvdod
import no.nav.pensjon.brev.api.model.maler.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaYrkesskade
import no.nav.pensjon.brev.api.model.maler.vedlegg.MaanedligPensjonFoerSkattDtoSelectors.TilleggspensjonGjeldendeSelectors.pgaYrkesskadeAvdod
import no.nav.pensjon.brev.api.model.maler.vedlegg.MaanedligPensjonFoerSkattTabell
import no.nav.pensjon.brev.api.model.maler.vedlegg.MaanedligPensjonFoerSkattTabellSelectors.AlderspensjonPerManedSelectors.tilleggspensjon
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner


private object TilleggspensjonenBold : ParagraphPhrase<LangBokmalNynorskEnglish>() {
    override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        text(
            bokmal { + "Tilleggspensjonen " },
            nynorsk { + "Tilleggspensjonen " },
            english { + "Your supplementary pension " },
            FontType.BOLD
        )
    }
}


data class MaanedligPensjonFoerSkattTilleggsPensjon(
    val tilleggspensjonGjeldende: Expression<MaanedligPensjonFoerSkattDto.TilleggspensjonGjeldende?>,
    val beregnetPensjonPerManedGjeldende: Expression<MaanedligPensjonFoerSkattTabell.AlderspensjonPerManed>,
    val regelverkstype: Expression<AlderspensjonRegelverkType>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //vedleggBelopFullTP_001
        ifNotNull(tilleggspensjonGjeldende) { tilleggspensjon ->
            val harTilleggspensjonUtbetalt = beregnetPensjonPerManedGjeldende.tilleggspensjon
                .ifNull(Kroner(0)).greaterThan(0)
            showIf(
                (not(tilleggspensjon.erRedusert)
                        and not(tilleggspensjon.kombinertMedAvdod)
                        and regelverkstype.isOneOf(AP2011, AP2016)
                        ) or (harTilleggspensjonUtbetalt and regelverkstype.isOneOf(AP1967))
            ) {
                paragraph {
                    includePhrase(TilleggspensjonenBold)
                    text(
                        bokmal { + "din avhenger av antall år med pensjonspoeng og størrelsen på pensjonspoengene. Du får pensjonspoeng for år med inntekt over folketrygdens grunnbeløp." },
                        nynorsk { + "din er avhengig av kor mange år du har hatt med pensjonspoeng, og storleiken på pensjonspoenga. Du får pensjonspoeng for år med inntekt over grunnbeløpet i folketrygda." },
                        english { + "depends on the number of years you earned pension points and how many pension points you earned. You receive pension points for years when you had an income greater than the National Insurance basic amount (G)." },
                    )
                }
            }

            //veldeggBelopRedusTP_001
            showIf(tilleggspensjon.erRedusert and regelverkstype.isOneOf(AP2011, AP2016)) {
                paragraph {
                    includePhrase(TilleggspensjonenBold)
                    text(
                        bokmal { + "din avhenger av antall år med pensjonspoeng og størrelsen på pensjonspoengene. Du får pensjonspoeng for år med inntekt over folketrygdens grunnbeløp. Du får ikke full tilleggspensjon fordi du har opptjent pensjonspoeng i mindre enn 40 år." },
                        nynorsk { + "din er avhengig av kor mange år du har hatt med pensjonspoeng, og storleiken på pensjonspoenga. Du får pensjonspoeng for år med inntekt over grunnbeløpet i folketrygda. Du får ikkje full tilleggspensjon fordi du har tent opp pensjonspoeng i mindre enn 40 år." },
                        english { + "depends on the number of years you earned pension points and how many pension points you earned. You receive pension points for years when you had an income greater than the National Insurance basic amount (G). You do not qualify for a full supplementary pension because you have earned pension points for less than 40 years." },
                    )
                }
            }

            //vedleggBelopTPAvdod_001
            showIf(tilleggspensjon.kombinertMedAvdod and regelverkstype.isOneOf(AP2011, AP2016, AP1967)) {
                paragraph {
                    includePhrase(TilleggspensjonenBold)
                    text(
                        bokmal { + "til en gjenlevende alderspensjonist kan enten bestå av pensjonistens egen tilleggspensjon eller 55 prosent av summen av pensjonistens egen tilleggspensjon og den avdødes tilleggspensjon. Tilleggspensjonen din er gitt etter det siste alternativet, da dette gir det høyeste beløpet for deg." },
                        nynorsk { + "til ein attlevande alderspensjonist kan anten bestå av pensjonistens eigen tilleggspensjon eller 55 prosent av summen av pensjonistens eigen tilleggspensjon og tilleggspensjonen til den avdøde. Tilleggspensjonen din er gitt etter det siste alternativet då det gir det høgaste beløpet for deg." },
                        english { + "for a widowed old age pensioner can be calculated either as the pensioner's own supplementary pension or as 55 percent of the sum of the pensioner's own supplementary pension and the deceased's supplementary pension. Your supplementary pension has been calculated using the latter method, as this is more beneficial for you." },
                    )
                }
            }

            //vedleggBelopTPYS_001
            showIf(tilleggspensjon.pgaYrkesskade and regelverkstype.isOneOf(AP2011, AP2016)) {
                paragraph {
                    text(
                        bokmal { + "Fordi uførheten din skyldes yrkesskade, er tilleggspensjonen din beregnet etter egne regler for yrkesskade. Dette gir deg en høyere tilleggspensjon enn en beregning etter ordinære regler for alderspensjon ville gitt deg." },
                        nynorsk { + "Sidan uførleiken din skriv seg frå yrkesskade, har vi rekna ut tilleggspensjonen din etter eigne reglar for yrkesskade. Dette gir deg ein høgare tilleggspensjon enn ordinære reglar for alderspensjon." },
                        english { + "Since your disability is the result of an occupational injury, your supplementary pension is calculated using particular regulations. This grants you a higher supplementary pension than the ordinary regulations for retirement pension." },
                    )
                }
            }

            //vedleggBelopTPYSAvdod_001
            showIf(tilleggspensjon.pgaYrkesskadeAvdod and regelverkstype.isOneOf(AP2011, AP2016, AP1967)) {
                paragraph {
                    text(
                        bokmal { + "Tilleggspensjonen etter avdøde er beregnet etter egne regler for yrkesskade. Dette gir deg en høyere tilleggspensjon." },
                        nynorsk { + "Tilleggspensjonen etter den avdøde er rekna ut etter eigne reglar for yrkesskade. Dette gir deg ein høgare tilleggspensjon. " },
                        english { + "The deceased's supplementary pension is calculated using particular regulations for occupational injury. This grants you a higher supplementary pension." },
                    )
                }
            }

            //vedleggBelopTPUngUfor_001
            showIf(tilleggspensjon.pgaUngUfore and regelverkstype.isOneOf(AP2011, AP2016)) {
                paragraph {
                    text(
                        bokmal { + "Tilleggspensjonen din er beregnet etter spesielle bestemmelser for unge uføre, som er mer gunstig enn ordinære bestemmelser. Denne beregningsfordelen videreføres ved overgang til alderspensjon." },
                        nynorsk { + "Tilleggspensjonen din er rekna ut etter spesielle føresegner for unge uføre, som er gunstigare enn ordinære føresegner. Denne utrekningsfordelen blir vidareført ved overgang til alderspensjon." },
                        english { + "Your supplementary pension has been calculated in accordance with particular regulations for young people with disabilities, which are more beneficial than the ordinary regulations. This favourable calculation continues to be used when you start receiving a retirement pension." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vær oppmerksom på at tilleggspensjonen din vil bli omregnet dersom du flytter fra Norge." },
                        nynorsk { + "Ver merksam på at tilleggspensjonen din vil bli omrekna dersom du flyttar frå Noreg." },
                        english { + "Please note that your supplementary pension will be recalculated if you move away from Norway." },
                    )
                }
            }

            //vedleggBelopTPUngUforAvdod_001
            showIf(tilleggspensjon.pgaUngUforeAvdod and regelverkstype.isOneOf(AP2011, AP2016, AP1967)) {
                paragraph {
                    text(
                        bokmal { + "Tilleggspensjonen etter avdøde er beregnet etter spesielle bestemmelser for unge uføre, som er mer gunstig enn ordinære bestemmelser." },
                        nynorsk { + "Tilleggspensjonen etter den avdøde er utrekna etter spesielle føresegner for unge uføre, som er gunstigare enn ordinære føresegner." },
                        english { + "The deceased's supplementary pension has been calculated in accordance with particular regulations for young people with disabilities, which are more beneficial than the ordinary regulations." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Tilleggspensjonen etter avdøde er beregnet etter spesielle bestemmelser for unge uføre, som er mer gunstig enn ordinære bestemmelser." },
                        nynorsk { + "Ver merksam på at tilleggspensjonen din vil bli omrekna dersom du flyttar frå Noreg." },
                        english { + "Please note that your supplementary pension will be recalculated if you move away from Norway." },
                    )
                }
            }
        }

    }

}