import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Post} from '../../models/post.model';

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

  getAll(): Observable<Post[]> {
    return this.http.get<Post[]>(API_URL);
  }

  getAllForModer(): Observable<Post[]> {
    return this.http.get<Post[]>(API_URL + '/all');
  }

  getById(id: bigint): Observable<Post> {
    return this.http.get(API_URL + '/' + id);
  }

  getDeletedPostById(id: bigint) {
    return this.http.get(API_URL + '/all/' + id);
  }

  getPostImage(id: bigint): Observable<any> {
    return this.http.get(API_URL + '/' + id + '/image');

  }

  add(post: Post, file: File): Observable<Post> {
    let fd = new FormData();
    let postBlob: Blob;
    // @ts-ignore
    postBlob = new Blob([JSON.stringify(post)], {type: 'application/json'});
    fd.append('post', postBlob);
    fd.append('file', file);
    return this.http.post(API_URL, fd);
  }

  getByAuthor(id: bigint): Observable<Post[]> {
    return this.http.get<Post[]>(API_URL + '/author/' + id);
  }

  editPost(post: Post, file: File): Observable<Post> {
    let fd = new FormData();
    let postBlob: Blob;
    // @ts-ignore
    postBlob = new Blob([JSON.stringify(post)], {type: 'application/json'});
    fd.append('post', postBlob);
    fd.append('file', file);
    return this.http.put(API_URL + '/' + post.id, fd);
  }

  deletePost(id: bigint): Observable<Post> {
    return this.http.patch(API_URL + '/' + id, {
      delete: true
    }, httpOptions);
  }

  deletePostByModer(id: bigint): Observable<Post> {
    return this.http.delete(API_URL + '/all/' + id);
  }

  restore(id: bigint): Observable<Post> {
    return this.http.patch(API_URL + '/' + id, {
      delete: false
    }, httpOptions);
  }

  ratePost(postId: bigint, userId: bigint, liked: boolean): Observable<any> {
    return this.http.post(API_URL + '/' + postId + '/rate', {
      postId,
      userId,
      liked
    });
  }

  checkMark(postId: bigint, userId: bigint): Observable<any> {
    return this.http.get(API_URL + '/rate/' + postId + '/' + userId);
  }

}
