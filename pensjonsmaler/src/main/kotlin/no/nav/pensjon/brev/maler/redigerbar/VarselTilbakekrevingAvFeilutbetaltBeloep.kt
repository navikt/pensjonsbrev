package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.VARSEL
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.SaksbehandlerValgSelectors.hvisAktueltAaIleggeRentetillegg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Constants.BESKJED_TIL_NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.ETTERSENDELSE_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Redigerbar.SaksType
import no.nav.pensjon.brev.maler.fraser.vedlegg.vedleggVarselTilbakekreving
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.quoted
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.dokumentDato
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselTilbakekrevingAvFeilutbetaltBeloep : RedigerbarTemplate<VarselTilbakekrevingAvFeilutbetaltBeloepDto> {
    override val kategori: TemplateDescription.Brevkategori = VARSEL
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(FAM_PL, AFP, BARNEP, GJENLEV, ALDER, GENRL, AFP_PRIVAT)
    override val kode = Pesysbrevkoder.Redigerbar.PE_VARSEL_OM_TILBAKEKREVING_FEILUTBETALT_BELOEP
    override val template = createTemplate(
        name = kode.name,
        letterDataType = VarselTilbakekrevingAvFeilutbetaltBeloepDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - tilbakekreving av feilutbetalt beløp",
            isSensitiv = false,
            distribusjonstype = VIKTIG,
            brevtype = INFORMASJONSBREV
        )
    ) {
        title {
            text(
                Bokmal to "Vi vurderer om du må betale tilbake ",
                Nynorsk to "Vi vurderer om du må betale tilbake ",
                English to "We are considering demanding repayment of incorrectly paid ",
            )
            includePhrase(SaksType(pesysData.sakstype))
        }
        outline {
            paragraph {
                textExpr(
                    Bokmal to "Vi viser til vedtaket vårt ".expr()
                            + felles.dokumentDato.format() +
                            ". Du har fått " + fritekst("beløp") +
                            " kroner for mye utbetalt i ",

                    Nynorsk to "Vi viser til vedtaket vårt ".expr()
                            + felles.dokumentDato.format() +
                            ". Du har fått " + fritekst("beløp") +
                            " kroner for mykje utbetalt i ",

                    English to "We refer to our decision dated ".expr()
                            + felles.dokumentDato.format() +
                            ". You have received NOK " + fritekst("beløp") +
                            " too much in ",
                )

                includePhrase(SaksType(pesysData.sakstype))

                textExpr(
                    Bokmal to " fra og med ".expr() + fritekst("dato") + " til og med " +
                            fritekst("dato") + ".",
                    Nynorsk to " frå og med ".expr() + fritekst("dato") + " til og med " +
                            fritekst("dato") + ".",
                    English to " starting from ".expr() + fritekst("dato") + " up to and including " +
                            fritekst("dato") + ".",
                )
            }

            paragraph {
                text(
                    Bokmal to "Før vi avgjør om du skal betale tilbake, har du rett til å uttale deg. Dette må du gjøre innen 14 dager etter at du har fått dette varselet.",
                    Nynorsk to "Før vi avgjer om du skal betale tilbake, har du rett til å uttale deg. Dette må du gjere innan 14 dagar etter at du har fått dette varselet.",
                    English to "Before we decide whether you have to pay back the overpaid amount, you are entitled to make a statement. You must do this within 14 days of receiving this letter.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Dette er kun et varsel om at vi vurderer å kreve tilbake det feilutbetalte beløpet. Det er ikke et vedtak om tilbakekreving.",
                    Nynorsk to "Dette er berre eit varsel om at vi vurderer å krevje tilbake det feilutbetalte beløpet. Det er ikkje eit vedtak om tilbakekrevjing.",
                    English to "This letter is a prior notification that we are considering claiming repayment of the incorrectly paid amount, and not a final decision on repayment.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Dersom vi vedtar at du må betale tilbake hele eller deler av det feilutbetalte beløpet, trekker vi fra skatten på beløpet vi krever tilbake.",
                    Nynorsk to "Dersom vi vedtar at du må betale tilbake heile eller delar av det feilutbetalte beløpet, trekker vi frå skatten på beløpet vi krev tilbake.",
                    English to "If we decide you have to repay all or part of the overpaid amount, we will make a deduction for tax from the amount we are reclaiming payment of.",
                )
            }
            title1 {
                text(
                    Bokmal to "Dette har skjedd",
                    Nynorsk to "Dette har skjedd",
                    English to "What has happened",
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
                    Bokmal to "Dette legger vi vekt på i vurderingen vår",
                    Nynorsk to "Dette legg vi vekt på i vurderinga vår",
                    English to "We emphasizes the following in our assessment",
                )
            }
            paragraph {
                text(
                    Bokmal to "For å avgjøre om vi kan kreve tilbake, tar vi først stilling til:",
                    Nynorsk to "For å avgjere om vi kan krevje tilbake, tek vi først stilling til:",
                    English to "In order to determine whether we can demand repayment, we must first find out:",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Bokmal to "om du forstod eller burde forstått at beløpet du fikk utbetalt var feil",
                            Nynorsk to "om du forstod eller burde forstått at beløpet du fekk utbetalt var feil",
                            English to "whether you understood or ought to have understood that the amount you received was incorrect",
                        )
                    }
                    item {
                        text(
                            Bokmal to "om du har gitt riktig informasjon til Nav",
                            Nynorsk to "om du har gitt rett informasjon til Nav",
                            English to "whether you have provided Nav with correct information",
                        )
                    }
                    item {
                        text(
                            Bokmal to "om du har gitt all nødvendig informasjon til Nav i rett tid",
                            Nynorsk to "om du har gitt all nødvendig informasjon til Nav i rett tid",
                            English to "whether you have provided Nav with all the necessary information within the prescribed deadlines",
                        )
                    }
                }
                text(
                    Bokmal to "Selv om det er Nav som er skyld i feilutbetalingen, kan vi kreve at du betaler tilbake pengene. Dette går fram av folketrygdloven § 22-15.",
                    Nynorsk to "Sjølv om det er Nav som er skyld i feilutbetalinga, kan vi krevje at du betaler tilbake pengane. Dette går fram av folketrygdlova § 22-15.",
                    English to "We can also demand that you pay back the overpaid amount if Nav has made a mistake in paying out the wrong amount. This follows from section 22-15 of the National Insurance Act.",
                )
            }

            showIf(saksbehandlerValg.hvisAktueltAaIleggeRentetillegg) {
                paragraph {
                    text(
                        Bokmal to "Hvis du bevisst har gitt oss feil eller mangelfull informasjon eller opptrådt grovt uaktsomt, kan vi beregne et rentetillegg på ti prosent av beløpet vi krever tilbakebetalt. Dette går fram av folketrygdloven § 22-17a.",
                        Nynorsk to "Dersom du bevisst har gitt oss feil eller mangelfull informasjon eller har handla grovt aktlaust, kan vi berekne eit rentetillegg på ti prosent av beløpet som vi krev tilbakebetalt. Dette går fram av folketrygdlova § 22-17a.",
                        English to "If you have intentionally given us incorrect or insufficient information or have acted with gross negligence, we are entitled to add 10 per cent of the recoverable amount to the claim.  This follows from section 22-17a of the National Insurance Act.",
                    )
                }
            }

            title1 {
                text(
                    Bokmal to "Foreløpig vurdering",
                    Nynorsk to "Foreløpig vurdering",
                    English to "Preliminary assessment",
                )
            }
            paragraph {
                eval(
                    fritekst(
                        "foreløpig individuell vurdering " +
                                "Hvor åpenbar/ synlig var feilen? " +
                                "Forstod/ burde brukeren forstått at utbetalingen skyltes en feil?  " +
                                "Vis for eksempel til konkret informasjon som er gitt i tidligere vedtak. " +
                                "Har brukeren latt være å melde fra om endringer av betydning for ytelsen? " +
                                "Kan brukeren selv ha forårsaket feilutbetalingen ved å gi Nav feil eller mangelfulle opplysninger?"
                    )
                )
            }
            title1 {
                text(
                    Bokmal to "Slik uttaler du deg",
                    Nynorsk to "Slik uttaler du deg",
                    English to "How to make a statement",
                )
            }
            showIf(pesysData.sakstype.equalTo(Sakstype.ALDER)) {
                paragraph {
                    textExpr(
                        Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på Din Pensjon og velge ".expr() + quoted("Kontakt Nav om pensjon") +", eller logge deg inn på $BESKJED_TIL_NAV_URL og velge "+ quoted("Send beskjed til Nav") +". Du kan også sende uttalelsen din til oss i posten. Adressen finner du på $ETTERSENDELSE_URL.",
                        Nynorsk to "Du kan sende uttalen din ved å logge deg inn på Din Pensjon og velje ".expr() + quoted("Kontakt Nav om pensjon") +", eller logge deg inn på $BESKJED_TIL_NAV_URL og velje " + quoted("Send beskjed til Nav") +". Du kan også sende uttalen din til oss i posten. Adressa finn du på $ETTERSENDELSE_URL.",
                        English to "You can submit your statement by logging in to your personal ".expr() + quoted("Din Pensjon") +" pension page and selecting " + quoted("Kontakt Nav") +", or by logging in to $BESKJED_TIL_NAV_URL and selecting " + quoted("Send beskjed til Nav") +". You can also send us your statement by post. You can find the address at $ETTERSENDELSE_URL.",
                    )
                }
            }.orShow {
                paragraph {
                    textExpr(
                        Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på $BESKJED_TIL_NAV_URL og velge ".expr() + quoted("Send beskjed til Nav") + ". Du kan også sende uttalelsen din til oss i posten. Adressen finner du på $ETTERSENDELSE_URL.",
                        Nynorsk to "Du kan sende uttalen din ved å logge deg inn på $BESKJED_TIL_NAV_URL og velje ".expr() + quoted("Send beskjed til Nav") +". Du kan også sende uttalen din til oss i posten. Adressa finn du på $ETTERSENDELSE_URL.",
                        English to "You can submit your statement by logging in to $BESKJED_TIL_NAV_URL and selecting ".expr() + quoted("Send beskjed til Nav") +". You can also send us your statement by post. You can find the address at $ETTERSENDELSE_URL.",
                    )
                }
            }

            title1 {
                text(
                    Bokmal to "Hva skjer videre i din sak",
                    Nynorsk to "Kva skjer vidare i saka di",
                    English to "What happens next in your case",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi vil vurdere saken og sende deg et vedtak. Dersom du må betale hele eller deler av beløpet, vil du få beskjed om hvordan du betaler tilbake i vedtaket.",
                    Nynorsk to "Vi vil vurdere saka og sende deg eit vedtak. Dersom du må betale heile eller delar av beløpet, vil du få melding om korleis du betaler tilbake, i vedtaket.",
                    English to "We will consider your case and send you a decision. If you have to repay all or part of the overpaid amount, you will be notified about how to make the repayment in the decision.",
                )
            }

            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }

        includeAttachment(vedleggVarselTilbakekreving, argument)
    }

}