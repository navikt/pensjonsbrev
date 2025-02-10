package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.SAKSBEHANDLINGSTIDER_BP
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants.SAKSBEHANDLINGSTIDER_OMS
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjonExpr
import no.nav.pensjon.etterlatte.maler.klage.KlageSaksbehandlingstidDTOSelectors.borIUtlandet
import no.nav.pensjon.etterlatte.maler.klage.KlageSaksbehandlingstidDTOSelectors.datoForVedtak
import no.nav.pensjon.etterlatte.maler.klage.KlageSaksbehandlingstidDTOSelectors.datoMottatKlage
import no.nav.pensjon.etterlatte.maler.klage.KlageSaksbehandlingstidDTOSelectors.sakType
import java.time.LocalDate

data class KlageSaksbehandlingstidDTO(
    val sakType: SakType,
    val borIUtlandet: Boolean,
    val datoMottatKlage: LocalDate,
    val datoForVedtak: LocalDate,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object KlageSaksbehandlingstid : EtterlatteTemplate<KlageSaksbehandlingstidDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.KLAGE_SAKSBEHANDLINGS_INFO

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = KlageSaksbehandlingstidDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Klagebehandlingstid",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
            ),
        ) {
            title {
                textExpr(
                    Bokmal to "Klage -".expr() + sakType.format() + " - orientering om saksbehandlingstid ",
                    Nynorsk to "Klage -".expr() + sakType.format() + " - orientering om saksbehandlingstid ",
                    English to "Appeals –".expr() + sakType.format() + " - Information about processing time  ",
                )
            }

            outline {
                title2 {
                    text(
                        Bokmal to "Klageren:",
                        Nynorsk to "Klagaren:",
                        English to "Appellant:"
                    )
                }

                title2 {
                    text(
                        Bokmal to "Klagemotpart: Nav familie- og pensjonsytelser",
                        Nynorsk to "Klagemotpart: Nav familie- og pensjonsytelser",
                        English to "Respondent: Nav familie- og pensjonsytelser"
                    )
                }

                paragraph {
                    textExpr(
                        Bokmal to "Vi har ".expr() + datoMottatKlage.format() + " fått klagen over vårt vedtak av " + datoForVedtak.format() + ".",
                        Nynorsk to "Klaga på vedtaket vårt av ".expr() + datoMottatKlage.format() + " blei motteken " + datoForVedtak.format() + ".",
                        English to "We received a complaint about ".expr() + datoMottatKlage.format() + " our decision of " + datoForVedtak.format() + "."
                    )
                }

                title2 {
                    text(
                        Bokmal to "Behandlingstid",
                        Nynorsk to "Behandlingstid",
                        English to "Processing time"
                    )
                }

                paragraph {
                    textExpr(
                        Bokmal to "Du kan finne vår saksbehandlingstid på ".expr() + ifElse(
                            sakType.equalTo(SakType.BARNEPENSJON), SAKSBEHANDLINGSTIDER_BP.expr(), SAKSBEHANDLINGSTIDER_OMS.expr()
                        ) + ".",
                        Nynorsk to "Du finn saksbehandlingstida vår på ".expr() + ifElse(
                            sakType.equalTo(SakType.BARNEPENSJON), SAKSBEHANDLINGSTIDER_BP.expr(), SAKSBEHANDLINGSTIDER_OMS.expr()
                        ) + ".",
                        English to "You can find more information about case processing time here: ".expr() + ifElse(
                            sakType.equalTo(SakType.BARNEPENSJON), SAKSBEHANDLINGSTIDER_BP.expr(), SAKSBEHANDLINGSTIDER_OMS.expr()
                        ) + ".",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Hvis saken din ikke er ferdigbehandlet av oss i løpet av denne tiden, vil du få nærmere beskjed.",
                        Nynorsk to "Dersom saka di ikkje har blitt ferdigbehandla innan denne tida, vil du få nærmare beskjed.",
                        English to "We will contact you with more information if you have not received our decision during that time."
                    )
                }

                title2 {
                    text(
                        Bokmal to "Meld fra om endringer",
                        Nynorsk to "Meld frå om endringar",
                        English to "Duty to report changes",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av klagen din, som",
                        Nynorsk to "Vi ber om at du held oss orientert om forhold som kan få betydning for avgjerda vi tek i klaga di. Døme på slike forhold er",
                        English to "Please keep us informed about any changes that can have significance for our decision concerning your appeal, such as",
                    )

                    list {
                        item {
                            text(
                                Bokmal to "endringer av nåværende familie- eller omsorgsforhold",
                                Nynorsk to "endringar i noverande familie- eller omsorgsforhold",
                                English to "changes in family or care relationships",
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
                title2 {
                    text(
                        Bokmal to "Har du spørsmål?",
                        Nynorsk to "Har du spørsmål?",
                        English to "Do you have questions?"
                    )
                }

                paragraph {
                    textExpr(
                        Bokmal to "Du kan finne svar på ".expr() + sakUrl(sakType) + "." +
                                " Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon " + kontakttelefonPensjonExpr(borIUtlandet) + " hverdager mellom klokken 09.00-15.00." +
                                " Om du oppgir <fødselsnummer ditt/fødselsnummer til barnet/ fødselsnummer til den du er verge for>, kan vi lettere gi deg rask og god hjelp.",
                        Nynorsk to "Du finn meir informasjon på ".expr() + sakUrl(sakType) + "." +
                                " Dersom du ikkje finn svar på spørsmålet ditt der, kan du ringje oss på telefon " + kontakttelefonPensjonExpr(borIUtlandet) + " kvardagar mellom klokka 09.00–15.00."  +
                                " Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir  <fødselsnummeret ditt / fødselsnummeret til barnet / fødselsnummeret til den du er verje for>.",
                        English to "For more information, visit us online: ".expr() + sakUrl(sakType) + "." +
                                " If you cannot find the answer to your question, you can call us by phone at " + kontakttelefonPensjonExpr(borIUtlandet) + " Monday to Friday between 9:00 AM and 3:00 PM." + "" +
                                " If you state <your national identity number/child's national identity number/ for guardians, the national identity number of your ward >, we will be able to provide you with fast and adequate help.",
                    )
                }

            }
        }

}



























