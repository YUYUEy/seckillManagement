package com.xxy.seckill.seckillmanagement.dao;

import com.xxy.seckill.seckillmanagement.dataobject.ItemDAO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ItemDAOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat May 04 11:21:13 CST 2019
     */
    int deleteByPrimaryKey(Integer id);

    int deleteItem(@Param("idList")String[] idList);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat May 04 11:21:13 CST 2019
     */
    int insert(ItemDAO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat May 04 11:21:13 CST 2019
     */
    int insertSelective(ItemDAO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat May 04 11:21:13 CST 2019
     */
    ItemDAO selectByPrimaryKey(Integer id);

    List<ItemDAO> listItem(@Param("title")String title);

    Integer listItemCount(@Param("title")String title);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat May 04 11:21:13 CST 2019
     */
    int updateByPrimaryKeySelective(ItemDAO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item
     *
     * @mbg.generated Sat May 04 11:21:13 CST 2019
     */
    int updateByPrimaryKey(ItemDAO record);

    int increaseSales(@Param("id")Integer id, @Param("amount")Integer amount);
}