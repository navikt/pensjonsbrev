package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
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
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTOSelectors.bosattIUtlandet
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTOSelectors.harVerge
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTOSelectors.innstillingTekstLinjer
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTOSelectors.klageDato
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTOSelectors.sakType
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTOSelectors.under18Aar
import no.nav.pensjon.etterlatte.maler.klage.KlageOversendelseBrukerDTOSelectors.vedtakDato
import java.time.LocalDate

data class KlageOversendelseBrukerDTO(
    val sakType: SakType,
    val klageDato: LocalDate,
    val vedtakDato: LocalDate,
    val innstillingTekst: String,
    val under18Aar: Boolean,
    val harVerge: Boolean,
    val bosattIUtlandet: Boolean
): FerdigstillingBrevDTO {
    override val innhold: List<Element> = emptyList()
    val innstillingTekstLinjer = innstillingTekst.lines()
}

@TemplateModelHelpers
object KlageOversendelsesbrevBruker : EtterlatteTemplate<KlageOversendelseBrukerDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.KLAGE_OVERSENDELSE_BRUKER

    override val template = createTemplate(
        name = kode.name,
        letterDataType = KlageOversendelseBrukerDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vi har sendt klagen din tiil Nav klageinstans vest",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    ) {
        title {
            text(
                Language.Bokmal to "Vi har sendt klagen din til Nav klageinstans vest",
                Language.Nynorsk to "",
                Language.English to "",
            )
        }

        outline {
            paragraph {
                textExpr(
                    Language.Bokmal to "Vi har ".expr() + klageDato.format() + " fått klagen din på vedtaket om " + sakType.format() + " som ble gjort " + vedtakDato.format() + ", og har kommet frem til at vi ikke endrer vedtaket. Nav klageinstans skal vurdere saken din på nytt.",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Klageinstansen vurderer alle sider av saken på selvstendig grunnlag. Resultatet av klagebehandlingen kan bli at vårt vedtak ikke blir endret, eller at det blir endret helt eller delvis. Klageinstansen kan også oppheve vedtaket vårt, og sende saken tilbake til oss for helt eller delvis ny behandling.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            paragraph {
                textExpr(
                    Language.Bokmal to "Saksbehandlingstiden til Nav klageinstans finner du på ".expr() + ifElse(sakType.equalTo(
                        SakType.BARNEPENSJON), Constants.SAKSBEHANDLINGSTIDER_BP.expr(), Constants.SAKSBEHANDLINGSTIDER_OMS.expr()) + ".",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }

            title1 {
                text(
                    Language.Bokmal to "Dette er vurderingen vi har sendt til Nav klageinstans",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            formaterTekstlinjer(innstillingTekstLinjer)

            title1 {
                text(
                    Language.Bokmal to "Har du nye opplysninger?",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Har du nye opplysninger eller ønsker å uttale deg, kan du sende oss dette via ".expr() + sakUrl(
                        sakType
                    ) + "#klage.",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }

            title1 {
                text(
                    Language.Bokmal to "Har du spørsmål?",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Du kan finne svar på ".expr() + sakUrl(sakType) + ". På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. ",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Du kan også kontakte Nav klageinstans på ".expr() + sakUrl(sakType) + " eller telefon " + ifElse(
                        bosattIUtlandet,
                        Constants.KONTAKTTELEFON_PENSJON_MED_LANDKODE,
                        Constants.KONTAKTTELEFON_PENSJON
                    ) + ", hverdager 09.00-15.00. Hvis du oppgir fødselsnummer" + ifElse(
                        harVerge,
                        " til den du er verge for".expr(),
                        ifElse(under18Aar, " til barnet", "")
                    ) + ", kan vi lettere gi deg rask hjelp.",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }
        }
    }
}

fun sakUrl(sakType: Expression<SakType>): Expression<String> {
    return ifElse(sakType.equalTo(SakType.BARNEPENSJON), Constants.BARNEPENSJON_URL.expr(), Constants.OMS_URL.expr())
}

fun <T : Any> OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, T>.formaterTekstlinjer(
    linjer: Expression<List<String>>
) {
    paragraph {
        forEach(linjer) { linje ->
            textExpr(Language.Bokmal to linje, Language.Nynorsk to linje, Language.English to linje)
            newline()
        }
    }
}

