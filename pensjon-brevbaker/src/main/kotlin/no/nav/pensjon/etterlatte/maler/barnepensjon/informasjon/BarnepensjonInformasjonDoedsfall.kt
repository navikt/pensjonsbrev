package no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallDTOSelectors.avdoedNavn
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallDTOSelectors.borIutland
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

data class BarnepensjonInformasjonDoedsfallDTO(
    override val innhold: List<Element>,
    val avdoedNavn: String,
    val borIutland: Boolean,
) : BrevDTO


@TemplateModelHelpers
object BarnepensjonInformasjonDoedsfall : EtterlatteTemplate<BarnepensjonInformasjonDoedsfallDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INFORMASJON_DOEDSFALL

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = BarnepensjonInformasjonDoedsfallDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Informasjon om barnepensjon",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Informasjon om barnepensjon",
                    Nynorsk to "Informasjon om barnepensjon",
                    English to "Information about children’s pension",
                )
            }

            outline {
                paragraph {
                    textExpr(
                        Bokmal to "Vi skriver til deg fordi vi har fått melding om at ".expr() + avdoedNavn + " er død, og du kan ha rett til barnepensjon.".expr(),
                        Nynorsk to "Vi skriv til deg fordi vi har fått melding om at ".expr() + avdoedNavn + " er død, og du kan ha rett til barnepensjon. ".expr(),
                        English to "We are writing to you because we have received notice that ".expr() + avdoedNavn + " has died, and we would like to inform you about children's pension.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvem kan ha rett til barnepensjon?",
                        Nynorsk to "Kven kan ha rett til barnepensjon?",
                        English to "Who may be entitled to a children’s pension?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "For å ha rett til barnepensjon, må du som hovedregel være medlem i folketrygden og under 20 år.",
                        Nynorsk to "For å ha rett til barnepensjon må du som hovudregel vere medlem i folketrygda og under 20 år.",
                        English to "To be entitled to a children’s pension, you must as a rule have been a member of the National Insurance Scheme and under the age of 20.",
                    )
                }

                showIf(borIutland) {
                    title2 {
                        text(
                            Bokmal to "Hvordan søker du?",
                            Nynorsk to "Korleis søkjer du?",
                            English to "How do you apply?",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Vi har informasjon om at du bor i utlandet. Du finner informasjon om hvordan du søker på nav.no/barnepensjon."+
                                    "Bor du i et land Norge har trygdeavtale med, må du kontakte trygdemyndigheten i bostedslandet ditt før du søker barnepensjon. Du finner informasjon om hvilke land Norge har avtale med på ${Constants.Utland.BP}",
                            Nynorsk to "Du finn informasjon og søknad på nav.no/barnepensjon. Er du under 18 år, må verja di søkje om barnepensjon på dine vegner.",
                            English to "According to our records, you live abroad. You will find information and the application form at nav.no/barnepensjon."+
                                    "If you live in a country Norway has a social security agreement with, you must contact the social security authority in your country of residence before you apply for children’s pension. See which countries Norway has a social security agreement with here: ${Constants.Utland.BP}",
                        )
                    }
                }
                showIf(borIutland.not()) {
                    title2 {
                        text(
                            Bokmal to "Hvordan søker du?",
                            Nynorsk to "Korleis søkjer du?",
                            English to "How do you apply?",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Du finner informasjon og søknad på nav.no/barnepensjon. Er du under 18 år, må vergen din søke om barnepensjon på vegne av deg.",
                            Nynorsk to "Du finn informasjon og søknad på nav.no/barnepensjon. Er du under 18 år, må verja di søkje om barnepensjon på dine vegner.",
                            English to "You will find information and the application form at nav.no/barnepensjon. If you are under the age of 18, your guardian must apply for a children’s pension on your behalf.",
                        )
                    }
                }

                paragraph {
                    text(
                        Bokmal to "Dersom du bor i utlandet, må du kontakte trygdemyndigheten i bostedslandet ditt.",
                        Nynorsk to "Dersom du bur i utlandet, må du kontakte trygdemyndigheitene i bustadlandet ditt. ",
                        English to "If you live outside Norway, you must contact the National Insurance authoriti in your country of residence.",
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
                        Bokmal to "Andre pensjonsordninger",
                        Nynorsk to "Andre pensjonsordningar",
                        English to "Other pension schemes",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet.",
                        Nynorsk to "Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet.",
                        English to "If the deceased was a member of a private or public pension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company.",
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
                        Bokmal to "Du finner mer informasjon på ${Constants.BARNEPENSJON_URL}. På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss.",
                        Nynorsk to "Du finn meir informasjon på ${Constants.BARNEPENSJON_URL}. Du kan chatte med oss eller skrive til oss på ${Constants.KONTAKT_URL}.",
                        English to "You can find more information at ${Constants.Engelsk.BARNEPENSJON_URL}. At ${Constants.Engelsk.KONTAKT_URL} you can chat or write to us.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon ${Constants.KONTATTELEFON_PENSJON_MED_LANDKODE}, hverdager kl. 09.00 - 15.00.",
                        Nynorsk to "Dersom du ikkje finn svar på nav.no, kan du ringje oss på telefon ${Constants.KONTATTELEFON_PENSJON_MED_LANDKODE}, kvardagar kl. 09.00 til 15.00.",
                        English to "If you do not find the answer at nav.no, you can call us at ${Constants.KONTATTELEFON_PENSJON_MED_LANDKODE}, weekdays from 09:00 to 15:00.",
                    )
                }
            }
        }
}
