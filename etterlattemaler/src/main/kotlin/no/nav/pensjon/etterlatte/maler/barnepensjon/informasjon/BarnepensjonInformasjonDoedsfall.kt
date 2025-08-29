package no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallDTOSelectors.avdoedNavn
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallDTOSelectors.borIutland
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallDTOSelectors.erOver18aar
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.BARNEPENSJON_URL

data class BarnepensjonInformasjonDoedsfallDTO(
    val avdoedNavn: String,
    val borIutland: Boolean,
    val erOver18aar: Boolean,
) : RedigerbartUtfallBrevDTO

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
                    bokmal { +"Informasjon om barnepensjon" },
                    nynorsk { +"Informasjon om barnepensjon" },
                    english { +"Information about children’s pension" },
                )
            }

            outline {
                paragraph {
                    text(
                        bokmal { +"Vi skriver til deg fordi vi har fått melding om at " + avdoedNavn + " er død, og du kan ha rett til barnepensjon." },
                        nynorsk { +"Vi skriv til deg fordi vi har fått melding om at " + avdoedNavn + " er død, og du kan ha rett til barnepensjon. " },
                        english { +"We are writing to you because we have received notice that " + avdoedNavn + " has died, and we would like to inform you about children's pension." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Hvem kan ha rett til barnepensjon?" },
                        nynorsk { +"Kven kan ha rett til barnepensjon?" },
                        english { +"Who may be entitled to a children’s pension?" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"For å ha rett til barnepensjon, må du som hovedregel være medlem i folketrygden og under 20 år." },
                        nynorsk { +"For å ha rett til barnepensjon må du som hovudregel vere medlem i folketrygda og under 20 år." },
                        english { +"To be entitled to a children’s pension, you must as a rule have been a member of the National Insurance Scheme and under the age of 20." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Hvordan søker du?" },
                        nynorsk { +"Korleis søkjer du?" },
                        english { +"How do you apply?" },
                    )
                }
                showIf(borIutland.not().and(erOver18aar.not())) {
                    paragraph {
                        text(
                            bokmal { +"Du finner informasjon og søknad på $BARNEPENSJON_URL." },
                            nynorsk { +"Du finn informasjon og søknad på $BARNEPENSJON_URL." },
                            english { +"You will find information and the application form at ${Constants.Engelsk.BARNEPENSJON_URL}." },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Er du under 18 år, må vergen din søke om barnepensjon for deg. Vergen til barnet kan være enten forelderen eller andre personer oppnevnt av Statsforvalteren." },
                            nynorsk { +"Er du under 18 år, må verja di søkje om barnepensjon på dine vegner. Verja til barnet kan vere anten forelderen eller ein person som Statsforvaltaren har utnemnd." },
                            english { +"If you are under the age of 18, your guardian must apply for a children’s pension on your behalf. A child’s guardian can be a parent or another person appointed by the County Governor." },
                        )
                    }
                }

                showIf(borIutland.and(erOver18aar.not())) {
                    paragraph {
                        text(
                            bokmal { +"Vi har informasjon om at du bor i utlandet. Du finner informasjon om hvordan du søker på $BARNEPENSJON_URL." },
                            nynorsk { +"Etter dei opplysningane vi har, bur du i utlandet. Du kan lese meir på $BARNEPENSJON_URL om korleis du søkjer." },
                            english { +"According to our records, you live abroad. You will find information and the application form at ${Constants.Engelsk.BARNEPENSJON_URL}." },
                        )
                    }

                    paragraph {
                        text(
                            bokmal { +"Bor du i et land Norge har trygdeavtale med, må du kontakte trygdemyndigheten i bostedslandet ditt før du søker barnepensjon. Du finner informasjon om hvilke land Norge har avtale med på ${Constants.Utland.BP}." },
                            nynorsk { +"Dersom du bur i eit land som Noreg har trygdeavtale med, må du kontakte trygdemaktene i dette landet før du søkjer om barnepensjon. På ${Constants.Utland.BP} finn du meir informasjon om kva land Noreg har avtale med." },
                            english { +"If you live in a country Norway has a social security agreement with, you must contact the social security authority in your country of residence before you apply for children’s pension. See which countries Norway has a social security agreement with here: ${Constants.Utland.BP}." },
                        )
                    }

                    paragraph {
                        text(
                            bokmal { +"Er du under 18 år, må vergen din søke om barnepensjon på vegne av deg. Vergen til barnet kan være enten forelderen eller andre personer oppnevnt av Statsforvalteren." },
                            nynorsk { +"Viss du er under 18 år, må verja di søkje om barnepensjon på dine vegner. Verja til barnet kan vere anten forelderen eller ein person som Statsforvaltaren har utnemnd." },
                            english { +"If you are under the age of 18, your guardian must apply for children’s pension on your behalf." },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"" },
                            nynorsk { +"" },
                            english { +"A child’s guardian can be a parent or another person appointed by the County Governor." },
                        )
                    }
                }

                showIf(borIutland.not().and(erOver18aar)) {
                    paragraph {
                        text(
                            bokmal { +"Du finner informasjon og søknad på $BARNEPENSJON_URL." },
                            nynorsk { +"Du finn informasjon og søknad på $BARNEPENSJON_URL." },
                            english { +"You will find information and the application form at ${Constants.Engelsk.BARNEPENSJON_URL}." },
                        )
                    }
                }

                showIf(borIutland.and(erOver18aar)) {
                    paragraph {
                        text(
                            bokmal { +"Vi har informasjon om at du bor i utlandet. Du finner informasjon om hvordan du søker på $BARNEPENSJON_URL." },
                            nynorsk { +"Etter dei opplysningane vi har, bur du i utlandet. Du kan lese meir på $BARNEPENSJON_URL om korleis du søkjer." },
                            english { +"According to our records, you live abroad. Go to ${Constants.Engelsk.BARNEPENSJON_URL} for more information on how to apply." },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +"Bor du i et land Norge har trygdeavtale med, må du kontakte trygdemyndigheten i bostedslandet ditt før du søker barnepensjon. Du finner informasjon om hvilke land Norge har avtale med på ${Constants.Utland.BP}." },
                            nynorsk { +"Dersom du bur i eit land som Noreg har trygdeavtale med, må du kontakte trygdemaktene i dette landet før du søkjer om barnepensjon. På ${Constants.Utland.BP} finn du meir informasjon om kva land Noreg har avtale med." },
                            english { +"If you live in a country Norway has a social security agreement with, you must contact the social security authority in your country of residence before you apply for children’s pension. See which countries Norway has a social security agreement with here: ${Constants.Utland.BP}." },
                        )
                    }
                }

                title2 {
                    text(
                        bokmal { +"Rettigheter hvis avdøde har bodd eller arbeidet i utlandet" },
                        nynorsk { +"Rettar når avdøde har budd eller arbeidd i utlandet" },
                        english { +"Rights if the deceased has lived or worked abroad" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis avdøde tidligere har bodd eller arbeidet i utlandet, kan dette få betydning for hvor mye du får utbetalt. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rettigheter fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med." },
                        nynorsk { +"Dersom avdøde tidlegare har budd eller arbeidd i utlandet, kan det påverke kor mykje du får utbetalt. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Derfor kan du også ha rettar frå andre land. Vi kan hjelpe deg med søknad til land Noreg har trygdeavtale med." },
                        english { +"If the deceased has lived or worked abroad, this may affect the amount of your pension. Norway cooperates with a number of countries through the EEA Agreement and other social security agreements. Therefore, you may also be entitled to a pension from other countries. We can assist you with your application to countries with which Norway has a social security agreement." },
                    )
                }

                title2 {
                    text(
                        bokmal { +"Andre pensjonsordninger" },
                        nynorsk { +"Andre pensjonsordningar" },
                        english { +"Other pension schemes" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Dersom avdøde hadde en privat eller offentlig pensjonsordning og du har spørsmål om dette, kan du kontakte avdødes arbeidsgiver. Du kan også ta kontakt med pensjonsordningen eller forsikringsselskapet." },
                        nynorsk { +"Dersom avdøde hadde ei privat eller offentleg pensjonsordning og du har spørsmål om dette, kan du kontakte arbeidsgivaren til den avdøde. Du kan også ta kontakt med pensjonsordninga eller forsikringsselskapet." },
                        english { +"If the deceased was a member of a private or public pension scheme and you have questions about this, you can contact the deceased's employer. You can also contact the pension scheme or insurance company." },
                    )
                }

                includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(erOver18aar.not(), borIutland))
            }
        }
}
