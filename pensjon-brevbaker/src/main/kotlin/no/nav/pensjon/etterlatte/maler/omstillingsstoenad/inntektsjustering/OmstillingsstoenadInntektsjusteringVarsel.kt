package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadInntektsjusteringVarselDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadInntektsjusteringVarselDTOSelectors.inntektsaar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.inntektsjustering.OmstillingsstoenadInntektsjusteringVarselDTOSelectors.virkningstidspunkt
import java.time.LocalDate

data class OmstillingsstoenadInntektsjusteringVarselDTO(
    override val innhold: List<Element>,
    val inntektsaar: Int,
    val bosattUtland: Boolean,
    val virkningstidspunkt: LocalDate,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInntektsjusteringVarsel : EtterlatteTemplate<OmstillingsstoenadInntektsjusteringVarselDTO>,
    Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNTEKTSJUSTERING_VARSEL

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadInntektsjusteringVarselDTO::class,
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
                textExpr(
                    Bokmal to "Forhåndsvarsel om vurdering av omstillingsstønad for ".expr() + inntektsaar.format() ,
                    Nynorsk to "Førehandsvarsel om vurdering av omstillingsstønad for ".expr() + inntektsaar.format(),
                    English to "Advance notice of assessment of adjustment allowance for ".expr() + inntektsaar.format(),
                )
            }

            outline {
                paragraph {
                    textExpr(
                        Bokmal to "Dette er et forhåndsvarsel om at vi vil fatte et nytt vedtak om omstillingsstønad fra 1. januar ".expr() + inntektsaar.format()+", fordi omstillingsstønaden din skal beregnes ut fra inntekten du forventer å ha neste kalenderår.",
                        Nynorsk to "Dette er eit førehandsvarsel om at vi vil fatte eit nytt vedtak om omstillingsstønad frå 1. januar ".expr() + inntektsaar.format()+", då omstillingsstønaden din skal reknast ut med utgangspunkt i inntekta du forventar å ha neste kalenderår.",
                        English to "This is an advance notice that we will be passing a new decision regarding adjustment allowance from 1 January ".expr() + inntektsaar.format()+", as your adjustment allowance will be calculated based on the income you are expecting in the next calendar year.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Sjekk beregningen og meld fra om du forventer annen inntekt",
                        Nynorsk to "Sjekk utrekninga og meld frå dersom du forventar anna inntekt",
                        English to "Check the calculation and notify us if you are expecting a different income",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Vi forhåndsvarsler deg slik at du kan sjekke om beregningen av omstillingsstønaden din fra 1. januar ".expr() + inntektsaar.format()+" er korrekt. Dette er en foreløpig beregning som vi har gjort på bakgrunn av opplysningene vi har om inntekten din i år.",
                        Nynorsk to "Vi varslar deg på førehand, slik at du kan sjekke at utrekninga av omstillingsstønaden din frå 1. januar ".expr() + inntektsaar.format()+" stemmer. Dette er ei førebels utrekning som vi har gjort på bakgrunn av opplysningane vi har om inntekta di i år.",
                        English to "We notify you in advance so that you can check whether the calculation of your adjustment allowance from 1 January ".expr() + inntektsaar.format()+" is correct. This is a preliminary calculation, based on the information we hold about your income this year.",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Du kan se hvordan vi har beregnet omstillingsstønaden din i vedlegg til «Utkast – Vedtak om omstillingsstønad fra ".expr() + virkningstidspunkt.format() +"».",
                        Nynorsk to "I vedlegget «Utkast – Vedtak om omstillingsstønad frå ".expr() + virkningstidspunkt.format() +"» kan du sjå korleis vi har rekna ut omstillingsstønaden din.",
                        English to "You can see how we have calculated your adjustment allowance in the attachment to «Draft document – Decision regarding adjustment allowance from ".expr() + virkningstidspunkt.format() +"».",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Meld fra til oss hvis du forventer en annen inntekt neste år enn det som er lagt til grunn i vedlegget. Du får da et nytt vedtak med bakgrunn i de nye opplysningene du gir oss.",
                        Nynorsk to "Meld frå til oss dersom du forventar ei anna inntekt neste år enn den som er lagt til grunn i vedlegget. Du får då eit nytt vedtak basert på dei nye opplysningane du gir oss.",
                        English to "Notify us if or you are expecting to earn a different income next year than what has been applied as a basis in the attachment. You will then receive a new decision based on the new information you provide.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Om du mener inntekten din ikke vil endres neste år, trenger du ikke melde fra til oss.",
                        Nynorsk to "Dersom du ikkje reknar med at inntekta di vil endre seg neste år, treng du ikkje melde frå til oss.",
                        English to "If you believe that your income will not change next year, you do not need to notify us.",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Hvis du ikke har gitt oss nye opplysninger eller innsigelser til «Utkast – Vedtak om omstillingsstønad fra ".expr() + virkningstidspunkt.format() +"» som er vedlagt, vil vedlegget anses som et vedtak fra 1. januar "+inntektsaar.format()+". Du vil da ikke motta et nytt vedtaksbrev.",
                        Nynorsk to "Dersom du ikkje har nye opplysningar eller innvendingar til «Utkast – Vedtak om omstillingsstønad frå ".expr() + virkningstidspunkt.format() +"» som er lagt ved, vil vedlegget bli rekna som vedtak frå og med 1. januar "+inntektsaar.format()+". Du vil då ikkje få eit nytt vedtaksbrev.",
                        English to "If have not provided us with new information or objections to  the attached «Draft document – Decision regarding adjustment allowance from ".expr() + virkningstidspunkt.format() +"», the attachment will be considered a decision effective from 1 January "+inntektsaar.format()+". You will then not receive a new decision letter.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Frister for å sende inn nye opplysninger",
                        Nynorsk to "Fristar for å sende inn nye opplysningar",
                        English to "Deadline for submitting new information",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis opplysningene ikke stemmer, må du gi beskjed til oss snarest og innen fire uker fra du får dette forhåndsvarselet.",
                        Nynorsk to "Dersom opplysningane ikkje stemmer, må du gi oss beskjed seinast innan fire veker frå du får dette førehandsvarselet.",
                        English to "If the information is not correct, you must notify us as soon as possible, no later than four weeks from the date you received this advance notice.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvordan melde fra?",
                        Nynorsk to "Slik melder du frå",
                        English to "How to notify us",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Meld fra om endringer i inntekten din ved å sende skjema om endring av inntekt neste år. Dette finner du på nav.no/omstillingsstonad#har-inntekt.",
                        Nynorsk to "Meld frå om endringar i inntekta di ved å sende inn skjemaet om endring av inntekt for neste år. Du finn dette på nav.no/omstillingsstonad#har-inntekt.",
                        English to "Notify us of changes to your income by submitting the form for change of income next year. This is available from: nav.no/omstillingsstonad#har-inntekt.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan også sende beskjed på nav.no/send-beskjed. Her får du ikke lagt ved dokumentasjon på inntekten din.",
                        Nynorsk to "Alternativt kan du sende beskjed på nav.no/send-beskjed. Ver merksam på at du her ikkje kan leggje ved dokumentasjon på inntekta di.",
                        English to "You can also send as a message via: nav.no/send-beskjed. However you cannot attach documentation of your income using this method.",
                    )
                }

                showIf(bosattUtland) {
                    paragraph {
                        text(
                            Bokmal to "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, må du sende brev til Nav familie- og pensjonsytelser, Postboks 6600 Etterstad, 0607 Oslo, Norway.",
                            Nynorsk to "Dersom du ikkje har BankID eller andre måtar å logge deg på heimesida vår, nav.no, må du sende brev til Nav familie- og pensjonsytelser, Postboks 6600 Etterstad, 0607 Oslo, Norway.",
                            English to "If you do not have BankID or other means of logging into our home page nav.no, you must send a letter to Nav familie- og pensjonsytelser, P.O. Box 6600 Etterstad, 0607 Oslo, Norway.",
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            Bokmal to "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, må du sende brev til Nav skanning, Postboks 1400, 0109 OSLO.",
                            Nynorsk to "Dersom du ikkje har BankID eller andre måtar å logge deg på heimesida vår, nav.no, må du sende brev til Nav skanning, Postboks 1400, 0109 OSLO.",
                            English to "If you do not have BankID or other means of logging into our home page nav.no, you must send a letter to Nav skanning, P.O. Box 1400, 0109 OSLO, Norway.",
                        )
                    }
                }

                title2 {
                    text(
                        Bokmal to "Frist for å klage",
                        Nynorsk to "Frist for å klage",
                        English to "Deadline for appeals",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "Etter at vedtaket har tredd i kraft 1. januar ".expr() + inntektsaar.format() + ", har du seks ukers klagefrist på vedtaket. Du finner informasjon om hvordan du klager i «Utkast – Vedtak om omstillingsstønad fra " + virkningstidspunkt.format() + "».",
                        Nynorsk to "Etter at vedtaket har tredd i kraft 1. januar ".expr() + inntektsaar.format() + ", har du seks veker på deg til å klage på vedtaket. Sjå «Utkast – Vedtak om omstillingsstønad frå " + virkningstidspunkt.format() + "» for meir informasjon om korleis du går fram for å klage.   ",
                        English to "After the decision becomes effective on 1 January ".expr() + inntektsaar.format() + ", you have a deadline of six weeks to appeal against the decision. You can find further information on how to submit an appeal in «Draft document – Decision regarding adjustment allowance from " + virkningstidspunkt.format() + "»",
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)

            }
        }

    }