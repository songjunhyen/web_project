package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Admin;

@Mapper
public interface AdminDao {

    @Insert("""
            INSERT INTO `admin` (regDate, adminId, adminPw, `name`, email, adminclass)
            VALUES (#{regDate}, #{adminId}, #{adminPw}, #{name}, #{email}, #{adminclass})
            """)
    void signup(Admin newAdmin);

    @Update("""
            UPDATE `admin`
            SET `name` = #{name}, adminPw = #{newpw}
            WHERE email = #{email}
            """)
    void modify(String newpw, String name, String email);
    
    @Delete("""
            DELETE FROM `admin`
            WHERE id = #{id} AND email = #{email}
            """)
    void signout(int id, String email);

    @Select("""
            SELECT EXISTS(SELECT 1 FROM `admin` WHERE adminId = #{userid})
            """)
    boolean checkid(String userid);

    @Select("""
            SELECT adminPw FROM `admin` WHERE adminId = #{userid}
            """)
    String getHashedPassword(String userid);

    @Select("""
            SELECT id FROM `admin` WHERE adminId = #{adminId}
            """)
    int getid(String adminId);

    @Select("""
            SELECT * FROM `admin` WHERE id = #{adminid} AND adminId = #{userid}
            """)
    Admin getadmin(int adminid, String userid);

    @Select("""
            SELECT * FROM `admin` WHERE email = #{email}
            """)
    Admin getbyemail(String email);

    @Select("""
            SELECT adminclass FROM `admin` WHERE adminId = #{userid}
            """)
    int getadminclass(String userid);

    @Select("""
            SELECT * FROM `admin` WHERE adminid = #{username}
            """)
	Admin findByUserid(String username);

    @Select("""
            SELECT * FROM `admin` WHERE adminid = #{userid}
            """)
	Admin getbyuserid(String userid);

    @Select("""
            SELECT * FROM `admin` WHERE name = #{name}
            """)
	Admin getbyname(String name);

    @Select("""
            SELECT * FROM `admin`
            WHERE 
                (#{adminclass} IS NULL OR adminclass = #{adminclass})
                AND (#{name} IS NULL OR name LIKE CONCAT('%', #{name}, '%'))
                AND (#{email} IS NULL OR email LIKE CONCAT('%', #{email}, '%'))
            """)
    List<Admin> searcAL(String adminclass, String name, String email);

	@Update("""
	        UPDATE `admin`
		    SET adminid = #{newId},
		        adminpw = #{encodedPassword}
		    WHERE adminid = #{adminId}
			""")	
	void resetPassword(String adminId, String newId, String encodedPassword);  

}