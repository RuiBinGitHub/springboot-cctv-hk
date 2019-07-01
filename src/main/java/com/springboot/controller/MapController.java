package com.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/map")
public class MapController {

	@RequestMapping(value = "/compare")
	public ModelAndView compare() {
		ModelAndView view = new ModelAndView();
		view.setViewName("map/compare");
		return view;
	}  
}
