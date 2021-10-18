import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule, Routes } from '@angular/router';
import { UserManagementComponent } from './_components/user-management/user-management.component';
import { EventListComponent } from './_components/event-list/event-list.component';
import { CreateEventComponent } from './_components/create-event/create-event.component';
import { EditEventComponent } from './_components/edit-event/edit-event.component';
import { DeleteEventComponent } from './_components/delete-event/delete-event.component';
import { ShowSingleEventComponent } from './_components/show-single-event/show-single-event.component';
import { AuthGuardService } from './_services/auth-guard.service';

const routes: Routes = 
[
  { path: 'user-management', component: UserManagementComponent },

  { path: 'event-list', component: EventListComponent, canActivate: [AuthGuardService]},//, data: {roles: ['']} 
  { path: 'create-event', component: CreateEventComponent },
  { path: 'edit-event', component: EditEventComponent },
  { path: 'delete-event', component: DeleteEventComponent },
  { path: 'event/:type/:id', component: ShowSingleEventComponent },

  { path: '', redirectTo: '/event-list', pathMatch: 'full' },
  { path: '**', redirectTo: '/event-list', pathMatch: 'full' } 
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
