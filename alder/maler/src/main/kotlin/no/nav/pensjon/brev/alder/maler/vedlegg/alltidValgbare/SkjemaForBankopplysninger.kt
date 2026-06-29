package no.nav.pensjon.brev.alder.maler.vedlegg.alltidValgbare

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val skjemaForBankopplysninger = createAttachment<LangBokmalEnglish, EmptyVedleggData>(
    title = {
        text(
            bokmal { +"Skjema for bankopplysninger" },
            english { +"Bank details" }
        )
    },
    includeSakspart = false
) {
    paragraph {
        text(
            bokmal { +"Norsk personnummer (11 siffer): ___________________________________________" },
            english { +"Norwegian Identity number (11 digits): ___________________________________" },
        )
    }
    paragraph {
        text(
            bokmal { +"Navn: ___________________________________________________________________" },
            english { +"Name: ___________________________________________________________________" },
        )
    }
    paragraph {
        text(
            bokmal { +"Adresse: ________________________________________________________________" },
            english { +"Address: ________________________________________________________________" },
        )
    }
    paragraph {
        text(
            bokmal { +"IBAN-nummer/ kontonummer: ________________________________________________" },
            english { +"IBAN-number/ account number: _____________________________________________" },
        )
    }
    paragraph {
        text(
            bokmal { +"SWIFT/BIC-kode: __________________________________________________________" },
            english { +"SWIFT/ BIC-code: _________________________________________________________" },
        )
    }
    paragraph {
        text(
            bokmal { +"Dette er koden man oppgir ved utenlandsbetalinger. Koden finner du som regel på banken sine hjemmesider." },
            english { +"This is the code you enter when making international payments. You can usually find the code on your bank's website." }
        )
    }
    paragraph {
        text(
            bokmal { +"Bankens navn: ___________________________________________________________" },
            english { +"Name of your bank: _______________________________________________________" },
        )
    }
    paragraph {
        text(
            bokmal { +"Bankens adresse: ________________________________________________________" },
            english { +"Bank address: ___________________________________________________________" },
        )
    }
    paragraph {
        text(
            bokmal { +"Underskrift: ____________________________________________________________" },
            english { +"Signature: ______________________________________________________________" },
        )
    }
    paragraph {
        text(
            bokmal { +"Kopi av legitimasjon (pass, førerkort eller ID-kort med bilde) må vedlegges." },
            english { +"Copy of valid identity document (e.g. passport, driving license, bank card with photo) must be attached." }
        )
    }
}