package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDtoSelectors.SaksbehandlerValgSelectors.folketrygdlovenParagraf
import no.nav.pensjon.brev.api.model.maler.redigerbar.AvslagGjenlevendepensjonDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.generated.TBU2212_Generated
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

//PE_GP_04_010 Vedtak - avslag av gjenlevendepensjon

@TemplateModelHelpers
object AvslagGjenlevendepensjon : RedigerbarTemplate<AvslagGjenlevendepensjonDto> {

override val featureToggle = FeatureToggles.brevmalAvslagGjenlevendepensjon.toggle

    override val kode = Pesysbrevkoder.Redigerbar.GP_AVSLAG_GJENLEVENDEPENSJON
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.GJENLEV)

    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag av gjenlevendepensjon",
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
                    bokmal { +"Nav viser til søknaden din om gjenlevendepensjon mottatt PE_Kravdata_Kravhode_KravMotattDato. Søknaden din er avslått." },
                    nynorsk { +"Nav viser til søknaden din om etterlatnepensjon motteken PE_Kravdata_Kravhode_KravMotattDato. Søknaden din er avslått." },
                    english { +"Nav makes reference to your application for a survivor's pension, received on PE_Kravdata_Kravhode_KravMotattDato. Your application has been denied." }
                )
            }

            showIf(saksbehandlerValg.folketrygdlovenParagraf.isOneOf(AvslagGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenParagraf.paragraf17_2_foersteEllerTredje_ledd)) {
                paragraph {
                    text(
                        bokmal {
                            +"I folketrygdloven paragraf 17-2 er det angitt hvilke personer som kan få rett til ytelser til gjenlevende ektefelle. "
                            +"Du tilhører ikke denne gruppen fordi <FRITEKST: Angi grunn>."
                        },
                        nynorsk {
                            +"I folketrygdlova paragraf 17-2 er det fastsett kven som kan få rett til ytingar til attlevande ektefelle. "
                            +"Du høyrer ikkje til denne gruppa fordi <FRITEKST: Angi grunn>."
                        },
                        english {
                            +"The National Insurance Act paragraph 17-2 specifies who may have the right to receive benefit as a surviving spouse. "
                            +"You are not entitled to survivor’s benefit in accordance with this provision because <FRITEKST: Angi grunn>."
                        }
                    )
                }
            }.orShowIf(saksbehandlerValg.folketrygdlovenParagraf.isOneOf(AvslagGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenParagraf.paragraf17_2_andre_ledd)) {
                paragraph {
                    text(
                        bokmal {
                            +"Etter folketrygdloven paragraf 17-2 andre ledd kan det gis gjenlevendeytelser dersom ektefellen er forsvunnet og det er avsagt kjennelse eller dom om at vedkommende formodes å være død. "
                            +"Du har ikke rett til gjenlevendepensjon etter denne bestemmelsen fordi <FRITEKST: Angi grunn>."
                        },
                        nynorsk {
                            +"Etter folketrygdlova paragraf 17-2 andre ledd kan det givast ytingar til attlevande dersom ektefellen er forsvunnen, og det er avsagt kjennelse eller dom om at vedkomande er rekna som død. "
                            +"Du har ikkje rett til attlevandepensjon etter denne føresegna fordi <FRITEKST: Angi grunn>."
                        },
                        english {
                            +"According to the National Insurance Act paragraph 17-2 second sub-section, survivor's benefit may be awarded if the spouse can not be found or a ruling or judgment has been made that the spouse is assumed dead. "
                            +"You are not entitled to survivor's benefit in accordance with this provision because <FRITEKST: Angi grunn>."
                        }
                    )
                }
            }.orShowIf(saksbehandlerValg.folketrygdlovenParagraf.isOneOf(AvslagGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenParagraf.paragraf17_3)) {
                paragraph {
                    text(
                        bokmal {
                            +"Etter hovedregelen i folketrygdloven paragraf 17-3 er det et vilkår for rett til gjenlevendepensjon at avdøde de siste <FRITEKST: tre/fem> årene fram til dødsfallet var medlem i folketrygden. "
                            +"Vi har lagt til grunn at avdøde de siste <FRITEKST: tre/fem> årene hadde følgende medlemsperioder: <FRITEKST: FOM - TOM>. "
                            +"Vilkåret om medlemskap før dødsfallet er derfor ikke oppfylt, og det er heller ikke unntaksregler som gir deg rett til gjenlevendepensjon."
                        },
                        nynorsk {
                            +"Etter hovudregelen i folketrygdlova paragraf 17‑3 er det eit vilkår for rett til attlevandepensjon at den avdøde dei siste <FRITEKST: tre/fem> åra fram til dødsfallet var medlem i folketrygda. "
                            +"Vi har lagt til grunn at den avdøde dei siste <FRITEKST: tre/fem> åra hadde følgjande medlemsperiodar: <FRITEKST: FOM – TOM>. "
                            +"Vilkåret om medlemskap før dødsfallet er difor ikkje oppfylt, og det finst heller ikkje unntaksreglar som gir deg rett til attlevandepensjon."
                        },
                        english {
                            +"The main provision of paragraph 17-3 of the National Insurance Act states that it is a requirement for a survivor's pension that the deceased had been a member of the National Insurance Scheme during the <FRITEKST: three/five> years prior to death. "
                            +"We have based our decision on our finding that the deceased in the <FRITEKST: three/five> years prior to death was a member of the National Insurance Scheme for the following period(s): <FRITEKST: FOM - TOM>. "
                            +"Thus the National Insurance Scheme's requirement that the deceased must have been a member for <FRITEKST: three/five> years prior to death has not been met, and there are no exemption rules that entitle you to a survivor's benefit."
                        }
                    )
                }
            }.orShowIf(saksbehandlerValg.folketrygdlovenParagraf.isOneOf(AvslagGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenParagraf.paragraf17_5)) {
                paragraph {
                    text(
                        bokmal {
                            +"Etter folketrygdloven paragraf 17-5 er det et vilkår for rett til pensjon at ekteskapet har vart i minst fem år, eller at ektefellene har eller har hatt felles barn. "
                            +"Gjenlevende ektefelle kan også få pensjon hvis han/hun hadde omsorg for avdødes barn ved dødsfallet, og ekteskapet og omsorgen for barna til sammen da hadde vart i minst fem år. "
                            +"Du fyller ikke disse vilkårene fordi <FRITEKST: Angi grunn>. Vi har også vurdert din rett til overgangsstønad til gjenlevende ektefelle etter folketrygdloven paragraf 17-6. "
                            +"Du fyller ikke rett til overgangsstønad fordi <FRITEKST: Angi grunn>."
                        },
                        nynorsk {
                            +"Etter folketrygdlova paragraf 17-5 er det eit vilkår for rett til pensjon at ekteskapet har vart i minst fem år, eller at ektefellane har eller har hatt felles barn. "
                            +"Gjenlevande ektefelle kan òg få pensjon dersom han/ho hadde omsorg for avdøde sine barn ved dødsfallet, og ekteskapet og omsorga for barna til saman då hadde vart i minst fem år. "
                            +"Du oppfyller ikkje desse vilkåra fordi <FRITEKST: Angi grunn>. "
                            +"Vi har òg vurdert retten din til overgangsstønad til gjenlevande ektefelle etter folketrygdlova paragraf 17-6. "
                            +"Du har ikkje rett til overgangsstønad fordi <FRITEKST: Angi grunn>."
                        },
                        english {
                            +"According to the National Insurance Act paragraph 17-5, it is a requirement for a right to a pension that the marriage has lasted for at least five years or that the spouses have or have had children together. "
                            +"The surviving spouse may also be entitled to a pension if he/she cared for the deceased's children at the time of death, and that the marriage and the care for the children in combination had lasted at least five years. "
                            +"You do not meet these requirements because <FRITEKST: Angi grunn>. "
                            +"We have also assessed your right to transitional benefit as a surviving spouse in accordance with the National Insurance Act paragraph 17-6. "
                            +"You do not meet the requirements for transitional benefit because <FRITEKST: Angi grunn>"
                        }
                    )
                }
            }.orShowIf(saksbehandlerValg.folketrygdlovenParagraf.isOneOf(AvslagGjenlevendepensjonDto.SaksbehandlerValg.FolketrygdlovenParagraf.paragraf17_10)) {
                paragraph {
                    text(
                        bokmal {
                            +"Etter folketrygdloven paragraf 17-10 kan det gis ytelser til gjenlevende skilt person når vedkommende ikke har giftet seg igjen og den tidligere ektefellen døde innen fem år etter skilsmissen. "
                            +"Det er også et vilkår at ekteskapet varte minst 25 år, eller 15 år hvis ektefellene hadde barn sammen. "
                            +"Dersom den skilte personen helt eller delvis var forsørget av bidrag fra avdøde, kan det gis ytelser selv om dødsfallet skjedde mer enn fem år etter skilsmissen. "
                            +"Du fyller ikke disse vilkårene fordi <FRITEKST: Angi grunn>."
                        },
                        nynorsk {
                            +"Etter folketrygdlova paragraf 17-10 kan det givast ytingar til gjenlevande skild person når vedkomande ikkje har gifta seg igjen, og den tidlegare ektefellen døydde innan fem år etter skilsmissa. "
                            +"Det er òg eit vilkår at ekteskapet varte i minst 25 år, eller 15 år dersom ektefellane hadde barn saman. "
                            +"Dersom den skilde personen heilt eller delvis var forsørgd av bidrag frå avdøde, kan det givast ytingar sjølv om dødsfallet skjedde meir enn fem år etter skilsmissa. "
                            +"Du oppfyller ikkje desse vilkåra fordi <FRITEKST: Angi grunn>."
                        },
                        english {
                            +"According to paragraph 17-10 of the National Insurance Act, benefits may be given to a surviving divorced person if the person has not remarried and the previous spouse died within five years of the divorce. "
                            +"It is also a requirement that the marriage lasted at least 25 years, or 15 years if the couple had children in common. "
                            +"If the divorced person was partly or entirely supported by contributions from the deceased, benefits may be given even if the death occurred more than five years after the divorce. "
                            +"You do not meet these requirements because <FRITEKST: Angi grunn>."
                        }
                    )
                }
            }

            includePhrase(TBU2212_Generated(vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon))
            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon))
        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageGjenlevendepensjon)
    }
}