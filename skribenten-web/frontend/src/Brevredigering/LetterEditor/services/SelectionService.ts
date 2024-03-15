export type ClickCoords = { x: number; y: number };
type NodeDistances = { node: ChildNode; distances: { rect: DOMRect; x: number; y: number }[] };

export class SelectionService {
  readonly debugLogging: boolean = false;

  constructor(debugLogging?: boolean) {
    if (debugLogging !== undefined) {
      this.debugLogging = debugLogging;
    }
  }

  /**
   * Returns the current offset of the cursor, or -1 if not able to get selection or range.
   *
   */
  getCursorOffset(): number {
    const sel = window.getSelection();
    const range = sel?.getRangeAt(0);
    return range?.collapsed ? range.startOffset : -1;
  }

  /**
   * Attempt to focus cursor to the start of the given node.
   * @param node node to focus cursor to
   * @param focusStolen callback invoked if focus is stolen
   */
  focusStartOfNode(node: HTMLElement, focusStolen?: () => void) {
    this.warn("Attempting to focus cursor at start of: ", node);
    const sel = window.getSelection();
    if (sel === null) {
      this.warn("window.getSelection() is null");
    } else {
      sel.selectAllChildren(node);
      sel.collapseToStart();
      focusStolen && focusStolen();
    }
  }

  /**
   * Focus cursor at the given offset in the node.
   *
   * @param node the node to focus cursor to
   * @param offset the offset in the node
   * @param focusStolen callback to indicate focus stolen
   */
  focusAtOffset(node: ChildNode, offset: number, focusStolen?: () => void) {
    const range = document.createRange();
    range.setStart(node, offset);
    range.collapse();

    const sel = window.getSelection() as Selection;
    if (sel === null) {
      this.warn("window.getSelection() is null");
    } else {
      sel.removeAllRanges();
      sel.addRange(range);
      focusStolen && focusStolen();
    }
  }



  private yDistanceFromClick(rect: DOMRect, click: ClickCoords): number {
    return Math.abs(click.y - Math.max(Math.min(click.y, rect.bottom), rect.top));
  }

  private xDistanceFromClick(rect: DOMRect, click: ClickCoords): number {
    return Math.abs(click.x - Math.max(Math.min(click.x, rect.right), rect.left));
  }

  /**
   * Find the maximum offset we can use within the node and still be within the rect that is on the line nearest the click.
   * @param node the node to find the offset within
   * @param rectOnClickedLine the rect on the line nearest the click
   * @private
   */
  private findMaxOffsetInRect(node: ChildNode, rectOnClickedLine: DOMRect): number | null {
    let offset = null;
    // @ts-expect-error -- not sure why node has no length attribute
    for (let index = 0; index < node.length; index++) {
      const range = document.createRange();
      range.setStart(node, index);
      range.setEnd(node, index + 1);
      if (range.getClientRects()[0].top == rectOnClickedLine.top) {
        offset = index;
      }
    }
    return offset;
  }

  /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
  private warn(message: string, ...values: any[]) {
    if (this.debugLogging) {
      // eslint-disable-next-line no-console
      console.log("SelectionService: " + message, ...values);
    }
  }
}
