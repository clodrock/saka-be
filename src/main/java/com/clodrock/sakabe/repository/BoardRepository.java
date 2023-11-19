package com.clodrock.sakabe.repository;

import com.clodrock.sakabe.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<Board> findByBoardId(String boardId);

    @Query("SELECT b FROM Board b JOIN b.userList o WHERE o.email = :username")
    List<Board> findBoardsByUser(@Param("username") String username);

    @Query("SELECT b FROM Board b JOIN b.ownerList o WHERE o.email = :username")
    List<Board> findBoardsByOwner(@Param("username") String username);
}
