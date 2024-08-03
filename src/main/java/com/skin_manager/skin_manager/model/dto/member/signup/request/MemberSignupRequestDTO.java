package com.skin_manager.skin_manager.model.dto.member.signup.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSignupRequestDTO {
    private String id;
    private String pwd;
    private String email;
    private String role;
    private String sns;
}
