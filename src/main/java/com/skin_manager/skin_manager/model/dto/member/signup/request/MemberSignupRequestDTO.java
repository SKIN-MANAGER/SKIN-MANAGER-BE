package com.skin_manager.skin_manager.model.dto.member.signup.request;

import com.skin_manager.skin_manager.util.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberSignupRequestDTO {

    private String memberId;
    private String pwd;
    private String email;
    private MemberRole memberRole;
    private String sns;
}
