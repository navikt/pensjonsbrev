package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonUtlandDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonUtlandDtoSelectors.PesysDataSelectors.kravMottattDato
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonUtlandDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.generated.TBU2212_Generated
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_GP_04_033 Vedtak avslag av gjenlevendepensjon utland
//Brevgruppe 3


@TemplateModelHelpers
object AvslagGjenlevendepensjonUtland : RedigerbarTemplate<AvslagGjenlevendepensjonUtlandDto> {

    override val featureToggle = FeatureToggles.brevmalAvslagGjenlevendepensjonUtland.toggle

    override val kode = Pesysbrevkoder.Redigerbar.GP_AVSLAG_GJENLEVENDEPENSJON_UTLAND
    override val kategori = Brevkategori.FOERSTEGANGSBEHANDLING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.GJENLEV)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag ytelse til gjenlevende (bosatt utland)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Gjenlevendepensjon - melding om vedtak" },
                nynorsk { +"Gjenlevendepensjon - melding om vedtak" },
                english { +"Survivor's pension - notification of decision" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { +"Nav viser til din søknad om gjenlevendepensjon mottatt " + pesysData.kravMottattDato.format() + ". Søknaden din er avslått." },
                    nynorsk { +"Nav viser til søknaden din om attlevandepensjon motteken " + pesysData.kravMottattDato.format() + ". Søknaden din er avslått." },
                    english { +"Nav refers to your application for a survivor's pension, received on " + pesysData.kravMottattDato.format() + ". Your application has been denied." }
                )
            }
            paragraph {
                text(
                    bokmal { +"FRITEKST: Endre avslagsbegrunnelsen nedenfor dersom avdøde mottok pensjon eller tidsbegrenset uførestønad ved dødsfallet men likevel ikke fyller inngangsvilkårene i § 17-3." },
                    nynorsk { +"FRITEKST: Endre avslagsbegrunnelsen nedenfor dersom avdøde mottok pensjon eller tidsbegrenset uførestønad ved dødsfallet men likevel ikke fyller inngangsvilkårene i § 17-3." },
                    english { +"FRITEKST: Endre avslagsbegrunnelsen nedenfor dersom avdøde mottok pensjon eller tidsbegrenset uførestønad ved dødsfallet men likevel ikke fyller inngangsvilkårene i § 17-3." }
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Et vilkår for å ha rett til ytelser som gjenlevende er at avdøde de siste "
                        + fritekst("tre/fem") + " årene før dødsfallet har vært medlem i folketrygden eller mottatt pensjon fra folketrygden. Vi har lagt til grunn at avdøde de siste "
                        + fritekst("tre/fem") + " årene før dødsfallet hadde følgende medlemsperioder i folketrygden: "
                        + fritekst("FOM - TOM") + ". Vilkåret i folketrygdloven om medlemskap fram til dødsfallet er derfor ikke oppfylt."
                    },
                    nynorsk {
                        +"Eit vilkår for å ha rett til ytingar som attlevande er at den avdøde dei siste "
                        + fritekst("tre/fem") + " åra før dødsfallet har vore medlem i folketrygda eller har motteke pensjon frå folketrygda. Vi har lagt til grunn at den avdøde dei siste "
                        + fritekst("tre/fem") + " åra før dødsfallet hadde følgjande medlemsperiodar i folketrygda: "
                        + fritekst("FOM - TOM") + ". Vilkåret i folketrygdlova om medlemskap fram til dødsfallet er derfor ikkje oppfylt."
                    },
                    english {
                        +"In order to receive benefits as a survivor, it is a requirement that the deceased was a member of the National Insurance Scheme or received a pension from the National Insurance Scheme in the "
                        + fritekst("three/five") + " years prior to death. We have based our decision on our finding that the deceased in the "
                        + fritekst("three/five") + " years prior to death was a member of the National Insurance Scheme in the following periods: "
                        + fritekst("FOM - TOM") + ". Thus the requirement in the National Insurance Act that the deceased must have been a member for "
                        + fritekst("three/five") + " years prior to death has not been met."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Det fremgår av søknaden din at avdøde har bodd og arbeidet i et "
                        + fritekst("annet EØS-land/avtaleland") + ". Vilkåret om "
                        + fritekst("tre/fem") + " års medlemskap kan oppfylles gjennom medregning av perioder med medlemskap i "
                        + fritekst("andre EØS-land/avtaleland") + "."
                    },
                    nynorsk {
                        +"Det går fram av søknaden din at den avdøde har budd og arbeidd i eit "
                        + fritekst("anna EØS-land/avtaleland") + ". Vilkåret om "
                        + fritekst("tre/fem") + " års medlemskap kan oppfyllast gjennom medrekning av periodar med medlemskap i "
                        + fritekst("andre EØS-land/avtaleland") + "."
                    },
                    english {
                        +"Your application shows that the deceased has lived and worked in "
                        + fritekst("another EEA country/another country with which Norway has a social security agreement") + ". The requirement that the deceased must have been a member of the National Insurance Scheme for "
                        + fritekst("three/five") + " years prior to death can be fulfilled by including periods of membership in "
                        + fritekst("other EEA countries/countries with which Norway has a social security agreement") + "."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Vi har fått opplyst at avdøde har vært medlem i trygden i "
                        + fritekst("Fyll inn aktuelt land") + " i tidsrommet "
                        + fritekst("FOM - TOM") + ". Vilkåret om minst "
                        + fritekst("tre/fem") + " års medlemskap fram til dødsfallet kan derfor heller ikke oppfylles gjennom medregning av medlemsperioder i "
                        + fritekst("Fyll inn aktuelt land") + "."
                    },
                    nynorsk {
                        +"Vi har fått opplyst at den avdøde har vore medlem i trygda i "
                        + fritekst("Fyll inn aktuelt land") + " i tidsrommet "
                        + fritekst("FOM - TOM") + ". Vilkåret om minst "
                        + fritekst("tre/fem") + " års medlemskap fram til dødsfallet kan derfor heller ikkje oppfyllast gjennom medrekning av medlemsperiodar i "
                        + fritekst("Fyll inn aktuelt land") + "."
                    },
                    english {
                        +"We have been informed that the deceased has been a member of the social security scheme in "
                        + fritekst("Fyll inn aktuelt land") + " in the period between "
                        + fritekst("FOM - TOM") + ". The requirement that the deceased must have been a member of the National Insurance Scheme for at least "
                        + fritekst("three/five") + " years prior to death is therefore not met by including the deceased's period of membership in "
                        + fritekst("Fyll inn aktuelt land") + "."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 17 og "
                        + fritekst("EØS-avtalens bestemmelser om trygd/trygdeavtalen mellom Norge og <aktuelt land>") + "."
                    },
                    nynorsk {
                        +"Vedtaket er gjort etter føresegner i folketrygdlova kapittel 17 og "
                        + fritekst("EØS-avtala sine føresegner om trygd/trygdeavtalen mellom Noreg og <aktuelt land>") + "."
                    },
                    english {
                        +"This decision has been made in accordance with Chapter 17 of the National Insurance Act and "
                        + fritekst("the EEA agreement on social security/the social security agreement between Norway and <aktuelt land>") + "."
                    },
                )
            }
            includePhrase(TBU2212_Generated(vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon))
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon))
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon)
    }
}