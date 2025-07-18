package com.loopers.domain.users;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserModel extends BaseEntity {

    private String userId;
    private String gender;
    private String birthDate;
    private String email;

    public UserModel() {}

    public UserModel(String userId, String gender, String birthDate, String email) {
        this.userId = userId;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
    }

    public String getUserId() {return userId;}
    public String getGender() {return gender;}
    public String getBirthDate() {return birthDate;}
    public String getEmail() {return email;}
}
