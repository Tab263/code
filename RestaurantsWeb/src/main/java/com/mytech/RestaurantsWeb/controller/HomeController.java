package com.mytech.RestaurantsWeb.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.mytech.RestaurantsWeb.helpers.AppHelper;
import com.mytech.RestaurantsWeb.mailer.EmailService;
import com.mytech.RestaurantsWeb.payments.paypal.PaypalPaymentIntent;
import com.mytech.RestaurantsWeb.payments.paypal.PaypalPaymentMethod;
import com.mytech.RestaurantsWeb.payments.paypal.PaypalService;
import com.mytech.RestaurantsWeb.security.AppUserDetails;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.restaurant.service.entities.CartLine;
import com.restaurant.service.entities.Contact;
import com.restaurant.service.entities.Customer;
import com.restaurant.service.entities.FCategory;
import com.restaurant.service.entities.Income;
import com.restaurant.service.entities.IncomeItem;
import com.restaurant.service.entities.Ingredient;
import com.restaurant.service.entities.Order;
import com.restaurant.service.entities.OrderItem;
import com.restaurant.service.entities.Post;
import com.restaurant.service.entities.RestaurantTable;
import com.restaurant.service.entities.TCategory;
import com.restaurant.service.exceptions.PostNotFoundException;
import com.restaurant.service.repositories.CustomerRepository;
import com.restaurant.service.repositories.IngredientRepository;
import com.restaurant.service.repositories.RestaurantTableRespository;
import com.restaurant.service.services.CartService;
import com.restaurant.service.services.ContactService;
import com.restaurant.service.services.CustomerService;
import com.restaurant.service.services.FCategoryService;
import com.restaurant.service.services.IncomeItemService;
import com.restaurant.service.services.IncomeService;
import com.restaurant.service.services.IngredientService;
import com.restaurant.service.services.OrderItemService;
import com.restaurant.service.services.OrderService;
import com.restaurant.service.services.PostService;
import com.restaurant.service.services.RestaurantTableService;
import com.restaurant.service.services.TCategoryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private IngredientService ingredientService;
	@Autowired
	private CustomerService customerService;

	@Autowired
	private FCategoryService fCategoryService;

	@Autowired
	private RestaurantTableService restaurantTableService;

	@Autowired
	private TCategoryService tCategoryService;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderItemService orderItemService;

	@Autowired
	private PaypalService paypalService;
	@Autowired
	private IncomeService incomeService;
	@Autowired
	private IncomeItemService incomeItemService;
	@Autowired
	private CustomerRepository customerRepository;

	private Logger log = LoggerFactory.getLogger(getClass());

	public static final String PAYPAL_SUCCESS_URL = "success";
	public static final String PAYPAL_CANCEL_URL = "checkout";
	@Autowired
	private IngredientRepository ingredientRepository;

	@Autowired
	private RestaurantTableRespository restaurantTableRespository;

	@GetMapping("/menu")
	public String viewMenuPage(Model model, @RequestParam("tableId") Long tableId) {
		List<Ingredient> listIng = ingredientService.getAllIngredientWithCategoryAndUnit();
		List<FCategory> listCate = fCategoryService.getAllCategory();
		boolean res = restaurantTableService.getTableStatusById(tableId);
		String tableName = restaurantTableService.getTableNameById(tableId);
		Long table = restaurantTableService.getTableIdById(tableId);
		BigDecimal total = cartService.calculateTotalForTable(tableName);
		List<CartLine> cartLines = cartService.listCartLines(tableName);
		List<CartLine> orderHistory = cartService.getOrderHistory();
		List<CartLine> orderHistory1 = cartService.getOrderHistoryCheckout(tableName);

		BigDecimal total1 = BigDecimal.ZERO;
		for (CartLine order : orderHistory1) {
			BigDecimal quantity = order.getQuantity();
			if (order.isHalfPortion()) {
				quantity = quantity.multiply(BigDecimal.valueOf(2));
			}
			total1 = total1.add(order.getPrice().multiply(quantity));
		}

		model.addAttribute("orderHistory", orderHistory);
		model.addAttribute("total", total);
		model.addAttribute("total1", total1);
		model.addAttribute("table", table);
		model.addAttribute("res", res);
		model.addAttribute("tableId", tableId);
		model.addAttribute("tableName", tableName);
		model.addAttribute("listCate", listCate);
		model.addAttribute("listIng", listIng);
		model.addAttribute("cartLines", cartLines);

		return "menu";
	}

	@GetMapping("/hotpot")
	public String viewHotpotPage(Model model, @RequestParam("tableId") Long tableId) {
		List<Ingredient> listIng = ingredientService.getAllIngredientWithCategoryAndUnit();
		List<FCategory> listCate = fCategoryService.getAllCategory();
		boolean res = restaurantTableService.getTableStatusById(tableId);
		String tableName = restaurantTableService.getTableNameById(tableId);
		Long table = restaurantTableService.getTableIdById(tableId);
		BigDecimal total = cartService.calculateTotalForTable(tableName);
		List<CartLine> cartLines = cartService.listCartLines(tableName);
		List<CartLine> orderHistory = cartService.getOrderHistory();
		List<CartLine> orderHistory1 = cartService.getOrderHistoryCheckout(tableName);

		BigDecimal total1 = BigDecimal.ZERO;
		for (CartLine order : orderHistory1) {
			BigDecimal quantity = order.getQuantity();
			if (order.isHalfPortion()) {
				quantity = quantity.multiply(BigDecimal.valueOf(2));
			}
			total1 = total1.add(order.getPrice().multiply(quantity));
		}

		model.addAttribute("orderHistory", orderHistory);
		model.addAttribute("total", total);
		model.addAttribute("total1", total1);
		model.addAttribute("table", table);
		model.addAttribute("res", res);
		model.addAttribute("tableId", tableId);
		model.addAttribute("tableName", tableName);
		model.addAttribute("listCate", listCate);
		model.addAttribute("listIng", listIng);
		model.addAttribute("cartLines", cartLines);

		return "hotpot";
	}

	@GetMapping("/checkout")
	public String viewCheckout(Model model, @RequestParam("tableId") Long tableId) {
		List<Ingredient> listIng = ingredientService.getAllIngredientWithCategoryAndUnit();
		List<FCategory> listCate = fCategoryService.getAllCategory();
		boolean res = restaurantTableService.getTableStatusById(tableId);
		String tableName = restaurantTableService.getTableNameById(tableId);
		Long table = restaurantTableService.getTableIdById(tableId);
		BigDecimal total = cartService.calculateTotalForTable(tableName);
		List<CartLine> cartLines = cartService.listCartLines(tableName);
		List<CartLine> orderHistory = cartService.getOrderHistoryCheckout(tableName);
		BigDecimal total1 = BigDecimal.ZERO;
		for (CartLine order : orderHistory) {
			BigDecimal quantity = order.getQuantity();
			if (order.isHalfPortion()) {
				quantity = quantity.multiply(BigDecimal.valueOf(2));
			}
			total1 = total1.add(order.getPrice().multiply(quantity));
		}
		model.addAttribute("total1", total1);
		model.addAttribute("orderHistory", orderHistory);
		model.addAttribute("total", total);
		model.addAttribute("table", table);
		model.addAttribute("res", res);
		model.addAttribute("tableId", tableId);
		model.addAttribute("tableName", tableName);
		model.addAttribute("listCate", listCate);
		model.addAttribute("listIng", listIng);
		model.addAttribute("cartLines", cartLines);

		return "checkout";
	}

	@GetMapping("/orderList")
	public String viewOrderList(Model model, @RequestParam("tableId") Long tableId) {
		List<Ingredient> listIng = ingredientService.getAllIngredientWithCategoryAndUnit();
		List<FCategory> listCate = fCategoryService.getAllCategory();
		boolean res = restaurantTableService.getTableStatusById(tableId);
		String tableName = restaurantTableService.getTableNameById(tableId);
		Long table = restaurantTableService.getTableIdById(tableId);
		BigDecimal total = cartService.calculateTotalForTable(tableName);
		List<CartLine> cartLines = cartService.listCartLines(tableName);
		List<CartLine> orderHistory = cartService.getOrderHistoryCheckout(tableName);
		BigDecimal total1 = BigDecimal.ZERO;
		for (CartLine order : orderHistory) {
			BigDecimal quantity = order.getQuantity();
			if (order.isHalfPortion()) {
				quantity = quantity.multiply(BigDecimal.valueOf(2));
			}
			total1 = total1.add(order.getPrice().multiply(quantity));
		}
		model.addAttribute("total1", total1);
		model.addAttribute("orderHistory", orderHistory);
		model.addAttribute("total", total);
		model.addAttribute("table", table);
		model.addAttribute("res", res);
		model.addAttribute("tableId", tableId);
		model.addAttribute("tableName", tableName);
		model.addAttribute("listCate", listCate);
		model.addAttribute("listIng", listIng);
		model.addAttribute("cartLines", cartLines);

		return "orderList";
	}

	@PostMapping("/process")
	public String processCheckout(@RequestParam("fullname") String fullName, @RequestParam("email") String email,
			@RequestParam("pay-method") String paymentMethod, @RequestParam("tip-amount") double tips,
			@RequestParam("discount") double discount, @RequestParam("total") double total,
			@RequestParam("total1") double total1, @RequestParam("tax") double tax,
			@RequestParam("ingredientId") List<Long> ingredientIds, @RequestParam("tableId") Long tableId,
			@RequestParam("quantity") List<BigDecimal> quantities, @RequestParam("price") List<BigDecimal> prices,
			@RequestParam("halfPortion") List<Boolean> halfPortions, RedirectAttributes redirectAttributes,
			HttpServletRequest request) {
		if (email != null && !email.isEmpty() && discount != 0) {
			Customer customer = customerService.findByEmail(email);
			if (customer != null) {
				customer.setPoints(0);
				customerService.updateCustomer(customer);
			}
		}
		if (email == null || email.isEmpty()) {

		} else {
			customerService.updatePointsByEmail(email, total);
		}

		Order order = new Order();
		order.setFullName(fullName);
		order.setEmail(email);
		order.setTips(tips);
		order.setDiscount(discount);
		order.setTotal(total);
		order.setTotal1(total1);
		order.setTax(tax);
		order.setOrdertime(LocalDateTime.now());

		order.setPaymentMethod(paymentMethod);

		RestaurantTable restaurantTable = new RestaurantTable();
		restaurantTable.setId(tableId);
		order.setRestaurantTable(restaurantTable);

		List<OrderItem> orderItems = new ArrayList<>();
		for (int i = 0; i < ingredientIds.size(); i++) {
			Long ingredientId = ingredientIds.get(i);
			BigDecimal quantity = quantities.get(i);
			BigDecimal price = prices.get(i);
			Boolean halfPortion = halfPortions.get(i);
			Ingredient ingredient = ingredientRepository.findById(ingredientId)
					.orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientId));

			OrderItem orderItem = new OrderItem();
			orderItem.setIngredient(ingredient);
			orderItem.setQuantity(quantity);
			orderItem.setPrice(price);
			orderItem.setHalfPortion(halfPortion);
			orderItem.setOrder(order);

			orderItems.add(orderItem);
		}

		order.setOrderItems(orderItems);

		orderService.createOrder(order);

		for (OrderItem orderItem : orderItems) {
			orderItemService.createOrderItem(orderItem);
		}

		// tien

		LocalDateTime currentDateTime = LocalDateTime.now();
		LocalDateTime startOfDay = currentDateTime.toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = currentDateTime.toLocalDate().atTime(23, 59, 59);

		List<Income> existingIncome = incomeService.findByDayBetween(startOfDay, endOfDay);

		if (existingIncome.isEmpty()) {

			Income income = new Income();
			income.setDay(currentDateTime);

			List<IncomeItem> incomeItems = new ArrayList<>();

			for (int i = 0; i < ingredientIds.size(); i++) {
				Long ingredientId = ingredientIds.get(i);
				BigDecimal quantity = quantities.get(i);
				BigDecimal price = prices.get(i);
				Boolean halfPortion = halfPortions.get(i);
				Ingredient ingredient = ingredientRepository.findById(ingredientId)
						.orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientId));

				IncomeItem incomeItem = new IncomeItem();
				incomeItem.setIngredient(ingredient);
				BigDecimal soldQuantity = halfPortion ? quantity.multiply(BigDecimal.valueOf(2)) : quantity;
				incomeItem.setSoldQuantity(soldQuantity);
				incomeItem.setHalfPortion(halfPortion);
				incomeItem.setPrice(price);
				System.out.println("ingredient ID" + ingredient.getPrice());
				incomeItem.setIncome(income);

				incomeItems.add(incomeItem);

			}
			income.setIncomeItems(incomeItems);

			incomeService.save(income);

			for (IncomeItem incomeItem : incomeItems) {
				incomeItemService.save(incomeItem);
			}
		} else {

			Income incomeToUpdate = existingIncome.get(0);
			List<IncomeItem> incomeItems = incomeToUpdate.getIncomeItems();

			for (int i = 0; i < ingredientIds.size(); i++) {
				Long ingredientId = ingredientIds.get(i);
				BigDecimal quantity = quantities.get(i);
				BigDecimal price = prices.get(i);
				Boolean halfPortion = halfPortions.get(i);

				Ingredient ingredient = ingredientRepository.findById(ingredientId)
						.orElseThrow(() -> new RuntimeException("Ingredient not found with id: " + ingredientId));

				boolean found = false;
				for (IncomeItem incomeItem : incomeItems) {
					if (incomeItem.getIngredient().getId().equals(ingredientId)) {
						incomeItem.setSoldQuantity(incomeItem.getSoldQuantity().add(quantity));
						found = true;
						break;
					}
				}

				if (!found) {
					IncomeItem incomeItem = new IncomeItem();
					incomeItem.setIngredient(ingredient);
					BigDecimal soldQuantity = halfPortion ? quantity.multiply(BigDecimal.valueOf(2)) : quantity;
					incomeItem.setSoldQuantity(soldQuantity);
					incomeItem.setHalfPortion(halfPortion);
					incomeItem.setPrice(price);
					incomeItem.setIncome(incomeToUpdate);
					incomeItems.add(incomeItem);
				}
			}

			incomeToUpdate.setIncomeItems(incomeItems);
			incomeService.save(incomeToUpdate);

			for (IncomeItem incomeItem : incomeItems) {
				incomeItemService.save(incomeItem);
			}
		}
		List<OrderItem> orderItem = order.getOrderItems();
		double orderTotal = order.getTotal();
		double orderDiscount = order.getDiscount();
		int customerPoints = customerService.calculateCustomerPoints(email);
		sendPointsInfoEmail(email, customerPoints, orderItem, orderTotal, orderDiscount, tips, total1, tax);

		if ("paypal".equals(paymentMethod)) {
			String cancelUrl = AppHelper.getSiteURL(request) + "/" + PAYPAL_CANCEL_URL;
			String successUrl = AppHelper.getSiteURL(request) + "/" + PAYPAL_SUCCESS_URL + "?email=" + email;
			try {
				Payment payment = paypalService.createPayment(total, "USD", PaypalPaymentMethod.PAYPAL,
						PaypalPaymentIntent.ORDER, "payment description", cancelUrl, successUrl);
				for (Links links : payment.getLinks()) {
					if (links.getRel().equals("approval_url")) {

						return "redirect:" + links.getHref();
					}
				}
			} catch (PayPalRESTException e) {
				System.out.println(e.getMessage());
				log.error(e.getMessage());
			}
		}
		restaurantTableService.checkoutTable(tableId);

		return "redirect:/success?email=" + email;
	}

	private void sendPointsInfoEmail(String email, int customerPoints, List<OrderItem> orderItems, double orderTotal,
			double orderDiscount, double tips, double total1, double tax) {
		String subject = "Your Reward Points Information and Order Invoice";

		Map<String, Object> templateModel = new HashMap<>();
		templateModel.put("points", customerPoints);
		templateModel.put("orderItems", orderItems);
		templateModel.put("orderDiscount", orderDiscount);
		templateModel.put("total", orderTotal);
		templateModel.put("tips", tips); // Thêm thông tin tips vào template model
		templateModel.put("total1", total1); // Thêm thông tin total1 vào template model
		templateModel.put("tax", tax); // Thêm thông tin tax vào template model

		emailService.sendHtmlMessage(email, subject, templateModel, "points-info-template");
	}
	
	

	@GetMapping("/remove")
	@ResponseBody
	public String removeCartItem(@RequestParam("ingredientId") Long ingredientId,
			@RequestParam("tableName") String tableName, @RequestParam("isHalfPortion") boolean isHalfPortion) {
		cartService.removeCartItem(ingredientId, tableName, isHalfPortion);
		return "Success";
	}

	@PostMapping("/place-order")
	public String placeOrder(@RequestParam("ingredientId") List<Long> ingredientIds,
			@RequestParam("tableId") List<Long> tableIds, @RequestParam("quantity") List<BigDecimal> quantities,
			@RequestParam("price") List<BigDecimal> prices, @RequestParam("halfPortion") List<Boolean> halfPortions,
			@RequestParam("tableName") List<String> tableNames, RedirectAttributes redirectAttributes) {

		try {
			if (ingredientIds.size() != tableIds.size() || ingredientIds.size() != quantities.size()
					|| ingredientIds.size() != prices.size() || ingredientIds.size() != tableNames.size()
					|| ingredientIds.size() != halfPortions.size()) {

				redirectAttributes.addFlashAttribute("error", "Dữ liệu không hợp lệ.");
				return "redirect:/menu?tableId=" + tableIds.get(0);
			}

			for (int i = 0; i < ingredientIds.size(); i++) {
				Long ingredientId = ingredientIds.get(i);
				Long tableId = tableIds.get(i);
				BigDecimal quantity = quantities.get(i);
				BigDecimal price = prices.get(i);
				Boolean halfPortion = halfPortions.get(i);
				String tableName = tableNames.get(i);
				CartLine cartLine = cartService.createCartLine(ingredientId, tableId, quantity, price, halfPortion,
						true, tableName);

				if (cartLine == null) {
					redirectAttributes.addFlashAttribute("error", "Dữ liệu không hợp lệ.");
					return "redirect:/menu?tableId=" + tableIds.get(0);
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xử lý đơn đặt hàng.");
			return "redirect:/menu?tableId=" + tableIds.get(0);
		}

		return "redirect:/menu?tableId=" + tableIds.get(0);
	}

	@GetMapping("/search-customer")
	@ResponseBody
	public Customer searchCustomerByEmail(@RequestParam("email") String email) {

		Customer customer = customerService.findByEmail(email);
		return customer;
	}

	@GetMapping("/removei")
	public String removeIngredient(@RequestParam("ingredientId") Long ingredientId,
			@RequestParam("tableId") Long tableId, @RequestParam("quantity") BigDecimal quantity,
			@RequestParam("price") BigDecimal price, @RequestParam("tableName") String tableName, Model model) {
		cartService.removeIngredient(ingredientId, tableId, quantity, price, tableName, false, false);
		return "redirect:/menu?tableId=" + tableId;
	}

	@GetMapping("/success")
	public String success(Model model, @RequestParam("email") String email) {

		Customer customer = customerService.findByEmail(email);

		if (customer == null) {

			return "success";
		} else {

			Integer pointsEarned = customer.getPoints();

			model.addAttribute("customer", customer);
			model.addAttribute("pointsEarned", pointsEarned);

			return "success";
		}

	}

	@GetMapping("/checkin")
	public String checkinTable(@RequestParam("tableId") Long tableId) {
		restaurantTableService.checkinTable(tableId);
		return "redirect:/menu?tableId=" + tableId;
	}

	@GetMapping("/table")
	public String viewTablePage(Model model, Authentication authentication) {
		List<RestaurantTable> listtable = restaurantTableService.getAllIngredientWithCategoryAndUnit();
		List<TCategory> listCate = tCategoryService.getAllCategory();
		model.addAttribute("listtable", listtable);
		model.addAttribute("listCate", listCate);
		return "table";
	}

	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/";
	}

	@GetMapping("/register")
	public String viewRegisterPage(Model model) {
		model.addAttribute("customer", new Customer());
		return "register";
	}

	@PostMapping("/register")
	public String createCustomer(@Valid Customer customer, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "register";
		}
		customerService.registerCustomer(customer);
		return "redirect:/login";
	}

	// vinh
	@GetMapping("/add")
	public String addIngredient(@RequestParam("ingredientId") Long ingredientId, @RequestParam("tableId") Long tableId,
			@RequestParam("quantity") BigDecimal quantity, @RequestParam("price") BigDecimal price,
			@RequestParam("tableName") String tableName, Model model) {

		Map<String, Object> response = cartService.addIngredient(ingredientId, tableId, quantity, price, tableName,
				false, false, false);

		return "redirect:/menu?tableId=" + tableId;
	}

	@Autowired
	private EmailService emailService;
	@Autowired
	private ContactService contactService;

	@Autowired
	private PostService postService;
	@Autowired
	private SpringTemplateEngine templateEngine;

	@GetMapping("/contact")
	public String viewContactPage(Model model) {
		model.addAttribute("contact", new Contact());
		return "contact";
	}

	@PostMapping("/contact")
	public String createContact(@Valid Contact contact, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes, @RequestParam("g-recaptcha-response") String recaptchaResponse) {
		if (bindingResult.hasErrors()) {
			return "contact";
		}

		// Verify reCAPTCHA
		String url = "https://www.google.com/recaptcha/api/siteverify";

		String params = "?secret=6LfFmbkpAAAAACvDRqFi8WkQSr3evdxb0FwdJ41L&response=" + recaptchaResponse;

		RestTemplate restTemplate = new RestTemplate();
		String result = restTemplate.postForObject(url + params, null, String.class);
		if (!result.contains("\"success\": true")) {
			model.addAttribute("message", "Captcha invalid!");
			return "contact";
		}

		contact.setCreatedTime(LocalDateTime.now());
		Contact savedContact = contactService.registerContact(contact);

		// Prepare data for the template
		Map<String, Object> templateModel = Map.of("name", savedContact.getFullName(), "email", savedContact.getEmail(),
				"phone", savedContact.getPhone(), "message", savedContact.getMessage());

		// Send email notification to admin
		emailService.sendHtmlMessage("vinhdiesel92025@gmail.com", "[VENUS Restaurant]_Messages from Customers",
				templateModel, "email/adminNotification");

		redirectAttributes.addFlashAttribute("message",
				"Your message has been successfully sent. Thank you for contacting us!");
		return "redirect:/contact";
	}

	@GetMapping("/blog")
	public String listUnarchivedPosts(Model model, @RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size) {
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(6);

		Page<Post> postPage = postService.findUnarchivedPostsSorted(PageRequest.of(currentPage - 1, pageSize));

		model.addAttribute("blogPage", postPage);

		int totalPages = postPage.getTotalPages();
		if (totalPages > 0) {
			List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		return "blog";
	}

	@GetMapping("/blog-single")
	public String postSingle(Model model, @RequestParam("id") Optional<Long> id) {
		long idPost = id.orElse(1L);
		try {
			Post post = postService.findById(idPost);
			model.addAttribute("blogsingle", post);
		} catch (PostNotFoundException e) {

		}
		return "blog-single";
	}

	@GetMapping("/about")
	public String aboutPage() {
		return "about"; // Tên của file template Thymeleaf (without the .html extension)
	}

	//vinh
	@GetMapping("/")
	public String viewHomePage(Model model, Authentication authentication) {
	    // Use PageRequest to limit the number of ingredients to 8
	    Page<Ingredient> page = ingredientService.getAllIngredientsWithPagination(PageRequest.of(0, 10));
	    List<Ingredient> listIng = page.getContent(); // Get the content from the page
	    model.addAttribute("listIng", listIng);

	    // Fetch recent blog posts
	    Page<Post> recentPosts = postService.findUnarchivedPostsSorted(PageRequest.of(0, 3)); // Fetch 3 recent posts
	    model.addAttribute("posts", recentPosts.getContent());

	    return "index";
	}
	// end vinh
//hao
	@GetMapping("/user_detail")
	public String viewUserDetail(Model model, @AuthenticationPrincipal AppUserDetails loggedUser) {
		Long CusId = loggedUser.getId();
		Optional<Customer> cus = customerRepository.findById(CusId);

		// Truyền thông tin người dùng vào model để hiển thị trên trang HTML
		model.addAttribute("cus", cus);

		// Trả về tên của trang HTML user_detail
		return "user_detail";
	}

	@GetMapping("/forgot_password")
	public String showForgotPassword(Model model) {
		model.addAttribute("email", "Forgot Password");
		return "/authentications/forgot_password";
	}

}
