import AdresseVisning, { type AdresseVisningTag } from "~/components/AdresseVisning";
import HentOgVisAdresse from "~/components/HentOgVisAdresse";
import { useLandData } from "~/hooks/useLandData";
import { type Mottaker } from "~/types/brev";
import { type Nullable } from "~/types/Nullable";
import { MOTTAKER_TAG_BRUKER, MOTTAKER_TAG_VERGE } from "~/utils/AdresseUtils";
import { getCountryNameByKode } from "~/utils/countryUtils";

function mapMottakerTags(mottaker: Mottaker): AdresseVisningTag[] {
  if (mottaker.type === "NorskAdresse" || mottaker.type === "UtenlandskAdresse") {
    if (mottaker.manueltAdressertTil === "BRUKER") {
      return [MOTTAKER_TAG_BRUKER];
    }
    if (mottaker.manueltAdressertTil === "ANNEN") {
      return [MOTTAKER_TAG_VERGE];
    }
  }
  return [];
}

const OppsummeringAvMottaker = (props: { saksId: string; mottaker: Nullable<Mottaker>; withTitle: boolean }) => {
  const { data: landData } = useLandData();

  if (props.mottaker === null || props.mottaker.type === "Samhandler") {
    return (
      <HentOgVisAdresse
        sakId={props.saksId}
        samhandlerId={props.mottaker?.type === "Samhandler" ? props.mottaker.tssId : undefined}
        showMottakerTitle={props.withTitle}
      />
    );
  }

  const adresselinjer: string[] = [];
  if (props.mottaker.adresselinje1) adresselinjer.push(props.mottaker.adresselinje1);
  if (props.mottaker.adresselinje2) adresselinjer.push(props.mottaker.adresselinje2);
  if (props.mottaker.adresselinje3) adresselinjer.push(props.mottaker.adresselinje3);

  if (props.mottaker.type === "NorskAdresse") {
    const postLinje = [props.mottaker.postnummer, props.mottaker.poststed].filter(Boolean).join(" ");
    if (postLinje) adresselinjer.push(postLinje);
  }

  if (props.mottaker.type === "UtenlandskAdresse") {
    const landNavn = getCountryNameByKode(props.mottaker.landkode, landData || []);
    if (landNavn) adresselinjer.push(landNavn);
  }

  return (
    <AdresseVisning
      adresselinjer={adresselinjer}
      navn={props.mottaker.navn}
      tags={mapMottakerTags(props.mottaker)}
      withTitle={props.withTitle}
    />
  );
};

export default OppsummeringAvMottaker;
