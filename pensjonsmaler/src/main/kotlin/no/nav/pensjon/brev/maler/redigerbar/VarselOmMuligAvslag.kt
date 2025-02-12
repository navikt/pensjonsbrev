package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VarselOmMuligAvslag : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    // PE_IY_03_051
    override val kode = Pesysbrevkoder.Redigerbar.PE_VARSEL_OM_MULIG_AVSLAG
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.VARSEL
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.all

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = EmptyRedigerbarBrevdata::class,
            languages = languages(Language.Bokmal, Language.English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Varsel om mulig avslag/opphør p.g.a. manglende opplysninger",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Language.Bokmal to "Varsel om mulig avslag eller opphør av stønad på grunn av manglende opplysninger",
                    Language.English to "Notice of possible refusal or termination of benefit due to inadequate information",
                )
            }
            outline {
                // [PE_IY_03_051_tekst]

                paragraph {
                    text(
                        Language.Bokmal to "I forbindelse med behandlingen av stønadssaken din har vi bedt deg sende oss nødvendige opplysninger og dokumentasjon.",
                        Language.English to "Regarding the processing of your benefit case, we have asked you to send us the necessary details and documentary evidence.",
                    )
                }
                // [PE_IY_03_051_tekst]

                paragraph {
                    text(
                        Language.Bokmal to "Vi har ikke hørt fra deg og minner om dette.",
                        Language.English to "We have not heard from you and would like to remind you to provide us with the necessary details and documentary evidence.",
                    )
                }
                // [PE_IY_03_051_tekst]

                paragraph {
                    text(
                        Language.Bokmal to "Vi gjør deg oppmerksom på at søknaden din kan bli avslått på grunn av manglende opplysninger eller løpende stønad blir stanset hvis vi ikke hører fra deg innen 14 dager. Dette går fram av paragrafene 21-3 og 21-7 i folketrygdloven.",
                        Language.English to "Please notice that your application may be refused due to inadequate information. The current benefit will be stopped unless we hear from you within 14 days. The authority for this will be found in Sections 21-3 and 21-7 of the National Insurance Act.",
                    )
                }
                // [PE_IY_03_051_tekst]
            }
        }
}
