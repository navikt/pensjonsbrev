package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.inntektsaar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.AarligInntektsjusteringVedtakDTOSelectors.virkningstidspunkt

@TemplateModelHelpers
object OmstillingsstoenadInntektsjusteringVarsel: EtterlatteTemplate<AarligInntektsjusteringVedtakDTO>,
    Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNTEKTSJUSTERING_VARSEL

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Varselbrev - inntektsjustering",
                isSensitiv = true,
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            ),
        ) {
            title {
                text(
                    bokmal { +"Forhåndsvarsel om vurdering av omstillingsstønad for " + inntektsaar.format() },
                    nynorsk { +"Førehandsvarsel om vurdering av omstillingsstønad for " + inntektsaar.format() },
                    english { +"Advance notice of assessment of adjustment allowance for " + inntektsaar.format() },
                )
            }

            outline {
                paragraph {
                    text(
                        bokmal { +"Dette er et forhåndsvarsel om at vi vil fatte et nytt vedtak om omstillingsstønad fra 1. januar " + inntektsaar.format() + ", fordi omstillingsstønaden din skal beregnes ut fra inntekten du forventer å ha neste kalenderår." },
                        nynorsk { +"Dette er eit førehandsvarsel om at vi vil fatte eit nytt vedtak om omstillingsstønad frå 1. januar " + inntektsaar.format() + ", då omstillingsstønaden din skal reknast ut med utgangspunkt i inntekta du forventar å ha neste kalenderår." },
                        english { +"This is an advance notice that we will be passing a new decision regarding adjustment allowance from 1 January " + inntektsaar.format() + ", as your adjustment allowance will be calculated based on the income you are expecting in the next calendar year." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Sjekk beregningen og meld fra om du forventer annen inntekt" },
                        nynorsk { +"Sjekk utrekninga og meld frå dersom du forventar anna inntekt" },
                        english { +"Check the calculation and notify us if you are expecting a different income" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vi forhåndsvarsler deg slik at du kan sjekke om beregningen av omstillingsstønaden din fra 1. januar " + inntektsaar.format() + " er korrekt. Dette er en foreløpig beregning som vi har gjort på bakgrunn av opplysningene vi har om inntekten din i år." },
                        nynorsk { +"Vi varslar deg på førehand, slik at du kan sjekke at utrekninga av omstillingsstønaden din frå 1. januar " + inntektsaar.format() + " stemmer. Dette er ei førebels utrekning som vi har gjort på bakgrunn av opplysningane vi har om inntekta di i år." },
                        english { +"We notify you in advance so that you can check whether the calculation of your adjustment allowance from 1 January " + inntektsaar.format() + " is correct. This is a preliminary calculation, based on the information we hold about your income this year." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan se hvordan vi har beregnet omstillingsstønaden din i vedlegg til «Utkast – Vedtak om omstillingsstønad fra " + virkningstidspunkt.format() + "»." },
                        nynorsk { +"I vedlegget «Utkast – Vedtak om omstillingsstønad frå " + virkningstidspunkt.format() + "» kan du sjå korleis vi har rekna ut omstillingsstønaden din." },
                        english { +"You can see how we have calculated your adjustment allowance in the attachment to «Draft document – Decision regarding adjustment allowance from " + virkningstidspunkt.format() + "»." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Meld fra til oss hvis du forventer en annen inntekt neste år enn det som er lagt til grunn i vedlegget. Du får da et nytt vedtak med bakgrunn i de nye opplysningene du gir oss." },
                        nynorsk { +"Meld frå til oss dersom du forventar ei anna inntekt neste år enn den som er lagt til grunn i vedlegget. Du får då eit nytt vedtak basert på dei nye opplysningane du gir oss." },
                        english { +"Notify us if or you are expecting to earn a different income next year than what has been applied as a basis in the attachment. You will then receive a new decision based on the new information you provide." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Om du mener inntekten din ikke vil endres neste år, trenger du ikke melde fra til oss." },
                        nynorsk { +"Dersom du ikkje reknar med at inntekta di vil endre seg neste år, treng du ikkje melde frå til oss." },
                        english { +"If you believe that your income will not change next year, you do not need to notify us." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du ikke har gitt oss nye opplysninger eller innsigelser til «Utkast – Vedtak om omstillingsstønad fra " + virkningstidspunkt.format() + "» som er vedlagt, vil vedlegget anses som et vedtak fra 1. januar " + inntektsaar.format() + ". Du vil da ikke motta et nytt vedtaksbrev." },
                        nynorsk { +"Dersom du ikkje har nye opplysningar eller innvendingar til «Utkast – Vedtak om omstillingsstønad frå " + virkningstidspunkt.format() + "» som er lagt ved, vil vedlegget bli rekna som vedtak frå og med 1. januar " + inntektsaar.format() + ". Du vil då ikkje få eit nytt vedtaksbrev." },
                        english { +"If have not provided us with new information or objections to  the attached «Draft document – Decision regarding adjustment allowance from " + virkningstidspunkt.format() + "», the attachment will be considered a decision effective from 1 January " + inntektsaar.format() + ". You will then not receive a new decision letter." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Frister for å sende inn nye opplysninger" },
                        nynorsk { +"Fristar for å sende inn nye opplysningar" },
                        english { +"Deadline for submitting new information" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis opplysningene ikke stemmer, må du gi beskjed til oss senest innen fire uker fra du får dette forhåndsvarselet." },
                        nynorsk { +"Dersom opplysningane ikkje stemmer, må du gi oss beskjed seinast innan fire veker frå du får dette førehandsvarselet." },
                        english { +"If the information is not correct, you must notify us as soon as possible, no later than four weeks from the date you received this advance notice." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Hvordan melde fra?" },
                        nynorsk { +"Slik melder du frå" },
                        english { +"How to notify us" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan melde fra om endringer i forventet inntekt til neste år på ${Constants.OMS_MELD_INN_ENDRING_URL}." },
                        nynorsk { +"Du kan melde frå om endringar i forventa inntekt for neste år på ${Constants.OMS_MELD_INN_ENDRING_URL}." },
                        english { +"Notify us of changes in expected income for the next year at ${Constants.OMS_MELD_INN_ENDRING_URL}." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan også sende beskjed på nav.no/send-beskjed. Her får du ikke lagt ved dokumentasjon på inntekten din." },
                        nynorsk { +"Alternativt kan du sende beskjed på nav.no/send-beskjed. Ver merksam på at du her ikkje kan leggje ved dokumentasjon på inntekta di." },
                        english { +"You can also send as a message via: nav.no/send-beskjed. However you cannot attach documentation of your income using this method." },
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuIkkeBankID(bosattUtland))

                title2 {
                    text(
                        bokmal { +"Frist for å klage" },
                        nynorsk { +"Frist for å klage" },
                        english { +"Deadline for appeals" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Etter at vedtaket har tredd i kraft 1. januar " + inntektsaar.format() + ", har du seks ukers klagefrist på vedtaket. Du finner informasjon om hvordan du klager i «Utkast – Vedtak om omstillingsstønad fra " + virkningstidspunkt.format() + "»." },
                        nynorsk { +"Etter at vedtaket har tredd i kraft 1. januar " + inntektsaar.format() + ", har du seks veker på deg til å klage på vedtaket. Sjå «Utkast – Vedtak om omstillingsstønad frå " + virkningstidspunkt.format() + "» for meir informasjon om korleis du går fram for å klage.   " },
                        english { +"After the decision becomes effective on 1 January " + inntektsaar.format() + ", you have a deadline of six weeks to appeal against the decision. You can find further information on how to submit an appeal in «Draft document – Decision regarding adjustment allowance from " + virkningstidspunkt.format() + "»" },
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)

            }
        }
}

