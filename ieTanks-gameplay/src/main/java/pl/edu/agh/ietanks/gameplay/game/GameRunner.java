package pl.edu.agh.ietanks.gameplay.game;

import com.google.common.collect.Lists;
import pl.edu.agh.ietanks.engine.api.BoardDefinition;
import pl.edu.agh.ietanks.engine.api.Engine;
import pl.edu.agh.ietanks.engine.api.events.Event;
import pl.edu.agh.ietanks.engine.api.events.RoundResults;
import pl.edu.agh.ietanks.gameplay.bot.BotExecutor;
import pl.edu.agh.ietanks.gameplay.game.api.BotAlgorithm;
import pl.edu.agh.ietanks.gameplay.game.api.Game;
import pl.edu.agh.ietanks.gameplay.game.innerapi.GameHistoryStorage;
import pl.edu.agh.ietanks.gameplay.game.innerapi.GameLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


class GameRunner implements Runnable, Game {

    private GameLogger LOGGER = new StandardOutputGameLogger();

    private final UUID gameId;
    private final BoardDefinition gameBoard;
    private final List<BotAlgorithm> bots;
    private final Engine gameEngine;
    private final GameHistoryStorage historyStorage;
    private final List<Event> gameEvents;

    private void setupEngineParams() {
        gameEngine.setup(gameBoard,
                Lists.transform(bots, botAlgorithm -> new BotExecutor(botAlgorithm.id(), botAlgorithm.pythonCode()))
        );

        LOGGER.startGame();
    }

    @Override
    public void run() {
        setupEngineParams();

        RoundResults rResults;
        while ((rResults = gameEngine.nextMove()) != null && !rResults.isGameFinished()) {
            List<Event> roundEvents = rResults.getRoundEvents();
            gameEvents.addAll(roundEvents);

            LOGGER.nextRoundResults(rResults, gameEngine.currentBoard());
        }

        historyStorage.storeFinishedGame(this);
    }

    public GameRunner(GameHistoryStorage historyStorage,
                      Engine gameEngine,
                      BoardDefinition gameBoard,
                      List<BotAlgorithm> gameBots) {
        this.gameId = UUID.randomUUID();
        this.historyStorage = historyStorage;
        this.gameEngine = gameEngine;
        this.gameEvents = new ArrayList<>();
        this.gameBoard = gameBoard;
        this.bots = gameBots;
    }

    @Override
    public UUID getId() {
        return gameId;
    }

    @Override
    public List<Event> getGameEvents() {
        return Collections.unmodifiableList(gameEvents);
    }

    @Override
    public List<BotAlgorithm> getGameParticipants() {
        return Collections.unmodifiableList(bots);
    }
}
