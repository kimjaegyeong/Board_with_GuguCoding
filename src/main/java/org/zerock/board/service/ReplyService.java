package org.zerock.board.service;

import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);
    List<ReplyDTO> getList(Long bno); //특정 게시물의 댓글 목록
    void modify(ReplyDTO replyDTO);
    void remove(Long rno);

    //ReplyDTO를 Reply객체로 변환. Board객체의 처리가 수반됨
    default Reply dtoToEntity(ReplyDTO replyDTO){
        Board board = Board.builder().bno(replyDTO.getBno()).build();
        Reply reply= Reply.builder().rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .board(board)
                .build();
    return reply;
    }
    //Reply객체를 ReplyDTO로 변환. Board객체가 필요하지 않기 때문에 게시물 번호만
    default ReplyDTO entityToDto(Reply reply){
        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(reply.getRno())
                .bno(reply.getBoard().getBno())
                .replyer(reply.getReplyer())
                .text(reply.getText())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
        return replyDTO;
    }
}
