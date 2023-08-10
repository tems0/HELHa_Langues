import { Reponse } from "./reponse";

export interface Qcmquestion {
  qcmQuestionId: number;
  questionNom: string;
  responses: Reponse[];
}
