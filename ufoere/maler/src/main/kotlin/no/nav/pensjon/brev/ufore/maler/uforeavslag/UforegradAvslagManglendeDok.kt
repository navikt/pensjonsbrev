package no.nav.pensjon.brev.ufore.maler.uforeavslag

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_AVSLAG_OKT_GRAD_MANGLENDE_DOK
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDto
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.SaksbehandlervalgSelectors.VisVurderingFraVilkarvedtak
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.kravMottattDato
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.UforeAvslagPendataSelectors.vurdering
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.pesysData
import no.nav.pensjon.brev.ufore.api.model.maler.redigerbar.UforeAvslagEnkelDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK

@TemplateModelHelpers
object UforegradAvslagManglendeDok : RedigerbarTemplate<UforeAvslagEnkelDto> {

    override val kode = UT_AVSLAG_OKT_GRAD_MANGLENDE_DOK
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Avslag uføretrygd - 21-3",
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        ),
    )
    {
        title {
            text (bokmal { + "Nav har avslått søknaden din om økt uføregrad"},
                nynorsk { + "Nav har avslått søknaden din om auka uføregrad"})
        }
        outline {
            paragraph {
                text(bokmal { +"Vi har avslått søknaden din om økt uføregrad som vi fikk den " + pesysData.kravMottattDato.format() + "." },
                    nynorsk { +"Vi har avslått søknaden din om auka uføregrad som vi fekk den " + pesysData.kravMottattDato.format() + "." })
            }
            title1 {
                text(bokmal { +"Derfor får du ikke økt uføregrad" },
                    nynorsk { +"Derfor får du ikkje auka uføregrad" })
            }
            paragraph {
                text(bokmal { +"Vi har ikke fått de opplysningene som er nødvendige for å vurdere saken din. " +
                        "I vårt brev av " + fritekst("dato") + " varslet vi deg om at søknaden din ville bli avslått dersom vi ikke fikk opplysningene innen fristen." },
                    nynorsk { +"Vi har ikkje fått dei opplysningane som er nødvendige for å vurdere saka di. " +
                            "I brevet vårt av " + fritekst("dato") + " varsla vi deg om at søknaden din ville bli avslått dersom vi ikkje fekk opplysningane innan fristen." })
            }

            showIf(saksbehandlerValg.VisVurderingFraVilkarvedtak) {
                paragraph {
                    text(bokmal { +pesysData.vurdering },
                        nynorsk { +pesysData.vurdering })
                }
            }
            paragraph {
                text(bokmal { + fritekst("Individuell vurdering") },
                    nynorsk { + fritekst("Individuell vurdering") })
            }

            paragraph {
                text(bokmal { + "Vi har ikke fått disse dokumentene og avslår derfor søknaden din om økt uføregrad." },
                    nynorsk { + "Vi har ikkje fått desse dokumenta og avslår derfor søknaden din om auka uføregrad." })
            }
            paragraph {
                text(bokmal { +"Vedtaket har vi gjort etter folketrygdloven § 21-3 " +
                fritekst("Vurdere om det skal henvises til bestemmelser i kap 12, og hvis 21-7 er brukt, må du angi hvilken bokstav som er vurdert.")},
                    nynorsk { +"Vedtaket har vi gjort etter folketrygdlova § 21-3 " +
                fritekst("Vurdere om det skal henvises til bestemmelser i kap 12, og hvis 21-7 er brukt, må du angi hvilken bokstav som er vurdert.")})
            }

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}
