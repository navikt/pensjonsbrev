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
            text(
                bokmal { + 
                        "Du har for lav pensjonsopptjening til at du kan ta ut " + uttaksgrad.format() +
                        " prosent pensjon fra " + virkFom.format() + ". Derfor har vi avslått søknaden din og uttaksgraden blir som før." },
                nynorsk { + 
                        "Du har for låg pensjonsopptening til at du kan ta ut " + uttaksgrad.format() +
                        " prosent pensjon frå " + virkFom.format() + ". Derfor har vi avslått søknaden din." },
                english { + 
                        "Your accumulated pension capital is not sufficient for you to draw a retirement pension at " + uttaksgrad.format() +
                        " percent from " + virkFom.format() + ". Therefore, we have declined your application." },
            )
        }

        includePhrase(AvslagHjemler(regelverkType, harEOSLand, prorataBruktIBeregningen, avtaleland))

        title2 {
            text(
                bokmal { + "Slik har vi beregnet" },
                nynorsk { + "Slik har vi berekna" },
                english { + "The calculations made are as follows" }
            )
        }
        paragraph {
            list {
                item {
                    text(
                        bokmal { + "For å kunne ta ut mer alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() +
                                ", må pensjonen din minst utgjøre " + minstePensjonssats.format() + " i året." },
                        nynorsk { + "For å kunne ta ut meir alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() +
                                ", må pensjonen din minst utgjer " + minstePensjonssats.format() + " i året." },
                        english { + "In order for you to be eligible for a higher retirement pension before the age of " + normertPensjonsalder.aarOgMaanederFormattert() +
                                ", your retirement pension must be, at minimum, " + minstePensjonssats.format() + " a year." },
                    )

                    showIf(prorataBruktIBeregningen) {
                        text(
                            bokmal { + " Vi har tatt hensyn til at du også har trygdetid fra land som Norge har trygdeavtale med." },
                            nynorsk { + " Vi har tatt omsyn til at du også har trygdetid frå land som Noreg har trygdeavtale med." },
                            english { + " We have taken into account any periods of national insurance coverage" +
                                    " that you may have in countries with which Norway has a social security agreement." }
                        )
                    }
                }
                item {
                    showIf(uttaksgrad.notEqualTo(100)) {
                        text(
                            bokmal { + " Vi beregner den delen du ønsker å ta ut nå og hva du ville ha fått hvis du tar ut resten av pensjonen ved " + normertPensjonsalder.aarOgMaanederFormattert() + "." },
                            nynorsk { + " Vi bereknar den delen du ynskjer å ta ut nå og kva du ville ha fått dersom du tar resten av pensjonen ved " + normertPensjonsalder.aarOgMaanederFormattert() + "." },
                            english { + "We calculate the part you wish to withdraw now and what you would have received" +
                                    " if you take the rest of the pension at the age of " + normertPensjonsalder.aarOgMaanederFormattert() + "." }
                        )
                    }
                    text(
                        bokmal { + "Dersom du hadde tatt ut " + uttaksgrad.format() + " prosent alderspensjon fra "
                                + virkFom.format() + ", ville du fått " + totalPensjon.format() + " årlig i full pensjon når du blir " +
                                normertPensjonsalder.aarOgMaanederFormattert() + ". " },
                        nynorsk { + "Dersom du hadde tatt ut " + uttaksgrad.format() + " prosent alderspensjon frå "
                                + virkFom.format() + ", ville du fått " + totalPensjon.format() + " årleg i full pensjon når du blir " +
                                normertPensjonsalder.aarOgMaanederFormattert() + ". " },
                        english { + "If you draw a retirement pension of " + uttaksgrad.format() + " percent from "
                                + virkFom.format() + ", your retirement pension is calculated to be " + totalPensjon.format() + " a year at age " +
                                normertPensjonsalder.aarOgMaanederFormattert() + ". " },
                    )
                    showIf(afpBruktIBeregning) {
                        text(
                            bokmal { + "I denne beregningen har vi inkludert AFP." },
                            nynorsk { + "I denne berekninga har vi inkludert AFP." },
                            english { + "This amount includes contractual pension (AFP)." }
                        )
                    }
                }
            }
        }

        paragraph {
            text(
                bokmal { + "I vedlegg " },
                nynorsk { + "I vedlegg " },
                english { + "Appendix " }
            )
            namedReference(opplysningerBruktIBeregningenAP2025Vedlegg)
            text(
                bokmal { + " finner du en tabell som viser opplysninger brukt i beregningen." },
                nynorsk { + " finn du ein tabell som viser opplysningar brukt i berekninga." },
                english { + " includes a table with information about how your pension is calculated." }
            )
        }
        paragraph {
            text(
                bokmal { + "Beregningen er uavhengig av sivilstanden din." },
                nynorsk { + "Berekninga er uavhengig av sivilstanden din." },
                english { + "The calculation is independent of your marital status." }
            )
        }

        title2 {
            text(
                bokmal { + "Du kan sjekke om du kan ta ut mer alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() },
                nynorsk { + "Du kan sjekke om du kan ta ut meir alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() },
                english { + "You can check if you can draw more retirement pension before the age of " + normertPensjonsalder.aarOgMaanederFormattert() },
            )
        }
        paragraph {
            text(
                bokmal { + "I Din pensjon på $DIN_PENSJON_URL kan du sjekke når du har mulighet til å ta ut mer alderspensjon. " +
                        "Du kan også se hva pensjonen din blir, avhengig av når og hvor mye du tar ut." },
                nynorsk { + "I Din pensjon på $DIN_PENSJON_URL kan du sjekke når du har høve til å ta ut meir alderspensjon. " +
                        "Du kan også sjå kva pensjonen din blir, avhengig av når og kor mykje du tek ut." },
                english { + "Log on to " + quoted("Din pensjon") + " at $DIN_PENSJON_URL  to find out more about your pension payments. " +
                        "You can also see how your payments change depending on when you start drawing a retirement pension and what percentage of retirement pension you choose." },
            )
        }


        paragraph {
            text(
                bokmal { + "Du må sende oss en ny søknad når du ønsker å ta ut mer alderspensjon. " +
                        "En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden." },
                nynorsk { + "Du må sende oss ein ny søknad når du ønskjer å ta ut meir alderspensjon. " +
                        "Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden." },
                english { + "You have to submit an application when you want to increase your retirement pension. " +
                        "Any change will be implemented at the earliest the month after we have received the application." }
            )
        }
    }
}
