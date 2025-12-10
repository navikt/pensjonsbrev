export function Divider() {
  return (
    <div
      css={{
        width: "100%",
        height: "1px",
        background: "var(--ax-neutral-400A)",
      }}
    />
  );
}

export const VerticalDivider = () => {
  return (
    <div
      css={{
        width: "1px",
        background: "var(--ax-neutral-400A)",
        alignSelf: "stretch",
      }}
    />
  );
};
