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
                column(alignment = RIGHT, columnSpan = 1) {
                    text(
                        Bokmal to "År",
                        Nynorsk to "År",
                        English to "Year"
                    )
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(
                        Bokmal to "Pensjonsgivende inntekt (kr)",
                        Nynorsk to "Pensjonsgivande inntekt (kr)",
                        English to "Pensionable income (NOK)"
                    )
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(
                        Bokmal to "Gj.snittlig G (kr)",
                        Nynorsk to "Gj.snittlig G (kr)",
                        English to "Average G (NOK)"
                    )
                }
                column(alignment = RIGHT, columnSpan = 1) {
                    text(
                        Bokmal to "Pensjonspoeng",
                        Nynorsk to "Pensjonspoeng",
                        English to "Pension points"
                    )
                }
                column(columnSpan = 2) { text(Bokmal to "Merknad", Nynorsk to "Merknad", English to "Notes") }
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
                                eval(it.pensjonsgivendeinntekt.format(), BOLD)
                            }.orShow {
                                eval(it.pensjonsgivendeinntekt.format())
                            }
                        }

                        cell {
                            showIf(bruktIBeregningen) {
                                eval(it.grunnbelopVeiet.format(), BOLD)
                            }.orShow {
                                eval(it.grunnbelopVeiet.format())
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
                    Bokmal to "Omsorgspoeng er godskrevet",
                    Nynorsk to "Omsorgspoeng er godskrevet",
                    English to "Points for care work are credited",
                    fontType
                )
            }.orShowIf(poengTallsType.isOneOf(FPP)) {
                text(
                    Bokmal to "Framtidig pensjonspoeng",
                    Nynorsk to "Framtidig pensjonspoeng",
                    English to "Pension point earning year",
                    fontType
                )
            }
        }
    }
}