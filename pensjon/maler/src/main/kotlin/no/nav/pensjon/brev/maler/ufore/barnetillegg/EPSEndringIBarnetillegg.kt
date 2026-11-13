package no.nav.pensjon.brev.maler.ufore.barnetillegg

import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.ufore.fraser.Ufoeretrygd
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TableScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

object EPSEndringIBarnetillegg {

    data class Brevdata(
        val virkningstidspunkt: Expression<LocalDate>,
        val nettoUforetrygdUtenTillegg: Expression<Kroner>,
        val nettoBarnetilleggFB: Expression<Kroner>,
        val nettoBarnetilleggSB: Expression<Kroner>,
        val totalbelop: Expression<Kroner>,
        val barnetilleggSB: Expression<Boolean>,
    )

    data class Outline(val data: Brevdata) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                    text(bokmal { +"Vi endrer barnetillegget i uføretrygden din fra " + data.virkningstidspunkt.format() + "." },
                        nynorsk { +"Vi har endra barnetillegget i uføretrygda di frå " + data.virkningstidspunkt.format() + "." }
                )
                table(header = {
                    column { text(
                        bokmal { +"Din ytelse per måned" },
                        nynorsk { +"Din ytelse per månad" }) }
                    column(alignment = RIGHT) { text(bokmal { +"Beløp" }, nynorsk { +"Beløp" }) }
                }) {
                    beloepsrader(data)
                }
            }

            showIf(data.totalbelop.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal { +"Uføretrygden blir fortsatt utbetalt senest den 20. hver måned." },
                        nynorsk { +"Uføretrygda blir fortsatt utbetalt seinast den 20. kvar månad." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Derfor har vi endret barnetillegget ditt " },
                    nynorsk { +"Derfor har vi endra barnetillegget ditt " },
                )
            }

            paragraph {
                text(bokmal { +"Du får barnetillegg for fellesbarn fordi du bor sammen med barnets andre forelder. For fellesbarn, bruker vi begge foreldrenes inntekt (inkludert uføretrygd) når vi beregner størrelsen på barnetillegget." },
                    nynorsk { +"Du får barnetillegg for fellesbarn fordi du bur saman med barnets andre forelder. For fellesbarn, brukar vi begge foreldrenes inntekt (inkludert uføretrygd) når vi reknar ut storleiken på barnetillegget." },)

            }
            paragraph {
                text(bokmal { +"Inntekten (inkludert uføretrygd) til barnets andre forelder er endret, og det har ført til at barnetillegget ditt har endret seg. Dette påvirker bare barnetillegg for fellesbarn, og ikke uføretrygden din." },
                    nynorsk { +"Inntekta (inkludert uføretrygd) til barnets andre forelder er endra, og det har ført til at barnetillegget ditt har endra seg. Dette påverkar berre barnetillegg for fellesbarn, og ikkje uføretrygda di." },)
            }
            paragraph {
                text(bokmal { +"Dersom du også får barnetillegg for særkullsbarn, påvirkes ikke dette av endringene. Du får barnetillegg for særkullsbarn hvis du ikke bor sammen med barnets andre forelder." },
                    nynorsk { +"Dersom du også får barnetillegg for særkullsbarn, vert ikkje dette påverka av endringane. Du får barnetillegg for særkullsbarn dersom du ikkje bur saman med barnets andre forelder." })
            }
            paragraph {
                text(
                    bokmal { +"Du kan lese mer om dette i vedlegget " },
                    nynorsk { +"Du kan lese meir om dette i vedlegget " },
                )
                namedReference(vedleggOpplysningerBruktIBeregningUTLegacy)
                text(
                    bokmal { +"." },
                    nynorsk { +"." },
                )
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter Folketrygdloven §§ 12-15, 12-16 og 22-12." },
                    nynorsk { +"Vedtaket har vi gjort etter Folketrygdlova §§ 12-15, 12-16 og 22-12." })
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer())
            includePhrase(Ufoeretrygd.Etteroppgjor)
            includePhrase(Ufoeretrygd.RettTilAKlage)
            includePhrase(Ufoeretrygd.RettTilInnsyn)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.HarDuSporsmal)
        }
    }

    private fun <LetterData : Any> TableScope<LangBokmalNynorsk, LetterData>.beloepsrader(data: Brevdata) {
        row {
            cell {
                text(
                    bokmal { +"Uføretrygd" },
                    nynorsk { +"Uføretrygd" },
                )
            }
            cell {
                text(
                    bokmal { +data.nettoUforetrygdUtenTillegg.format() },
                    nynorsk { +data.nettoUforetrygdUtenTillegg.format() },
                )
            }
        }
        row {
            cell {
                text(
                    bokmal { +"Barnetillegg fellesbarn" },
                    nynorsk { +"Barnetillegg fellesbarn" },
                )
            }
            cell {
                text(
                    bokmal { +data.nettoBarnetilleggFB.format(false) },
                    nynorsk { +data.nettoBarnetilleggFB.format(false) },
                )
            }
        }
        showIf(data.barnetilleggSB) {
            row {
                cell {
                    text(
                        bokmal { +"Barnetillegg særkullsbarn" },
                        nynorsk { +"Barnetillegg særkullsbarn" },
                    )
                }
                cell {
                    text(
                        bokmal { +data.nettoBarnetilleggSB.format(false) },
                        nynorsk { +data.nettoBarnetilleggSB.format(false) },
                    )
                }
            }
        }
        row {
            cell {
                text(
                    bokmal { +"Totalt" },
                    nynorsk { +"Totalt" },
                )
            }
            cell {
                text(
                    bokmal { +data.totalbelop.format(false) },
                    nynorsk { +data.totalbelop.format(false) },
                )
            }
        }
    }
}
