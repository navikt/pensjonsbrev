package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmOktMinsteIFU
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmOktMinsteIFUOgLavereReduksjonsprosent
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmOktMinsteIFUOgLavereReduksjonsprosentRedigerbar
import no.nav.pensjon.brev.ufore.maler.lovendringer2026.VarselOmOktMinsteIFURedigerbar
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
    fun testHtmlOktMinsteIFU() {
        LetterTestImpl(
            template = VarselOmOktMinsteIFU.template,
            argument = EmptyAutobrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml(VarselOmOktMinsteIFU::class.simpleName!!)
    }

    @Test
    fun testHtmlVarselOmOktMinsteIFUOgLavereReduksjonsprosent() {
        LetterTestImpl(
            template = VarselOmOktMinsteIFUOgLavereReduksjonsprosent.template,
            argument = EmptyAutobrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml(VarselOmOktMinsteIFUOgLavereReduksjonsprosent::class.simpleName!!)
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
    fun testHtmlVarselOmOktMinsteIFUS() {
        LetterTestImpl(
            template = VarselOmOktMinsteIFURedigerbar.template,
            argument = EmptyRedigerbarBrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml("VarselOmOktMinsteIFUS")
    }

    @Test
    fun testHtmlVarselOmOktMinsteIFUOgLavereReduksjonsprosentS() {
        LetterTestImpl(
            template = VarselOmOktMinsteIFUOgLavereReduksjonsprosentRedigerbar.template,
            argument = EmptyRedigerbarBrevdata,
            language = Language.Bokmal,
            felles = Fixtures.felles
        ).renderTestHtml("VarselOmOktMinsteIFUOgLavereReduksjonsprosentS")
    }
}
