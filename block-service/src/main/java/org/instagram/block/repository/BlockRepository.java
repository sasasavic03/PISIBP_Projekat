package org.instagram.block.repository;

import org.instagram.block.model.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block, Long> {

    Optional<Block> findByBlockerIdAndBlockedId(Long blockerId, Long blockedId);

    List<Block> findByBlockerId(Long blockerId);

    List<Block> findByBlockedId(Long blockedId);

    boolean existsByBlockerIdAndBlockedId(Long blockerId, Long blockedId);
}
