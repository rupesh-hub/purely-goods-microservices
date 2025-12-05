import {Component, inject, OnInit} from '@angular/core';
import {PaymentService} from './payment.service';

@Component({
  selector: 'purely-goods-payment',
  imports: [],
  standalone: true,
  template: `
    <p>
      payment works!
    </p>
  `,
  styles: ``
})
export class PaymentComponent implements OnInit {

  private paymentService: PaymentService = inject(PaymentService);

  ngOnInit() {

    this.paymentService.payment()
      .subscribe({
        next: (response: any) => console.log(response),
        error: (error: any) => console.log(error),
        complete: () => console.log("completed")
      });

  }

}
