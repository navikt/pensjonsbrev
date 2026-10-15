package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.PdfByggerTestService
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.brev.brevbaker.renderTestPDF
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.Fixtures
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO
import no.nav.pensjon.etterlatte.fixtures.createOmstillingsstoenadAktivitetspliktInformasjon6mndDto
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.NasjonalEllerUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon10mndInnhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon4mndInnhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.OmstillingsstoenadAktivitetspliktInformasjon6mndInnhold
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

internal class OmstillingsstoenadAktivitetspliktUtlandTest {

    private val languages = listOf(Language.Bokmal, Language.Nynorsk, Language.English)

    @Test
    fun `utland variants render as HTML`() {
        languages.forEach { language ->
            LetterTestImpl(
                OmstillingsstoenadAktivitetspliktInformasjon4mndInnhold.template,
                createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO(
                    nasjonalEllerUtland = NasjonalEllerUtland.UTLAND,
                ),
                language,
                Fixtures.felles,
            ).renderTestHtml(fileName(EtterlatteBrevKode.AKTIVITETSPLIKT_INFORMASJON_4MND_INNHOLD, language))

            LetterTestImpl(
                OmstillingsstoenadAktivitetspliktInformasjon6mndInnhold.template,
                createOmstillingsstoenadAktivitetspliktInformasjon6mndDto(
                    nasjonalEllerUtland = NasjonalEllerUtland.UTLAND,
                ),
                language,
                Fixtures.felles,
            ).renderTestHtml(fileName(EtterlatteBrevKode.AKTIVITETSPLIKT_INFORMASJON_6MND_INNHOLD, language))

            LetterTestImpl(
                OmstillingsstoenadAktivitetspliktInformasjon10mndInnhold.template,
                createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO(
                    nasjonalEllerUtland = NasjonalEllerUtland.UTLAND,
                ),
                language,
                Fixtures.felles,
            ).renderTestHtml(fileName(EtterlatteBrevKode.AKTIVITETSPLIKT_INFORMASJON_10MND_INNHOLD, language))
        }
    }

    @Test
    @Tag(TestTags.MANUAL_TEST)
    fun `utland variants render as PDF`() {
        val pdfByggerService = PdfByggerTestService()

        languages.forEach { language ->
            LetterTestImpl(
                OmstillingsstoenadAktivitetspliktInformasjon4mndInnhold.template,
                createOmstillingsstoenadAktivitetspliktInformasjon4mndInnholdDTO(
                    nasjonalEllerUtland = NasjonalEllerUtland.UTLAND,
                ),
                language,
                Fixtures.felles,
            ).renderTestPDF(
                fileName(EtterlatteBrevKode.AKTIVITETSPLIKT_INFORMASJON_4MND_INNHOLD, language),
                pdfByggerService = pdfByggerService,
            )

            LetterTestImpl(
                OmstillingsstoenadAktivitetspliktInformasjon6mndInnhold.template,
                createOmstillingsstoenadAktivitetspliktInformasjon6mndDto(
                    nasjonalEllerUtland = NasjonalEllerUtland.UTLAND,
                ),
                language,
                Fixtures.felles,
            ).renderTestPDF(
                fileName(EtterlatteBrevKode.AKTIVITETSPLIKT_INFORMASJON_6MND_INNHOLD, language),
                pdfByggerService = pdfByggerService,
            )

            LetterTestImpl(
                OmstillingsstoenadAktivitetspliktInformasjon10mndInnhold.template,
                createOmstillingsstoenadAktivitetspliktInformasjon10mndInnholdDTO(
                    nasjonalEllerUtland = NasjonalEllerUtland.UTLAND,
                ),
                language,
                Fixtures.felles,
            ).renderTestPDF(
                fileName(EtterlatteBrevKode.AKTIVITETSPLIKT_INFORMASJON_10MND_INNHOLD, language),
                pdfByggerService = pdfByggerService,
            )
        }
    }

    private fun fileName(brevkode: EtterlatteBrevKode, language: Language) =
        "${brevkode.name}_UTLAND_${language.javaClass.simpleName}"
}
