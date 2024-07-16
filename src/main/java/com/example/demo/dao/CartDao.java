package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Cart;


@Mapper
public interface CartDao {

	@Select("""
			SELECT * FROM cart WHERE userid = ${userid}
			""")
	public List<Cart> GetCartList(int userid);

	@Insert("""
			INSERT INTO cart (userid, productid, options, count)
            VALUES (#{userid}, #{productid},  #{options, #{count})
			""")
	public void AddCartList(int userid, int productid,String color, String size, int count);

	@Update("""
			UPDATE cart
			SET color = ${color},  size = ${size},   count = ${count}
			WHERE userid = ${userid} AND priductid = #{productid}
			""")
	public void ModifyCartList(int userid, int productid, String color, String size, int count);

	@Delete("""
			DELETE FROM cart
            WHERE userid = #{userid} AND priductid = #{productid} AND color = ${color} AND  size = ${size}
			""")
	public void DeleteCartList(int userid, int productid, String color, String size);
	

}
