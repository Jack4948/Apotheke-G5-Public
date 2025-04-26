package pharmacy.checkout;

import org.javamoney.moneta.Money;
import org.salespointframework.order.Cart;
import org.salespointframework.order.CartItem;
//import org.salespointframework.payment.Cash;
import org.salespointframework.core.Currencies;
import org.springframework.stereotype.Service;
import pharmacy.catalog.Medication;

import java.math.BigDecimal;

@Service
public class CheckoutService {
	private static final Money CO_PAYMENT_MIN = Money.of(5, Currencies.EURO);
	private static final Money CO_PAYMENT_MAX = Money.of(10, Currencies.EURO); // per medication
	private static final BigDecimal CO_PAYMENT_RATE = new BigDecimal("0.10");

	/**
	 * Calculates co-payment for all prescription medications in cart.
	 * RULE: 10% of total price, min 5€, max 10€ PER MEDICATION.
	 * @return total co-payment as a Money-Object.
	 */
	public Money calculateCoPayment(Cart cart) {
		Money totalCoPayment = Money.of(0, Currencies.EURO);

		for (CartItem item : cart){
			if (item.getProduct() instanceof Medication medication && medication.getNeedsPrescription()) {
				Money pricePerUnit = (Money) item.getPrice().divide(item.getQuantity().getAmount());
				Money coPaymentPerUnit = pricePerUnit.multiply(CO_PAYMENT_RATE);

				if (coPaymentPerUnit.isLessThan(CO_PAYMENT_MIN)) coPaymentPerUnit = CO_PAYMENT_MIN;
				else if (coPaymentPerUnit.isGreaterThan(CO_PAYMENT_MAX)) coPaymentPerUnit = CO_PAYMENT_MAX;
				totalCoPayment = totalCoPayment.add(coPaymentPerUnit.multiply(item.getQuantity().getAmount()));
			}
		}
		return totalCoPayment;
	}
}