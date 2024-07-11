package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.demo.vo.Product;

@Mapper
public interface ProductDao {

    @Insert("""
        INSERT INTO Product (name, price, description, imageUrl, count, category, maker, color, size, regDate, viewcount)
        VALUES (#{name}, #{price}, #{description}, #{imageUrl}, #{count}, #{category}, #{maker}, #{color}, #{size}, #{regDate}, #{viewcount})
        """)
    void addProduct(Product product);
    
    @Select("""
        SELECT name FROM Product WHERE id = #{productId}
        """)
    String searchProduct(int productId);

    @Update("""
        UPDATE Product SET
            name = #{product.name},
            price = #{product.price},
            description = #{product.description},
            imageUrl = #{product.imageUrl},
            count = #{product.count},
            category = #{product.category},
            maker = #{product.maker},
            color = #{product.color},
            size = #{product.size},
            regDate = #{product.regDate},
            viewcount = #{product.viewcount}
        WHERE id = #{productId}
        """)
    void modifyProduct(int productId, Product product);
    
    @Delete("""
        DELETE FROM Product WHERE id = #{productId}
        """)
    void deleteProduct(int productId);

    @Select("""
        SELECT * FROM Product
        """)
    List<Product> getProductList();

    @Select("""
        SELECT * FROM Product WHERE id = #{id}
        """)
    Product productDetail(int id);
}