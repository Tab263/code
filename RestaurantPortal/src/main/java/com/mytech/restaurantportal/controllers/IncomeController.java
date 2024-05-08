package com.mytech.restaurantportal.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.DocumentException;
import com.mytech.restaurantportal.exporters.IncomeExcelExport;
import com.restaurant.service.entities.Income;
import com.restaurant.service.services.IncomeService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/income")
public class IncomeController {

	@Autowired
	private IncomeService incomeService;

	@GetMapping("")
	public String getAllIncomes(Model model) {
		List<Income> incomes = incomeService.getAllIncomes();
		model.addAttribute("incomes", incomes);
		return "/apps/income/list";
	}

	@PostMapping("/export")
	public void export(HttpServletResponse response, @RequestParam(name = "format") String format,
			@RequestParam(name = "date") String date) throws IOException, DocumentException {
		System.out.println("Income export: " + format);
		System.out.println("Income export: " + date);
		List<Income> listIncome = incomeService.getAllIncomes();

		if ("excel".equals(format)) {
			IncomeExcelExport exporter = new IncomeExcelExport();
			exporter.export(listIncome, response);
		}
	}

	@GetMapping("/export/excel")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		List<Income> listIncome = incomeService.getAllIncomes();
		IncomeExcelExport exporter = new IncomeExcelExport();
		exporter.export(listIncome, response);
	}
}
