package com.team3.assign_back.domain.users.service;

import com.team3.assign_back.domain.intermediate.entity.UserTastePreference;
import com.team3.assign_back.domain.tastePreference.entity.TastePreference;
import com.team3.assign_back.domain.tastePreference.repository.TastePreferenceRepository;
import com.team3.assign_back.domain.tastePreference.repository.UserTastePreferenceRepository;
import com.team3.assign_back.domain.tastePreference.service.TastePreferenceEmbeddingService;
import com.team3.assign_back.domain.team.entity.Team;
import com.team3.assign_back.domain.team.repository.TeamRepository;
import com.team3.assign_back.domain.users.dto.UserRegisterRequestDto;
import com.team3.assign_back.domain.users.dto.UserResponseDto;
import com.team3.assign_back.domain.users.dto.UserSearchResponseDto;
import com.team3.assign_back.domain.users.entity.Users;
import com.team3.assign_back.domain.users.repository.UserRepository;
import com.team3.assign_back.global.exception.ErrorCode;
import com.team3.assign_back.global.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    @Value("${spring.cloud.aws.s3.default-profile-image}")
    private String defaultProfileImageUrl;

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TastePreferenceRepository tastePreferenceRepository;
    private final UserTastePreferenceRepository userTastePreferenceRepository;

    private final TastePreferenceEmbeddingService tastePreferenceEmbeddingService;

    @Transactional
    public void registerUser(String vendorId, UserRegisterRequestDto requestDto) {
        validateUserNotExist(vendorId);

        Team team = getTeamByName(requestDto.getTeamName());
        TastePreference tastePreference = createTastePreference(requestDto);
        Users users = createUser(vendorId, requestDto, team);
        createUserTastePreference(users, tastePreference);
        tastePreferenceEmbeddingService.saveEmbedding(tastePreference.getId());

        log.info("신규 사용자 등록 완료: vendorId={}", vendorId);
    }

    public UserResponseDto getUserInfo(String vendorId) {
        Users users = getUserByVendorId(vendorId);
        Team team = users.getTeam();

        return UserResponseDto.builder()
                .id(users.getId())
                .name(users.getName())
                .teamId(team.getId())
                .teamName(team.getName())
                .profileImageUrl(users.getProfileImgUrl() != null ? users.getProfileImgUrl() : defaultProfileImageUrl)
                .build();
    }

    public Page<UserSearchResponseDto> searchUsers(Long userId, String name, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return userRepository.searchUsersByFrequency(userId, name, pageable);
    }

    public Users getUserByVendorId(String vendorId) {
        return userRepository.findByVendorId(vendorId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public Long getUserIdByVendorId(String vendorId) {
        return userRepository.findUserIdByVendorId(vendorId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateUserNotExist(String vendorId) {
        if (userRepository.findByVendorId(vendorId).isPresent()) {
            log.warn("이미 등록된 사용자: vendorId={}", vendorId);
            throw new CustomException(ErrorCode.ALREADY_REGISTERED);
        }
    }

    private Team getTeamByName(String teamName) {
        return teamRepository.findByName(teamName)
                .orElseThrow(() -> {
                    log.warn("팀을 찾을 수 없습니다: teamName={}", teamName);
                    return new CustomException(ErrorCode.TEAM_NOT_FOUND);
                });
    }

    private TastePreference createTastePreference(UserRegisterRequestDto requestDto) {
        TastePreference tastePreference = TastePreference.builder()
                .spicy(requestDto.getSpicy())
                .salty(requestDto.getSalty())
                .sweet(requestDto.getSweet())
                .pros(requestDto.getPros())
                .cons(requestDto.getCons())
                .build();

        return tastePreferenceRepository.save(tastePreference);
    }

    private Users createUser(String vendorId, UserRegisterRequestDto requestDto, Team team) {
        Users users = Users.builder()
                .vendorId(vendorId)
                .name(requestDto.getName())
                .team(team)
                .profileImgUrl(defaultProfileImageUrl)
                .build();

        return userRepository.save(users);
    }

    private void createUserTastePreference(Users users, TastePreference tastePreference) {
        UserTastePreference userTastePreference = UserTastePreference.builder()
                .users(users)
                .tastePreference(tastePreference)
                .build();

        userTastePreferenceRepository.save(userTastePreference);
    }
}