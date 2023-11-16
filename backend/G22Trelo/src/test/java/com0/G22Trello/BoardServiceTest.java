package com0.G22Trello;
import com0.G22Trello.model.Board;
import com0.G22Trello.model.User;
import com0.G22Trello.model.Workspace;
import com0.G22Trello.repository.BoardRepository;
import com0.G22Trello.repository.UserRepository;
import com0.G22Trello.repository.WorkspaceRepository;
import com0.G22Trello.service.BoardService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BoardService boardService;
    String sDate1="21/07/2023";
    Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);

    BoardServiceTest() throws ParseException {
    }
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // Test case for create board method with valid Board and User details
    @Test
    public void testCreateBoard_ValidUserEmail() {
        Board board = new Board();
        board.setTitle("My Board");

        String userEmail = "mihir.patel@dal.ca";
        User user = new User();
        user.setEmail(userEmail);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(boardRepository.save(any(Board.class))).thenReturn(board);

        ResponseEntity<String> createBoardResponse = boardService.createBoard(board, userEmail);
        assertEquals(HttpStatus.CREATED, createBoardResponse.getStatusCode());
        assertEquals("Board created successfully", createBoardResponse.getBody());

        verify(boardRepository, times(1)).save(board);

        assertEquals(1, user.getBoards().size());
        assertTrue(user.getBoards().contains(board));
        verify(userRepository, times(1)).save(user);
    }

    // Test for create board method with Invalid User
    @Test
    public void testCreateBoard_UserNotFound() {
        BoardRepository boardRepository = Mockito.mock(BoardRepository.class);
        UserRepository userRepository = Mockito.mock(UserRepository.class);

        Board board = new Board("Milestone", "TestBoard", date1);
        String userEmail = "nonexistent_user@example.com";

        Mockito.when(userRepository.findByEmail(Mockito.anyString())).thenReturn(null);

        BoardService boardService = new BoardService(boardRepository, userRepository);
        ResponseEntity<String> response = boardService.createBoard(board, userEmail);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

    // Test case for updating board method with Valid details
    @Test
    public void testUpdateBoard_Valid(){

        Board existingBoard = new Board(10,"Milestone", "TestBoard", LocalDate.now(), date1);


        int boardID = existingBoard.getBoardID();
        Board updateBoard = new Board("Milestone","Updated TestBoard",date1);
        when(boardRepository.findById(boardID)).thenReturn(java.util.Optional.of(existingBoard));
        when(boardRepository.save(updateBoard)).thenReturn(updateBoard);


        String result = boardService.updateBoard(boardID,updateBoard);
        verify(boardRepository, times(1)).findById(boardID);


        Assertions.assertEquals("Board ID " + boardID +" has been updated successfully.",result );

    }

    // Test case for updating board method, for board that is Not Present
    @Test
    public void testUpdateBoard_InValidBoard(){


        int boardID = 5000;
        Board updateBoard = new Board("Milestone","Updated TestBoard",date1);
        when(boardRepository.findById(boardID)).thenReturn(java.util.Optional.empty());


        String result = boardService.updateBoard(boardID,updateBoard);
        verify(boardRepository, times(1)).findById(boardID);


        Assertions.assertEquals("Board ID " + boardID +" does not exist.",result,result );

    }

    // Test case for Add user to board method, for valid board and user details
    @Test
    public void testAddUserToBoard_ValidUserAndBoard() {
        int boardId = 1;
        String userEmail = "user@example.com";

        Board board = new Board();
        board.setBoardID(boardId);
        List<User> users = new ArrayList<>();
        board.setUsers(users);

        User user = new User();
        user.setEmail(userEmail);
        List<Board> boards = new ArrayList<>();
        user.setBoards(boards);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        ResponseEntity<String> response = boardService.addUserToBoard(boardId, userEmail);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User added successfully to the board", response.getBody());

        verify(boardRepository, times(1)).findById(boardId);
        verify(userRepository, times(1)).findByEmail(userEmail);

        assertTrue(board.getUsers().contains(user));
        assertTrue(user.getBoards().contains(board));
    }


    // Test for remove user from board method, with invalid user
    @Test
    public void testRemoveUserFromBoard_UserNotFound() {
        int boardId = 2;
        String userEmail = "mihir.patel@dal.ca";

        Board board = new Board();
        board.setBoardID(boardId);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(userRepository.findByEmail(userEmail)).thenReturn(null);

        ResponseEntity<String> removeUserResponse = boardService.removeUserFromBoard(boardId, userEmail);
        assertEquals(HttpStatus.NOT_FOUND, removeUserResponse.getStatusCode());
        assertEquals("User not found", removeUserResponse.getBody());

        verify(boardRepository, never()).save(any());
        verify(userRepository, never()).save(any());
    }

    // Test for DeleteBoard method to delete the board
    @Test
    @Transactional
    public void testDeleteBoard_Valid(){
            int boardId = 12;
            Board board = new Board("Milestone", "TestBoard12", date1);
            board.setBoardID(boardId);

            when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

            ResponseEntity<String> response = boardService.deleteBoard(boardId);

            verify(boardRepository, times(1)).findById(boardId);
            verify(boardRepository, times(1)).delete(board);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Board deleted successfully", response.getBody(),response.getBody());

    }

    // Test for DeleteBoard method, where the boardID is not Valid
    @Test
    @Transactional
    public void testDeleteBoard_InValidBoardID(){
        int boardId = 10;

        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());
        ResponseEntity<String> response = boardService.deleteBoard(boardId);

        verify(boardRepository, times(1)).findById(boardId);
        verify(boardRepository, never()).delete(any());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Board not found", response.getBody(),response.getBody());
    }

    // Test for get all boards method checking if its returning all the boards
    @Test
    public void testGetAllBoards() {
        Board board1 = new Board();
        board1.setBoardID(1);
        board1.setTitle("Board 1");

        Board board2 = new Board();
        board2.setBoardID(2);
        board2.setTitle("Board 2");

        List<Board> mockBoards = new ArrayList<>();
        mockBoards.add(board1);
        mockBoards.add(board2);

        when(boardRepository.findAll()).thenReturn(mockBoards);

        List<Board> allBoards = boardService.getAllBoards();

        assertEquals(2, allBoards.size());

        assertEquals(board1.getBoardID(), allBoards.get(0).getBoardID());
        assertEquals(board1.getTitle(), allBoards.get(0).getTitle());
        assertEquals(board2.getBoardID(), allBoards.get(1).getBoardID());
        assertEquals(board2.getTitle(), allBoards.get(1).getTitle());

        verify(boardRepository, times(1)).findAll();
    }
}


