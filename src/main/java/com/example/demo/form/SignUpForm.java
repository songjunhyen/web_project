package com.example.demo.form;
import lombok.Data;

@Data
public class SignUpForm {
    private String _csrf;
    private String userid;
    private String pw;
    private String name;
    private String email;
    private String address;

    // 기본 생성자
    public SignUpForm() {}

    // 모든 필드를 포함하는 생성자
    public SignUpForm(String _csrf,String userid, String pw, String name, String email, String address) {
    	 this._csrf = _csrf;
        this.userid = userid;
        this.pw = pw;
        this.name = name;
        this.email = email;
        this.address = address;
    }


}
