package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.AllService;
import com.example.demo.service.ProductService;
import com.example.demo.util.ImageUtils;
import com.example.demo.util.SecurityUtils;
import com.example.demo.vo.Product;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

	private final ProductService productService;
	private final AllService allService;

	public ProductController(ProductService productService, AllService allService) {
		this.productService = productService;
		this.allService =  allService;
	}
	
	@GetMapping("/test/product/Main")
	public String mainPage() {
		return "product/main"; // "product/main.jsp"를 반환하도록 설정
	}

	@GetMapping("/product/add")
	public String write() {
		return "product/productadd"; // "product/productadd.jsp"를 반환하도록 설정
	}

	@GetMapping("/product/detail")
	public String detail(HttpSession session, @RequestParam int id, Model model, HttpServletRequest request,
			HttpServletResponse response) {		
		boolean result = productService.searchProduct(id);
		
		String userid = SecurityUtils.getCurrentUserId();
		String userRole = "";
		int adminClass = 5;
		if (userid != null && !userid.equals("Anonymous")) {
			userRole = allService.isuser(userid);
			if (userRole.equals("admin")) {
				adminClass = allService.getadminclass(userid);
			}
			model.addAttribute("userRole", userRole);
			model.addAttribute("adminClass", adminClass);
		}
		
		if (!result) {
			model.addAttribute("message", "오류가 발생하였습니다.");
			return "error";
		} else {
			String writerid = productService.getwriterid(id);

			Product product = productService.ProductDetail(id);

			if (writerid.equals(userid)) {
				model.addAttribute("product", product);
				return "product/productdetail";
			} else {
				Cookie[] cookies = request.getCookies();
				boolean shouldUpdateViewCount = true;
				LocalDateTime now = LocalDateTime.now();
				String productCookieName = "viewedProduct_" + id;

				if (cookies != null) {
					for (Cookie cookie : cookies) {
						if (cookie.getName().equals(productCookieName)) {
							// 쿠키에서 저장된 시간 추출
							LocalDateTime lastVisitTime = LocalDateTime.parse(cookie.getValue());
							if (Duration.between(lastVisitTime, now).toMinutes() <= 1440) { // 1일을 분 단위로 계산
								shouldUpdateViewCount = false;
							}
							// 쿠키 유효 시간 갱신
							cookie.setMaxAge(86400); // 1일 (24시간 × 60분 × 60초)
							cookie.setValue(now.toString());
							response.addCookie(cookie);
							break;
						}
					}
				}

				// 쿠키 설정: 제품 ID와 방문 시간을 저장
				if (shouldUpdateViewCount) {
					Cookie viewedCookie = new Cookie(productCookieName, now.toString());
					viewedCookie.setMaxAge(86400); // 쿠키 유효 시간 1일
					response.addCookie(viewedCookie);

					// 조회수 증가
					productService.updateViewCount(id);
				}
								
				model.addAttribute("product", product);
				return "product/productdetail";
			}
		}
	}

	@GetMapping("/product/list")
	public String list(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
		List<Product> products = productService.getProductlist();
		Collections.reverse(products);
		int pageSize = 10; // 한 페이지에 보여줄 게시물 수
		int totalCount = products.size();
		int totalPages = (int) Math.ceil((double) totalCount / pageSize);
		int start = (page - 1) * pageSize;
		int end = Math.min(start + pageSize, totalCount);

		List<Product> paginatedProducts = products.subList(start, end);

		model.addAttribute("products", paginatedProducts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		return "product/productlist"; // "product/productadd.jsp"를 반환하도록 설정
	}

	@GetMapping("/product/modify")
	public String modify(Model model, @RequestParam int id) {
		model.addAttribute("productId", id);
		return "product/productmodify"; // "product/productadd.jsp"를 반환하도록 설정
	}

    @PostMapping("/product/ADD")
    public String addProduct(HttpSession session, Model model, @RequestParam String name, @RequestParam int price,
                             @RequestParam String description, @RequestPart("imageFiles") MultipartFile[] imageFiles,
                             @RequestParam int count, @RequestParam String category, @RequestParam String maker,
                             @RequestParam String color, @RequestParam String size, @RequestParam String options) {

        String userid = SecurityUtils.getCurrentUserId();

        List<String> imageUrls = new ArrayList<>();
        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploadimg/";

        // 디렉토리 존재 여부 확인 및 생성
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        for (MultipartFile imageFile : imageFiles) {
            if (imageFile != null && !imageFile.isEmpty()) {
                String originalFileName = imageFile.getOriginalFilename();
                if (originalFileName != null && !originalFileName.isEmpty()) {
                    try {
                        originalFileName = new String(originalFileName.getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        model.addAttribute("errorMessage", "파일 이름 인코딩 처리에 실패했습니다.");
                    }

                    String fileExtension = "";
                    if (originalFileName.contains(".")) {
                        fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    }

                    String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                    String filePath = uploadDir + uniqueFileName;

                    try {
                        // 리사이징 적용
                        ImageUtils.resizeImage(imageFile, filePath, 1024, 768); // 원하는 크기로 조정

                        System.out.println("File uploaded to: " + filePath);  // 디버깅용
                        imageUrls.add("/uploadimg/" + uniqueFileName); // 웹에서 접근할 수 있는 경로
                    } catch (IOException e) {
                        e.printStackTrace();
                        model.addAttribute("errorMessage", "파일 업로드에 실패했습니다.");
                    }
                }
            }
        }

        String combinedImageUrls = String.join(",", imageUrls);
        Product product = new Product(0, userid, name, price, description, combinedImageUrls, count, category, maker, color,
                size, "");
        product.setAdditionalOptions(options);

        productService.addProduct(product);
        model.addAttribute("product", product);
        return "redirect:/Home/Main";
    }
    
    @PostMapping("/product/Detail")
    public String ProductDetail(HttpSession session, HttpServletRequest request, HttpServletResponse response, Model model, @RequestParam int id) {
        boolean result = productService.searchProduct(id);
        if (!result) {
            model.addAttribute("message", "제품 추가 중 오류가 발생하였습니다.");
            return "error";
        }

        String writerid = productService.getwriterid(id);
        String userid = SecurityUtils.getCurrentUserId();
        Product product = productService.ProductDetail(id);

        if (writerid.equals(userid)) {
            model.addAttribute("product", product);
            model.addAttribute("userid", userid);
            return "product/productdetail";
        }

        handleViewCountCookie(request, response, id);
        
		String userRole = "";
		int adminClass = 5;
		if (userid != null && !userid.equals("Anonymous")) {
			userRole = allService.isuser(userid);
			if (userRole.equals("admin")) {
				adminClass = allService.getadminclass(userid);
			}
			model.addAttribute("userRole", userRole);
			model.addAttribute("adminClass", adminClass);
		}
		
        model.addAttribute("product", product);
        return "product/productdetail";
    }

    private void handleViewCountCookie(HttpServletRequest request, HttpServletResponse response, int productId) {
        Cookie[] cookies = request.getCookies();
        boolean shouldUpdateViewCount = true;
        LocalDateTime now = LocalDateTime.now();
        String productCookieName = "viewedProduct_" + productId;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(productCookieName)) {
                    try {
                        LocalDateTime lastVisitTime = LocalDateTime.parse(cookie.getValue());
                        if (Duration.between(lastVisitTime, now).toMinutes() <= 1440) {
                            shouldUpdateViewCount = false;
                        }
                    } catch (DateTimeParseException e) {
                        // 날짜 형식 오류 처리
                        e.printStackTrace();
                    }
                    // 쿠키 유효 시간 갱신
                    updateCookie(response, productCookieName, now.toString(), 86400);
                    break;
                }
            }
        }

        // 쿠키 설정 및 조회수 증가
        if (shouldUpdateViewCount) {
            updateCookie(response, productCookieName, now.toString(), 86400);
            productService.updateViewCount(productId);
        }
    }

    private void updateCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(maxAge); // 쿠키 유효 시간 설정
        response.addCookie(cookie);
    }

	@PostMapping("/product/Modify")
	public String modifyProduct(HttpSession session, Model model, @RequestParam int productId,
			@RequestParam String name, @RequestParam int price, @RequestParam String description,
			@RequestParam int count, @RequestParam String category, @RequestParam String maker,
			@RequestParam String color, @RequestParam String size, @RequestParam List<String> options) {

		boolean result = productService.searchProduct(productId);

		if (!result) {
			model.addAttribute("message", "제품 수정 중 오류가 발생하였습니다.");
			return "error";
		} else {
			String userid = SecurityUtils.getCurrentUserId();
			Product product = new Product(0, userid, name, price, description, "", count, category, maker, color, size, "");
			productService.modifyProduct(productId, product);
			model.addAttribute("product", product);
			return "redirect:/product/detail?id=" + productId;
		}
	}

	@PostMapping("/product/Delete")
	public String deleteProduct(Model model, @RequestParam int id) {
		boolean result = productService.searchProduct(id);
		if (!result) {
			model.addAttribute("message", "제품 삭제 중 오류가 발생하였습니다.");
			return "error";
		} else {
			productService.deleteProduct(id);
			model.addAttribute("productId", id);
			return "redirect:/product/list";
		}
	}
}


