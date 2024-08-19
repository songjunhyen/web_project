package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.PurchaseDao;
import com.example.demo.vo.NonMemberPurchaseInfo;
import com.example.demo.vo.PurchaseInfo;

@Service
public class PurchaseService {
	
	@Autowired
	private PurchaseDao purchaseDao;

	private String generateUniqueOrderNumber() {
		String orderNumber;
		do {
			orderNumber = UUID.randomUUID().toString().replaceAll("-", "");
		} while (isOrderNumberExists(orderNumber));
		return orderNumber;
	}

	private boolean isOrderNumberExists(String orderNumber) {
		return purchaseDao.countPurchaseByOrderNumber(orderNumber) > 0
				|| purchaseDao.countGuestPurchaseByOrderNumber(orderNumber) > 0;
	}

	public void requestPurchase(PurchaseInfo pinfo) {
		String orderNumber = generateUniqueOrderNumber();

		// 앞쪽 4자리를 분리하고 중간에 '-' 삽입
		orderNumber = orderNumber.substring(0, 4) + "-" + orderNumber.substring(4);

		pinfo.setOrderNumber(orderNumber);
		purchaseDao.requestPurchase(pinfo);
	}

	public void nonmemreqPurchase(NonMemberPurchaseInfo nPinfo) {
		String orderNumber = generateUniqueOrderNumber();

		// 앞쪽 5자리를 분리하고 중간에 '-' 삽입
		orderNumber = orderNumber.substring(0, 5) + "-" + orderNumber.substring(5);

		nPinfo.setOrderNumber(orderNumber);
		purchaseDao.nonmemreqPurchase(nPinfo);
	}
}