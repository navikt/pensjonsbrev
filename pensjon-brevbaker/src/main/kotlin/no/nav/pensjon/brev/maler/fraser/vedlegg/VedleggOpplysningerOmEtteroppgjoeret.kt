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
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

data class Introduksjon(val periode: Expression<Year>) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Vi bruker opplysningene som du selv legger inn som inntekt på ${Constants.INNTEKTSPLANLEGGEREN_URL}, og opplysninger fra Skatteetaten. ".expr() +
                        "Vi har gjort en ny beregning av uføretrygden din for " + periode.format() + " etter opplysninger fra Skatteetaten. " +
                        "Du kan se skatteoppgjøret ditt på ${Constants.SKATTEETATEN_URL}.",
                Nynorsk to "Vi nyttar opplysningane som du legg sjølv inn som inntekt på ${Constants.INNTEKTSPLANLEGGEREN_URL}, og opplysningar frå Skatteetaten. ".expr() +
                        "Vi har gjort ei ny utrekning av uføretrygda di for " + periode.format() + " etter opplysningar frå Skatteetaten." +
                        "Du kan sjå skatteoppgjeret ditt på ${Constants.SKATTEETATEN_URL}.",
            )
        }
        paragraph {
            text(
                Bokmal to "Husk at du må melde fra til oss innen 3 uker hvis du mener beregningene feil.",
                Nynorsk to "Hugs at du må melde frå til oss innan 3 veker om du meiner berekningane er feil",
            )
        }
    }
}

data class FikkSkulleFaattTabell(
    val periode: Expression<Year>,
    val harFaattForMye: Expression<Boolean>,
    val harGjenlevendeTillegg: Expression<Boolean>,
    val ufoeretrygd: Expression<OpplysningerOmEtteroppgjoeretDto.AvviksResultat>,
    val barnetillegg: Expression<OpplysningerOmEtteroppgjoeretDto.Barnetillegg?>,
    val totaltAvvik: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            textExpr(
                Bokmal to "Hva du fikk utbetalt og hva du skulle fått utbetalt i ".expr() + periode.format(),
                Nynorsk to "Kva du fekk utbetalt og kva du skulle ha fått utbetalt i ".expr() + periode.format(),
            )
        }
        paragraph {
            table(
                header = {
                    column(columnSpan = 5) {
                        text(
                            Bokmal to "Type stønad",
                            Nynorsk to "Type stønad",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    column(columnSpan = 4, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Dette skulle du fått",
                            Nynorsk to "Dette skulle du fått",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    column(columnSpan = 4, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Dette fikk du",
                            Nynorsk to "Dette fekk du",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    column(columnSpan = 4, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                        text(
                            Bokmal to "Avviksbeløp",
                            Nynorsk to "Avviksbeløp",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                }
            ) {
                fun avviksResultatRad(
                    typeStoenad: TextElement<LangBokmalNynorsk>,
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
                                    Nynorsk to it.skulleFaatt.format() + " kr",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to it.fikk.format() + " kr",
                                    Nynorsk to it.fikk.format() + " kr",
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to it.avvik.format() + " kr",
                                    Nynorsk to it.avvik.format() + " kr",
                                )
                            }
                        }
                    }
                }
                avviksResultatRad(
                    newTextExpr(
                        Bokmal to "Uføretrygd".expr() + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegg", ""),
                        Nynorsk to "Uføretrygd".expr() + ifElse(harGjenlevendeTillegg, " og attlevandetillegg", ""),
                    ),
                    ufoeretrygd,
                )
                avviksResultatRad(
                    newText(
                        Bokmal to "Barnetillegg særkullsbarn",
                        Nynorsk to "Barnetillegg særkullsbarn",
                    ),
                    barnetillegg.saerkull_safe.resultat_safe,
                )
                avviksResultatRad(
                    newText(
                        Bokmal to "Barnetillegg fellesbarn",
                        Nynorsk to "Barnetillegg fellesbarn",
                    ),
                    barnetillegg.felles_safe.resultat_safe,
                )
                row {
                    cell {
                        textExpr(
                            Bokmal to "Beløp du har fått for ".expr() + ifElse(harFaattForMye, "mye", "lite"),
                            Nynorsk to "Belop du har fått for ".expr() + ifElse(harFaattForMye, "mykje", "lite"),
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        )
                    }
                    cell { }
                    cell { }
                    cell {
                        textExpr(
                            Bokmal to totaltAvvik.format() + " kr",
                            Nynorsk to totaltAvvik.format() + " kr",
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
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Du har fått ".expr() + totaltAvvik.absoluteValue().format() + " kroner for " +
                        ifElse(harFaattForMye, "mye", "lite") + " i uføretrygd",
                Nynorsk to "Du har fått ".expr() + totaltAvvik.absoluteValue().format() + " kroner for " +
                        ifElse(harFaattForMye, "mykje", "lite") + " i uføretrygd",
            )
            showIf(harBarnetillegg and harGjenlevendeTillegg) {
                text(
                    Bokmal to ", barnetillegg og gjenlevendetillegg",
                    Nynorsk to ", barnetillegg og attlevandetillegg",
                )
            }.orShowIf(harBarnetillegg) {
                text(
                    Bokmal to " og barnetillegg",
                    Nynorsk to " og barnetillegg",
                )
            }.orShowIf(harGjenlevendeTillegg) {
                text(
                    Bokmal to " og gjenlevendetillegg",
                    Nynorsk to " og attlevandetillegg"
                )
            }
            textExpr(
                Bokmal to " i ".expr() + periode.format() + ".",
                Nynorsk to " i ".expr() + periode.format() + ".",
            )
        }
    }
}

data class OmBeregningAvBarnetillegg(
    val barnetillegg: Expression<OpplysningerOmEtteroppgjoeretDto.Barnetillegg>,
    val periode: Expression<Year>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                Bokmal to "Om beregning av barnetillegg",
                Nynorsk to "Om utrekning av barnetillegg",
            )
        }
        paragraph {
            text(
                Bokmal to "Det er personinntekt som avgjør hvor mye du får i barnetillegg. Dette står i §12-2 i skatteloven. Personinntekter omfatter:",
                Nynorsk to "Det er personinntekta som avgjer kor mykje du får i barnetillegg. Dette står i §12-2 i skattelova. Personinntekter omfattar:",
            )
            list {
                item {
                    text(
                        Bokmal to "pensjonsgivende inntekt",
                        Nynorsk to "pensjonsgivande inntekt",
                    )
                }
                item {
                    text(
                        Bokmal to "uføretrygd",
                        Nynorsk to "uføretrygd",
                    )
                }
                item {
                    text(
                        Bokmal to "alderspensjon fra folketrygden",
                        Nynorsk to "alderspensjon frå folketrygda",
                    )
                }
                item {
                    text(
                        Bokmal to "andre pensjoner og ytelser, også fra utlandet",
                        Nynorsk to "andre pensjonar og ytingar, også frå utlandet"
                    )
                }
            }
            text(
                Bokmal to "Hvis personinntekten din overstiger et visst beløp (fribeløp), blir barnetillegget redusert eller falle helt bort. " +
                        "Fikk du innvilget barnetillegg i løpet av året, eller barnetillegget opphørte i løpet av året" +
                        ", er det bare inntekten for perioden med rett til barnetillegg som har betydning.",
                Nynorsk to "Dersom personinntekta di er over eit visst beløp (fribeløp), vil barnetillegget bli redusert eller falle bort heilt. " +
                        "Viss barnetillegg blei innvilga eller avvikla i løpet av året, vil det berre vere inntekta for perioden med rett til " +
                        "barnetillegg som har betydning.",
            )
        }

        ifNotNull(barnetillegg.felles) { fellesbarn ->
            title2 {
                text(
                    Bokmal to "For barn som bor sammen med begge foreldrene:",
                    Nynorsk to "For barn som bur saman med begge foreldra:",
                )
            }
            paragraph {
                list {
                    item {
                        textExpr(
                            Bokmal to "Barnetillegget beregnes ut fra inntekten til deg og din ".expr() + fellesbarn.sivilstand.ubestemtForm() + ".",
                            Nynorsk to "Barnetillegget blir rekna ut med utgangspunkt i di eiga inntekt og inntekta til ".expr() + fellesbarn.sivilstand.ubestemtForm() + ".",
                        )
                    }
                    item {
                        textExpr(
                            Bokmal to "Fribeløpet er 4,6 ganger folketrygdens grunnbeløp. I ".expr() + periode.format() + " var fribeløpet " + fellesbarn.fribeloep.format() + " kroner.",
                            Nynorsk to "Fribeløpet er 4,6 gonger grunnbeløpet i folketrygda. I ".expr() + periode.format() + " var fribeløpet " + fellesbarn.fribeloep.format() + " kroner.",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Fribeløpet øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.",
                            Nynorsk to "Fribeløpet aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn.",
                        )
                    }
                    showIf(barnetillegg.mindreEnn40AarTrygdetid) {
                        item {
                            text(
                                Bokmal to "Fribeløpet blir redusert ut fra trygdetiden du har.",
                                Nynorsk to "Fribeløpet blir redusert ut frå trygdetida du har.",
                            )
                        }
                    }
                    item {
                        text(
                            Bokmal to "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet.",
                            Nynorsk to "Barnetillegget blir redusert med 50 prosent av inntekta som er over fribeløpet.",
                        )
                    }
                }
            }
        }

        ifNotNull(barnetillegg.saerkull) { saerkull ->
            title2 {
                text(
                    Bokmal to "For barn som ikke bor sammen med begge foreldrene:",
                    Nynorsk to "For barn som ikkje bur saman med begge foreldra:",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Bokmal to "Barnetillegget beregnes ut fra inntekten din.",
                            Nynorsk to "Barnetillegget blir rekna ut på grunnlag av inntekta di.",
                        )
                    }
                    item {
                        textExpr(
                            Bokmal to "Fribeløpet er 3,1 ganger folketrygdens grunnbeløp. I ".expr() + periode.format() + " var fribeløpet " + saerkull.fribeloep.format() + " kroner.",
                            Nynorsk to "Fribeløpet er 3,1 gongar grunnbeløpet i folketrygda. I ".expr() + periode.format() + " var fribeløpet " + saerkull.fribeloep.format() + " kroner.",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Fribeløpet øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn.",
                            Nynorsk to "Fribeløpet aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn.",
                        )
                    }
                    showIf(barnetillegg.mindreEnn40AarTrygdetid) {
                        item {
                            text(
                                Bokmal to "Fribeløpet blir redusert ut fra trygdetiden du har.",
                                Nynorsk to "Fribeløpet blir redusert ut frå trygdetida du har.",
                            )
                        }
                    }
                    item {
                        text(
                            Bokmal to "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet.",
                            Nynorsk to "Barnetillegget blir redusert med 50 prosent av inntekta som er over fribeløpet.",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Inntekten til en ektefelle/parner/samboer som ikke er forelder for barnet, har ingen betydning.",
                            Nynorsk to "Inntekta til ein ektefelle/partnar/sambuer som ikkje er forelder til barnet, har inga betydning.",
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
                Nynorsk to "Når vi reknar ut barnetillegg, byrjar vi med å oppdatere kor mykje du skulle hatt i uføretrygd. ".expr() +
                        "Etter denne utrekninga, blir barnetillegget ditt " + skulleFaatt.format() + " kroner for " + periode.format() + ".",
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Du har fått utbetalt ".expr() + barnetillegg.totaltResultat.fikk.format() + " kroner i barnetillegg. Du har fått " +
                        barnetillegg.totaltResultat.avvik.absoluteValue().format() + " kroner for " +
                        ifElse(barnetillegg.totaltResultat.harFaattForMye, "mye", "lite") + " i barnetillegg.",
                Nynorsk to "Du har fått utbetalt ".expr() + barnetillegg.totaltResultat.fikk.format() + " kroner i barnetillegg. Du har fått " +
                        barnetillegg.totaltResultat.avvik.absoluteValue().format() + " kroner for " +
                        ifElse(barnetillegg.totaltResultat.harFaattForMye, "mykje", "lite") + " i barnetillegg.",
            )
        }

        title2 {
            text(
                Bokmal to "Beregningene er gjort hver for seg hvis:",
                Nynorsk to "Utrekningane er gjort kvar for seg viss:",
            )
        }
        paragraph {
            list {
                item {
                    text(
                        Bokmal to "du har flere barn som har ulike bosituasjoner.",
                        Nynorsk to "du har fleire barn som har ulike busituasjoner.",
                    )
                }
                item {
                    text(
                        Bokmal to "barnet bor med begge foreldre i deler av året, og en av foreldrene resten av året.",
                        Nynorsk to "barnet bur med begge foreldra delar av året, og en av foreldra resten av året.",
                    )
                }
            }
        }

        val harFellesTillegg = barnetillegg.felles.notNull()

        paragraph {
            textExpr(
                Bokmal to "Tabellene under viser inntektene du".expr() + ifElse(harFellesTillegg, " og annen forelder", "") +
                        " har hatt i " + periode.format() + ". Det er disse inntektene vi har brukt for å beregne barnetillegget.",
                Nynorsk to "Tabellene under viser inntektene du".expr() + ifElse(harFellesTillegg, " og anna forelder", "") +
                        " har hatt i " + periode.format() + ". Det er desse inntektene vi har brukt for å rekne ut barnetillegget.",
            )
        }

        title2 {
            text(
                Bokmal to "Din personinntekt",
                Nynorsk to "Personinntekta di",
            )
        }
        paragraph {
            includePhrase(
                InntektTabell(
                    barnetillegg.personinntekt.inntekt,
                    newText(
                        Bokmal to "Total personinntekt",
                        Nynorsk to "Samla personinntekt",
                        fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                    ),
                )
            )
        }

        title2 {
            text(
                Bokmal to "Beløp som er trukket fra personinntekten din",
                Nynorsk to "Beløp som er trekt frå personinntekta di",
            )
        }
        paragraph {
            showIf(barnetillegg.personinntekt.fratrekk.fratrekk.isNotEmpty()) {
                includePhrase(
                    FratrekkTabell(
                        barnetillegg.personinntekt.fratrekk,
                        newText(
                            Bokmal to "Totalbeløp som er trukket fra personinntekten din",
                            Nynorsk to "Totalbeløp som er trekt frå personinntekta di",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        ),
                    )
                )
            } orShow {
                textExpr(
                    Bokmal to "Du har ikke hatt inntekter som er trukket fra personinntekten din i ".expr() + periode.format() +
                            ". Hvis du har hatt inntekter som kan trekkes fra, må du sende oss dokumentasjon på det innen 3 uker.",
                    Nynorsk to "Du har ikkje hatt inntekter som er trekte frå personinntekta di i ".expr() + periode.format() +
                            ". Dersom du har hatt inntekter som kan trekkjast frå, må du sende oss dokumentasjon på dette innan 3 veker.",
                )
            }
        }

        ifNotNull(barnetillegg.felles) { fellesbarn ->
            title2 {
                text(
                    Bokmal to "Personinntekt til annen forelder",
                    Nynorsk to "Personinntekta til den andre forelderen",
                )
            }
            paragraph {
                includePhrase(
                    InntektTabell(
                        fellesbarn.personinntektAnnenForelder.inntekt,
                        newText(
                            Bokmal to "Total personinntekt til annen forelder",
                            Nynorsk to "Den samla personinntekta til den andre forelderen",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        ),
                    )
                )
            }
            paragraph {
                text(
                    Bokmal to "Mottar annen forelder uføretrygd eller alderspensjon fra NAV, regnes dette også med som personinntekt.",
                    Nynorsk to "Mottar den andre forelderen uføretrygd eller alderspensjon frå NAV, blir dette også rekna som personinntekt.",
                )
            }

            showIf(fellesbarn.personinntektAnnenForelder.fratrekk.fratrekk.isNotEmpty()) {
                title2 {
                    text(
                        Bokmal to "Beløp som er trukket fra annen forelder sin personinntekt",
                        Nynorsk to "Beløp som er trekt frå personinntekta til den andre forelderen",
                    )
                }
                paragraph {
                    includePhrase(
                        FratrekkTabell(
                            fellesbarn.personinntektAnnenForelder.fratrekk,
                            newText(
                                Bokmal to "Totalbeløp som er trukket fra personinntekten til annen forelder",
                                Nynorsk to "Totalbeløp som er trekt frå personinntekta til den andre forelderen",
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
                            Nynorsk to "Folketrygdens grunnbeløp på inntil ".expr() + fellesbarn.grunnbelop.format() + " kroner er halde utanfor inntekta til den andre forelderen.",
                        )
                    }
                }
            } orShow {
                paragraph {
                    textExpr(
                        Bokmal to "Annen forelder har ikke hatt inntekt som er trukket fra sin personinntekt i ".expr() + periode.format() + ".",
                        Nynorsk to "Den andre forelderen har ikkje hatt inntekt som er trekt frå sin personinntekt i ".expr() + periode.format() + ".",
                    )
                }
            }

        }

        ifNotNull(barnetillegg.saerkull) { saerkull ->
            paragraph {
                textExpr(
                    Bokmal to "Du hadde for høy samlet inntekt i ".expr() + periode.format() + " for å få utbetalt barnetillegg for særkullsbarn. " +
                            "Sum av samlet inntekt som gjør at barnetillegget ikke blir utbetalt var " + saerkull.samletInntekt.format() + " kroner. " +
                            "Inntektstaket for å få utbetalt barnetillegg for særkullsbarn var " + saerkull.inntektstakSamletInntekt.format() + " kroner.",
                    Nynorsk to "Du hadde for høg samla inntekt i ".expr() + periode.format() + " til å få utbetalt barnetillegg for særkullsbarn. " +
                            "Summen av den samla inntekta som gjer at barnetillegget ikkje blir utbetalt, var " + saerkull.samletInntekt.format() + " kroner. " +
                            "Inntektstaket for å få utbetalt barnetillegg for særkullsbarn var " + saerkull.inntektstakSamletInntekt.format() + " kroner.",
                )
            }
        }

        ifNotNull(barnetillegg.felles) { fellesbarn ->
            paragraph {
                textExpr(
                    Bokmal to "Dere hadde for høy samlet inntekt i ".expr() + periode.format() + " for å få utbetalt barnetillegg for fellesbarn. " +
                            "Sum av samlet inntekt som gjør at barnetillegget ikke blir utbetalt var " + fellesbarn.samletInntekt.format() + " kroner. " +
                            "Inntektstaket for å få utbetalt barnetillegg for fellessbarn var " + fellesbarn.inntektstakSamletInntekt.format() + " kroner.",
                    Nynorsk to "Dere hadde for høg samla inntekt i ".expr() + periode.format() + " til å få utbetalt barnetillegg for fellesbarn. " +
                            "Summen av den samla inntekta som gjer at barnetillegget ikkje blir utbetalt, var " + fellesbarn.samletInntekt.format() + " kroner. " +
                            "Inntektstaket for å få utbetalt barnetillegg for fellesbarn var " + fellesbarn.inntektstakSamletInntekt.format() + " kroner.",
                )
            }
        }
    }
}

data class OmBeregningAvUfoeretrygd(
    val harGjenlevendeTillegg: Expression<Boolean>,
    val pensjonsgivendeInntekt: Expression<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk>,
    val periode: Expression<Year>,
    val pensjonsgivendeInntektBruktIBeregningen: Expression<Kroner>,
    val ufoeretrygd: Expression<OpplysningerOmEtteroppgjoeretDto.AvviksResultat>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            textExpr(
                Bokmal to "Om beregningen av uføretrygd".expr() + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegg", ""),
                Nynorsk to "Om utrekning av uføretrygd".expr() + ifElse(harGjenlevendeTillegg, " og attlevandetillegg", ""),
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Det er pensjonsgivende inntekt som avgjør hvor mye du får i uføretrygd".expr()
                        + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegg", "")
                        + ". Dette står i § 3-15 i folketrygdloven. Pensjonsgivende inntekt er blant annet:",
                Nynorsk to "Det er pensjonsgivande inntekt som avgjer kor mykje du får i uføretrygd".expr()
                        + ifElse(harGjenlevendeTillegg, " og attlevandetillegg", "")
                        + ". Dette står i § 3-15 i folketrygdlova. Døme på pensjonsgivande inntekt:",
            )
            list {
                item {
                    text(
                        Bokmal to "brutto lønnsinntekt fra Norge inkludert feriepenger",
                        Nynorsk to "brutto lønsinntekt frå Noreg, inkludert feriepengar",
                    )
                }
                item {
                    text(
                        Bokmal to "lønnsinntekt fra utlandet",
                        Nynorsk to "lønsinntekt frå utlandet",
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt fra selvstendig næringsvirksomhet",
                        Nynorsk to "inntekt frå sjølvstendig næringsverksemd",
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt som fosterforelder",
                        Nynorsk to "inntekt som fosterforelder",
                    )
                }
                item {
                    text(
                        Bokmal to "omsorgslønn",
                        Nynorsk to "omsorgsløn",
                    )
                }
            }

            val inntektFoerFratrekk = pensjonsgivendeInntekt.inntekt.sum.format()
            textExpr(
                Bokmal to "Din pensjonsgivende inntekt har i ".expr() + periode.format() + " vært " + inntektFoerFratrekk + " kroner.",
                Nynorsk to "Den pensjonsgivande inntekta di i ".expr() + periode.format() + " var " + inntektFoerFratrekk + " kroner.",
            )
        }

        paragraph {
            text(
                Bokmal to "Hva kan bli trukket fra den pensjonsgivende inntekten din?",
                Nynorsk to "Kva kan trekkjast frå den pensjonsgivande inntekta?",
            )
            list {
                item {
                    text(
                        Bokmal to "inntekt før du ble uføretrygdet",
                        Nynorsk to "inntekt før du blei uføretrygda",
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt etter at uføretrygden din opphørte",
                        Nynorsk to "inntekt etter at uføretrygda blei avvikla",
                    )
                }
                item {
                    text(
                        Bokmal to "erstatning for inntektstap (erstatningsoppgjør)",
                        Nynorsk to "erstatning for inntektstap (erstatningsoppgjer)",
                    )
                }
                item {
                    text(
                        Bokmal to "inntekt fra helt avsluttet arbeid eller virksomhet",
                        Nynorsk to "inntekt frå heilt avslutta arbeid eller verksemd",
                    )
                }
                item {
                    text(
                        Bokmal to "etterbetaling du har fått fra NAV",
                        Nynorsk to "etterbetalingar du har fått frå NAV",
                    )
                }
            }

            val inntektEtterFratrekk = pensjonsgivendeInntektBruktIBeregningen.format()
            textExpr(
                Bokmal to "Etter beregningen er gjort, har du ".expr() + inntektEtterFratrekk + " kroner i pensjonsgivende inntekt.",
                Nynorsk to "Utrekninga vår viser at du har ".expr() + inntektEtterFratrekk + " kroner i pensjonsgivande inntekt.",
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Du skulle ha fått ".expr() + ufoeretrygd.skulleFaatt.format() + " kroner i uføretrygd i " + periode.format()
                        + ". Du fikk imidlertid " + ufoeretrygd.fikk.format() + " kroner. Du har derfor fått " + ufoeretrygd.avvik.absoluteValue().format()
                        + " kroner for " + ifElse(ufoeretrygd.harFaattForMye, "mye", "lite") + " i uføretrygd.",
                Nynorsk to "Du skulle ha fått ".expr() + ufoeretrygd.skulleFaatt.format() + " kroner i uføretrygd i " + periode.format()
                        + ". Du fekk derimot " + ufoeretrygd.fikk.format() + " kroner. Du har derfor fått " + ufoeretrygd.avvik.absoluteValue().format()
                        + " kroner for " + ifElse(ufoeretrygd.harFaattForMye, "mykje", "lite") + " i uføretrygd.",
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Tabellene under viser inntektene du har hatt i ".expr() + periode.format()
                        + ". Det er disse inntektene vi har brukt for å beregne uføretrygden din"
                        + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegget ditt.", "."),
                Nynorsk to "Tabellane under viser inntektene du har hatt i løpet av ".expr() + periode.format()
                        + ". Det er desse inntektene vi har nytta for å berekne uføretrygda di"
                        + ifElse(harGjenlevendeTillegg, " og attlevandetillegget ditt.", "."),
            )
        }

        title1 {
            text(
                Bokmal to "Din pensjonsgivende inntekt",
                Nynorsk to "Di pensjonsgivande inntekt"
            )
        }
        showIf(pensjonsgivendeInntekt.inntekt.inntekter.isNotEmpty()) {
            paragraph {
                includePhrase(
                    InntektTabell(
                        pensjonsgivendeInntekt.inntekt,
                        newText(
                            Bokmal to "Total pensjonsgivende inntekt",
                            Nynorsk to "Samla pensjonsgivande inntekt",
                            fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                        ),
                    ),
                )
            }

            title1 {
                text(
                    Bokmal to "Beløp som er trukket fra den pensjonsgivende inntekten din",
                    Nynorsk to "Beløp som er trekt frå den pensjonsgivande inntekta di",
                )
            }
            paragraph {
                showIf(pensjonsgivendeInntekt.fratrekk.fratrekk.isNotEmpty()) {
                    includePhrase(
                        FratrekkTabell(
                            pensjonsgivendeInntekt.fratrekk,
                            newText(
                                Bokmal to "Totalbeløp som er trukket fra",
                                Nynorsk to "Totalbeløp som er trekt frå",
                                fontType = Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                            )
                        )
                    )
                } orShow {
                    textExpr(
                        Bokmal to "Du har ikke hatt inntekter som er trukket fra den pensjonsgivende inntekten din i ".expr()
                                + periode.format() + ". Hvis du har hatt inntekter som kan trekkes fra, må du sende oss dokumentasjon på det innen 3 uker.",
                        Nynorsk to "Du har ikkje hatt inntekter som er trekte frå den pensjonsgivande inntekta di i ".expr()
                                + periode.format() + ". Dersom du har hatt inntekter som kan trekkjast frå, må du sende oss dokumentasjon på dette innan 3 veker.",
                    )
                }
            }
        } orShow {
            paragraph {
                textExpr(
                    Bokmal to "Du har ikke hatt pensjonsgivende inntekt i ".expr() + periode.format() + ".",
                    Nynorsk to "Du har ikkje hatt pensjonsgivande inntekt i ".expr() + periode.format() + ".",
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

data class FratrekkTabell(val fratrekk: Expression<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk>, val sumText: TextElement<LangBokmalNynorsk>) :
    ParagraphPhrase<LangBokmalNynorsk>() {

    override fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.template() =
        table(
            header = {
                column(columnSpan = 3) {
                    text(
                        Bokmal to "Type inntekt",
                        Nynorsk to "Type inntekt",
                    )
                }
                column(columnSpan = 3) {
                    text(
                        Bokmal to "Årsak til trekk",
                        Nynorsk to "Årsak til trekk",
                    )
                }
                column(columnSpan = 2, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(
                        Bokmal to "Beløp",
                        Nynorsk to "Beløp",
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
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.FOER_INNVILGET_UFOERETRYGD -> when(second) {
                    Bokmal -> "Inntekt før uføretrygden ble innvilget"
                    Nynorsk -> "Inntekt før uføretrygda vart innvilga"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ETTER_OPPHOERT_UFOERETRYGD -> when(second) {
                    Bokmal -> "Inntekt etter at uføretrygden opphørte"
                    Nynorsk -> "Inntekt etter at uføretrygda var avslutta"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ERSTATNING_INNTEKTSTAP_ERSTATNINGSOPPGJOER -> when(second) {
                    Bokmal -> "Erstatning for inntektstap ved erstatningsoppgjør"
                    Nynorsk -> "Erstatning for inntektstap ved erstatningsoppgjer"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ETTERSLEP_AVSLUTTET_ARBEID_ELLER_VIRKSOMHET -> when(second) {
                    Bokmal -> "Inntekt fra helt avsluttet arbeid eller virksomhet"
                    Nynorsk -> "Inntekt frå heilt avslutta arbeid eller verksemd"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ANNET -> when(second) {
                    Bokmal -> "Annet"
                    Nynorsk -> "Anna"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ETTERBETALING_FRA_NAV -> when(second) {
                    Bokmal -> "Etterbetaling fra NAV"
                    Nynorsk -> "Etterbetaling frå NAV"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.INNTEKT_INNTIL_1G -> when(second) {
                    Bokmal -> "Inntekt inntil ett grunnbeløp"
                    Nynorsk -> "Inntekt inntil eit grunnbeløp"
                    English -> TODO()
                }
            }
    }

    object LocalizedFratrekkType : LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType>() {
        // TODO Flere språk
        override fun apply(first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType, second: Language): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.ARBEIDSINNTEKT -> when(second) {
                    Bokmal -> "Arbeidsinntekt"
                    Nynorsk -> "Arbeidsinntekt"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.UTLANDSINNTEKT -> when(second) {
                    Bokmal -> "Utlandsinntekt"
                    Nynorsk -> "Utlandsinntekt"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.NAERINGSINNTEKT -> when(second) {
                    Bokmal -> "Næringsinntekt"
                    Nynorsk -> "Næringsinntekt"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.ANDRE_PENSJONER_OG_YTELSER -> when(second) {
                    Bokmal -> "Pensjon fra andre enn NAV"
                    Nynorsk -> "Pensjonar frå andre enn NAV"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.FORVENTET_PENSJON_FRA_UTLANDET -> when(second) {
                    Bokmal -> "Pensjon fra utlandet"
                    Nynorsk -> "Pensjonar frå utlandet"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.INNTEKT -> when(second) {
                    Bokmal -> "Inntekt"
                    Nynorsk -> "Inntekt"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.FRATREKKBAR_INNTEKT -> when(second) {
                    Bokmal -> "Inntekt som kan trekkes fra"
                    Nynorsk -> "Inntekt som kan trekkjast frå"
                    English -> TODO()
                }
            }

    }
}

data class InntektTabell(val inntekt: Expression<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt>, val sumText: TextElement<LangBokmalNynorsk>) :
    ParagraphPhrase<LangBokmalNynorsk>() {

    override fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.template() =
        table(
            header = {
                column(columnSpan = 3) {
                    text(
                        Bokmal to "Type inntekt",
                        Nynorsk to "Type inntekt",
                    )
                }
                column(columnSpan = 3) {
                    text(
                        Bokmal to "Mottatt av",
                        Nynorsk to "Motteke av",
                    )
                }
                column(columnSpan = 2, alignment = Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT) {
                    text(
                        Bokmal to "Registrert inntekt",
                        Nynorsk to "Registrert inntekt",
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
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.INNMELDT_AV_ARBEIDSGIVER -> when(second) {
                    Bokmal -> "Elektronisk innmeldt fra arbeidsgiver"
                    Nynorsk -> "Elektronisk meldt inn frå arbeidsgivar"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.OPPGITT_AV_SKATTEETATEN -> when(second) {
                    Bokmal -> "Oppgitt av skatteetaten"
                    Nynorsk -> "Oppgjeva av skatteetaten"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.OPPGITT_AV_BRUKER -> when(second) {
                    Bokmal -> "Opplyst av deg"
                    Nynorsk -> "Opplyst av deg"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.NAV -> when(second) {
                    Bokmal -> "NAV"
                    Nynorsk -> "NAV"
                    English -> TODO()
                }
            }
    }

    object LocalizedInntektType : LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType>() {
        // TODO flere språk
        override fun apply(first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType, second: Language): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.ARBEIDSINNTEKT -> when(second) {
                    Bokmal -> "Arbeidsinntekt"
                    Nynorsk -> "Arbeidsinntekt"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.UTLANDSINNTEKT -> when(second) {
                    Bokmal -> "Utlandsinntekt"
                    Nynorsk -> "Utlandsinntekt"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.NAERINGSINNTEKT -> when(second) {
                    Bokmal -> "Næringsinntekt"
                    Nynorsk -> "Næringsinntekt"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.UFOERETRYGD -> when(second) {
                    Bokmal -> "Uføretrygd"
                    Nynorsk -> "Uføretrygd"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.ANDRE_PENSJONER_OG_YTELSER -> when(second) {
                    Bokmal -> "Pensjon fra andre enn NAV"
                    Nynorsk -> "Pensjonar frå andre enn NAV"
                    English -> TODO()
                }
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.FORVENTET_PENSJON_FRA_UTLANDET -> when(second) {
                    Bokmal -> "Pensjon fra utlandet"
                    Nynorsk -> "Pensjonar frå utlandet"
                    English -> TODO()
                }
            }
    }
}