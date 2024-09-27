package ai.qodo.cover.plugin;

public class CoverError extends Exception {
    public CoverError(String message, Throwable cause) {
        super(message, cause);
    }

    public CoverError(String message) {
    }
}
