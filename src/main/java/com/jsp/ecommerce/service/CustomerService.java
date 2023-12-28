package com.jsp.ecommerce.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.jsp.ecommerce.dao.CustomerDao;
import com.jsp.ecommerce.dao.ProductDao;
import com.jsp.ecommerce.dto.Customer;
import com.jsp.ecommerce.dto.Item;
import com.jsp.ecommerce.dto.PaymentDetails;
import com.jsp.ecommerce.dto.Product;
import com.jsp.ecommerce.dto.ShoppingCart;
import com.jsp.ecommerce.dto.ShoppingOrder;
import com.jsp.ecommerce.helper.AES;
import com.jsp.ecommerce.helper.EmailLogic;
import com.jsp.ecommerce.repository.PaymentDetailsRepository;
import com.jsp.ecommerce.repository.ShoppingOrderRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomerService {
	@Autowired
	CustomerDao customerDao;

	@Autowired
	ProductDao productDao;

	@Autowired
	EmailLogic emailLogic;

	@Autowired
	ShoppingOrderRepository orderRepository;

	@Autowired
	PaymentDetailsRepository detailsRepository;

	public String signup(Customer customer, ModelMap map) {
		// to check Email and Mobile is Unique
		List<Customer> exCustomers = customerDao.findByEmailOrMobile(customer.getEmail(), customer.getMobile());
		if (!exCustomers.isEmpty()) {
			map.put("fail", "Account Already Exists");
			return "Signup";
		} else {
			// Generating otp
			int otp = new Random().nextInt(100000, 999999);
			customer.setOtp(otp);
			// Encrypting password
			customer.setPassword(AES.encrypt(customer.getPassword(), "123"));
			customerDao.save(customer);
			// Send OTP to email
			emailLogic.sendOtp(customer);
			// Carrying id
			map.put("id", customer.getId());
			return "EnterOtp";
		}
	}

	public String verifyOtp(int id, int otp, ModelMap map) {
		Customer customer = customerDao.findById(id);
		if (customer.getOtp() == otp) {
			customer.setVerified(true);
			customerDao.update(customer);
			map.put("pass", "Account Created Succesfully");
			return "Login.html";
		} else {
			map.put("fail", "Invalid Otp, Try Again");
			map.put("id", id);
			return "EnterOtp";
		}
	}

	public String login(String emph, String password, ModelMap map, HttpSession session) {
		if (emph.equals("admin") && password.equals("admin")) {
			session.setAttribute("admin", "admin");
			map.put("pass", "Admin Login Success");
			return "AdminHome";
		} else {
			long mobile = 0;
			String email = null;
			try {
				mobile = Long.parseLong(emph);
			} catch (NumberFormatException e) {
				email = emph;
			}

			List<Customer> customers = customerDao.findByEmailOrMobile(email, mobile);
			if (customers.isEmpty()) {
				map.put("fail", "Invalid Email or Mobile");
				return "Login.html";
			} else {
				Customer customer = customers.get(0);
				if (AES.decrypt(customer.getPassword(), "123").equals(password)) {
					if (customer.isVerified()) {
						session.setAttribute("customer", customer);
						map.put("pass", "Login Success");
						return "CustomerHome";
					} else {
						int otp = new Random().nextInt(100000, 999999);
						customer.setOtp(otp);
						customerDao.save(customer);
						// Send OTP to email
						emailLogic.sendOtp(customer);
						// Carrying id
						map.put("fail", "Verify First");
						map.put("id", customer.getId());
						return "EnterOtp";
					}
				} else {
					map.put("fail", "Invalid Password");
					return "Login.html";
				}
			}
		}
	}

	public String fetchProducts(ModelMap map, Customer customer) {
		List<Product> products = productDao.fetchDisplayProducts();
		if (products.isEmpty()) {
			map.put("fail", "No Products Present");
			return "CustomerHome";
		} else {

			if (customer.getCart() == null)
				map.put("items", null);
			else {
				map.put("items", customer.getCart().getItems());
			}

			map.put("products", products);
			return "CustomerViewProduct";
		}
	}

	public String addToCart(Customer customer, int id, ModelMap map, HttpSession session) {
		Product product = productDao.findById(id);

		ShoppingCart cart = customer.getCart();
		if (cart == null)
			cart = new ShoppingCart();

		if ((cart.getTotalAmount() + product.getPrice()) < 100000) {
			List<Item> items = cart.getItems();
			if (items == null)
				items = new ArrayList<Item>();

			if (product.getStock() > 0) {
				boolean flag = true;
				// if item Already Exists in cart
				for (Item item : items) {
					if (item.getName().equals(product.getName())) {
						flag = false;
						item.setQuantity(item.getQuantity() + 1);
						item.setPrice(item.getPrice() + product.getPrice());
						break;
					}
				}
				if (flag) {
					// If item is New in cart
					Item item = new Item();
					item.setCategory(product.getCategory());
					item.setName(product.getName());
					item.setPicture(product.getPicture());
					item.setPrice(product.getPrice());
					item.setQuantity(1);
					items.add(item);
				}
				cart.setItems(items);
				cart.setTotalAmount(cart.getItems().stream().mapToDouble(x -> x.getPrice()).sum());
				customer.setCart(cart);
				customerDao.save(customer);
				// updating stock
				product.setStock(product.getStock() - 1);
				productDao.save(product);
				session.setAttribute("customer", customer);
				map.put("pass", "Product Added to Cart");
				return fetchProducts(map, customer);
			} else {
				map.put("fail", "Out of stock");
				return fetchProducts(map, customer);
			}
		} else {
			map.put("fail", "Out of Transaction Limit");
			return fetchProducts(map, customer);
		}
	}

	public String viewCart(Customer customer, ModelMap map) {
		ShoppingCart cart = customer.getCart();
		if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
			map.put("fail", "No Items in Cart");
			return "CustomerHome";
		} else {
			map.put("cart", cart);
			return "ViewCart";
		}
	}

	public String removeFromCart(Customer customer, int id, ModelMap map, HttpSession session) {
		Product product = productDao.findById(id);

		ShoppingCart cart = customer.getCart();
		if (cart == null) {
			map.put("fail", "Item not in Cart");
			return fetchProducts(map, customer);
		} else {
			List<Item> items = cart.getItems();
			if (items == null || items.isEmpty()) {
				map.put("fail", "Item not in Cart");
				return fetchProducts(map, customer);
			} else {
				Item item = null;
				for (Item item2 : items) {
					if (item2.getName().equals(product.getName())) {
						item = item2;
						break;
					}
				}
				if (item == null) {
					map.put("fail", "Item not in Cart");
					return fetchProducts(map, customer);
				} else {
					if (item.getQuantity() > 1) {
						item.setQuantity(item.getQuantity() - 1);
						item.setPrice(item.getPrice() - product.getPrice());
					} else {
						items.remove(item);
					}
				}
				cart.setItems(items);
				cart.setTotalAmount(cart.getItems().stream().mapToDouble(x -> x.getPrice()).sum());
				customer.setCart(cart);
				customerDao.save(customer);

				// updating stock
				product.setStock(product.getStock() + 1);
				productDao.save(product);

				if (item != null && item.getQuantity() == 1)
					productDao.deleteItem(item);

				session.setAttribute("customer", customer);
				map.put("pass", "Product Removed from Cart");
				return fetchProducts(map, customer);
			}
		}
	}

	public String createOrder(Customer customer, ModelMap map) throws RazorpayException {
		RazorpayClient client = new RazorpayClient("rzp_test_S6TGBrvbUykMqU", "Ps62zRWlFHl45Z9VXPzMN8u8");

		JSONObject object = new JSONObject();
		object.put("amount", customer.getCart().getTotalAmount() * 100);
		object.put("currency", "INR");

		Order order = client.orders.create(object);

		PaymentDetails details = new PaymentDetails();
		details.setAmount(customer.getCart().getTotalAmount());
		details.setCurrency(order.get("currency"));
		details.setDescription("Shopping Cart Payment for the products");
		details.setImage(
				"https://www.shutterstock.com/image-vector/mobile-application-shopping-online-on-260nw-1379237159.jpg");
		details.setKeyCode("rzp_test_S6TGBrvbUykMqU");
		details.setName("Ecommerce Shopping");
		details.setOrder_id(order.get("id"));
		details.setStatus("created");

		detailsRepository.save(details);

		map.put("details", details);
		map.put("customer", customer);
		return "PaymentPage";
	}

	public String completeOrder(int id, String razorpay_payment_id, Customer customer, ModelMap map) {
		PaymentDetails details = detailsRepository.findById(id).orElse(null);
		details.setPayment_id(razorpay_payment_id);
		details.setStatus("success");
		detailsRepository.save(details);

		ShoppingOrder order = new ShoppingOrder();
		order.setAmount(details.getAmount());
		order.setCustomer(customer);
		order.setDateTime(LocalDateTime.now());
		order.setOrder_id(details.getOrder_id());
		order.setPayment_id(razorpay_payment_id);
		order.setItems(customer.getCart().getItems());

		orderRepository.save(order);

		customer.getCart().setItems(null);
		customerDao.save(customer);

		map.put("pass", "Payment Complete");
		return "CustomerHome";
	}

	public String fetchAllorder(Customer customer, ModelMap map) {
		List<ShoppingOrder> orders = orderRepository.findByCustomer(customer);
		if (orders.isEmpty()) {
			map.put("fail", "No Orders Placed Yet");
			return "CustomerHome";
		} else {
			map.put("orders", orders);
			return "ViewOrders";
		}
	}

	public String fetchAllorderItems(int id, Customer customer, ModelMap map) {
		ShoppingOrder order = orderRepository.findById(id).orElse(null);
		map.put("order", order);
		return "ViewOrderItems";
	}

}
