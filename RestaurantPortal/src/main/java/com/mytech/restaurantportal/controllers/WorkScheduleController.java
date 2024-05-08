package com.mytech.restaurantportal.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mytech.restaurantportal.security.AppUserDetails;
import com.nimbusds.oauth2.sdk.ParseException;
import com.restaurant.service.entities.Customer;
import com.restaurant.service.entities.StaffSchedule;
import com.restaurant.service.entities.TCategory;
import com.restaurant.service.entities.User;
import com.restaurant.service.entities.WorkSchedule;
import com.restaurant.service.exceptions.UserNotFoundException;
import com.restaurant.service.repositories.WorkScheduleRepository;
import com.restaurant.service.services.CustomerService;
import com.restaurant.service.services.TCategoryService;
import com.restaurant.service.services.UserService;
import com.restaurant.service.services.WorkScheduleService;

@Controller
@RequestMapping("/work")
public class WorkScheduleController {

	private String defaultRedirectURL = "redirect:/work/page/1?sortField=email&sortDir=asc";
	@Autowired
	private WorkScheduleService workScheduleService;
	@Autowired
	private UserService userService;
	@Autowired
	private CustomerService customerservice;
	@Autowired
	private TCategoryService tCategoryService;
	@Autowired
	private WorkScheduleRepository workScheduleRepository ;
	
	@GetMapping("/list")
	public String list(Model model) {
	    List<WorkSchedule> workSchedules = workScheduleService.findAllWorkSchedules();

	   
	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    workSchedules.forEach(schedule -> {
	        String reversedDate = LocalDate.parse(schedule.getWorkDate(), inputFormatter)
	                                      .format(outputFormatter);
	        schedule.setWorkDate(reversedDate);
	    });

	 
	    workSchedules = workSchedules.stream()
	        .sorted(Comparator.comparing(schedule -> LocalDate.parse(schedule.getWorkDate(), outputFormatter)))
	        .collect(Collectors.toList());

	  
	    Map<String, List<WorkSchedule>> workSchedulesMap = new LinkedHashMap<>();
	    for (WorkSchedule schedule : workSchedules) {
	        String workDate = schedule.getWorkDate();
	        if (!workSchedulesMap.containsKey(workDate)) {
	            workSchedulesMap.put(workDate, new ArrayList<>());
	        }
	        workSchedulesMap.get(workDate).add(schedule);
	    }

	    model.addAttribute("workSchedulesMap", workSchedulesMap);
	    model.addAttribute("currentPage", "1");
	    model.addAttribute("sortField", "email");
	    model.addAttribute("sortDir", "asc");
	    model.addAttribute("reverseSortDir", "desc");
	    model.addAttribute("searchText", "");
	    model.addAttribute("moduleURL", "/add");

	    return "/apps/work/list";
	}


	
	
	
	@GetMapping("/listStaff")
    public String listStaff(Model model) {
        List<StaffSchedule> workStaffSchedules = workScheduleService.findAllStaffWorkSchedules();
        
       
        Map<String, List<StaffSchedule>> workSchedulesMap = workStaffSchedules.stream()
                .collect(Collectors.groupingBy(StaffSchedule::getWorkDate));

		model.addAttribute("workSchedulesMap", workSchedulesMap);

      
        model.addAttribute("currentPage", "1");
        model.addAttribute("sortField", "email");
        model.addAttribute("sortDir", "asc");
        model.addAttribute("reverseSortDir", "desc");
        model.addAttribute("searchText", "");
        model.addAttribute("moduleURL", "/add");

        return "/apps/work/listStaff";
    }
	
	@GetMapping("/listwork")
	public String listchef(Model model, @AuthenticationPrincipal AppUserDetails loggedUser) {
	    Long userId = loggedUser.getId();
	    List<WorkSchedule> works = workScheduleRepository.findByUserIdOrderByWorkDateAsc(userId);
	   
	    model.addAttribute("works", works);
	    model.addAttribute("currentPage", "1");
	    model.addAttribute("sortField", "email");
	    model.addAttribute("sortDir", "asc");
	    model.addAttribute("reverseSortDir", "desc");
	    model.addAttribute("searchText", "");
	    model.addAttribute("moduleURL", "/add");

	    return "/apps/work/listwork";
	}

	
	
	@GetMapping("/addStaff")
	public String addStaff(Model model) {

		List<Customer> customer = customerservice.findAllStaffCustomers();
		List<TCategory>  tCategory =  tCategoryService.getAllCategory();

		model.addAttribute("customer", customer);
		model.addAttribute("tCategory",  tCategory);

		model.addAttribute("currentPage", "1");
		model.addAttribute("sortField", "email");
		model.addAttribute("sortDir", "asc");
		model.addAttribute("reverseSortDir", "desc");
		model.addAttribute("searchText", "");
		model.addAttribute("moduleURL", "/add");

		return "/apps/work/addStaff";
	}




	@GetMapping("/add")
	public String add(Model model) {

		List<User> user = userService.getAllChef();

		model.addAttribute("user", user);

		model.addAttribute("currentPage", "1");
		model.addAttribute("sortField", "email");
		model.addAttribute("sortDir", "asc");
		model.addAttribute("reverseSortDir", "desc");
		model.addAttribute("searchText", "");
		model.addAttribute("moduleURL", "/add");

		return "/apps/work/add";
	}

	@PostMapping("/schedule")
	public String saveAllWorkSchedules(@RequestParam(name = "userId", required = false) List<Long> userIds,
	        @RequestParam(name = "workDate", required = false) List<String> workDates,
	        @RequestParam(name = "startTime", required = false) List<String> startTimes,
	        @RequestParam(name = "endTime", required = false) List<String> endTimes,
	        @RequestParam(name = "startTime1", required = false) List<String> startTimes1,
	        @RequestParam(name = "endTime1", required = false) List<String> endTimes1,
	        @RequestParam(name = "note", required = false) List<String> notes, RedirectAttributes redirectAttributes)
	        throws UserNotFoundException, ParseException, java.text.ParseException {

	  
	    if (userIds.size() != workDates.size() || userIds.size() != startTimes.size()
	            || userIds.size() != endTimes.size() || userIds.size() != startTimes1.size()
	            || userIds.size() != endTimes1.size() || userIds.size() != notes.size()) {
	       
	    }

	    boolean hasStartTime1 = startTimes1 != null && !startTimes1.isEmpty();
	    boolean hasEndTime1 = endTimes1 != null && !endTimes1.isEmpty();
	    for (int i = 0; i < userIds.size(); i++) {
	    	 Long userId = userIds.get(i);
	         String workDateStr = workDates.get(i);
	         String startTimeStr = startTimes.get(i);
	         String endTimeStr = endTimes.get(i);
	         String startTime1Str = hasStartTime1 ? startTimes1.get(i) : null;
	         String endTime1Str = hasEndTime1 ? endTimes1.get(i) : null;
	         String note = (notes != null && !notes.isEmpty()) ? notes.get(i) : null;

	      
	        User user = userService.get(userId);

	       
	        WorkSchedule workSchedule = new WorkSchedule();
	        workSchedule.setUser(user);
	        workSchedule.setWorkDate(workDateStr);
	        workSchedule.setStartTime(startTimeStr);
	        workSchedule.setEndTime(endTimeStr);
	        workSchedule.setStartTime1(startTime1Str);
	        workSchedule.setEndTime1(endTime1Str);
	        workSchedule.setNote(note);

	      
	        List<WorkSchedule> savedWorkSchedules = workScheduleService.saveAll(Collections.singletonList(workSchedule));

	       
	        if (!savedWorkSchedules.isEmpty()) {
	            redirectAttributes.addFlashAttribute("message", "Work schedule saved successfully");
	        } else {
	            redirectAttributes.addFlashAttribute("error", "Failed to save work schedule");
	        }
	    }

	   
	    return "redirect:/work/list";
	}
	
	@PostMapping("/scheduleStaff")
	public String saveAllStafffWorkSchedules(@RequestParam(name = "userId", required = false) List<Long> userIds,
	        @RequestParam(name = "workDate", required = false) List<String> workDates,
	        @RequestParam(name = "cateId", required = false) List<Long> cateIds,
	        @RequestParam(name = "startTime", required = false) List<String> startTimes,
	        @RequestParam(name = "endTime", required = false) List<String> endTimes,
	        @RequestParam(name = "startTime1", required = false) List<String> startTimes1,
	        @RequestParam(name = "endTime1", required = false) List<String> endTimes1,
	        @RequestParam(name = "note", required = false) List<String> notes, RedirectAttributes redirectAttributes)
	        throws UserNotFoundException, ParseException, java.text.ParseException {

	  
	    if (userIds.size() != workDates.size() || userIds.size() != startTimes.size()
	            || userIds.size() != endTimes.size() || userIds.size() != startTimes1.size()
	            || userIds.size() != endTimes1.size() || userIds.size() != notes.size() || userIds.size() != cateIds.size() ) {
	       
	    }

	    boolean hasStartTime1 = startTimes1 != null && !startTimes1.isEmpty();
	    boolean hasEndTime1 = endTimes1 != null && !endTimes1.isEmpty();
	    for (int i = 0; i < userIds.size(); i++) {
	    	 Long userId = userIds.get(i);
	    	 Long cateId = cateIds.get(i);
	         String workDateStr = workDates.get(i);
	         String startTimeStr = startTimes.get(i);
	         String endTimeStr = endTimes.get(i);
	         String startTime1Str = hasStartTime1 ? startTimes1.get(i) : null;
	         String endTime1Str = hasEndTime1 ? endTimes1.get(i) : null;
	         String note = (notes != null && !notes.isEmpty()) ? notes.get(i) : null;

	      
	        Customer customer = customerservice.get(userId);
	        TCategory tCategory =  tCategoryService.getCategoryById(cateId);

	       
	        StaffSchedule  workSchedule = new StaffSchedule();
	        workSchedule.setCustomer(customer);
	        workSchedule.setCategory(tCategory);
	        
	        workSchedule.setWorkDate(workDateStr);
	        workSchedule.setStartTime(startTimeStr);
	        workSchedule.setEndTime(endTimeStr);
	        workSchedule.setStartTime1(startTime1Str);
	        workSchedule.setEndTime1(endTime1Str);
	        workSchedule.setNote(note);

	      
	        List<StaffSchedule> savedWorkSchedules = workScheduleService.saveStaff(Collections.singletonList(workSchedule));

	       
	        if (!savedWorkSchedules.isEmpty()) {
	            redirectAttributes.addFlashAttribute("message", "Work schedule saved successfully");
	        } else {
	            redirectAttributes.addFlashAttribute("error", "Failed to save work schedule");
	        }
	    }

	   
	    return "redirect:/work/listStaff";
	}
	
	

	
	@GetMapping("/listchef")
	public ResponseEntity<List<User>> getAllUsers() {
	    List<User> users = userService.getAllUsers();
	    return ResponseEntity.ok().body(users);
	}



}
