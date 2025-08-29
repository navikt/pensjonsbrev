package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder

import no.nav.pensjon.brev.api.model.PoengTallsType
import no.nav.pensjon.brev.api.model.PoengTallsType.*
import no.nav.pensjon.brev.api.model.vedlegg.Pensjonspoeng
import no.nav.pensjon.brev.api.model.vedlegg.PensjonspoengSelectors.arstall
import no.nav.pensjon.brev.api.model.vedlegg.PensjonspoengSelectors.bruktIBeregningen
import no.nav.pensjon.brev.api.model.vedlegg.PensjonspoengSelectors.grunnbelopVeiet
import no.nav.pensjon.brev.api.model.vedlegg.PensjonspoengSelectors.pensjonsgivendeinntekt
import no.nav.pensjon.brev.api.model.vedlegg.PensjonspoengSelectors.pensjonspoeng
import no.nav.pensjon.brev.api.model.vedlegg.PensjonspoengSelectors.poengtallstype
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.PLAIN
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text

data class TabellPoengrekke(val pensjonspoeng: Expression<List<Pensjonspoeng>>) : OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            table({
                column(alignment = RIGHT, columnSpan = 2) {
                    text(
                        bokmal { + "År" },
                        nynorsk { + "År" },
                        english { + "Year" }
                    )
                }
                column(alignment = RIGHT, columnSpan = 6) {
                    text(
                        bokmal { + "Pensjonsgivende inntekt (kr)" },
                        nynorsk { + "Pensjonsgivande inntekt (kr)" },
                        english { + "Pensionable income (NOK)" }
                    )
                }
                column(alignment = RIGHT, columnSpan = 4) {
                    text(
                        bokmal { + "Gj.snittlig G (kr)" },
                        nynorsk { + "Gj.snittleg G (kr)" },
                        english { + "Average G (NOK)" }
                    )
                }
                column(alignment = RIGHT, columnSpan = 5) {
                    text(
                        bokmal { + "Pensjonspoeng" },
                        nynorsk { + "Pensjonspoeng" },
                        english { + "Pension points" }
                    )
                }
                column(columnSpan = 5) { text(bokmal { + "Merknad" }, nynorsk { + "Merknad" }, english { + "Notes" }) }
            }) {
                forEach(pensjonspoeng) {
                    val bruktIBeregningen = it.bruktIBeregningen
                    row {
                        cell {
                            showIf(bruktIBeregningen) {
                                eval(it.arstall.format(), BOLD)
                            }.orShow {
                                eval(it.arstall.format())
                            }
                        }

                        cell {
                            showIf(bruktIBeregningen) {
                                eval(it.pensjonsgivendeinntekt.format(false), BOLD)
                            }.orShow {
                                eval(it.pensjonsgivendeinntekt.format(false))
                            }
                        }

                        cell {
                            showIf(bruktIBeregningen) {
                                eval(it.grunnbelopVeiet.format(false), BOLD)
                            }.orShow {
                                eval(it.grunnbelopVeiet.format(false))
                            }
                        }

                        cell {
                            showIf(bruktIBeregningen) {
                                eval(it.pensjonspoeng.format(), BOLD)
                            }.orShow {
                                eval(it.pensjonspoeng.format())
                            }
                        }
                        cell {
                            ifNotNull(it.poengtallstype) { poengtallstype ->
                                showIf(bruktIBeregningen) {
                                    includePhrase(PoengTallsTypeMerknad(poengtallstype, BOLD))
                                }.orShow {
                                    includePhrase(PoengTallsTypeMerknad(poengtallstype))
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private data class PoengTallsTypeMerknad(
        val poengTallsType: Expression<PoengTallsType>,
        val fontType: Element.OutlineContent.ParagraphContent.Text.FontType = PLAIN
    ) :
        TextOnlyPhrase<LangBokmalNynorskEnglish>() {
        override fun TextOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(poengTallsType.isOneOf(G, H, J, K, L)) {
                text(
                    bokmal { + "Omsorgspoeng er godskrevet" },
                    nynorsk { + "Omsorgspoeng er godskrevet" },
                    english { + "Points for care work are credited" },
                    fontType
                )
            }.orShowIf(poengTallsType.isOneOf(FPP)) {
                text(
                    bokmal { + "Framtidig pensjonspoeng" },
                    nynorsk { + "Framtidig pensjonspoeng" },
                    english { + "Pension point earning year" },
                    fontType
                )
            }
        }
    }
}