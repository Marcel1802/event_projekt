import { Event3Squad } from "./Event3squad";
import { Game } from "./game";
import { Group } from "./Group";
import { Person } from "./person";

export interface Event3 {
    id: string; // UUID
    eventName: string;
    date: Date;
    game:Game;
    description: string;
    createdBy: Person;
    createdTime: Date;
    squads: Event3Squad[];
    group:Group | null;
    type:number;
}
