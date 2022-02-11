package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.no.nav.pensjon.brev.maler.vedlegg.MaanedligeUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.no.nav.pensjon.brev.maler.vedlegg.maanedligeUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.newText
import org.junit.jupiter.api.Test
import java.time.LocalDate

class MaanedligeUfoeretrygdFoerSkatt {

    @Test
    fun testVedlegg() {
        val template = createTemplate(
            name = "test-template",
            base = PensjonLatex,
            letterDataType = Unit::class,
            title = newText(Bokmal to ""),
            letterMetadata = LetterMetadata(
                "test mal",
                isSensitiv = false
            ),
        ) {
            outline {

            }

            includeAttachment(
                maanedligeUfoeretrygdFoerSkatt,
                MaanedligeUfoeretrygdFoerSkattDto(LocalDate.now(), LocalDate.now(), 91543).expr()
            )
        }

        Letter(
            template,
            Unit,
            Bokmal,
            Fixtures.fellesAuto
        ).render()
    }
}