package com0.G22Trello.controller;

import com0.G22Trello.model.Board;
import com0.G22Trello.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;
    @PostMapping("/create")
    public ResponseEntity<String> createBoard(@RequestParam String boardTitle, @RequestParam String userEmail) {
        Board board = new Board();
        board.setTitle(boardTitle);

        return boardService.createBoard(board, userEmail);
    }

    @PutMapping("/{boardId}")
    public String updateBoard(@PathVariable("boardId") int boardId, @RequestBody Board board) {
        return boardService.updateBoard(boardId, board);
    }

    @DeleteMapping("/{boardId}/delete")
    public ResponseEntity<String> deleteBoard(@PathVariable("boardId") int boardId) {
        return boardService.deleteBoard(boardId);
    }

    @GetMapping("/boards")
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    @PutMapping("/{boardId}/addUser/{userEmail}")
    public ResponseEntity<String> addUserToBoard(@PathVariable("boardId") int boardId, @PathVariable("userEmail") String email) {
        return boardService.addUserToBoard(boardId, email);
    }

    // Remove a user from a board
    @PutMapping("/{boardId}/removeUser/{userEmail}")
    public ResponseEntity<String> removeUserFromBoard(@PathVariable("boardId") int boardId, @PathVariable("userEmail") String email) {
        return boardService.removeUserFromBoard(boardId, email);
    }

    //@GetMapping()

}