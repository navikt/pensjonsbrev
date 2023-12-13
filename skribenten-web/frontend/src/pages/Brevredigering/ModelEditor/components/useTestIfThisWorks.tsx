import { useQuery } from "@tanstack/react-query";

import { getTemplate } from "~/api/skribenten-api-endpoints";

const TEST_TEMPLATE = "INFORMASJON_OM_SAKSBEHANDLINGSTID";

export function useTestIfThisWorks(typeName: string) {
  const letterModelSpecification = useQuery({
    queryKey: getTemplate.queryKey(TEST_TEMPLATE),
    queryFn: () => getTemplate.queryFn(TEST_TEMPLATE),
  }).data?.modelSpecification;

  return letterModelSpecification?.types[typeName];

}
