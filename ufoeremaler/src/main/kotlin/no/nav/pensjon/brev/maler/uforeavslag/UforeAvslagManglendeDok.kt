package no.nav.pensjon.brev.maler.uforeavslag

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.maler.fraser.Felles.*
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.*
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.SaksbehandlervalgSelectors.brukVurderingFraVilkarsvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforeAvslagManglendeDok : RedigerbarTemplate<UforeAvslagDto> {

    override val featureToggle = FeatureToggles.uforeAvslag.toggle

    override val kode = UT_AVSLAG_MANGLENDE_DOK
    override val kategori = TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 21-3",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om uføretrygd"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått din søknad om uføretrygd som vi fikk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Begrunnelse for vedtaket" })
            }
            paragraph {
                text(bokmal { +"For at vi skal kunne ta stilling til søknaden din om uføretrygd, må du gi oss de opplysningene vi trenger. " +
                        "Vi sendte deg et brev " + fritekst("dato") + " der vi ba deg sende oss dokumentene som manglet, " +
                        "og varslet deg om at søknaden din ville bli avslått dersom vi ikke fikk dem innen fristen." })
            }
            showIf(saksbehandlerValg.brukVurderingFraVilkarsvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering })
                }
            }.orShow {
                paragraph {
                    text(bokmal { + fritekst("Forklar nærmere hvilken dokumentasjon vi ba om, og hvorfor vi ikke kan behandle søknaden uten disse opplysningene") })
                }
            }
            paragraph {
                text(bokmal { + "Vi har ikke mottatt disse dokumentene og avslår derfor søknaden din om uføretrygd." })
            }
            paragraph {
                text(bokmal { +"Vedtaket er gjort etter folketrygdloven §§ 21-3 " +
                fritekst("Vurdere om det skal henvises til bestemmelser i kap 12, og hvis 21-7 er brukt, må du angi hvilken bokstav som er vurdert.")})
            }

            includePhrase(RettTilAKlageKort)
            includePhrase(RettTilInnsyn)
            includePhrase(HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
