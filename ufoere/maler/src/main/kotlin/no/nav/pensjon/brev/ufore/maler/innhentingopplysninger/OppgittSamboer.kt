package no.nav.pensjon.brev.ufore.maler.innhentingopplysninger

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_INNH_OPPL_OPPGITT_SAMBOER
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.InnhentingOpplysningerSamboerDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.InnhentingOpplysningerSamboerDtoSelectors.SaksbehandlervalgSelectors.ukjentSamboer
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.InnhentingOpplysningerSamboerDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.FeatureToggles
import no.nav.pensjon.brev.ufore.maler.fraser.Constants
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG

@TemplateModelHelpers
object OppgittSamboer : RedigerbarTemplate<InnhentingOpplysningerSamboerDto> {

    override val featureToggle = FeatureToggles.innhentingOpplysninger.toggle

    override val kode = UT_INNH_OPPL_OPPGITT_SAMBOER
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Oppgitt samboer – ulike bostedsadresser-ukjent samboer",
            distribusjonstype = VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Du må sende flere opplysninger" })
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har mottatt melding om at du har blitt samboer. " })
            }

            showIf(saksbehandlerValg.ukjentSamboer) {
                paragraph {
                    text(bokmal { +"Vi ber om at du sender oss navn og fødselsnummer på din samboer. Vi trenger også informasjon om hvilken dato dere ble samboere. Dersom du og din samboer ikke har samme registrerte bostedsadresser må dere melde flytting til Folkeregisteret. Du kan melde flytting her: ${Constants.SKATTEETATEN_MELD_FLYTTING}" })
                }
            }.orShow {
                paragraph {
                    text(bokmal { +"Ifølge opplysninger i Folkeregisteret er dere ikke bosatt på samme adresse. " })
                }
                paragraph {
                    text(bokmal { +"Vi ber om at dere oppdaterer opplysninger i Folkeregisteret så snart som mulig med riktig flyttedato. Du kan melde flytting her: ${Constants.SKATTEETATEN_MELD_FLYTTING}" })
                }
            }

            includePhrase(Felles.MeldFraOmEndringer)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}
