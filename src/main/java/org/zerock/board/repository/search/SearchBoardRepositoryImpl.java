package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {
//        log.info("search1....................");
//        QBoard board = QBoard.board;
//        JPQLQuery<Board> jpqlQuery = from(board);
//        jpqlQuery.select(board).where(board.bno.eq(1L));
// ===========================================================

//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//        QMember member = QMember.member;
//        JPQLQuery<Board> jpqlQuery = from(board);
//        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
//        List<Board> result = jpqlQuery.fetch();
//        result.stream().forEach(i-> System.out.println(i));
//        log.info(result+"");
//        log.info("-----------------------");
//        log.info(jpqlQuery+"");
//        log.info("-----------------------");

// ===========================================================
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;
        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member.email, reply.count());
        log.info(jpqlQuery + "");
        tuple.groupBy(board);
        log.info("-----------------------");
        log.info(tuple + "");
        log.info("-----------------------");
        List<Tuple> result = tuple.fetch();
        log.info(result + "");
        result.forEach(i -> System.out.println(i));
        //result.stream().forEach(i-> System.out.println(i));
        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        //PageRequestDTO 자체를 파라미터로 받지 않는 이유는 DTO를 가능하면 Repository 영역에서
        // 다루지 않기 위해서이다.
        log.info("searchPage.....................");
        QBoard board = QBoard.board;
        QMember member = QMember.member;
        QReply reply = QReply.reply;

        JPQLQuery<Board> jpqlQuery = from(board);
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));
        JPQLQuery<Tuple> tuple = jpqlQuery.select(board, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = board.bno.gt(0L);

        if (type != null) {
            String[] typeArr = type.split(" ");
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for (String t : typeArr) {
                switch (t) {
                    case "t":
                        conditionBuilder.or(board.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(board.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
            tuple.where(booleanBuilder);
            //order by
            Sort sort = pageable.getSort();
            //tuple.orderBy(board.bno.desc()); //직접 코드로 sort 처리 하는 방법

            //pageable에서 sort 객체 가져와서 처리하는 방법
            sort.stream().forEach(order -> {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                String prop = order.getProperty();

                PathBuilder orderByExpression = new PathBuilder(Board.class, "board"); //2번째 파라미터는 from()할 때 쓰는 변수명이랑 같아야 함
                tuple.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
            });
            tuple.groupBy(board);
            //page 처리
            tuple.offset(pageable.getOffset());
            tuple.limit(pageable.getPageSize());
            List<Tuple> result = tuple.fetch();
            long count = tuple.fetchCount();
            log.info(result + "");

        return new PageImpl<Object[]>(
                result.stream().map(t -> t.toArray()).collect(Collectors.toList()),
                pageable,
                count
        );

    }
}
