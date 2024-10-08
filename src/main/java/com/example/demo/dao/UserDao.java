package com.example.demo.dao;

import java.util.List;

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
			VALUES (#{regdate}, #{userid}, #{userpw}, #{name}, #{email}, #{memberClass}, #{address})
			""")
	void signup(Member member);

	@Update("""
			UPDATE `user`
			SET userpw = #{pw}, `name` = #{name}, email = #{email}, address = #{address}
			WHERE userid = #{userid}
			""")
	void modify(String userid, String pw, String name, String email, String address);

	@Delete("""
			DELETE FROM `user`
			WHERE id = #{id}
			""")
	void signout(int id);

	@Select("SELECT * FROM `user` WHERE userid = #{userid}")
	Member findByUserid(String userid);

	@Select("""
			SELECT COUNT(*) FROM `user` WHERE userid = #{userid}
			""")
	int checkid(String userid);

	@Select("""
			SELECT COUNT(*) FROM `user` WHERE userid = #{userid} AND userpw = #{pw}
			""")
	int checkpw(String userid, String pw);

	@Select("""
			SELECT id FROM `user` WHERE userid = #{userid}
			""")
	int getid(String userid);

	@Select("""
			SELECT COUNT(*) FROM `user` WHERE userid = #{userid}
			""")
	int countByUserid(String userid);

	@Select("SELECT * FROM `user` WHERE email = #{email}")
	Member findByUserEmail(String email);

	@Insert("""
			INSERT INTO `user` (regDate, userid, userpw, `name`, email, class, address)
			VALUES (#{regdate}, #{userid}, #{userpw}, #{name}, #{email}, #{memberClass}, #{address})
			ON DUPLICATE KEY UPDATE
			userpw = VALUES(userpw),
			`name` = VALUES(`name`),
			email = VALUES(email),
			address = VALUES(address)
			""")
	void save(Member member);

	@Select("""
			    SELECT * FROM `user`
			    WHERE
			        (#{name} IS NULL OR name LIKE CONCAT('%', #{name}, '%'))
			        AND (#{email} IS NULL OR email LIKE CONCAT('%', #{email}, '%'))
			""")
	List<Member> searcUL(String name, String email);
	
	@Update("""
	        UPDATE `user`
	        SET userpw = #{newPassword}
	        WHERE userid = #{userid}
	    """)	
	void resetPassword(String userid, String newPassword);
}