package no.nav.pensjon.brev.alder.maler.vedlegg.alltidValgbare

import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.choice
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val uttaksskjema = createAttachment<LangBokmalEnglish, EmptyVedleggData>(
    title = {
        text(
            bokmal { +"Hvordan vil du ta ut alderspensjonen din?" },
            english { +"How do you want to withdraw your retirement pension?" }
        )
    },
    includeSakspart = false
) {
    paragraph {
        formChoice({
            text(
                bokmal { +"Vi ber deg vennligst oppgi hvordan du ønsker å ta ut alderspensjonen din. For mer informasjon om de ulike alternativene kan du se det vedlagte informasjonsskrivet. Den samme informasjonen finner du også på våre nettsider ${Constants.NAV_URL}." },
                english { +"Please state how you would like to withdraw your retirement pension. For more information about the various options, you can see the attached information letter. You can also find the same information on our website ${Constants.NAV_URL}." }
            )
        }) {
            choice(
                bokmal { +"Jeg ønsker å ta ut full alderspensjon (100 %) fra ordinær uttaksdato i Norge– måneden etter jeg fyller 67 år." },
                english { +"I want to draw a full retirement pension (100%) from the ordinary withdrawal date in Norway - the month after I turn 67." }
            )
            choice(
                bokmal { +"Jeg ønsker å ta ut alderspensjon etter en bestemt uttaksgrad fra et bestemt tidspunkt." },
                english { +"I want to withdraw retirement pension according to a specific withdrawal rate from a specific withdrawal time." }
            )
        }
    }
    paragraph {
        formText(Size.SHORT, {
            text(
                bokmal { +"Oppgi ønsket tidspunkt for uttak av alderspensjon:" },
                english { +"State the desired time for withdrawal of retirement pension:" }
            )
        })
    }
    paragraph {
        formChoice(
            {
                text(
                    bokmal { +"Oppgi ønsket uttaksgrad:" },
                    english { +"Tick off the desired withdrawal rate:" }
                )
            }
        ) {
            choice(
                bokmal { +"20 %" },
                english { +"20 %" },
            )
            choice(
                bokmal { +"40 %" },
                english { +"40 %" },
            )
            choice(
                bokmal { +"50 %" },
                english { +"50 %" },
            )
            choice(
                bokmal { +"60 %" },
                english { +"60 %" },
            )
            choice(
                bokmal { +"80 %" },
                english { +"80 %" },
            )
            choice(
                bokmal { +"100 %" },
                english { +"100 %" },
            )
        }
    }
    paragraph {
        text(
            bokmal { +"Hvis du ikke har mulighet til uttak av ønsket uttaksgrad, vil søknaden bli avslått." },
            english { +"If you do not meet the criteria to be granted your desired withdrawal rate, the application will be rejected." }
        )
    }
    paragraph {
        text(
            bokmal { +"Dersom du ikke huker av for et alternativ, vil vi vurdere din rett til 100% pensjon fra første mulige tidspunkt." },
            english { +"If you do not opt for an alternative, we will try to grant a pension with 100 % withdrawal rate at the earliest possible time." }
        )
    }
    paragraph {
        formText(Size.FILL, {
            text(
                bokmal { +"Navn (i blokkbokstaver) og fødselsdato" },
                english { +"Name (in block letters) and date of birth" }
            )
        })
    }
    paragraph {
        formText(Size.FILL, {
            text(
                bokmal { +"Dato, sted og signatur:" },
                english { +"Date, place, and signature" }
            )
        })
    }
    paragraph {
        formText(Size.SHORT, {
            text(
                bokmal { +"Telefonnummer:" },
                english { +"Phone number:" }
            )
        })
        text(
            bokmal { +"(for at vi lettere kan komme i kontakt, hvis vi trenger mer informasjon fra deg)." },
            english { +"(so that we can more easily get in touch if we need more information from you)." }
        )
    }
    // Side 2 i vedlegget
    title1 {
        text(
            bokmal { +"Informasjon om uttak av alderspensjon" },
            english { +"Information on withdrawal of retirement pension" }
        )
    }
    paragraph {
        text(
            bokmal { +"I Norge er ordinær pensjonsalder måneden etter du fyller 67 år. Hvis du oppfyller vilkårene for å få innvilget norsk alderspensjon vil du altså alltid kunne ta den ut 100 % fra måneden etter fylte 67 år." },
            english { +"In Norway, ordinary retirement age is the month after you turn 67. If you meet the conditions for being granted a Norwegian old-age pension, you will therefore always be able to withdraw 100% of it from the month after you turn 67." }
        )
    }
    paragraph {
        text(
            bokmal {
                +"Norge har også en ordning som heter ${quoted("fleksibelt uttak av alderspensjon fra 62 år")}. Dette innebærer at hvis du har hatt høy nok opptjening kan du ta ut pensjon allerede fra måneden etter at du fyller 62 år. Du kan lese mer om hva ${
                    quoted(
                        "høy nok opptjening"
                    )
                } betyr under punktet ${quoted("valg av uttakstidspunkt - høyere pensjon jo lenger du venter")}."
            },
            english {
                +"Norway also has an option called ${quoted("flexible withdrawal of retirement pension from the age of 62")}. This means that if you have had high enough earnings, you can draw a pension from the month after you turn 62. You can read more about what ${
                    quoted(
                        "high enough earnings"
                    )
                } means under the heading ${quoted("choice of withdrawal time - higher pension the longer you wait")}."
            }
        )
    }
    title2 {
        text(
            bokmal { +"Valg av uttaksgrad" },
            english { +"Choice of withdrawal rate" }
        )
    }
    paragraph {
        list {
            item {
                text(
                    bokmal { +"Du kan velge hvor stor del av pensjonen du ønsker å ta ut, på følgende uttaksgrader: 20, 40, 50, 60, 80 eller 100 %." },
                    english { +"You can choose how much of the pension you want to take out, at the following withdrawal rates: 20, 40, 50, 60, 80 or 100%." }
                )
            }
            item {
                text(
                    bokmal { +"Hvis du har gradert uttak (mellom 20 -80 %) kan du endre uttaksgrad én gang i året. Imidlertid kan du alltid velge å endre uttaket til 100 % (gitt at du har høy nok opptjening) eller til 0 % (sette uttak av pensjon på pause). Slike endringer kan tidligst iverksettes måneden etter du søkte om endring." },
                    english { +"If you have a withdrawal rate between 20 -80% you can change this rate once a year. However, you can always choose to change the withdrawal to 100% (provided you have high enough earnings) or to 0% (pause pension withdrawal). Such changes can be implemented no earlier than the month after you applied for a change." }
                )
            }
        }
    }
    paragraph {
        text(
            bokmal { +"Hvis du har gradert uttak (mellom 20 -80 %) av alderspensjonen ved fylte 67 år, må du aktivt søke om å endre uttaksgraden til 100. Du får altså ikke automatisk 100 % alderspensjon ved fylte 67 år, selv om 67 år er ordinær pensjonsalder." },
            english { +"If you have a withdrawal rate between 20 and 80% at the age of 67, you must actively apply to change the withdrawal rate to 100. You will not automatically receive 100% of the old-age pension at the age of 67, even though 67 is the normal retirement age." }
        )
    }
    title2 {
        text(
            bokmal { +"Valg av uttakstidspunkt - høyere pensjon jo lenger du venter" },
            english { +"Choice of withdrawal time - higher pension the longer you wait" }
        )
    }
    paragraph {
        text(
            bokmal { +"Den årlige pensjonen beregnes ut fra hvor gammel du er når du velger å ta ut pensjon, og forventet levealder for ditt årskull. Jo lenger du venter før du tar ut alderspensjon, desto høyere blir årlig utbetalt pensjon. Vær oppmerksom på at tidlig uttak av pensjon vil gi deg lavere utbetalt pensjon per måned. Tidlig uttak betyr nemlig at du fordeler den opptjente pensjonen din over flere år, og dermed får du mindre utbetalt hvert år. For at du skal ha mulighet for tidlig uttak må din beregnede pensjon ved fylte 67 år være minst like stor som ditt årskulls minstepensjon. Dette betyr at ikke alle har krav på tidlig uttak, nettopp fordi det ikke skal være mulig å ende opp med mindre utbetalt pensjon ved fylte 67 år enn det man ville fått utbetalt på minstepensjon." },
            english { +"Your annual pension is calculated from how old you are when you choose to take out a pension, and the expected life expectancy for your age cohort. The longer you wait before drawing an old-age pension, the higher the annual pension paid out. Please note that early withdrawal of pension will give you a lower paid pension per month. Early withdrawal means that you spread your accrued pension over several years and thus receive less payment each year. In order for you to have the option of early withdrawal, your calculated pension at the age of 67 must be at least as large as your age cohort's minimum pension. This means that not everyone is entitled to early withdrawal, precisely because it should not be possible to end up with a smaller paid pension at the age of 67 than what you would have been paid on the minimum pension." }
        )
    }
    paragraph {
        text(
            bokmal { +"Det er viktig å merke seg er at minstepensjon er et variabelt begrep, og det avhenger av den totale trygdetiden. Den fastsatte statlige minstepensjonen gjelder for personer med 40 års norsk trygdetid. Dersom din trygdetid er mindre enn 40 år, vil minstepensjonsnivået beregnes proporsjonalt i forhold til antall år med faktisk norsk trygdetid." },
            english { +"It is important to note that the minimum pension is a variable term, and it depends on the total period of social security coverage. The fixed state minimum pension applies to persons with 40 years of Norwegian social security periods. If your social security period is less than 40 years, the minimum pension level will be calculated proportionally in relation to the number of years you have been insured in Norway." }
        )
    }
}