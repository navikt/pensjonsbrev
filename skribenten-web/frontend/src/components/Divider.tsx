export function Divider() {
  return (
    <div
      css={{
        width: "100%",
        height: "1px",
        background: "var(--ax-border-neutral-subtle)",
      }}
    />
  );
}

export const VerticalDivider = () => {
  return (
    <div
      css={{
        width: "1px",
        background: "var(--ax-border-neutral-subtle)",
        alignSelf: "stretch",
      }}
    />
  );
};
