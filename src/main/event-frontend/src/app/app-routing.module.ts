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
import { ManageGroupsComponent } from './_components/manage-groups/manage-groups.component';

const routes: Routes = 
[
  { path: 'user-management', component: UserManagementComponent, canActivate: [AuthGuardService], data: {roles: ['event_usermanagement']} },// create / delete bans only for global admins
  { path: 'group-management', component: ManageGroupsComponent, canActivate: [AuthGuardService], data: {roles: ['event_usermanagement']} },

  { path: 'event-list', component: EventListComponent, canActivate: [AuthGuardService]},//, data: {roles: ['']} 
  { path: 'create-event', component: CreateEventComponent, canActivate: [AuthGuardService], data: {roles: ['event_canCreateEvent']}},
  { path: 'edit-event', component: EditEventComponent, canActivate: [AuthGuardService], data: {roles: ['event_canEditAll','event_canEditOwn']} },
  { path: 'delete-event', component: DeleteEventComponent, canActivate: [AuthGuardService], data: {roles: ['event_canDeleteAll','event_canDeleteOwn']} },
  { path: 'event/:type/:id', component: ShowSingleEventComponent, canActivate: [AuthGuardService] },

  { path: '', redirectTo: '/event-list', pathMatch: 'full'},
  { path: '**', redirectTo: '/event-list', pathMatch: 'full'} 
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
