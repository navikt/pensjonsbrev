package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingstoenadInformasjonDoedsfallDTOSelectors.avdoedNavn

data class OmstillingstoenadInformasjonDoedsfallDTO(
    override val innhold: List<Element>,
    val avdoedNavn: String,
) : BrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInformasjonDoedsfall : EtterlatteTemplate<OmstillingstoenadInformasjonDoedsfallDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INFORMASJON_DOEDSFALL

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingstoenadInformasjonDoedsfallDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Informasjon om omstillingsstønad",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            ),
        ) {
            title {
                text(
                    Bokmal to "Informasjon om omstillingsstønad",
                    Nynorsk to "Informasjon om omstillingsstønad",
                    English to "Information about adjustment allowance",
                )
            }

            outline {
                paragraph {
                    textExpr(
                        Bokmal to "Vi skriver til deg fordi vi har fått melding om at ".expr() + avdoedNavn + " er død, og du kan ha rett til omstillingsstønad.".expr(),
                        Nynorsk to "Vi skriv til deg fordi vi har fått melding om at ".expr() + avdoedNavn + " er død, og du kan ha rett til omstillingsstønad. ".expr(),
                        English to "We are writing to you because we have received notice that ".expr() + avdoedNavn + " has died, and we would like to inform you about children's pension.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvem kan ha rett til omstillingsstønad?",
                        Nynorsk to "Kven kan ha rett til omstillingsstønad?",
                        English to "Who may be entitled to a adjustment allowance?",
                    )
                }
                paragraph {
                    list {
                        item {
                            text(
                                Bokmal to "For å ha rett til omstillingsstønad, må du som hovedregel være medlem i folketrygden og under 20 år.",
                                Nynorsk to "For å ha rett til omstillingsstønad må du som hovudregel vere medlem i folketrygda og under 20 år.",
                                English to "To be entitled to a adjustment allowance, you must as a rule have been a member of the National Insurance Scheme and under the age of 20.",
                                )
                        }
                        item {
                            text(
                                Bokmal to "ha vært gift med den avdøde i minst fem år, eller  ",
                                Nynorsk to "ha vore gift med den avdøde i minst fem år, eller  ",
                                English to "have been married to the deceased for at least five years, or  "
                            )
                        }
                        item {
                            text(
                                Bokmal to "ha vært gift eller samboer med den avdøde og ha eller ha hatt barn med den avdøde, eller  ",
                                Nynorsk to "ha vore gift eller sambuar med den avdøde og ha eller ha hatt barn med den avdøde, eller  ",
                                English to "have been married to or a cohabitant with the deceased, and have/had children together, or  "
                            )
                        }
                        item {
                            text(
                                Bokmal to "ha omsorg for barn med minst halvparten av tiden.  ",
                                Nynorsk to "ha omsorg for barn med minst halvparten av full tid.",
                                English to "have care of children at least half the time.  "
                            )
                        }
                    }
                }

                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om disse rettighetene på nav.no/omstillingsstonad",
                        Nynorsk to "You can read more about this at nav.no/omstillingsstonad",
                        English to "Du kan lese meir om desse rettane på nav.no/omstillingsstonad.  ",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Kort om omstillingsstønaden ",
                        Nynorsk to "Kort om omstillingsstønaden  ",
                        English to "What is an adjustment allowance? ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Omstillingsstønad er en tidsbegrenset stønad som vanligvis varer i tre år.  Støtten skal sikre inntekt og gi hjelp til selvhjelp i en omstillingsperiode etter dødsfallet. Etter seks måneder må du som hovedregel være i arbeid eller en annen aktivitet slik at du etter hvert kan forsørge deg selv.",
                        Nynorsk to "Omstillingsstønad er ein tidsavgrensa stønad som vanlegvis varar i tre år. Støtta skal sikre inntekt og gi hjelp til sjølvhjelp i ein omstillingsperiode etter dødsfallet. Etter seks månader må du som hovudregel være i arbeid eller en annan aktivitet slik at du etter kvart kan forsørgje deg sjølv.",
                        English to "The adjustment allowance is a time-limited benefit that normally only lasts three years. The allowance is intended to guarantee an income and to help you help yourself in an adjustment period after the death. After six months, you will normally be expected to have found employment or be participating in another type of activity, so that you can eventually provide for yourself. ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du tar nødvendig utdanning eller trenger hjelp til å få jobb, kan du søke om å få pensjonen forlenget med inntil to år.   ",
                        Nynorsk to "Om du tar naudsynt utdanning eller treng hjelp til å få jobb, kan du søkje om å få pensjonen forlengja med inntil to år. ",
                        English to "The benefit may be extended by up to two years, if you need help finding employment, such as completing an education or work training. ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Er du født i 1963 eller tidligere og har hatt lav arbeidsinntekt de siste fem årene før dødsfallet, kan du få stønaden til du blir 67 år. ",
                        Nynorsk to "Er du født i 1963 eller tidlegare og har hatt låg arbeidsinntekt dei siste fem åra før dødsfallet, kan du få stønaden til du blir 67 år. ",
                        English to "If you were born in 1963 or earlier and you had a low earned income in the last five years before the death, you may keep the benefit until you turn 67 years old. ",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvor mye kan du få? ",
                        Nynorsk to "Hvor mye kan du få? ",
                        English to "How much are you entitled to? ",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Stønaden er 2,25 ganger grunnbeløpet per år. Hvis den avdøde har bodd utenfor Norge etter fylte 16 år, kan det få betydning for størrelsen. ",
                        Nynorsk to "Stønaden er 2,25 gongar grunnbeløpet per år. Viss den avdøde har budd utanfor Noreg etter fylte 16 år, kan det påverke stønaden. ",
                        English to "The benefit is 2.25 times the National Insurance basic amount G, depending on the period of national insurance coverage for the person who died. ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du har arbeidsinntekt, blir stønaden redusert med 45 prosent av den inntekten som overstiger halve folketrygdens grunnbeløp. Noen ytelser, som for eksempel sykepenger og dagpenger, likestilles med arbeidsinntekt. ",
                        Nynorsk to "Viss du har arbeidsinntekt, blir stønaden redusert med 45 prosent av den inntekta som overstig halve grunnbeløpet i folketrygda. Nokre ytingar, som til dømes sjukepengar og dagpengar, er likestilte med arbeidsinntekt. ",
                        English to "The adjustment allowance will be reduced on the basis of income that you receive or expect to receive. Some benefits, such as sickness benefits and unemployment benefits are equivalent to earned income. Your pension will be reduced by 45 per cent of your income that exceeds half of the National Insurance basic amount. ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om grunnbeløp på nav.no/grunnbelop. ",
                        Nynorsk to "Du kan lese meir om grunnbeløp på nav.no/grunnbelop. ",
                        English to "You can read more about the National Insurance basic amount at nav.no/grunnbelop. ",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvordan søker du? ",
                        Nynorsk to "Korleis søkjer du? ",
                        English to "Other pension schemes ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Vi oppfordrer deg til å søke så snart som mulig fordi vi vanligvis kun etterbetaler for tre måneder. Det er nye regler for gjenlevendeytelser fra 1. januar 2024. Tidspunktet for når NAV mottar søknaden din kan ha betydning for hvilke regler som gjelder for deg.  ",
                        Nynorsk to "Vi oppmodar deg til å søkje så snart som mogleg fordi vi vanlegvis berre etterbetaler for tre månader. Det er nye reglar for ytingar til attlevande frå 1. januar 2024. Tidspunktet for når NAV mottar søknaden din kan ha betydning for kva regler som gjeld for deg.  ",
                        English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company.  ",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Dersom du bor i utlandet, må du kontakte trygdemyndigheten i bostedslandet ditt. ",
                        Nynorsk to "Dersom du bur i utlandet, må du kontakte trygdemyndigheitene i bustadlandet ditt. ",
                        English to "If you are receiving tariff-based early retirement pensions (AFP) in the public sector, you should contact us for further guidance. ",
                    )
                }



                title2 {
                    text(
                        Bokmal to "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet",
                        Nynorsk to "Rettar når avdøde har budd eller arbeidd i utlandet",
                        English to "Rights if the deceased has lived or worked abroad",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis avdøde har bodd eller arbeidet i utlandet, kan dette få betydning for hvor mye du får ubetalt. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rettigheter fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.",
                        Nynorsk to "Dersom avdøde har budd eller arbeidd i utlandet, kan det påverke kor mykje du får ubetalt. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rettar frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med.",
                        English to "If the deceased has lived or worked abroad, this may affect the amount of your pension. Norway cooperates with a number of countries through the EEA Agreement and other social security agreements. Therefore, you may also be entitled to a pension from other countries. We can assist you with your application to countries with which Norway has a social security agreement.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Andre ytelser du kan ha rett til som gjenlevende ",
                        Nynorsk to "Andre ytingar du kan ha rett til som attlevande ",
                        English to "",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan også ha rett til stønad til barnetilsyn, tilleggsstønad og stønad til skolepenger. Forsørger du barn under 18 år, kan du ha rett til utvidet barnetrygd. ",
                        Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet. ",
                        English to "",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Andre pensjonsordninger",
                        Nynorsk to "Andre pensjonsordningar",
                        English to "Other pension schemes",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet. ",
                        Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet. ",
                        English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company. ",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Har du spørsmål?",
                        Nynorsk to "Har du spørsmål?",
                        English to "Do you have any questions?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du finner mer informasjon på nav.no/omstillingsstønad. På nav.no/kontakt kan du chatte eller skrive til oss.",
                        Nynorsk to "Du finn meir informasjon på nav.no/omstillingsstønad. Du kan chatte med oss eller skrive til oss på nav.no/kontakt.",
                        English to "You can find more information at nav.no/omstillingsstønad.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon + 47 55 55 33 34, hverdager kl. 09.00 - 15.00.",
                        Nynorsk to "Dersom du ikkje finn svar på nav.no, kan du ringje oss på telefon + 47 55 55 33 34, kvardagar kl. 09.00 til 15.00.",
                        English to "At nav.no/kontakt you can chat or write to us. If you do not find the answer at nav.no, you can call us at +47 55 55 33 34, Weekdays from 09:00 to 15:00.",
                    )
                }
            }
        }
}
