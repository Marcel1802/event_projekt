import { NativeDateModule } from "@angular/material/core";
import { Person } from "./person";

export interface Ban {
    id:string;//UUID
    bannedPerson:Person;
    bannedFrom:Person;
    banTime:Date;
    bannedUntil: Date | null;
    reason:string;
    isPermanent:boolean;
}