package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
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
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTOSelectors.borINorgeEllerIkkeAvtaleland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTOSelectors.mottattDato
import java.time.LocalDate

data class OmstillingsstoenadMottattSoeknadDTO(
    override val innhold: List<Element>,
    val mottattDato: LocalDate,
    val borINorgeEllerIkkeAvtaleland: Boolean,
) : BrevDTO

@TemplateModelHelpers
object OmstillingsstoenadMottattSoeknad : EtterlatteTemplate<OmstillingsstoenadMottattSoeknadDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INFORMASJON_MOTTATT_SOEKNAD

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadMottattSoeknadDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vi har mottatt søknaden din om omstillingsstønad",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                textExpr(
                    Bokmal to "Vi har mottatt søknaden din om omstillingsstønad ".expr() + mottattDato.format(),
                    Nynorsk to "Vi har fått søknaden din om omstillingsstønad ".expr() + mottattDato.format(),
                    English to "We received your application for adjustment allowance on ".expr() + mottattDato.format(),
                )
            }

            outline {
                konverterElementerTilBrevbakerformat(innhold)

                showIf(borINorgeEllerIkkeAvtaleland) {
                    title2 {
                        text(
                            Bokmal to "Du må melde fra om endringer",
                            Nynorsk to "Meld frå om endringar",
                            English to "Report changes",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to
                                "Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av søknaden din, som",
                            Nynorsk to
                                "Vi ber om at du held oss orientert om forhold som kan få betydning for avgjerda vi tek i klaga di. Døme på slike forhold er",
                            English to
                                "Please keep us informed about any changes that can have significance for our decision concerning your appeal, such as",
                        )
                        list {
                            item {
                                text(
                                    Bokmal to "changes in family or care relationships",
                                    Nynorsk to "endringar i noverande familie- eller omsorgsforhold",
                                    English to "varig opphald på ein institusjon",
                                )
                            }
                            item {
                                text(
                                    Bokmal to "flytting eller opphold i et annet land over tid",
                                    Nynorsk to "flytting til eller langvarig opphald i eit anna land",
                                    English to "relocation or residence in another country over time",
                                )
                            }
                            item {
                                text(
                                    Bokmal to "varig opphold i institusjon",
                                    Nynorsk to "varig opphald på ein institusjon",
                                    English to "permanent residence in an institution",
                                )
                            }
                        }
                    }
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
