package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntekt

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.text

data class YrkesskadeForklaring(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) ) {
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