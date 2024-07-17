package com.example.demo.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Admin;

@Mapper
public interface AdminDao {

    @Insert("""
            INSERT INTO `admin` (regDate, adminid, adminpw, `name`, email, adminclass)
            VALUES (#{regDate}, #{adminid}, #{adminpw}, #{name}, #{email}, #{adminclass})
            """)
    void signup(Admin newAdmin);

    @Update("""
            UPDATE `admin`
            SET `name` = #{name}, adminpw = #{newpw}
            WHERE email = #{email}
            """)
    void modify(String newpw, String name, String email);

    @Delete("""
            DELETE FROM `admin`
            WHERE id = #{id} AND email = #{email}
            """)
    void signout(int id, String email); 

    @Select("""
            SELECT EXISTS(SELECT 1 FROM `admin` WHERE adminid = #{userid})
            """)
    boolean checkid(String userid);

    @Select("""
            SELECT EXISTS(SELECT 1 FROM `admin` WHERE adminid = #{userid} AND adminpw = #{pw})
            """)
    boolean checkpw(String userid, String pw);

    @Select("""
            SELECT id From `admin` WHERE adminid = #{adminid} AND adminpw = #{pw}
            """)            
    int getid(String adminid, String pw);

    @Select("""
            SELECT * FROM `admin` WHERE id = #{adminid} AND adminid = #{userid} AND adminpw = #{pw}
            """)
    Admin getadmin(int adminid, String userid, String pw);

    @Select("""
            SELECT * FROM `admin` WHERE email = #{email}
            """)
    Admin getbyemail(String email);
}