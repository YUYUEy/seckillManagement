package com.xxy.seckill.seckillmanagement.dataobject;

public class DictDAOKey {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column code_dict.id
     *
     * @mbg.generated Sun Jun 09 16:03:28 CST 2019
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column code_dict.catalog
     *
     * @mbg.generated Sun Jun 09 16:03:28 CST 2019
     */
    private String catalog;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column code_dict.value
     *
     * @mbg.generated Sun Jun 09 16:03:28 CST 2019
     */
    private String value;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column code_dict.id
     *
     * @return the value of code_dict.id
     *
     * @mbg.generated Sun Jun 09 16:03:28 CST 2019
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column code_dict.id
     *
     * @param id the value for code_dict.id
     *
     * @mbg.generated Sun Jun 09 16:03:28 CST 2019
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column code_dict.catalog
     *
     * @return the value of code_dict.catalog
     *
     * @mbg.generated Sun Jun 09 16:03:28 CST 2019
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column code_dict.catalog
     *
     * @param catalog the value for code_dict.catalog
     *
     * @mbg.generated Sun Jun 09 16:03:28 CST 2019
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog == null ? null : catalog.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column code_dict.value
     *
     * @return the value of code_dict.value
     *
     * @mbg.generated Sun Jun 09 16:03:28 CST 2019
     */
    public String getValue() {
        return value;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column code_dict.value
     *
     * @param value the value for code_dict.value
     *
     * @mbg.generated Sun Jun 09 16:03:28 CST 2019
     */
    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }
}