package com.xxy.seckill.seckillmanagement.service.model;

public class EnwordsModel {
    private Integer id;
    private String word;
    private String translation;
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word == null ? null : word.trim();
    }
    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation == null ? null : translation.trim();
    }
}
