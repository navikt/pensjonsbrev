package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU1155_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // [TBU1155EN, TBU1155NN, TBU1155]

        paragraph {
            text(
                Bokmal to "Du har jobbet mer enn 50 prosent over en lengre periode etter at du fylte 26 år. Du søkte om uføretrygd etter at du fylte 36 år og oppfyller derfor ikke dette vilkåret.",
                Nynorsk to "Du har arbeidd i meir enn 50 prosent over ein lengre periode etter at du fylte 26 år. Du søkte om uføretrygd etter at du fylte 36 år og oppfyller derfor ikkje dette vilkåret.",
                English to "You have been working more than 50 percent over a long period after turning 26. Your application for disability benefit was submitted after you turned 36. Therefore, you do not meet the criteria.",
            )
        }
    }
}
