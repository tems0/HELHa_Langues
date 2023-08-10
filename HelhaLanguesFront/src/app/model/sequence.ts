import { Qcmquestion } from "./qcmquestion";

export interface Sequence {
  sequenceId: number;
  languages: string;
  audioMP3: string;
  videoMP4: string;
  score: number;
  timer: string;
  scoreGot: number;
  completed: boolean;
  qcmQuestions: Qcmquestion[];
}
