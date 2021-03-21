import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

const baseUrl = 'http://localhost:8080/rest/post';
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
    return this.http.get(`${baseUrl}/all`);
  }

  getAllForModer() {
    return this.http.get(baseUrl + '/all-moder');
  }

  get(id) {
    return this.http.get(`${baseUrl}/${id}`);
  }

  add(title: string, description: string, text: string, author: bigint) {
    return this.http.post(baseUrl + '/add', {
      title,
      description,
      text,
      author
    }, httpOptions);
  }

  getByAuthor(id) {
    return this.http.get(`${baseUrl}/all/${id}`);
  }

  editPost(id: bigint, title: string, description: string, text: string) {
    return this.http.patch(`${baseUrl}/edit/${id}`, {
      title,
      description,
      text
    }, httpOptions);
  }

  deletePost(id: bigint) {
    return this.http.delete(`${baseUrl}/${id}`);
  }

  deletePostByModer(id: bigint) {
    return this.http.delete(baseUrl + '/moder/' + id);
  }

  restore(id: bigint) {
    return this.http.patch(`${baseUrl}/${id}`, {});
  }
}
