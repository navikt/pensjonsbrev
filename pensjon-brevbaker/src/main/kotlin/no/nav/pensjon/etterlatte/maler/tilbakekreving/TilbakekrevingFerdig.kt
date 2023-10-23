package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdigDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingFerdigDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.erBP
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingInnholdDTOSelectors.erOMS

data class TilbakekrevingFerdigDTO(
    override val innhold: List<Element>,
    val data: TilbakekrevingInnholdDTO
) : BrevDTO

@TemplateModelHelpers
object TilbakekrevingFerdig : EtterlatteTemplate<TilbakekrevingFerdigDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TILBAKEKREVING_FERDIG

    override val template = createTemplate(
        name = kode.name,
        letterDataType = TilbakekrevingFerdigDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Tilbakekreving",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                Bokmal to "Du må betale tilbake gjenlevendepensjon",
                Nynorsk to "Du må betale tilbake gjenlevendepensjon",
                English to "Du må betale tilbake gjenlevendepensjon"
            )
        }
        outline {

            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(DuHarRettTilAaKlage)
            includePhrase(DuHarRettTilInnsyn)
            includePhrase(HarDuSpoersmaal(data.erOMS, data.erBP))
        }

        includeAttachment(tilbakekrevingVedlegg, data)
    }
}

private object DuHarRettTilAaKlage : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Bokmal to "Du har rett til å klage",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to """
Hvis du mener at vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok dette vedtaket.  
Klagen kan sendes via innlogging på nettsiden vår nav.no, eller sendes til oss i posten. 
Skriftlig klage som sendes i posten må inneholde navn, fødselsnummer og adresse, og den må være underskrevet av deg.
Bruk gjerne skjemaet som du finner på ${Constants.KLAGE_URL}.  
Klagen sendes til ${Constants.POSTADRESSE}.
                """.trimIndent(),
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to "RESTERENDE FOR KLAGE KOMMER", // TODO EY-2806
                Nynorsk to "",
                English to "",
            )
        }

    }
}

private object DuHarRettTilInnsyn : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Bokmal to "Du har rett til innsyn",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            text(
                Bokmal to """
På nav.no/Min side kan du se dokumentene i saken din. 
               """.trimIndent(),
                Nynorsk to "",
                English to ""
            )
        }
    }
}

private data class HarDuSpoersmaal(
    val erOMS: Expression<Boolean>,
    val erBP: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Bokmal to "Har du spørsmål?",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            showIf(erOMS) {
                text(
                    Bokmal to "Du kan finne svar på ${Constants.OMS_URL} ",
                    Nynorsk to "",
                    English to "",
                )
            }
            showIf(erBP) {
                text(
                    Bokmal to "Du kan finne svar på ${Constants.BARNEPENSJON_URL} ",
                    Nynorsk to "",
                    English to "",
                )
            }
            text(
                Bokmal to """
På ${Constants.KONTAKT_URL} kan du chatte eller skrive til oss.
Du kan også kontakte oss på telefon ${Constants.KONTAKTTELEFON_PENSJON}, hverdager 09.00-15.00.
Hvis du oppgir fødselsnummer, kan vi lettere gi deg rask og god hjelp.
                """.trimIndent(),
                Nynorsk to "",
                English to ""
            )
        }
    }
}