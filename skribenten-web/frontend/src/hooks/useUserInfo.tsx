import { useQuery } from "@tanstack/react-query";

import type { UserInfo } from "~/api/bff-endpoints";
import { getUserInfo } from "~/api/bff-endpoints";

export function useUserInfo() {
  const getUserInfoQuery = useQuery<UserInfo>(getUserInfo);

  return getUserInfoQuery.data;
}
