package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dao.ReviewDao;
import com.example.demo.vo.Review;

@Service
public class ReviewService {
	private ReviewDao reviewDao;
	
	ReviewService(ReviewDao reviewDao){
		this.reviewDao = reviewDao;
	}

	public void AddReview(String writer, int productid, String body, double star) {		
		reviewDao.AddReview(writer, productid, body, star);
	}

	public List<Review> ReviewList(int productid) {
		return reviewDao.ReviewList(productid);
	}

	public void ReviewModify(String writer, int productid, int reviewid, String body) {
		reviewDao.ReviewModify(writer, productid, reviewid, body);
	}

	public void ReviewDelete(String writer, int productid, int reviewid) {
		reviewDao.ReviewDelete(writer, productid, reviewid);
	}

	public double GetAverStar(int productid) {
		return reviewDao.GetAverStar(productid);
	}

	public boolean iswriter(String writer) {
		boolean iswrite = false;
		if(reviewDao.iswriter(writer)) {
			iswrite= true;
		}
		return iswrite;
	}
}
