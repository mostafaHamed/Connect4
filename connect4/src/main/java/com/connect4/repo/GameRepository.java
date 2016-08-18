package com.connect4.repo;

import java.util.Map;
import com.connect4.model.Game;

/**
 * GameRepo interface
 * @author mhamed
 *
 */
public interface GameRepository {
	
	Game saveGame(Game game);
	
	Game getGame(String gameId); 
	
	Game updateGame(Game game);
	
	Map<Object, Object> getAllGameSessions();
	
	void deleteGame(String gameId);

}
