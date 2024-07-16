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
            INSERT INTO `admin` (regDate, userid, userpw, `name`, email, adminclass)
            VALUES (#{regDate}, #{adminid}, #{adminpw}, #{name}, #{email}, #{adminclass})
            """)
    void signup(Admin newAdmin);

    @Update("""
            UPDATE `admin`
            SET adminpw = #{pw}, `name` = #{name}, email = #{email}
            WHERE adminid = #{adminid}
            """)
    void modify(String adminid, String pw, String name, String email);

    @Delete("""
            DELETE FROM `admin`
            WHERE id = #{id}
            """)
    void signout(int id);

    @Select("""
            SELECT EXISTS(SELECT 1 FROM `admin` WHERE adminid = #{userid})
            """)
    boolean checkid(String userid);

    @Select("""
            SELECT EXISTS(SELECT 1 FROM `admin` WHERE adminid = #{userid} AND userpw = #{pw})
            """)
    boolean checkpw(String userid, String pw);

    @Select("""
    		SELECT id From `admin`
    		WHERE adminid = {$adminid} AND userpw = {$pw} 
    		""")    		
	int getid(String userid, String pw);
    
    @Select("""
            SELECT * FROM `admin` WHERE id = #{adminid} AND adminid = #{userid} AND adminpw = #{pw})
            """)
	Admin getadmin(int adminid, String userid, String pw);

    @Select("""
            SELECT * FROM `admin` WHERE email = #{email})
            """)
	Admin getbyemail(String email);
}