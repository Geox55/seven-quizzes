package it.sevenbits.courses.quizzes.timer;

import it.sevenbits.courses.quizzes.core.repository.game.IGameRepository;

/**
 * Timer thread
 */
public class Timer extends Thread {
    private final IGameRepository gameRepository;
    private final String roomId;
    private static final long MILLISECONDS_FOR_QUESTION = 30000L;

    /**
     * Constructor
     * @param gameRepository - game repository
     * @param roomId - room id
     */
    public Timer(final IGameRepository gameRepository, final String roomId) {
        super();
        this.gameRepository = gameRepository;
        this.roomId = roomId;
    }

    /**
     * Run start
     */
    @Override
    public void run()  {
        try {
            Thread.sleep(gameRepository.getCountQuestions() * MILLISECONDS_FOR_QUESTION);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        gameRepository.finish(roomId);
    }

}
