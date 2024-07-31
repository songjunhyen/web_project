package com.example.demo.vo;

import java.time.LocalDate;

import lombok.Data;

@Data
public class Admin {
    private int id; // 자동 증가
    private String adminId; // SQL 쿼리와 일치
    private String adminPw; // SQL 쿼리와 일치
    private String name;
    private String email;
    private int adminclass = 1; // 관리자 등급
    private LocalDate regDate;

    public Admin(String adminId, String adminPw, String name, String email) {
        this.adminId = adminId;
        this.adminPw = adminPw;
        this.name = name;
        this.email = email;
        this.regDate = LocalDate.now();
    }
}