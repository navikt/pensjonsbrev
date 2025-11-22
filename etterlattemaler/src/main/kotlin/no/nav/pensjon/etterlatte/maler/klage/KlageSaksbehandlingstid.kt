package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
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
                text(
                    bokmal { +"Klage -" + sakType.format() + " - orientering om saksbehandlingstid " },
                    nynorsk { +"Klage -" + sakType.format() + " - orientering om saksbehandlingstid " },
                    english { +"Appeals –" + sakType.format() + " - Information about processing time  " },
                )
            }

            outline {
                title2 {
                    text(
                        bokmal { +"Klageren:" },
                        nynorsk { +"Klagaren:" },
                        english { +"Appellant:" }
                    )
                }

                title2 {
                    text(
                        bokmal { +"Klagemotpart: Nav familie- og pensjonsytelser" },
                        nynorsk { +"Klagemotpart: Nav familie- og pensjonsytelser" },
                        english { +"Respondent: Nav familie- og pensjonsytelser" }
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Vi har " + datoMottatKlage.format() + " fått klagen over vårt vedtak av " + datoForVedtak.format() + "." },
                        nynorsk { +"Klaga på vedtaket vårt av " + datoMottatKlage.format() + " blei motteken " + datoForVedtak.format() + "." },
                        english { +"We received a complaint about " + datoMottatKlage.format() + " our decision of " + datoForVedtak.format() + "." }
                    )
                }

                title2 {
                    text(
                        bokmal { +"Behandlingstid" },
                        nynorsk { +"Behandlingstid" },
                        english { +"Processing time" }
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Du kan finne vår saksbehandlingstid på " + ifElse(
                            sakType.equalTo(SakType.BARNEPENSJON), SAKSBEHANDLINGSTIDER_BP.expr(), SAKSBEHANDLINGSTIDER_OMS.expr()
                        ) + "." },
                        nynorsk { +"Du finn saksbehandlingstida vår på " + ifElse(
                            sakType.equalTo(SakType.BARNEPENSJON), SAKSBEHANDLINGSTIDER_BP.expr(), SAKSBEHANDLINGSTIDER_OMS.expr()
                        ) + "." },
                        english { +"You can find more information about case processing time here: " + ifElse(
                            sakType.equalTo(SakType.BARNEPENSJON), SAKSBEHANDLINGSTIDER_BP.expr(), SAKSBEHANDLINGSTIDER_OMS.expr()
                        ) + "." },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Hvis saken din ikke er ferdigbehandlet av oss i løpet av denne tiden, vil du få nærmere beskjed." },
                        nynorsk { +"Dersom saka di ikkje har blitt ferdigbehandla innan denne tida, vil du få nærmare beskjed." },
                        english { +"We will contact you with more information if you have not received our decision during that time." }
                    )
                }

                title2 {
                    text(
                        bokmal { +"Meld fra om endringer" },
                        nynorsk { +"Meld frå om endringar" },
                        english { +"Duty to report changes" },
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av klagen din, som" },
                        nynorsk { +"Vi ber om at du held oss orientert om forhold som kan få betydning for avgjerda vi tek i klaga di. Døme på slike forhold er" },
                        english { +"Please keep us informed about any changes that can have significance for our decision concerning your appeal, such as" },
                    )

                    list {
                        item {
                            text(
                                bokmal { +"endringer av nåværende familie- eller omsorgsforhold" },
                                nynorsk { +"endringar i noverande familie- eller omsorgsforhold" },
                                english { +"changes in family or care relationships" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"flytting eller opphold i et annet land over tid" },
                                nynorsk { +"flytting til eller langvarig opphald i eit anna land" },
                                english { +"relocation or residence in another country over time" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"varig opphold i institusjon" },
                                nynorsk { +"varig opphald på ein institusjon" },
                                english { +"permanent residence in an institution" },
                            )
                        }
                    }
                }
                title2 {
                    text(
                        bokmal { +"Har du spørsmål?" },
                        nynorsk { +"Har du spørsmål?" },
                        english { +"Do you have questions?" }
                    )
                }

                paragraph {
                    text(
                        bokmal { +"Du kan finne svar på " + sakUrl(sakType) + "." +
                        " Hvis du ikke finner svar på spørsmålet ditt, kan du ringe oss på telefon " + kontakttelefonPensjonExpr(borIUtlandet) + " hverdager mellom klokken 09.00-15.00." +
                                " Om du oppgir <fødselsnummer ditt/fødselsnummer til barnet/ fødselsnummer til den du er verge for>, kan vi lettere gi deg rask og god hjelp." },
                        nynorsk { +"Du finn meir informasjon på " + sakUrl(sakType) + "." +
                                " Dersom du ikkje finn svar på spørsmålet ditt der, kan du ringje oss på telefon " + kontakttelefonPensjonExpr(borIUtlandet) + " kvardagar mellom klokka 09.00–15.00."  +
                                " Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir  <fødselsnummeret ditt / fødselsnummeret til barnet / fødselsnummeret til den du er verje for>." },
                        english { +"For more information, visit us online: " + sakUrl(sakType) + "." +
                                " If you cannot find the answer to your question, you can call us by phone at " + kontakttelefonPensjonExpr(borIUtlandet) + " Monday to Friday between 9:00 AM and 3:00 PM." + "" +
                                " If you state <your national identity number/child's national identity number/ for guardians, the national identity number of your ward >, we will be able to provide you with fast and adequate help." },
                    )
                }

            }
        }

}



























