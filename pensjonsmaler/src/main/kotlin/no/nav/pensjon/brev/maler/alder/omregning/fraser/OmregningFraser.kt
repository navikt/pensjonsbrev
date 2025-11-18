package no.nav.pensjon.brev.maler.alder.omregning.fraser

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

data class Omregning2016Hjemler(
    val pensjonstilleggInnvilget: Expression<Boolean>,
    val garantipensjonInnvilget: Expression<Boolean>,
    val godkjentYrkesskade: Expression<Boolean>,
    val oppfyltVedSammenleggingKap19: Expression<Boolean>,
    val oppfyltVedSammenleggingKap20: Expression<Boolean>,
    val oppfyltVedSammenleggingFemArKap19: Expression<Boolean>,
    val oppfyltVedSammenleggingFemArKap20: Expression<Boolean>,
    val eksportTrygdeavtaleEOS: Expression<Boolean>,
    val borINorge: Expression<Boolean>,
    val erEOSLand: Expression<Boolean>,
    val skjermingstilleggInnvilget: Expression<Boolean>,
    val gjenlevenderettAnvendt: Expression<Boolean>,
    val garantitilleggInnvilget: Expression<Boolean>,
    val eksportTrygdeavtaleAvtaleland: Expression<Boolean>,
    val avtaleland: Expression<String?>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            pensjonstilleggInnvilget.not()
                    and garantipensjonInnvilget.not()
                    and godkjentYrkesskade.not()
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget.not()
                    and garantipensjonInnvilget.not()
                    and godkjentYrkesskade
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget
                    and garantipensjonInnvilget.not()
                    and godkjentYrkesskade.not()
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget
                    and garantipensjonInnvilget.not()
                    and godkjentYrkesskade
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget
                    and garantipensjonInnvilget
                    and godkjentYrkesskade.not()
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget
                    and garantipensjonInnvilget
                    and godkjentYrkesskade
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget.not()
                    and garantipensjonInnvilget
                    and godkjentYrkesskade.not()
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget.not()
                    and garantipensjonInnvilget
                    and godkjentYrkesskade
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    nynorsk { +"Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    english { +"This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(skjermingstilleggInnvilget) {
            paragraph {
                text(
                    bokmal { +"Du er også innvilget skjermingstillegg etter folketrygdloven § 19-9a." },
                    nynorsk { +"Du er også innvilga skjermingstillegg etter folketrygdlova § 19-9a." },
                    english { +"You have also been granted the supplement for the disabled pursuant to the provisions of § 19-9a of the National Insurance Act." }
                )
            }
        }

        showIf(gjenlevenderettAnvendt and garantitilleggInnvilget.not()) {
            paragraph {
                text(
                    bokmal { +"Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024." },
                    nynorsk { +"Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024." },
                    english { +"The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the National Insurance Act § 19-16 and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024." }
                )
            }
        }

        showIf(gjenlevenderettAnvendt and garantitilleggInnvilget) {
            paragraph {
                text(
                    bokmal { +"Gjenlevenderett er innvilget etter § 19-16 og gjenlevendetillegg etter kapittel 20 i folketrygdloven." },
                    nynorsk { +"Attlevanderett er innvilga etter § 19-16 og attlevandetillegg etter kapittel 20 i folketrygdlova." },
                    english { +"The survivor's rights in your retirement pension and the survivor's supplement have been granted pursuant to the provisions of § 19-16 and Chapter 20 of the National Insurance Act." }
                )
            }
        }

        showIf(garantitilleggInnvilget) {
            paragraph {
                text(
                    bokmal { +"Du er også innvilget garantitillegg for opptjente rettigheter etter folketrygdloven § 20-20." },
                    nynorsk { +"Du er også innvilga garantitillegg for opptente rettar etter folketrygdlova § 20-20." },
                    english { +"You have also been granted the guarantee supplement for accumulated rights pursuant to the provisions of § 20-20 of the National Insurance Act." }
                )
            }
        }

        showIf(
            (
                    oppfyltVedSammenleggingKap19
                            or oppfyltVedSammenleggingKap20
                            or oppfyltVedSammenleggingFemArKap19
                            or oppfyltVedSammenleggingFemArKap20)
                    and borINorge
                    and erEOSLand
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004." },
                    nynorsk { +"Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004." },
                    english { +"This decision was also made pursuant to the provisions of Regulation (EC) 883/2004." }
                )
            }
        }

        showIf(
            oppfyltVedSammenleggingKap19.not()
                    and oppfyltVedSammenleggingKap20.not()
                    and oppfyltVedSammenleggingFemArKap19.not()
                    and oppfyltVedSammenleggingFemArKap20.not()
                    and eksportTrygdeavtaleEOS
                    and borINorge.not()
                    and erEOSLand.not()
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 7." },
                    nynorsk { +"Vedtaket er også gjort etter EØS-avtalens reglar i forordning 883/2004, artikkel 7." },
                    english { +"This decision was also made pursuant to the provisions of Article 7 of Regulation (EC) 883/2004." }
                )
            }
        }

        showIf(
            (
                    oppfyltVedSammenleggingKap19
                            or oppfyltVedSammenleggingKap20
                            or oppfyltVedSammenleggingFemArKap19
                            or oppfyltVedSammenleggingFemArKap20)
                    and eksportTrygdeavtaleEOS
                    and borINorge.not()
                    and erEOSLand
        ) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004." },
                    nynorsk { +"Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004." },
                    english { +"This decision was also made pursuant to the provision of Regulation (EC) 883/2004." }
                )
            }
        }

        showIf(
            (
                    oppfyltVedSammenleggingKap19
                            or oppfyltVedSammenleggingKap20
                            or oppfyltVedSammenleggingFemArKap19
                            or oppfyltVedSammenleggingFemArKap20
                            or eksportTrygdeavtaleAvtaleland)
                    and erEOSLand.not()
        ) {
            ifNotNull(avtaleland) { avtaleland ->
                paragraph {
                    showIf(avtaleland.notNull()) {
                        text(
                            bokmal { + "Vedtaket er også gjort etter reglene i trygdeavtalen med " + avtaleland },
                            nynorsk { + "Vedtaket er også gjort etter reglane i trygdeavtalen med " + avtaleland },
                            english { + "This decision was also made pursuant the provisions of the Social Security Agreement with " + avtaleland },
                        )
                    }
                }
            }
        }
    }

}