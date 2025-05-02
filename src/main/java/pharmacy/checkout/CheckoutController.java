package pharmacy.checkout;

import org.salespointframework.core.Currencies;
import org.salespointframework.inventory.*;
import org.salespointframework.order.CartItem;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.Cart;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.annotation.SessionScope;
import pharmacy.catalog.Medication;
import pharmacy.catalog.MedicationCatalog;

import org.salespointframework.inventory.MultiInventory;
import org.salespointframework.catalog.Product.ProductIdentifier;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.javamoney.moneta.Money;
import java.util.Optional;



import java.util.Optional;

@Controller
@SessionAttributes("cart") // keeps cart in session
class CheckoutController {

	private final MedicationCatalog catalog;
	private final UniqueInventory<UniqueInventoryItem> inventory;
	private final CheckoutService checkoutService;
	private final Cart cart; // salespoint
	//private final OrderManagement<Order> orderManagement;
	// TODO: possibly
	//private final BusinessTime businessTime;
	//private final UserAccountManagement userAccountManagement;

	@Autowired
	CheckoutController(MedicationCatalog catalog, Cart cart, UniqueInventory<UniqueInventoryItem> inventory, OrderManagement<Order> orderManagement, CheckoutService checkoutService) {
		Assert.notNull(catalog, "MedicationCatalog must not be null");
		Assert.notNull(cart, "Cart must not be null");
		//Assert.notNull(orderManagement, "OrderManagement must not be null");
		Assert.notNull(inventory, "Inventory must not be null!");
		Assert.notNull(checkoutService, "CheckoutService must not be null!");
		//Assert.notNull(businessTime, "BusinessTime must not be null!");
		//Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");

		this.catalog = catalog;
		this.cart = cart;
		this.inventory = inventory;
		//this.orderManagement = orderManagement;
		this.checkoutService = checkoutService;
		// this.businessTime = businessTime;
		// this.userAccountManagement = userAccountManagement;
	}


	// for @SessionAttributes (always a Cart-Object in Model)
	@ModelAttribute("cart")
	Cart initializeCart(){
		return cart;
	}


	@GetMapping("/checkout")
	//@PreAuthorize("hasAnyRole('ROLE_APOTHEKER', 'ROLE_CHEF')")
	public String checkoutView(Model model){
		if (!cart.isEmpty()){
			Money coPayment = checkoutService.calculateCoPayment(cart);
			model.addAttribute("calculatedCoPayment", coPayment);
			model.addAttribute("totalPriceInsurance", coPayment);
			model.addAttribute("totalPriceCash", cart.getPrice());
		}
		return "checkout"; // name of checkout.html template file
	}


	// add medication to cart ---------------------------------------------------
	@PostMapping("/checkout/add")
	public String addToCart(@RequestParam String barcode, @RequestParam(defaultValue = "1") int quantity,RedirectAttributes redirectAttributes){
		if (barcode == null || barcode.trim().isEmpty() || quantity <= 0){
			redirectAttributes.addFlashAttribute("errorMessage", "Ungültige Eingabe für Barcode oder Menge.");
			return "redirect:/checkout";
		}

		Optional<Medication> medicationOpt = catalog.findByBarcode(barcode);
		// TODO: name suchen

		if (medicationOpt.isPresent()){
			Medication medication = medicationOpt.get();
			Quantity requestedQuantity = Quantity.of(quantity);

			Optional<UniqueInventoryItem> inventoryItemOpt = inventory.findByProduct(medication);
			if (inventoryItemOpt.isEmpty() || inventoryItemOpt.get().getQuantity().isLessThan(requestedQuantity)) {
				redirectAttributes.addFlashAttribute("errorMessage",
					"Item '" + medication.getName() + "' nicht oder nicht in ausreichender Menge ("+ quantity +") verfügbar. Bestand: "
						+ inventoryItemOpt.map(item -> item.getQuantity().getAmount().intValue()).orElse(0));
				// Option to Order for tomorrow
				redirectAttributes.addFlashAttribute("outOfStockItem", medication); // remember medication
				return "redirect:/checkout#backorder"; // back to checkout but with Back Order anchor
			}

			cart.addOrUpdateItem(medication, requestedQuantity);
			redirectAttributes.addFlashAttribute("successMessage", quantity + "x " + medication.getName() + " zum Warenkorb hinzugefügt!");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Item mit Barcode '" + barcode + "' nicht gefunden.");
		}
		return "redirect:/checkout";
	}


	// update/delete medication form cart ---------------------------------------
	@PostMapping("/checkout/updateQuantity")
	public String updateQuantity(@RequestParam String itemId, @RequestParam int quantity, RedirectAttributes redirectAttributes){
		if (quantity < 0) {
			redirectAttributes.addFlashAttribute("errorMessage", "Menge darf nicht negativ sein.");
			return "redirect:/checkout";
		}

		// TODO: maybe use barcode or name instead??
		Optional<CartItem> itemOpt = cart.getItem(itemId);
		if (itemOpt.isPresent()){
			CartItem item = itemOpt.get();
			Quantity requestedQuantity = Quantity.of(quantity);
			Optional<UniqueInventoryItem> inventoryItemOpt = inventory.findByProductIdentifier(item.getProduct().getId());

			if (inventoryItemOpt.isEmpty() || inventoryItemOpt.get().getQuantity().isLessThan(requestedQuantity)) {
				redirectAttributes.addFlashAttribute("errorMessage", "Bestand für '" + item.getProductName() + "' reicht nicht für Menge " + quantity + ". Bestand: " + inventoryItemOpt.map(invItem -> invItem.getQuantity().getAmount().intValue()).orElse(0));
				return "redirect:/checkout";
			}

			cart.removeItem(itemId);
			if (quantity == 0){
				redirectAttributes.addFlashAttribute("infoMessage", item.getProductName() + " entfernt.");
			} else {
				cart.addOrUpdateItem(item.getProduct(), requestedQuantity);
				redirectAttributes.addFlashAttribute("infoMessage", "Menge für " + item.getProductName() + " auf " + quantity + " geändert.");
			}
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Artikel nicht im Warenkorb gefunden.");
		}

		return "redirect:/checkout";
	}


	// delete item from cart -------------------------------------
	@PostMapping("/checkout/remove")
	public String removeItem(@RequestParam String itemId, RedirectAttributes redirectAttributes){

		Optional<CartItem> itemOpt = cart.getItem(itemId);
		if(itemOpt.isPresent()){
			cart.removeItem(itemId);
			redirectAttributes.addFlashAttribute("infoMessage", itemOpt.get().getProductName() + " aus Warenkorb entfernt.");
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "Zu löschender Artikel nicht im Warenkorb gefunden.");
		}
		return "redirect:/checkout";
	}
	// clear cart
	@PostMapping("/checkout/clear")
	public String clearCart(RedirectAttributes redirectAttributes) {
		if (!cart.isEmpty()) {
			cart.clear();
			redirectAttributes.addFlashAttribute("infoMessage", "Warenkorb geleert.");
		}
		return "redirect:/checkout";
	}

	// finish checkout -------------------------------------------
	@PostMapping("/checkout/finish")
	public String finish(@RequestParam String paymentMethod, RedirectAttributes redirectAttributes){
		if (cart.isEmpty()){
			redirectAttributes.addFlashAttribute("errorMessage", "Warenkorb ist leer.");
			return "redirect:/checkout";
		}

		// Real Order would be made here ---------
		// Order order = new Order(userAccountManagement.getCurrentUser().orElseThrow(), Cash.CASH); // oder andere PaymentMethod
		// cart.addItemsTo(order);
		// orderManagement.payOrder(order);
		// orderManagement.completeOrder(order);
		// cart.clear();

		// Prototype -------------
		Money finalTotal = (Money) cart.getPrice();
		Money coPayment = Money.of(0, Currencies.EURO);
		boolean hasInsurance  = paymentMethod.equalsIgnoreCase("INSURANCE");

		if (hasInsurance) {
			coPayment = checkoutService.calculateCoPayment(cart);
			finalTotal = coPayment;
		}

		redirectAttributes.addFlashAttribute("billTotal", finalTotal);
		redirectAttributes.addFlashAttribute("billCoPayment", coPayment);
		redirectAttributes.addFlashAttribute("billPaymentMethod", paymentMethod);
		// redirectAttributes.addFlashAttribute("checkoutItems", new ArrayList<>(cart.toList()));

		cart.clear();
		return "redirect:/checkout/bill";
	}

	// show finished checkout bill ---------------------------------
	@GetMapping("/checkout/bill")
	public String billView(Model model) {
		if (!model.containsAttribute("billTotal")) return "redirect:/checkout";
	return "bill"; // name of bill.html template
	}


	// backorder for out of stock items ----------------------------
	@PostMapping("/checkout/backorder")
	public String backorder(@RequestParam String itemName, @RequestParam String productId, RedirectAttributes redirectAttributes){
		// TODO: make real order in system with unique id
		// simple prototype
		String backorderNumber = "B" + System.currentTimeMillis() % 10000;
		redirectAttributes.addFlashAttribute("backorderNumber", backorderNumber);
		redirectAttributes.addFlashAttribute("backorderItem", itemName);

		return "redirect:/checkout/backorderbill";
	}

	// show backorder bill ----------------------------------------
	@PostMapping("/checkout/backorderbill")
	public String bestellzettelView(Model model){
		if (!model.containsAttribute("backorderNumber")) return "redirect:/checkout";
		return "backorderbill"; // name of html template
	}

	// idk if needed =============================================
	@Bean
	@SessionScope
	public Cart cart() {
		return new Cart();
	}

}

