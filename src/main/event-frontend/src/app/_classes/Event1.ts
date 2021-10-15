import { Game } from "./game";
import { Group } from "./Group";
import { Person } from "./person";

export interface Event1 {
    id: string; //UUID
    eventName: string;
    game: Game;
    date: Date;
    description: string;
    minPeople: number;
    maxPeople: number;
    createdBy: Person;
    createdTime: Date;
    participants: Person[];
    group:Group | null;
    type:number;
} 