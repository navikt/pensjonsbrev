package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDto
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.erNyttEtteroppgjoer
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.harTjentOver80prosentAvOIFU
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.kanSoekeOmNyInntektsgrense
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.oppjustertInntektFoerUfoerhet
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.opplysningerOmEtteroppgjoeretUfoeretrygd
import no.nav.pensjon.brev.api.model.maler.ForhaandsvarselEtteroppgjoerUfoeretrygdDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.pensjonsgivendeInntektBruktIBeregningen
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.periode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.totaltAvvik
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggOpplysningerOmEtteroppgjoeret
import no.nav.pensjon.brev.maler.vedlegg.vedleggPraktiskInformasjonEtteroppgjoerUfoeretrygd
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.absoluteValue
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


// PE_UT_23_001 Varsel - etteroppgjør av uføretrygd ved feilutbetaling (auto)
// Brevet bestilles av BPEN092 (Etteroppgjør Uføre), brevet går ut til de som har fått for mye uføretrygd utbetalt.
// The conditional for showing the letter is: ResultatEO or ResultatForrigeEO = 'tilbakekr'

@TemplateModelHelpers
object ForhaandsvarselEtteroppgjoerUfoeretrygdAuto : AutobrevTemplate<ForhaandsvarselEtteroppgjoerUfoeretrygdDto> {

    override val kode = Pesysbrevkoder.AutoBrev.UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO

    override val template = createTemplate(
            name = kode.name,
            letterDataType = ForhaandsvarselEtteroppgjoerUfoeretrygdDto::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata = LetterMetadata(
                displayTitle = "Varsel - etteroppgjør av uføretrygd ved feilutbetaling (automatisk)",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            )
        ) {
            title {
                val periode = opplysningerOmEtteroppgjoeretUfoeretrygd.periode.format()
                showIf(erNyttEtteroppgjoer) {
                    text(
                        bokmal { + "Nytt forhåndsvarsel om etteroppgjør av uføretrygd for " + periode },
                        nynorsk { + "Nytt førehandsvarsel om etteroppgjer av uføretrygd for " + periode },
                        english { + "New notice of settlement for disability benefit for " + periode }
                    )
                }.orShow {
                    text(
                        bokmal { + "Forhåndsvarsel om etteroppgjør av uføretrygd for " + periode },
                        nynorsk { + "Førehandsvarsel om etteroppgjer av uføretrygd for " + periode },
                        english { + "Notice of settlement for disability benefit for " + periode }
                    )
                }
            }
            outline {
                paragraph {
                    text(
                        bokmal { + "Hvert år sjekker Nav inntekten din for å se om du har fått utbetalt riktig beløp i uføretrygd året før. Uføretrygden din er beregnet etter nye opplysninger om inntekt fra Skatteetaten." },
                        nynorsk { + "Kvart år sjekkar Nav inntekta di for å sjå om du fekk utbetalt rett beløp i uføretrygd året før. Uføretrygda di blir rekna ut etter nye opplysningar om inntekt frå Skatteetaten." },
                        english { + "Every year, Nav checks your income information in your tax settlement to see whether you have received the correct amount of disability benefit in the previous year. We use updated income information from the Norwegian Tax Administration to calculate your benefit." }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vår beregning viser at du har fått " + opplysningerOmEtteroppgjoeretUfoeretrygd.totaltAvvik.absoluteValue()
                            .format() + " for mye utbetalt." },
                        nynorsk { + "Utrekninga vår viser at du har fått utbetalt " + opplysningerOmEtteroppgjoeretUfoeretrygd.totaltAvvik.absoluteValue()
                            .format() + " for mykje." },
                        english { + "Our calculations show that you have received an overpayment of " + opplysningerOmEtteroppgjoeretUfoeretrygd.totaltAvvik.absoluteValue()
                            .format() + "." }
                    )
                }


                title1 {
                    text(
                        bokmal { + "Sjekk beregningen og meld fra hvis noe er feil" },
                        nynorsk { + "Sjekk utrekninga og meld frå dersom noko er feil" },
                        english { + "Check the calculation and inform us of any errors" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Dette brevet er et forhåndsvarsel, slik at du kan sjekke at beregningene i vedlegg " },
                        nynorsk { + "Dette brevet er eit førehandsvarsel, og du har såleis høve til å sjekke at utrekningane i vedlegget " },
                        english { + "This letter is an advance notice regarding the calculations provided in the appendix " },
                    )
                    namedReference(vedleggOpplysningerOmEtteroppgjoeret)
                    text(
                        bokmal { + " er korrekte, og melde fra til oss hvis noe er feil eller mangler." },
                        nynorsk { + " er korrekte, og melde frå til oss dersom noko er feil eller manglar." },
                        english { + ". Please review the calculations and inform us of any errors or missing information." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Hvis vi ikke hører fra deg innen 3 uker, tar vi utgangspunkt i at beregningene våre er korrekte, og sender saken videre til Skatteetaten. Beregningen blir gjort om til et vedtak etter 4 uker fra du mottok dette brevet. Du vil ikke motta et nytt vedtak." },
                        nynorsk { + "Dersom vi ikkje høyrer frå deg innan 3 veker, tek vi utgangspunkt i at utrekningane våre er korrekte, og sender saka vidare til Skatteetaten. Utrekninga blir gjort om til vedtak 4 veker etter at du har fått dette brevet. Du får ikkje eit nytt vedtak." },
                        english { + "If we do not hear from you within 3 weeks, we will assume that our calculations are correct and proceed to forward the case to the Norwegian Tax Administration. The calculation will be converted to an official decision 4 weeks from the date you received this letter. You will not receive a new decision letter." }
                    )
                }

                title1 {
                    text(
                        bokmal { + "Du vil få informasjon om hvordan du kan betale tilbake etter 4 uker" },
                        nynorsk { + "Etter 4 veker får du informasjon om korleis du kan betale tilbake" },
                        english { + "You will receive payment instructions after 4 weeks" }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Du vil få en faktura med informasjon fra Skatteetaten etter 4 uker om når og hvordan du kan betale tilbake pengene. Før du kan få svar på spørsmål om saken din eller kan betale tilbake, må du ha mottatt betalingsinformasjon fra Skatteetaten." },
                        nynorsk { + "Når det har gått 4 veker, vil Skatteetaten sende deg en faktura med informasjon om når og korleis du kan betale tilbake pengane. Før du kan få svar på spørsmål om saka di eller betale tilbake, må du ha fått betalingsinformasjon frå Skatteetaten." },
                        english { + "After 4 weeks, you will receive an invoice from the Norwegian Tax Administration with information about how and when to repay the overpaid amount. Please note that you must receive this information before we can answer any questions regarding your case, or for you to start repaying the amount owed." }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Fordi du har betalt skatt av det du har fått for mye utbetalt, vil vi trekke fra skatt fra beløpet du skal betale tilbake. I betalingsinformasjonen du får fra Skatteetaten står det hvor mye du faktisk skal betale tilbake." },
                        nynorsk { + "Ettersom du har betalt skatt av det du har fått for mykje utbetalt, vil vi trekkje frå skatten frå beløpet du skal betale tilbake. I betalingsinformasjonen frå Skatteetaten står det kor mykje du faktisk skal betale tilbake." },
                        english { + "As you have already paid tax on the overpaid amount, tax will be deducted from the amount you are required to repay. The payment information provided by the Norwegian Tax Administration will specify the exact amount you are required to repay." }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Du kan lese mer om tilbakebetaling i vedlegget " },
                        nynorsk { + "Du kan lese meir om tilbakebetaling i vedlegget " },
                        english { + "You can read more about repayment in the appendix " }
                    )
                    namedReference(vedleggPraktiskInformasjonEtteroppgjoerUfoeretrygd)
                    text(bokmal { + "." }, nynorsk { + "." }, english { + "." })
                }

                showIf(harTjentOver80prosentAvOIFU) {
                    title1 {
                        text(
                            bokmal { + "Du har tjent over 80 prosent av inntekten du hadde før du ble ufør" },
                            nynorsk { + "Du har tent over 80 prosent av inntekta du hadde før du blei ufør" },
                            english { + "You have earned over 80 percent of the income you had before you received disability benefit" }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Det er mulig at du ikke trenger å betale tilbake hele/deler av beløpet du har fått for mye utbetalt dersom inntektsøkningen skyldes arbeidsforsøk, jf. forskrift om uføretrygd § 4-1. Er dette aktuelt for deg, vil du få et eget brev." },
                            nynorsk { + "Det er mogleg at du ikkje treng å betale tilbake heile/delar av beløpet du har fått for mykje utbetalt dersom inntektsauken kjem av eit arbeidsforsøk, jf. forskrift om uføretrygd § 4-1. Dersom dette gjeld deg, vil du få eit eige brev." },
                            english { + "It is possible that you may not need to repay the entire or part of the amount that you have been overpaid if the increase in income is due to a work attempt, as specified in Section 4-1 of the regulations from the National Insurance Scheme. If this applies to your situation, you will receive a separate letter." }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "I " + opplysningerOmEtteroppgjoeretUfoeretrygd.periode.format() + " var 80 prosent av inntekten din før du ble ufør " + oppjustertInntektFoerUfoerhet.format() + ". " },
                            nynorsk { + "I " + opplysningerOmEtteroppgjoeretUfoeretrygd.periode.format() + " var 80 prosent av inntekta di før du blei ufør " + oppjustertInntektFoerUfoerhet.format() + ". " },
                            english { + "In " + opplysningerOmEtteroppgjoeretUfoeretrygd.periode.format() + ", 80 percent of your pre-disability benefit income was " + oppjustertInntektFoerUfoerhet.format() + ". " }
                        )
                        text(
                            bokmal { + "Du tjente " + opplysningerOmEtteroppgjoeretUfoeretrygd.pensjonsgivendeInntektBruktIBeregningen.format() + " i " + opplysningerOmEtteroppgjoeretUfoeretrygd.periode.format() + "." },
                            nynorsk { + "Du tente " + opplysningerOmEtteroppgjoeretUfoeretrygd.pensjonsgivendeInntektBruktIBeregningen.format() + " i " + opplysningerOmEtteroppgjoeretUfoeretrygd.periode.format() + "." },
                            english { + "You earned " + opplysningerOmEtteroppgjoeretUfoeretrygd.pensjonsgivendeInntektBruktIBeregningen.format() + " in " + opplysningerOmEtteroppgjoeretUfoeretrygd.periode.format() + "." }
                        )
                    }
                }

                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven § 12-14. " },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova § 12-14. " },
                        english { + "This decision is made in accordance with Section 12-14 of the National Insurance Act." },
                    )
                }

                showIf(kanSoekeOmNyInntektsgrense) {
                    title1 {
                        text(
                            bokmal { + "Søke om ny inntektsgrense" },
                            nynorsk { + "Søkje om ny inntektsgrense" },
                            english { + "Applying for a new income threshold" }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "Er du arbeidstaker og har gradert uføretrygd, kan du søke om ny inntektsgrense. Dette gjelder hvis du har hatt høy lønnsøkning, uten at det skyldes overtidsjobbing, ekstravakter eller høyere stillingsprosent." },
                            nynorsk { + "Dersom du er arbeidstakar og har gradert uføretrygd, kan du søkje om ny inntektsgrense. Dette gjeld viss du har hatt høg lønsauke, utan at det skuldast overtidsjobbing, ekstravakter eller høgare stillingsprosent." },
                            english { + "If you are an employee receiving partial disability benefit, you can apply for a new income threshold. This applies if your wage has significantly increased, unrelated to working overtime, extra shifts, or a higher job percentage." },
                        )
                    }
                }

                showIf(erNyttEtteroppgjoer) {
                    title1 {
                        text(
                            bokmal { + "For deg som har fått flere vedtak om etteroppgjør for samme år" },
                            nynorsk { + "For deg som har fått fleire vedtak om etteroppgjer for same år" },
                            english { + "Multiple post-settlements for the same year" }
                        )
                    }
                    paragraph {
                        text(
                            bokmal { + "I noen tilfeller gjør vi flere etteroppgjør for samme år. Alle oppgjør er gyldige. Vi sammenligner dem for å se om du må betale tilbake, eller om du har penger til gode." },
                            nynorsk { + "I enkelte tilfelle utfører vi fleire etteroppgjer for same år. Alle oppgjer er gyldige. Vi samanliknar dei for å sjå om du må betale tilbake, eller om du har pengar til gode." },
                            english { + "In some cases, we issue multiple post-settlements for the same year. All settlements are valid and apply. We compare them to see if you need to repay or if you are owed a refund." }
                        )
                    }
                }

                title1 {
                    text(
                        bokmal { + "Du må melde fra om endringer" },
                        nynorsk { + "Hugs å melde frå om endringar" },
                        english { + "Update your income information" }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "For at du skal få utbetalt riktig uføretrygd fremover, er det viktig at du oppdaterer inntekten din. Dette gjør du på ${Constants.INNTEKTSPLANLEGGEREN_URL}. I vedlegget " },
                        nynorsk { + "For at du skal få utbetalt rett uføretrygd framover, er det viktig at du oppdaterer inntekta di. Dette gjer du på ${Constants.INNTEKTSPLANLEGGEREN_URL}. I vedlegget " },
                        english { + "To ensure that you receive the correct amount of disability benefit in the future, it is important that you update your income information. You can do this at ${Constants.INNTEKTSPLANLEGGEREN_URL}. In the appendix " }
                    )
                    namedReference(vedleggDineRettigheterOgPlikterUfoere)
                    text(
                        bokmal { + " ser du hvilke endringer du må si fra om." },
                        nynorsk { + " ser du kva endringar du må seie frå om." },
                        english { + ", you can find the changes you need to report." }
                    )
                }

                title1 {
                    text(
                        bokmal { + "Frist for å sende inn nye opplysninger" },
                        nynorsk { + "Frist for å sende inn nye opplysningar" },
                        english { + "Deadline for submitting new information" }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Hvis du mener at beregningene i vedlegg " },
                        nynorsk { + "Dersom du meiner at utrekningane i vedlegg " },
                        english { + "If you believe that the calculations in appendix " }
                    )
                    namedReference(vedleggOpplysningerOmEtteroppgjoeret)
                    text(bokmal { + " er feil, må du melde fra til oss innen 3 uker fra du fikk dette brevet. Du vil da få en ny vurdering og et nytt vedtak." },
                        nynorsk { + " er feil, må du melde frå til oss innan 3 veker frå du fekk dette brevet. Du vil då få ei ny vurdering og eit nytt vedtak." },
                        english { + " are incorrect, please notify us within 3 weeks from the date you received this letter. You will then receive a new assessment and a new decision." })
                }
                paragraph {
                    text(
                        bokmal { + "I vedlegget " },
                        nynorsk { + "I vedlegget " },
                        english { + "You can read about how to submit documentation in the appendix " }
                    )
                    namedReference(vedleggPraktiskInformasjonEtteroppgjoerUfoeretrygd)
                    text(
                        bokmal { + " kan du lese om hvordan du ettersender dokumentasjon." },
                        nynorsk { + " kan du lese om korleis du ettersendar dokumentasjon." },
                        english { + "." }
                    )
                }

                title1 {
                    text(
                        bokmal { + "Frist for å klage" },
                        nynorsk { + "Frist for å klage" },
                        english { + "Deadline for submitting an appeal" }
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Dette er et varselsbrev. Du har en frist på 3 uker fra du får varselet til å komme med tilbakemeldinger eller nye opplysninger. Hører vi ikke fra deg, blir varselet automatisk omgjort til et vedtak etter 4 uker. Fra vedtaket er gyldig, er fristen for å klage på vedtaket 6 uker. Du finner skjema og informasjon om hvordan du klager på ${Constants.KLAGE_URL}. Du må som hovedregel begynne å betale tilbake selv om du klager på vedtaket, se forvaltningsloven § 42." },
                        nynorsk { + "Dette er eit varselbrev. Du har ein frist på 3 veker frå du får varselet til å komma med tilbakemeldingar eller nye opplysningar. Høyrer vi ikkje frå deg, blir varselet automatisk omgjort til eit vedtak etter 4 veker. Frå vedtaket er gyldig, er fristen for å klaga på vedtaket 6 veker. Du finn skjema og informasjon om korleis du klagar, på ${Constants.KLAGE_URL}. Du må som hovudregel byrje å betale tilbake sjølv om du klagar på vedtaket, jf. forvaltingslova § 42." },
                        english { + "The deadline for appealing the decision is 6 weeks after the case has been forwarded to the Norwegian Tax Administration. You can find forms and information about how to appeal at ${Constants.KLAGE_URL}. As a rule, you are required to start repaying even if you appeal the decision, as stated in section 42 of the Public Administration Act." }
                    )
                }

                includePhrase(Felles.HarDuSpoersmaal(Constants.ETTEROPPGJOR_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
            }
            includeAttachment(vedleggOpplysningerOmEtteroppgjoeret, opplysningerOmEtteroppgjoeretUfoeretrygd)
            includeAttachment(vedleggPraktiskInformasjonEtteroppgjoerUfoeretrygd)
            includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
        }
}
