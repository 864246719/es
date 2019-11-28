package cn.fllday.es.entity;

import java.io.Serializable;

public class User implements Serializable {

    private Integer id;
    private String name;
    private Integer age;
    private String brith;
    private String sex;
    public User() {

    }

    public User(Integer id, String name, Integer age, String brith, String sex) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.brith = brith;
        this.sex = sex;
    }

    public User(String name, Integer age, String brith, String sex) {
        this.name = name;
        this.age = age;
        this.brith = brith;
        this.sex = sex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBrith() {
        return brith;
    }

    public void setBrith(String brith) {
        this.brith = brith;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
