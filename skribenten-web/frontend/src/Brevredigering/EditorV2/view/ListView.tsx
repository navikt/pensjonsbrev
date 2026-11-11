import { Fragment } from "react";

import { type ListBlockV2 } from "~/types/brevV2";

import { DeletedItemsAtV2 } from "../diff/DeletedMarkupV2";
import { TextContainerView } from "./text/TextContainerView";

export function ListView({ list, block }: { list: ListBlockV2; block: number }) {
  const Tag = list.type === "NUMBERED_LIST" ? "ol" : "ul";
  return (
    <Tag>
      {list.items.map((item, index) => (
        <Fragment key={index}>
          <DeletedItemsAtV2 block={block} item={index} />
          <li>
            <TextContainerView content={item.content} path={{ kind: "item", block, item: index }} />
          </li>
        </Fragment>
      ))}
      <DeletedItemsAtV2 block={block} item={list.items.length} trailing />
    </Tag>
  );
}
