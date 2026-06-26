package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Beregning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Krav
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Trygdetid
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.beregning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.krav.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.trygdetid.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU039V_TBU044V_1. Brevkode-/kravårsak-/strenglogikk er erstattet av eksplisitte
 * boolske flagg på DTO-en ([Visningsflagg]).
 */
data class TBU039V_TBU044V_1(
    val beregning: Expression<Beregning>,
    val trygdetid: Expression<Trygdetid>,
    val krav: Expression<Krav>,
    val flagg: Expression<Visningsflagg>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { + "Dette er trygdetiden din" },
                nynorsk { + "Dette er trygdetida di" },
            )
        }

        showIf(flagg.visStandardFastsettelseTrygdetid) {
            paragraph {
                text(
                    bokmal { + "Vi fastsetter trygdetiden din ut fra faktisk trygdetid" },
                    nynorsk { + "Vi fastset trygdetida di ut frå faktisk trygdetid" },
                )
                showIf(flagg.harFramtidigTrygdetid) {
                    text(
                        bokmal { + " og framtidig trygdetid" },
                        nynorsk { + " og framtidig trygdetid" },
                    )
                }
                text(
                    bokmal { + ". Den faktiske trygdetiden din er perioder du har vært medlem av folketrygden fra fylte 16 år og fram til uføretidspunktet. " },
                    nynorsk { + ". Den faktiske trygdetida di er periodar du har vore medlem av folketrygda frå fylte 16 år og fram til uføretidspunktet. " },
                )
                showIf(flagg.harFramtidigTrygdetid) {
                    text(
                        bokmal { + "Den framtidige trygdetiden er perioden fra uføretidspunktet ditt og fram til og med det året du fyller 66 år." },
                        nynorsk { + "Den framtidige trygdetida er perioden frå uføretidspunktet ditt og fram til og med det året du fyller 66 år." },
                    )
                }
            }
        }.orShow {
            paragraph {
                text(
                    bokmal { + "I utgangspunktet fastsetter vi trygdetiden din ut fra faktisk og fremtidig trygdetid. Er ytelsen innvilget etter unntaksregelen § 12-2 tredje ledd, så får du ikke fremtidig trygdetid, jf. folketrygdloven § 12-2 fjerde ledd. " },
                    nynorsk { + "I utgangspunktet fastsetter vi trygdetida di ut fra faktisk og framtidig trygdetid. Er ytelsen innvilga etter unntaksregelen § 12-2 tredje ledd, så får du ikkje framtidig trygdetid, jf. folketrygdlova § 12-2 fjerde ledd. " },
                )
                text(
                    bokmal { + "Den faktiske trygdetiden din er perioder du har vært medlem av folketrygden fra fylte 16 år og fram til uføretidspunktet. " },
                    nynorsk { + "Den faktiske trygdetida di er periodar du har vore medlem av folketrygda frå fylte 16 år og fram til uføretidspunktet. " },
                )
                text(
                    bokmal { + "Den framtidige trygdetiden er perioden fra uføretidspunktet ditt og fram til og med det året du fyller 66 år." },
                    nynorsk { + "Den framtidige trygdetida er perioden frå uføretidspunktet ditt og fram til og med det året du fyller 66 år." },
                )
            }
        }

        paragraph {
            text(
                bokmal { + "Størrelsen på uføretrygden din er avhengig av hvor lenge du har vært medlem av folketrygden. Full trygdetid er 40 år. Dersom trygdetiden er kortere enn 40 år, blir uføretrygden redusert." },
                nynorsk { + "Storleiken på uføretrygda di er avhengig av kor lenge du har vore medlem av folketrygda. Full trygdetid er 40 år. Dersom trygdetida er kortare enn 40 år, blir uføretrygda redusert." },
            )
        }

        showIf(
            beregning.anvendtTrygdetid.greaterThanOrEqual(40)
                and (not(flagg.yrkesskadeVilkaarOppfylt) or not(flagg.ufoeregradLikYrkesskadegrad)),
        ) {
            paragraph {
                text(
                    bokmal { + "Trygdetiden din er fastsatt til 40 år." },
                    nynorsk { + "Trygdetida di er fastsett til 40 år." },
                )
            }
        }

        showIf(
            beregning.anvendtTrygdetid.lessThan(40)
                and flagg.yrkesskadeVilkaarOppfylt
                and not(flagg.ufoeregradLikYrkesskadegrad),
        ) {
            paragraph {
                text(
                    bokmal { + "Trygdetiden din er fastsatt til 40 år, for den delen du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom." },
                    nynorsk { + "Trygdetida di er fastsett til 40 år for den delen du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Trygdetiden i folketrygden er fastsatt til " + beregning.anvendtTrygdetid.format() + " år for den delen av uførheten din som ikke skyldes en godkjent yrkesskade eller yrkessykdom." },
                    nynorsk { + "Trygdetida i folketrygda er fastsett til " + beregning.anvendtTrygdetid.format() + " år for den delen av uføretrygda di som ikkje skuldas ein godkjend yrkesskade eller yrkessjukdom." },
                )
                showIf(not(flagg.erMaanedEtterFoedsel) and trygdetid.trygdetidFomNorge.notNull()) {
                    text(
                        bokmal { + " Den faktiske trygdetiden din i denne perioden er fastsatt på grunnlag av følgende perioder:" },
                        nynorsk { + " Den faktiske trygdetida di i denne perioden er fastsett på grunnlag av følgjande periodar:" },
                    )
                }
            }
        }

        showIf(flagg.yrkesskadeVilkaarOppfylt and flagg.ufoeregradLikYrkesskadegrad) {
            paragraph {
                text(
                    bokmal { + "Trygdetiden din er fastsatt til 40 år, fordi du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom." },
                    nynorsk { + "Trygdetida di er fastsett til 40 år fordi du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom." },
                )
            }
        }

        showIf(beregning.anvendtTrygdetid.lessThan(40) and not(flagg.yrkesskadeVilkaarOppfylt)) {
            paragraph {
                text(
                    bokmal { + "Trygdetiden din i folketrygden er fastsatt til " + beregning.anvendtTrygdetid.format() + " år. " },
                    nynorsk { + "Trygdetida di i folketrygda er fastsett til " + beregning.anvendtTrygdetid.format() + " år." },
                )
                showIf(not(flagg.erMaanedEtterFoedsel) and trygdetid.trygdetidFomNorge.notNull()) {
                    text(
                        bokmal { + "Den faktiske trygdetiden din er fastsatt på grunnlag av følgende perioder:" },
                        nynorsk { + " Den faktiske trygdetida di er fastsett på grunnlag av følgjande periodar:" },
                    )
                }
            }
        }

        showIf(
            not(flagg.erMaanedEtterFoedsel)
                and trygdetid.trygdetidFomNorge.notNull()
                and (beregning.anvendtTrygdetid.lessThan(40) or krav.boddArbeidetUtland),
        ) {
            paragraph {
                text(
                    bokmal { + "Trygdetiden din i Norge" },
                    nynorsk { + "Trygdetida di i Noreg" },
                )
            }
        }
    }
}
