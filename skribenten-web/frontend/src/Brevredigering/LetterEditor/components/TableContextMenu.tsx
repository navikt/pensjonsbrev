import { css } from "@emotion/react";
import { ActionMenu } from "@navikt/ds-react";
import { useEffect, useRef } from "react";

type Props = {
  anchor: { x: number; y: number } | null;
  onClose: () => void;
  children: React.ReactNode;
};

/**
 * A right-click ActionMenu positioned at absolute screen coordinates.
 * Uses an invisible trigger <button> so ActionMenu’s focus & a11y logic work.
 */
export default function TableContextMenu({ anchor, onClose, children }: Props) {
  const triggerRef = useRef<HTMLButtonElement>(null);
  const open = Boolean(anchor);

  const menuSize = css`
    width: 260px;
    min-width: 260px;
  `;

  /* focus the hidden trigger whenever we open (for keyboard navigation) */
  useEffect(() => {
    if (open && triggerRef.current) triggerRef.current.focus();
  }, [open]);

  const triggerStyle = anchor
    ? css({
        position: "fixed",
        top: anchor.y,
        left: anchor.x,
        width: 0,
        height: 0,
        padding: 0,
        border: 0,
        background: "transparent",
      })
    : css({ display: "none" });

  return (
    <ActionMenu
      onOpenChange={(isOpen) => {
        if (!isOpen) onClose();
      }}
      open={open}
    >
      <ActionMenu.Trigger>
        <button aria-hidden css={triggerStyle} ref={triggerRef} />
      </ActionMenu.Trigger>

      <ActionMenu.Content align="start" css={menuSize}>
        {children}
      </ActionMenu.Content>
    </ActionMenu>
  );
}
