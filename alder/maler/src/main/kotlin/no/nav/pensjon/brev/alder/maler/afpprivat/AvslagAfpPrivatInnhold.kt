package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.afpprivat.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.Constants.KLAGE_URL
import no.nav.pensjon.brev.alder.maler.felles.Vedtak
import no.nav.pensjon.brev.alder.model.afpprivat.AvslagAfpPrivatDto.Begrunnelse
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text
import java.time.LocalDate

/**
 * Felles innhold for avslag på AFP i privat sektor.
 */
data class AvslagAfpPrivatInnhold(
    val kravMottattDato: Expression<LocalDate>,
    val begrunnelse: Expression<Begrunnelse>,
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
                bokmal { +"Vi har avslått søknaden din om AFP i privat sektor." },
                nynorsk { +"Vi har avslått søknaden din om AFP i privat sektor." },
                english { +"We have declined your application for contractual pension (AFP) in the private sector." },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"VAFP i privat sektor gis etter bestemmelsene i lov om statstilskott til arbeidstakere som tar ut avtalefestet pensjon i privat sektor (AFP-tilskottsloven). " +
                            "Fellesordningen for AFP har vurdert at du oppfyller de avtalemessige vilkårene for rett til AFP. " +
                            "Nav har avgjort andre spørsmål om retten til pensjon. "
                },
                nynorsk {
                    +"AFP i privat sektor blir gitt etter reglane i lov om statstilskott til arbeidstakarar som tar ut avtalefesta pensjon i privat sektor (AFP-tilskottslova). " +
                        "Fellesordninga for AFP har funne at du oppfyller dei avtalemessige vilkåra for rett til AFP. " +
                        "Nav har avgjort dei andre spørsmåla om retten til pensjon."
                },
                english {
                    +"The rules for AFP in the private sector are set out in the Act on state subsidies to employees who receive contractual pension in the private sector (AFP Subsidies Act). " +
                        "Fellesordningen for AFP has determined that you meet the contractual terms for the right to AFP. " +
                        "Nav has decided on other issues regarding the right to pension."
                },
            )
        }

        title2 {
            text(
                bokmal { +"Begrunnelse for avslaget " },
                nynorsk { +"Grunngiving for avslaget" },
                english { +"Reasons for the decision" },
            )
        }

        // Hjemmelshenvisning — gruppert per § i AFP-tilskottsloven.
        showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_AVSLAG_AP)) {
            paragraph {
                text(
                    bokmal { +"AFP i privat sektor før fylte 70 år må tas ut sammen med alderspensjon fra folketrygden. " +
                            "Søknaden din om AFP er avslått fordi du har fått avslag på søknaden din om alderspensjon. " },
                    nynorsk { +"Ein må ta ut AFP i privat sektor før fylte 70 år saman med alderspensjon frå folketrygda. " +
                            "Søknaden din om AFP er avslått fordi du har fått avslag på søknaden din om alderspensjon." },
                    english { +"AFP in the private sector before the age of 70 must be drawn together with a retirement pension from the National Insurance Scheme. " +
                            "Your application for AFP has been declined because your application for a retirement pension has been declined." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis du får rett til alderspensjon fra folketrygden senere og ønsker å ta ut AFP i privat sektor, må du sende inn ny søknad om AFP. " +
                            "Ett av vilkårene er at du på uttakstidspunktet for AFP må være reell arbeidstaker i en bedrift som omfattes av AFP-ordningen." },
                    nynorsk { +"Om du får rett til alderspensjon frå folketrygda seinare og ønskjer å ta ut AFP i privat sektor, må du sende inn ny søknad om AFP. " +
                            "Eitt av vilkåra er at du på uttakstidspunktet for AFP må vere reell arbeidstakar i ei verksemd som er omfatta av AFP-ordninga." },
                    english { +"If you later become entitled to a retirement pension from the National Insurance Scheme and wish to draw AFP in the private sector, you must submit a new application for AFP. " +
                            "One of the conditions is that, at the time you start drawing AFP, you must be a genuine employee of a company covered by the AFP scheme." },
                )
            }
        }

        showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_IKKE_AP)) {
            paragraph {
                text(
                    bokmal { +"AFP i privat sektor før fylte 70 år må tas ut sammen med alderspensjon fra folketrygden. " +
                            "Søknaden din om AFP er avslått fordi du ikke har søkt om å ta ut alderspensjon. " },
                    nynorsk { +"Ein må ta ut AFP i privat sektor før fylte 70 år saman med alderspensjon frå folketrygda. " +
                            "Søknaden din om AFP er avslått fordi du ikkje har søkt om å ta ut alderspensjon." },
                    english { +"AFP in the private sector before the age of 70 must be drawn together with a retirement pension from the National Insurance Scheme. " +
                            "Your application for AFP has been declined because you have not applied for a retirement pension." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du får rett til alderspensjon fra folketrygden senere og ønsker å ta ut AFP i privat sektor, må du sende inn ny søknad om AFP. " +
                            "Ett av vilkårene er at du på uttakstidspunktet for AFP må være reell arbeidstaker i en bedrift som omfattes av AFP-ordningen. " },
                    nynorsk { +"Om du får rett til alderspensjon frå folketrygda og ønskjer å ta ut AFP i privat sektor, må du sende inn ny søknad om AFP. " +
                            "Eitt av vilkåra er at du på uttakstidspunktet for AFP må vere reell arbeidstakar i ei verksemd som er omfatta av AFP-ordninga." },
                    english { +"If you later become entitled to a retirement pension from the National Insurance Scheme and wish to draw AFP in the private sector, you must submit a new application for AFP. " +
                            "One of the conditions is that, at the time you start drawing AFP, you must be a genuine employee of a company covered by the AFP scheme." },
                )
            }

        }

        showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_UT_E_62)) {
            paragraph {
                text(
                    bokmal { +"AFP i privat sektor kan ikke gis til personer som har mottatt uføretrygd fra folketrygden etter fylte 62 år. " +
                            "Søknaden din er avslått fordi du har mottatt uføretrygd etter at du fylte 62 år. " },
                    nynorsk { +"Ein kan ikkje få AFP i privat sektor om ein har mottatt uføretrygd frå folketrygda etter fylte 62 år. " +
                            "Søknaden din er avslått fordi du har mottatt uføretrygd etter at du fylte 62 år." },
                    english { +"AFP in the private sector cannot be granted to persons who have received disability benefit from the National Insurance Scheme after reaching the age of 62. " +
                            "Your application has been declined because you received disability benefit after you turned 62." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Som hovedregel vil du heller ikke kunne få rett til AFP senere selv om du skulle si fra deg uføretrygden. " +
                            "Unntaket er når du har betalt tilbake hele uføretrygden du har fått utbetalt etter fylte 62 år i forbindelse med etteroppgjøret for uføretrygd. " },
                    nynorsk { +"Som hovudregel vil du heller ikkje kunne få rett til AFP seinare sjølv om du skulle seie frå deg uføretrygda. " +
                            "Unntaket er når du har betalt tilbake heile uføretrygda du har fått utbetalt etter fylte 62 år i samband med etteroppgjeret for uføretrygd." },
                    english { +"Generally, you will not be entitled to AFP later even if you give up your disability benefit. " +
                            "The exception is if you have repaid in full all disability benefit paid to you after the age of 62 as part of the final annual settlement for disability benefit." },
                )
            }

        }

        showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_AVSLAG_AP, Begrunnelse.BRUKER_IKKE_AP)) {
            paragraph {
                text(
                    bokmal { +"Vedtaket er gjort etter AFP-tilskottsloven paragraf 5 første ledd bokstav b. " },
                    nynorsk { +"Vedtaket er gjort etter AFP-tilskottslova § 5 første ledd bokstav b." },
                    english { +"This decision follows from section 5, first paragraph, letter b, of the AFP Subsidies Act." },
                )
            }
        }

        showIf(begrunnelse.isOneOf(Begrunnelse.BRUKER_UP_E_62, Begrunnelse.BRUKER_LOP_UP, Begrunnelse.BRUKER_UT_E_62)) {
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter AFP-tilskottsloven paragraf 8 første ledd. "
                    },
                    nynorsk {
                        +"Vedtaket er gjort etter AFP-tilskottslova § 8 første ledd."
                    },
                    english {
                        +"This decision follows from section 8, first paragraph, of the AFP Subsidies Act."
                    },
                )
            }
        }

        title2{
            text(
            bokmal { +"Du har rett til å klage " },
            nynorsk { +"Du har rett til å klage" },
            english { +"You have the right to appeal" },
            )
        }

        paragraph {
            text(
                bokmal { +"Hvis du mener vedtaket er feil, kan du klage innen 6 uker fra den datoen vedtaket har kommet fram til deg. " +
                        "Du finner skjema og informasjon på $KLAGE_URL. " },
                nynorsk { +"Om du meiner vedtaket er feil, kan du klage innan 6 veker frå den datoen vedtaket har komme fram til deg. " +
                        "Du finn skjema og informasjon på $KLAGE_URL." },
                english { +"If you think the decision is wrong, you may appeal the decision within 6 weeks from the date the decision was delivered to you. " +
                        "You can find the form and more information at $KLAGE_URL." },
            )
        }

        paragraph {
            text(
                bokmal { +"Du får vite mer om hvordan du klager i vedlegget om rettigheter." },
                nynorsk { +"Du får vite meir om korleis du klagar i vedlegget om rettar." },
                english { +"You will find more information on how to appeal in the appendix about your rights." },
            )
        }
    }
}
