package no.nav.pensjon.brev.alder.maler.avslag.gradsendring

import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.text

data class InnholdSoeknadFoerEttAar(
    val regelverkType: Expression<AlderspensjonRegelverkType>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Du kan endre uttaksgraden ett år etter at du startet å ta ut alderspensjon. " +
                        "Deretter kan du endre uttaksgraden med tolv måneders mellomrom. " +
                        "Du kan likevel på ethvert tidspunkt ta ut 100 prosent alderspensjon eller stanse pensjonen." },
                nynorsk { + "Du kan endre uttaksgraden eitt år etter at du starta å ta ut alderspensjon. Deretter kan" +
                        " du endre uttaksgraden med tolv månadars mellomrom. Du kan likevel på eitkvart tidspunkt ta ut" +
                        " 100 prosent alderspensjon eller stanse pensjonen." },
                english { + "You may change your pension level one year after you began drawing a retirement pension." +
                        " After that, you may change your pension level every 12 months. You may, however, still retire" +
                        " completely or stop drawing your pension at any time." },
            )
        }

        paragraph {
            text(
                bokmal { + "Det er mindre enn tolv måneder siden du sist endret uttaksgraden eller startet å ta ut alderspensjon. " +
                        "Derfor har vi avslått søknaden din." },
                nynorsk { + "Det er mindre enn tolv månader sidan du sist endra uttaksgraden eller starta å ta ut alderspensjon." +
                        " Derfor har vi avslått søknaden din." },
                english { + "It has been less than 12 months since you last changed your pension level or started drawing your" +
                        " retirement pension. Therefore, we have declined your application." },
            )
        }
        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2025)) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 20-14." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova § 20-14." },
                    english { + "This decision was made pursuant to the provisions of § 20-14 of the National Insurance Act." },
                )
            }
        }.orShowIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2016)) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-10 og 20-14." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-10 og 20-14." },
                    english { + "This decision was made pursuant to the provisions of §§ 19-10 and 20-14 of the National Insurance Act." },
                )
            }
        }.orShowIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2011)) {
            paragraph {
                text(
                    bokmal { + "Vedtaket er gjort etter folketrygdloven § 19-10." },
                    nynorsk { + "Vedtaket er gjort etter folketrygdlova § 19-10." },
                    english { + "This decision was made pursuant to the provisions of § 19-10 of the National Insurance Act." },
                )
            }
        }

        paragraph {
            text(
                bokmal { + "Dersom du er usikker på når alderspensjonen din ble innvilget eller endret sist, " +
                        "kan du finne mer informasjon om dette i Din pensjon på ${Constants.DIN_PENSJON_URL}." },
                nynorsk { + "Dersom du er usikker på når alderspensjonen din blei innvilga eller endra sist," +
                        " kan du finne meir informasjon om dette i nettenesta Din pensjon på ${Constants.DIN_PENSJON_URL}." },
                english { + "If you are not sure when your retirement pension was granted or last changed, you can" +
                        " find more information by logging on to the online service " + quoted("Din pensjon") +" at" +
                        " ${Constants.DIN_PENSJON_URL}." },
            )
        }
    }
}