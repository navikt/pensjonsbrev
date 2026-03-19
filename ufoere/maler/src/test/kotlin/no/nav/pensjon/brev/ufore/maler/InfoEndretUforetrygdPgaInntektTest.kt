package no.nav.pensjon.brev.ufore.maler

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.TestTags
import no.nav.brev.brevbaker.renderTestHtml
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.ufore.Fixtures
import no.nav.pensjon.brev.ufore.api.model.maler.info.InfoEndretUTPgaInntektDto
import no.nav.pensjon.brev.ufore.maler.info.InfoEndretUforetrygdPgaInntekt
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TestTags.MANUAL_TEST)
class InfoEndretUforetrygdPgaInntektTest {
    @Test
    fun testHtml() {
        LetterTestImpl(
            InfoEndretUforetrygdPgaInntekt.template,
            Fixtures.create<InfoEndretUTPgaInntektDto>(),
            Language.Bokmal,
            Fixtures.felles
        ).renderTestHtml(InfoEndretUforetrygdPgaInntekt.kode.name)
    }
}