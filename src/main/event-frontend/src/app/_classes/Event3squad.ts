import { Event3 } from "./Event3";
import { Event3Slot } from "./Event3Slot";

export interface Event3Squad {
    id: string; // UUID
    squadname: string;
    description:string;
    slots: Event3Slot[];
}
