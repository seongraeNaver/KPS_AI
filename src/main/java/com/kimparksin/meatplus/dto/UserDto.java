package com.kimparksin.meatplus.dto;

import com.kimparksin.meatplus.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String pwd;
    private String name;
    private String email;
    private String address;
    private LocalDate birthdate;
    private LocalDateTime regDate;

    // Entity -> DTO 변환
    public UserDto(User user) {
        this.id = user.getId();
        this.pwd = user.getPwd();
        this.name = user.getName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.birthdate = user.getBirthdate();
        this.regDate = user.getRegDate();
    }

    // DTO -> Entity 변환
    public User toEntity() {
        User user = new User();
        user.setId(this.id);
        user.setPwd(this.pwd);
        user.setName(this.name);
        user.setEmail(this.email);
        user.setAddress(this.address);
        user.setBirthdate(this.birthdate);
        user.setRegDate(this.regDate);
        return user;
    }
}
