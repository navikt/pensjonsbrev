package no.nav.pensjon.brev.ufore.maler.info

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_FORESPORSEL_TRYGDETID_SENDT_LAND
import no.nav.pensjon.brev.ufore.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object ForesporselTrygdetidSendtLand : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_FORESPORSEL_TRYGDETID_SENDT_LAND
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om at forespørsel om trygdetid er sendt til avtaleland",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Forespørsel om trygdetid er sendt til " + fritekst("land") })
        }
        outline {
            paragraph {
                text(bokmal { + "Vi viser til ditt krav om uføretrygd av " + fritekst("dato") + ". " })
            }
            paragraph {
                text(bokmal { + "Vi har i brev av " + fritekst("dato") + " funnet at vilkårene i folketrygdlovens § 12-2 ikke er oppfylt etter nasjonale regler. " })
            }
            paragraph {
                text(bokmal { + "Vi skal ta stilling til om vilkårene i § 12-2 kan oppfylles gjennom sammenlegging av medlemstid for tilsvarende periode i " + fritekst("land") + " så snart de mottar bekreftelse på medlemskapsperioder i " + fritekst("land") + " fra trygdemyndigheten der. " })
            }
            paragraph {
                text(bokmal { + "Forespørsel om medlemskapsperioder i " + fritekst("land") + " er sendt til trygdemyndighetene i " + fritekst("land") + " dag. " })
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
