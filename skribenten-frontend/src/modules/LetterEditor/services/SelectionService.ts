
export type ClickCoords = { x: number, y: number }
type NodeDistances = { node: ChildNode, distances: { rect: DOMRect, x: number, y: number }[] }

export class SelectionService {
    readonly debugLogging: boolean = false

    constructor(debugLogging?: boolean) {
        if (debugLogging !== undefined) {
            this.debugLogging = debugLogging
        }
    }

    /**
     * Attempt to focus cursor to the start of the given node.
     * @param node node to focus cursor to
     * @param focusStolen callback invoked if focus is stolen
     */
    focusStartOfNode(node: HTMLElement, focusStolen?: () => void) {
        this.warn("Attempting to focus cursor at start of: ", node)
        const sel = window.getSelection()
        if (sel !== null) {
            sel.selectAllChildren(node)
            sel.collapseToStart()
            focusStolen && focusStolen()
        } else {
            this.warn("window.getSelection() is null")
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
        this.warn("attempting to focus cursor at the end of the line nearest to click at: ", click)

        const nodeDistances = nodes.map(n => this.getTextNodeWithDistances(n, click))
            .filter((n): n is NodeDistances => n != null)

        this.warn("all distances to click:", nodeDistances)

        if (nodeDistances.length === 0) {
            this.warn("found empty nodes, will focus cursor to start of line")
            this.focusStartOfNode(nodes[0], focusStolen)
        } else {
            const nearest = this.findNodesOnTheLineVerticallyNearestClick(nodeDistances)
            if (nearest.length > 0) {
                this.warn("using nodes on the line vertically nearest the click:", nearest)
                this.focusHorizontallyNearestClick(nearest, focusStolen)
            } else {
                this.warn("couldn't find any nodes on a line vertically near to the click")
            }
        }
    }

    /**
     * Focus cursor at the given offset in the node.
     *
     * @param node the node to focus cursor to
     * @param offset the offset in the node
     */
    focusAtOffset(node: ChildNode, offset: number) {
        const range = document.createRange()
        range.setStart(node, offset)
        range.collapse()

        const sel = window.getSelection() as Selection
        if (sel !== null) {
            sel.removeAllRanges()
            sel.addRange(range)
        } else {
            this.warn("window.getSelection() is null")
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
        const nearest = this.findNearestHorizontally(nodes)
        this.warn("found horizontally nearest node:", nearest)

        const offset = this.findMaxOffsetInRect(nearest.node, nearest.rect)

        if (offset === null) {
            this.warn("couldn't find max offset in node:", nearest)
        } else {
            //TODO: should only add 1 to offset if it is the last character in the block, now it works for individual TextContent nodes
            if (offset === nearest.node.length - 1) {
                this.warn("adding 1 to offset")
                this.focusAtOffset(nearest.node, offset + 1)
            } else {
                this.focusAtOffset(nearest.node, offset)
            }
            focusStolen && focusStolen()
        }

    }

    /**
     * Find the node that has the least horizontal distance, and the corresponding inner DOMRect.
     * @param nodes the nodes with distances to find the node in
     * @private
     */
    private findNearestHorizontally(nodes: NodeDistances[]): { node: ChildNode, rect: DOMRect } {
        const nearest = nodes.map(n => ({node: n.node, distance: n.distances.reduce((prev, next) => next.x <= prev.x ? next : prev)}))
            .reduce((prev, next) => next.distance.x <= prev.distance.x ? next : prev)

        return {node: nearest.node, rect: nearest.distance.rect}
    }

    /**
     * Filters the nodes that are on the line vertically nearest the click.
     * Also filters the distances array to only contain those that are vertically nearest the clicked line.
     *
     * @param nodes the nodes to filter.
     * @private
     */
    private findNodesOnTheLineVerticallyNearestClick(nodes: NodeDistances[]): NodeDistances[] {
        const leastVerticalDistance = this.findLeastVerticalDistance(nodes)

        this.warn("nearest vertical distance to click", leastVerticalDistance)

        return nodes.map(n => ({...n, distances: n.distances.filter(d => d.y === leastVerticalDistance)}))
            .filter(n => n.distances.length > 0)
    }

    private findLeastVerticalDistance(nodes: NodeDistances[]): number {
        return nodes.flatMap(n => n.distances.map(d => d.y)).reduce((prev, next) => next < prev ? next : prev)
    }

    /**
     * Get the inner text ChildNode of the node with it's inner client rects, and the distances for those rects to the click.
     * @param node the node to use
     * @param click the click to calculate the distances to
     * @private
     */
    private getTextNodeWithDistances(node: HTMLElement, click: ClickCoords): NodeDistances | null {
        const textNode = node.firstChild
        if (textNode == null || textNode.length === 0) {
            return null
        }

        const range = document.createRange()
        range.selectNodeContents(textNode)
        const rects = range.getClientRects()

        const distances: { rect: DOMRect, x: number, y: number }[] = []
        for (let i = 0; i < rects.length; i++) {
            const rect = rects[i]
            distances.push({rect, x: Math.round(this.xDistanceFromClick(rect, click)), y: Math.round(this.yDistanceFromClick(rect, click))})
        }

        return {node: textNode, distances}
    }

    private yDistanceFromClick(rect: DOMRect, click: ClickCoords): number {
        return Math.abs(click.y - Math.max(Math.min(click.y, rect.bottom), rect.top))
    }

    private xDistanceFromClick(rect: DOMRect, click: ClickCoords): number {
        return Math.abs(click.x - Math.max(Math.min(click.x, rect.right), rect.left))
    }

    /**
     * Find the maximum offset we can use within the node and still be within the rect that is on the line nearest the click.
     * @param node the node to find the offset within
     * @param rectOnClickedLine the rect on the line nearest the click
     * @private
     */
    private findMaxOffsetInRect(node: ChildNode, rectOnClickedLine: DOMRect): number | null {
        let offset = null
        for (let i = 0; i < node.length; i++) {
            const range = document.createRange()
            range.setStart(node, i)
            range.setEnd(node, i + 1)
            if (range.getClientRects()[0].top == rectOnClickedLine.top) {
                offset = i
            }
        }
        return offset
    }

    /* eslint-disable-next-line @typescript-eslint/no-explicit-any */
    private warn(msg: string, ...values: any[]) {
        if (this.debugLogging) {
            console.log("SelectionService: " + msg, ...values)
        }
    }

}