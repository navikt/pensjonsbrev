package no.nav.pensjon.brev.maler

import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.renderMarkup
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.InnvilgelseUfoeretrygdDto
import no.nav.pensjon.brev.maler.legacy.redigerbar.InnvilgelseUforetrygd
import no.nav.pensjon.brev.template.ContentOrControlStructure
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FinnBlokkMedID {

    @Test
    fun finnBlokkMedID() {
        //-1396155602
        //317533618
        val template = InnvilgelseUforetrygd.template.outline
        template.forEach { cs ->
            if (cs is ContentOrControlStructure.Content) {
                if (cs.content.stableHashCode() == -1396155602) {
                    println("hello: -1396155602")
                } else if (cs.content.stableHashCode() == 317533618) {
                    println("hello: 317533618")
                }
            }
        }
    }

    @Test
    fun `jaha hva er iden da`() {
        InnvilgelseUforetrygd.template.outline.forEach { cs ->
            if (cs is ContentOrControlStructure.Content) {
                if (cs.content.stableHashCode() == -1396155602) {
                    println("hello: -1396155602")
                } else if (cs.content.stableHashCode() == 317533618) {
                    println("hello: 317533618")
                }
            }
        }
    }

    @OptIn(InterneDataklasser::class)
    @Test
    fun `gi meg markup`() {
        //-1396155602
        //317533618
        val markup = LetterTestImpl(
            InnvilgelseUforetrygd.template,
            Fixtures.create<InnvilgelseUfoeretrygdDto>(),
            Language.Bokmal,
            Fixtures.fellesAuto
        ).renderMarkup()
        val blokkenMedStorB = markup.letterMarkup.blocks[19]
        assertThat(blokkenMedStorB).isInstanceOfSatisfying<ParagraphImpl>(ParagraphImpl::class.java) {
            assertThat((it.content[0] as LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl).text).isEqualTo("Vi har satt uføretidspunktet ditt til ")
        }
        println(blokkenMedStorB.id)
        assertThat(blokkenMedStorB.id).isEqualTo(-2044364376)
    }
}