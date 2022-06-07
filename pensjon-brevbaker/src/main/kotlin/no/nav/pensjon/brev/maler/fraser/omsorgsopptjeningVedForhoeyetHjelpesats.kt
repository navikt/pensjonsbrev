package no.nav.pensjon.brev.maler.fraser

// MF-000094
// Vedtak - innvilgelse av omsorgsopptjening ved forhøyet hjelpestønad sats 3 eller 4

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.text

val omsorgsopptjen_Tittel_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Du får pensjonsopptjening for omsorgsarbeid for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>",
            Nynorsk to "Du får pensjonsopptening for omsorgsarbeid for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>",
            English to "Earned pension savings for unpaid care work for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>"
        )
    }
}

val omsorgsopptjenHjelpestoenadInnledn_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit>  {
    title1 {
        text(
            Bokmal to "Du har fått godkjent pensjonsopptjening for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng> fordi du utfører omsorgsarbeid for barn som har rett til forhøyet hjelpestønad etter sats 3 eller 4.",
            Nynorsk to "Du har fått godkjent pensjonsopptening for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng> fordi du utfører omsorgsarbeid for barn som har rett til forhøgd hjelpestønad etter sats 3 eller 4.",
            English to "You have been credited pension earnings for <omsorgGodskrGrunnlagAr.arInnvilgetOmrsorgspoeng>, because you care for a child who is entitled to higher rate assistance allowance at rate 3 or rate 4."
        )
    }
}

val omsorgsopptjenHjelpestKap20Hjemmel_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
    title1 {
        text(
            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 3-16 første ledd bokstav b, 20-8 første ledd bokstav b og 20-21.",
            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 3-16 første ledd bokstav b, 20-8 første ledd bokstav b og 20-21.",
            English to "This decision was made pursuant to the provisions of sections 3-16, first paragraph, litra b, 20-8 first paragraph, litra b, and 20-21 of the National Insurance Act."
        )
    }
}

val omsorgsopptjenHjelpestønadHjemmel2_001 = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {

}