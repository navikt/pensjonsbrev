package no.nav.pensjon.brev.maler.alder.avslag.gradsendring


import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data object InnholdSoeknadFoerEttAar : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                Bokmal to "Du kan endre uttaksgraden ett år etter at du startet å ta ut alderspensjon. " +
                        "Deretter kan du endre uttaksgraden med tolv måneders mellomrom. " +
                        "Du kan likevel på ethvert tidspunkt ta ut 100 prosent alderspensjon eller stanse pensjonen.",
                Nynorsk to "",
                English to "",
            )
        }

        paragraph {
            text(
                Bokmal to "Det er mindre enn tolv måneder siden du sist endret uttaksgraden eller startet å ta ut alderspensjon. " +
                        "Derfor har vi avslått søknaden din.",
                Nynorsk to "",
                English to "",
            )
        }

        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-14",
                Nynorsk to "",
                English to "",
            )
        }

        paragraph {
            text(
                Bokmal to "Dersom du er usikker på når alderspensjonen din ble innvilget eller endret sist, " +
                        "kan du finne mer informasjon om dette i Din pensjon på ${Constants.DIN_PENSJON_URL}.",
                Nynorsk to "",
                English to "",
            )
        }
    }
}
