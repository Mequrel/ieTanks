package pl.edu.agh.ietanks.gameplay.bot.api.adapters;

import com.google.common.base.Optional;
import pl.edu.agh.ietanks.engine.api.GameplayBoardView;
import pl.edu.agh.ietanks.gameplay.bot.api.Board;
import pl.edu.agh.ietanks.gameplay.bot.api.Missile;
import pl.edu.agh.ietanks.gameplay.bot.api.Position;

import java.util.Collection;
import java.util.List;

public class BoardAdapter implements Board {

    private GameplayBoardView engineBoard;

    public BoardAdapter(GameplayBoardView engineBoard) {
        this.engineBoard = engineBoard;
    }

    @Override
    public Integer[] tankIds() {
        List<Integer> tankIds = engineBoard.tankIds();
        Integer[] convertedTankIds = new Integer[tankIds.size()];
        int i = 0;
        for (Integer tankId : tankIds)
            convertedTankIds[i++] = tankId.intValue();
        return convertedTankIds;
    }

    @Override
    public Position findTank(int tankId) {
        Optional<pl.edu.agh.ietanks.engine.api.Position> position = engineBoard.findTank(tankId);
        if (position.isPresent()) {
            return new PositionAdapter(position.get());
        } else {
            return null;
        }
    }

    @Override
    public Integer findTank(Position position) {
        return engineBoard.findTank(convertToEnginePosition(position));
    }

    @Override
    public Missile[] findMissiles() {
        Collection<pl.edu.agh.ietanks.engine.api.Missile> missiles = engineBoard.findMissiles();
        return convertEngineMissiles(missiles);
    }

    @Override
    public Missile[] findMissiles(Position position) {
        Collection<pl.edu.agh.ietanks.engine.api.Missile> missiles = engineBoard.findMissiles(convertToEnginePosition(position));
        return convertEngineMissiles(missiles);
    }

    @Override
    public boolean isWithin(Position position) {
        return engineBoard.isWithin(convertToEnginePosition(position));
    }

    @Override
    public boolean isAccessibleForTank(Position position) {
        return engineBoard.isAccessibleForTank(convertToEnginePosition(position));
    }

    private Missile[] convertEngineMissiles(Collection<pl.edu.agh.ietanks.engine.api.Missile> missiles) {
        Missile[] convertedMissiles = new Missile[missiles.size()];
        int i = 0;
        for(pl.edu.agh.ietanks.engine.api.Missile missile : missiles) {
            convertedMissiles[i] = new MissileAdapter(missile);
            ++i;
        }
        return convertedMissiles;
    }

    private pl.edu.agh.ietanks.engine.api.Position convertToEnginePosition(Position position) {
        return new pl.edu.agh.ietanks.engine.api.Position(position.fromTop(), position.fromLeft());
    }
}
