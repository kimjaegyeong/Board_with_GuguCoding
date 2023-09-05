package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Reply;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import javax.transaction.Transactional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    @Override
    public Long register(BoardDTO dto) {
        Board board = dtoToEntity(dto);
        boardRepository.save(board);
        return board.getBno();
    }

//    @Override
//    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
//        Function<Object[], BoardDTO> fn = (en-> entityToDTO((Board)en[0], (Member)en[1], (Long)en[2]));
//        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
//        return new PageResultDTO<>(result, fn);
//    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        Function<Object[], BoardDTO> fn = (en-> entityToDTO((Board)en[0], (Member)en[1], (Long)en[2]));
        //function은 entity를 BoardDTO로 변환할 때 어떤 행위를 통해 변환할 지 지정해주는 함수형인터페이스이다.
        Page<Object[]> result = boardRepository.searchPage( // pageRequestDTO를 이용해서 db로부터 조건에 맞는 page객체 가져오기
                pageRequestDTO.getType(), // 지정한 type 조건
                pageRequestDTO.getKeyword(), //지정한 keyword
                pageRequestDTO.getPageable(Sort.by("bno").descending())); //정렬 방법

        return new PageResultDTO<>(result, fn); //PageResultDTO로 Page객체와 entity->DTO 변환 기준을 파라미터로 전달
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result = boardRepository.getBoardByBno(bno);
        Object[] arr = (Object[]) result;
        return entityToDTO((Board)arr[0],(Member)arr[1], (Long)arr[2]);
    }
    @Transactional
    @Override
    public void BoardModify(BoardDTO boardDTO) {
        Board board= boardRepository.getOne(boardDTO.getBno());
        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());
        boardRepository.save(board);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) { //삭제 기능 구현, 트랜잭션 추가
        replyRepository.deleteByBno(bno); //댓글 먼저 삭제하고
        boardRepository.deleteById(bno); //게시글 삭제
    }


}
