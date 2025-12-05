import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {PaymentComponent} from './payment/payment.component';

@Component({
  selector: 'purely-goods-root',
  imports: [RouterOutlet, PaymentComponent],
  standalone: true,
  template: `
    <purely-goods-payment/>
    <router-outlet/>
  `
})
export class AppComponent {



}
