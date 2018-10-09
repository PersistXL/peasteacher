package com.peas.api.entity.manager;


import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class Role implements Serializable {

    @Id
    private String id;

    private String name;

}
