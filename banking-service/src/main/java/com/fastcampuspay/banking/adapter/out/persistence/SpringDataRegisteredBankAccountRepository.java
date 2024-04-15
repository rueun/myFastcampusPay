package com.fastcampuspay.banking.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataRegisteredBankAccountRepository extends JpaRepository<RegisteredBankAccountJpaEntity, Long> {

    @Query("SELECT r FROM RegisteredBankAccountJpaEntity r WHERE r.membershipId = :membershipId")
    List<RegisteredBankAccountJpaEntity> findByMembershipId(@Param("membershipId") String membershipId);
}
