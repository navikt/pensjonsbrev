package no.nav.pensjon.brev.maler.fraser


import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*


// Phrases that are repeated and found in VedleggOrienteringOmRettigheter.kt
//  vedleggInnsynSakPensjon_001
//  vedleggInnsynSakPensjon_001
//  vedleggInnsynSakUTPesys_001
//  vedleggHjelpFraAndre_001
//  vedleggKlagePensjon_001

object VedleggInnsynSakUnder18_001 : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Er du under 18 år, har vergen din rett til å se dokumentene i saken din på vegne av deg.",
                Nynorsk to "Er du under 18 år, har verja di rett til å sjå dokumenta i saka di på vegner av deg.",
                English to "If you are under the age of 18, your guardian is entitled to see the documents in your case on your behalf."
            )
        }
}

