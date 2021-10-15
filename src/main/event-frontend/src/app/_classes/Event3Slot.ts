import { Event3Squad } from "./Event3squad";
import { Person } from "./person";

export interface Event3Slot {
    UUID: string; // UUID
    role: String;
    slotUsedBy: Person | null;
}