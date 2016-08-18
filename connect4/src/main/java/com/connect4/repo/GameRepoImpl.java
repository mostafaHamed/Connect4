package com.connect4.repo;

import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.connect4.model.Game;

/**
 * GameRepo class
 * @author mhamed
 *
 */
@Repository
public class GameRepoImpl implements GameRepository{

	private static final String KEY = "Connect4Game";
	
	private RedisTemplate<String, Object> redisTemplate;
	private HashOperations hashOperations;

	@Autowired
	public GameRepoImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}
	
	@Override
	public Game saveGame(Game game) {
		 hashOperations.put(KEY, game.getGameId(), game);
		 return game;
	}

	@Override
	public Game getGame(String gameId) {
		return (Game) hashOperations.get(KEY, gameId);
	}

	@Override
	public Game updateGame(Game game) {
		 hashOperations.put(KEY, game.getGameId(), game);
		 return game;
	}

	@Override
	public Map<Object, Object> getAllGameSessions() {
		return hashOperations.entries(KEY);
	}

	@Override
	public void deleteGame(String gameId) {
		hashOperations.delete(KEY, gameId);
	}

}
