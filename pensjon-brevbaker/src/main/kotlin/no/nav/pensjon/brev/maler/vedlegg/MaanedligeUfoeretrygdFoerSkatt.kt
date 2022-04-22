package no.nav.pensjon.brev.no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.MaanedligUfoeretrygdFoerSkattDto
import no.nav.pensjon.brev.maler.fraser.tabellBeregnetUTHele
import no.nav.pensjon.brev.maler.fraser.vedleggBelopUT_001
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.map
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

val maanedligUfoeretrygdFoerSkatt = createAttachment<LangBokmalNynorskEnglish, MaanedligUfoeretrygdFoerSkattDto>(
    title = newText(
        Bokmal to "Dette er din månedlige uføretrygd før skatt",
        Nynorsk to "Dette er den månadlege uføretrygda di før skatt",
        English to "This is your monthly disability benefit before tax",
    ),
) {
    val gjeldendeUfoeretrygd = argument().map { it.gjeldendeBeregnetUTPerMaaned }

    includePhrase(vedleggBelopUT_001)

    includePhrase(tabellBeregnetUTHele, gjeldendeUfoeretrygd)

    showIf(argument().map { it.tidligereUfoeretrygdPerioder.isNotEmpty()}) {
        val virkDato = argument().map { it.virkningDatoFraOgMed_krav }
        title1 {
            textExpr(
                Bokmal to "Oversikt over uføretrygdens størrelse fra ".expr() + virkDato.format(),
                Nynorsk to "Oversikt over storleik på uføretrygda frå ".expr() + virkDato.format(),
                English to "Disability benefit payment specifications as of ".expr() + virkDato.format(),
            )
        }

        paragraph {
            text(
                Bokmal to "Nedenfor ser du den månedlige uføretrygden din for tidligere perioder.",
                Nynorsk to "Nedanfor ser du uføretrygda di kvar månad for tidligare periodar.",
                English to "Below is a presentation of your monthly disability benefit for your previous periods.",
            )
        }

        forEach(argument().map { it.tidligereUfoeretrygdPerioder }) {
            includePhrase(tabellBeregnetUTHele, it)
        }
    }
}
