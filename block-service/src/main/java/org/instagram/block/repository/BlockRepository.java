package org.instagram.block.repository;

import org.instagram.block.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findByBlockerIdAndBlockedId(Long blockerId, Long blockedId);

    List<Block> findByBlockerId(Long blockerId);

    @Query("SELECT COUNT(b) > 0 FROM Block b WHERE b.blockerId = :blockerId AND b.blockedId = :blockedId")
    boolean existsByBlockerIdAndBlockedId(@Param("blockerId") Long blockerId, @Param("blockedId") Long blockedId);
}
