package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselOmMuligAvslagDto
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object VarselOmMuligAvslagLegacy : RedigerbarTemplate<VarselOmMuligAvslagDto> {


    override val kode = Brevkode.Redigerbar.PE_VARSEL_OM_MULIG_AVSLAG
    override val kategori: TemplateDescription.Brevkategori = TemplateDescription.Brevkategori.VARSEL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = VarselOmMuligAvslagDto::class,
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel om mulig avslag/opphør p.g.a. manglende opplysninger",
            isSensitiv = false,
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Varsel om mulig avslag eller opphør av stønad på grunn av manglende opplysninger",
                English to "Notice of possible refusal or termination of benefit due to inadequate information",
            )
        }
        outline {
            //[PE_IY_03_051_tekst]

            paragraph {
                text(
                    Bokmal to "I forbindelse med behandlingen av stønadssaken din har vi bedt deg sende oss nødvendige opplysninger og dokumentasjon.",
                    English to "Regarding the processing of your benefit case, we have asked you to send us the necessary details and documentary evidence.",
                )
            }
            //[PE_IY_03_051_tekst]

            paragraph {
                text(
                    Bokmal to "Vi har ikke hørt fra deg og minner om dette.",
                    English to "We have not heard from you and would like to remind you to provide us with the necessary details and documentary evidence.",
                )
            }
            //[PE_IY_03_051_tekst]

            paragraph {
                text(
                    Bokmal to "Vi gjør deg oppmerksom på at søknaden din kan bli avslått på grunn av manglende opplysninger eller løpende stønad blir stanset hvis vi ikke hører fra deg innen 14 dager. Dette går fram av paragrafene 21-3 og 21-7 i folketrygdloven.",
                    English to "Please notice that your application may be refused due to inadequate information. The current benefit will be stopped unless we hear from you within 14 days. The authority for this will be found in Sections 21-3 and 21-7 of the National Insurance Act.",
                )
            }
            //[PE_IY_03_051_tekst]
        }
    }
}

