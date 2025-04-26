package pharmacy.welcome;

import java.util.Optional;

import org.salespointframework.catalog.Product.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.Order.OrderIdentifier;
import org.salespointframework.payment.Cash;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import pharmacy.catalog.IngredientCatalog;
import pharmacy.catalog.MixtureIngredient;
import pharmacy.order.LabOrder;

@Controller
@SessionAttributes("cart")
public class LabController {
  private final OrderManagement<LabOrder> orderManagement;
  private final IngredientCatalog ingredientCatalog;
  private final UserAccountManagement userAccountManagement;

  LabController(OrderManagement<LabOrder> orderManagement, IngredientCatalog ingredientCatalog,
      UserAccountManagement userAccountManagement) {
    this.orderManagement = orderManagement;
    this.ingredientCatalog = ingredientCatalog;
    this.userAccountManagement = userAccountManagement;
  }

  @GetMapping("/labor/liste")
  public String labList(Model model) {
    model.addAttribute("orderList", orderManagement.findAll(Pageable.unpaged()));
    return "lab-list";
  }

  @GetMapping("/labor/neu")
  public String labNew(Model model, @RequestParam("search") Optional<String> search) {
    searchIngredients(model, search);
    return "lab-new";
  }

  @ModelAttribute("cart")
  Cart initializeCart() {
    return new Cart();
  }

  @PostMapping("/labor/neu")
  public String labNewIngredient(
      Model model,
      @RequestParam("search") Optional<String> search,
      @RequestParam("id") MixtureIngredient ingredient,
      @RequestParam("qty") int qty,
      @ModelAttribute Cart cart) {
    searchIngredients(model, search);
    cart.addOrUpdateItem(ingredient, qty);

    return "lab-new";
  }

  @PostMapping("/labor/bestellung")
  public String labOrder(@ModelAttribute Cart cart) {
    UserAccount anonymous = userAccountManagement.findByUsername("anonymous").orElseThrow();
    LabOrder order = new LabOrder(anonymous.getId(), Cash.CASH);
    cart.addItemsTo(order);
    cart.clear();

    order.addChargeLine(order.getOrderLines().getTotal().multiply(0.1), "Marge");
    orderManagement.payOrder(order);

    return "redirect:/labor/liste";
  }

  @PostMapping("/labor/herstellen")
  public String labManufacture(@RequestParam("id") LabOrder order) {
    orderManagement.completeOrder(order);
    return "redirect:/labor/liste";
  }

  private void searchIngredients(Model model, Optional<String> search) {
    if (!search.isPresent()) {
      model.addAttribute("searchResults", Streamable.empty());
    } else {
      Streamable<MixtureIngredient> ingredients = ingredientCatalog.findByName(search.orElseThrow());
      model.addAttribute("searchResults", ingredients);
    }
  }

  interface SearchData {
    String getSearch();
  }

  interface AddIngredientData {
    @NotEmpty
    ProductIdentifier getId();

    @NotEmpty
    int getQty();

    @NotEmpty
    Cart getCart();
  }
}
