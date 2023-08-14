package se.sundsvall.archive.api.exception;

public class ArchiveException extends RuntimeException {

	private static final long serialVersionUID = 7729884335418958967L;

	public ArchiveException(String message) {
		super(message);
	}

	public ArchiveException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
