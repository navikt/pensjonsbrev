package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr


data class HjemlerInnvilgelseForAP2011AP2016(
    val garantipensjonInnvilget: Expression<Boolean>,
    val godkjentYrkesskade: Expression<Boolean>,
    val innvilgetFor67: Expression<Boolean>,
    val pensjonstilleggInnvilget: Expression<Boolean>,
    val regelverkType: Expression<AlderspensjonRegelverkType>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(regelverkType.isNotAnyOf(AP2025, AP1967)) {
            /* AP2011TidligUttakHjemmel, AP2011TidligUttakPenHjemmel, AP2011Hjemmel, AP2011PenTHjemmel, AP2011YrkeskadeHjemmel, AP2011YrkesskadePenTHjemmel
            AP2016TidligUttakHjemmel, AP2016TidligUttakPenTHjemmel, AP2016TidligUttakPenTGarantiPensjonHjemmel, AP2016TidligUttakGarantiPensjonHjemmel,
            AP2016Hjemmel, AP2016YrksesskadeHjemmel, AP2016MNTHjemmel, AP2016YrkesskadeMNTHjemmel, AP2016MNTGarantiPensjonHjemmel,
            AP2016YrkesskadeMNTGarantiPensjonHjemmel, AP2016GarantiPensjonHjemmel, AP2016YrkesskadeGarantiPensjonHjemmel */
            paragraph {
                // vilkårene for minste trygdetid må oppfylles
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til ",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til ",
                    English to "This decision was made pursuant to the provisions of §§ 19-2 to "
                )
                showIf(pensjonstilleggInnvilget) {
                    text(
                        Bokmal to "19-9",
                        Nynorsk to "19-9",
                        English to "19-9"
                    )
                }.orShow {
                    // minste pensjonsnivå
                    text(
                        Bokmal to "19-8",
                        Nynorsk to "19-8",
                        English to "19-8"
                    )
                }
                text(
                    Bokmal to ", 19-10",
                    Nynorsk to ", 19-10",
                    English to ", 19-10"
                )
                showIf(innvilgetFor67) {
                    text(
                        Bokmal to ", 19-11",
                        Nynorsk to ", 19-11",
                        English to ", 19-11"
                    )
                }
                showIf(regelverkType.isOneOf(AP2016)) {
                    text(
                        Bokmal to ", 19-15",
                        Nynorsk to ", 19-15",
                        English to ", 19-15"

                    )
                }
                showIf(godkjentYrkesskade) {
                    text(
                        Bokmal to ", 19-20",
                        Nynorsk to ", 19-20",
                        English to ", 19-20"
                    )
                }
                showIf(regelverkType.isOneOf(AP2016)) {
                    text(
                        Bokmal to ", 20-2, 20-3",
                        Nynorsk to ", 20-2, 20-3",
                        English to ", 20-2, 20-3"
                    )
                    showIf(garantipensjonInnvilget) {
                        text(
                            Bokmal to ", 20-9",
                            Nynorsk to ", 20-9",
                            English to ", 20-9"
                        )
                    }.orShow {
                        text(
                            Bokmal to ", 20-12",
                            Nynorsk to ", 20-12",
                            English to ", 20-12"
                        )
                    }
                    showIf(innvilgetFor67) {
                        text(
                            Bokmal to " til 20-15",
                            Nynorsk to " til 20-15",
                            English to " to 20-15"
                        )
                    }.orShow {
                        text(
                            Bokmal to " til 20-14",
                            Nynorsk to " til 20-14",
                            English to " to 20-14"
                        )
                    }
                    text(
                        Bokmal to ", 20-19",
                        Nynorsk to ", 20-19",
                        English to ", 20-19"
                    )
                }
                text(
                    Bokmal to " og 22-12.",
                    Nynorsk to " og 22-12.",
                    English to " and 22-12 of the National Insurance Act."
                )
            }
        }
    }
}

data class SkjermingstilleggHjemmel(
    val skjermingstilleggInnvilget: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(skjermingstilleggInnvilget) {
            paragraph {
                text(
                    Bokmal to "Du er også innvilget skjermingstillegg etter folketrygdloven § 19-9a.",
                    Nynorsk to "Du er også innvilga skjermingstillegg etter folketrygdlova § 19-9a.",
                    English to "You have also been granted the supplement for the disabled pursuant to the provisions of § 19-9a of the National Insurance Act."
                )
            }
        }
    }
}

data class AP2025TidligUttakHjemmel(
    val regelverkType: Expression<AlderspensjonRegelverkType>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(regelverkType.isOneOf(AP2025)) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-2, 20-3, 20-9 til 20-15, 22-12 og 22-13.",
                    English to "This decision was made pursuant to the provisions of §§ 20-2, 20-3, 20-9 to 20-15, 22-12 and 22-13 of the National Insurance Act."
                )
            }
        }
    }
}


data class GarantitilleggHjemmel(
    val garantitilleggInnvilget: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(garantitilleggInnvilget) {
            paragraph {
                text(
                    Bokmal to "Du er også innvilget garantitillegg for opptjente rettigheter etter folketrygdloven § 20-20.",
                    Nynorsk to "Du er også innvilga garantitillegg for opptente rettar etter folketrygdlova § 20-20.",
                    English to "You have also been granted the guarantee supplement for accumulated rights pursuant to the provisions of § 20-20 of the National Insurance Act."
                )
            }
        }
    }
}

data class GjenlevendetilleggKap19Hjemmel(
    val gjenlevendetilleggKap19Innvilget: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(gjenlevendetilleggKap19Innvilget) {
            paragraph {
                text(
                    Bokmal to "Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024.",
                    Nynorsk to "Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024.",
                    English to "The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the National Insurance Act § 19-16 " +
                            "and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024."
                )
            }
        }
    }
}

data class InnvilgetGjRettKap19For2024(
    val gjenlevenderettAnvendt: Expression<Boolean>,
    val gjenlevendetilleggKap19Innvilget: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(gjenlevenderettAnvendt and not(gjenlevendetilleggKap19Innvilget)) {
            paragraph {
                text(
                    Bokmal to "Gjenlevenderett er innvilget etter § 19-16 i folketrygdloven.",
                    Nynorsk to "Attlevanderett er innvilga etter § 19-16 i folketrygdlova.",
                    English to "The survivor's rights in your retirement pension has been granted pursuant to the provisions of § 19-16 of the National Insurance Act"
                )
            }
            paragraph {
                text(
                    Bokmal to "Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024.",
                    Nynorsk to "Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024.",
                    English to "The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the " +
                            "National Insurance Act § 19-16 and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024."
                )
            }
        }
    }
}

// euArt6Hjemmel, euArt7Hjemmel, erArt6Og7Hjemmel
data class EOSLandAvtaleHjemmel(
    val borINorge: Expression<Boolean>,
    val eksportTrygdeavtaleEOS: Expression<Boolean>,
    val erEOSLand: Expression<Boolean>,
    val harOppfyltVedSammenlegging: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(erEOSLand) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004,",
                    Nynorsk to "Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004,",
                    English to "This decision was also made pursuant to the provisions of Regulation (EC) 883/2004,"
                )
                showIf(harOppfyltVedSammenlegging and borINorge) {
                    // euArt6Og7Hjemmel
                    text(Bokmal to " artikkel 6.", Nynorsk to " artikkel 6.", English to " article 6.")
                }.orShowIf(harOppfyltVedSammenlegging and not(borINorge) and eksportTrygdeavtaleEOS) {
                    text(Bokmal to " artikkel 6 og 7.", Nynorsk to " artikkel 6 og 7.", English to " articles 6 and 7.")
                }.orShowIf(not(harOppfyltVedSammenlegging) and not(borINorge) and eksportTrygdeavtaleEOS) {
                    text(Bokmal to " artikkel 7.", Nynorsk to " artikkel 7.", English to " article 7.")
                }
            }
        }
    }
}

data class BilateralAvtaleHjemmel(
    val avtalelandNavn: Expression<String?>,
    val eksportTrygdeavtaleAvtaleland: Expression<Boolean>,
    val erEOSLand: Expression<Boolean>,
    val harOppfyltVedSammenlegging: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf((harOppfyltVedSammenlegging or eksportTrygdeavtaleAvtaleland) and not(erEOSLand)) {
            paragraph {
                textExpr(
                    Bokmal to "Vedtaket er også gjort etter reglene i trygdeavtalen med ".expr() + avtalelandNavn.ifNull(
                        then = "AVTALELAND"
                    ) + ".",
                    Nynorsk to "Vedtaket er også gjort etter reglane i trygdeavtalen med ".expr() + avtalelandNavn.ifNull(
                        then = "AVTALELAND"
                    ) + ".",
                    English to "This decision was also made pursuant the provisions of the Social Security Agreement with ".expr() + avtalelandNavn.ifNull(
                        then = "AVTALELAND"
                    ) + "."
                )
            }
        }
    }
}
