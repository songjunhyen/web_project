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
            INSERT INTO `user` (regDate, userid, userpw, `name`, email, class)
            VALUES (#{newAdmin.regDate}, #{newAdmin.userid}, #{newAdmin.userpw}, #{newAdmin.name}, #{newAdmin.email}, #{newAdmin.class})
            """)
    void signup(Admin newAdmin);

    @Update("""
            UPDATE `user`
            SET userpw = #{pw}, `name` = #{name}, email = #{email}
            WHERE id = #{id}
            """)
    void modify(int id, String pw, String name, String email);

    @Delete("""
            DELETE FROM `user`
            WHERE id = #{id}
            """)
    void signout(int id);

    @Select("""
            SELECT EXISTS(SELECT 1 FROM `user` WHERE userid = #{userid})
            """)
    boolean checkid(String userid);

    @Select("""
            SELECT EXISTS(SELECT 1 FROM `user` WHERE userid = #{userid} AND userpw = #{pw})
            """)
    boolean checkpw(String userid, String pw);

    @Select("""
    		SELECT id From `user`
    		WHERE userid = {$userid} AND userpw = {$pw} 
    		""")    		
	int getid(String userid, String pw);

}