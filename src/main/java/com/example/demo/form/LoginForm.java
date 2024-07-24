package com.example.demo.form;

import lombok.Data;

@Data
public class LoginForm {
    private String userid;
    private String pw;
    private String _csrf;

    // 기본 생성자
    public LoginForm() {}

    // 모든 필드를 포함하는 생성자
    public LoginForm(String _csrf,String userid, String pw) {
    	 this._csrf = _csrf;
        this.userid = userid;
        this.pw = pw;
    }
}