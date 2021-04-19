# Pet web-service

_**This is web-application for pet owners and their pets or for people who want to have a pet, as well as for pet expert. 
In this web-app, users can find a lot of useful information from posts written by a pet expert. In addition, pet owners
can ask any question in the online chat and the expert will answer it.**_

## _Roles._
* ### Pet owner - a user who can read posts and ask for online help.
* ### Pet expert - a user who can write posts and provide online help.
* ### Moderator - a user who has the rights listed above and has the rights to resolve disputes.
* ### Administrator - a user who has full rights and also has rights to appoint moderators.

## _Use cases._

* **User registration.** After registration, the user is assigned a pet owner role without pets.


* In the profile user can change the profile information. Besides, he or she can add pets to their profile.
  

* If a user wants to write posts and provide online help, they can choose the appropriate section in their profile.
  He or she needs to fill in all the necessary data and send a request, which the moderator will consider. After the 
  moderator has considered the request, the request will either be confirmed, and the user will be assigned a pet expert 
  role, or the request will be rejected.


* The **moderator** can be any user appointed by the administrator.


* User can have multiple roles.


* **Owner** can ask for help from a pet expert in online chat section. The **expert's** rating is displayed in the
chat section and their profiles.


* **Pet owner** and **expert** can read the posts. If the post is incorrect or contains errors, users can leave a
  complaint about it.
  
  
* **Pet expert** can add a new post that will be visible to all users. He or she should specify the title, 
  small description, for whom this post is intended, and the main text of the post.
  
  
* **Pet expert** can edit previously written posts. To do this, he must select ‘Edit posts’ section and select the post 
  which he wants to change from list of posts.
  
  
* If **expert** has chosen that he is ready to provide online help, he or she can go to the 
  section with the online chat and answer the questions sent to him or her.
  

* If the question is incorrect, an **expert** can complain about the pet owner who asked it.



* **Moderator** can review complaints and make decisions about the correctness of the complaint. If the complaint is 
  correct **moderator** must resolve it. The moderator has rights to resolve disputed situations: blocking the user, 
  adjusting the articles, removing the post from public access.
