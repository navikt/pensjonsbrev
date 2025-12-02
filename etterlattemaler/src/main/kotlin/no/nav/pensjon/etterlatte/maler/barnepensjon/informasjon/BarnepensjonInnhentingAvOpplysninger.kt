package no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
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
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INNHENTING_AV_OPPLYSNINGER

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Du må sende oss flere opplysninger",
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Du må sende oss flere opplysninger" },
                    nynorsk { +"Du må sende oss fleire opplysningar" },
                    english { +"You must submit additional information" },
                )
            }

            outline {
                paragraph {
                    text(
                        bokmal { +
                            "<Redigerbar tekst for å fylle inn det som er nødvendig å innhente av opplysninger og/eller dokumentasjon.> " },
                        nynorsk { +
                            "<Redigerbar tekst for å fylle inn det som er nødvendig å innhente av opplysninger og/eller dokumentasjon.> " },
                        english { +
                            "<Redigerbar tekst for å fylle inn det som er nødvendig å innhente av opplysninger og/eller dokumentasjon.> " },
                    )
                }

                showIf(erOver18aar.not()) {
                    title2 {
                        text(
                            bokmal { +"Hvordan sende opplysninger til oss?" },
                            nynorsk { +"Korleis melder du frå om endringar?" },
                            english { +"How to submit information to Nav?" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +
                                "Frem til barn fyller 18 år, kan du som forelder eller verge, " +
                                "ikke logge deg inn på våre nettsider på vegne av barnet. " +
                                "Du må sende opplysninger/dokumentasjon per post til denne adressen:" },
                            nynorsk { +
                                "Fram til barnet fyller 18 år, kan du som forelder eller verja, " +
                                "ikkje logge deg inn på nettsidane våre på vegner av barnet. " +
                                "Du må sende oss opplysningar og dokumentasjon til adressa:" },
                            english { +
                                "The interests of children are attended to by a guardian until a child reaches the age of 18. " +
                                "You may not log on to our website on behalf of your child. " +
                                "If you want to send us something, you must use our mailing address:" },
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
