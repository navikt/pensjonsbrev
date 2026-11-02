package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_KRAV_SENDT_UTLAND
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object KravSendtUtland : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_KRAV_SENDT_UTLAND
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(SKJEMA_FOR_BANKOPPLYSNINGER)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Krav alderspensjon sendt til utlandet",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Kravet ditt om alderspensjon er sendt til " + fritekst("land") })
        }
        outline {
            paragraph {
                text(bokmal { + "Vi viser til ditt krav om alderspensjon fra " + fritekst("land") + " mottatt hos oss " + fritekst("dato") + ". Det er denne datoen som blir kravdatoen i avtalelandet. " })
            }

            paragraph {
                text(bokmal { + "Kravet er sendt til " + fritekst("land") + " i dag. " })
            }

            title1 {
                text(bokmal { + "Hva skjer i avtalelandet?" })
            }

            paragraph {
                text(bokmal { + "Kravet behandles av trygdemyndigheter i " + fritekst("land") + " etter deres nasjonale lovgivning. Når vedtaket er fattet, sendes det til deg og Nav arbeid og ytelser. " })
            }
            paragraph {
                text(bokmal { + "Vi kan dessverre ikke si noe om andre lands saksbehandlingstid utover at den er svært varierende og kan være lang. " })
            }
        }
    }
}
