package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntektsgrense

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object SlikFastsetterViUfoeregraden : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text (
                bokmal { + "Slik fastsetter vi uføregraden din" },
                nynorsk { + "Slik fastset vi uføregraden din" },
            )
        }

        paragraph {
            text (
                bokmal { + "Uføregraden din fastsetter vi ved å sammenligne inntekten din før og etter at du ble ufør. Har du ikke inntektsevne, setter vi uføregraden til 100 prosent. Har du fortsatt inntektsevne, vil uføregraden tilsvare inntektsevnen din som du har tapt. Uføregraden din graderes i trinn på fem prosentpoeng. Vi vurderer alltid om uføregraden skal settes lavere enn 100 prosent." },
                nynorsk { + "Uføregraden din fastset vi ved å samanlikne inntekta di før og etter at du blei ufør. Dersom du ikkje har inntektsevne, set vi uføregraden til 100 prosent. Har du framleis inntektsevne, tilsvarer uføregraden den inntektsevna di som du har tapt. Uføregraden din blir gradert i trinn på fem prosentpoeng. Vi vurderer alltid om uføregraden skal setjast lågare enn 100 prosent." },
            )
        }
    }
}
