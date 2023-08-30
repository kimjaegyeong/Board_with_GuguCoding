package org.zerock.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.service.ReplyService;

import java.util.List;

@RestController
@RequestMapping("/replices/")
@Slf4j
@RequiredArgsConstructor
public class ReplyController {
    private final ReplyService replyService;
    @GetMapping(value="/board/{bno}", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ReplyDTO>> getListByBoard(@PathVariable("bno") Long bno){
        log.info("BNO : "+ bno);
        return new ResponseEntity<>(replyService.getList(bno), HttpStatus.OK);
    }

}
