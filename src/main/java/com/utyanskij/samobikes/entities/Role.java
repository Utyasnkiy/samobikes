package com.utyanskij.samobikes.entities;

import javax.persistence.*;


//Role - это сущность (Entity) в приложении, представляющая роли пользователей.
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    //Construct
    public Role() {
    }

    public Role(String name) {
        this.name = name;
    }

    //getters, setters
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

    @Override
    public String toString() {
        return this.name.replace("ROLE_", "");
    }
}
