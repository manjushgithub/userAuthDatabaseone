package com.auth.user.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Token extends BaseModel{

    private String value;
    private Date expirydate;
    private boolean isDeleted;
    @ManyToOne
    private User user;



}
