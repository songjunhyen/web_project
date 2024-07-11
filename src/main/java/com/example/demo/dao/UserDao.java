package com.example.demo.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Member;

@Mapper
public interface UserDao {
    @Insert("""
            INSERT INTO `user` (regDate, userid, userpw, `name`, email, class, address)
            VALUES (#{newMember.regDate}, #{newMember.userid}, #{newMember.userpw}, #{newMember.name}, #{newMember.email}, #{newMember.class}, #{newMember.address})
            """)
    void signup(Member newMember);

    @Update("""
            UPDATE `user`
            SET userpw = #{pw}, `name` = #{name}, email = #{email}, address = #{address}
            WHERE id = #{id}
            """)
    void modify(int id, String pw, String name, String email, String address);

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