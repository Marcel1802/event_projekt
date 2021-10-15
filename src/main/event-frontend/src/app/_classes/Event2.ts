import { Event2Team } from "./Event2Team";
import { Game } from "./game";
import { Group } from "./Group";
import { Person } from "./person";

export interface Event2 {
    id:string;//UUID
    eventName:string;
    game:Game;
    date:Date;
    description:string;
    createdBy:Person;
    createdTime:Date;
    group:Group | null;
    teams: Event2Team[];
    type:number;
}