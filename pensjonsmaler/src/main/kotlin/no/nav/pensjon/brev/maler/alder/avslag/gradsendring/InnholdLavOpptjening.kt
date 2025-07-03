package no.nav.pensjon.brev.maler.alder.avslag.gradsendring


import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalder
import no.nav.pensjon.brev.maler.alder.avslag.gradsendring.fraser.AvslagHjemler
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP2025Vedlegg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.aarOgMaanederFormattert
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class InnholdLavOpptjening(
    val afpBruktIBeregning: Expression<Boolean>,
    val normertPensjonsalder: Expression<NormertPensjonsalder>,
    val uttaksgrad: Expression<Int>,
    val prorataBruktIBeregningen: Expression<Boolean>,
    val virkFom: Expression<LocalDate>,
    val minstePensjonssats: Expression<Kroner>,
    val totalPensjon: Expression<Kroner>,
    val borINorge: Expression<Boolean>,
    val harEOSLand: Expression<Boolean>,
    val regelverkType : Expression<AlderspensjonRegelverkType>,
    val avtaleland: Expression<String?>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to
                        "Du har for lav pensjonsopptjening til at du kan ta ut ".expr() + uttaksgrad.format() +
                        " prosent pensjon fra " + virkFom.format() + ". Derfor har vi avslått søknaden din og uttaksgraden blir som før.",
                Nynorsk to
                        "Du har for låg pensjonsopptening til at du kan ta ut ".expr() + uttaksgrad.format() +
                        " prosent pensjon frå " + virkFom.format() + ". Derfor har vi avslått søknaden din.",
                English to
                        "Your accumulated pension capital is not sufficient for you to draw a retirement pension at ".expr() + uttaksgrad.format() +
                        " percent from ".expr() + virkFom.format() + ". Therefore, we have declined your application.",
            )
        }

        includePhrase(AvslagHjemler(regelverkType, harEOSLand, prorataBruktIBeregningen, avtaleland))

        title2 {
            text(
                Bokmal to "Slik har vi beregnet",
                Nynorsk to "Slik har vi berekna",
                English to "The calculations made are as follows"
            )
        }
        paragraph {
            list {
                item {
                    textExpr(
                        Bokmal to "For å kunne ta ut mer alderspensjon før du blir ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                ", må pensjonen din minst utgjøre ".expr() + minstePensjonssats.format() + " kroner i året.",
                        Nynorsk to "For å kunne ta ut meir alderspensjon før du blir ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                ", må pensjonen din minst utgjer ".expr() + minstePensjonssats.format() + " kroner i året.",
                        English to "In order for you to be eligible for a higher retirement pension before the age of ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                ", your retirement pension must be, at minimum, NOK ".expr() + minstePensjonssats.format() + " a year.",
                    )

                    showIf(prorataBruktIBeregningen) {
                        text(
                            Bokmal to " Vi har tatt hensyn til at du også har trygdetid fra land som Norge har trygdeavtale med.",
                            Nynorsk to " Vi har tatt omsyn til at du også har trygdetid frå land som Noreg har trygdeavtale med.",
                            English to " We have taken into account any periods of national insurance coverage" +
                                    " that you may have in countries with which Norway has a social security agreement."
                        )
                    }
                }
                item {
                    showIf(uttaksgrad.notEqualTo(100)) {
                        textExpr(
                            Bokmal to " Vi beregner den delen du ønsker å ta ut nå og hva du ville ha fått hvis du tar ut resten av pensjonen ved ".expr() + normertPensjonsalder.aarOgMaanederFormattert() + ".",
                            Nynorsk to " Vi bereknar den delen du ynskjer å ta ut nå og kva du ville ha fått dersom du tar resten av pensjonen ved ".expr() + normertPensjonsalder.aarOgMaanederFormattert() + ".",
                            English to "We calculate the part you wish to withdraw now and what you would have received".expr() +
                                    " if you take the rest of the pension at the age of " + normertPensjonsalder.aarOgMaanederFormattert() + "."
                        )
                    }
                    textExpr(
                        Bokmal to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra "
                                + virkFom.format() + ", ville du fått ".expr() + totalPensjon.format() + " kroner årlig i full pensjon når du blir ".expr() +
                                normertPensjonsalder.aarOgMaanederFormattert() + ". ",
                        Nynorsk to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå "
                                + virkFom.format() + ", ville du fått ".expr() + totalPensjon.format() + " kroner årleg i full pensjon når du blir ".expr() +
                                normertPensjonsalder.aarOgMaanederFormattert() + ". ",
                        English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from "
                                + virkFom.format() + ", your retirement pension is calculated to be NOK " + totalPensjon.format() + " a year at age ".expr() +
                                normertPensjonsalder.aarOgMaanederFormattert() + ". ",
                    )
                    showIf(afpBruktIBeregning) {
                        text(
                            Bokmal to "I denne beregningen har vi inkludert AFP.",
                            Nynorsk to "I denne berekninga har vi inkludert AFP.",
                            English to "This amount includes contractual pension (AFP)."
                        )
                    }
                }
            }
        }

        paragraph {
            text(
                Bokmal to "I vedlegg ",
                Nynorsk to "I vedlegg ",
                English to "Appendix "
            )
            namedReference(opplysningerBruktIBeregningenAP2025Vedlegg)
            text(
                Bokmal to " finner du en tabell som viser opplysninger brukt i beregningen.",
                Nynorsk to " finn du ein tabell som viser opplysningar brukt i berekninga.",
                English to " includes a table with information about how your pension is calculated."
            )
        }
        paragraph {
            text(
                Bokmal to "Beregningen er uavhengig av sivilstanden din.",
                Nynorsk to "Berekninga er uavhengig av sivilstanden din.",
                English to "The calculation is independent of your marital status."
            )
        }

        title2 {
            textExpr(
                Bokmal to "Du kan sjekke om du kan ta ut mer alderspensjon før du blir ".expr() + normertPensjonsalder.aarOgMaanederFormattert(),
                Nynorsk to "Du kan sjekke om du kan ta ut meir alderspensjon før du blir ".expr() + normertPensjonsalder.aarOgMaanederFormattert(),
                English to "You can check if you can draw more retirement pension before the age of ".expr() + normertPensjonsalder.aarOgMaanederFormattert(),
            )
        }
        paragraph {
            textExpr(
                Bokmal to "I Din pensjon på $DIN_PENSJON_URL kan du sjekke når du har mulighet til å ta ut mer alderspensjon. ".expr() +
                        "Du kan også se hva pensjonen din blir, avhengig av når og hvor mye du tar ut.",
                Nynorsk to "I Din pensjon på $DIN_PENSJON_URL kan du sjekke når du har høve til å ta ut meir alderspensjon. ".expr() +
                        "Du kan også sjå kva pensjonen din blir, avhengig av når og kor mykje du tek ut.",
                English to "Log on to ".expr() + quoted("Din pensjon") + " at $DIN_PENSJON_URL  to find out more about your pension payments. " +
                        "You can also see how your payments change depending on when you start drawing a retirement pension and what percentage of retirement pension you choose.",
            )
        }


        paragraph {
            text(
                Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut mer alderspensjon. " +
                        "En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut meir alderspensjon. " +
                        "Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden.",
                English to "You have to submit an application when you want to increase your retirement pension. " +
                        "Any change will be implemented at the earliest the month after we have received the application."
            )
        }
    }
}
