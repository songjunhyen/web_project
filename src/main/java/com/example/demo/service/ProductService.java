package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dao.ProductDao;
import com.example.demo.util.Util;
import com.example.demo.vo.Product;

@Service
public class ProductService {
	private ProductDao productDao;

	ProductService(ProductDao productDao) {
		this.productDao = productDao;
	}

	public void addProduct(Product product) {
		productDao.addProduct(product);
	}

	public boolean searchProduct(int productId) {
		if(!Util.isEmpty(productDao.searchProduct(productId))) {
			return true;
		} else {
			return false;
		}
	}

	public void modifyProduct(int productId, Product product) {
		productDao.modifyProduct(productId, product);
	}

	public void deleteProduct(int productId) {
		productDao.deleteProduct(productId);
	}

	public List<Product> getProductlist() {
		return productDao.getProductList();
	}

	public Product ProductDetail(int id) {
		return productDao.productDetail(id);
	}

	public String getwriterid(int id) {
		return productDao.getwriter(id);
	}

	public void updateViewCount(int id) {
		productDao.updateViewCount(id);
	}

}
