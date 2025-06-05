package no.nav.pensjon.brev.maler.alder.omregning

import no.nav.pensjon.brev.api.model.maler.alderApi.AlderspensjonPerManed
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


data class OmregningAlderUfore2016Felles(
    val virkFom: Expression<LocalDate>,
    val uttaksgrad: Expression<Int>,
    val totalPensjon: Expression<Kroner>,
    val beregningsperioder: Expression<List<AlderspensjonPerManed>>,
    val gjenlevendetilleggKap19Innvilget: Expression<Boolean>,
    val avdodNavn: Expression<String>,
    val avdodFnr: Expression<String>,
    val gjenlevenderettAnvendt: Expression<Boolean>,
    val eksportTrygdeavtaleEos: Expression<Boolean>,
    val eksportTrygdeavtaleAvtaleland: Expression<Boolean>,
    val faktiskBostedsland: Expression<String>,
    val erEksportberegnet: Expression<Boolean>,
    val eksportberegnetUtenGarantipensjon: Expression<Boolean>,
    val pensjonstilleggInnvilget: Expression<Boolean>,
    val garantipensjonInnvilget: Expression<Boolean>,
    val godkjentYrkesskade: Expression<Boolean>,
    val skjermingstilleggInnvilget: Expression<Boolean>,
    val garantitilleggInnvilget: Expression<Boolean>,
    val oppfyltVedSammenleggingKap19: Expression<Boolean>,
    val oppfyltVedSammenleggingKap20: Expression<Boolean>,
    val oppfyltVedSammenleggingFemArKap19: Expression<Boolean>,
    val oppfyltVedSammenleggingFemArKap20: Expression<Boolean>,
    val borINorge: Expression<Boolean>,
    val erEOSLand: Expression<Boolean>,
    val eksportTrygdeavtaleEOS: Expression<Boolean>,
    val avtaleland: Expression<String>

) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Bokmal to "Vedtak",
                Nynorsk to "Vedtak",
                English to "Decision"
            )
        }

        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til ".expr() + uttaksgrad.format()
                        + " prosent alderspensjon fra " + virkFom.format(),
                Nynorsk to "Uføretrygden din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til ".expr() + uttaksgrad.format()
                        + " prosent alderspensjon fra " + virkFom.format(),
                English to "Uføretrygden din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til ".expr() + uttaksgrad.format()
                        + " prosent alderspensjon fra " + virkFom.format()

            )
        }

        paragraph {
            textExpr(
                Bokmal to "Du får ".expr() + totalPensjon.format() + " kroner kvar månad før skatt frå "+ virkFom.format() + " i alderspensjon fra folketrygden.",
                Nynorsk to "Du får ".expr() + totalPensjon.format() + " kroner hver måned før skatt fra "+ virkFom.format() + " i alderspensjon frå folketrygda.",
                English to "You will receive NOK ".expr() + totalPensjon.format() + " every month before tax from "+ virkFom.format() + "  as retirement pension from the National Insurance Scheme"

            )
        }

        paragraph {
            text(
                Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. " +
                        "Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på nav.no/utbetalinger.", //TODO url
                Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. " +
                        "Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på nav.no/utbetalinger.",
                English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. " +
                        "Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at nav.no/utbetalingsinformasjon."
            )
        }

        showIf(beregningsperioder.size().greaterThan(1) and totalPensjon.greaterThan(0)) {
            paragraph {
                text(
                    Bokmal to "Du kan lese mer om andre beregningsperioder i vedlegget.",
                    Nynorsk to "Du kan lese meir om andre berekningsperiodar i vedlegget.",
                    English to "There is more information about other calculation periods in the attachment."
                )
            }
        }

        showIf(gjenlevendetilleggKap19Innvilget and avdodNavn.notNull()) {
            paragraph {
                textExpr(
                    Bokmal to "Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter ".expr() + avdodNavn,
                    Nynorsk to "Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter ".expr() + avdodNavn,
                    English to "You receive a survivor’s supplement in the retirement pension because you have pension rights after ".expr() + avdodNavn

                )
            }
            paragraph {
                text(
                    Bokmal to "Alderspensjonen er basert på din egen pensjonsopptjening. " +
                            "Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, " +
                            "og alderspensjon du har tjent opp selv.",
                    Nynorsk to "Alderspensjonen er basert på di eiga pensjonsopptening. " +
                            "Attlevandetillegget er skilnaden mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, " +
                            "og alderspensjon du har tent opp sjølv.",
                    English to "The retirement pension is based on your own pension earnings. " +
                            "The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, " +
                            "and retirement pension you have earned yourself."
                )
            }
        }

        showIf(gjenlevenderettAnvendt and avdodFnr.notNull()) {
            title2 {
                text(
                    Bokmal to "Gjenlevenderett i alderspensjon",
                    Nynorsk to "Attlevenderett i alderspensjon",
                    English to "Survivor's rights in retirement pension"
                )
            }
            paragraph {
                textExpr(
                Bokmal to "I beregningen vår har vi tatt utgangspunkt i din egen opptjening. Dette gir deg en høyere pensjon enn om vi hadde tatt utgangspunkt i pensjonsrettighetene du har etter ".expr()+avdodNavn,
                Nynorsk to "I vår berekning har vi teke utgangspunkt i di eiga opptening. Dette gir deg ein høgare pensjon enn om vi hadde teke utgangspunkt i pensjonsrettane du har etter ".expr() + avdodNavn,
                English to "We have based our calculation on your own earnings. This gives you a higher pension than if we had based it on the pension rights you have after ".expr() + avdodNavn
                )
            }
        }

        showIf(eksportTrygdeavtaleEos) {
            paragraph {
                textExpr(
                    Bokmal to "Vi forutsetter at du bor i ".expr() + faktiskBostedsland +"Hvis du skal flytte til et land utenfor EØS-området, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon.",
                    Nynorsk to "Vi føreset at du bur i ".expr() + faktiskBostedsland +"Dersom du skal flytte til eit land utanfor EØS-området, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon.",
                    English to "We presume that you live in ".expr() + faktiskBostedsland +"If you are moving to a country outside the EEA region, it is important that you contact NAV We will then reassess your eligibility for retirement pension." //TODO: Skal NAV byttes ut med noe kontakt info?
                )
            }
        }

        showIf(eksportTrygdeavtaleAvtaleland) {
            paragraph {
                textExpr(
                    Bokmal to "Vi forutsetter at du bor i ".expr() + faktiskBostedsland +"Hvis du skal flytte til et annet land, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon.",
                    Nynorsk to "Vi føreset at du bur i ".expr() + faktiskBostedsland +"Dersom du skal flytte til eit anna land, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon.",
                    English to "We presume that you live in ".expr() + faktiskBostedsland +"If you are moving to another country, it is important that you contact NAV We will then reassess your eligibility for retirement pension." //TODO: Skal NAV byttes ut med noe kontakt info?
                )
            }
        }

        showIf(erEksportberegnet and eksportberegnetUtenGarantipensjon) {
            paragraph {
                textExpr(
                    Bokmal to "For å ha rett til full alderspensjon når du bor i ".expr() + faktiskBostedsland +", må du ha vært medlem i folketrygden i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikke rett til full pensjon. " +
                    "I vedleggene finner du mer detaljerte opplysninger.",
                    Nynorsk to "For å ha rett til full alderspensjon når du bur i ".expr() + faktiskBostedsland +", må du ha vore medlem i folketrygda i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikkje rett til full pensjon. " +
                    "I vedlegga finn du meir detaljerte opplysningar.",
                    English to "To be eligible for a full retirement pension while living in ".expr() + faktiskBostedsland +", you must have been a member of the National Insurance scheme earning pension rights for at least 20 years. " +
                            "You have been a member for less than 20 years, and are therefore not eligible for a full pension. " +
                    "There is more detailed information in the attachments."
                )
            }
        }

        showIf(uttaksgrad.lessThan(100)) {
            paragraph {
                text(
                    Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut mer alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                    Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut meir alderspensjon. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden.",
                    English to "You have to submit an application when you want to increase your retirement pension. Any change will be implemented at the earliest the month after we have received the application."
                )
            }
        }

        showIf(pensjonstilleggInnvilget.not() and garantipensjonInnvilget.not() and godkjentYrkesskade.not()) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act."
                )
            }
        }

        showIf(pensjonstilleggInnvilget.not() and garantipensjonInnvilget.not() and godkjentYrkesskade) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act."
                )
            }
        }

        showIf(pensjonstilleggInnvilget and garantipensjonInnvilget.not() and godkjentYrkesskade.not()) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act."
                )
            }
        }

        showIf(pensjonstilleggInnvilget and garantipensjonInnvilget.not() and godkjentYrkesskade) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act."
                )
            }
        }

        showIf(pensjonstilleggInnvilget and garantipensjonInnvilget and godkjentYrkesskade.not()) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act."
                )
            }
        }

        showIf(pensjonstilleggInnvilget and garantipensjonInnvilget and godkjentYrkesskade) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act."
                )
            }
        }

        showIf(pensjonstilleggInnvilget.not() and garantipensjonInnvilget and godkjentYrkesskade.not()) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act."
                )
            }
        }

        showIf(pensjonstilleggInnvilget.not() and garantipensjonInnvilget and godkjentYrkesskade) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12.",
                    English to "This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act."
                )
            }
        }

        showIf(skjermingstilleggInnvilget) {
            paragraph {
                text(
                    Bokmal to "Du er også innvilget skjermingstillegg etter folketrygdloven § 19-9a.",
                    Nynorsk to "Du er også innvilga skjermingstillegg etter folketrygdlova § 19-9a.",
                    English to "You have also been granted the supplement for the disabled pursuant to the provisions of § 19-9a of the National Insurance Act."
                )
            }
        }

        showIf(gjenlevenderettAnvendt and garantitilleggInnvilget.not()) {
            paragraph {
                text(
                    Bokmal to "Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024.",
                    Nynorsk to "Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024.",
                    English to "The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the National Insurance Act § 19-16 and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024."
                )
            }
        }

        showIf(gjenlevenderettAnvendt and garantitilleggInnvilget) {
            paragraph {
                text(
                    Bokmal to "Gjenlevenderett er innvilget etter § 19-16 og gjenlevendetillegg etter kapittel 20 i folketrygdloven.",
                    Nynorsk to "Attlevanderett er innvilga etter § 19-16 og attlevandetillegg etter kapittel 20 i folketrygdlova.",
                    English to "The survivor's rights in your retirement pension and the survivor's supplement have been granted pursuant to the provisions of § 19-16 and Chapter 20 of the National Insurance Act."
                )
            }
        }

        showIf(garantitilleggInnvilget) {
            paragraph {
                text(
                    Bokmal to "Du er også innvilget garantitillegg for opptjente rettigheter etter folketrygdloven § 20-20.",
                    Nynorsk to "Du er også innvilga garantitillegg for opptente rettar etter folketrygdlova § 20-20.",
                    English to "You have also been granted the guarantee supplement for accumulated rights pursuant to the provisions of § 20-20 of the National Insurance Act."
                )
            }
        }

        showIf((
                oppfyltVedSammenleggingKap19
                or oppfyltVedSammenleggingKap20
                or oppfyltVedSammenleggingFemArKap19
                or oppfyltVedSammenleggingFemArKap20)
                and borINorge
                and erEOSLand ) {
            paragraph {
                    text(
                        Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004.",
                        Nynorsk to "Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004.",
                        English to "This decision was also made pursuant to the provisions of Regulation (EC) 883/2004."
                    )
            }
        }

        showIf(
            oppfyltVedSammenleggingKap19.not()
                    and oppfyltVedSammenleggingKap20.not()
                    and oppfyltVedSammenleggingFemArKap19.not()
                    and oppfyltVedSammenleggingFemArKap20.not()
                    and eksportTrygdeavtaleEOS
                    and borINorge.not()
                    and erEOSLand.not() ) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 7.",
                    Nynorsk to "Vedtaket er også gjort etter EØS-avtalens reglar i forordning 883/2004, artikkel 7.",
                    English to "This decision was also made pursuant to the provisions of Article 7 of Regulation (EC) 883/2004."
                )
            }
        }

        showIf((
            oppfyltVedSammenleggingKap19
                    or oppfyltVedSammenleggingKap20
                    or oppfyltVedSammenleggingFemArKap19
                    or oppfyltVedSammenleggingFemArKap20)
                    and eksportTrygdeavtaleEOS
                    and borINorge.not()
                    and erEOSLand) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004.",
                    Nynorsk to "Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004.",
                    English to "This decision was also made pursuant to the provision of Regulation (EC) 883/2004."
                )
            }
        }

        showIf((
                oppfyltVedSammenleggingKap19
                        or oppfyltVedSammenleggingKap20
                        or oppfyltVedSammenleggingFemArKap19
                        or oppfyltVedSammenleggingFemArKap20
                        or eksportTrygdeavtaleAvtaleland)
                and erEOSLand.not()) { //TODO: Her bryr vi oss ikke om bor i norge?
            paragraph {
                textExpr(
                    Bokmal to "Vedtaket er også gjort etter reglene i trygdeavtalen med ".expr() + avtaleland,
                    Nynorsk to "Vedtaket er også gjort etter reglane i trygdeavtalen med ".expr() + avtaleland,
                    English to "This decision was also made pursuant the provisions of the Social Security Agreement with ".expr() + avtaleland,
                )
            }
        }

    }
}