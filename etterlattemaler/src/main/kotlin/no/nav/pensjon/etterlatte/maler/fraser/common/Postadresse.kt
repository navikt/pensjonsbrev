package no.nav.pensjon.etterlatte.maler.fraser.common

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

fun OutlineOnlyScope<LanguageSupport.Triple<Language.Bokmal, Language.Nynorsk, Language.English>, out Any>.postadresse(
    utland: Expression<Boolean>
) {
    showIf(utland) {
        paragraph {
            text(
                bokmal { +"Nav familie- og pensjonsytelser" },
                nynorsk { +"Nav familie- og pensjonsytelser" },
                english { +"Nav familie- og pensjonsytelser" }
            )
        }
        paragraph {
            text(
                bokmal { +"Postboks 6600 Etterstad" },
                nynorsk { +"Postboks 6600 Etterstad" },
                english { +"Postboks 6600 Etterstad" }
            )
        }
        paragraph {
            text(
                bokmal { +"0607 Oslo" },
                nynorsk { +"0607 Oslo" },
                english { +"0607 Oslo" }
            )
        }
        paragraph {
            text(
                bokmal { +"Norge/Norway" },
                nynorsk { +"Noreg/Norway" },
                english { +"Norway" }
            )
        }
    }.orShow {
        paragraph {
            text(
                bokmal { +"Nav skanning" },
                nynorsk { +"Nav skanning" },
                english { +"Nav skanning" }
            )
        }
        paragraph {
            text(
                bokmal { +"Postboks 1400" },
                nynorsk { +"Postboks 1400" },
                english { +"Postboks 1400" }
            )
        }
        paragraph {
            text(
                bokmal { +"0109 OSLO" },
                nynorsk { +"0109 OSLO" },
                english { +"0109 OSLO" }
            )
        }
    }
}

