package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.entity.Board;

@SpringBootTest
public class BoardServiceTests {
    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){
        BoardDTO dto = BoardDTO.builder()
                .title("test.")
                .content("test...")
                .writerEmail("user55@aaa.com") //현재 데이터베이스에 존재하는 회원 이메일
                .build();

       Long bno = boardService.register(dto);
    }
    @Test
    public void testShow(){
        BoardDTO dto = boardService.get(100L);
        System.out.println(dto);
    }

    @Test
    public void testRemove(){
        boardService.removeWithReplies(100L);
    }

    @Test
    public void testModify(){
        BoardDTO boardDTO= BoardDTO.builder().bno(2L)
                .title("제목변경.")
                .content("내용변경.")
                .build();
        boardService.BoardModify(boardDTO);
    }

}
