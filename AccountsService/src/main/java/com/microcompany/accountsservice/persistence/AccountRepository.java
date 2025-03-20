package com.microcompany.accountsservice.persistence;

import com.microcompany.accountsservice.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByOwnerId(Long OwnerId);

    @Query(nativeQuery = true, value = "Select SUM(BALANCE) from accounts a where a.OWNERID = ?1")
    Integer getSumAccounts(long uid);
}
