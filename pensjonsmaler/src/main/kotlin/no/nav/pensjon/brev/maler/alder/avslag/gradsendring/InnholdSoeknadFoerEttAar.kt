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
                Nynorsk to "Du kan endre uttaksgraden eitt år etter at du starta å ta ut alderspensjon. Deretter kan" +
                        " du endre uttaksgraden med tolv månadars mellomrom. Du kan likevel på eitkvart tidspunkt ta ut" +
                        " 100 prosent alderspensjon eller stanse pensjonen.",
                English to "You may change your pension level one year after you began drawing a retirement pension." +
                        " After that, you may change your pension level every 12 months. You may, however, still retire" +
                        " completely or stop drawing your pension at any time.",
            )
        }

        paragraph {
            text(
                Bokmal to "Det er mindre enn tolv måneder siden du sist endret uttaksgraden eller startet å ta ut alderspensjon. " +
                        "Derfor har vi avslått søknaden din.",
                Nynorsk to "Det er mindre enn tolv månader sidan du sist endra uttaksgraden eller starta å ta ut alderspensjon." +
                        " Derfor har vi avslått søknaden din.",
                English to "It has been less than 12 months since you last changed your pension level or started drawing your" +
                        " retirement pension. Therefore, we have declined your application.",
            )
        }

        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven § 20-14",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova § 20-14.",
                English to "This decision was made pursuant to the provisions of § 20-14 of the National Insurance Act.",
            )
        }

        paragraph {
            text(
                Bokmal to "Dersom du er usikker på når alderspensjonen din ble innvilget eller endret sist, " +
                        "kan du finne mer informasjon om dette i Din pensjon på ${Constants.DIN_PENSJON_URL}.",
                Nynorsk to "Dersom du er usikker på når alderspensjonen din blei innvilga eller endra sist," +
                        " kan du finne meir informasjon om dette i nettenesta Din pensjon på ${Constants.DIN_PENSJON_URL}.",
                English to "If you are not sure when your retirement pension was granted or last changed, you can" +
                        " find more information by logging on to the online service \"Din pensjon\" at" +
                        " ${Constants.DIN_PENSJON_URL}.",
            )
        }
    }
}
