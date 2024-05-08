package com.mytech.restaurantportal.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mytech.restaurantportal.helpers.AppConstant;
import com.restaurant.service.dtos.CartLineDTO;
import com.restaurant.service.enums.OrderStatus;
import com.restaurant.service.services.CartService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/apis/v1/chef")
public class ChefRestController {
	@Autowired
	private CartService cartService;

	@GetMapping("/dishes")

	public ResponseEntity<List<CartLineDTO>> getAllCartLines() {
		List<CartLineDTO> cartLines = cartService.listAllCartLines().stream().map(cartLine -> {
			CartLineDTO dto = new CartLineDTO();
			dto.setCartLineId(cartLine.getId()); 
			dto.setTableName(cartLine.getTableName());
			dto.setIngredientId(cartLine.getIngredient().getId());
			dto.setIngredientName(cartLine.getIngredient().getIngredientName()); // Thêm dòng này
			dto.setOrderTime(cartLine.getOrderTime());
			dto.setQuantity(cartLine.getQuantity());
			dto.setPrice(cartLine.getPrice());
			dto.setStatus(cartLine.getStatus());
			dto.setIngredientPhoto(AppConstant.imageUrl + cartLine.getIngredientPhoto()); 
			// nếu không muốn sử dụng hình ảnh
			dto.setHalfPortion(cartLine.isHalfPortion());
			return dto;
		}).collect(Collectors.toList());
		return ResponseEntity.ok(cartLines);
	}

	@PostMapping("/updateStatus")
	public ResponseEntity<String> updateCartLineStatus(@RequestBody CartLineDTO cartLineDTO) {
		Long cartLineId = cartLineDTO.getCartLineId(); // Sử dụng trường mới thêm vào
		OrderStatus status = cartLineDTO.getStatus();

		if (cartLineId == null || status == null) {
			return ResponseEntity.badRequest().body("Missing cartLineId or status");
		}

		try {
			cartService.updateCartLineStatus(cartLineId, status);
			return ResponseEntity.ok("Status updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Failed to update status: " + e.getMessage());
		}
	}



}
