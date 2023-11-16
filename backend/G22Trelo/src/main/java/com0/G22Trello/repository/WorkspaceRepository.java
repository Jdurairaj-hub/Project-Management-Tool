package com0.G22Trello.repository;


import com0.G22Trello.model.User;
import com0.G22Trello.model.Workspace;
import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace,Integer>{




        Workspace findByWorkspaceTitle(String workspaceTitle);


}
