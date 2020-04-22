package dev.rdchaves.subtitle;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import dev.rdchaves.subtitle.rename.Renamer;

public class App {

	private static final String PATH_PARAMETER = "path";
	private static final String PATH_DOESNT_EXIST_MESSAGE = "[ERROR] The \"" + PATH_PARAMETER
			+ "\" parameter doesn't exists. If the path contains white space, enclose it in quotation marks";
	private static final String PATH_NOT_PRESENT_MESSAGE = "The \"" + PATH_PARAMETER + "\" parameter is not present";

	public static void main(String[] args) throws IOException {

		String pathParam = Optional.of(System.getProperty(PATH_PARAMETER)).map(StringUtils::trimToNull)
				.orElseThrow(() -> new IllegalArgumentException(PATH_NOT_PRESENT_MESSAGE));

		try {
			new Renamer(Paths.get(pathParam).toRealPath()).execute();
		} catch (IOException e) {
			System.err.println(PATH_DOESNT_EXIST_MESSAGE);
		}
	}
}
