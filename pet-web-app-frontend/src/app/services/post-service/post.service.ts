import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

const API_URL = 'http://localhost:8080/rest/post';
const httpOptions = {
  headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class PostService {

  constructor(private http: HttpClient) {
  }

  getAll() {
    return this.http.get(API_URL);
  }

  getAllForModer() {
    return this.http.get(API_URL + '/moder');
  }

  get(id) {
    return this.http.get(API_URL + '/' + id);
  }

  add(title: string, description: string, text: string, author: bigint) {
    return this.http.post(API_URL, {
      title,
      description,
      text,
      author
    }, httpOptions);
  }

  getByAuthor(id) {
    return this.http.get(API_URL + '/author/' + id);
  }

  editPost(id: bigint, title: string, description: string, text: string) {
    return this.http.put(API_URL + '/' + id , {
      title,
      description,
      text
    }, httpOptions);
  }

  deletePost(id: bigint) {
    return this.http.delete(API_URL + '/' + id);
  }

  deletePostByModer(id: bigint) {
    return this.http.delete(API_URL + '/moder/' + id);
  }

  restore(id: bigint) {
    return this.http.patch(API_URL + '/' + id, {});
  }

  ratePost(postId: bigint, userId: bigint, liked: boolean) {
    return this.http.post(API_URL + '/rate', {
      postId,
      userId,
      liked
    });
  }

  checkMark(postId, userId) {
    return this.http.get(API_URL + '/rate/' + postId + '/' + userId);
  }

}
