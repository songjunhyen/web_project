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
			INSERT INTO `user` (regDate, userid, userpw, `name`, email, viplevel, class, address)
			VALUES (#{regdate}, #{userid}, #{userpw}, #{name}, #{email}, #{viplevel}, #{memberClass}, #{address})
			""")
	void signup(Member member);

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
			SELECT COUNT(*) FROM `user` WHERE userid = #{userid}
			""")
	int checkid(String userid);

	@Select("""
			SELECT COUNT(*) FROM `user` WHERE userid = #{userid} AND userpw = #{pw}
			""")
	int checkpw(String userid, String pw);

	@Select("""
			SELECT id FROM `user` WHERE userid = #{userid} AND userpw = #{pw}
			""")
	int getid(String userid, String pw);

}