import { Game } from "./game";
import { Group } from "./Group";
import { Person } from "./person";

export interface Event1 {
    id: string; //UUID
    eventName: string;
    game: Game;
    date: Date;
    description: string;
    group:Group | null;
    type:number;
    createdBy: Person;
    createdTime: Date;

    minPeople: number;
    maxPeople: number;
    participants: Person[];
    
} 