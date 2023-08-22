package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.AvviksResultatSelectors.avvik
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.AvviksResultatSelectors.fikk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.AvviksResultatSelectors.harFaattForMye
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.AvviksResultatSelectors.skulleFaatt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.AvviksResultatSelectors.skulleFaatt_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.inntektstakSamletInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.personinntektAnnenForelder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.resultat
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.resultat_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.samletInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.SaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.SaerkullsbarnSelectors.inntektstakSamletInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.SaerkullsbarnSelectors.resultat_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.SaerkullsbarnSelectors.samletInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.felles
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.felles_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.mindreEnn40AarTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.personinntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.saerkull
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.saerkull_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.totaltResultat
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.FratrekkSelectors.FratrekkLinjeSelectors.aarsak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.FratrekkSelectors.FratrekkLinjeSelectors.beloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.FratrekkSelectors.FratrekkLinjeSelectors.type
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.FratrekkSelectors.fratrekk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.FratrekkSelectors.sum
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.InntektSelectors.InntektLinjeSelectors.beloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.InntektSelectors.InntektLinjeSelectors.registerKilde
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.InntektSelectors.InntektLinjeSelectors.type
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.InntektSelectors.inntekter
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.InntektSelectors.sum
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.fratrekk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.InntektOgFratrekkSelectors.inntekt
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.model.ubestemtForm
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

data class Introduksjon(val periode: Expression<Year>) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Vi bruker opplysningene som du selv legger inn som inntekt på ${Constants.INNTEKTSPLANLEGGEREN_URL}, og opplysninger fra Skatteetaten. ".expr() +
                        "Vi har gjort en ny beregning av uføretrygden din for " + periode.format() + " etter opplysninger fra Skatteetaten. " +
                        "Du kan se skatteoppgjøret ditt på ${Constants.SKATTEETATEN_URL}.",
            )
        }
        paragraph {
            text(
                Bokmal to "Husk at du må melde fra til oss innen 3 uker hvis du mener beregningene feil.",
            )
        }
    }
}

data class FikkSkulleFaattTabell(
    val harFaattForMye: Expression<Boolean>,
    val harGjenlevendeTillegg: Expression<Boolean>,
    val ufoeretrygd: Expression<OpplysningerOmEtteroppgjoeretDto.AvviksResultat>,
    val barnetillegg: Expression<OpplysningerOmEtteroppgjoeretDto.Barnetillegg?>,
    val totaltAvvik: Expression<Kroner>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        title1 {
            text(
                Bokmal to "Hva du fikk utbetalt og hva du skulle fått utbetalt",
            )
        }
        paragraph {
            table(
                header = {
                    column(columnSpan = 5) {
                        text(
                            Bokmal to "Type stønad",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    column(columnSpan = 4, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Dette skulle du fått",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    column(columnSpan = 4, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Dette fikk du",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    column(columnSpan = 4, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Avviksbeløp",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                }
            ) {
                fun avviksResultatRad(
                    typeStoenad: TextElement<LangBokmal>,
                    resultat: Expression<OpplysningerOmEtteroppgjoeretDto.AvviksResultat?>
                ) {
                    ifNotNull(resultat) {
                        row {
                            cell {
                                addTextContent(typeStoenad)
                            }
                            cell {
                                textExpr(
                                    Bokmal to it.skulleFaatt.format() + " kr",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to it.fikk.format() + " kr",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to it.avvik.format() + " kr",
                                )
                            }
                        }
                    }
                }
                avviksResultatRad(
                    newTextExpr(
                        Bokmal to "Uføretrygd".expr() + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegg", ""),
                    ),
                    ufoeretrygd,
                )
                avviksResultatRad(
                    newText(
                        Bokmal to "Barnetillegg særkullsbarn",
                    ),
                    barnetillegg.saerkull_safe.resultat_safe,
                )
                avviksResultatRad(
                    newText(
                        Bokmal to "Barnetillegg fellesbarn",
                    ),
                    barnetillegg.felles_safe.resultat_safe,
                )
                row {
                    cell {
                        textExpr(
                            Bokmal to "Beløp du har fått for ".expr() + ifElse(harFaattForMye, "mye", "lite"),
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    cell { }
                    cell { }
                    cell {
                        textExpr(
                            Bokmal to totaltAvvik.format() + " kr",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                }
            }
        }
    }
}

data class DuHarFaattAvviksBeloep(
    val totaltAvvik: Expression<Kroner>,
    val harFaattForMye: Expression<Boolean>,
    val harBarnetillegg: Expression<Boolean>,
    val harGjenlevendeTillegg: Expression<Boolean>,
    val periode: Expression<Year>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Du har fått ".expr() + totaltAvvik.absoluteValue().format() + " kroner for " +
                        ifElse(harFaattForMye, "mye", "lite") + " i uføretrygd",
            )
            showIf(harBarnetillegg and harGjenlevendeTillegg) {
                text(
                    Bokmal to ", barnetillegg og gjenlevendetillegg",
                )
            }.orShowIf(harBarnetillegg) {
                text(
                    Bokmal to " og barnetillegg",
                )
            }.orShowIf(harGjenlevendeTillegg) {
                text(
                    Bokmal to " og gjenlevendetillegg"
                )
            }
            textExpr(
                Bokmal to " i perioden ".expr() + periode.format(),
            )
        }
    }
}

data class OmBeregningAvBarnetillegg(
    val barnetillegg: Expression<OpplysningerOmEtteroppgjoeretDto.Barnetillegg>,
    val periode: Expression<Year>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        title1 {
            text(
                Bokmal to "Om beregning av barnetillegg",
            )
        }
        paragraph {
            text(
                Bokmal to "Det er personinntekt som avgjør hvor mye du får i barnetillegg. Dette står i §12-2 i skatteloven. Personinntekter omfatter:",
            )
            list {
                item {
                    text(
                        Bokmal to "pensjonsgivende inntekt",
                    )
                }
                item {
                    text(
                        Bokmal to "uføretrygd",
                    )
                }
                item {
                    text(
                        Bokmal to "alderspensjon fra folketrygden",
                    )
                }
                item {
                    text(
                        Bokmal to "andre pensjoner og ytelser, også fra utlandet",
                    )
                }
            }
            text(
                Bokmal to "Hvis personinntekten din overstiger et visst beløp (fribeløp), blir barnetillegget redusert eller faller helt bort." +
                        "Fikk du innvilget barnetillegg i løpet av året, eller barnetillegget opphørte i løpet av året" +
                        ", er det bare inntekten for perioden med rett til barnetillegg som har betydning.",
            )
        }

        ifNotNull(barnetillegg.felles) { fellesbarn ->
            title2 {
                text(
                    Bokmal to "For barn som bor sammen med begge foreldrene:",
                )
            }
            paragraph {
                list {
                    item {
                        textExpr(
                            Bokmal to "Barnetillegget beregnes ut fra inntekten til deg og din ".expr() + fellesbarn.sivilstand.ubestemtForm() + ".",
                        )
                    }
                    item {
                        textExpr(
                            Bokmal to "Fribeløpet er 4,6 ganger folketrygdens grunnbeløp. I ".expr() + periode.format() + " var fribeløpet " + fellesbarn.fribeloep.format() + " kroner.",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Fribeløpet øker med 40% av folketrygdens grunnbeløp for hvert ekstra barn.",
                        )
                    }
                    showIf(barnetillegg.mindreEnn40AarTrygdetid) {
                        item {
                            text(
                                Bokmal to "Fribeløpet blir redusert ut fra trygdetiden du har.",
                            )
                        }
                    }
                    item {
                        text(
                            Bokmal to "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet.",
                        )
                    }
                }
            }
        }

        ifNotNull(barnetillegg.saerkull) { saerkull ->
            title2 {
                text(
                    Bokmal to "For barn som ikke bor sammen med begge foreldrene:",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Bokmal to "Barnetillegget beregnes ut fra inntekten din.",
                        )
                    }
                    item {
                        textExpr(
                            Bokmal to "Fribeløpet er 3,1 ganger folketrygdens grunnbeløp. I ".expr() + periode.format() + " var fribeløpet " + saerkull.fribeloep.format() + " kroner.",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Fribeløpet øker med 40% av folketrygdens grunnbeløp for hvert ekstra barn.",
                        )
                    }
                    showIf(barnetillegg.mindreEnn40AarTrygdetid) {
                        item {
                            text(
                                Bokmal to "Fribeløpet blir redusert ut fra trygdetiden du har.",
                            )
                        }
                    }
                    item {
                        text(
                            Bokmal to "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet.",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Inntekten til en ektefelle/parner/samboer som ikke er forelder for barnet, har ingen betydning."
                        )
                    }
                }
            }
        }


        paragraph {
            val skulleFaatt =
                barnetillegg.saerkull.resultat_safe.skulleFaatt_safe.ifNull(Kroner(0)) + barnetillegg.felles.resultat_safe.skulleFaatt_safe.ifNull(
                    Kroner(0)
                )
            textExpr(
                Bokmal to "Ved beregning av barnetillegg har vi først oppdatert hvor mye du skulle hatt i uføretrygd. ".expr() +
                        "Etter denne beregningen er gjort, blir ditt barnetillegg " + skulleFaatt.format() + " kroner for " + periode.format() + ".",
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Du har fått utbetalt ".expr() + barnetillegg.totaltResultat.fikk.format() + " kroner i barnetillegg. Du har fått " +
                        barnetillegg.totaltResultat.avvik.absoluteValue().format() + " kroner for " +
                        ifElse(barnetillegg.totaltResultat.harFaattForMye, "mye", "lite") + " i barnetillegg.",
            )
        }

        title2 {
            text(
                Bokmal to "Beregningene er gjort hver for seg hvis",
            )
        }
        paragraph {
            list {
                item {
                    text(
                        Bokmal to "du har flere barn som har ulike bosituasjoner.",
                    )
                }
                item {
                    text(
                        Bokmal to "barnet bor med begge foreldre i deler av året, og en av foreldrene resten av året.",
                    )
                }
            }
        }

        val harFellesTillegg = barnetillegg.felles.notNull()

        paragraph {
            textExpr(
                Bokmal to "Tabellene under viser inntektene du".expr() + ifElse(harFellesTillegg, " og annen forelder", "") +
                        " har hatt i perioden " + ifElse(harFellesTillegg, "dere", "du") + " hadde rett til barnetillegg" +
                        "Det er disse inntektene vi har brukt for å beregne barnetillegget.",
            )
        }

        title2 {
            text(
                Bokmal to "Din personinntekt",
            )
        }
        paragraph {
            includePhrase(
                InntektTabell(
                    barnetillegg.personinntekt.inntekt,
                    newText(
                        Bokmal to "Total personinntekt",
                        fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                    ),
                )
            )
        }

        title2 {
            text(
                Bokmal to "Beløp som er trukket fra personinntekten din",
            )
        }
        paragraph {
            showIf(barnetillegg.personinntekt.fratrekk.fratrekk.isNotEmpty()) {
                includePhrase(
                    FratrekkTabell(
                        barnetillegg.personinntekt.fratrekk,
                        newText(
                            Bokmal to "Totalbeløp som er trukket fra personinntekten din",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        ),
                    )
                )
            } orShow {
                textExpr(
                    Bokmal to "Du har ikke hatt inntekter som er trukket fra personinntekten din i ".expr() + periode.format() +
                            ". Hvis du har hatt inntekter som kan trekkes fra, må du sende oss dokumentasjon på det innen 3 uker.",
                )
            }
        }

        ifNotNull(barnetillegg.felles) { fellesbarn ->
            title2 {
                text(
                    Bokmal to "Personinntekt til annen forelder",
                )
            }
            paragraph {
                includePhrase(
                    InntektTabell(
                        fellesbarn.personinntektAnnenForelder.inntekt,
                        newText(
                            Bokmal to "Total personinntekt til annen forelder",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        ),
                    )
                )
            }
            paragraph {
                text(
                    Bokmal to "Mottar annen forelder uføretrygd eller alderspensjon fra NAV, regnes dette også med som personinntekt.",
                )
            }

            showIf(fellesbarn.personinntektAnnenForelder.fratrekk.fratrekk.isNotEmpty()) {
                title2 {
                    text(
                        Bokmal to "Beløp som er trukket fra annen forelder sin personinntekt",
                    )
                }
                paragraph {
                    includePhrase(
                        FratrekkTabell(
                            fellesbarn.personinntektAnnenForelder.fratrekk,
                            newText(
                                Bokmal to "Totalbeløp som er trukket fra personinntekten til annen forelder",
                                fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                            ),
                        )
                    )
                }

                showIf(
                    fellesbarn.resultat.avvik.notEqualTo(0)
                            and fellesbarn.personinntektAnnenForelder.inntekt.sum.greaterThan(fellesbarn.personinntektAnnenForelder.fratrekk.sum)
                ) {
                    paragraph {
                        textExpr(
                            Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + fellesbarn.grunnbelop.format() + " kroner er holdt utenfor inntekten til annen forelder.",
                        )
                    }
                }
            } orShow {
                paragraph {
                    textExpr(
                        Bokmal to "Annen forelder har ikke hatt inntekt som er trukket fra sin personinntekt i ".expr() + periode.format() + ".",
                    )
                }
            }

        }

        ifNotNull(barnetillegg.saerkull) { saerkull ->
            includePhrase(ForHoeyInntektBarnetillegg(false, periode, saerkull.samletInntekt, saerkull.inntektstakSamletInntekt))
        }

        ifNotNull(barnetillegg.felles) { fellesbarn ->
            includePhrase(ForHoeyInntektBarnetillegg(true, periode, fellesbarn.samletInntekt, fellesbarn.inntektstakSamletInntekt))
        }
    }
}

private data class ForHoeyInntektBarnetillegg(
    val gjelderFlerePersonersInntekt: Boolean,
    val periode: Expression<Year>,
    val samletInntekt: Expression<Kroner>,
    val inntektstak: Expression<Kroner>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() =
        paragraph {
            if (gjelderFlerePersonersInntekt) {
                text(
                    Bokmal to "Du ",
                )
            } else {
                text(
                    Bokmal to "Dere ",
                )
            }
            textExpr(
                Bokmal to "hadde for høy samlet inntekt i ".expr() + periode.format() + " for å få utbetalt barnetillegg for særkullsbarn. " +
                        "Sum av samlet inntekt som gjør at barnetillegget ikke blir utbetalt var " + samletInntekt.format() + " kroner. " +
                        "Inntektstaket for å få utbetalt barnetillegg for særkullsbarn var " + inntektstak.format() + " kroner.",
            )
        }
}

data class OmBeregningAvUfoeretrygd(
    val harGjenlevendeTillegg: Expression<Boolean>,
    val pensjonsgivendeInntekt: Expression<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk>,
    val periode: Expression<Year>,
    val pensjonsgivendeInntektBruktIBeregningen: Expression<Kroner>,
    val ufoeretrygd: Expression<OpplysningerOmEtteroppgjoeretDto.AvviksResultat>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        title1 {
            textExpr(
                Bokmal to "Om beregningen av uføretrygd".expr() + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegg", ""),
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Det er pensjonsgivende inntekt som avgjør hvor mye du får i uføretrygd".expr()
                        + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegg", "")
                        + ". Dette står i § 3-15 i folketrygdloven. Pensjonsgivende inntekt er blant annet:",
            )
            list {
                item {
                    text(
                        Bokmal to "brutto lønnsinntekt fra Norge inkludert feriepenger",
                    )
                }
                item {
                    text(
                        Bokmal to "lønnsinntekt fra utlandet",
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt fra selvstendig næringsvirksomhet",
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt som fosterforelder",
                    )
                }
                item {
                    text(
                        Bokmal to "omsorgslønn",
                    )
                }
            }

            val inntektFoerFratrekk = pensjonsgivendeInntekt.inntekt.sum.format()
            textExpr(
                Bokmal to "Din pensjonsgivende inntekt har i perioden ".expr() + periode.format() + " vært " + inntektFoerFratrekk + " kroner.",
            )
        }

        paragraph {
            text(
                Bokmal to "Hva kan bli trukket fra den pensjonsgivende inntekten din?",
            )
            list {
                item {
                    text(
                        Bokmal to "inntekt før du ble uføretrygdet",
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt etter at uføretrygden din opphørte",
                    )
                }
                item {
                    text(
                        Bokmal to "erstatning for inntektstap (erstatningsoppgjør)",
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt fra helt avsluttet arbeid eller virksomhet",
                    )
                }
                item {
                    text(
                        Bokmal to "etterbetaling du har fått fra NAV",
                    )
                }
            }

            val inntektEtterFratrekk = pensjonsgivendeInntektBruktIBeregningen.format()
            textExpr(
                Bokmal to "Etter beregningen er gjort, har du ".expr() + inntektEtterFratrekk + " kroner i pensjonsgivende inntekt.",
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Du skulle ha fått ".expr() + ufoeretrygd.skulleFaatt.format() + " kroner i uføretrygd i " + periode.format()
                        + ". Du fikk imidlertid " + ufoeretrygd.fikk.format() + " kroner. Du har derfor fått " + ufoeretrygd.avvik.absoluteValue().format()
                        + " kroner for " + ifElse(ufoeretrygd.harFaattForMye, "mye", "lite") + " i uføretrygd.",
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Tabellene under viser inntektene du har hatt i ".expr() + periode.format()
                        + ". Det er disse inntektene vi har brukt for å beregne uføretrygden din"
                        + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegget ditt.", "."),
            )
        }

        title1 {
            text(
                Bokmal to "Din pensjonsgivende inntekt",
            )
        }
        showIf(pensjonsgivendeInntekt.inntekt.inntekter.isNotEmpty()) {
            paragraph {
                includePhrase(
                    InntektTabell(
                        pensjonsgivendeInntekt.inntekt,
                        newText(
                            Bokmal to "Total pensjonsgivende inntekt",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        ),
                    ),
                )
            }

            title1 {
                text(
                    Bokmal to "Beløp som er trukket fra den pensjonsgivende inntekten din",
                )
            }
            paragraph {
                showIf(pensjonsgivendeInntekt.fratrekk.fratrekk.isNotEmpty()) {
                    includePhrase(
                        FratrekkTabell(
                            pensjonsgivendeInntekt.fratrekk,
                            newText(
                                Bokmal to "Totalbeløp som er trukket fra",
                                fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                            )
                        )
                    )
                } orShow {
                    textExpr(
                        Bokmal to "Du har ikke hatt inntekter som er trukket fra den pensjonsgivende inntekten din i ".expr()
                                + periode.format() + ". Hvis du har hatt inntekter som kan trekkes fra, må du sende oss dokumentasjon på det innen 3 uker.",
                    )
                }
            }
        } orShow {
            paragraph {
                textExpr(
                    Bokmal to "Du har ikke hatt pensjonsgivende inntekt i ".expr() + periode.format() + ".",
                )
            }
        }
    }
}

data class ErOpplysningeneOmInntektFeil(
    val harFellesTillegg: Expression<Boolean>,
) : OutlinePhrase<LangBokmal>() {
    override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
        title1 {
            text(
                Bokmal to "Er opplysningene om inntekt feil?",
            )
        }
        paragraph {
            text(
                Bokmal to "Mener du at inntektsopplysningene i skatteoppgjøret er feil, er det Skatteetaten som skal vurdere om inntekten kan endres.",
            )
        }
        paragraph {
            text(
                Bokmal to "Vi gjør et nytt etteroppgjør automatisk hvis Skatteetaten endrer inntekten din. Du får tilbakemelding hvis endringen påvirker etteroppgjøret ditt.",
            )
        }

        showIf(harFellesTillegg) {
            title2 {
                text(
                    Bokmal to "Barnetillegg og feil i den andre forelderens inntektsopplysninger",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mener inntektsopplysningene for den andre forelderen er feil, må den andre forelderen kontakte Skatteetaten.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi oppdaterer ikke automatisk etteroppgjøret ditt når vi får en korrigering fra Skatteetaten som gjelder den andre forelderen. " +
                            "Du må derfor gi beskjed til oss. Vi gjør da et manuelt etteroppgjør. Du trenger ikke å sende inn dokumentasjon.",
                )
            }
        }

        title2 {
            text(
                Bokmal to "Endringer i pensjonsytelser",
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis inntekten din fra pensjonsytelser utenom NAV blir endret, må du gi beskjed til oss når endringen er gjort. " +
                        "Vi gjør da et nytt etteroppgjør. Du kan gi beskjed ved å skrive til oss på ${Constants.SKRIV_TIL_OSS_URL} " +
                        "eller ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON}.",
            )
        }
    }
}

data class FratrekkTabell(val fratrekk: Expression<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk>, val sumText: TextElement<LangBokmal>) :
    ParagraphPhrase<LangBokmal>() {

    override fun ParagraphOnlyScope<LangBokmal, Unit>.template() =
        table(
            header = {
                column(columnSpan = 3) {
                    text(
                        Bokmal to "Type inntekt",
                    )
                }
                column(columnSpan = 3) {
                    text(
                        Bokmal to "Årsak til trekk",
                    )
                }
                column(columnSpan = 2, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(
                        Bokmal to "Beløp",
                    )
                }
            }
        ) {
            forEach(fratrekk.fratrekk) { fratrekkLinje ->
                row {
                    cell {
                        eval(fratrekkLinje.type.format(LocalizedFratrekkType))
                    }
                    cell {
                        eval(fratrekkLinje.aarsak.format(LocalizedAarsak))
                    }
                    cell {
                        includePhrase(Felles.KronerText(fratrekkLinje.beloep))
                    }
                }
            }
            row {
                cell { addTextContent(sumText) }
                cell { }
                cell { includePhrase(Felles.KronerText(fratrekk.sum, fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD)) }
            }
        }

    object LocalizedAarsak : LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak>() {
        // TODO flere språk
        override fun apply(first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak, second: Language): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.FOER_INNVILGET_UFOERETRYGD -> "Inntekt før uføretrygden ble innvilget"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ETTER_OPPHOERT_UFOERETRYGD -> "Inntekt etter at uføretrygden opphørte"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ERSTATNING_INNTEKTSTAP_ERSTATNINGSOPPGJOER -> "Erstatning for inntektstap ved erstatningsoppgjør"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ETTERSLEP_AVSLUTTET_ARBEID_ELLER_VIRKSOMHET -> "Inntekt fra helt avsluttet arbeid eller virksomhet"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ANNET -> "Annet"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ETTERBETALING_FRA_NAV -> "Etterbetaling fra NAV"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.INNTEKT_INNTIL_1G -> "Inntekt inntil ett grunnbeløp"
            }
    }

    object LocalizedFratrekkType : LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType>() {
        // TODO Flere språk
        override fun apply(first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType, second: Language): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.ARBEIDSINNTEKT -> "Arbeidsinntekt"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.UTLANDSINNTEKT -> "Utlandsinntekt"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.NAERINGSINNTEKT -> "Næringsinntekt"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.ANDRE_PENSJONER_OG_YTELSER -> "Pensjon fra andre enn NAV"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.FORVENTET_PENSJON_FRA_UTLANDET -> "Pensjon fra utlandet"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.INNTEKT -> "Inntekt"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.FRATREKKBAR_INNTEKT -> "Inntekt som kan trekkes fra"
            }

    }
}

data class InntektTabell(val inntekt: Expression<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt>, val sumText: TextElement<LangBokmal>) :
    ParagraphPhrase<LangBokmal>() {

    override fun ParagraphOnlyScope<LangBokmal, Unit>.template() =
        table(
            header = {
                column(columnSpan = 3) {
                    text(
                        Bokmal to "Type inntekt",
                    )
                }
                column(columnSpan = 3) {
                    text(
                        Bokmal to "Mottatt av",
                    )
                }
                column(columnSpan = 2, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(
                        Bokmal to "Registrert inntekt",
                    )
                }
            }
        ) {
            forEach(inntekt.inntekter) { inntektLinje ->
                row {
                    cell {
                        eval(inntektLinje.type.format(LocalizedInntektType))
                    }
                    cell {
                        eval(inntektLinje.registerKilde.format(LocalizedKilde))
                    }
                    cell {
                        includePhrase(Felles.KronerText(inntektLinje.beloep))
                    }
                }
            }
            row {
                cell { addTextContent(sumText) }
                cell { }
                cell { includePhrase(Felles.KronerText(inntekt.sum, fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD)) }
            }
        }

    object LocalizedKilde : LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde>() {
        // TODO flere språk
        override fun apply(first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde, second: Language): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.INNMELDT_AV_ARBEIDSGIVER -> "Elektronisk innmeldt fra arbeidsgiver"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.OPPGITT_AV_SKATTEETATEN -> "Oppgitt av skatteetaten"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.OPPGITT_AV_BRUKER -> "Opplyst av deg"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.NAV -> "NAV"
            }
    }

    object LocalizedInntektType : LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType>() {
        // TODO flere språk
        override fun apply(first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType, second: Language): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.ARBEIDSINNTEKT -> "Arbeidsinntekt"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.UTLANDSINNTEKT -> "Utlandsinntekt"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.NAERINGSINNTEKT -> "Næringsinntekt"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.UFOERETRYGD -> "Uføretrygd"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.ANDRE_PENSJONER_OG_YTELSER -> "Pensjon fra andre enn NAV"
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.FORVENTET_PENSJON_FRA_UTLANDET -> "Pensjon fra utlandet"
            }
    }
}