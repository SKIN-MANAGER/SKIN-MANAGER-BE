package com.skin_manager.skin_manager.model.dto.member.login.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLoginRequestDTO {
    private String id;
    private String pwd;
}
