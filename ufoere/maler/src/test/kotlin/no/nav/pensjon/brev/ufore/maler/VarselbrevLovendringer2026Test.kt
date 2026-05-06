package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmHoyereMinstesatsForIFU
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmHoyereMinstesatsForIFUOgLavereReduksjonsprosent
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmHoyereMinstesatsForIFUOgLavereReduksjonsprosentRedigerbar
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmHoyereMinstesatsForIFURedigerbar
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmLavereReduksjonsprosent
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmLavereReduksjonsprosentRedigerbar
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class VarselbrevLovendringer2026Test {

    @Test
    fun testHtmlVarselOmLavereReduksjonsprosent() {
        LetterTestImpl(
            template = VarselOmLavereReduksjonsprosent.template,
            argument = EmptyAutobrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml(VarselOmLavereReduksjonsprosent::class.simpleName!!)
    }

    @Test
    fun testHtmlVarselOmHoyereMinstesatsForIFU() {
        LetterTestImpl(
            template = VarselOmHoyereMinstesatsForIFU.template,
            argument = EmptyAutobrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml(VarselOmHoyereMinstesatsForIFU::class.simpleName!!)
    }

    @Test
    fun testHtmlVarselOmHoyereMinstesatsForIFUOgLavereReduksjonsprosent() {
        LetterTestImpl(
            template = VarselOmHoyereMinstesatsForIFUOgLavereReduksjonsprosent.template,
            argument = EmptyAutobrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml(VarselOmHoyereMinstesatsForIFUOgLavereReduksjonsprosent::class.simpleName!!)
    }

    @Test
    fun testHtmlVarselOmLavereReduksjonsprosentS() {
        LetterTestImpl(
            template = VarselOmLavereReduksjonsprosentRedigerbar.template,
            argument = EmptyRedigerbarBrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml("VarselOmLavereReduksjonsprosentS")
    }

    @Test
    fun testHtmlVarselOmHoyereMinstesatsForIFUS() {
        LetterTestImpl(
            template = VarselOmHoyereMinstesatsForIFURedigerbar.template,
            argument = EmptyRedigerbarBrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml("VarselOmHoyereMinstesatsForIFUS")
    }

    @Test
    fun testHtmlVarselOmHoyereMinstesatsForIFUOgLavereReduksjonsprosentS() {
        LetterTestImpl(
            template = VarselOmHoyereMinstesatsForIFUOgLavereReduksjonsprosentRedigerbar.template,
            argument = EmptyRedigerbarBrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml("VarselOmHoyereMinstesatsForIFUOgLavereReduksjonsprosentS")
    }
}
