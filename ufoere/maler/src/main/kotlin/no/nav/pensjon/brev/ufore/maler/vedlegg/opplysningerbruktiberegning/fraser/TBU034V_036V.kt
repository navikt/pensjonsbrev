package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU034V_036V. Generell tekst om aarlig G-justering samt eget avsnitt om
 * yrkesskade/yrkessykdom. Brevkode-/beloepsgrense-betingelser er erstattet med flagg.
 */
data class TBU034V_036V(
    val flagg: Expression<Visningsflagg>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(flagg.visGrunnbeloepJustering) {
            paragraph {
                text(
                    bokmal { + "Folketrygdens grunnbeløp endres hvert år, og uføretrygden din blir justert ut fra dette." },
                    nynorsk { + "Grunnbeløpet i folketrygda blir endra kvart år, og uføretrygda di blir justert ut frå dette." },
                )
            }
        }

        showIf(flagg.visYrkesskadeBeregning) {
            title1 {
                text(
                    bokmal { + "Beregning av uføretrygd som skyldes yrkesskade eller yrkessykdom" },
                    nynorsk { + "Berekning av uføretrygd som kjem av yrkesskade eller yrkessjukdom" },
                )
            }
            paragraph {
                text(
                    bokmal { + "For den delen av uførheten din som skyldes en godkjent yrkesskade eller yrkessykdom, fastsetter vi en yrkesskadegrad. Det er yrkesskadegraden som bestemmer hvor mye av uføretrygden din som skal beregnes etter særbestemmelser. Vi tar utgangspunkt i inntekten på skadetidspunktet ditt, når vi gjør beregningen. Skadetidspunktet blir alltid satt til den første i måneden på det tidspunktet du får en yrkesskade eller yrkessykdom." },
                    nynorsk { + "For den delen av uførleiken din som kjem av ein godkjend yrkesskade eller yrkessjukdom, fastset vi ein yrkesskadegrad. Det er yrkesskadegraden som bestemmer kor mykje av uføretrygda di som skal bereknast etter særreglar. Når vi bereknar, tek vi utgangspunkt i inntekta på skadetidspunktet ditt. Skadetidspunktet blir alltid satt til den første i månaden på det tidspunktet du får ein yrkesskade eller yrkessjukdom." },
                )
            }
        }
    }
}
