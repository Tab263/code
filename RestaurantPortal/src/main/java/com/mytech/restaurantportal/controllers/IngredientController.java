
package com.mytech.restaurantportal.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mytech.restaurantportal.helpers.AppConstant;
import com.mytech.restaurantportal.helpers.AppHelper;
import com.mytech.restaurantportal.storage.StorageService;
import com.restaurant.service.dtos.IngredientDTO;
import com.restaurant.service.entities.FCategory;
import com.restaurant.service.entities.Ingredient;
import com.restaurant.service.enums.IngredientStatus;
import com.restaurant.service.exceptions.ProductNotFoundException;
import com.restaurant.service.paging.PagingAndSortingHelper;
import com.restaurant.service.paging.PagingAndSortingParam;
import com.restaurant.service.services.FCategoryService;
import com.restaurant.service.services.IngredientService;

@Controller
@RequestMapping("/ingredient")
public class IngredientController {

	private String defaultRedirectURL = "redirect:/ingredient/page/1?sortField=ingredientCode&sortDir=asc";

	@Autowired
	private IngredientService ingredientService;

	@Autowired
	public FCategoryService fCategoryService;

	private final StorageService storageService;

	public IngredientController(StorageService storageService) {
		this.storageService = storageService;
	}

	@GetMapping("")
	public String getIngredientList(Model model) {
		List<Ingredient> listIng = ingredientService.getAllIngredient();

		List<FCategory> listCate = fCategoryService.getAllCategory();

		model.addAttribute("listIng", listIng);
		model.addAttribute("listCate", listCate);
		model.addAttribute("currentPage", "1");
		model.addAttribute("sortField", "ingredientCode");
		model.addAttribute("sortDir", "asc");
		model.addAttribute("reverseSortDir", "desc");
		model.addAttribute("searchText", "");
		model.addAttribute("moduleURL", "/ingredient");

		return "/apps/ingredient/list";
	}
	
	@PostMapping("/search")
	public List<Ingredient> search(@RequestParam("searchText") String searchText, Model model) {

		System.out.println("Search Employee: " + searchText);
		List<FCategory> listCate = fCategoryService.getAllCategory();
		List<Ingredient> listIngs;
		if (searchText == null || searchText.equals("")) {
			listIngs = ingredientService.getAllIngredient();
		} else {
			listIngs = ingredientService.search(searchText);
		}
		model.addAttribute("listIng", listIngs);
		model.addAttribute("listCate", listCate);
		model.addAttribute("currentPage", "1");
		model.addAttribute("sortField", "ingredientCode");
		model.addAttribute("sortDir", "asc");
		model.addAttribute("reverseSortDir", "desc");
		model.addAttribute("searchText", "");
		model.addAttribute("moduleURL", "/ingredient");

		return listIngs;
	}

	@GetMapping("/edit/{id}")

	public String edit(@PathVariable("id") Long id, Model model) throws ProductNotFoundException {

		List<FCategory> listCate = fCategoryService.getAllCategory();
		Optional<Ingredient> ingredientOptional = ingredientService.get(id);

		if (ingredientOptional.isPresent()) {
			Ingredient ingredient = ingredientOptional.get();
			model.addAttribute("ingredient", ingredient);
		}
		model.addAttribute("listCate", listCate);
		return "/apps/ingredient/edit";
	}

	@PostMapping("/update")
	public String updateIngredient(@RequestParam("ingredientName") String ingredientName,
			@RequestParam("price") BigDecimal price, @RequestParam("photo") MultipartFile photo,
			 @RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
		Ingredient ingredient = ingredientService.findById(id);
		if (!photo.isEmpty()) {
			String fileName = AppHelper.encode(ingredient.getIngredientName());
			ingredient.setPhoto(fileName);
			storageService.store(photo, fileName);

			List<String> files = storageService.loadAll()
					.map(path -> MvcUriComponentsBuilder
							.fromMethodName(PortalController.class, "serveFile", path.getFileName().toString()).build()
							.toUri().toString())
					.collect(Collectors.toList());

			for (String filename : files) {
				System.out.println("Uploaded file: " + filename);
			}
		}
		ingredientService.updateIngredient(ingredientName, price,id);
		redirectAttributes.addFlashAttribute("message", "The ingredient has been updated successfully.");
		return "redirect:/ingredient";
	}

	@GetMapping("/page")
	public String listByPage() {
		return defaultRedirectURL;
	}

	@GetMapping("/page/{pageNum}")
	public String listByPage(
			@PagingAndSortingParam(listName = "listIng", moduleURL = "/ingredient", defaultSortField = "ingredientCode") PagingAndSortingHelper helper,
			@PathVariable(name = "pageNum") int pageNum,
			@RequestParam(name = "sortField", defaultValue = "ingredientCode", required = false) String sortField,
			@RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		System.out
				.println("UserControllerlistByPage::" + pageNum + " sortField: " + sortField + " sortDir: " + sortDir);
		ingredientService.listIngByPage(pageNum, AppConstant.pageCount, helper);

		return "/apps/users/list";
	}

	@GetMapping("/add")
	public String add(Model model) {

		List<FCategory> listCate = fCategoryService.getAllCategory();

		Ingredient ingredient = new Ingredient();

		model.addAttribute("ingredient", ingredient);

		model.addAttribute("listCate", listCate);

		return "/apps/ingredient/add";
	}

	@PostMapping("/add")
	public String saveIngredient(@RequestParam(name = "ingredientName") String ingredientName,
			@RequestParam(name = "ingredientCode") String ingredientCode, @RequestParam("avatar") MultipartFile file,
			@RequestParam(name = "weight") BigDecimal weight, @RequestParam(name = "description") String description,
			@RequestParam(name = "halfPortionAvailable", required = false, defaultValue = "false") boolean halfPortionAvailable,
			@RequestParam(name = "categoryId") Long categoryId,
			@RequestParam(name = "quantityInStock") BigDecimal quantityInStock,
			@RequestParam(name = "price") BigDecimal price,
			@RequestParam(name = "status", required = false, defaultValue = "Available") IngredientStatus status) {
		System.out.println(
				"Ingredient save: " + ingredientName + " -- " + ingredientCode + " -- CategoryId: " + categoryId);

		IngredientDTO ingredientDTO = new IngredientDTO();
		ingredientDTO.setIngredientName(ingredientName);
		ingredientDTO.setIngredientCode(ingredientCode);
		if (!file.isEmpty()) {
			String fileName = AppHelper.encode(ingredientDTO.getIngredientName());
			ingredientDTO.setPhoto(fileName);
			storageService.store(file, fileName);

			List<String> files = storageService.loadAll()
					.map(path -> MvcUriComponentsBuilder
							.fromMethodName(PortalController.class, "serveFile", path.getFileName().toString()).build()
							.toUri().toString())
					.collect(Collectors.toList());

			for (String filename : files) {
				System.out.println("Uploaded file: " + filename);
			}
		}
		ingredientDTO.setHalfPortionAvailable(halfPortionAvailable);
		ingredientDTO.setWeight(weight);
		ingredientDTO.setDescription(description);
		ingredientDTO.setCategoryId(categoryId);
		ingredientDTO.setCategoryId(categoryId);
		ingredientDTO.setQuantityInStock(quantityInStock);
		ingredientDTO.setPrice(price);
		ingredientDTO.setStatus(status);
		ingredientService.saveIngredient(ingredientDTO);

		return "redirect:/ingredient";
	}


	//tien
	@PostMapping("/updateQuantity")
	public String updateQuantity(@RequestParam("ingredientId") Long ingredientId, @RequestParam("quantity") BigDecimal quantity) {

		System.out.println("Ingredient ID: " + ingredientId);
		ingredientService.updateQuantity(ingredientId, quantity);
		return "redirect:/ingredient";
	}

	@GetMapping("/delete/{id}")
	public String deleteDish(@PathVariable("id") Long id) throws ProductNotFoundException {
		ingredientService.delete(id);
		return "redirect:/ingredient";
	}
	@PostMapping("/updateStatus")
    @ResponseBody
    public ResponseEntity<?> updateStatus(@RequestParam("id") Long id, @RequestParam("status") String status) {
        try {
            IngredientStatus ingStatus = IngredientStatus.valueOf(status); // Chuyển đổi String sang enum
            ingredientService.updateIngredientStatus(id, ingStatus);
            return ResponseEntity.ok(Map.of("message", "Trạng thái đã được cập nhật thành công."));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Lỗi khi cập nhật trạng thái: " + e.getMessage()));
        }
	}
}
