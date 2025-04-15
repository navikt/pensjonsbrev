package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.*
import no.nav.pensjon.brev.api.model.Beregningsmetode.FOLKETRYGD
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedSelectors.navn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.anvendtTT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.anvendtTT_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.beregningsMetode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.beregningsMetode_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.anvendtTT_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.beregningsMetode_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BrukerSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdodBeregningKap3
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedBeregningKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidsdetaljerKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidsdetaljerVedVirkNokkelInfo
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedYrkesskadedetaljerVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.tilleggspensjonVedVirk
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningeromavdodbruktiberegning.OpplysningerOmAvdodTabell
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

@TemplateModelHelpers
val opplysningerOmAvdoedBruktIBeregning =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerOmAvdoedBruktIBeregningDto>(
        title = newText(
            Bokmal to "Opplysninger om avdøde brukt i beregningen",
            Nynorsk to "Opplysningar om avdøde brukte i berekninga",
            English to "Information regarding the deceased that provides the basis for the calculation",
        ),
        includeSakspart = false,
    ) {
        val regelverkstype = alderspensjonVedVirk.regelverkType
        showIf(
            beregnetPensjonPerManedVedVirk.virkDatoFom.lessThan(LocalDate.of(2024, 1, 1))
                    or bruker.foedselsdato.lessThan(LocalDate.of(1944, 1, 1))
        ) {
            paragraph {
                textExpr(
                    Bokmal to "Som gjenlevende ektefelle har du fått en gunstigere beregning av alderspensjon. I tabellen nedenfor ser du hvilke opplysninger om ".expr() +
                            avdoed.navn + " som vi har lagt til grunn for beregningen.",
                    Nynorsk to "Som attlevande ektefelle har du fått ei gunstigare berekning av alderspensjonen. I tabellen nedanfor ser du kva opplysningar om ".expr() +
                            avdoed.navn + " som vi har lagt til grunn for berekninga.",
                    English to "As a surviving spouse, a more generous formula is used to calculate your retirement pension. The table below shows the information on ".expr() +
                            avdoed.navn + ", which we have used in this calculation.",
                )
            }
        }.orShow {
            paragraph {
                textExpr(
                    Bokmal to "Som gjenlevende ektefelle har du fått beregnet et gjenlevendetillegg i alderspensjonen din. I tabellen nedenfor ser du hvilke opplysninger om ".expr() +
                            avdoed.navn + " som vi har lagt til grunn for beregningen.",
                    Nynorsk to "Som attlevande ektefelle har du fått berekna attlevandetillegg i alderspensjonen. I tabellen nedanfor ser du kva opplysningar om den avdøde ektefellen din ".expr() +
                            avdoed.navn + " som vi har lagt til grunn for berekninga.",
                    English to "As a surviving spouse, a survivor's supplement in retirement pension has been calculated. The table below shows the information on your deceased spouse ".expr() +
                            avdoed.navn + ", which we have used in this calculation.",
                )
            }
        }

        includePhrase(
            OpplysningerOmAvdodTabell(
                alderspensjonVedVirk = alderspensjonVedVirk,
                tilleggspensjonVedVirk = tilleggspensjonVedVirk,
                avdoedTrygdetidsdetaljerKap19VedVirk = avdoedTrygdetidsdetaljerKap19VedVirk,
                avdodBeregningKap3 = avdodBeregningKap3,
                avdoedTrygdetidsdetaljerVedVirkNokkelInfo = avdoedTrygdetidsdetaljerVedVirkNokkelInfo,
                avdoedBeregningKap19VedVirk = avdoedBeregningKap19VedVirk,
                beregnetPensjonPerManedVedVirk = beregnetPensjonPerManedVedVirk,
                avdoed = avdoed,
                avdoedYrkesskadedetaljerVedVirk = avdoedYrkesskadedetaljerVedVirk
            )
        )

        val anvendtTTKap19 = avdoedTrygdetidsdetaljerKap19VedVirk.anvendtTT_safe.ifNull(0)
        val ikkeFullAnvendtTTKap19 = anvendtTTKap19.greaterThan(0) and anvendtTTKap19.lessThan(40)
        val beregningsmetodeKap19 = avdoedTrygdetidsdetaljerKap19VedVirk.beregningsMetode_safe

        val anvendtTTAP1967 = avdoedTrygdetidsdetaljerVedVirkNokkelInfo.anvendtTT_safe.ifNull(0)
        val ikkeFullAnvendtTTAP1967 = anvendtTTAP1967.greaterThan(0) and anvendtTTAP1967.lessThan(40)
        val beregningsmetodeAP1967 = avdoedTrygdetidsdetaljerVedVirkNokkelInfo.beregningsMetode_safe

        showIf(
            ((regelverkstype.isOneOf(AP2011, AP2016)
                            and (ikkeFullAnvendtTTKap19 and beregningsmetodeKap19.equalTo(FOLKETRYGD))
                    or beregningsmetodeKap19.notEqualTo(FOLKETRYGD)))

                    or

                    (regelverkstype.equalTo(AP1967) and
                    ((ikkeFullAnvendtTTAP1967 and beregningsmetodeAP1967.equalTo(FOLKETRYGD))
                    or beregningsmetodeAP1967.notEqualTo(FOLKETRYGD)))
        ) {
            //trygdetidOverskrift_001
            //trygdetidOverskrift_001 - AP1967
            title1 {
                text(
                    Bokmal to "Trygdetid",
                    Nynorsk to "Trygdetid",
                    English to "Period of national insurance coverage",
                )
            }

            //norskTTAvdodInfoGenerell_001
            //norskTTAvdodInfoGenerell_001 - AP1967
            paragraph {
                text(
                    Bokmal to "Trygdetid er perioder med medlemskap i folketrygden. Som hovedregel er dette bo- eller arbeidsperioderi Norge. Trygdetid har betydning for beregning av pensjonen. Full trygdetid er 40 år.",
                    Nynorsk to "Trygdetid er periodar med medlemskap i folketrygda. Som hovudregel er dette bu- eller arbeidsperiodar i Noreg. Trygdetid har betydning for berekninga av pensjonen. Full trygdetid er 40 år.",
                    English to "The period of national insurance coverage is periods as a member of the National Insurance Scheme. As a general rule, these are periods registered as living or working in Norway. The period of national insurance coverage affects the calculation of the pension. The full insurance period is 40 years.",
                )
            }

            //norskTTTabellAvdødInnl_001
            //norskTTTabellAvdødInnl_001 - AP1967
            paragraph {
                text(
                    Bokmal to "Tabellen nedenfor viser perioder vi har registrert at avdøde har bodd og/eller arbeidet i Norge. Disse opplysningene er brukt for å fastsette avdødes norske trygdetid:",
                    Nynorsk to "Tabellen nedanfor viser periodar vi har registrert at avdøde har budd og/eller arbeidd i Noreg. Desse opplysningane er brukte for å fastsetje den norske trygdetida til avdøde:",
                    English to "The table below shows the periods when the deceased have been registered as living and/or working in Norway. This information has been used to establish the deceased Norwegian national insurance coverage:",
                )
            }
        }

        paragraph {
            table(
                {

                }
            ){

            }
        }

    }
