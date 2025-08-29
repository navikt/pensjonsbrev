package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.AvviksResultatSelectors.avvik
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.AvviksResultatSelectors.fikk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.AvviksResultatSelectors.skulleFaatt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.grunnbelop
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.harSamletInntektOverInntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.inntektstakSamletInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.personinntektAnnenForelder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.resultat
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.resultat_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.samletInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.FellesbarnSelectors.sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.SaerkullsbarnSelectors.fribeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.SaerkullsbarnSelectors.harSamletInntektOverInntektstak
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
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.model.ubestemtForm
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

data class Introduksjon(val periode: Expression<Year>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Vi bruker opplysningene som du selv legger inn som inntekt på ${Constants.INNTEKTSPLANLEGGEREN_URL}, og opplysninger fra Skatteetaten. " +
                        "Vi har gjort en ny beregning av uføretrygden din for " + periode.format() + " etter opplysninger fra Skatteetaten. " +
                        "Du kan se skatteoppgjøret ditt på ${Constants.SKATTEETATEN_URL}." },

                nynorsk { + "Vi nyttar opplysningane som du legg inn sjølv som inntekt på ${Constants.INNTEKTSPLANLEGGEREN_URL}, og opplysningar frå Skatteetaten. " +
                        "Vi har gjort ei ny utrekning av uføretrygda di for " + periode.format() + " etter opplysningar frå Skatteetaten. " +
                        "Du kan sjå skatteoppgjeret ditt på ${Constants.SKATTEETATEN_URL}." },

                english { + "We use the income information you have registered on ${Constants.INNTEKTSPLANLEGGEREN_URL} and information from the Norwegian Tax Administration. " +
                        "Your disability benefit for " + periode.format() + " has been recalculated based on the tax settlement provided by the Norwegian Tax Administration. " +
                        "You can find your tax settlement at ${Constants.SKATTEETATEN_URL}." },
            )
        }
        paragraph {
            text(
                bokmal { + "Husk at du må melde fra til oss innen 3 uker hvis du mener beregningene er feil." },
                nynorsk { + "Hugs at du må melde frå til oss innan 3 veker om du meiner berekningane er feil." },
                english { + "Please inform us within 3 weeks if you believe the calculations are incorrect." },
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
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Hva du fikk utbetalt og hva du skulle fått utbetalt i " + periode.format() },
                nynorsk { + "Kva du fekk utbetalt og kva du skulle ha fått utbetalt i " + periode.format() },
                english { + "What you received and what you should have received in " + periode.format() },
            )
        }
        paragraph {
            table(
                header = {
                    column(columnSpan = 5) {
                        text(
                            bokmal { + "Type stønad" },
                            nynorsk { + "Type stønad" },
                            english { + "Type of benefit" },
                        )
                    }
                    column(columnSpan = 4, alignment = ColumnAlignment.RIGHT) {
                        text(
                            bokmal { + "Dette skulle du fått" },
                            nynorsk { + "Dette skulle du fått" },
                            english { + "You should have received" },
                        )
                    }
                    column(columnSpan = 4, alignment = ColumnAlignment.RIGHT) {
                        text(
                            bokmal { + "Dette fikk du" },
                            nynorsk { + "Dette fekk du" },
                            english { + "You received" },
                        )
                    }
                    column(columnSpan = 4, alignment = ColumnAlignment.RIGHT) {
                        text(
                            bokmal { + "Avviksbeløp" },
                            nynorsk { + "Avviksbeløp" },
                            english { + "Deviation amount" },
                        )
                    }
                }
            ) {
                fun avviksResultatRad(
                    typeStoenad: TextElement<LangBokmalNynorskEnglish>,
                    resultat: Expression<OpplysningerOmEtteroppgjoeretDto.AvviksResultat?>
                ) {
                    ifNotNull(resultat) {
                        row {
                            cell { addTextContent(typeStoenad) }
                            cell { includePhrase(KronerText(it.skulleFaatt)) }
                            cell { includePhrase(KronerText(it.fikk)) }
                            cell { includePhrase(KronerText(it.avvik)) }
                        }
                    }
                }
                avviksResultatRad(
                    newTextExpr(
                        Bokmal to "Uføretrygd".expr() + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegg", ""),
                        Nynorsk to "Uføretrygd".expr() + ifElse(harGjenlevendeTillegg, " og attlevandetillegg", ""),
                        English to "Disability benefit".expr() + ifElse(
                            harGjenlevendeTillegg,
                            " and survivor supplement",
                            ""
                        ),
                    ),
                    ufoeretrygd,
                )
                avviksResultatRad(
                    newText(
                        Bokmal to "Barnetillegg særkullsbarn",
                        Nynorsk to "Barnetillegg særkullsbarn",
                        English to "Child supplement, children from a previous relationship"
                    ),
                    barnetillegg.saerkull_safe.resultat_safe,
                )
                avviksResultatRad(
                    newText(
                        Bokmal to "Barnetillegg fellesbarn",
                        Nynorsk to "Barnetillegg fellesbarn",
                        English to "Child supplement, joint children"
                    ),
                    barnetillegg.felles_safe.resultat_safe,
                )
                row {
                    cell {
                        text(
                            bokmal { + "Beløp du har fått for " + ifElse(harFaattForMye, "mye", "lite") },
                            nynorsk { + "Beløp du har fått for " + ifElse(harFaattForMye, "mykje", "lite") },
                            english { + "Amount you were paid too " + ifElse(harFaattForMye, "much", "little") },
                            fontType = FontType.BOLD,
                        )
                    }
                    cell { }
                    cell { }
                    cell { includePhrase(KronerText(totaltAvvik, FontType.BOLD)) }
                }
            }
        }
    }
}

data class DuHarFaattAvviksBeloep(
    val totaltAvvik: Expression<Kroner>,
    val harFaattForMye: Expression<Boolean>,
    val periode: Expression<Year>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Du fikk utbetalt " + totaltAvvik.absoluteValue().format() + " for " +
                        ifElse(harFaattForMye, "mye", "lite") },
                nynorsk { + "Du fekk utbetalt " + totaltAvvik.absoluteValue().format() + " for " +
                        ifElse(harFaattForMye, "mykje", "lite") },
                english { + "You have received " + totaltAvvik.absoluteValue().format() + " too " +
                        ifElse(harFaattForMye, "much", "little") },
            )
            text(
                bokmal { + " i " + periode.format() + "." },
                nynorsk { + " i " + periode.format() + "." },
                english { + " in " + periode.format() + "." },
            )
        }
    }
}

data class OmBeregningAvBarnetillegg(
    val barnetillegg: Expression<OpplysningerOmEtteroppgjoeretDto.Barnetillegg>,
    val periode: Expression<Year>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Om beregning av barnetillegg for " + periode.format() },
                nynorsk { + "Om utrekning av barnetillegg for " + periode.format() },
                english { + "Child supplement calculation for " + periode.format() },
            )
        }
        paragraph {
            val skulleFaatt = barnetillegg.totaltResultat.skulleFaatt
            text(
                bokmal { + "Ved beregning av barnetillegg har vi først oppdatert hvor mye du skulle hatt i uføretrygd. " +
                        "Etter denne beregningen er gjort, blir ditt barnetillegg " + skulleFaatt.format() + " for " + periode.format() + "." },
                nynorsk { + "Når vi reknar ut barnetillegg, byrjar vi med å oppdatere kor mykje du skulle hatt i uføretrygd. " +
                        "Etter denne utrekninga, blir barnetillegget ditt " + skulleFaatt.format() + " for " + periode.format() + "." },
                english { + "In calculating the child supplement, we first updated the amount you should have received in disability benefit. " +
                        "After this calculation, your child supplement is " + skulleFaatt.format() + " for " + periode.format() + "." },
            )
        }
        paragraph {
            text(
                bokmal { + "Beregningene er gjort hver for seg hvis:" },
                nynorsk { + "Utrekningane er gjort kvar for seg viss:" },
                english { + "The calculations are done separately for each child if:" }
            )
        }
        paragraph {
            list {
                item {
                    text(
                        bokmal { + "du har flere barn som har ulike bosituasjoner." },
                        nynorsk { + "du har fleire barn som har ulike busituasjoner." },
                        english { + "you have multiple children with different living arrangements." }
                    )
                }
                item {
                    text(
                        bokmal { + "barnet bor med begge foreldre i deler av året, og en av foreldrene resten av året." },
                        nynorsk { + "barnet bur med begge foreldra delar av året, og en av foreldra resten av året." },
                        english { + "the child lives with both parents for part of the year and with one of the parents for the remainder of the year." }
                    )
                }
            }
        }
        paragraph {
            text(
                bokmal { + "Det er personinntekt som avgjør hvor mye du får i barnetillegg. Dette står i §12-2 i skatteloven. Personinntekter omfatter:" },
                nynorsk { + "Det er personinntekta som avgjer kor mykje du får i barnetillegg. Dette står i §12-2 i skattelova. Personinntekter omfattar:" },
                english { + "Personal income determines how much you receive in child supplement. This is stated in §12-2 of the Taxation Act. Personal income includes:" },
            )
            list {
                item {
                    text(
                        bokmal { + "pensjonsgivende inntekt" },
                        nynorsk { + "pensjonsgivande inntekt" },
                        english { + "pensionable income" },
                    )
                }
                item {
                    text(
                        bokmal { + "uføretrygd" },
                        nynorsk { + "uføretrygd" },
                        english { + "disability benefit" },
                    )
                }
                item {
                    text(
                        bokmal { + "alderspensjon fra folketrygden" },
                        nynorsk { + "alderspensjon frå folketrygda" },
                        english { + "retirement pension from the National Insurance Scheme" },
                    )
                }
                item {
                    text(
                        bokmal { + "andre pensjoner og ytelser, også fra utlandet" },
                        nynorsk { + "andre pensjonar og ytingar, også frå utlandet" },
                        english { + "other pensions and benefits, including those from abroad" },
                    )
                }
            }
            text(
                bokmal { + "Hvis personinntekten din overstiger et visst beløp (fribeløp), blir barnetillegget redusert eller faller helt bort. " +
                        "Fikk du innvilget barnetillegg i løpet av året, eller barnetillegget opphørte i løpet av året, er inntekten kortet ned for kun å gjelde den perioden du mottar barnetillegg. " +
                        "Dette regnes ut ved å dele årsinntekten på antall måneder med uføretrygd i etteroppgjørsåret og multiplisere med antall måneder du mottar barnetillegg." },

                nynorsk { + "Dersom personinntekta di er over eit visst beløp (fribeløp), vil barnetillegget bli redusert eller falle bort heilt. " +
                        "Fekk du innvilga barnetillegg i løpet av året, eller barnetillegget opphøyrde i løpet av året, er inntekta korta ned for berre å gjelde den perioden du mottar barnetillegg. " +
                        "Dette vert rekna ut ved å dele årsinntekta på talet månader med uføretrygd i etteroppgjørsåret og så multiplisert med talet månader du mottar barnetillegg." },

                english { + "If your personal income exceeds a certain amount (threshold), the child supplement is reduced or completely discontinued. " +
                        "If you were granted the child supplement during the year, or if the child supplement ceased during the year, the income is adjusted to apply only to the period when you receive the child supplement. " +
                        "This calculation is done by dividing the annual income by the number of months with disability benefit in the post-settlement year and multiplying it by the number of months you receive the child supplement." }
            )
        }

        ifNotNull(barnetillegg.felles) { fellesbarn ->
            title2 {
                text(
                    bokmal { + "For barn som bor sammen med begge foreldrene:" },
                    nynorsk { + "For barn som bur saman med begge foreldra:" },
                    english { +  "Child supplement for children living with both parents:" }
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +  "Barnetillegget beregnes ut fra inntekten til deg og din " + fellesbarn.sivilstand.ubestemtForm() + "." },
                            nynorsk { +  "Barnetillegget blir rekna ut med utgangspunkt i di eiga inntekt og inntekta til " + fellesbarn.sivilstand.ubestemtForm() + "." },
                            english { +  "Child supplement is calculated based on the total income of you and your " + fellesbarn.sivilstand.ubestemtForm() + "." }
                        )
                    }
                    item {
                        text(
                            bokmal { +  "Fribeløpet er 4,6 ganger folketrygdens grunnbeløp. I " + periode.format() + " var fribeløpet " + fellesbarn.fribeloep.format() + "." },
                            nynorsk { +  "Fribeløpet er 4,6 gonger grunnbeløpet i folketrygda. I " + periode.format() + " var fribeløpet " + fellesbarn.fribeloep.format() + "." },
                            english { +  "The free allowance is 4.6 times the basic amount of the National Insurance Scheme. In " + periode.format() +
                                    " the free allowance was " + fellesbarn.fribeloep.format() + "." },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "Fribeløpet øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn." },
                            nynorsk { +  "Fribeløpet aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn." },
                            english { +  "The free allowance increases by 40 percent of the basic amount of the National Insurance Scheme for each additional child." },
                        )
                    }
                    showIf(barnetillegg.mindreEnn40AarTrygdetid) {
                        item {
                            text(
                                bokmal { +  "Fribeløpet blir redusert ut fra trygdetiden du har." },
                                nynorsk { +  "Fribeløpet blir redusert ut frå trygdetida du har." },
                                english { +  "The free allowance is reduced based on your period of national insurance." }
                            )
                        }
                    }
                    item {
                        text(
                            bokmal { +  "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet." },
                            nynorsk { +  "Barnetillegget blir redusert med 50 prosent av inntekta som er over fribeløpet." },
                            english { +  "Child supplement will be reduced by 50 percent of the income exceeding the free allowance." },
                        )
                    }
                }
            }
        }

        ifNotNull(barnetillegg.saerkull) { saerkull ->
            title2 {
                text(
                    bokmal { +  "For barn som ikke bor sammen med begge foreldrene:" },
                    nynorsk { +  "For barn som ikkje bur saman med begge foreldra:" },
                    english { +  "Child supplement for children living with one parent:" },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +  "Barnetillegget beregnes ut fra inntekten din." },
                            nynorsk { +  "Barnetillegget blir rekna ut på grunnlag av inntekta di." },
                            english { +  "Child supplement is calculated based on your income." },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "Fribeløpet er 3,1 ganger folketrygdens grunnbeløp. I " + periode.format() + " var fribeløpet " + saerkull.fribeloep.format() + "." },
                            nynorsk { +  "Fribeløpet er 3,1 gongar grunnbeløpet i folketrygda. I " + periode.format() + " var fribeløpet " + saerkull.fribeloep.format() + "." },
                            english { +  "The free allowance is 3.1 times the basic amount of the National Insurance Scheme. In " + periode.format() +
                                    " the free allowance was " + saerkull.fribeloep.format() + "." },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "Fribeløpet øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn." },
                            nynorsk { +  "Fribeløpet aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn." },
                            english { +  "The free allowance increases by 40 percent of the basic amount of the National Insurance Scheme for each additional child." },
                        )
                    }
                    showIf(barnetillegg.mindreEnn40AarTrygdetid) {
                        item {
                            text(
                                bokmal { +  "Fribeløpet blir redusert ut fra trygdetiden du har." },
                                nynorsk { +  "Fribeløpet blir redusert ut frå trygdetida du har." },
                                english { +  "The free allowance is reduced based on your period of the national insurance." },
                            )
                        }
                    }
                    item {
                        text(
                            bokmal { +  "Barnetillegget blir redusert med 50 prosent av inntekten som overstiger fribeløpet." },
                            nynorsk { +  "Barnetillegget blir redusert med 50 prosent av inntekta som er over fribeløpet." },
                            english { +  "Child supplement will be reduced by 50 percent of the income exceeding the free allowance." },
                        )
                    }
                    item {
                        text(
                            bokmal { +  "Inntekten til en ektefelle/parner/samboer som ikke er forelder for barnet, har ingen betydning." },
                            nynorsk { +  "Inntekta til ein ektefelle/partnar/sambuer som ikkje er forelder til barnet, har inga betydning." },
                            english { +  "The income of a spouse/partner/cohabitant that is not a parent of the child is of no significance." },
                        )
                    }
                }
            }
        }

        val harFellesTillegg = barnetillegg.felles.notNull()
        paragraph {
            val erflereTabellerPersonInntekt = (barnetillegg.personinntekt.fratrekk.fratrekk.isNotEmpty() or harFellesTillegg)
            text(
                bokmal { +  ifElse(erflereTabellerPersonInntekt, "Tabellene", "Tabellen")
                        + " under viser inntektene du"
                        + ifElse(harFellesTillegg, " og annen forelder", "") },
                nynorsk { +  ifElse(erflereTabellerPersonInntekt, "Tabellene", "Tabellen")
                        + " under viser inntektene du"
                        + ifElse(harFellesTillegg, " og anna forelder", "") },
                english { +  ifElse(erflereTabellerPersonInntekt, "The tables under show", "The table under shows")
                        + " the personal incomes you"
                        + ifElse(harFellesTillegg, " and the other parent", "") }
            )
            text(
                bokmal { +  " har hatt i " + periode.format() + ". Det er disse inntektene vi har brukt for å beregne barnetillegget." },
                nynorsk { +  " har hatt i " + periode.format() + ". Det er desse inntektene vi har brukt for å rekne ut barnetillegget." },
                english { +  " have in " + periode.format() + ". These incomes were used to calculate your child supplement." }
            )
        }

        title2 {
            text(
                bokmal { +  "Personinntekten din" },
                nynorsk { +  "Personinntekta di" },
                english { +  "Your personal income" },
            )
        }
        paragraph {
            includePhrase(
                InntektTabell(
                    barnetillegg.personinntekt.inntekt,
                    newText(
                        Bokmal to "Total personinntekt",
                        Nynorsk to "Samla personinntekt",
                        English to "Total personal income",
                        fontType = FontType.BOLD,
                    ),
                )
            )
        }

        title2 {
            text(
                bokmal { +  "Beløp som er trukket fra personinntekten din" },
                nynorsk { +  "Beløp som er trekt frå personinntekta di" },
                english { +  "Amounts deducted from your personal income" },
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
                            English to "Total amount deducted from your personal income",
                            fontType = FontType.BOLD,
                        ),
                    )
                )
            } orShow {
                text(
                    bokmal { +  "Du har ikke hatt inntekter som er trukket fra personinntekten din i " + periode.format() +
                            ". Hvis du har hatt inntekter som kan trekkes fra, må du sende oss dokumentasjon på det innen 3 uker." },
                    nynorsk { +  "Du har ikkje hatt inntekter som er trekte frå personinntekta di i " + periode.format() +
                            ". Dersom du har hatt inntekter som kan trekkjast frå, må du sende oss dokumentasjon på dette innan 3 veker." },
                    english { +  "None of your personal income in " + periode.format() + " has been deducted. " +
                            "If you have had income that can be deducted, you must provide us with documentation within 3 weeks." },
                )
            }
        }

        ifNotNull(barnetillegg.felles) { fellesbarn ->
            title2 {
                text(
                    bokmal { +  "Personinntekten til annen forelder" },
                    nynorsk { +  "Personinntekta til den andre forelderen" },
                    english { +  "Personal income of the other parent" },
                )
            }
            paragraph {
                includePhrase(
                    InntektTabell(
                        fellesbarn.personinntektAnnenForelder.inntekt,
                        newText(
                            Bokmal to "Total personinntekt til annen forelder",
                            Nynorsk to "Den samla personinntekta til den andre forelderen",
                            English to "Total personal income for the other parent",
                            fontType = FontType.BOLD,
                        ),
                    )
                )
            }
            paragraph {
                text(
                    bokmal { +  "Mottar annen forelder uføretrygd eller alderspensjon fra Nav, regnes dette også med som personinntekt." },
                    nynorsk { +  "Mottar den andre forelderen uføretrygd eller alderspensjon frå Nav, blir dette også rekna som personinntekt." },
                    english { +  "If the other parent receives disability benefit or retirement pension from Nav, this will also count as personal income." }
                )
            }

            showIf(fellesbarn.personinntektAnnenForelder.fratrekk.fratrekk.isNotEmpty()) {
                title2 {
                    text(
                        bokmal { +  "Beløp som er trukket fra annen forelder sin personinntekt" },
                        nynorsk { +  "Beløp som er trekt frå personinntekta til den andre forelderen" },
                        english { +  "Amounts deducted from the other parent's personal income" }
                    )
                }
                paragraph {
                    includePhrase(
                        FratrekkTabell(
                            fellesbarn.personinntektAnnenForelder.fratrekk,
                            newText(
                                Bokmal to "Totalbeløp som er trukket fra personinntekten til annen forelder",
                                Nynorsk to "Totalbeløp som er trekt frå personinntekta til den andre forelderen",
                                English to "Total amount deducted from the other parent's personal income",
                                fontType = FontType.BOLD,
                            ),
                        )
                    )
                }

                showIf(
                    fellesbarn.resultat.avvik.notEqualTo(0)
                            and fellesbarn.personinntektAnnenForelder.inntekt.sum.greaterThan(fellesbarn.personinntektAnnenForelder.fratrekk.sum)
                ) {
                    paragraph {
                        text(
                            bokmal { +  "Folketrygdens grunnbeløp på inntil " + fellesbarn.grunnbelop.format() + " er holdt utenfor inntekten til annen forelder." },
                            nynorsk { +  "Folketrygdens grunnbeløp på inntil " + fellesbarn.grunnbelop.format() + " er halde utanfor inntekta til den andre forelderen." },
                            english { +  "The National Insurance Scheme basic amount of up to " + fellesbarn.grunnbelop.format() +
                                    " has been excluded from the income of the other parent." },
                        )
                    }
                }
            } orShow {
                paragraph {
                    text(
                        bokmal { +  "Annen forelder har ikke hatt inntekt som er trukket fra sin personinntekt i " + periode.format() + "." },
                        nynorsk { +  "Den andre forelderen har ikkje hatt inntekt som er trekt frå sin personinntekt i " + periode.format() + "." },
                        english { +  "No income was deducted from the other parent's income in " + periode.format() + "." }
                    )
                }
            }

        }

        ifNotNull(barnetillegg.saerkull) { saerkull ->
            showIf(saerkull.harSamletInntektOverInntektstak) {
                paragraph {
                    text(
                        bokmal { +  "Du hadde for høy samlet inntekt i " + periode.format() + " for å få utbetalt barnetillegg for særkullsbarn. " +
                                "Sum av samlet inntekt som gjør at barnetillegget ikke blir utbetalt var " + saerkull.samletInntekt.format() + ". " +
                                "Inntektstaket for å få utbetalt barnetillegg for særkullsbarn var " + saerkull.inntektstakSamletInntekt.format() + "." },

                        nynorsk { +  "Du hadde for høg samla inntekt i " + periode.format() + " til å få utbetalt barnetillegg for særkullsbarn. " +
                                "Summen av den samla inntekta som gjer at barnetillegget ikkje blir utbetalt, var " + saerkull.samletInntekt.format() + ". " +
                                "Inntektstaket for å få utbetalt barnetillegg for særkullsbarn var " + saerkull.inntektstakSamletInntekt.format() + "." },

                        english { +  "In " + periode.format() + " your income was too high to receive child supplement. The amount preventing the " +
                                "payment of the child supplement was " + saerkull.samletInntekt.format() + ". The income threshold to receive child supplement " +
                                "for children from a previous relationship was " + saerkull.inntektstakSamletInntekt.format() + "." },
                    )
                }
            }
        }

        ifNotNull(barnetillegg.felles) { fellesbarn ->
            showIf(fellesbarn.harSamletInntektOverInntektstak) {
                paragraph {
                    text(
                        bokmal { +  "Dere hadde for høy samlet inntekt i " + periode.format() + " for å få utbetalt barnetillegg for fellesbarn. " +
                                "Sum av samlet inntekt som gjør at barnetillegget ikke blir utbetalt var " + fellesbarn.samletInntekt.format() + ". " +
                                "Inntektstaket for å få utbetalt barnetillegg for fellessbarn var " + fellesbarn.inntektstakSamletInntekt.format() + "." },

                        nynorsk { +  "Dere hadde for høg samla inntekt i " + periode.format() + " til å få utbetalt barnetillegg for fellesbarn. " +
                                "Summen av den samla inntekta som gjer at barnetillegget ikkje blir utbetalt, var " + fellesbarn.samletInntekt.format() + ". " +
                                "Inntektstaket for å få utbetalt barnetillegg for fellesbarn var " + fellesbarn.inntektstakSamletInntekt.format() + "." },

                        english { +  "In " + periode.format() + " your combined income was too high to receive child supplement. The amount preventing the " +
                                "payment of the child supplement was " + fellesbarn.samletInntekt.format() + ". The income threshold to receive child supplement " +
                                "for joint children was " + fellesbarn.inntektstakSamletInntekt.format() + "." },
                    )
                }
            }
        }
    }
}

data class OmBeregningAvUfoeretrygd(
    val barnetillegg: Expression<OpplysningerOmEtteroppgjoeretDto.Barnetillegg?>,
    val harGjenlevendeTillegg: Expression<Boolean>,
    val pensjonsgivendeInntekt: Expression<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk>,
    val periode: Expression<Year>,
    val pensjonsgivendeInntektBruktIBeregningen: Expression<Kroner>,
    val ufoeretrygd: Expression<OpplysningerOmEtteroppgjoeretDto.AvviksResultat>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +  "Om beregningen av uføretrygd" + ifElse(
                    harGjenlevendeTillegg,
                    " og gjenlevendetillegg", "")
                        + " for " + periode.format() },
                nynorsk { +  "Om utrekning av uføretrygd" + ifElse(
                    harGjenlevendeTillegg,
                    " og attlevandetillegg", "")
                        + " for " + periode.format() },
                english { +  "Disability benefit" + ifElse(harGjenlevendeTillegg, " and survivor's supplement", "")
                        + " calculation for " + periode.format() },
            )
        }

        paragraph {
            showIf(pensjonsgivendeInntekt.inntekt.inntekter.isNotEmpty()) {
                val inntektFoerFratrekk = pensjonsgivendeInntekt.inntekt.sum.format()
                text(
                    bokmal { +  "I " + periode.format() + " var den pensjonsgivende inntekten din " + inntektFoerFratrekk + "." },
                    nynorsk { +  "I " + periode.format() + " var den pensjonsgivande inntekta di " + inntektFoerFratrekk + "." },
                    english { +  "In " + periode.format() + ", your pensionable income was " + inntektFoerFratrekk + "." },
                )
            }
            showIf(pensjonsgivendeInntekt.fratrekk.fratrekk.isNotEmpty()) {
                val inntektEtterFratrekk = pensjonsgivendeInntektBruktIBeregningen.format()
                text(
                    bokmal { +  " Etter fratrekk, viser beregningen vår at du hadde " + inntektEtterFratrekk + " i pensjonsgivende inntekt." },
                    nynorsk { +  " Etter fråtrekk, viser berekninga vår at du hadde " + inntektEtterFratrekk + " i pensjonsgjevande inntekt." },
                    english { +  " After deductions, our calculation shows that you had a pensionable income of " + inntektEtterFratrekk + "." },
                )
            }
        }

        paragraph {
            text(
                bokmal { +  "Det er pensjonsgivende inntekt som avgjør hvor mye du får i uføretrygd"
                        + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegg", "")
                        + ". Dette står i § 3-15 i folketrygdloven. Pensjonsgivende inntekt er blant annet:" },

                nynorsk { +  "Det er pensjonsgivande inntekt som avgjer kor mykje du får i uføretrygd"
                        + ifElse(harGjenlevendeTillegg, " og attlevandetillegg", "")
                        + ". Dette står i § 3-15 i folketrygdlova. Døme på pensjonsgivande inntekt:" },

                english { +  "The pensionable income determines how much you receive in disability benefit"
                        + ifElse(harGjenlevendeTillegg, " and survivor's supplement", "")
                        + ". This is stated in § 3-15 of the National Insurance Act. Pensionable income includes:" },
            )
            list {
                item {
                    text(
                        bokmal { +  "brutto lønnsinntekt fra Norge inkludert feriepenger" },
                        nynorsk { +  "brutto lønsinntekt frå Noreg, inkludert feriepengar" },
                        english { +  "gross income from employment in Norway, including holiday pay" },
                    )
                }
                item {
                    text(
                        bokmal { +  "lønnsinntekt fra utlandet" },
                        nynorsk { +  "lønsinntekt frå utlandet" },
                        english { +  "income from employment abroad" },
                    )
                }
                item {
                    text(
                        bokmal { +  "inntekt fra selvstendig næringsvirksomhet" },
                        nynorsk { +  "inntekt frå sjølvstendig næringsverksemd" },
                        english { +  "income from self-employment" },
                    )
                }
                item {
                    text(
                        bokmal { +  "inntekt som fosterforelder" },
                        nynorsk { +  "inntekt som fosterforelder" },
                        english { +  "income as a foster parent" },
                    )
                }
                item {
                    text(
                        bokmal { +  "omsorgslønn" },
                        nynorsk { +  "omsorgsløn" },
                        english { +  "care benefit" },
                    )
                }
            }
        }

        paragraph {
            text(
                bokmal { +  "Hva kan bli trukket fra den pensjonsgivende inntekten din?" },
                nynorsk { +  "Kva kan trekkjast frå den pensjonsgivande inntekta?" },
                english { +  "What can be deducted from your pensionable income?" },
            )
            list {
                item {
                    text(
                        bokmal { +  "inntekt før du ble uføretrygdet" },
                        nynorsk { +  "inntekt før du blei uføretrygda" },
                        english { +  "income before you received disability benefit" },
                    )
                }
                item {
                    text(
                        bokmal { +  "inntekt etter at uføretrygden din opphørte" },
                        nynorsk { +  "inntekt etter at uføretrygda blei avvikla" },
                        english { +  "income after your disability benefit ceased" },
                    )
                }
                item {
                    text(
                        bokmal { +  "erstatningoppgjør for inntektstap" },
                        nynorsk { +  "erstatningsoppgjer for inntektstap" },
                        english { +  "compensation settlement payments for loss of income" },
                    )
                }
                item {
                    text(
                        bokmal { +  "inntekt fra helt avsluttet arbeid eller virksomhet" },
                        nynorsk { +  "inntekt frå heilt avslutta arbeid eller verksemd" },
                        english { +  "income from fully concluded work or business" },
                    )
                }
                item {
                    text(
                        bokmal { +  "etterbetaling du har fått fra Nav" },
                        nynorsk { +  "etterbetalingar du har fått frå Nav" },
                        english { +  "back payment you have received from Nav" },
                    )
                }
            }
        }

        showIf(barnetillegg.felles_safe.notNull() and periode.greaterThanOrEqual(2023)) {
            paragraph {
                text(
                    bokmal { +  "Hva kan holdes utenfor personinntekten til den andre forelderen?" },
                    nynorsk { +  "Kva kan haldast utanfor personinntekta til den andre forelderen?" },
                    english { +  "Income that can be excluded from the other parent's personal income?" },
                )
                list {
                    item {
                        text(
                            bokmal { +  "erstatningsoppgjør for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon fra Nav" },
                            nynorsk { +  "erstatningsoppgjer for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon frå Nav" },
                            english { +  "compensation settlement payments for loss of income if the other parent receives disability benefit or retirement pension from Nav" },
                        )
                    }
                }
            }
        }

        showIf(pensjonsgivendeInntekt.inntekt.inntekter.isNotEmpty()) {
            paragraph {
                val erFlereTabellerPensjonsgivendeInntekt =
                    (pensjonsgivendeInntekt.inntekt.inntekter.isNotEmpty() and pensjonsgivendeInntekt.fratrekk.fratrekk.isNotEmpty())
                text(
                    bokmal { +  ifElse(erFlereTabellerPensjonsgivendeInntekt, "Tabellene", "Tabellen") },
                    nynorsk { +  ifElse(erFlereTabellerPensjonsgivendeInntekt, "Tabellene", "Tabellen") },
                    english { +  ifElse(
                        erFlereTabellerPensjonsgivendeInntekt,
                        "The tables below show",
                        "The table below show"
                    ) }
                )
                text(
                    bokmal { +  " under viser inntektene du har hatt i " + periode.format()
                            + ". Det er disse inntektene vi har brukt for å beregne uføretrygden din"
                            + ifElse(harGjenlevendeTillegg, " og gjenlevendetillegget ditt.", ".") },

                    nynorsk { +  " under viser inntektene du har hatt i løpet av " + periode.format()
                            + ". Det er desse inntektene vi har nytta for å berekne uføretrygda di"
                            + ifElse(harGjenlevendeTillegg, " og attlevandetillegget ditt.", ".") },

                    english { +  " the pensionable incomes you had in " + periode.format()
                            + ". These incomes were used to calculate your disability benefit"
                            + ifElse(harGjenlevendeTillegg, " and survivor's supplement.", ".") },
                )
            }
        }

        title1 {
            text(
                bokmal { +  "Din pensjonsgivende inntekt" },
                nynorsk { +  "Di pensjonsgivande inntekt" },
                english { +  "Your pensionable income" },
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
                            English to "Total pensionable income",
                            fontType = FontType.BOLD,
                        ),
                    ),
                )
            }

            title1 {
                text(
                    bokmal { +  "Beløp som er trukket fra den pensjonsgivende inntekten din" },
                    nynorsk { +  "Beløp som er trekt frå den pensjonsgivande inntekta di" },
                    english { +  "Amounts deducted from your pensionable income" }
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
                                English to "Total amount deducted",
                                fontType = FontType.BOLD,
                            )
                        )
                    )
                } orShow {
                    text(
                        bokmal { +  "Du har ikke hatt inntekter som er trukket fra den pensjonsgivende inntekten din i "
                                + periode.format() + ". Hvis du har hatt inntekter som kan trekkes fra, må du sende oss dokumentasjon på det innen 3 uker." },
                        nynorsk { +  "Du har ikkje hatt inntekter som er trekte frå den pensjonsgivande inntekta di i "
                                + periode.format() + ". Dersom du har hatt inntekter som kan trekkjast frå, må du sende oss dokumentasjon på dette innan 3 veker." },
                        english { +  "No income has been deducted from your pensionable income in "
                                + periode.format() + ". If you have had deductable income, you must provide us with documentation within 3 weeks." },
                    )
                }
            }
        } orShow {
            paragraph {
                text(
                    bokmal { +  "Du har ikke hatt pensjonsgivende inntekt i " + periode.format() + "." },
                    nynorsk { +  "Du har ikkje hatt pensjonsgivande inntekt i " + periode.format() + "." },
                    english { +  "You have not had any pensionable income in " + periode.format() + "." }
                )
            }
        }
    }
}

data class ErOpplysningeneOmInntektFeil(
    val harFellesTillegg: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { +  "Er opplysningene om inntekt feil?" },
                nynorsk { +  "Er opplysningane om inntekt feil?" },
                english { +  "Are the income details incorrect?" },
            )
        }
        paragraph {
            text(
                bokmal { +  "Mener du at inntektsopplysningene i skatteoppgjøret er feil, er det Skatteetaten som skal vurdere om inntekten kan endres." },
                nynorsk { +  "Dersom du meiner at inntektsopplysningane i skatteoppgjeret er feil, er det Skatteetaten som skal vurdere om inntekta kan endrast." },
                english { +  "If you believe that the income information in your tax settlement is incorrect, it is the Norwegian Tax Administration that must assess whether " +
                        "your income can be changed." },
            )
        }
        paragraph {
            text(
                bokmal { +  "Vi gjør et nytt etteroppgjør automatisk hvis Skatteetaten endrer inntekten din. Du får tilbakemelding hvis endringen påvirker etteroppgjøret ditt." },
                nynorsk { +  "Vi utfører automatisk eit nytt etteroppgjer dersom Skatteetaten endrar inntekta di. Du får tilbakemelding dersom endringa påverker etteroppgjeret ditt." },
                english { +  "We will automatically issue a new settlement if the Norwegian Tax Administration changes your income. You will be notified if any changes affect your post-settlement." },
            )
        }

        showIf(harFellesTillegg) {
            title2 {
                text(
                    bokmal { +  "Barnetillegg og feil i den andre forelderens inntektsopplysninger" },
                    nynorsk { +  "Barnetillegg og feil i inntektsopplysningane til den andre forelderen" },
                    english { +  "Child supplement and errors in the other parent's income information" },
                )
            }
            paragraph {
                text(
                    bokmal { +  "Hvis du mener inntektsopplysningene for den andre forelderen er feil, må den andre forelderen kontakte Skatteetaten." },
                    nynorsk { +  "Dersom du meiner at inntektsopplysningane til den andre forelderen er feil, må den andre forelderen kontakte Skatteetaten." },
                    english { +  "If you believe that the income information for the other parent is incorrect, the other parent must contact the Norwegian Tax Administration." },
                )
            }
            paragraph {
                text(
                    bokmal { +  "Vi oppdaterer ikke automatisk etteroppgjøret ditt når vi får en korrigering fra Skatteetaten som gjelder den andre forelderen. " +
                            "Du må derfor gi beskjed til oss. Vi gjør da et manuelt etteroppgjør. Du trenger ikke å sende inn dokumentasjon." },
                    nynorsk { +  "Vi oppdaterer ikkje automatisk etteroppgjeret ditt når vi får ei korrigering frå Skatteetaten som gjeld den andre forelderen. " +
                            "Du må difor gi beskjed til oss. Vi utfører då eit etteroppgjer manuelt. Du treng ikkje sende inn dokumentasjon." },
                    english { +  "We do not automatically update your settlement when we receive a correction from the the Norwegian Tax Administration regarding the other parent. " +
                            "Therefore, you need to inform us. We will then conduct a manual post-settlement. You do not need to submit documentation." },
                )
            }
        }

        title2 {
            text(
                bokmal { +  "Endringer i pensjonsytelser" },
                nynorsk { +  "Endringar i pensjonsytingar" },
                english { +  "Changes in pension payments" },
            )
        }
        paragraph {
            text(
                bokmal { +  "Hvis inntekten din fra pensjonsytelser utenom Nav blir endret, må du gi beskjed til oss når endringen er gjort. " +
                        "Vi gjør da et nytt etteroppgjør. Du kan gi beskjed ved å skrive til oss på ${Constants.SKRIV_TIL_OSS_URL} " +
                        "eller ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON}." },

                nynorsk { +  "Dersom inntekta di frå pensjonsytingar utanom Nav blir endra, må du gi beskjed til oss når endringa er gjort. " +
                        "Vi utfører då eit nytt etteroppgjer. Du kan gi beskjed ved å skrive til oss på ${Constants.SKRIV_TIL_OSS_URL} " +
                        "eller ringje til oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON}." },

                english { +  "If your income pensions outside of Nav changes, you must notify us once the changes have been made. " +
                        "We will then conduct a new settlement. You can notify us by writing to us at ${Constants.SKRIV_TIL_OSS_URL} " +
                        "or by calling us at +47 ${Constants.NAV_KONTAKTSENTER_TELEFON}." },
            )
        }
    }
}

data class FratrekkTabell(
    val fratrekk: Expression<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk>,
    val sumText: TextElement<LangBokmalNynorskEnglish>
) : ParagraphPhrase<LangBokmalNynorskEnglish>() {

    override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        table(
            header = {
                column(columnSpan = 3) {
                    text(
                        bokmal { +  "Type inntekt" },
                        nynorsk { +  "Type inntekt" },
                        english { +  "Type of income" },
                    )
                }
                column(columnSpan = 3) {
                    text(
                        bokmal { +  "Årsak til trekk" },
                        nynorsk { +  "Årsak til trekk" },
                        english { +  "Reason for deduction" },
                    )
                }
                column(columnSpan = 2, alignment = ColumnAlignment.RIGHT) {
                    text(
                        bokmal { +  "Beløp" },
                        nynorsk { +  "Beløp" },
                        english { +  "Amount" },
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
                        includePhrase(KronerText(fratrekkLinje.beloep))
                    }
                }
            }
            row {
                cell { addTextContent(sumText) }
                cell { }
                cell { includePhrase(KronerText(fratrekk.sum, fontType = FontType.BOLD)) }
            }
        }

    object LocalizedAarsak :
        LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak>() {
        override fun apply(
            first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak,
            second: Language
        ): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.FOER_INNVILGET_UFOERETRYGD -> when (second) {
                    Bokmal -> "Inntekt før uføretrygden ble innvilget"
                    Nynorsk -> "Inntekt før uføretrygda vart innvilga"
                    English -> "Income before disability benefit was granted"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ETTER_OPPHOERT_UFOERETRYGD -> when (second) {
                    Bokmal -> "Inntekt etter at uføretrygden opphørte"
                    Nynorsk -> "Inntekt etter at uføretrygda var avslutta"
                    English -> "Income after disability benefit ceased"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ERSTATNING_INNTEKTSTAP_ERSTATNINGSOPPGJOER -> when (second) {
                    Bokmal -> "Erstatning for inntektstap ved erstatningsoppgjør"
                    Nynorsk -> "Erstatning for inntektstap ved erstatningsoppgjer"
                    English -> "Compensation for loss of income through compensation settlement"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ETTERSLEP_AVSLUTTET_ARBEID_ELLER_VIRKSOMHET -> when (second) {
                    Bokmal -> "Inntekt fra avsluttet arbeidforhold eller virksomhet"
                    Nynorsk -> "Inntekt frå avslutta arbeidforhold eller verksemd"
                    English -> "Income from terminated employment or business"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ANNET -> when (second) {
                    Bokmal -> "Annet"
                    Nynorsk -> "Anna"
                    English -> "Other"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.ETTERBETALING_FRA_NAV -> when (second) {
                    Bokmal -> "Etterbetaling fra Nav"
                    Nynorsk -> "Etterbetaling frå Nav"
                    English -> "Back payment from Nav"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.Aarsak.INNTEKT_INNTIL_1G -> when (second) {
                    Bokmal -> "Inntekt inntil ett grunnbeløp"
                    Nynorsk -> "Inntekt inntil eit grunnbeløp"
                    English -> "Income up to the basic amount of the National Insurance Scheme"
                }
            }

        override fun stableHashCode(): Int = "LocalizedAarsak".hashCode()
    }

    object LocalizedFratrekkType :
        LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType>() {
        override fun apply(
            first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType,
            second: Language
        ): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.ARBEIDSINNTEKT -> when (second) {
                    Bokmal -> "Arbeidsinntekt"
                    Nynorsk -> "Arbeidsinntekt"
                    English -> "Employment income"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.UTLANDSINNTEKT -> when (second) {
                    Bokmal -> "Utlandsinntekt"
                    Nynorsk -> "Utlandsinntekt"
                    English -> "Income from abroad"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.NAERINGSINNTEKT -> when (second) {
                    Bokmal -> "Næringsinntekt"
                    Nynorsk -> "Næringsinntekt"
                    English -> "Business income"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.ANDRE_PENSJONER_OG_YTELSER -> when (second) {
                    Bokmal -> "Pensjon fra andre enn Nav"
                    Nynorsk -> "Pensjonar frå andre enn Nav"
                    English -> "Pensions from other than Nav"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.FORVENTET_PENSJON_FRA_UTLANDET -> when (second) {
                    Bokmal -> "Pensjon fra utlandet"
                    Nynorsk -> "Pensjonar frå utlandet"
                    English -> "Pensions from abroad"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.INNTEKT -> when (second) {
                    Bokmal -> "Inntekt"
                    Nynorsk -> "Inntekt"
                    English -> "Income"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Fratrekk.FratrekkLinje.InntektType.FRATREKKBAR_INNTEKT -> when (second) {
                    Bokmal -> "Inntekt som kan trekkes fra"
                    Nynorsk -> "Inntekt som kan trekkjast frå"
                    English -> "Deductable income"
                }
            }

        override fun stableHashCode(): Int = "LocalizedFratrekkType".hashCode()
    }
}

data class InntektTabell(
    val inntekt: Expression<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt>,
    val sumText: TextElement<LangBokmalNynorskEnglish>
) : ParagraphPhrase<LangBokmalNynorskEnglish>() {

    override fun ParagraphOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        table(
            header = {
                column(columnSpan = 3) {
                    text(
                        bokmal { +  "Type inntekt" },
                        nynorsk { +  "Type inntekt" },
                        english { +  "Type of income" },
                    )
                }
                column(columnSpan = 3) {
                    text(
                        bokmal { +  "Kilde" },
                        nynorsk { +  "Kjelde" },
                        english { +  "Source" },
                    )
                }
                column(columnSpan = 2, alignment = ColumnAlignment.RIGHT) {
                    text(
                        bokmal { +  "Registrert inntekt" },
                        nynorsk { +  "Registrert inntekt" },
                        english { +  "Registered income" },
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
                        includePhrase(KronerText(inntektLinje.beloep))
                    }
                }
            }
            row {
                cell { addTextContent(sumText) }
                cell { }
                cell { includePhrase(KronerText(inntekt.sum, fontType = FontType.BOLD)) }
            }
        }

    object LocalizedKilde :
        LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde>() {
        override fun apply(
            first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde,
            second: Language
        ): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.INNMELDT_AV_ARBEIDSGIVER -> when (second) {
                    Bokmal -> "Elektronisk innmeldt fra arbeidsgiver"
                    Nynorsk -> "Elektronisk meldt inn frå arbeidsgivar"
                    English -> "Electronically reported by the employer"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.OPPGITT_AV_SKATTEETATEN -> when (second) {
                    Bokmal -> "Skatteetaten"
                    Nynorsk -> "Skatteetaten"
                    English -> "The Norwegian Tax Administration"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.OPPGITT_AV_BRUKER -> when (second) {
                    Bokmal -> "Opplyst av deg"
                    Nynorsk -> "Opplyst av deg"
                    English -> "Provided by you"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.Kilde.NAV -> "Nav"
            }

        override fun stableHashCode(): Int = "LocalizedKilde".hashCode()
    }

    object LocalizedInntektType :
        LocalizedFormatter<OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType>() {
        override fun apply(
            first: OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType,
            second: Language
        ): String =
            when (first) {
                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.ARBEIDSINNTEKT -> when (second) {
                    Bokmal -> "Arbeidsinntekt"
                    Nynorsk -> "Arbeidsinntekt"
                    English -> "Employment income"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.UTLANDSINNTEKT -> when (second) {
                    Bokmal -> "Utlandsinntekt"
                    Nynorsk -> "Utlandsinntekt"
                    English -> "Income from abroad"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.NAERINGSINNTEKT -> when (second) {
                    Bokmal -> "Næringsinntekt"
                    Nynorsk -> "Næringsinntekt"
                    English -> "Business income"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.UFOERETRYGD -> when (second) {
                    Bokmal -> "Uføretrygd"
                    Nynorsk -> "Uføretrygd"
                    English -> "Disability benefit"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.ANDRE_PENSJONER_OG_YTELSER -> when (second) {
                    Bokmal -> "Pensjon fra andre enn Nav"
                    Nynorsk -> "Pensjonar frå andre enn Nav"
                    English -> "Pensions from other than Nav"
                }

                OpplysningerOmEtteroppgjoeretDto.InntektOgFratrekk.Inntekt.InntektLinje.InntektType.FORVENTET_PENSJON_FRA_UTLANDET -> when (second) {
                    Bokmal -> "Pensjon fra utlandet"
                    Nynorsk -> "Pensjonar frå utlandet"
                    English -> "Pensions from abroad"
                }
            }

        override fun stableHashCode(): Int = "LocalizedInntektType".hashCode()
    }
}