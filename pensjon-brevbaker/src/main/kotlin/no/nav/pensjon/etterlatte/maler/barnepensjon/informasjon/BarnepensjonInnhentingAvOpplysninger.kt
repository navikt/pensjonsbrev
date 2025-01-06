package no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInnhentingAvOpplysningerDTOSelectors.borIUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonInnhentingAvOpplysningerDTOSelectors.erOver18aar
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles

data class BarnepensjonInnhentingAvOpplysningerDTO(
    val erOver18aar: Boolean,
    val borIUtlandet: Boolean,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object BarnepensjonInnhentingAvOpplysninger : EtterlatteTemplate<BarnepensjonInnhentingAvOpplysningerDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BP_INFORMASJON_INNHENTING_AV_OPPLYSNINGER

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = BarnepensjonInnhentingAvOpplysningerDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Du må sende oss flere opplysninger",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Du må sende oss flere opplysninger",
                    Nynorsk to "Du må sende oss fleire opplysningar",
                    English to "You must submit additional information",
                )
            }

            outline {
                paragraph {
                    text(
                        Bokmal to
                            "<Redigerbar tekst for å fylle inn det som er nødvendig å innhente av opplysninger og/eller dokumentasjon.> ",
                        Nynorsk to
                            "<Redigerbar tekst for å fylle inn det som er nødvendig å innhente av opplysninger og/eller dokumentasjon.> ",
                        English to
                            "<Redigerbar tekst for å fylle inn det som er nødvendig å innhente av opplysninger og/eller dokumentasjon.> ",
                    )
                }

                showIf(erOver18aar.not()) {
                    title2 {
                        text(
                            Bokmal to "Hvordan sende opplysninger til oss?",
                            Nynorsk to "Korleis melder du frå om endringar?",
                            English to "How to submit information to Nav?",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to
                                "Frem til barn fyller 18 år, kan du som forelder eller verge, " +
                                "ikke logge deg inn på våre nettsider på vegne av barnet. " +
                                "Du må sende opplysninger/dokumentasjon per post til denne adressen:",
                            Nynorsk to
                                "Fram til barnet fyller 18 år, kan du som forelder eller verja, " +
                                "ikkje logge deg inn på nettsidane våre på vegner av barnet. " +
                                "Du må sende oss opplysningar og dokumentasjon til adressa:",
                            English to
                                "The interests of children are attended to by a guardian until a child reaches the age of 18. " +
                                "You may not log on to our website on behalf of your child. " +
                                "If you want to send us something, you must use our mailing address:",
                        )
                    }
                    includePhrase(Felles.AdresseMedMellomrom(borIUtlandet))
                }.orShow {
                    includePhrase(Felles.HvordanSendeOpplysninger(borIUtlandet))
                }
                includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(erOver18aar.not(), borIUtlandet))
            }
        }
}
