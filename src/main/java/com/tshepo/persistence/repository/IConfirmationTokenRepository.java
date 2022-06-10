package com.tshepo.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tshepo.persistence.Account;
import com.tshepo.persistence.tokens.ConfirmationToken;

@Repository
@Transactional(readOnly = true)
public interface IConfirmationTokenRepository extends CrudRepository<ConfirmationToken, Long>{
	
	Optional<ConfirmationToken> findByToken(String token);
	
	@Transactional
	@Modifying
	@Query("update ConfirmationToken set confirmedAt = :confirmedAt where token = :token")
	int confirmedAt(@Param ("token") String token, @Param("confirmedAt") LocalDateTime confirmedAt);
	
	@Query("select s from ConfirmationToken s where account = :account")
	List<ConfirmationToken> findByAccount(@Param("account") Account account);
	
}
