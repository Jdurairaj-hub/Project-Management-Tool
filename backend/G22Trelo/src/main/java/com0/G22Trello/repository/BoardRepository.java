package com0.G22Trello.repository;

import com0.G22Trello.model.Board;
import com0.G22Trello.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Integer> {

}
