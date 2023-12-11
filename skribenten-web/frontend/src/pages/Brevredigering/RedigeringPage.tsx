import { useMutation, useQuery } from "@tanstack/react-query";
import React, { useState } from "react";

import { getTemplate, renderLetter } from "../../api/skribenten-api-endpoints";
import type { ObjectTypeSpecification, ObjectTypeSpecifications } from "../../types/brevbakerTypes";
import Actions from "./OldComponents/LetterEditor/actions";
import LetterEditor from "./OldComponents/LetterEditor/LetterEditor";
import type { LetterEditorState } from "./OldComponents/LetterEditor/model/state";
import type { ObjectValue } from "./OldComponents/ModelEditor/model";
import ModelEditor from "./OldComponents/ModelEditor/ModelEditor";

const VAL = {
  mottattSoeknad: "2023-09-11",
  ytelse: "test",
  land: "NO",
  inkluderVenterSvarAFP: null,
  svartidUker: 1,
};

export function RedigeringPage() {
  const b = "INFORMASJON_OM_SAKSBEHANDLINGSTID";
  const [modelValue, setModelValue] = useState<ObjectValue>({});
  const [editorState, setEditorState] = useState<LetterEditorState | null>(null);

  const modelSpec = useQuery({
    queryKey: getTemplate.queryKey(b),
    queryFn: () => getTemplate.queryFn(b),
  }).data;

  const c = useMutation<unknown, unknown, string>({
    mutationFn: async (id) => {
      if (modelSpec) {
        return await renderLetter(id, { letterData: VAL, editedLetter: undefined });
      }
    },
    onSuccess: (letter) => {
      console.log(letter);
      setEditorState(Actions.create(letter));
    },
  });
  if (!modelSpec) {
    return <></>;
  }
  console.log(editorState);
  return (
    <div>
      <button onClick={() => c.mutate(b)}>test</button>
      <ModelEditor spec={modelSpec.modelSpecification} updateValue={setModelValue} value={modelValue} />
      <div>
        {editorState && <LetterEditor editorState={editorState} updateState={(update) => setEditorState(update)} />}
      </div>
    </div>
  );
}

export function initObjectFromSpec(allSpecs: ObjectTypeSpecifications, spec: ObjectTypeSpecification): any {
  const object = {};
  for (const [field, fieldSpec] of Object.entries(spec)) {
    object[field] = initValueFromSpec(allSpecs, fieldSpec);
  }
  return object;
}

export function initValueFromSpec(objectSpecs: ObjectTypeSpecifications, spec: any, nullIfNullable?: boolean): any;
export function initValueFromSpec(objectSpecs: ObjectTypeSpecifications, spec: any, nullIfNullable = true): any | null {
  if (spec.nullable && nullIfNullable) {
    return null;
  } else {
    switch (spec.type) {
      case "scalar": {
        switch (spec.kind) {
          case "NUMBER": {
            return 0;
          }
          case "DOUBLE": {
            return 0;
          }
          case "STRING": {
            return "";
          }
          case "BOOLEAN": {
            return false;
          }
          case "DATE": {
            return "";
          }
          default: {
            throw `Unsupported scalar kind ${spec.kind} for: ${JSON.stringify(spec)}`;
          }
        }
      }
      case "enum": {
        return spec.values[0];
      }
      case "array": {
        return [];
      }
      case "object": {
        return initObjectFromSpec(objectSpecs, objectSpecs[spec.typeName]);
      }
    }
  }
}
