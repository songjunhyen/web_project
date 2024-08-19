package com.example.demo.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.demo.vo.NonMemberPurchaseInfo;
import com.example.demo.vo.PurchaseInfo;

@Mapper
public interface PurchaseDao {

    @Insert("""
            INSERT INTO PurchaseRecords (order_number, product_id, quantity, optionin, created_at)
            VALUES (#{orderNumber}, #{productid}, #{quantity}, #{sizecolor}, #{requestDate})
            """)
    void requestPurchase(PurchaseInfo pinfo);

    @Insert("""
            INSERT INTO GuestPurchaseRecords (order_number, product_id, quantity, optionin, created_at, guest_name, guest_email, guest_phone, guest_address)
            VALUES (#{orderNumber}, #{productid}, #{quantity}, #{sizecolor}, #{requestDate}, #{guestName}, #{email}, #{phonenum}, #{guestAddress})
            """)
    void nonmemreqPurchase(NonMemberPurchaseInfo nPinfo);
    
    @Select("""
            SELECT COUNT(*) FROM PurchaseRecords WHERE order_number = #{orderNumber}
            """)
    int countPurchaseByOrderNumber(String orderNumber);

    @Select("""
            SELECT COUNT(*) FROM GuestPurchaseRecords WHERE order_number = #{orderNumber}
            """)
    int countGuestPurchaseByOrderNumber(String orderNumber);
    
    
}