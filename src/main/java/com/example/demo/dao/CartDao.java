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
			    SELECT * FROM cart WHERE userid = #{userid}
			""")
	public List<Cart> GetCartList(String userid);

	@Insert("""
			    INSERT INTO cart (userid, productid, productname, color, size, count, price)
			    VALUES (#{userid}, #{productid}, #{name}, #{color}, #{size}, #{count}, #{price})
			""")
	public void AddCartList(String userid, int productid, String name, String color, String size, int count, int price);

	@Insert("""
			    INSERT INTO cart (userid, productid, productname, color, size, count, price)
			    VALUES (#{userid}, #{productid}, #{productname}, #{color}, #{size}, #{count}, #{price})
			""")
	public void insertCart(String userid, int productid, String productname, String color, String size, int count,
			int price);

	@Update("""
			    UPDATE cart
			    SET count = #{count}
			    WHERE userid = #{userid} AND productid = #{productid} AND color = #{color} AND size = #{size}
			""")
	void updateCount(String userid, int productid, String color, String size, int count);

	@Delete("""
			    DELETE FROM cart
			    WHERE id = #{id} AND userid = #{userid} AND productid = #{productid} AND color = #{color} AND size = #{size}
			""")
	public void DeleteCartList(int id, String userid, int productid, String color, String size);

	@Select("""
			    SELECT COUNT(*) FROM cart
			    WHERE userid = #{userid}
			      AND productid = #{productid}
			      AND color = #{color}
			      AND size = #{size}
			""")
	public int checking(String userid, int productid, String color, String size);

	@Select("""
			    SELECT id FROM cart
			    WHERE userid = #{userid}
			      AND productid = #{productid}
			      AND productname = #{productname}
			      AND color = #{color}
			      AND size = #{size}
			""")
	public int GetCartId(String userid, int productid, String productname, String color, String size);

	@Update("""
			    UPDATE cart
			    SET color = #{color}
			    WHERE id = #{id} AND userid = #{userid} AND productid = #{productid} AND size = #{size}
			""")
	public void updateColor(int id, String userid, int productid, String color, String size);

	@Update("""
			    UPDATE cart
			    SET size = #{size}
			    WHERE id = #{id} AND userid = #{userid} AND productid = #{productid} AND color = #{color}
			""")
	public void updateSize(int id, String userid, int productid, String color, String size);

	@Update("""
			    UPDATE cart
			    SET size = #{size}, color = #{color}
			    WHERE id = #{id} AND userid = #{userid} AND productid = #{productid}
			""")
	public void updateTwo(int id, String userid, int productid, String color, String size);
}