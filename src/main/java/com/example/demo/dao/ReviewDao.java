package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Review;

@Mapper
public interface ReviewDao {

	@Insert("""
			    INSERT INTO review (writer, productid, reviewtext, star, regDate)
			    VALUES (#{writer}, #{productid}, #{body}, #{star}, NOW())
			""")
	void AddReview(String writer, int productid, String body, double star);

	@Select("""
			    SELECT * FROM review WHERE productid = #{productid}
			""")
	List<Review> ReviewList(int productid);

	@Update("""
			    UPDATE review
			    SET reviewtext = #{body}, regDate = NOW()
			    WHERE writer = #{writer} AND productid = #{productid} AND id = #{reviewid}
			""")
	void ReviewModify(String writer, int productid, int reviewid, String body);

	@Delete("""
			    DELETE FROM review
			    WHERE writer = #{writer} AND productid = #{productid} AND id = #{reviewid}
			""")
	void ReviewDelete(String writer, int productid, int reviewid);

	@Select("""
			    SELECT AVG(star) FROM review WHERE productid = #{productid}
			""")
	Double GetAverStar(int productid);

	@Select("""
			    SELECT COUNT(*) > 0 FROM review WHERE writer = #{writer}
			""")
	boolean iswriter(String writer);
}