import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {FormsModule} from '@angular/forms';


import { AppComponent } from './app.component';
import { PostsListComponent } from './components/posts-list/posts-list.component';
import { UserLoginComponent } from './components/user-login/user-login.component';
import { AddUserComponent } from './components/add-user/add-user.component';
import { ReadPostComponent } from './components/read-post/read-post.component';
import {AppRoutingModule} from './app-routing.module';
import { ProfileComponent } from './components/profile/profile.component';
import { BoardAdminComponent } from './components/board-admin/board-admin.component';
import { BoardModeratorComponent } from './components/board-moderator/board-moderator.component';
import { BoardExpertComponent } from './components/board-expert/board-expert.component';
import {authInterceptorProviders} from './helpers/auth.interceptor';
import { EditProfileComponent } from './components/edit-profile/edit-profile.component';
import { AddPostComponent } from './components/add-post/add-post.component';
import { EditPostComponent } from './components/edit-post/edit-post.component';
import {AddExpertComponent} from './components/add-expert/add-expert.component';
import {AddPetComponent} from './components/add-pet/add-pet.component';
import {ChatComponent} from './components/chat/chat.component';
import {ExpertsListComponent} from './components/experts-list/experts-list.component';
import {ChatListComponent} from './components/chat-lists/chat-list.component';

@NgModule({
  declarations: [
    AppComponent,
    PostsListComponent,
    UserLoginComponent,
    AddUserComponent,
    AddExpertComponent,
    AddPetComponent,
    ReadPostComponent,
    ProfileComponent,
    BoardAdminComponent,
    BoardModeratorComponent,
    BoardExpertComponent,
    EditProfileComponent,
    AddPostComponent,
    EditPostComponent,
    ChatComponent,
    ExpertsListComponent,
    ChatListComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
