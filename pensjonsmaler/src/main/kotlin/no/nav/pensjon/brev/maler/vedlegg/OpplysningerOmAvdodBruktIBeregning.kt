package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP1967
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2011
import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType.AP2016
import no.nav.pensjon.brev.api.model.Beregningsmetode.EOS
import no.nav.pensjon.brev.api.model.Beregningsmetode.FOLKETRYGD
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedPoengrekkeVedVirkSelectors.inneholderFramtidigPoeng
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedPoengrekkeVedVirkSelectors.inneholderOmsorgspoeng
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedPoengrekkeVedVirkSelectors.pensjonspoeng
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedSelectors.navn
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.anvendtTT_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerKap19VedVirkSelectors.beregningsMetode_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.anvendtTT_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.AvdoedTrygdetidsdetaljerVedVirkNokkelInfoSelectors.beregningsMetode_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.BrukerSelectors.foedselsdato
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdodBeregningKap3
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedBeregningKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedPoengrekkeVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidAvtaleland
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidNorge
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidsdetaljerKap19VedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedTrygdetidsdetaljerVedVirkNokkelInfo
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.avdoedYrkesskadedetaljerVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.bruker
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmAvdoedBruktIBeregningDtoSelectors.tilleggspensjonVedVirk
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.TabellPoengrekke
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningeromavdodbruktiberegning.OpplysningerOmAvdodTabell
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate


// V00006 i metaforce
@TemplateModelHelpers
val vedleggOpplysningerOmAvdoedBruktIBeregning =
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
            regelverkstype.isOneOf(AP2011, AP2016) and
                    ((ikkeFullAnvendtTTKap19 and beregningsmetodeKap19.equalTo(FOLKETRYGD))
                    or beregningsmetodeKap19.notEqualTo(FOLKETRYGD))
        ) {
            //trygdetidOverskrift_001
            //norskTTAvdodInfoGenerell_001
            //norskTTTabellAvdødInnl_001
            includePhrase(TrygdetidNorgeAvdodTabellInnledning)
            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetid(avdoedTrygdetidNorge))
        }.orShowIf(
            regelverkstype.equalTo(AP1967) and
                    ((ikkeFullAnvendtTTAP1967 and beregningsmetodeAP1967.equalTo(FOLKETRYGD))
                            or beregningsmetodeAP1967.notEqualTo(FOLKETRYGD))
        ) {
            //trygdetidOverskrift_001 - AP1967
            //norskTTAvdodInfoGenerell_001 - AP1967
            //norskTTTabellAvdødInnl_001 - AP1967
            //includePhrase(TrygdetidNorgeAvdodTabellInnledning)

            //TODO Tabellen for AP1967 trygdetid i norge mangler! Skjuler også innledningen.
            //
        }

        showIf(
            (beregningsmetodeKap19.equalTo(EOS) or beregningsmetodeAP1967.equalTo(EOS))
                    and avdoedTrygdetidEOS.size().greaterThan(0)
        ) {
            paragraph {
                text(
                    Bokmal to "Avdødes trygdetid i øvrige EØS-land er fastsatt på grunnlag av følgende perioder:",
                    Nynorsk to "Trygdetida til avdøde i andre EØS-land er fastsett på grunnlag av følgjande periodar:",
                    English to "The deceased's period of national insurance coverage in other EEA countries is based on the following periods:",
                )
            }
            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.UtenlandskTrygdetid(avdoedTrygdetidEOS))
        }

        // TODO her var det feil-implementert logikk som alltid vil returnere true. Fjernet den, men det betyr at det kan hende trygdetid i avtaleland vises unødvendig.
        //      fungerer i dag på grunn av sjekken på om du har trygdetidsperioder.
        showIf(avdoedTrygdetidAvtaleland.size().greaterThan(0)) {
            paragraph {
                text(
                    Bokmal to "Avdødes trygdetid i avtaleland er fastsatt på grunnlag av følgende perioder:",
                    Nynorsk to "Trygdetida til avdøde i avtaleland er fastsett på grunnlag av følgjande periodar:",
                    English to "The deceased's period of national insurance coverage in a signatory country is based on the following periods:",
                )
            }
            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.UtenlandskTrygdetid(avdoedTrygdetidAvtaleland))
        }
        title1 {
            text(
                Bokmal to "Pensjonsopptjening",
                Nynorsk to "Pensjonsopptening",
                // TODO ser denne teksten også er brukt for Pensjonsbeholdning. Er det det samme?
                //  det er lite konsekvent ordbruk for pensjonsopptjening og beholdning på engelsk.
                English to "Accumulated pension capital",
            )
        }
        paragraph {
            text(
                Bokmal to "Tabellen under viser den avdødes pensjonsgivende inntekten og pensjonspoeng. Det er bare inntekt for ferdiglignede år som vises i tabellen.",
                Nynorsk to "Tabellen under viser den pensjonsgivande inntekta og pensjonspoenga til den avdøde. Det er berre inntekt for ferdiglikna år som viser i tabellen.",
                English to "The table below shows the pensionable income and pension points to the deceased. Only income from assessed years are shown in the table.",
            )
        }

        showIf(avdoedPoengrekkeVedVirk.inneholderFramtidigPoeng) {
            paragraph {
                text(
                    Bokmal to "Framtidige pensjonspoeng blir lagt til grunn for beregningen av pensjon fra og med uføreåret eller året for dødsfallet fram til året vedkommende fyller eller skulle ha fylt 66 år.",
                    Nynorsk to "Framtidige pensjonspoeng blir lagde til grunn for berekninga av pensjonen frå og med uføreåret eller året før dødsfallet og fram til året vedkommande fyller eller skulle ha fylt 66 år.",
                    English to "Future pension points will be credited to the basis for calculation of pension starting from the year of disablement or year of death until the year in which the person concerned turns or would have turned 66 years.",
                )
            }
        }
        showIf(avdoedPoengrekkeVedVirk.inneholderOmsorgspoeng) {
            paragraph {
                text(
                    Bokmal to "Omsorgspoeng vises bare hvis annen inntekt gir et lavere pensjonspoeng enn omsorgspoenget.",
                    Nynorsk to "Omsorgspoeng blir berre vist dersom anna inntekt gir lågare pensjonspoeng enn omsorgspoenga.",
                    English to "Points for care work are only shown if other income results in lower pension points than the points for care work.",
                )
            }
        }
        title1 {
            text(
                Bokmal to "Avdødes pensjonspoeng",
                Nynorsk to "Avdødes pensjonspoeng",
                English to "The deceased's pension points",
            )
        }
        includePhrase(TabellPoengrekke(avdoedPoengrekkeVedVirk.pensjonspoeng))
    }

private object TrygdetidNorgeAvdodTabellInnledning : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Trygdetid",
                Nynorsk to "Trygdetid",
                English to "Period of national insurance coverage",
            )
        }

        paragraph {
            text(
                Bokmal to "Trygdetid er perioder med medlemskap i folketrygden. Som hovedregel er dette bo- eller arbeidsperioderi Norge. Trygdetid har betydning for beregning av pensjonen. Full trygdetid er 40 år.",
                Nynorsk to "Trygdetid er periodar med medlemskap i folketrygda. Som hovudregel er dette bu- eller arbeidsperiodar i Noreg. Trygdetid har betydning for berekninga av pensjonen. Full trygdetid er 40 år.",
                English to "The period of national insurance coverage is periods as a member of the National Insurance Scheme. As a general rule, these are periods registered as living or working in Norway. The period of national insurance coverage affects the calculation of the pension. The full insurance period is 40 years.",
            )
        }
        paragraph {
            text(
                Bokmal to "Tabellen nedenfor viser perioder vi har registrert at avdøde har bodd og/eller arbeidet i Norge. Disse opplysningene er brukt for å fastsette avdødes norske trygdetid:",
                Nynorsk to "Tabellen nedanfor viser periodar vi har registrert at avdøde har budd og/eller arbeidd i Noreg. Desse opplysningane er brukte for å fastsetje den norske trygdetida til avdøde:",
                English to "The table below shows the periods when the deceased have been registered as living and/or working in Norway. This information has been used to establish the deceased Norwegian national insurance coverage:",
            )
        }
    }
}
