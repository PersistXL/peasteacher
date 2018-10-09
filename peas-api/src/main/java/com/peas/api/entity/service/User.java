package com.peas.api.entity.service;

import com.peas.api.entity.manager.Role;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class User implements Serializable {


    @Id
    private String id;

    private String name;

    private Integer sex;

    @Column(name = "role_id")
    private String roleId;

}