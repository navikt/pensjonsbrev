package no.nav.pensjon.brev.maler.alder.endring.sivilstand.fraser

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.KravArsakType
import no.nav.pensjon.brev.api.model.MetaforceSivilstand
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

data class SivilstandHjemler(
    val regelverkType: Expression<AlderspensjonRegelverkType>,
    val kravArsakType: Expression<KravArsakType>,
    val sivilstand: Expression<MetaforceSivilstand>,
    val saertilleggInnvilget: Expression<Boolean>,
    val pensjonstilleggInnvilget: Expression<Boolean>,
    val minstenivaaIndividuellInnvilget: Expression<Boolean>,
    val minstenivaaPensjonistParInnvilget: Expression<Boolean>,
    val garantipensjonInnvilget: Expression<Boolean>,
    val saerskiltSatsErBrukt: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(
            kravArsakType.isNotAnyOf(
                KravArsakType.ALDERSOVERGANG,
                KravArsakType.VURDER_SERSKILT_SATS,
            )
                and regelverkType.isNotAnyOf(AlderspensjonRegelverkType.AP2025),
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ " },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ " },
                    english { + "This decision was made pursuant to the provisions of §§ " },
                )
                includePhrase(SivilstandSamboerHjemler(sivilstand))
                showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967) and saertilleggInnvilget) {
                    text(
                        bokmal { + ", 3-3" },
                        nynorsk { + ", 3-3" },
                        english { + ", 3-3" },
                    )
                }
                showIf(pensjonstilleggInnvilget or minstenivaaIndividuellInnvilget or minstenivaaPensjonistParInnvilget) {
                    text(
                        bokmal { + ", 19-8" },
                        nynorsk { + ", 19-8" },
                        english { + ", 19-8" },
                    )
                }
                showIf(pensjonstilleggInnvilget) {
                    text(
                        bokmal { + ", 19-9" },
                        nynorsk { + ", 19-9" },
                        english { + ", 19-9" },
                    )
                }
                showIf(garantipensjonInnvilget) {
                    text(
                        bokmal { + ", 20-9" },
                        nynorsk { + ", 20-9" },
                        english { + ", 20-9" },
                    )
                }
                text(
                    bokmal { + " og 22-12." },
                    nynorsk { + " og 22-12." },
                    english { + " and 22-12." },
                )
            }

            showIf(
                kravArsakType.isOneOf(KravArsakType.ALDERSOVERGANG) and
                    regelverkType.isNotAnyOf(
                        AlderspensjonRegelverkType.AP2025,
                    ),
            ) {
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven § 20-20." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova § 20-20." },
                        english { + 
                            "This decision was made pursuant to the provisions of § 20-20 of the National Insurance Act." },
                    )
                }
            }
        }

        showIf(
            kravArsakType.isOneOf(KravArsakType.VURDER_SERSKILT_SATS) and saerskiltSatsErBrukt,
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ " },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ " },
                    english { + "This decision was made pursuant to the provisions of §§ " },
                )
                showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP1967)) {
                    includePhrase(SivilstandSamboerHjemler(sivilstand))
                    showIf(saertilleggInnvilget) {
                        text(
                            bokmal { + ", 3-3" },
                            nynorsk { + ", 3-3" },
                            english { + ", 3-3" },
                        )
                    }
                    text(
                        bokmal { + ", 19-8 og 22-12." },
                        nynorsk { + ", 19-8 og 22-12." },
                        english { + ", 19-8 and 22-12 of the National Insurance Act." },
                    )
                }.orShowIf(
                    regelverkType.isOneOf(
                        AlderspensjonRegelverkType.AP2011,
                        AlderspensjonRegelverkType.AP2016,
                    ),
                ) {
                    showIf(sivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
                        text(
                            bokmal { + "1-5, " },
                            nynorsk { + "1-5, " },
                            english { + "1-5, " },
                        )
                    }
                    text(
                        bokmal { + "19-8, 19-9 og 22-12." },
                        nynorsk { + "19-8, 19-9 og 22-12." },
                        english { + "19-8, 19-9 and 22-12 of the National Insurance Act." },
                    )
                }
            }
        }

        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2025)) {
            // hjemmelSivilstandAP2025
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 20-9, 20-17 femte avsnitt og 22-12." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 20-9, 20-17 femte avsnitt og 22-12." },
                    english { + 
                        "This decision was made pursuant to the provisions of §§ 20-9, 20-17 fifth paragraph, and 22-12 of the National Insurance Act." },
                )
            }
        }
    }
}

private data class SivilstandSamboerHjemler(
    val sivilstand: Expression<MetaforceSivilstand>,
) : TextOnlyPhrase<LangBokmalNynorskEnglish>() {
    override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(sivilstand.isOneOf(MetaforceSivilstand.SAMBOER_1_5)) {
            text(
                bokmal { + "1-5, " },
                nynorsk { + "1-5, " },
                english { + "1-5, " },
            )
        }
        text(
            bokmal { + "3-2" },
            nynorsk { + "3-2" },
            english { + "3-2" },
        )
    }
}
