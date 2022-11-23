export function updateByIndex<T>(arr: T[], index: number, map: Updater<T>): T[] {
    return arr.map((e, i) => {
        if (index === i) {
            return map(e)
        } else {
            return e
        }
    })
}

export function updateWithPartial<T>(partial: Partial<T>, orig: T): T {
    return {...orig, ...partial}
}

export type Mapper<T, R> = (t: T) => R
export type Updater<T> = Mapper<T, T>
