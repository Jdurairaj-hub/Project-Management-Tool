package com0.G22Trello.service;

import com0.G22Trello.model.Board;
import com0.G22Trello.model.User;
import com0.G22Trello.model.Workspace;
import com0.G22Trello.repository.BoardRepository;
import com0.G22Trello.repository.UserRepository;
import com0.G22Trello.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class BoardService {

    @Autowired
    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<String> createBoard(Board board, String userEmail){
        LocalDate today = LocalDate.now();
        board.setDateCreated(today);
        Board createdBoard = boardRepository.save(board);
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        user.getBoards().add(createdBoard);
        userRepository.save(user);

        return new ResponseEntity<>("Board created successfully", HttpStatus.CREATED);
    }

    public String updateBoard(int boardId, Board updatedBoard) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()) {
            Board existingBoard = optionalBoard.get();

            existingBoard.setTitle(updatedBoard.getTitle());
            existingBoard.setDateCreated(updatedBoard.getDateCreated());
            existingBoard.setDateToBeCompleted(updatedBoard.getDateToBeCompleted());

            boardRepository.save(existingBoard);
            return "Board ID " + boardId + " has been updated successfully.";
        } else {
            return "Board ID " + boardId + " does not exist.";
        }
    }

    public ResponseEntity<String> addUserToBoard(int boardId, String email) {
        Board board = boardRepository.findById(boardId).orElse(null);
        User user = userRepository.findByEmail(email);

        if (board == null) {
            return new ResponseEntity<>("Board not found", HttpStatus.NOT_FOUND);
        } else if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } else {
            List<User> users = board.getUsers();
            List<Board> boards = user.getBoards();

            if (users.contains(user) || boards.contains(board)) {
                return new ResponseEntity<>("User already added to the board", HttpStatus.CONFLICT);
            }

            users.add(user);
            board.setUsers(users);
            boardRepository.save(board);

            boards.add(board);
            user.setBoards(boards);
            userRepository.save(user);

            return new ResponseEntity<>("User added successfully to the board", HttpStatus.OK);
        }
    }

//    public ResponseEntity<String> removeUserFromBoard(int boardId, String email) {
//        Board board = boardRepository.findById(boardId).orElse(null);
//        User user = userRepository.findByEmail(email);
//
//        if (board == null) {
//            return new ResponseEntity<>("Board not found", HttpStatus.NOT_FOUND);
//        } else if (user == null) {
//            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
//        } else {
//            List<User> users = board.getUsers();
//            List<Board> boards = user.getBoards();
//
//            if (!users.contains(user) || !boards.contains(board)) {
//                return new ResponseEntity<>("User is not a member of the board", HttpStatus.CONFLICT);
//            }
//
//            users.remove(user);
//            board.setUsers(users);
//            boardRepository.save(board);
//
//            boards.remove(board);
//            user.setBoards(boards);
//            userRepository.save(user);
//
//            return new ResponseEntity<>("User removed successfully from the board", HttpStatus.OK);
//        }
//    }

    public ResponseEntity<String> removeUserFromBoard(int boardId, String email) {
        Board board = boardRepository.findById(boardId).orElse(null);
        User user = userRepository.findByEmail(email);

        if (board == null) {
            return new ResponseEntity<>("Board not found", HttpStatus.NOT_FOUND);
        } else if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        } else {
            List<User> users = board.getUsers();
            List<Board> boards = user.getBoards();


            if (!users.contains(user) && !boards.contains(board)) {
                return new ResponseEntity<>("User is not a member of the workspace", HttpStatus.CONFLICT);
            }

//            users.remove(user);
//            board.setUsers(users);
//            boardRepository.save(board);
//
//            boards.remove(board);
//            user.setBoards(boards);
//            userRepository.save(user);

            users.remove(user);
            board.setUsers(users);
            boardRepository.save(board);

            boards.remove(board);
            user.setBoards(boards);
            userRepository.save(user);

            return new ResponseEntity<>("User removed successfully from the board", HttpStatus.OK);
        }
    }



    public ResponseEntity<String> deleteBoard(int boardId) {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board != null) {
            boardRepository.delete(board);
            return new ResponseEntity<>( "Board deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>( "Board not found", HttpStatus.NOT_FOUND);
        }
    }

    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }


    public BoardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

