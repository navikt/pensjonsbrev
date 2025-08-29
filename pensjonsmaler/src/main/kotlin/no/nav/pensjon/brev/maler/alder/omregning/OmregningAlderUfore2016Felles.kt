package no.nav.pensjon.brev.maler.alder.omregning

import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate


data class OmregningAlderUfore2016Felles(
    val virkFom: Expression<LocalDate>,
    val uttaksgrad: Expression<Int>,
    val totalPensjon: Expression<Kroner>,
    val antallBeregningsperioder: Expression<Int>,
    val gjenlevendetilleggKap19Innvilget: Expression<Boolean>,
    val avdodNavn: Expression<String?>,
    val avdodFnr: Expression<String?>,
    val gjenlevenderettAnvendt: Expression<Boolean>,
    val eksportTrygdeavtaleAvtaleland: Expression<Boolean>,
    val faktiskBostedsland: Expression<String?>,
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
    val avtaleland: Expression<String?>,
    val innvilgetFor67: Expression<Boolean>,
    val fullTrygdetid: Expression<Boolean>,


    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                bokmal { + "Vedtak" },
                nynorsk { + "Vedtak" },
                english { + "Decision" }
            )
        }

        paragraph {
            text(
                bokmal { + "Uføretrygden din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til " + uttaksgrad.format()
                        + " prosent alderspensjon fra " + virkFom.format() },
                nynorsk { + "Uføretrygden din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til " + uttaksgrad.format()
                        + " prosent alderspensjon fra " + virkFom.format() },
                english { + "Uføretrygden din opphører fra måneden etter at du fyller 67 år. Vi har derfor regnet den om til " + uttaksgrad.format()
                        + " prosent alderspensjon fra " + virkFom.format() }

            )
        }

        paragraph {
            text(
                bokmal { + "Du får " + totalPensjon.format() + " kvar månad før skatt frå " + virkFom.format() + " i alderspensjon fra folketrygden." },
                nynorsk { + "Du får " + totalPensjon.format() + " hver måned før skatt fra " + virkFom.format() + " i alderspensjon frå folketrygda." },
                english { + "You will receive " + totalPensjon.format() + " every month before tax from " + virkFom.format() + " as retirement pension from the National Insurance Scheme" }

            )
        }

        paragraph {
            text(
                bokmal { + "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. " +
                        "Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL." },
                nynorsk { + "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. " +
                        "Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL." },
                english { + "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. " +
                        "Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL." }
            )
        }

        showIf(antallBeregningsperioder.greaterThan(1) and totalPensjon.greaterThan(0)) {
            paragraph {
                text(
                    bokmal { + "Du kan lese mer om andre beregningsperioder i vedlegget." },
                    nynorsk { + "Du kan lese meir om andre berekningsperiodar i vedlegget." },
                    english { + "There is more information about other calculation periods in the attachment." }
                )
            }
        }
    ifNotNull(avdodNavn) { avdodNavn ->
        showIf(gjenlevendetilleggKap19Innvilget) {
            paragraph {
                text(
                    bokmal { + "Du får et gjenlevendetillegg i alderspensjonen fordi du har pensjonsrettigheter etter " + avdodNavn },
                    nynorsk { + "Du får eit attlevandetillegg i alderspensjonen fordi du har pensjonsrettar etter " + avdodNavn },
                    english { + "You receive a survivor’s supplement in the retirement pension because you have pension rights after " + avdodNavn }

                )
            }
            paragraph {
                text(
                    bokmal { + "Alderspensjonen er basert på din egen pensjonsopptjening. " +
                            "Gjenlevendetillegget er differansen mellom alderspensjon basert på din egen pensjonsopptjening og opptjening fra den avdøde, " +
                            "og alderspensjon du har tjent opp selv." },
                    nynorsk { + "Alderspensjonen er basert på di eiga pensjonsopptening. " +
                            "Attlevandetillegget er skilnaden mellom alderspensjon basert på di eiga pensjonsopptening og opptening frå den avdøde, " +
                            "og alderspensjon du har tent opp sjølv." },
                    english { + "The retirement pension is based on your own pension earnings. " +
                            "The survivor’s supplement is the difference between retirement pension based on your own pension earnings and earnings from the deceased, " +
                            "and retirement pension you have earned yourself." }
                )
            }
        }
    }
        ifNotNull(avdodNavn){ avdodNavn ->
            showIf(gjenlevenderettAnvendt and avdodFnr.notNull()) {
                title2 {
                    text(
                        bokmal { + "Gjenlevenderett i alderspensjon" },
                        nynorsk { + "Attlevenderett i alderspensjon" },
                        english { + "Survivor's rights in retirement pension" }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "I beregningen vår har vi tatt utgangspunkt i din egen opptjening. Dette gir deg en høyere pensjon enn om vi hadde tatt utgangspunkt i pensjonsrettighetene du har etter " + avdodNavn },
                        nynorsk { + "I vår berekning har vi teke utgangspunkt i di eiga opptening. Dette gir deg ein høgare pensjon enn om vi hadde teke utgangspunkt i pensjonsrettane du har etter " + avdodNavn },
                        english { + "We have based our calculation on your own earnings. This gives you a higher pension than if we had based it on the pension rights you have after " + avdodNavn }
                    )
                }
            }
        }

        ifNotNull(faktiskBostedsland) {faktiskBostedsland ->
            showIf(eksportTrygdeavtaleEOS) {
                paragraph {
                    text(
                        bokmal { + "Vi forutsetter at du bor i " + faktiskBostedsland + "Hvis du skal flytte til et land utenfor EØS-området, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon." },
                        nynorsk { + "Vi føreset at du bur i " + faktiskBostedsland + "Dersom du skal flytte til eit land utanfor EØS-området, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon." },
                        english { + "We presume that you live in " + faktiskBostedsland + "If you are moving to a country outside the EEA region, it is important that you contact Nav We will then reassess your eligibility for retirement pension." }
                    )
                }
            }

            showIf(eksportTrygdeavtaleAvtaleland) {
                paragraph {
                    text(
                        bokmal { + "Vi forutsetter at du bor i " + faktiskBostedsland + "Hvis du skal flytte til et annet land, må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon." },
                        nynorsk { + "Vi føreset at du bur i " + faktiskBostedsland + "Dersom du skal flytte til eit anna land, må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon." },
                        english { + "We presume that you live in " + faktiskBostedsland + "If you are moving to another country, it is important that you contact Nav We will then reassess your eligibility for retirement pension." }
                    )
                }
            }

            showIf(erEksportberegnet and eksportberegnetUtenGarantipensjon) {
                paragraph {
                    text(
                        bokmal { + "For å ha rett til full alderspensjon når du bor i " + faktiskBostedsland + ", må du ha vært medlem i folketrygden i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikke rett til full pensjon. " +
                                "I vedleggene finner du mer detaljerte opplysninger." },
                        nynorsk { + "For å ha rett til full alderspensjon når du bur i " + faktiskBostedsland + ", må du ha vore medlem i folketrygda i minst 20 år. Du har mindre enn 20 års medlemstid og har derfor ikkje rett til full pensjon. " +
                                "I vedlegga finn du meir detaljerte opplysningar." },
                        english { + "To be eligible for a full retirement pension while living in " + faktiskBostedsland + ", you must have been a member of the National Insurance scheme earning pension rights for at least 20 years. " +
                                "You have been a member for less than 20 years, and are therefore not eligible for a full pension. " +
                                "There is more detailed information in the attachments." }
                    )
                }
            }
        }

        showIf(uttaksgrad.lessThan(100)) {
            paragraph {
                text(
                    bokmal { + "Du må sende oss en ny søknad når du ønsker å ta ut mer alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden." },
                    nynorsk { + "Du må sende oss ein ny søknad når du ønskjer å ta ut meir alderspensjon. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden." },
                    english { + "You have to submit an application when you want to increase your retirement pension. Any change will be implemented at the earliest the month after we have received the application." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget.not()
                    and garantipensjonInnvilget.not()
                    and godkjentYrkesskade.not()
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    english { + "This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget.not()
                    and garantipensjonInnvilget.not()
                    and godkjentYrkesskade
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    english { + "This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget
                    and garantipensjonInnvilget.not()
                    and godkjentYrkesskade.not()
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    english { + "This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget
                    and garantipensjonInnvilget.not()
                    and godkjentYrkesskade
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 til 20-14, 20-19 og 22-12." },
                    english { + "This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 19-20, 20-2, 20-3, 20-12 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget
                    and garantipensjonInnvilget
                    and godkjentYrkesskade.not()
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    english { + "This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget
                    and garantipensjonInnvilget
                    and godkjentYrkesskade
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    english { + "This decision was made pursuant to the provisions of §§ 19-2 to 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget.not()
                    and garantipensjonInnvilget
                    and godkjentYrkesskade.not()
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    english { + "This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(
            pensjonstilleggInnvilget.not()
                    and garantipensjonInnvilget
                    and godkjentYrkesskade
        ) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-2 til 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12." },
                    english { + "This decision was made pursuant to the provisions of §§ 19-2 to 19-8, 19-10, 19-15, 19-20, 20-2, 20-3, 20-9 to 20-14, 20-19 and 22-12 of the National Insurance Act." }
                )
            }
        }

        showIf(skjermingstilleggInnvilget) {
            paragraph {
                text(
                    bokmal { + "Du er også innvilget skjermingstillegg etter folketrygdloven § 19-9a." },
                    nynorsk { + "Du er også innvilga skjermingstillegg etter folketrygdlova § 19-9a." },
                    english { + "You have also been granted the supplement for the disabled pursuant to the provisions of § 19-9a of the National Insurance Act." }
                )
            }
        }

        showIf(gjenlevenderettAnvendt and garantitilleggInnvilget.not()) {
            paragraph {
                text(
                    bokmal { + "Gjenlevendetillegg er gitt etter nye bestemmelser i folketrygdloven § 19-16 og kapittel 10A i tilhørende forskrift om alderspensjon i folketrygden som gjelder fra 1. januar 2024." },
                    nynorsk { +  "Attlevandetillegg er innvilga etter nye reglar i folketrygdlova § 19-16 og forskrift om alderspensjon i folketrygda kapittel 10A som gjeld frå 1. januar 2024." },
                    english { +  "The survivor's supplement in your retirement pension has been granted in accordance with the changes to the provisions of the National Insurance Act § 19-16 and the regulations on retirement pension in the National Insurance chapter 10A, which apply from 1 January 2024." }
                )
            }
        }

        showIf(gjenlevenderettAnvendt and garantitilleggInnvilget) {
            paragraph {
                text(
                    bokmal { +  "Gjenlevenderett er innvilget etter § 19-16 og gjenlevendetillegg etter kapittel 20 i folketrygdloven." },
                    nynorsk { +  "Attlevanderett er innvilga etter § 19-16 og attlevandetillegg etter kapittel 20 i folketrygdlova." },
                    english { +  "The survivor's rights in your retirement pension and the survivor's supplement have been granted pursuant to the provisions of § 19-16 and Chapter 20 of the National Insurance Act." }
                )
            }
        }

        showIf(garantitilleggInnvilget) {
            paragraph {
                text(
                    bokmal { +  "Du er også innvilget garantitillegg for opptjente rettigheter etter folketrygdloven § 20-20." },
                    nynorsk { +  "Du er også innvilga garantitillegg for opptente rettar etter folketrygdlova § 20-20." },
                    english { +  "You have also been granted the guarantee supplement for accumulated rights pursuant to the provisions of § 20-20 of the National Insurance Act." }
                )
            }
        }

        showIf(
            (
                    oppfyltVedSammenleggingKap19
                            or oppfyltVedSammenleggingKap20
                            or oppfyltVedSammenleggingFemArKap19
                            or oppfyltVedSammenleggingFemArKap20)
                    and borINorge
                    and erEOSLand
        ) {
            paragraph {
                text(
                    bokmal { +  "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004." },
                    nynorsk { +  "Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004." },
                    english { +  "This decision was also made pursuant to the provisions of Regulation (EC) 883/2004." }
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
                    and erEOSLand.not()
        ) {
            paragraph {
                text(
                    bokmal { +  "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 7." },
                    nynorsk { +  "Vedtaket er også gjort etter EØS-avtalens reglar i forordning 883/2004, artikkel 7." },
                    english { +  "This decision was also made pursuant to the provisions of Article 7 of Regulation (EC) 883/2004." }
                )
            }
        }

        showIf(
            (
                    oppfyltVedSammenleggingKap19
                            or oppfyltVedSammenleggingKap20
                            or oppfyltVedSammenleggingFemArKap19
                            or oppfyltVedSammenleggingFemArKap20)
                    and eksportTrygdeavtaleEOS
                    and borINorge.not()
                    and erEOSLand
        ) {
            paragraph {
                text(
                    bokmal { +  "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004." },
                    nynorsk { +  "Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004." },
                    english { +  "This decision was also made pursuant to the provision of Regulation (EC) 883/2004." }
                )
            }
        }

        showIf(
            (
                    oppfyltVedSammenleggingKap19
                            or oppfyltVedSammenleggingKap20
                            or oppfyltVedSammenleggingFemArKap19
                            or oppfyltVedSammenleggingFemArKap20
                            or eksportTrygdeavtaleAvtaleland)
                    and erEOSLand.not()
        ) {
            ifNotNull(avtaleland) { avtaleland ->
                paragraph {
                    showIf(avtaleland.notNull()) {
                        text(
                            bokmal { +  "Vedtaket er også gjort etter reglene i trygdeavtalen med " + avtaleland },
                            nynorsk { +  "Vedtaket er også gjort etter reglane i trygdeavtalen med " + avtaleland },
                            english { +  "This decision was also made pursuant the provisions of the Social Security Agreement with " + avtaleland },
                        )
                    }
                }
            }
        }

        title2 {
            text(
                bokmal { +  "Andre pensjonsordninger" },
                nynorsk { +  "Andre pensjonsordningar" },
                english { +  "Other pension schemes" }
            )
        }

        paragraph {
            text(
                bokmal { +  "Mange er tilknyttet en eller flere offentlige eller private pensjonsordninger som de har pensjonsrettigheter fra. " +
                        "Du bør kontakte de du har slike ordninger med for å undersøke hvilke rettigheter du kan ha. " +
                        "Du kan også undersøke med siste arbeidsgiver." },
                nynorsk { +  "Mange er knytte til ei eller fleire offentlege eller private pensjonsordningar som de har pensjonsrettar frå. " +
                        "Du bør kontakte dei du har slike ordningar med for å undersøke kva for rettar du har. " +
                        "Du kan også undersøkje med siste arbeidsgivar." },
                english { +  "Many people are also members of one or more public or private pension schemes where they also have pension rights." +
                        " You must contact the company/ies you have pension arrangements with, if you have any questions about this." +
                        " You can also contact your most recent employer." }
            )
        }

        showIf(
            innvilgetFor67.not()
                    and uttaksgrad.equalTo(100)
                    and fullTrygdetid.not()
                    and borINorge
        ) {

            title2 {
                text(
                    bokmal { +  "Supplerende stønad" },
                    nynorsk { +  "Supplerande stønad" },
                    english { +  "Supplementary benefit" }
                )
            }

            paragraph {
                text(
                    bokmal { +  "Hvis du har kort botid i Norge når du fyller 67 år, kan du søke om supplerende stønad. " +
                            "Stønaden er behovsprøvd og all inntekt fra Norge og utlandet blir regnet med. " +
                            "Inntekten til eventuell ektefelle, samboer eller registrert partner blir også regnet med. " +
                            "Du kan lese mer om supplerende stønad på nettsiden vår " },
                    nynorsk { +  "Dersom du har kort butid i Noreg når du fyller 67 år, kan du søkje om supplerande stønad. " +
                            "Stønaden er behovsprøvd, og all inntekt frå Noreg og utlandet blir rekna med. " +
                            "Inntekta til eventuell ektefelle, sambuar eller registrert partnar skal også reknast med. " +
                            "Du kan lese meir om supplerande stønad på nettsida vår " },
                    english { +  "If you have only lived a short period in Norway before reaching 67 years of age, you can apply for supplementary benefit. " +
                            "The benefit is means-tested and your total income from Norway and abroad is taken into account. " +
                            "The income of any spouse, cohabitant or registered partner will also be taken into account. " +
                            "You can read more about supplementary benefit at our website " },
                )
            }
        }

        title2 {
            text(
                bokmal { +  "Det er egne skatteregler for pensjon" },
                nynorsk { +  "Det er eigne skattereglar for pensjon" },
                english { +  "Pensions are subject to special tax rules" }
            )
        }

        paragraph {
            text(
                bokmal { +  "Du bør endre skattekortet når du begynner å ta ut alderspensjon. Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL. " +
                        "Der får du også mer informasjon om skattekort for pensjonister. " +
                        "Vi får skattekortet elektronisk. Du skal derfor ikke sende det til oss. " },
                nynorsk { +  "Du bør endre skattekortet når du byrjar å ta ut alderspensjon. Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL. " +
                        "Der får du også meir informasjon om skattekort for pensjonistar. " +
                        "Vi får skattekortet elektronisk. Du skal derfor ikkje sende det til oss." },
                english { +  "When you start draw retirement pension, you should change your tax deduction card. " +
                        "You can change your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL. " +
                        "There you will find more information regarding tax deduction card for pensioners. " +
                        "We will receive the tax card directly from the Norwegian Tax Administration, meaning you do not need to send it to us." },
            )
        }

        paragraph {
            text(
                bokmal { +  "På $DIN_PENSJON_URL kan du se hva du betaler i skatt. " +
                        "Her kan du også legge inn ekstra skattetrekk om du ønsker det. " +
                        "Dersom du endrer skattetrekket, vil dette gjelde fra måneden etter at vi har fått beskjed." },
                nynorsk { +  "På $DIN_PENSJON_URL kan du sjå kva du betaler i skatt. " +
                        "Her kan du også leggje inn tilleggsskatt om du ønskjer det. " +
                        "Dersom du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed." },
                english { +  "At $DIN_PENSJON_URL you can see how much tax you are paying. " +
                        "Here you can also add surtax, if you want. If you change your income tax rate, this will be applied from the month after we have been notified of the change." },
            )
        }

        title2 {
            text(
                bokmal { +  "Alderspensjonen din reguleres årlig" },
                nynorsk { +  "Alderspensjonen din blir regulert årleg" },
                english { +  "Your retirement pension will be adjusted annually" }
            )
        }

        paragraph {
            text(
                bokmal { +  "Reguleringen skjer med virkning fra 1. mai og selve økningen blir vanligvis etterbetalt i juni. " +
                        "Du får informasjon om dette på utbetalingsmeldingen din. " +
                        "På $NAV_URL kan du lese mer om hvordan pensjonene reguleres." },
                nynorsk { +  "Reguleringa skjer med verknad frå 1. mai, og sjølve auken blir vanlegvis etterbetalt i juni. " +
                        "Du får informasjon om dette på utbetalingsmeldinga di. " +
                        "På $NAV_URL kan du lese meir om korleis pensjonane blir regulerte." },
                english { +  "The pension amount will be adjusted with effect from 1 May, and the actual increase is usually paid retroactively in June. " +
                        "You will be informed about this on your payout notice. " +
                        "You can read more about how pensions are adjusted at $NAV_URL." },
            )
        }

        showIf(gjenlevendetilleggKap19Innvilget) {
            paragraph {
                text(
                    bokmal { +  "Gjenlevendetillegg skal ikke reguleres når pensjonen øker fra 1. mai hvert år." },
                    nynorsk { +  "Attlevendetillegg skal ikkje regulerast når pensjonen aukar frå 1. mai kvart år." },
                    english { +  "The survivor’s supplement will not be adjusted when the pension increases from May 1st each year." }
                )
            }
        }

        title2 {
            text(
                bokmal { +  "Arbeidsinntekt ved siden av alderspensjonen kan gi høyere pensjon" },
                nynorsk { +  "Arbeidsinntekt ved sida av alderspensjonen kan gi høgare pensjon" },
                english { +  "Income from employment in addition to your retirement pension may increase your future pension" }
            )
        }

        paragraph {
            text(
                bokmal { +  "Du kan arbeide så mye du vil selv om du tar ut alderspensjon, uten at pensjonen din blir redusert." +
                        " Fram til og med det året du fyller 75 år, kan arbeidsinntekt i tillegg føre til at pensjonen din øker." },
                nynorsk { +  "Du kan arbeide så mykje du vil sjølv om du tek ut alderspensjon, utan at pensjonen din blir redusert. " +
                        "Fram til og med det året du fyller 75 år, kan arbeidsinntekt i tillegg føre til at pensjonen din aukar." },
                english { +  "You may combine work with drawing a pension, without deductions being made  in your pension. " +
                        "If you continue to work, you may accumulate additional pension rights. This will apply up to and including the year you turn 75." }
            )
        }

        showIf(uttaksgrad.equalTo(100)) {
            paragraph {
                text(
                    bokmal { +  "En eventuell økning vil gjelde fra 1. januar året etter at skatteoppgjøret ditt er ferdig." },
                    nynorsk { +  "Ein eventuell auke vil gjelde frå 1. januar året etter at skatteoppgjeret ditt er ferdig." },
                    english { +  "Any increase, if applicable, will take effect as of 1 January of the year after the tax assessment of your income is complete." }
                )
            }
        }.orShow {
            paragraph {
                text(
                    bokmal { +  "Den nye opptjeningen vil bli lagt til den utbetalte alderspensjonen din når du søker om endret uttaksgrad eller ny beregning av den uttaksgraden du har nå." },
                    nynorsk { +  "Den nye oppteninga blir lagd til den utbetalte alderspensjonen din når du søkjer om endra uttaksgrad eller ny berekning av den uttaksgraden du har no." },
                    english { +  "Any new accumulation of rights will be added to your pension payments when you apply to have your retirement percentage amended or apply to have your pension recalculated at your current retirement percentage." }
                )
            }
        }

        title2 {
            text(
                bokmal { +  "Du må melde fra om endringer" },
                nynorsk { +  "Du må melde frå om endringar" },
                english { +  "You must notify Nav if anything changes " }
            )
        }

        paragraph {
            text(
                bokmal { +  "Hvis du får endringer i familiesituasjon, planlegger opphold i utlandet, eller ektefellen eller samboeren din får endringer i inntekten, kan det ha betydning for beløpet du får utbetalt fra Nav. " +
                        "I slike tilfeller må du derfor straks melde fra til oss. I vedlegget ser du hvilke endringer du må si fra om. " },
                nynorsk { +  "Dersom du får endringar i familiesituasjonen, planlegg opphald i utlandet, eller ektefellen, partnaren eller sambuaren din får endringar i inntekta, kan det få noko å seie for beløpet du får utbetalt frå Nav. " +
                        "I slike tilfelle må du derfor straks melde frå til oss. I vedlegget ser du kva endringar du må seie frå om. " },
                english { +  "If there are changes in your family situation or you are planning a long-term stay abroad, or there are changes in the income of your spouse or co-habiting partner, these might affect the payments you receive from Nav. " +
                        "In such cases, you must notify Nav immediately. The appendix specifies which changes you are obligated to notify us of. " }
            )
        }

        paragraph {
            text(
                bokmal { +  "Hvis du har fått utbetalt for mye fordi du ikke har gitt oss beskjed, må du vanligvis betale tilbake pengene. " +
                        "Du er selv ansvarlig for å holde deg orientert om bevegelser på kontoen din, og du må melde fra om eventuelle feil til Nav." },
                nynorsk { +  "Dersom du har fått utbetalt for mykje fordi du ikkje har gitt oss beskjed, må du vanlegvis betale tilbake pengane. " +
                        "Du er sjølv ansvarleg for å halde deg orientert om rørsler på kontoen din, og du må melde frå om eventuelle feil til Nav." },
                english { +  "If your payments have been too high as a result of you failing to notify us of a change, the incorrect payment must normally be repaid. " +
                        "It is your responsibility to keep yourself informed of movements in your account, and you are obligated to report any and all errors to Nav." }
            )
        }

        title2 {
            text(
                bokmal { +  "Du har rett til å klage" },
                nynorsk { +  "Du har rett til å klage" },
                english { +  "You have the right to appeal" }
            )
        }
        paragraph {
            text(
                bokmal { +  "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. " +
                        "Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}." },
                nynorsk { +  "Om du meiner vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. " +
                        "Klagen skal vere skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}." },
                english { +  "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which " +
                        "you received notice of the decision. Your appeal must be made in writing. You will find a form you can use and more " +
                        "information about appeals at ${Constants.KLAGE_URL}." }
            )
        }

        paragraph {
            text(
                bokmal { +  "I vedlegget får du vite mer om hvordan du går fram." },
                nynorsk { +  "I vedlegget får du vite meir om korleis du går fram." },
                english { +  "The appendix includes information on how to proceed." }
            )
        }

        title2 {
            text(
                bokmal { +  "Du har rett til innsyn" },
                nynorsk { +  "Du har rett til innsyn" },
                english { +  "You have the right to access your file" }
            )
        }
        paragraph {
            text(
                bokmal { +  "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram." },
                nynorsk { +  "Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram." },
                english { +  "You have the right to access all documents pertaining to your case. The attachment includes information on how to proceed." }
            )
        }

        includePhrase(Felles.HarDuSpoersmaal.alder)

    }
}