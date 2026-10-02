package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.afpprivat.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.felles.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.MINSIDE_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.alder.maler.felles.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.model.afpprivat.InnvilgelseAvAfpAutoDto
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import java.time.LocalDate

/**
 * Felles innhold for innvilgelse av AFP i privat sektor.
 *
 * Brukes av både [InnvilgelseAvAfpAuto] (autobrev, PE_AF_04_115) og
 * den redigerbare malen `InnvilgelseAvAfp` (PE_AF_04_111). Inneholder hele
 * brevkroppen, men ikke avsluttende `Har du spørsmål?`-blokk — den
 * inkluderes separat i hver mal.
 */
data class InnvilgelseAvAfpInnhold(
    val kravMottattDato: Expression<LocalDate>,
    val virkningFom: Expression<LocalDate>,
    val totalPensjon: Expression<Kroner>,
    val livsvarigBrutto: Expression<Kroner?>,
    val kronetilleggBrutto: Expression<Kroner?>,
    val kompensasjonstilleggBrutto: Expression<Kroner?>,
    val brukerUnder70Aar: Expression<Boolean>,
    val bosattINorge: Expression<Boolean>,
    val opptjening: Expression<Kroner>,
    val forholdstallUttak: Expression<Double>,
    val justeringsbeloep: Expression<Kroner?>,
    val referansebeloep: Expression<Kroner?>,
    val kompensasjonstilleggForholdstall: Expression<Double?>,
    val harEtterbetaling: Expression<Boolean>,
    val etterbetalingVirkningFom: Expression<LocalDate?>,
    val etterbetalingVirkningTom: Expression<LocalDate?>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                bokmal { +"Vedtak" },
                nynorsk { +"Vedtak" },
                english { +"Decision" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Du er innvilget AFP i privat sektor. " +
                            "Du får " + totalPensjon.format() + " hver måned før skatt fra " + virkningFom.format() + "."
                },
                nynorsk {
                    +"Du er innvilga AFP i privat sektor. " +
                            "Du får " + totalPensjon.format() + " kvar månad før skatt frå " + virkningFom.format() + "."
                },
                english {
                    +""
                },
            )
        }

        ifNotNull(kronetilleggBrutto) { brutto ->
            showIf(brutto.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal { +"Av dette er AFP kronetillegget " + brutto.format() + " i måneden. Dette tillegget får du bare til og med den måneden du fyller 67 år." },
                        nynorsk { +"Av dette er AFP kronetillegget " + brutto.format() + " i månaden. Dette tillegget får du bare til og med den måneden du fyller 67 år." },
                        english { +" " + brutto.format() + "." },
                    )
                }
            }
        }

        paragraph {
            text(
                bokmal { +"AFP blir utbetalt samtidig som alderspensjonen senest den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL." },
                nynorsk { +"AFP blir utbetalt samtidig som alderspensjonen seinast den 20. kvar månad. Du finn oversikt over utbetalingane dine på $UTBETALINGER_URL." },
                english { +"" },
            )
        }

        title2 {
            text(
                bokmal { +"Begrunnelse for vedtaket" },
                nynorsk { +"Grunngiving for vedtaket " },
                english { +"" },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Vedtaket er gjort etter bestemmelsene i lov om statstilskott til arbeidstakere som tar ut avtalefestet pensjon i privat sektor (AFP-tilskottsloven). " +
                    "Fellesordningen for AFP har vurdert at du oppfyller de avtalemessige vilkårene for rett til AFP. Nav har avgjort andre spørsmål om retten til pensjon, blant annet beregningen. " +
                    "Beregningsreglene står i paragrafene 6 til 11 i AFP-tilskottsloven. "
                },
                nynorsk {
                    +"Vedtaket er gjort etter reglane i lov om statstilskott til arbeidstakarar som tar ut avtalefesta pensjon i privat sektor (AFP-tilskottslova). " +
                            "Fellesordninga for AFP har vurdert at du oppfyller dei avtalemessige vilkåra for rett til AFP. " +
                            "Nav har avgjort andre spørsmål om retten til pensjon, mellom anna berekninga. Berekningsreglane står i paragrafane 6 til 11 i AFP-tilskottslova. "
                },
                english {
                    +""
                },
            )
        }

        showIf(brukerUnder70Aar) {
            title2 {
                text(
                    bokmal { +"AFP i privat sektor og alderspensjon fra folketrygden " },
                    nynorsk { +"AFP i privat sektor og alderspensjon frå folketrygda " },
                    english { +"" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får AFP i privat sektor sammen med alderspensjonen din." },
                    nynorsk { +"Du får AFP i privat sektor saman med alderspensjonen din." },
                    english { +"" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du må ha tatt ut minst 20 prosent alderspensjon i minst en måned for å ha rett AFP i privat sektor. " +
                            "Deretter kan du endre uttaksgraden for alderspensjonen til 0 prosent uten at det påvirker AFP-en din.  " },
                    nynorsk { +"Du må ha tatt ut minst 20 prosent alderspensjon i minst ein månad for å ha rett AFP i privat sektor. " +
                            "Deretter kan du endre uttaksgraden for alderspensjonen til 0 prosent utan at det påverkar AFP-en din.  " },
                    english { +"" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL for å se hvordan endringer påvirker pensjonen din." },
                    nynorsk { +"Du kan bruke pensjonskalkulatoren på $DIN_PENSJON_URL for å sjå korleis endringar påverkar pensjonen din. " },
                    english { +"" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Du kan ha arbeidsinntekt uten at AFP blir redusert. " +
                            "Opptjening etter det året du fylte 61 år, påvirker ikke hvor mye du får i AFP. Ny opptjening kan gi økt alderspensjon.  " },
                    nynorsk { +"Du kan ha arbeidsinntekt utan at AFP blir redusert. " +
                            "Opptening etter det året du fylte 61 år, påverkar ikkje kor mykje du får i AFP. Ny opptening kan gi høgare alderspensjon.  " },
                    english { +"" },
                )
            }
        }

        showIf(brukerUnder70Aar){ //TODO: Etterbetaling?
            title2 {
                text(
                    bokmal { +"Etterbetaling" },
                    nynorsk { +"Etterbetaling" },
                    english { +"" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får etterbetalt pensjon fra <dato>. " +//TODO: Trenger vi å få inn etterbetalingsdato her?
                            "Etterbetalingen vil vanligvis bli utbetalt i løpet av sju virkedager. " +
                            "Vi kan trekke fra skatt og ytelser du har fått fra for eksempel Nav eller tjenestepensjonsordninger. " +
                            "Derfor kan etterbetalingen din bli forsinket. Tjenestepensjonsordninger har ni ukers frist på å kreve trekk i etterbetalingen. " +
                            "Du kan sjekke eventuelle trekk i utbetalingsmeldingen på $MINSIDE_URL. " },
                    nynorsk { +"Du får etterbetalt pensjon frå <dato>. " + //TODO: Trenger vi å få inn etterbetalingsdato her?
                            "Etterbetalinga vil vanlegvis bli utbetalt i løpet av sju vyrkedagar. " +
                            "Vi kan trekke frå skatt og ytingar du har fått frå til dømes Nav eller tenestepensjonsordningar. " +
                            "Derfor kan etterbetalinga di bli forsinka. Tenestepensjonsordningar har ni vekers frist på å krevje trekk i etterbetalinga. " +
                            "Du kan sjekke eventuelle trekk i utbetalingsmeldinga på $MINSIDE_URL." },
                    english { +"" },
                )
            }

        }


        title2 {
            text(
                bokmal { +"Det er egne skatteregler for pensjon" },
                nynorsk { +"Det er eigne skattereglar for pensjon " },
                english { +"Your pension" },
            )
        }

        showIf(bosattINorge){
            paragraph {
                ifNotNull(kompensasjonstilleggBrutto) {
                    text(
                        bokmal { +"Med unntak av kompensasjonstillegget er din AFP skattepliktig." },
                        nynorsk { +"Med unntak av kompensasjonstillegget er AFP skattepliktig." },
                        english { +"" },
                    )
                }
                .orShow {
                    text(
                        bokmal { +"Din AFP er skattepliktig. " },
                        nynorsk { +"AFP-en din er skattepliktig. " },
                        english { +"Your contractual pension is taxable. " },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { +"Du bør sjekke skattekortet når du begynner å ta ut AFP. " +
                            "Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL. " +
                            "Der får du også mer informasjon om skattekort for pensjonister. " +
                            "Vi får skattekortet elektronisk. Du skal derfor ikke sende det til oss. " },
                    nynorsk { +"Du bør sjekke skattekortet når du byrjar å ta ut AFP. " +
                            "Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL. " +
                            "Der får du også meir informasjon om skattekort for pensjonistar. " +
                            "Vi får skattekortet elektronisk. Du skal derfor ikkje sende det til oss. " },
                    english { +"" },
                )
            }

            paragraph {
                text(
                    bokmal { +"På $DIN_PENSJON_URL kan du se hva du betaler i skatt. " +
                            "Her kan du også legge inn ekstra skattetrekk om du ønsker det. " +
                            "Hvis du endrer skattetrekket, vil dette gjelde fra måneden etter at vi har fått beskjed. " },
                    nynorsk { +"På $DIN_PENSJON_URL kan du sjå kva du betaler i skatt. " +
                            "Her kan du også leggje inn ekstra skattetrekk om du ønskjer det. " +
                            "Om du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed. " },
                    english { +"" },
                )
            }
        }.orShow {
            paragraph {
                ifNotNull(kompensasjonstilleggBrutto) {
                    text(
                        bokmal { +"Med unntak av kompensasjonstillegget er din AFP skattepliktig til Norge." },
                        nynorsk { +"Med unntak av kompensasjonstillegget er AFP skattepliktig til Noreg." },
                        english { +"" },
                    )
                }.orShow {
                    text(
                        bokmal { +"Din AFP er skattepliktig til Norge." },
                        nynorsk { +"AFP er skattepliktig til Noreg." },
                        english { +"Your contractual pension is taxable. " },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Hvis du har spørsmål om skatteplikt til Norge etter flytting til utlandet, må du kontakte skattekontoret i den kommunen du har flyttet fra. " +
                            "Hvis du har spørsmål om skatteplikt til det landet du er bosatt i, må du kontakte skattemyndighetene der. " },
                    nynorsk { +"Om du har spørsmål om skatteplikt til Noreg etter flytting til utlandet, må du kontakte skattekontoret i den kommunen du har flytta frå. " +
                            "Om du har spørsmål om skatteplikt til det landet du er busett i, må du kontakte skattemyndighetane der. " },
                    english { +"" },
                )
            }

            paragraph {
                text(
                    bokmal { +"På $DIN_PENSJON_URL kan du se hva du betaler i skatt. " +
                            "Her kan du også legge inn ekstra skattetrekk om du ønsker det. " +
                            "Hvis du endrer skattetrekket, vil dette gjelde fra måneden etter at vi har fått beskjed. " },
                    nynorsk { +"På $DIN_PENSJON_URL kan du sjå kva du betaler i skatt. " +
                            "Her kan du også leggje inn ekstra skattetrekk om du ønskjer det. " +
                            "Om du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed. " },
                    english { +"" },
                )
            }
        }
    }
}
