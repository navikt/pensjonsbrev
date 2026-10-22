package no.nav.pensjon.brev.maler.klagebrev

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.Companion.pensjon
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.*
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder.Redigerbar.PE_KLAGE_BESLUTNING_OM_VEDTAKET_SKAL_IVERKSETTES
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.Brevkategori.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object KlageBeslutningOmVedtaketSkalIverksettesPE : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val featureToggle = FeatureToggles.brevmalKlageBeslutningOmVedtaketSkalIverksettes.toggle

    override val kode = PE_KLAGE_BESLUTNING_OM_VEDTAKET_SKAL_IVERKSETTES
    override val kategori = KLAGE_OG_ANKE
    override val brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = pensjon

    override val template = createTemplate(
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Klage - beslutning om vedtaket skal iverksettes",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        val vedtaketIverksettes = saksbehandlervalg("vedtaketIverksettes", "Vedtaket skal iverksettes").bool()
        val oekonomiskeForhold =
            saksbehandlervalg("oekonomiskeForhold", "Økonomiske forhold kan ikke vektlegges").bool()
        val tilbakekreving = saksbehandlervalg("tilbakekreving", "Hvis saken gjelder tilbakekreving, og vi gir avslag på utsatt iverksettelse").bool()

        title {
            text(bokmal { +"Vi har besluttet at vedtaket " })
            showIf(vedtaketIverksettes) {
                text(bokmal { +"skal " })
            }.orShow {
                text(bokmal { +"ikke skal " })
            }
            text(bokmal { +"iverksettes" })
        }

        outline {

            paragraph {
                text(bokmal {
                    +"Du har bedt om at vedtak datert "
                    +fritekst("dato") + " ikke iverksettes før "
                    +fritekst("klagefristen er ute / klagen er avgjort") + "."
                })
            }
            paragraph {
                text(bokmal { +"Søknaden er " })
                showIf(vedtaketIverksettes) {
                    text(bokmal { +"avslått." })
                }.orShow {
                    text(bokmal { +"innvilget." })
                }
            }

            title1 { text(bokmal { +"Rettslig grunnlag" }) }
            paragraph {
                text(bokmal {
                    +"En klage har som hovedregel ikke oppsettende virkning. "
                    +"Dette innebærer at vedtaket skal gjennomføres, selv om du klager på det."
                })
            }
            paragraph {
                text(bokmal {
                    +"Vedtaksorganet kan likevel beslutte at vedtaket ikke skal iverksettes før klagefristen er ute, "
                    +"eller klagen er avgjort. Det følger av forvaltningsloven § 42."
                })
            }
            paragraph {
                text(bokmal {
                    +"I vurderingen av om vedtaket likevel skal gis oppsettende virkning, "
                    +"skal det legges vekt på hva som antas å bli utfallet av klagebehandlingen. "
                })
                showIf(oekonomiskeForhold) {
                    text(bokmal {
                        +"Økonomiske forhold kan som hovedregel ikke vektlegges i vurderingen av om vedtaket skal gis oppsettende virkning."
                    })
                }
            }

            title1 { text(bokmal { +"Vurdering og konklusjon" }) }
            paragraph {
                text(bokmal {
                    +fritekst(
                        "Skriv en konkret begrunnelse med utgangspunkt i rettslig grunnlag. "
                                + "Aktuelle momenter: hva klagen er begrunnet med, "
                                + "hvor sannsynlig det er at klagen vil medføre omgjøring av det påklagede vedtaket m.m."
                    )
                })
            }
            paragraph {
                text(bokmal { +"Vi mener at det " })
                showIf(vedtaketIverksettes) {
                    text(bokmal {
                        +"ikke er tilstrekkelig grunnlag "
                    })
                }.orShow {
                    text(bokmal { +"er grunnlag " })
                }
                text(bokmal { +"for å utsette iverksettelse av vedtaket vårt av "
                    +fritekst("dato") + " fram til klagen er endelig avgjort." })
            }
            showIf(tilbakekreving) {
                paragraph {
                    text(bokmal {
                        +"Du kan be Skatteetaten om en nedbetalingsavtale hvis det er vanskelig for deg å betale tilbake det feilutbetalte beløpet samlet."
                    })
                }
            }

            title1{ text(bokmal { +"Prosessuell beslutning uten klagerett" }) }
            paragraph {
                text(bokmal {
                    +"Denne avgjørelsen er ikke et vedtak som gjelder individuelle rettigheter eller plikter (enkeltvedtak), "
                    +"men en prosessuell beslutning. Du kan ikke klage på denne beslutningen. "
                    +"Det følger av forvaltningsloven §§ 2 og 28."
                })
            }

            includePhrase(Felles.RettTilInnsynRedigerbarebrev)
            includePhrase(Felles.HarDuSpoersmaal.alder)
        }
    }
}