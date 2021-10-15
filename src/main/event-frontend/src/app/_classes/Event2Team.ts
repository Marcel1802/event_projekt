import { Person } from "./person"

export interface Event2Team {
    id:string;//UUID
    teamname:string;
    maxPeople:number;
    members:Person[];
}