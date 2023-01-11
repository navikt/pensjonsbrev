export class SelectionService {
    readonly debugLogging: boolean = false

    constructor(debugLogging?: boolean) {
        if (debugLogging !== undefined) {
            this.debugLogging = debugLogging
        }
    }

    focusStartOfNode(node: HTMLElement, focusStolen?: () => void) {
        this.warn("Attempting to steal focus at start of: ", node)
        const sel = window.getSelection()
        if (sel !== null) {
            sel.selectAllChildren(node)
            sel.collapseToStart()
            focusStolen && focusStolen()
        } else {
            this.warn("SelectionService: window.getSelection() is null")
        }
    }

    focusEndOfClickedLine(node: HTMLElement, clickedClientY: number, focusStolen?: () => void) {
        this.warn("Attempting to steal focus at end of clicked line in: ", node)

        const textNode: ChildNode | null = node.firstChild
        if (textNode === null || textNode.length === 0) {
            this.focusStartOfNode(node, focusStolen)
            return
        }

        const rectOnClickedLine = this.findRectOnClickedLine(textNode, clickedClientY)
        if (rectOnClickedLine === null) {
            this.warn(`SelectionService: couldn't find rect on clicked line at ${clickedClientY}`)
            return
        }

        const offset = this.findMaxOffsetInRect(textNode, rectOnClickedLine)
        if (offset === null) {
            this.warn("SelectionService: couldn't find max offset in node for rect on clicked line", node, rectOnClickedLine)
            return
        }

        this.focusAtOffset(textNode, offset)
        focusStolen && focusStolen()
    }

    findClosestNodeId(nodes: { [nodeId: number]: HTMLElement }, clickedCoords: { x: number, y: number }): number {
        const [closestNodeId] = Object.entries(nodes).reduce((previousValue, currentValue) => {
            const currentRect = currentValue[1].getBoundingClientRect()
            if (currentRect.top <= clickedCoords.y && currentRect.bottom >= clickedCoords.y) {
                if (previousValue == null) {
                    return currentValue
                } else if (previousValue[1].getBoundingClientRect().right < currentRect.right) {
                    return currentValue
                }
            }
            return previousValue
        })

        return Number(closestNodeId)
    }

    private focusAtOffset(node: ChildNode, offset: number) {
        const range = document.createRange()
        range.setStart(node, offset + 1)
        range.collapse()

        const sel = window.getSelection() as Selection
        if (sel !== null) {
            sel.removeAllRanges()
            sel.addRange(range)
        } else {
            this.warn("SelectionService: window.getSelection() is null")
        }
    }

    private findRectOnClickedLine(node: ChildNode, clickedClientY: number): DOMRect | null {
        const range = document.createRange()
        range.selectNodeContents(node)

        const rects = range.getClientRects()
        for (let i = 0; i < rects.length; i++) {
            if (clickedClientY >= rects[i].top && clickedClientY <= rects[i].bottom) {
                return rects[i]
            }
        }
        return null
    }

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
            console.log(msg, ...values)
        }
    }

}