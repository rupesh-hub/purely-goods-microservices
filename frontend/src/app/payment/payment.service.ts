import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { API_CONSTANTS } from '../constants';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private http:HttpClient = inject(HttpClient);

  public payment = ():Observable<any> => {
    return this.http.get<any>(`${API_CONSTANTS.BASE_URL}/${API_CONSTANTS.ENDPOINTS.PAYMENT.PING}`);
  }

}
