package no.nav.pensjon.brev.template

import no.nav.brev.brevbaker.createTemplate
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.render.dsl.SomeDto
import no.nav.pensjon.brev.template.render.dsl.testLetterMetadata
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@OptIn(TemplateRootScope.RedigerbartVedlegg::class)
class LetterTemplateStableHashTest {

    private fun vedlegg(tekst: String) = createAttachment<LangNynorsk, EmptyVedleggData>(
        title = { text(nynorsk { +"Vedlegg" }) },
    ) {
        paragraph { text(nynorsk { +tekst }) }
    }

    private fun mal(redigerbart: String? = null, ikkeRedigerbart: String? = null) = createTemplate(
        letterDataType = SomeDto::class,
        languages = languages(Nynorsk),
        letterMetadata = testLetterMetadata,
    ) {
        title { text(nynorsk { +"tittel" }) }
        outline { paragraph { text(nynorsk { +"innhold" }) } }
        if (redigerbart != null) {
            includeAttachmentRedigerbar(VedleggId("vedlegg1"), vedlegg(redigerbart), EmptyVedleggData.expr())
        }
        if (ikkeRedigerbart != null) {
            includeAttachment(vedlegg(ikkeRedigerbart))
        }
    }

    @Test
    fun `endring i redigerbart vedlegg endrer hash`() {
        assertThat(mal(redigerbart = "a").stableHashCode()).isEqualTo(mal(redigerbart = "a").stableHashCode())
        assertThat(mal(redigerbart = "a").stableHashCode()).isNotEqualTo(mal(redigerbart = "b").stableHashCode())
    }

    @Test
    fun `nytt redigerbart vedlegg endrer hash`() {
        assertThat(mal().stableHashCode()).isNotEqualTo(mal(redigerbart = "a").stableHashCode())
    }

    @Test
    fun `ikke-redigerbare vedlegg paavirker ikke hash`() {
        assertThat(mal(ikkeRedigerbart = "a").stableHashCode()).isEqualTo(mal().stableHashCode())
        assertThat(mal(ikkeRedigerbart = "a").stableHashCode()).isEqualTo(mal(ikkeRedigerbart = "b").stableHashCode())
    }
}
