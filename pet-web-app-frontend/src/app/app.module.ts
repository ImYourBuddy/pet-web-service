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
import {NotifierModule, NotifierOptions} from 'angular-notifier';
import {JwPaginationModule} from 'jw-angular-pagination';
import {EditPetComponent} from './components/edit-pet/edit-pet.component';
import {EditExpertComponent} from './components/edit-expert/edit-expert.component';

const customNotifierOptions: NotifierOptions = {
  position: {
    horizontal: {
      position: 'left',
      distance: 12
    },
    vertical: {
      position: 'bottom',
      distance: 12,
      gap: 10
    }
  },
  theme: 'material',
  behaviour: {
    autoHide: false,
    onClick: 'hide',
    onMouseover: 'pauseAutoHide',
    showDismissButton: true,
    stacking: 4
  },
  animations: {
    enabled: true,
    show: {
      preset: 'slide',
      speed: 300,
      easing: 'ease'
    },
    hide: {
      preset: 'fade',
      speed: 300,
      easing: 'ease',
      offset: 50
    },
    shift: {
      speed: 300,
      easing: 'ease'
    },
    overlap: 150
  }
};
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
    ChatListComponent,
    EditPetComponent,
    EditExpertComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NotifierModule.withConfig(customNotifierOptions),
    JwPaginationModule
  ],
  providers: [authInterceptorProviders, ChatComponent],
  bootstrap: [AppComponent]
})
export class AppModule { }
