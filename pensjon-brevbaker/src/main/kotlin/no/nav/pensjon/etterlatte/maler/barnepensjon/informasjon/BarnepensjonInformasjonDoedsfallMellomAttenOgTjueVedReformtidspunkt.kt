package no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon

import no.nav.pensjon.brev.template.Expression
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
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTOSelectors.borIutland
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.BARNEPENSJON_URL

data class BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO(
    val avdoedNavn: String,
    val borIutland: Boolean,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunkt : EtterlatteTemplate<BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INFORMASJON_DOEDSFALL_MELLOM_ATTEN_OG_TJUE_VED_REFORMTIDSPUNKT

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = BarnepensjonInformasjonDoedsfallMellomAttenOgTjueVedReformtidspunktDTO::class,
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
                    Bokmal to "Informasjon om barnepensjon fra folketrygden",
                    Nynorsk to "Informasjon om barnepensjon frå folketrygda",
                    English to "Information about children’s pension from National Insurance",
                )
            }

            outline {
                paragraph {
                    text(
                        Bokmal to "Vi skriver til deg fordi vi har registrert at du mistet en eller begge foreldrene dine etter at du ble 18 år og var under 20 år ved starten av 2024.",
                        Nynorsk to "Vi skriv til deg fordi vi har registrert at du mista den eine av eller begge foreldra dine etter at du blei 18 år, og var under 20 år ved starten av 2024.",
                        English to "We write to you, because our records show that you lost one or both of your parents after you turned 18 years old, and you were under the age of 20 at the beginning of 2024.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Barnepensjon er en stønad som gis når du har mistet en eller begge foreldrene dine.",
                        Nynorsk to "Barnepensjon er ein stønad som blir gitt når du har mista den eine av eller begge foreldra dine.",
                        English to "Children’s pension is a benefit granted when you have lost one or both of your parents.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Stortinget har endret reglene for barnepensjon fra 1. januar 2024. Dette innebærer at du kan ha rett til barnepensjon.",
                        Nynorsk to "Stortinget endra reglane for barnepensjon 1. januar 2024. Dette inneber at du kan ha rett på barnepensjon.",
                        English to "The Norwegian Parliament, the Storting, changed the rules for children’s pensions from 1 January 2024. As a result, you may be entitled to children’s pension.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hva betyr de nye reglene?",
                        Nynorsk to "Kva betyr dei nye reglane?",
                        English to "What do these new rules entail?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "• Du kan få barnepensjon til du blir 20 år. Tidligere hovedregel var 18 år.",
                        Nynorsk to "• Du kan få barnepensjon til du fyller 20 år. Tidlegare var grensa normalt 18 år.",
                        English to "• Children’s pension can now be granted up to age 20. Previously, the benefit was granted up to age 18.",
                    )
                    newline()
                    text(
                        Bokmal to "• Du må som hovedregel være medlem i folketrygden.",
                        Nynorsk to "• Du må som hovudregel vere medlem i folketrygda.",
                        English to "• Normally, you must be a member of the Norwegian National Insurance scheme.",
                    )
                }

                title2 {
                    text(
                        Bokmal to "Hvordan søker du?",
                        Nynorsk to "Korleis søkjer du?",
                        English to "How to apply",
                    )
                }
                showIf(borIutland.not()) {
                    paragraph {
                        text(
                            Bokmal to "Du finner informasjon og søknad på $BARNEPENSJON_URL.",
                            Nynorsk to "Du finn informasjon og søknad på $BARNEPENSJON_URL.",
                            English to "You can find more information and the application form at ${Constants.Engelsk.BARNEPENSJON_URL}.",
                        )
                    }
                }


                showIf(borIutland) {
                    paragraph {
                        text(
                            Bokmal to "Vi har informasjon om at du bor i utlandet. Du finner informasjon om hvordan du søker på $BARNEPENSJON_URL.",
                            Nynorsk to "Etter dei opplysningane vi har, bur du i utlandet. Du kan lese meir på $BARNEPENSJON_URL om korleis du søkjer.",
                            English to "Our records show that you live abroad. You can find information about how to apply at ${Constants.Engelsk.BARNEPENSJON_URL}.",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to "Bor du i et land Norge har trygdeavtale med, må du kontakte trygdemyndigheten i bostedslandet ditt før du søker barnepensjon. Du finner informasjon om hvilke land Norge har avtale med på ${Constants.Utland.BP}.",
                            Nynorsk to "Dersom du bur i eit land som Noreg har trygdeavtale med, må du kontakte trygdemaktene i dette landet før du søkjer om barnepensjon. På ${Constants.Utland.BP} finn du meir informasjon om kva land Noreg har avtale med.",
                            English to "If Norway has a social security agreement with the country where you live, you must contact the social security authority in your country of residence before you apply for children’s pension. Go to ${Constants.Utland.BP} to see which countries Norway has a social security agreement with.",
                        )
                    }
                }

                title2 {
                    text(
                        Bokmal to "Rettigheter hvis avdøde har bodd eller arbeidet i utlandet",
                        Nynorsk to "Rettar dersom avdøde har budd eller jobba i utlandet",
                        English to "Rights if the deceased has lived or worked abroad",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis avdøde tidligere har bodd eller arbeidet i utlandet, kan dette få betydning for hvor mye du får utbetalt. Norge har trygdesamarbeid med en rekke land gjennom EØS-avtalen og andre avtaler. Derfor kan du også ha rettigheter fra andre land. Vi kan hjelpe deg med søknad til land Norge har trygdeavtale med.",
                        Nynorsk to "Viss avdøde tidlegare har budd eller jobba i utlandet, kan dette verke inn på kor mykje du får utbetalt. Noreg har trygdesamarbeid med ei rekkje land gjennom EØS-avtalen og andre avtalar. Dette betyr at du kan ha rettar også frå andre land. Vi kan hjelpe deg å sende søknad til land som Noreg har trygdeavtale med.",
                        English to "If the deceased previously lived or worked abroad, this could affect the amount you receive. Norway practices social security coordination with a number of countries through the EEA and other agreements. This means you may also have accumulated rights in other countries. We can help you prepare applications to countries Norway has social security agreements with.",
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
                        Nynorsk to "Dersom du har spørsmål knytt til ei privat eller offentleg pensjonsordning som avdøde hadde, kan du kontakte arbeidsgivaren til avdøde. Alternativt kan du kontakte pensjonsordninga eller forsikringsselskapet. ",
                        English to "If the deceased had a private or public pension scheme, and you have questions about these, you can contact the deceased’s employer. You can also contact the pension scheme or insurance company directly.",
                    )
                }

                    includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(Expression.Literal(false), borIutland))
            }
        }
}
