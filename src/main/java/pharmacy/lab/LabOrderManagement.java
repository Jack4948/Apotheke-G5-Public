package pharmacy.lab;

import java.util.Optional;

import org.salespointframework.order.Order.OrderIdentifier;
import org.salespointframework.order.OrderCompletionFailure;
import org.salespointframework.order.OrderEvents.OrderCanceled;
import org.salespointframework.order.OrderEvents.OrderCompleted;
import org.salespointframework.order.OrderEvents.OrderPaid;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.OrderStatus;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.time.Interval;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.mysema.commons.lang.Assert;

import pharmacy.lab.LabOrder.LabOrderStatus;

@Service
public class LabOrderManagement implements OrderManagement<LabOrder> {
  private OrderManagement<LabOrder> om;

  public LabOrderManagement(OrderManagement<LabOrder> orderManagement) {
    this.om = orderManagement;
  }

  /**
   * Saves the given {@link Order} or persists changes to it.
   *
   * @param order the order to be saved, must not be {@literal null}.
   * @return
   */
  public LabOrder save(LabOrder order) {
    return om.save(order);
  }

  /**
   * Returns the order identified by an {@link OrderIdentifier}
   *
   * @param orderIdentifier identifier of the {@link Order} to be returned, must
   *                        not be {@literal null}.
   * @return the order if the orderIdentifier matches, otherwise
   *         {@link Optional#empty()}.
   */
  public Optional<LabOrder> get(OrderIdentifier orderIdentifier) {
    return om.get(orderIdentifier);
  }

  /**
   * Checks if this {@link OrderManagement} contains an order.
   *
   * @param orderIdentifier the {@link OrderIdentifier} of the {@link Order}, must
   *                        not be {@literal null}.
   * @return {@literal true} if the OrderManager contains the order,
   *         {@literal false} otherwise.
   */
  public boolean contains(OrderIdentifier orderIdentifier) {
    return om.contains(orderIdentifier);
  }

  /**
   * Returns all {@link Order}s having the {@link OrderStatus} {@code status}. If
   * no orders with the specified status
   * exist, an empty Iterable is returned.
   *
   * @param orderStatus Denoting the {@link OrderStatus} on which the
   *                    {@link Order}s will be requested.
   * @return a {@link Streamable} containing all {@link Order}s with the specified
   *         {@link OrderStatus}
   */
  public Streamable<LabOrder> findBy(OrderStatus orderStatus) {
    return om.findBy(orderStatus);
  }

  /**
   * Returns all {@link Order}s in between the {@link Interval}. So every entry
   * with an time stamp &le;
   * and &ge; from is returned. If no {@link Order}s within the specified time
   * span exist,
   * an empty {@link Iterable} is returned.
   *
   * @param interval The time interval to find {@link Order}s in, must not be
   *                 {@literal null}.
   * @return a {@link Streamable} containing all {@link Order}s in the given
   *         {@link Interval}.
   */
  public Streamable<LabOrder> findBy(Interval interval) {
    return om.findBy(interval);
  }

  /**
   * Returns all {@link Order}s of the given {@link UserAccount}. If this user has
   * no orders, an empty {@link Iterable}
   * is returned.
   *
   * @param userAccount Denoting the {@link UserAccount} on which the orders will
   *                    be requested, must not be
   *                    {@literal null}.
   * @return a {@link Streamable} containing all orders of the specified user.
   */
  public Streamable<LabOrder> findBy(UserAccount userAccount) {
    return om.findBy(userAccount);
  }

  /**
   * Returns all {@link Order}s from the given {@link UserAccount} in between the
   * dates {@code from} and {@code to},
   * including from and to. So every entry with an time stamp &le; to and &ge;
   * from is returned.
   * If this user has no {@link Order}s in this period, an empty {@link Iterable}
   * is returned.
   *
   * @param userAccount The {@link UserAccount} whose {@link Order}s shall be
   *                    returned, must not be {@literal null}.
   * @param interval    The time interval to find {@link Order}s in, must not be
   *                    {@literal null}.
   * @return a {@link Streamable} containing all orders from the specified user in
   *         the specified period.
   */
  public Streamable<LabOrder> findBy(UserAccount userAccount, Interval interval) {
    return om.findBy(userAccount, interval);
  }

  /**
   * Marks this order as "in progress"
   *
   * @param order the order to mark, must not be {@literal null}.
   * @see OrderCompleted
   */
  public void startOrder(LabOrder order) {
    Assert.notNull(order, "Order must not be null");
    order.setLabStatus(LabOrderStatus.IN_PROGRESS);
    om.save(order);
  }

  /**
   * Tries to complete this order, the {@link OrderStatus} has to be
   * {@link OrderStatus#PAID}.
   *
   * @param order the order to complete, must not be {@literal null}.
   * @throws OrderCompletionFailure in case the order can't be completed.
   * @see OrderCompleted
   */
  public void completeOrder(LabOrder order) throws OrderCompletionFailure {
    order.setLabStatus(LabOrderStatus.READY_FOR_PICKUP);
    om.completeOrder(order);
  }

  /**
   * Marks this order as picked up. The order status must be
   * {@link LabOrderStatus#READY_FOR_PICKUP}.
   *
   * @param order the order to mark, must not be {@literal null}.
   * @see OrderCompleted
   */
  public void pickupOrder(LabOrder order) throws OrderCompletionFailure {
    Assert.notNull(order, "Order must not be null");
    if (!order.isReadyForPickup()) {
      throw new OrderCompletionFailure(order, "The order is not ready for pickup yet");
    }
    order.setLabStatus(LabOrderStatus.PICKED_UP);
    om.save(order);
  }

  /**
   * Pays the {@link Order}, {@link OrderStatus} must be {@link OrderStatus#OPEN}
   * and {@link PaymentMethod} must be set.
   *
   * @param order the order to be payed, must not be {@literal null}.
   * @return true if the order could be paid
   * @see OrderPaid
   */
  public boolean payOrder(LabOrder order) {
    return om.payOrder(order);
  }

  /**
   * Cancels an {@link Order}, no matter what state it is in.
   *
   * @param order  the order to be canceled, must not be {@literal null}.
   * @param reason the reason the order was canceled.
   * @return true if the order could be canceled
   * @see OrderCanceled
   */
  public boolean cancelOrder(LabOrder order, String reason) {
    return om.cancelOrder(order, reason);
  }

  /**
   * Deletes the given {@link Order}.
   *
   * @param order must not be {@literal null}.
   * @return the deleted {@link Order}
   * @since 7.1
   */
  public LabOrder delete(LabOrder order) {
    return om.delete(order);
  }

  /**
   * Returns the {@link Page} of orders specified by the given {@link Pageable}.
   *
   * @return the {@link Page} of orders specified by the given {@link Pageable}.
   * @since 7.1
   */
  public Page<LabOrder> findAll(Pageable pageable) {
    return om.findAll(pageable);
  }
}
