package com.team3.assign_back.domain.intermediate.dto;

import com.team3.assign_back.domain.users.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParticipantsDto {
    private Long id;
    private String name;
    private String teamName;
    private String profileImageUrl;

    public static ParticipantsDto from(Users users){
        return new ParticipantsDto(
                users.getId(),
                users.getName(),
                users.getTeam().getName(),
                users.getProfileImgUrl()
        );
    }
}
