package com.example.demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class test {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public test(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/usr/home/test")
    @ResponseBody
    public String testDatabaseConnection() {
        try {
            jdbcTemplate.execute("SELECT 1");
            return "<html><body><h1>데이터베이스 연결 성공!</h1></body></html>";
        } catch (Exception e) {
            return "<html><body><h1>데이터베이스 연결 실패 :(</h1></body></html>";
        }
    }
}