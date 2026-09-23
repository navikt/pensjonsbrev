package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntekt

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.text

data class Grunnbeloep(
    val pe: Expression<PEgruppe10>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(
            pe.pebrevkode().notEqualTo("PE_UT_05_100")
                    and pe.pebrevkode().notEqualTo("PE_UT_07_100")
        ) {
            paragraph {
                text(
                    bokmal { + "Folketrygdens grunnbeløp endres hvert år, og uføretrygden din blir justert ut fra dette." },
                    nynorsk { + "Grunnbeløpet i folketrygda blir endra kvart år, og uføretrygda di blir justert ut frå dette." },
                )
            }
        }
    }
}