package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.avslag

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate

object IkkeSomFoelgeAvYrkesskadeYrkessykdomFraser {
    data class BegrunnelseForVedtaket(
        val dinForelder: Expression<String>,
        val doedsdato: Expression<LocalDate>,
        val yrkesskadeEllerYrkessykdom: Expression<String>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Begrunnelse for vedtaket",
                    Nynorsk to "",
                    English to "",
                )
            }
            val formatertDoedsdato = doedsdato.format()
            paragraph {
                textExpr(
                    Bokmal to "Barnepensjon er avslått fordi ".expr() + dinForelder + " som døde " + formatertDoedsdato + " ikke døde som følge av yrkesskade/yrkessykdom. ",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjon gis når du er medlem i folketrygden og den avdøde faren eller moren i de siste fem årene før dødsfallet var medlem i folketrygden eller mottok pensjon fra folketrygden. Ved dødsfall som skyldes yrkesskade eller sykdom gis det barnepensjon etter særbestemmelser om medlemskap.",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Du har i søknaden opplyst at dødsfallet var som følge av ".expr() + yrkesskadeEllerYrkessykdom + ". Det er NAV som behandler og gjør vedtak i slike saker. Det er gjort vedtak om at dødsfallet til " + dinForelder + " ikke var som følge av yrkesskade/yrkessykdom. Du fyller ikke vilkårene for [Fritekst: begrunnelse]. Barnepensjonen er derfor avslått.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven § 18 <riktig lovanvisning>.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}
