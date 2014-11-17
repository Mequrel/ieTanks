package pl.edu.agh.ietanks.boards;

import pl.edu.agh.ietanks.boards.api.BoardsReader;
import pl.edu.agh.ietanks.boards.model.Board;
import pl.edu.agh.ietanks.boards.model.Obstacle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BoardsBean implements BoardsReader {
    private Map<Integer, Board> boards = new HashMap<>();

    public BoardsBean() {
        Board board = new Board();
        board.setId(1);
        board.setName("Test board");
        board.setHeight(10);
        board.setWidth(20);

        List<Obstacle> obstacles = new ArrayList<>();
        Obstacle obstacle = new Obstacle();
        obstacle.setX(5);
        obstacle.setY(10);
        obstacles.add(obstacle);

        board.setObstacles(obstacles);
        boards.put(1, board);
    }

    @Override
    public List<Board> getBoards() {
        return new ArrayList<>(boards.values());
    }

    @Override
    public Board getBoard(int boardId) {
        return boards.get(boardId);
    }
}
