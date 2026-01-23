package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselOmMuligAvslag : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_03_051
    override val kode = Pesysbrevkoder.Redigerbar.PE_VARSEL_OM_MULIG_AVSLAG
    override val kategori = Brevkategori.VARSEL
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.all

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel om mulig avslag/opphør p.g.a. manglende opplysninger",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Varsel om mulig avslag eller opphør av ytelse på grunn av manglende opplysninger" },
                english { + "Notice of possible refusal or termination of benefit due to inadequate information" },
            )
        }
        outline {
            //[PE_IY_03_051_tekst]

            paragraph {
                text(
                    bokmal { + "I forbindelse med behandlingen av saken din har vi bedt deg sende oss nødvendige opplysninger og dokumentasjon." },
                    english { + "Regarding the processing of your case, we have asked you to send us the necessary details and documentary evidence." },
                )
            }
            //[PE_IY_03_051_tekst]

            paragraph {
                text(
                    bokmal { + "Vi har ikke hørt fra deg og minner om dette." },
                    english { + "We have not heard from you and would like to remind you to provide us with the necessary details and documentary evidence." },
                )
            }
            //[PE_IY_03_051_tekst]

            paragraph {
                text(
                    bokmal { + "Vi gjør deg oppmerksom på at søknaden din kan bli avslått på grunn av manglende opplysninger eller løpende ytelse blir stanset hvis vi ikke hører fra deg innen 14 dager. Dette går fram av paragrafene 21-3 og 21-7 i folketrygdloven." },
                    english { + "Please notice that your application may be refused due to inadequate information. The current benefit will be stopped unless we hear from you within 14 days. The authority for this will be found in Sections 21-3 and 21-7 of the National Insurance Act." },
                )
            }
            //[PE_IY_03_051_tekst]
        }
    }
}