// TODO: reimplement when an example template exists
import type { TEnum } from "~/types/brevbakerTypes";

export const EnumEditor = ({ spec }: { spec: TEnum }) => (
  <select>
    {spec.values.map((option) => (
      <option key={option} value={option}>
        {option}
      </option>
    ))}
  </select>
);
