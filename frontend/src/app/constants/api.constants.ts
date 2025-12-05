import {environment} from '../../environments/environment';

export const API_CONSTANTS = {
  BASE_URL: environment.API_URL,
  ENDPOINTS: {
    PAYMENT: {
      "PING":"payment-service/ping"
    },
    CART: {
      "PING":"cart-service/ping"
    }
  }
}
