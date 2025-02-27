package com.team3.assign_back.domain.users.repository;

import com.team3.assign_back.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long>, UserRepositoryCustom {
    Optional<Long> findByVendorId(String vendorId);

    @Query("SELECT u.team.id FROM Users u WHERE u.id = :id")
    Long findTeamIdByUsersId(@Param("id") Long userId);

}