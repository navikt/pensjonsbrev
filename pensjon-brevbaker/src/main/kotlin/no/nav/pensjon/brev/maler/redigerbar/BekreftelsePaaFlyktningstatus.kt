package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Felles.fulltNavn
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.BrukerSelectors.foedselsnummer
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.bruker
import no.nav.pensjon.brevbaker.api.model.FoedselsnummerSelectors.value
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.navn

@TemplateModelHelpers
object BekreftelsePaaFlyktningstatus : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    // PE_IY_03_049
    override val kode = Pesysbrevkoder.Redigerbar.PE_BEKREFTELSE_PAA_FLYKTNINGSTATUS
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = Sakstype.all


    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyRedigerbarBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Bekreftelse på flyktningstatus",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                Bokmal to "Bekreftelse på flyktningstatus"
            )
        }
        outline {
            paragraph {
                textExpr(
                    Bokmal to felles.avsenderEnhet.navn
                            + " ønsker å få bekreftet om følgende person er registrert med flyktningstatus hos dere:"
                )
            }
            paragraph {
                textExpr(
                    Bokmal to felles.bruker.fulltNavn()
                )
            }
            paragraph {
                textExpr(
                    Bokmal to felles.bruker.foedselsnummer.value
                )
            }
            paragraph {
                text(Bokmal to "Vi ønsker i den forbindelse kopi av vedtaket og dato for ankomst til Norge.")
            }
        }
    }
}

