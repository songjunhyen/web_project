package com.example.demo.vo;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private int id;
    private String title;
    private String body;
    private LocalDateTime regDate; // LocalDateTime을 사용하여 날짜와 시간을 처리
    private LocalDateTime updateDate; // updateDate는 수정 시 자동 업데이트
    private int viewcount;

    public Article(int id, String title, String body, String writer) {
    	this.id = id;
        this.title = title;
        this.body = body;
        this.regDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
        this.viewcount = 0;
    }
}