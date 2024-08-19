package com.example.demo.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.PurchaseDao;
import com.example.demo.vo.NonMemberPurchaseInfo;
import com.example.demo.vo.PaymentInfo;
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

	public String Searchis(String orderNumber) {
		boolean isMember = purchaseDao.countMemberOrders(orderNumber) > 0;
		boolean isNonMember = purchaseDao.countNonMemberOrders(orderNumber) > 0;
		if (isMember) {
			return "Member";
		} else if (isNonMember) {
			return "NonMember";
		} else {
			return "Unknown";
		}

	}

	public PurchaseInfo getOrderInfoByPInfo(String orderNumber) {
		return purchaseDao.getOrderInfoByPInfo(orderNumber);
	}

	public NonMemberPurchaseInfo getOrderInfoByNInfo(String orderNumber) {
		return purchaseDao.getOrderInfoByNInfo(orderNumber);
	}

	public String getPaymentStatus(String orderNumber) {
		return purchaseDao.getPaymentStatus(orderNumber);
	}

	public void saveupPaymentInfo(PaymentInfo paymentInfo) {
		if (purchaseDao.getPayment(paymentInfo.getOrderNumber())>0) {
			String status = purchaseDao.getPaymentStatus(paymentInfo.getOrderNumber());
			if (!status.equals("COMPLETED")) {
				purchaseDao.updateStatus(paymentInfo.getOrderNumber());
			}
		} else {
			purchaseDao.insertStatus(paymentInfo);
		}

	}
}