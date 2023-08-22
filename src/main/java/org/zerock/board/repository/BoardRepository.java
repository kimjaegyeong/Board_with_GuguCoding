package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {
    //별칭 필수. board 엔티티는 b, member 엔티티는 w라는 별칭 가짐.
    @Query("select b, w from Board b left join b.writer w where b.bno=:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);
    @Query("SELECT b, r FROM Board b LEFT JOIN Reply r ON r.board = b WHERE b.bno =:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value="select b, w, count(r) " +
    " from Board b" +
    " Left join b.writer w " +
    " left join Reply r on r.board = b" +
    " GROUP BY b",
    countQuery = "Select count(b) From Board  b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("Select b, w, count(r) " +
            " from Board b left join b.writer w " +
    " left outer join Reply r on r.board=b" +
    " where b.bno=:bno")
    Object getBoardByBno(@Param("bno") Long bno);
}
