// TODO: reimplement when an example template exists

// import { useState } from "react";
//
// import type { FieldType, ObjectTypeSpecifications, TArray } from "~/types/brevbakerTypes";
//
// import type { BoundAction } from "../../LetterEditor/lib/actions";
// import { bindAction } from "../../LetterEditor/lib/actions";
// import { ModelValueAction } from "../actions";
// import type { FieldValue } from "../model";
// import { initValueFromSpec } from "../model";
// import { FieldEditor } from "./FieldEditor";
//
// interface AddArrayValueProperties {
//   allSpecs: ObjectTypeSpecifications;
//   spec: FieldType;
//   addValue: BoundAction<[newValue: FieldValue<FieldType>]>;
// }
// const AddArrayValue = ({ allSpecs, spec, addValue }: AddArrayValueProperties) => {
//   const [value, updateValue] = useState(initValueFromSpec(allSpecs, spec, false));
//   const doAddValue = () => {
//     addValue(value);
//     updateValue(initValueFromSpec(allSpecs, spec, false));
//   };
//   return (
//     <div>
//       <FieldEditor allSpecs={allSpecs} spec={spec} updateValue={updateValue} value={value} />
//       <button onClick={doAddValue} type="button">
//         Add
//       </button>
//     </div>
//   );
// };
//
// export interface ArrayEditorProperties {
//   allSpecs: ObjectTypeSpecifications;
//   spec: TArray;
//   array: FieldValue<TArray>;
//   updateArray: BoundAction<[FieldValue<TArray>]>;
// }
//
// export const ArrayEditor = ({ allSpecs, spec, array, updateArray }: ArrayEditorProperties) => (
//   <div>
//     <ul>
//       {array.map((item: FieldValue<FieldType>, index: number) => (
//         <li key={index}>
//           <FieldEditor
//             allSpecs={allSpecs}
//             spec={spec.items}
//             updateValue={bindAction(ModelValueAction.UpdateArrayIndex, updateArray, array, index)}
//             value={item}
//           />
//         </li>
//       ))}
//     </ul>
//     <AddArrayValue
//       addValue={bindAction(ModelValueAction.AddToArray, updateArray, array)}
//       allSpecs={allSpecs}
//       spec={spec.items}
//     />
//   </div>
// );
