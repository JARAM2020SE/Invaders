package engine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import screen.*;

import entity.Pair;

/**
 * Implements core game logic.
 * 
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 * 
 */
public final class Core {

	/** Width of current screen. */
	private static final int WIDTH = 448;
	/** Height of current screen. */
	private static final int HEIGHT = 520;
	/** Max fps of current screen. */
	private static final int FPS = 60;

	/** Max lives. */
	private static final int MAX_LIVES = 3;
	/** Levels between extra life. */
	private static final int EXTRA_LIFE_FRECUENCY = 3;
	/** Total number of levels. */
	private static final int NUM_LEVELS = 7;
	
	/** Difficulty settings for easy level 1. */
	private static final GameSettings SETTINGS_EASY_LEVEL_1 =
			new GameSettings(3, 3, 60, 1000);
	/** Difficulty settings for easy level 2. */
	private static final GameSettings SETTINGS_EASY_LEVEL_2 =
			new GameSettings(3, 4, 50, 1500);
	/** Difficulty settings for easy level 3. */
	private static final GameSettings SETTINGS_EASY_LEVEL_3 =
			new GameSettings(4, 4, 40, 1500);
	/** Difficulty settings for easy level 4. */
	private static final GameSettings SETTINGS_EASY_LEVEL_4 =
			new GameSettings(4, 5, 30, 2000);
	/** Difficulty settings for easy level 5. */
	private static final GameSettings SETTINGS_EASY_LEVEL_5 =
			new GameSettings(5, 5, 20, 1500);
	/** Difficulty settings for easy level 6. */
	private static final GameSettings SETTINGS_EASY_LEVEL_6 =
			new GameSettings(6, 6, 10, 1500);
	/** Difficulty settings for easy level 7. */
	private static final GameSettings SETTINGS_EASY_LEVEL_7 =
			new GameSettings(6, 6, 2, 1000);

	/** Difficulty settings for medium level 1 */
	private static final GameSettings SETTINGS_MEDIUM_LEVEL_1 =
			new GameSettings(4, 4, 60, 2500);
	/** Difficulty settings for medium level 2. */
	private static final GameSettings SETTINGS_MEDIUM_LEVEL_2 =
			new GameSettings(5, 4, 50, 3000);
	/** Difficulty settings for medium level 3. */
	private static final GameSettings SETTINGS_MEDIUM_LEVEL_3 =
			new GameSettings(5, 5, 40, 2000);
	/** Difficulty settings for medium level 4. */
	private static final GameSettings SETTINGS_MEDIUM_LEVEL_4 =
			new GameSettings(6, 5, 30, 2000);
	/** Difficulty settings for medium level 5. */
	private static final GameSettings SETTINGS_MEDIUM_LEVEL_5 =
			new GameSettings(6, 6, 20, 1500);
	/** Difficulty settings for medium level 6. */
	private static final GameSettings SETTINGS_MEDIUM_LEVEL_6 =
			new GameSettings(7, 6, 10, 1500);
	/** Difficulty settings for medium level 7. */
	private static final GameSettings SETTINGS_MEDIUM_LEVEL_7 =
			new GameSettings(8, 7, 2, 1000);

	/** Difficulty settings for level 1. */
	private static final GameSettings SETTINGS_DIFFICULT_LEVEL_1 =
			new GameSettings(4, 5, 70, 3000);
	/** Difficulty settings for level 2. */
	private static final GameSettings SETTINGS_DIFFICULT_LEVEL_2 =
			new GameSettings(4, 6, 65, 2500);
	/** Difficulty settings for level 3. */
	private static final GameSettings SETTINGS_DIFFICULT_LEVEL_3 =
			new GameSettings(4, 6, 60, 2000);
	/** Difficulty settings for level 4. */
	private static final GameSettings SETTINGS_DIFFICULT_LEVEL_4 =
			new GameSettings(5, 6, 55, 2000);
	/** Difficulty settings for level 5. */
	private static final GameSettings SETTINGS_DIFFICULT_LEVEL_5 =
			new GameSettings(6, 7, 50, 2000);
	/** Difficulty settings for level 6. */
	private static final GameSettings SETTINGS_DIFFICULT_LEVEL_6 =
			new GameSettings(7, 7, 45, 2000);
	/** Difficulty settings for level 7. */
	private static final GameSettings SETTINGS_DIFFICULT_LEVEL_7 =
			new GameSettings(8, 7, 40, 2000);

	/** Frame to draw the screen on. */
	private static Frame frame;
	/** Screen currently shown. */
	private static Screen currentScreen;
	/** Difficulty settings list. */
	private static List<GameSettings> gameSettings;
	/** Application logger. */
	private static final Logger LOGGER = Logger.getLogger(Core.class
			.getSimpleName());
	/** Logger handler for printing to disk. */
	private static Handler fileHandler;
	/** Logger handler for printing to console. */
	private static ConsoleHandler consoleHandler;


	/**
	 * Test implementation.
	 * 
	 * @param args
	 *            Program args, ignored.
	 */
	public static void main(final String[] args) {
		try {
			LOGGER.setUseParentHandlers(false);

			fileHandler = new FileHandler("log");
			fileHandler.setFormatter(new MinimalFormatter());

			consoleHandler = new ConsoleHandler();
			consoleHandler.setFormatter(new MinimalFormatter());

			LOGGER.addHandler(fileHandler);
			LOGGER.addHandler(consoleHandler);
			LOGGER.setLevel(Level.ALL);

		} catch (Exception e) {
			// TODO handle exception
			e.printStackTrace();
		}

		frame = new Frame(WIDTH, HEIGHT);
		DrawManager.getInstance().setFrame(frame);
		int width = frame.getWidth();
		int height = frame.getHeight();
		
		GameState gameState;

		int returnCode = 1;
		int playerCode = 1;
		int difficultyCode;
		int resetCode;
		do {
			switch (returnCode) {
				case 1:
					// Main menu.
					currentScreen = new TitleScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " title screen at " + FPS + " fps.");
					returnCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing title screen.");
					break;
				case 2:
					// Game & score.
					gameSettings = new ArrayList<GameSettings>();
					currentScreen = new PlayerSelectScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " player select screen at " + FPS + " fps.");
					playerCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing player select screen.");

					currentScreen = new DifficultySelectScreen(width, height, FPS);
					LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
							+ " difficulty select screen at " + FPS + " fps.");
					difficultyCode = frame.setScreen(currentScreen);
					LOGGER.info("Closing difficulty select screen.");

					gameState = new GameState(1, new Pair(0, 0),
							new Pair(MAX_LIVES, MAX_LIVES),
							new Pair(0, 0),
							new Pair(0, 0),
							playerCode);

					if (playerCode == 1) {
						if (difficultyCode == 1) {
							gameSettings.add(SETTINGS_EASY_LEVEL_1);
							gameSettings.add(SETTINGS_EASY_LEVEL_2);
							gameSettings.add(SETTINGS_EASY_LEVEL_3);
							gameSettings.add(SETTINGS_EASY_LEVEL_4);
							gameSettings.add(SETTINGS_EASY_LEVEL_5);
							gameSettings.add(SETTINGS_EASY_LEVEL_6);
							gameSettings.add(SETTINGS_EASY_LEVEL_7);
							do {
								// One extra live every few levels.
								boolean bonusLife = gameState.getLevel()
										% EXTRA_LIFE_FRECUENCY == 0
										&& gameState.getLivesRemaining().getPlayer1Value() < MAX_LIVES;

								currentScreen = new GameScreen(gameState,
										gameSettings.get(gameState.getLevel() - 1),
										bonusLife, width, height, FPS);
								LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
										+ " game screen at " + FPS + " fps.");
								frame.setScreen(currentScreen);
								LOGGER.info("Closing game screen.");

								gameState = ((GameScreen) currentScreen).getGameState();

								gameState = new GameState(gameState.getLevel() + 1,
										gameState.getScore(),
										gameState.getLivesRemaining(),
										gameState.getBulletsShot(),
										gameState.getShipsDestroyed(),
                    					playerCode);

							} while (gameState.getLivesRemaining().getPlayer1Value() > 0
									&& gameState.getLevel() <= NUM_LEVELS);

							LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
									+ " score screen at " + FPS + " fps, with a score of "
									+ gameState.getScore() + ", "
									+ gameState.getLivesRemaining() + " lives remaining, "
									+ gameState.getBulletsShot() + " bullets shot and "
									+ gameState.getShipsDestroyed() + " ships destroyed.");
							currentScreen = new ScoreScreen(width, height, FPS, gameState);
							returnCode = frame.setScreen(currentScreen);
							break;
						} else if (difficultyCode == 2) {
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_1);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_2);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_3);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_4);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_5);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_6);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_7);
							do {
								// One extra live every few levels.
								boolean bonusLife = gameState.getLevel()
										% EXTRA_LIFE_FRECUENCY == 0
										&& gameState.getLivesRemaining().getPlayer1Value() < MAX_LIVES;

								currentScreen = new GameScreen(gameState,
										gameSettings.get(gameState.getLevel() - 1),
										bonusLife, width, height, FPS);
								LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
										+ " game screen at " + FPS + " fps.");
								frame.setScreen(currentScreen);
								LOGGER.info("Closing game screen.");

								gameState = ((GameScreen) currentScreen).getGameState();

								gameState = new GameState(gameState.getLevel() + 1,
										gameState.getScore(),
										gameState.getLivesRemaining(),
										gameState.getBulletsShot(),
										gameState.getShipsDestroyed(),
										playerCode);

							} while (gameState.getLivesRemaining().getPlayer1Value() > 0
									&& gameState.getLevel() <= NUM_LEVELS);

							LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
									+ " score screen at " + FPS + " fps, with a score of "
									+ gameState.getScore() + ", "
									+ gameState.getLivesRemaining() + " lives remaining, "
									+ gameState.getBulletsShot() + " bullets shot and "
									+ gameState.getShipsDestroyed() + " ships destroyed.");
							currentScreen = new ScoreScreen(width, height, FPS, gameState);
							returnCode = frame.setScreen(currentScreen);
							break;
						} else if (difficultyCode == 3) {
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_1);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_2);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_3);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_4);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_5);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_6);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_7);
							do {
								// One extra live every few levels.
								boolean bonusLife = gameState.getLevel()
										% EXTRA_LIFE_FRECUENCY == 0
										&& gameState.getLivesRemaining().getPlayer1Value() < MAX_LIVES;

								currentScreen = new GameScreen(gameState,
										gameSettings.get(gameState.getLevel() - 1),
										bonusLife, width, height, FPS);
								LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
										+ " game screen at " + FPS + " fps.");
								frame.setScreen(currentScreen);
								LOGGER.info("Closing game screen.");

								gameState = ((GameScreen) currentScreen).getGameState();

								gameState = new GameState(gameState.getLevel() + 1,
										gameState.getScore(),
										gameState.getLivesRemaining(),
										gameState.getBulletsShot(),
										gameState.getShipsDestroyed(),
										playerCode);

							} while (gameState.getLivesRemaining().getPlayer1Value() > 0
									&& gameState.getLevel() <= NUM_LEVELS);

							LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
									+ " score screen at " + FPS + " fps, with a score of "
									+ gameState.getScore() + ", "
									+ gameState.getLivesRemaining() + " lives remaining, "
									+ gameState.getBulletsShot() + " bullets shot and "
									+ gameState.getShipsDestroyed() + " ships destroyed.");
							currentScreen = new ScoreScreen(width, height, FPS, gameState);
							returnCode = frame.setScreen(currentScreen);
							break;
						}
					}
					else if (playerCode == 2) {
						if (difficultyCode == 1) {
							gameSettings.add(SETTINGS_EASY_LEVEL_1);
							gameSettings.add(SETTINGS_EASY_LEVEL_2);
							gameSettings.add(SETTINGS_EASY_LEVEL_3);
							gameSettings.add(SETTINGS_EASY_LEVEL_4);
							gameSettings.add(SETTINGS_EASY_LEVEL_5);
							gameSettings.add(SETTINGS_EASY_LEVEL_6);
							gameSettings.add(SETTINGS_EASY_LEVEL_7);
							do {
								// One extra live every few levels.
								boolean bonusLife = gameState.getLevel()
										% EXTRA_LIFE_FRECUENCY == 0
										&& gameState.getLivesRemaining().getPlayer1Value() < MAX_LIVES;

								currentScreen = new GameScreen(gameState,
										gameSettings.get(gameState.getLevel() - 1),
										bonusLife, width, height, FPS);
								LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
										+ " game screen at " + FPS + " fps.");
								frame.setScreen(currentScreen);
								LOGGER.info("Closing game screen.");

								gameState = ((GameScreen) currentScreen).getGameState();

								gameState = new GameState(gameState.getLevel() + 1,
										gameState.getScore(),
										gameState.getLivesRemaining(),
										gameState.getBulletsShot(),
										gameState.getShipsDestroyed(),
										playerCode);

							} while ((gameState.getLivesRemaining().getPlayer1Value() > 0
									|| gameState.getLivesRemaining().getPlayer2Value() > 0)
									&& gameState.getLevel() <= NUM_LEVELS);

							LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
									+ " score screen at " + FPS + " fps, with a score of "
									+ gameState.getScore() + ", "
									+ gameState.getLivesRemaining() + " lives remaining, "
									+ gameState.getBulletsShot() + " bullets shot and "
									+ gameState.getShipsDestroyed() + " ships destroyed.");
							currentScreen = new ScoreScreen(width, height, FPS, gameState);
							frame.setScreen(currentScreen);
							currentScreen = new Player2ScoreScreen(width, height, FPS, gameState);
							returnCode = frame.setScreen(currentScreen);
							break;
						} else if (difficultyCode == 2) {
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_1);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_2);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_3);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_4);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_5);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_6);
							gameSettings.add(SETTINGS_MEDIUM_LEVEL_7);
							do {
								// One extra live every few levels.
								boolean bonusLife = gameState.getLevel()
										% EXTRA_LIFE_FRECUENCY == 0
										&& gameState.getLivesRemaining().getPlayer1Value() < MAX_LIVES;

								currentScreen = new GameScreen(gameState,
										gameSettings.get(gameState.getLevel() - 1),
										bonusLife, width, height, FPS);
								LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
										+ " game screen at " + FPS + " fps.");
								frame.setScreen(currentScreen);
								LOGGER.info("Closing game screen.");

								gameState = ((GameScreen) currentScreen).getGameState();

								gameState = new GameState(gameState.getLevel() + 1,
										gameState.getScore(),
										gameState.getLivesRemaining(),
										gameState.getBulletsShot(),
										gameState.getShipsDestroyed(),
										playerCode);

							} while (
									(gameState.getLivesRemaining().getPlayer1Value() > 0
									|| gameState.getLivesRemaining().getPlayer2Value() > 0)
									&& gameState.getLevel() <= NUM_LEVELS
							);

							LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
									+ " score screen at " + FPS + " fps, with a score of "
									+ gameState.getScore() + ", "
									+ gameState.getLivesRemaining() + " lives remaining, "
									+ gameState.getBulletsShot() + " bullets shot and "
									+ gameState.getShipsDestroyed() + " ships destroyed.");
							currentScreen = new ScoreScreen(width, height, FPS, gameState);
							frame.setScreen(currentScreen);
							currentScreen = new Player2ScoreScreen(width, height, FPS, gameState);
							returnCode = frame.setScreen(currentScreen);
							LOGGER.info("Closing score screen.");
							break;
						} else if (difficultyCode == 3) {
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_1);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_2);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_3);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_4);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_5);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_6);
							gameSettings.add(SETTINGS_DIFFICULT_LEVEL_7);
							do {
								// One extra live every few levels.
								boolean bonusLife = gameState.getLevel()
										% EXTRA_LIFE_FRECUENCY == 0
										&& gameState.getLivesRemaining().getPlayer1Value() < MAX_LIVES;

								currentScreen = new GameScreen(gameState,
										gameSettings.get(gameState.getLevel() - 1),
										bonusLife, width, height, FPS);
								LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
										+ " game screen at " + FPS + " fps.");
								frame.setScreen(currentScreen);
								LOGGER.info("Closing game screen.");

								gameState = ((GameScreen) currentScreen).getGameState();

								gameState = new GameState(gameState.getLevel() + 1,
										gameState.getScore(),
										gameState.getLivesRemaining(),
										gameState.getBulletsShot(),
										gameState.getShipsDestroyed(),
										playerCode);

							} while (
									(gameState.getLivesRemaining().getPlayer1Value() > 0
									|| gameState.getLivesRemaining().getPlayer2Value() > 0)
									&& gameState.getLevel() <= NUM_LEVELS
							);

							LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
									+ " score screen at " + FPS + " fps, with a score of "
									+ gameState.getScore() + ", "
									+ gameState.getLivesRemaining() + " lives remaining, "
									+ gameState.getBulletsShot() + " bullets shot and "
									+ gameState.getShipsDestroyed() + " ships destroyed.");
							currentScreen = new ScoreScreen(width, height, FPS, gameState);
							frame.setScreen(currentScreen);
							currentScreen = new Player2ScoreScreen(width, height, FPS, gameState);
							returnCode = frame.setScreen(currentScreen);
							LOGGER.info("Closing score screen.");
							break;
						}
					}
			case 3:
				// Reset High Score.
				currentScreen = new ScoreResetSelectScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " score reset screen at " + FPS + " fps.");
				resetCode = frame.setScreen(currentScreen);
				if (resetCode == 1) {
					List<Score> highScores = new ArrayList<Score>();
					try {
						getFileManager().saveHighScores(highScores);

					}
					catch (IOException e) { }
				}
				currentScreen = new TitleScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " title screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing reset score screen.");
				break;
			case 4:
				// High scores.
				currentScreen = new HighScoreScreen(width, height, FPS);
				LOGGER.info("Starting " + WIDTH + "x" + HEIGHT
						+ " high score screen at " + FPS + " fps.");
				returnCode = frame.setScreen(currentScreen);
				LOGGER.info("Closing high score screen.");
				break;
			default:
				break;
			}

		} while (returnCode != 0);

		fileHandler.flush();
		fileHandler.close();
		System.exit(0);
	}

	/**
	 * Constructor, not called.
	 */
	private Core() {

	}

	/**
	 * Controls access to the logger.
	 * 
	 * @return Application logger.
	 */
	public static Logger getLogger() {
		return LOGGER;
	}

	/**
	 * Controls access to the drawing manager.
	 * 
	 * @return Application draw manager.
	 */
	public static DrawManager getDrawManager() {
		return DrawManager.getInstance();
	}

	/**
	 * Controls access to the input manager.
	 * 
	 * @return Application input manager.
	 */
	public static InputManager getInputManager() {
		return InputManager.getInstance();
	}

	/**
	 * Controls access to the file manager.
	 * 
	 * @return Application file manager.
	 */
	public static FileManager getFileManager() {
		return FileManager.getInstance();
	}

	/**
	 * Controls creation of new cooldowns.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @return A new cooldown.
	 */
	public static Cooldown getCooldown(final int milliseconds) {
		return new Cooldown(milliseconds);
	}

	/**
	 * Controls creation of new cooldowns with variance.
	 * 
	 * @param milliseconds
	 *            Duration of the cooldown.
	 * @param variance
	 *            Variation in the cooldown duration.
	 * @return A new cooldown with variance.
	 */
	public static Cooldown getVariableCooldown(final int milliseconds,
			final int variance) {
		return new Cooldown(milliseconds, variance);
	}
}