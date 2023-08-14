package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object Institusjonsoppholdfraser {
    object Innvilgelse : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Innlagt på institusjon – ingen reduksjon pga dokumenterte utgifter",
                    Nynorsk to "",
                    English to "",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du er innlagt i helseinstitusjon og barnepensjonen skal etter hovedregelen reduseres som følge av dette. Du har dokumentert nødvendige utgifter til bolig og barnepensjonen din vil derfor ikke være redusert når du er innlagt i institusjon.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }

    data class Lover(val erEtterbetaling: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(erEtterbetaling) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5, 18-8 og § 22-12.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5, 18-8 § 22-12 og § 22-13.",
                        Nynorsk to "",
                        English to "",
                    )
                }
            }
        }
    }
}
