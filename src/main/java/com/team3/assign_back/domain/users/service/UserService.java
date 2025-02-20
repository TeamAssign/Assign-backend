package com.team3.assign_back.domain.users.service;

import com.team3.assign_back.domain.intermediate.entity.UserTastePreference;
import com.team3.assign_back.domain.intermediate.entity.UserTastePreferenceId;
import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import com.team3.assign_back.domain.tastePreference.repository.TastePreferenceRepository;
import com.team3.assign_back.domain.tastePreference.repository.UserTastePreferenceRepository;
import com.team3.assign_back.domain.team.entity.Team;
import com.team3.assign_back.domain.team.repository.TeamRepository;
import com.team3.assign_back.domain.users.dto.UserRegisterRequestDto;
import com.team3.assign_back.domain.users.dto.UserRegisterResponseDto;
import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.domain.users.repository.UserRepository;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TastePreferenceRepository tastePreferenceRepository;
    private final UserTastePreferenceRepository userTastePreferenceRepository;

    @Transactional
    public UserRegisterResponseDto registerUser(String vendorId, UserRegisterRequestDto requestDto) {
        validateUserNotExist(vendorId);

        Team team = getTeamByName(requestDto.getTeamName());
        TastePreference tastePreference = createTastePreference(requestDto);
        Users user = createUser(vendorId, requestDto, team);
        createUserTastePreference(user, tastePreference);

        return convertToResponseDto(user, tastePreference);
    }

    private void validateUserNotExist(String vendorId) {
        if (userRepository.findByVendorId(vendorId).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED);
        }
    }

    private Team getTeamByName(String teamName) {
        return teamRepository.findByName(teamName)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
    }

    private TastePreference createTastePreference(UserRegisterRequestDto requestDto) {
        TastePreference tastePreference = TastePreference.builder()
                .spicy(requestDto.getSpicy())
                .salty(requestDto.getSalty())
                .sweet(requestDto.getSweet())
                .build();

        return tastePreferenceRepository.save(tastePreference);
    }

    private Users createUser(String vendorId, UserRegisterRequestDto requestDto, Team team) {
        Users user = Users.builder()
                .vendorId(vendorId)
                .name(requestDto.getName())
                .team(team)
                .pros(requestDto.getPros())
                .cons(requestDto.getCons())
                .profileImgUrl("default-profile.png")
                .build();

        return userRepository.save(user);
    }

    private void createUserTastePreference(Users user, TastePreference tastePreference) {
        UserTastePreference userTastePreference = UserTastePreference.builder()
                .id(new UserTastePreferenceId(user.getId(), tastePreference.getId()))
                .users(user)
                .tastePreference(tastePreference)
                .build();

        userTastePreferenceRepository.save(userTastePreference);
    }

    private UserRegisterResponseDto convertToResponseDto(Users user, TastePreference tastePreference) {
        return UserRegisterResponseDto.builder()
                .id(user.getId())
                .userName(user.getName())
                .teamName(user.getTeam().getName())
                .profileImageUrl(user.getProfileImgUrl())
                .spicy(tastePreference.getSpicy())
                .salty(tastePreference.getSalty())
                .sweet(tastePreference.getSweet())
                .pros(user.getPros())
                .cons(user.getCons())
                .build();
    }
}