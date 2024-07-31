package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.service.ReviewService;
import com.example.demo.util.SecurityUtils;
import com.example.demo.vo.Review;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {
	private ReviewService reviewService;

	ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@PostMapping("/Review/add")
	public String addReview(HttpSession session, @RequestParam int productId,
			@RequestParam String body, @RequestParam double star) {
		  String writer = SecurityUtils.getCurrentUserId();
		  
		reviewService.AddReview(writer, productId, body, star);
		return "redirect:/product/detail?id=" + productId;
	}

	@GetMapping("/Review/list")
	@ResponseBody
	public List<Review> getReviewList(@RequestParam int productid) {
	    List<Review> reviews = reviewService.ReviewList(productid);
	    return reviews;
	}

	@PostMapping("/Review/modify")
	public String ReviewModify(HttpSession session, Model model, @RequestParam int productid, @RequestParam int reviewid, @RequestParam String body) {
		String writer = SecurityUtils.getCurrentUserId();
		if (!reviewService.iswriter(writer)) {
			model.addAttribute("message", "권한이 없습니다.");
			return "redirect:/product/detail?id=" + productid;
		} else {
			reviewService.ReviewModify(writer, productid, reviewid, body);
			return "redirect:/product/detail?id=" + productid;
		}
	}

	@PostMapping("/Review/delete")
	public String ReviewDelete(HttpSession session, @RequestParam int productid, @RequestParam int reviewid) {
		String writer = SecurityUtils.getCurrentUserId();
		if (!reviewService.iswriter(writer)) {
			return "redirect:/product/detail?id=" + productid;
		} else {
			reviewService.ReviewDelete(writer, productid, reviewid);
			return "redirect:/product/detail?id=" + productid;
		}
	}

	@GetMapping("/Review/getstar")
	@ResponseBody
	public ResponseEntity<Double> GetAverStar(@RequestParam int productid) {
	    double averageStar = reviewService.GetAverStar(productid);
	    return ResponseEntity.ok().body(averageStar);
	}
}