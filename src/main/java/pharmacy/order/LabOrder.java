package pharmacy.order;

import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;

import jakarta.persistence.Entity;

@Entity
public class LabOrder extends Order {
  public LabOrder() {
    super();
  }

  public LabOrder(UserAccountIdentifier id) {
    super(id);
  }

  public LabOrder(UserAccountIdentifier id, PaymentMethod pm) {
    super(id, pm);
  }
}
