package no.nav.pensjon.brev.ufore.maler.svartid

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.ufore.api.model.maler.svartid.OrienteringOmSaksbehandlingstidDto
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.Brevkategori
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object OrienteringOmSaksbehandlingstidRedigerbar : RedigerbarTemplate<OrienteringOmSaksbehandlingstidDto> {

    // PE_UP_07_105
    override val kode = Ufoerebrevkoder.Redigerbar.UT_ORIENTERING_OM_SAKSBEHANDLINGSTID
    override val kategori = Brevkategori.INFORMASJONSBREV
    override val brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper = setOf(Sakstype.UFOREP)
    override val valgbareVedlegg = setOf(Ufoerebrevkoder.AlltidValgbareVedlegg.SKJEMA_FOR_BANKOPPLYSNINGER)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Orientering om saksbehandlingstid av søknad om uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
        letterDataType = OrienteringOmSaksbehandlingstidDto::class
    ) {
        val forlengetSaksbehandlingstid = saksbehandlervalg("forlengetSaksbehandlingstid", "Forlenget saksbehandlingstid").bool()

        title {
            showIf(forlengetSaksbehandlingstid) {
                text(
                    bokmal { + "Orientering om forlenget saksbehandlingstid uføretrygd" },
                    nynorsk { + "Orientering om forlenget saksbehandlingstid uføretrygd" },
                )
            }.orShow {
                text(
                    bokmal { + "Orientering om saksbehandlingstiden uføretrygd" },
                    nynorsk { + "Orientering om saksbehandlingstiden uføretrygd" },
                )
            }
        }
        outline {
            paragraph {
                showIf(forlengetSaksbehandlingstid) {
                    text(
                        bokmal { + "Vi har " + fritekst("dato") + " mottatt din søknad om uføretrygd. "
                            + "Det vil dessverre ta oss lengre tid enn antatt å behandle kravet. "
                            + "Forsinkelsen skyldes " + fritekst("årsak til forsinkelse") + "." },
                        nynorsk { + "Vi har " + fritekst("dato") + " fått søknaden din om uføretrygd. "
                            + "Det vil dessverre ta oss lengre tid enn venta å behandle kravet. "
                            + "Forsinkinga skuldast " + fritekst("årsak til forsinkelse") + "." }
                    )
                }.orShow {
                    text(
                        bokmal { + "Vi har " + fritekst("dato") + " mottatt søknaden din om uføretrygd." },
                        nynorsk { + "Vi har " + fritekst("dato") + " fått søknaden din om uføretrygd." },
                    )
                }
            }
            showIf(forlengetSaksbehandlingstid) {
                title1 {
                    text(
                        bokmal { + "Ny svartid" },
                        nynorsk { + "Ny svartid" },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Vi antar at kravet ditt kan bli ferdigbehandlet innen " + fritekst("svartid") + "." },
                        nynorsk { + "Vi reknar med at kravet ditt kan bli ferdigbehandla innan " + fritekst("svartid") + "." },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { + "Saksbehandlingstiden vår er vanligvis " + fritekst("svartid") + "." },
                        nynorsk { + "Saksbehandlingstida vår er vanlegvis " + fritekst("svartid") + "." },
                    )
                }
            }
            paragraph {
                text(
                    bokmal { + "Dersom vi trenger flere opplysninger fra deg, vil du høre fra oss." },
                    nynorsk { + "Dersom vi treng fleire opplysningar frå deg, vil du høyre frå oss." },
                )
            }
            includePhrase(OrienteringOmSaksbehandlingstidFraser.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilInnsyn)
            includePhrase(Felles.HarDuSporsmal)
        }
    }
}