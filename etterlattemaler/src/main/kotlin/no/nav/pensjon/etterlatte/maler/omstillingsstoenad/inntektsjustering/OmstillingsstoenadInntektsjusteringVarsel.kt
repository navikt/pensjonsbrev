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
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.selectors.aarligInntektsjusteringVedtakDTO.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.selectors.aarligInntektsjusteringVedtakData.*

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
                distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            ),
        ) {
            title {
                text(
                    bokmal { +"Forhåndsvarsel om vurdering av omstillingsstønad for " + data.inntektsaar.format() },
                    nynorsk { +"Førehandsvarsel om vurdering av omstillingsstønad for " + data.inntektsaar.format() },
                    english { +"Advance notice of assessment of adjustment allowance for " + data.inntektsaar.format() },
                )
            }

            outline {
                paragraph {
                    text(
                        bokmal { +"Vi varsler deg om at vi vil fatte et nytt vedtak om omstillingsstønad fra 1. januar " + data.inntektsaar.format() + ". Det er fordi omstillingsstønaden skal beregnes ut fra inntekten du forventer å ha i " + data.inntektsaar.format() + "." },
                        nynorsk { +"Vi varslar deg om at vi vil fatte eit nytt vedtak om omstillingsstønad frå 1. januar " + data.inntektsaar.format() + ". Grunnen er at omstillingsstønaden skal reknast ut med utgangspunkt i inntekta du forventar å ha i " + data.inntektsaar.format() + "." },
                        english { +"We would like to inform you that we will be making a new decision regarding your adjustment allowance with effect from 1 January " + data.inntektsaar.format() + ". This is because the adjustment allowance will be calculated based on the income you are expecting to receive in " + data.inntektsaar.format() + "." },
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
                        bokmal { +"Vi ber deg sjekke om beregningen av omstillingsstønaden din fra 1. januar " + data.inntektsaar.format() + " er riktig. Beregningen er foreløpig og basert på opplysningene vi har om inntekten din i år." },
                        nynorsk { +"Vi ber deg sjekke at utrekninga av omstillingsstønaden din frå 1. januar " + data.inntektsaar.format() + " stemmer. Utrekninga er førebels og på bakgrunn av opplysningane vi har om inntekta di i år." },
                        english { +"Please check that the calculation of your adjustment allowance from 1 January " + data.inntektsaar.format() + " is correct. This is a preliminary calculation, based on the information we hold about your income this year." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du finner beregningen i vedlegget «Utkast – Vedtak om omstillingsstønad fra " + data.virkningstidspunkt.format() + "»." },
                        nynorsk { +"I vedlegget «Utkast – Vedtak om omstillingsstønad frå " + data.virkningstidspunkt.format() + "» kan du sjå korleis vi har rekna ut omstillingsstønaden din." },
                        english { +"You can see how we have calculated your adjustment allowance in the attachment to «Draft document – Decision regarding adjustment allowance from " + data.virkningstidspunkt.format() + "»." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis du forventer en annen inntekt neste år enn det som er lagt til grunn i vedlegget, må du gi oss beskjed. Vi vil da fatte et nytt vedtak på bakgrunn av opplysningen du sender inn. Hvis inntekten din ikke vil endre seg neste år, trenger du ikke melde fra til oss." },
                        nynorsk { +"Meld frå til oss dersom du forventar ei anna inntekt neste år enn den som er lagt til grunn i vedlegget. Du får då eit nytt vedtak basert på dei nye opplysningane du gir oss. Dersom du ikkje reknar med at inntekta di vil endre seg neste år, treng du ikkje melde frå til oss." },
                        english { +"Notify us if or you are expecting to earn a different income next year than what has been applied as a basis in the attachment. You will then receive a new decision based on the new information you provide. If you believe that your income will not change next year, you do not need to notify us." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis vi ikke hører fra deg, vil vedlagte «Utkast – Vedtak om omstillingsstønad fra " + data.virkningstidspunkt.format() + "» bli vedtaket ditt fra 1. januar " + data.inntektsaar.format() + ". Du vil da ikke motta et nytt vedtaksbrev." },
                        nynorsk { +"Dersom du ikkje har nye opplysningar eller innvendingar til «Utkast – Vedtak om omstillingsstønad frå " + data.virkningstidspunkt.format() + "» som er lagt ved, vil vedlegget bli rekna som vedtak frå og med 1. januar " + data.inntektsaar.format() + ". Du vil då ikkje få eit nytt vedtaksbrev." },
                        english { +"If have not provided us with new information or objections to  the attached «Draft document – Decision regarding adjustment allowance from " + data.virkningstidspunkt.format() + "», the attachment will be considered a decision effective from 1 January " + data.inntektsaar.format() + ". You will then not receive a new decision letter." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Frist for å sende inn nye opplysninger" },
                        nynorsk { +"Frist for å sende inn nye opplysningar" },
                        english { +"Deadline for submitting new information" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis opplysningene ikke stemmer, må du gi beskjed til oss senest innen 4 uker fra du får dette brevet." },
                        nynorsk { +"Dersom opplysningane ikkje stemmer, må du gi oss beskjed seinast innan 4 veker frå du får dette brevet." },
                        english { +"If the information is not correct, you must notify us as soon as possible, no later than 4 weeks from the date you received this letter." },
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

                includePhrase(OmstillingsstoenadFellesFraser.HarDuIkkeBankID(data.bosattUtland))

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)

            }
        }
}

