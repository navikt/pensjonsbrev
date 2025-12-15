package no.nav.pensjon.brev.alder.maler.vedlegg.alltidValgbare

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
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
        formText(
            Size.FILL,
            {
                text(
                    bokmal { +"Norsk personnummer (11 siffer):" },
                    english { +"Norwegian Identity number (11 digits):" }
                )
            }
        )
    }
    paragraph {
        formText(
            Size.FILL,
            {
                text(
                    bokmal { +"Navn:" },
                    english { +"Name:" }
                )
            }
        )
    }
    paragraph {
        formText(
            Size.FILL,
            {
                text(
                    bokmal { +"Adresse:" },
                    english { +"Address:" }
                )
            }
        )
    }
    paragraph {
        formText(
            Size.FILL,
            {
                text(
                    bokmal { +"IBAN-nummer/ kontonummer:" },
                    english { +"IBAN-number/ account number:" }
                )
            }
        )
    }
    paragraph {
        formText(
            Size.FILL,
            {
                text(
                    bokmal { +"SWIFT/BIC-kode:" },
                    english { +"SWIFT/ BIC-code:" }
                )
            }
        )
    }
    paragraph {
        text(
            bokmal { +"Dette er koden man oppgir ved utenlandsbetalinger. Koden finner du som regel på banken sine hjemmesider." },
            english { +"This is the code you enter when making international payments. You can usually find the code on your bank's website." }
        )
    }
    paragraph {
        formText(
            Size.FILL,
            {
                text(
                    bokmal { +"Bankens navn:" },
                    english { +"Name of your bank:" }
                )
            }
        )
    }
    paragraph {
        formText(
            Size.FILL,
            {
                text(
                    bokmal { +"Bankens adresse:" },
                    english { +"Bank address:" }
                )
            }
        )
    }
    paragraph {
        formText(
            Size.FILL,
            {
                text(
                    bokmal { +"Underskrift:" },
                    english { +"Signature:" }
                )
            }
        )
    }
    paragraph {
        text(
            bokmal { +"Kopi av legitimasjon (pass, førerkort eller ID-kort med bilde) må vedlegges." },
            english { +"Copy of valid identity document (e.g. passport, driving license, bank card with photo) must be attached." }
        )
    }
}