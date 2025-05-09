package pharmacy.lab;

import org.salespointframework.order.Order;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class LabOrder extends Order {
  @Enumerated(EnumType.STRING)
  private LabOrderStatus labStatus = LabOrderStatus.OPEN;

  public LabOrder() {
    super();
  }

  public LabOrder(UserAccountIdentifier id) {
    super(id);
  }

  public LabOrder(UserAccountIdentifier id, PaymentMethod pm) {
    super(id, pm);
  }

  public boolean isOpenLab() {
    return labStatus == LabOrderStatus.OPEN;
  }

  public boolean isInProgress() {
    return labStatus == LabOrderStatus.IN_PROGRESS;
  }

  public boolean isReadyForPickup() {
    return labStatus == LabOrderStatus.READY_FOR_PICKUP;
  }

  public boolean isPickedUp() {
    return labStatus == LabOrderStatus.PICKED_UP;
  }

  public void setLabStatus(LabOrderStatus newStatus) {
    labStatus = newStatus;
  }

  public enum LabOrderStatus {
    OPEN,
    IN_PROGRESS,
    READY_FOR_PICKUP,
    PICKED_UP,
  }
}
