package no.nav.pensjon.etterlatte.maler.klage

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
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
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.fraser.common.kontakttelefonPensjonExpr
import no.nav.pensjon.etterlatte.maler.fraser.common.mottakersFoedselsnummer
import no.nav.pensjon.etterlatte.maler.fraser.common.saksbehandlingstiderUrl
import no.nav.pensjon.etterlatte.maler.klage.selectors.klageOversendelseBrukerDTO.*
import no.nav.pensjon.etterlatte.maler.klage.selectors.klageOversendelseBrukerData.*
import java.time.LocalDate

data class KlageOversendelseBrukerData(
    val sakType: SakType,
    val klageDato: LocalDate,
    val vedtakDato: LocalDate,
    val innstillingTekst: String,
    val under18Aar: Boolean,
    val harVerge: Boolean,
    val bosattIUtlandet: Boolean,
) {
    val innstillingTekstLinjer = innstillingTekst.lines()
}

data class KlageOversendelseBrukerDTO(
    override val innhold: List<Element> = emptyList(),
    override val data: KlageOversendelseBrukerData,
): FerdigstillingBrevDTO

@TemplateModelHelpers
object KlageOversendelsesbrevBruker : EtterlatteTemplate<KlageOversendelseBrukerDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.KLAGE_OVERSENDELSE_BRUKER

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vi har sendt klagen din tiil Nav klageinstans Vest",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    ) {
        title {
            text(
                bokmal { +"Vi har sendt klagen din til Nav klageinstans Vest" },
                nynorsk { +"Vi har sendt klaga di til Nav klageinstans Vest" },
                english { +"We have sent your appeal to Nav Appeals for Western Norway" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Vi har " + data.klageDato.format() + " fått klagen din på vedtaket om " + data.sakType.format() + " som ble gjort " + data.vedtakDato.format() + ", og har kommet frem til at vi ikke endrer vedtaket. Nav klageinstans skal vurdere saken din på nytt." },
                    nynorsk { +data.klageDato.format() + " fekk vi ei klage frå deg på vedtaket om " + data.sakType.format() + " som blei fatta " + data.vedtakDato.format() + ". Vi har kome fram til at vi ikkje endrar vedtaket. Nav klageinstans skal vurdere saka di på nytt." },
                    english { +"We received your appeal on " + data.klageDato.format() + " concerning our decision about " + data.sakType.format() + " that was made on " + data.vedtakDato.format() + ", and we have decided not to change our decision. Nav appeals will reconsider your case." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Klageinstansen vurderer alle sider av saken på selvstendig grunnlag. Resultatet av klagebehandlingen kan bli at vårt vedtak ikke blir endret, eller at det blir endret helt eller delvis. Klageinstansen kan også oppheve vedtaket vårt, og sende saken tilbake til oss for helt eller delvis ny behandling." },
                    nynorsk { +"Klageinstansen vurderer alle sider av saka på sjølvstendig grunnlag. Resultatet av klagebehandlinga kan bli at vedtaket vårt blir ståande, eller at det blir endra heilt eller delvis. Klageinstansen kan også oppheve vedtaket vårt og sende saka tilbake til oss for heilt eller delvis ny behandling." },
                    english { +"Nav appeals will consider all aspects of the case, on an independent basis. One outcome of the appeal process could be that we decide not to change our decision, or that we change our decision in whole or in part. Nav appeals can also repeal our decision, and send the case back to us for new processing, in whole or in part." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Saksbehandlingstiden til Nav klageinstans finner du på " + saksbehandlingstiderUrl(data.sakType) + "." },
                    nynorsk { +"Du finn saksbehandlingstida til Nav klageinstans på " + saksbehandlingstiderUrl(data.sakType) + "." },
                    english { +"The processing times at Nav appeals can be found online: " + saksbehandlingstiderUrl(data.sakType) + "." },
                )
            }

            title1 {
                text(
                    bokmal { +"Dette er vurderingen vi har sendt til Nav klageinstans" },
                    nynorsk { +"Dette er vurderinga vi har sendt til Nav klageinstans" },
                    english { +"This is the evaluation we sent to Nav appeals" },
                )
            }
            formaterTekstlinjer(data.innstillingTekstLinjer)

            title1 {
                text(
                    bokmal { +"Har du nye opplysninger?" },
                    nynorsk { +"Har du nye opplysningar?" },
                    english { +"Do you have any new information to give us?" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Har du nye opplysninger eller ønsker å uttale deg, kan du sende oss dette via " +
                            sakUrl(data.sakType) + "#klage." },
                    nynorsk { +"Dersom du har nye opplysningar eller ønskjer å uttale deg, kan du kontakte oss via " +
                            sakUrl(data.sakType) + "#klage." },
                    english { +"If you have any new information or have any comments you want to send us, you can send us this via " +
                            sakUrl(data.sakType) + "#klage." },
                )
            }

            title1 {
                text(
                    bokmal { +"Har du spørsmål?" },
                    nynorsk { +"Har du spørsmål?" },
                    english { +"Do you have any questions?" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan finne svar på " + sakUrl(data.sakType) + ". På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. " },
                    nynorsk { +"Du kan finne svar på " + sakUrl(data.sakType) + ". På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss. " },
                    english { +"You can find answers online: " + sakUrl(data.sakType) + ". Feel free to chat with us or write to us here: " + "${Constants.Engelsk.KONTAKT_URL}." },
                )
            }
            paragraph {
                text(
                    bokmal { +
                    "Du kan også kontakte Nav klageinstans på " + sakUrl(data.sakType) + " eller telefon " + kontakttelefonPensjonExpr(data.bosattIUtlandet) +
                            ", hverdager mellom klokken 09.00-15.00. Hvis du oppgir " +
                            mottakersFoedselsnummer(data.harVerge, data.under18Aar, Language.Bokmal) + ", kan vi lettere gi deg rask hjelp." },
                    nynorsk { +"Alternativt kan du kontakte Nav klageinstans på " + sakUrl(data.sakType) + " eller telefon " + kontakttelefonPensjonExpr(data.bosattIUtlandet) +
                            ", kvardagar mellom klokka 09:00–15:00. Det vil gjere det enklare for oss å gi deg rask og god hjelp om du oppgir " +
                            mottakersFoedselsnummer(data.harVerge, data.under18Aar, Language.Nynorsk) + "." },
                    english { +"You can also contact Nav Appeals here: " + sakUrl(data.sakType) + " or by phone " + kontakttelefonPensjonExpr(data.bosattIUtlandet) +
                            ", weekdays from 09:00-15:00. If you state " +
                            mottakersFoedselsnummer(data.harVerge, data.under18Aar, Language.English) +
                            ", we will be able to provide you with fast and adequate help." },
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
    forEach(linjer) { linje ->
        paragraph {
            text(bokmal { +linje }, nynorsk { +linje }, english { +linje })
        }
    }
}

