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
    const sel = window.getSelection() as Selection;
    const range = sel.getRangeAt(0);
    return range != null && range.collapsed ? range.startOffset : -1;
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
   * Attempt to focus cursor at the end of the line nearest to the click.
   *
   * @param nodes the nodes to find point of focus in
   * @param click the coordinates of the click
   * @param focusStolen callback invoked if focus is stolen
   */
  focusEndOfClickedLine(nodes: HTMLElement[], click: ClickCoords, focusStolen?: () => void) {
    this.warn("attempting to focus cursor at the end of the line nearest to click at: ", click);

    const nodeDistances = nodes
      .map((n) => this.getTextNodeWithDistances(n, click))
      .filter((n): n is NodeDistances => n != null);

    this.warn("all distances to click:", nodeDistances);

    if (nodeDistances.length === 0) {
      this.warn("found empty nodes, will focus cursor to start of line");
      this.focusStartOfNode(nodes[0], focusStolen);
    } else {
      const nearest = this.findNodesOnTheLineVerticallyNearestClick(nodeDistances);
      if (nearest.length > 0) {
        this.warn("using nodes on the line vertically nearest the click:", nearest);
        this.focusHorizontallyNearestClick(nearest, focusStolen);
      } else {
        this.warn("couldn't find any nodes on a line vertically near to the click");
      }
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

  /**
   * Focus cursor to the node and offset horizontally nearest the click.
   *
   * @param nodes the nodes on the line vertically nearest the click
   * @param focusStolen callback to indicate focus stolen
   * @private
   */
  private focusHorizontallyNearestClick(nodes: NodeDistances[], focusStolen?: () => void) {
    const nearest = this.findNearestHorizontally(nodes);
    this.warn("found horizontally nearest node:", nearest);

    const offset = this.findMaxOffsetInRect(nearest.node, nearest.rect);

    if (offset === null) {
      this.warn("couldn't find max offset in node:", nearest);
    } else {
      //should only add 1 to offset if it is the last character in the block
      // @ts-expect-error -- not sure why node has no length attribute
      if (offset === nearest.node.length - 1) {
        this.warn("adding 1 to offset");
        this.focusAtOffset(nearest.node, offset + 1, focusStolen);
      } else {
        this.focusAtOffset(nearest.node, offset, focusStolen);
      }
    }
  }

  /**
   * Find the node that has the least horizontal distance, and the corresponding inner DOMRect.
   * @param nodes the nodes with distances to find the node in
   * @private
   */
  private findNearestHorizontally(nodes: NodeDistances[]): { node: ChildNode; rect: DOMRect } {
    const nearest = nodes
      .map((n) => ({
        node: n.node,
        // eslint-disable-next-line unicorn/no-array-reduce
        distance: n.distances.reduce((previous, next) => (next.x <= previous.x ? next : previous)),
      }))
      // eslint-disable-next-line unicorn/no-array-reduce
      .reduce((previous, next) => (next.distance.x <= previous.distance.x ? next : previous));

    return { node: nearest.node, rect: nearest.distance.rect };
  }

  /**
   * Filters the nodes that are on the line vertically nearest the click.
   * Also filters the distances array to only contain those that are vertically nearest the clicked line.
   *
   * @param nodes the nodes to filter.
   * @private
   */
  private findNodesOnTheLineVerticallyNearestClick(nodes: NodeDistances[]): NodeDistances[] {
    const leastVerticalDistance = this.findLeastVerticalDistance(nodes);

    this.warn("nearest vertical distance to click", leastVerticalDistance);

    return nodes
      .map((n) => ({ ...n, distances: n.distances.filter((d) => d.y === leastVerticalDistance) }))
      .filter((n) => n.distances.length > 0);
  }

  private findLeastVerticalDistance(nodes: NodeDistances[]): number {
    return (
      nodes
        .flatMap((n) => n.distances.map((d) => d.y))
        // eslint-disable-next-line unicorn/no-array-reduce -- refactor to min function?
        .reduce((previous, next) => (next < previous ? next : previous))
    );
  }

  /**
   * Get the inner text ChildNode of the node with it's inner client rects, and the distances for those rects to the click.
   * @param node the node to use
   * @param click the click to calculate the distances to
   * @private
   */
  private getTextNodeWithDistances(node: HTMLElement, click: ClickCoords): NodeDistances | null {
    const textNode = node.firstChild;
    // @ts-expect-error -- not sure why node has no length attribute
    if (textNode == null || textNode.length === 0) {
      return null;
    }

    const range = document.createRange();
    range.selectNodeContents(textNode);
    const rects = range.getClientRects();

    const distances: { rect: DOMRect; x: number; y: number }[] = [];
    for (const rect of rects) {
      distances.push({
        rect,
        x: Math.round(this.xDistanceFromClick(rect, click)),
        y: Math.round(this.yDistanceFromClick(rect, click)),
      });
    }

    return { node: textNode, distances };
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
