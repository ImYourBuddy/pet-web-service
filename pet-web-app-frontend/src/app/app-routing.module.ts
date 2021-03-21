import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {PostsListComponent} from './components/posts-list/posts-list.component';
import {UserLoginComponent} from './components/user-login/user-login.component';
import {AddUserComponent} from './components/add-user/add-user.component';
import {ReadPostComponent} from './components/read-post/read-post.component';
import {BoardModeratorComponent} from './components/board-moderator/board-moderator.component';
import {BoardAdminComponent} from './components/board-admin/board-admin.component';
import {BoardExpertComponent} from './components/board-expert/board-expert.component';
import {ProfileComponent} from './components/profile/profile.component';
import {EditProfileComponent} from './components/edit-profile/edit-profile.component';
import {AddPostComponent} from './components/add-post/add-post.component';
import {EditPostComponent} from './components/edit-post/edit-post.component';
import {AddExpertComponent} from './components/add-expert/add-expert.component';

const routes: Routes = [
  { path: '', redirectTo: 'posts', pathMatch: 'full' },
  { path: 'posts', component: PostsListComponent },
  { path: 'login', component: UserLoginComponent },
  { path: 'signup', component: AddUserComponent },
  { path: 'posts/:id', component: ReadPostComponent },
  { path: 'posts/:id/edit', component: EditPostComponent },
  { path: 'profile', component: ProfileComponent },
  { path: 'profile/edit', component: EditProfileComponent },
  { path: 'profile/become-expert', component: AddExpertComponent },
  { path: 'moderator', component: BoardModeratorComponent },
  { path: 'admin', component: BoardAdminComponent },
  { path: 'expert', component: BoardExpertComponent },
  { path: 'add-post', component: AddPostComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
