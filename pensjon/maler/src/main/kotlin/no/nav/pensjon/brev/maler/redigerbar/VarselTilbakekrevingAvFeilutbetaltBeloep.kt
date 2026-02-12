package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.SaksbehandlerValgSelectors.hvisAktueltAaIleggeRentetillegg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselTilbakekrevingAvFeilutbetaltBeloep : RedigerbarTemplate<VarselTilbakekrevingAvFeilutbetaltBeloepDto> {
    override val kategori = Brevkategori.FEILUTBETALING
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(FAM_PL, AFP, BARNEP, GJENLEV, ALDER, GENRL, AFP_PRIVAT)
    override val kode = Pesysbrevkoder.Redigerbar.PE_VARSEL_OM_TILBAKEKREVING_FEILUTBETALT_BELOEP
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - tilbakekreving av feilutbetalt beløp",
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        val sakstype = pesysData.sakstype.format().ifNull(fritekst("ytelse"))
        title {
            text(
                bokmal { +"Vi vurderer om du må betale tilbake " + sakstype },
                nynorsk { +"Vi vurderer om du må betale tilbake " + sakstype },
                english { +"We are considering demanding repayment of incorrectly paid " + sakstype },
            )
        }
        outline {
            paragraph {
                val dato = fritekst("dato")
                text(
                    bokmal {
                        +"Vi viser til vedtaket vårt "
                        +fritekst("dato") +
                                ". Du har fått " + fritekst("beløp") +
                                " kroner for mye utbetalt i " + sakstype + " fra "
                        +dato + " til og med " + dato + "."
                    },

                    nynorsk {
                        +"Vi viser til vedtaket vårt "
                        +fritekst("dato") +
                                ". Du har fått " + fritekst("beløp") +
                                " kroner for mykje utbetalt i " + sakstype + " frå "
                        +dato + " til og med " + dato + "."
                    },

                    english {
                        +"We refer to our decision dated "
                        +fritekst("dato") + ". You have received NOK " + fritekst("beløp") +
                                " too much in " + sakstype + " starting from " + dato +
                                " up to and including " + dato + "."
                    },
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis du har opplysninger vi bør vite om når vi vurderer om du skal betale tilbake beløpet, ber vi om at du uttaler deg. Det må du gjøre innen 14 dager etter at du har fått dette varselet." },
                    nynorsk { +"Om du har opplysningar vi bør vite om når vi vurderer om du skal betale tilbake beløpet, ber vi om at du uttaler deg. Det må du gjere innan 14 dagar etter at du har fått dette varselet." },
                    english { +"If you have any information that may be relevant to our assessment of whether you should repay the amount, we ask that you provide a statement. You must do this within 14 days of receiving this letter." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Dette er bare et varsel om at vi vurderer å kreve tilbake det feilutbetalte beløpet. Du får et vedtak når saken er ferdig behandlet." },
                    nynorsk { +"Dette er berre eit varsel om at vi vurderer å krevje tilbake det feilutbetalte beløpet. Du får eit vedtak når saka er ferdig behandla." },
                    english { +"This letter is a prior notification that we are considering claiming repayment of the incorrectly paid amount. You will receive a decision when your case has been processed." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Hvis vi vedtar at du må betale tilbake hele eller deler av det feilutbetalte beløpet, trekker vi fra skatten på beløpet vi krever tilbake." },
                    nynorsk { +"Om vi vedtar at du må betale tilbake heile eller delar av det feilutbetalte beløpet, trekker vi frå skatten på beløpet vi krev tilbake." },
                    english { +"If we decide that you must repay all or part of the overpaid amount, we will make a deduction for tax from the amount being reclaimed." },
                )
            }
            title1 {
                text(
                    bokmal { +"Dette har skjedd" },
                    nynorsk { +"Dette har skjedd" },
                    english { +"What has happened" },
                )
            }
            paragraph {
                eval(
                    fritekst(
                        "beskriv hva som har skjedd i saken/ årsaken til feilutbetalingen. " +
                                "Gjør kort rede for årsaken til feilutbetalingen. " +
                                "Er årsaken den samme for hele perioden? " +
                                "Er det vi som har gjort en feil eller er det mottakeren? " +
                                "Har mottakeren gitt feilaktige/mangelfulle opplysninger? " +
                                "Har mottakeren latt være å melde fra om endringer av betydning? " +
                                "Har Nav fulgt opp melding fra mottakeren uten unødig opphold? " +
                                "Har Nav hatt tilgang til opplysningene fra andre kilder enn mottakeren?"
                    )
                )
            }

            title1 {
                text(
                    bokmal { +"Dette legger vi vekt på i vurderingen vår" },
                    nynorsk { +"Dette legg vi vekt på i vurderinga vår" },
                    english { +"We consider the following in our assessment" },
                )
            }
            paragraph {
                text(
                    bokmal { +"For å avgjøre om vi kan kreve tilbake, vurderer vi blant annet" },
                    nynorsk { +"For å avgjere om vi kan krevje tilbake, vurderer vi mellom anna" },
                    english { +"To determine whether we can demand repayment, we consider, among other things" },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"om du forsto eller burde forstått at beløpet du fikk utbetalt var feil" },
                            nynorsk { +"om du forstod eller burde forstått at beløpet du fekk utbetalt var feil" },
                            english { +"Whether you understood or should have understood that the amount you received was incorrect" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"om du har gitt riktig informasjon til Nav" },
                            nynorsk { +"om du har gitt rett informasjon til Nav" },
                            english { +"Whether you have provided Nav with correct information" },
                        )
                    }
                    item {
                        text(
                            bokmal { +"om du har gitt all nødvendig informasjon til Nav i rett tid" },
                            nynorsk { +"om du har gitt all nødvendig informasjon til Nav i rett tid" },
                            english { +"Whether you have provided Nav with all the necessary information within the prescribed deadlines" },
                        )
                    }
                }
                text(
                    bokmal { +"Selv om det er Nav som er skyld i feilutbetalingen, kan vi kreve at du betaler tilbake hele eller deler av beløpet." },
                    nynorsk { +"Sjølv om det er Nav som er skyld i feilutbetalinga, kan vi krevje at du betaler tilbake heile eller delar av beløpet." },
                    english { +"Even if Nav is responsible for the overpayment, we may still require you to repay all or part of the amount." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Dette går fram av folketrygdloven § 22-15." },
                    nynorsk { +"Dette går fram av folketrygdlova § 22-15." },
                    english { +"This follows from section 22-15 of the National Insurance Act." },
                )
            }

            showIf(saksbehandlerValg.hvisAktueltAaIleggeRentetillegg) {
                paragraph {
                    text(
                        bokmal { +"Hvis du bevisst har gitt oss feil eller mangelfull informasjon eller opptrådt grovt uaktsomt, kan vi beregne et rentetillegg på ti prosent av beløpet vi krever tilbakebetalt. Dette går fram av folketrygdloven § 22-17a." },
                        nynorsk { +"Dersom du bevisst har gitt oss feil eller mangelfull informasjon eller har handla grovt aktlaust, kan vi berekne eit rentetillegg på ti prosent av beløpet som vi krev tilbakebetalt. Dette går fram av folketrygdlova § 22-17a." },
                        english { +"If you have intentionally given us incorrect or insufficient information or have acted with gross negligence, we are entitled to add 10 per cent of the recoverable amount to the claim.  This follows from section 22-17a of the National Insurance Act." },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Slik uttaler du deg" },
                    nynorsk { +"Slik uttaler du deg" },
                    english { +"How to make a statement" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du har rett til å uttale deg før vi tar den endelige avgjørelsen om tilbakebetaling. Du kan skrive til oss på ${Constants.KONTAKT_URL} eller ringe oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}." },
                    nynorsk { +"Du har rett til å uttale deg får vi tar den endelege avgjerda om tilbakebetaling. Du kan skrive til oss på ${Constants.KONTAKT_URL} eller ringje oss på telefon ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}." },
                    english { +"You have the right to provide your comments before we make a final decision about repayment. You can write to us at ${Constants.KONTAKT_URL} or call us at ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan også sende et skriftlig svar til:" },
                    nynorsk { +"Du kan også sende eit skriftleg svar til:" },
                    english { +"You can also send a written response to:" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Nav Familie- og pensjonsytelser" },
                    nynorsk { +"Nav Familie- og pensjonsytingar " },
                    english { +"Nav Familie- og pensjonsytelser" },
                )
                newline()
                text(
                    bokmal { +"Postboks 6600 Etterstad" },
                    nynorsk { +"Postboks 6600 Etterstad" },
                    english { +"Postboks 6600 Etterstad" },
                )
                newline()
                text(
                    bokmal { +"0607 Oslo" },
                    nynorsk { +"0607 Oslo" },
                    english { +"0607 Oslo" }
                )
            }

            title1 {
                text(
                    bokmal { +"Hva skjer videre i saken din" },
                    nynorsk { +"Kva skjer vidare i saka di" },
                    english { +"What happens next in your case" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi vil vurdere saken og sende deg et vedtak. Hvis du må betale hele eller deler av beløpet, vil du få beskjed om hvordan du betaler tilbake i vedtaket. Nav kan gjøre trekk i framtidige utbetalinger for å kreve inn beløpet." },
                    nynorsk { +"Vi vil vurdere saka og sende deg eit vedtak. Dersom du må betale heile eller delar av beløpet, vil du få melding om korleis du betaler tilbake i vedtaket. Nav kan gjere trekk i framtidige utbetalingar for å krevje inn beløpet." },
                    english { +"We will consider your case and send you a decision. If you must repay all or part of the overpaid amount, you will be notified about how to make the repayment in the decision. Nav can make deductions from future payments to collect the amount." },
                )
            }

            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }

}
