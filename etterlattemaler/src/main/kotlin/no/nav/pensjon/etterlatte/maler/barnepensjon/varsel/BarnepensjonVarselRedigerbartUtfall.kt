package no.nav.pensjon.etterlatte.maler.barnepensjon.varsel

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselRedigerbartUtfallDTOSelectors.automatiskBehandla
import no.nav.pensjon.etterlatte.maler.barnepensjon.varsel.BarnepensjonVarselRedigerbartUtfallDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.gjenoppstaatt.ForhaandsvarselGjenoppstaattFraser

data class BarnepensjonVarselRedigerbartUtfallDTO(
    val automatiskBehandla: Boolean,
    val erBosattUtlandet: Boolean,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object BarnepensjonVarselRedigerbartUtfall : EtterlatteTemplate<BarnepensjonVarselRedigerbartUtfallDTO>, Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VARSEL_UTFALL

    override val template = createTemplate(
        letterDataType = BarnepensjonVarselRedigerbartUtfallDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"" },
                nynorsk { +"" },
                english { +"" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Stortinget har vedtatt nye regler for barnepensjon. De nye reglene gjelder fra 1. januar 2024." },
                    nynorsk { +"Stortinget har vedteke nye reglar for barnepensjon. Dei nye reglane gjeld frå og med 1. januar 2024." },
                    english { +"The Storting has adopted new rules for children’s pension. The new rules apply from 1 January 2024." },
                )
            }
            title2 {
                text(
                    bokmal { +"Forhåndsvarsel om innvilgelse av ny barnepensjon" },
                    nynorsk { +"Førehandsvarsel om ny innvilga barnepensjon" },
                    english { +" Advance notice regarding approval of new children’s pensions" },
                )
            }
            showIf(automatiskBehandla) {
                includePhrase(Automatisk(erBosattUtlandet))
            }.orShow { includePhrase(Manuelt(erBosattUtlandet)) }
            paragraph {
                text(
                    bokmal { +"Hvis du har andre ytelser fra Nav, Lånekassen, andre tjenestepensjonsordninger eller fra andre land enn Norge, må du selv undersøke hvilke konsekvenser barnepensjon fra folketrygden vil ha for deg." },
                    nynorsk { +"Dersom du får andre ytingar frå Nav, Lånekassen eller andre tenestepensjonsordningar, eller frå andre land enn Noreg, må du sjølv undersøkje kva konsekvensar barnepensjonen frå folketrygda får for deg." },
                    english { +"If you receive other benefits from Nav, Lånekassen, other pension schemes or from countries other than Norway, you must yourself investigate the consequences that the children’s pension from the National Insurance scheme will have for you." },
                )
            }
        }
    }

    data class Automatisk(val erBosattUtlandet: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Nav har vurdert at du har rett til ny barnepensjon fra 1. januar 2024. Vi har lagt ved en beregning av hva du vil få i barnepensjon." },
                    nynorsk { +"Nav har kome til at du har rett på ny barnepensjon frå 1. januar 2024. Vi har lagt ved ei utrekning som viser kor mykje du kjem til å få i barnepensjon." },
                    english { +"Nav has assessed that you have the right to a new children’s pension from 1 January 2024. We have enclosed a calculation of the amount you will receive in children’s pension." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Beregningen er gjort med bakgrunn i de opplysningene vi har om deg. Du er selv ansvarlig for å sjekke om opplysningene er riktige. Meld fra til oss hvis du ser noen feil eller mangler. Hvis du ikke melder fra om endringer og får utbetalt for mye barnepensjon, kan barnepensjon som er utbetalt feil kreves tilbake." },
                    nynorsk { +"Utrekninga er gjort på grunnlag av opplysningane vi har om deg. Du er sjølv ansvarleg for å sjekke at opplysningane stemmer. Sei frå til oss dersom du ser at noko er feil eller manglar. Dersom du får utbetalt for mykje barnepensjon fordi du ikkje har meldt frå om endringar, kan vi krevje at du betaler tilbake det du ikkje hadde rett på." },
                    english { +"The calculation has been made based on the information we hold about you. You are responsible for checking that this information is correct. You must notify Nav if you discover any errors or omissions. If you do not notify us of any changes and receive too much in children’s pension, we can require you to repay any overpaid amounts." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis vi ikke hører fra deg innen fire uker, tar vi utgangspunkt i at beregningene våre er riktige og du får vedtak om innvilget barnepensjon." },
                    nynorsk { +"Viss vi ikkje høyrer frå deg innan fire veker, tek vi for gitt at utrekningane våre stemmer, og du får eit vedtak om innvilga barnepensjon." },
                    english { +"If we do not hear from you within four weeks, we will assume that our calculations are correct and you will receive a formal decision to grant children’s pension." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Hvis du melder fra om endringer, vil vi gjøre endringer og du vil få vedtak gjort etter de nye opplysningene du gir oss." },
                    nynorsk { +"Dersom du melder frå om endringar, oppdaterer vi informasjonen vår og fattar eit nytt vedtak basert på dei nye opplysningane." },
                    english { +"If you notify us of any changes, we will implement these and you will then receive a decision based on the new information." },
                )
            }
            includePhrase(ForhaandsvarselGjenoppstaattFraser.ReglerForBarnepensjon)
            title2 {
                text(
                    bokmal { +"Du trenger ikke å søke" },
                    nynorsk { +"Du treng ikkje søkje" },
                    english { +"You do not need to apply" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har mottatt barnepensjon tidligere. Du trenger derfor ikke å søke på nytt." },
                    nynorsk { +"Du har fått barnepensjon tidlegare. Du treng difor ikkje å søkje på nytt." },
                    english { +"You have previously received children’s pension. You do not need to re-apply." },
                )
            }
            paragraph {
                text(
                    bokmal { +"De nye reglene gjelder fra 1. januar 2024. Selv om du var under 20 år før de nye reglene trådte i kraft, kan du ikke få utbetalt pensjon for tiden før regelendringen." },
                    nynorsk { +"Dei nye reglane gjeld frå og med 1. januar 2024. Sjølv om du var under 20 år før dei nye reglene tredde i kraft, kan du ikkje få utbetalt pensjon for tida før regelendringa." },
                    english { +"The new rules apply from 1 January 2024. Even if you were under the age of 20 before the new rules came into force, you cannot receive children’s pension for the period before the rule change." },
                )
            }
            title2 {
                text(
                    bokmal { +"Dette må du gjøre" },
                    nynorsk { +"Dette må du gjere" },
                    english { +"What you need to do:" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du må sjekke at du har et kontonummer registrert hos Nav." },
                    nynorsk { +"Sjekk at du har eit kontonummer registrert hos Nav." },
                    english { +"You must check that you have an account number registered with Nav." },
                )
            }
            includePhrase(ForhaandsvarselGjenoppstaattFraser.KontonummerOgSkatt(erBosattUtlandet))
        }
    }

    data class Manuelt(val erBosattUtlandet: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    bokmal { +"Nav vurderer om du har rett til ny barnepensjon fra 1. januar 2024. Vi har lagt ved en foreløpig beregning av hva du kan få i barnepensjon." },
                    nynorsk { +"Nav vurderer om du har rett til ny barnepensjon frå 1. januar 2024. Vi har lagt ved ei førebels utrekning som viser kor mykje du kan få i barnepensjon." },
                    english { +"Nav is assessing whether you have the right to a new children’s pension from 1 January 2024. We have enclosed a provisional calculation of the amount you can receive in children’s pension." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Beregningen er gjort med bakgrunn i de opplysningene vi har om deg. Du er selv ansvarlig for å sjekke om opplysningene er riktige. Når du har kontrollert opplysningene, må du gi beskjed til oss om vi har riktige opplysninger eller om det er nye opplysninger vi må legge til grunn i behandlingen av barnepensjonen." },
                    nynorsk { +"Utrekninga er gjort på grunnlag av opplysningane vi har om deg. Du er sjølv ansvarleg for å sjekke at opplysningane stemmer. Når du har kontrollert opplysningane, må du gi beskjed til oss om dei stemmer, eller om vi skal leggje til grunn nye opplysningar i behandlinga av barnepensjonen." },
                    english { +"The calculation has been made based on the information we hold about you. You are responsible for checking this information. After you have checked the information, you must notify us whether the information is correct, or whether there is additional information we need to take into account when processing the children’s pension." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du vil få vedtak om barnepensjon så fort du har gitt oss informasjonen vi trenger." },
                    nynorsk { +"Du vil få vedtak om barnepensjon så fort du har gitt oss informasjonen vi treng." },
                    english { +"You will receive a formal decision regarding children’s pension as soon as we have received the required information." },
                )
            }
            includePhrase(ForhaandsvarselGjenoppstaattFraser.ReglerForBarnepensjon)
            title2 {
                text(
                    bokmal { +"Du trenger ikke å søke" },
                    nynorsk { +"Du treng ikkje søkje" },
                    english { +"You do not need to apply" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har tidligere hatt utbetalt barnepensjon fra folketrygden. Du trenger derfor ikke å søke om ny barnepensjon. Du må imidlertid sjekke opplysningene og gi oss tilbakemelding snarest mulig for at vi kan gjøre vedtak i saken din." },
                    nynorsk { +"Du har tidlegare fått utbetalt barnepensjon frå folketrygda. Du treng difor ikkje å søkje om ny barnepensjon. Sjekk derimot opplysningane og gi oss tilbakemelding snarast mogleg, slik at vi kan fatte eit vedtak i saka di." },
                    english { +"You have previously received children’s pension from the National Insurance scheme. Therefore you do not need to apply for a new children’s pension. However, you must check your information and notify us as soon as possible, so that we can make a formal decision in your case." },
                )
            }
            paragraph {
                text(
                    bokmal { +"De nye reglene gjelder fra 1. januar 2024. Selv om du var under 20 år før de nye reglene trådte i kraft, kan du ikke få utbetalt pensjon i tiden før regelendringen." },
                    nynorsk { +"Dei nye reglane gjeld frå og med 1. januar 2024. Sjølv om du var under 20 år før dei nye reglene tredde i kraft, kan du ikkje få utbetalt pensjon i tida før regelendringa." },
                    english { +"The new rules apply from 1 January 2024. Even if you were under the age of 20 before the new rules came into force, you cannot receive children’s pension for the period before the rule change." },
                )
            }
            title2 {
                text(
                    bokmal { +"Dette må du gjøre" },
                    nynorsk { +"Dette må du gjere" },
                    english { +"What you need to do:" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du må sjekke at du har et kontonummer registrert hos Nav. Du må også bekrefte at opplysningene som er vedlagt er riktige, eller gi nye opplysninger for å få riktig barnepensjon." },
                    nynorsk { +"Sjekk at du har eit kontonummer registrert hos Nav. For å få rett barnepensjon må du også stadfeste at dei vedlagde opplysningane stemmer, eller gi nye, korrekte opplysningar." },
                    english { +"You need to make sure that your bank account is registered with Nav. You also need to confirm that the associated information is correct or provide us with new/updated information to receive the right amount of children’s pension." },
                )
            }
            includePhrase(ForhaandsvarselGjenoppstaattFraser.KontonummerOgSkatt(erBosattUtlandet))
        }
    }
}