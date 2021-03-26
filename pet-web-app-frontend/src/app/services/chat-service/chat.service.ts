import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

const API_URL = 'http://localhost:8080';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  constructor(private http: HttpClient) { }
}
