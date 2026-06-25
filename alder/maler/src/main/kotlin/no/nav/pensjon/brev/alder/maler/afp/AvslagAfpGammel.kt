package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggFolketrygden
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afp.AvslagAfpGammelDto
import no.nav.pensjon.brev.alder.model.afp.selectors.avslagAfpGammelDto.pesysData.*
import no.nav.pensjon.brev.alder.model.afp.selectors.avslagAfpGammelDto.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.navEnhet.*
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — avslag på avtalefestet pensjon etter den gamle AFP-ordningen
 * (lov av 23. desember 1988 — "tilskottsloven").
 *
 * Konvertert fra Exstream-malen `PE_AF_04_010`. Brevet har kun bokmål og nynorsk
 * i originalen. Hjemmelshenvisningen (paragraf i tilskottsloven) er fritekst og
 * må fylles ut av saksbehandler.
 */
@TemplateModelHelpers
object AvslagAfpGammel : RedigerbarTemplate<AvslagAfpGammelDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_AVSLAG_GAMMEL

    override val featureToggle = FeatureToggles.avslagAfpGammel.toggle

    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag på AFP",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) - Melding om avslag" },
                nynorsk { +"Avtalefesta pensjon (AFP) - Melding om avslag" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Vi viser til din søknad av " + pesysData.kravMottattDato.format() +
                            " om avtalefestet pensjon (AFP). Søknaden din om AFP er avslått."
                    },
                    nynorsk {
                        +"Vi viser til din søknad av " + pesysData.kravMottattDato.format() +
                            " om avtalefesta pensjon (AFP). Søknaden din om AFP er avslått."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"Grunngiving for vedtaket" },
                )
            }

            // FRITEKST i originalen: «Vedtaket er gjort etter lov av 23. desember 1988 om
            // statstilskott til ordninger for avtalefestet pensjon (tilskottsloven) paragraf …»
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter lov av 23. desember 1988 om statstilskott til ordninger for " +
                                "avtalefestet pensjon (tilskottsloven) paragraf " + fritekst("…") + "."
                    },
                    nynorsk {
                        +"Vedtaket er gjort etter lov av 23. desember 1988 om statstilskot til ordningar for " +
                                "avtalefesta pensjon (tilskotslova) paragraf " + fritekst("…") + "."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Klageadgang og informasjon" },
                    nynorsk { +"Klagerett og informasjon" },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Du kan klage på avslaget. Fristen for å klage er seks uker fra du mottar dette brevet. " +
                            "Klagen skal sendes til:"
                    },
                    nynorsk {
                        +"Du kan klage på avslaget. Fristen for å klage er seks veker frå du får dette brevet. " +
                            "Klaga skal sendast til:"
                    },
                )
            }

            paragraph {
                text(
                    bokmal { +felles.avsenderEnhet.navn },
                    nynorsk { +felles.avsenderEnhet.navn },
                )
                newline()
                text(
                    bokmal { +"Postboks 6600 Etterstad" },
                    nynorsk { +"Postboks 6600 Etterstad" },
                )
                newline()
                text(
                    bokmal { +"0607 Oslo" },
                    nynorsk { +"0607 Oslo" },
                )
                newline()
                text(
                    bokmal { +"NORWAY" },
                    nynorsk { +"NORWAY" },
                )
            }

            paragraph {
                text(
                    bokmal { +"Sammen med dette brevet sender vi deg også en orientering om klage- og ankebehandling." },
                    nynorsk { +"Saman med dette brevet sender vi deg òg ei orientering om klage- og ankebehandling." },
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi gjør oppmerksom på at du etter forvaltningsloven paragraf 18 har rett til å se sakens dokumenter." },
                    nynorsk { +"Vi gjer merksam på at du etter forvaltningslova paragraf 18 har rett til å sjå saksdokumenta." },
                )
            }
            includePhrase(HarDuSpoersmaal.alder)
        }
        includeAttachment(vedleggFolketrygden)
    }
}
