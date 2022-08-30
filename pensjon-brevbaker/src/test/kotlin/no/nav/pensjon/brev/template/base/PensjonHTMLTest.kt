package no.nav.pensjon.brev.template.base

import no.nav.pensjon.brev.*
import no.nav.pensjon.brev.api.model.maler.UfoerOmregningEnsligDto
import no.nav.pensjon.brev.maler.UfoerOmregningEnslig
import no.nav.pensjon.brev.maler.example.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.render.PensjonHTMLRenderer
import org.junit.jupiter.api.Test

class PensjonHTMLTest {
    @Test
    fun renderDesignReference() {
        Letter(
            DesignReferenceLetter.template,
            Fixtures.create<LetterExampleDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).let { PensjonHTMLRenderer.render(it) }
            .let { it.base64EncodedFiles()["index.html"]!! }
            .also { writeTestHTML("DESIGN_REFERENCE_LETTER", it) }
    }

    @Test
    fun renderUfoerOmregningEnslig() {
        Letter(
            UfoerOmregningEnslig.template,
            Fixtures.create<UfoerOmregningEnsligDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).let { PensjonHTMLRenderer.render(it) }
            .let { it.base64EncodedFiles()["index.html"]!! }
            .also { writeTestHTML("UFOER_OMREGNING_ENSLIG", it) }
    }
}