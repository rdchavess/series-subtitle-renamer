package dev.rdchaves.subtitle.rename;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * The Class Renamer.
 */
public class Renamer {

    /** The Constant EXTENSIONS_DELIMITER_REGEX. */
    private static final String EXTENSIONS_DELIMITER_REGEX = "\\s*,\\s*";

    /** The Constant BKP_DIR_NAME. */
    private static final String BKP_DIR_NAME = "subtitle-bkp";

    /** The root path. */
    private Path rootPath;
    
    /** The episode identifier.<br>Default value: S\d{2}E\d{2}<br>Ex: S01E01 */
    private Pattern episodeIdentifier = Pattern.compile("S\\d{2}E\\d{2}");
    
    /** The subtitle extensions.
     * <ul>
     *   <li>.srt</li>
     *   <li>.sub</li>
     * </ul>
     */
    private String[] subtitleExtensions = new String[] {"srt", "sub"};
    
    /** The video extensions.
     * <ul>
     *   <li>.3g2</li>
     *   <li>.3gp</li>
     *   <li>.amv</li>
     *   <li>.asf</li>
     *   <li>.avi</li>
     *   <li>.drc</li>
     *   <li>.flv</li>
     *   <li>.flv</li>
     *   <li>.flv</li>
     *   <li>.f4v</li>
     *   <li>.f4p</li>
     *   <li>.f4a</li>
     *   <li>.f4b</li>
     *   <li>.gif</li>
     *   <li>.gifv</li>
     *   <li>.m4v</li>
     *   <li>.mkv</li>
     *   <li>.mng</li>
     *   <li>.mov</li>
     *   <li>.qt</li>
     *   <li>.mp4</li>
     *   <li>.m4p</li>
     *   <li>.m4v</li>
     *   <li>.mpg</li>
     *   <li>.mp2</li>
     *   <li>.mpeg</li>
     *   <li>.mpe</li>
     *   <li>.mpv</li>
     *   <li>.mpg</li>
     *   <li>.mpeg</li>
     *   <li>.m2v</li>
     *   <li>.mts</li>
     *   <li>.m2ts</li>
     *   <li>.ts</li>
     *   <li>.mxf</li>
     *   <li>.nsv</li>
     *   <li>.ogv</li>
     *   <li>.ogg</li>
     *   <li>.rm</li>
     *   <li>.rmvb</li>
     *   <li>.roq</li>
     *   <li>.svi</li>
     *   <li>.vob</li>
     *   <li>.webm</li>
     *   <li>.wmv</li>
     *   <li>.yuv</li>
     * </ul>
     */
    private String[] videoExtensions = new String[] { "3g2", "3gp", "amv", "asf", "avi", "drc", "flv", "flv", "flv",
	    "f4v", "f4p", "f4a", "f4b", "gif", "gifv", "m4v", "mkv", "mng", "mov", "qt", "mp4", "m4p", "m4v", "mpg",
	    "mp2", "mpeg", "mpe", "mpv", "mpg", "mpeg", "m2v", "mts", "m2ts", "ts", "mxf", "nsv", "ogv", "ogg", "rm",
	    "rmvb", "roq", "svi", "vob", "webm", "wmv", "yuv" };

    /** The dry run. */
    private boolean dryRun;
    
    /** The overwrite. */
    private boolean overwrite;
    
    /** The backup files. */
    private boolean backupFiles;
    
    /** The suffix. */
    private Optional<String> suffix;

    /**
     * Instantiates a new renamer.
     *
     * @param rootPath the root path
     */
    public Renamer(Path rootPath) {
	this.rootPath = rootPath;

	// episode identifier
	Optional.ofNullable(System.getProperty("episodeIdentifier")).map(StringUtils::trimToNull).ifPresent(v -> {
	    this.episodeIdentifier = Pattern.compile(v);
	});

	// subtitle extensions
	Optional.ofNullable(System.getProperty("subtitleExtensions")).map(StringUtils::trimToNull).ifPresent(v -> {
	    this.subtitleExtensions = v.trim().split(EXTENSIONS_DELIMITER_REGEX);
	});

	// subtitle extensions
	Optional.ofNullable(System.getProperty("videoExtensions")).map(StringUtils::trimToNull).ifPresent(v -> {
	    this.videoExtensions = v.trim().split(EXTENSIONS_DELIMITER_REGEX);
	});

	// suffix
	this.suffix = Optional.ofNullable(System.getProperty("suffix")).map(StringUtils::trimToNull);

	// dry run
	Optional.ofNullable(Boolean.getBoolean("dryRun")).ifPresent(v -> {
	    this.dryRun = v;
	});

	// overwrite
	Optional.ofNullable(Boolean.getBoolean("overwrite")).ifPresent(v -> {
	    if (v) {
		System.out.println("[WARNING] When overwrite is on, files overwritten will be backuped");
	    }
	    this.overwrite = v;
	});

	// backupFiles
	Optional.ofNullable(Boolean.getBoolean("backupFiles")).ifPresent(v -> {
	    this.backupFiles = v;
	});
    }

    /**
     * Execute.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void execute() throws IOException {

	System.out.println("[INFO] Starting to rename...");
	File rootFile = rootPath.toFile();
	if (rootFile.isDirectory()) {
	    processDirectory();
	} else {
	    processFile();
	}
	System.out.println("[INFO] The rename is finished!");
    }

    /**
     * Process directory.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void processDirectory() throws IOException {

	System.out.println("[INFO] Selected directory: " + rootPath + ": ");

	final Map<String, FileMatched> files = mapFiles(rootPath);

	files.entrySet().forEach(e -> System.out.println(
		"[INFO] " + e.getKey() + ": \n\t- " + e.getValue().getVideo() + "\n\t- " + e.getValue().getSubtitle()));

	rename(files);
    }

    /**
     * Rename.
     *
     * @param files the files
     */
    private void rename(Map<String, FileMatched> files) {

	files.entrySet().stream().forEach(entry -> {
	    System.out.println();
	    FileMatched value = entry.getValue();
	    if (dryRun) {
		System.out.println("[INFO] " + value.dryRun(suffix));
	    } else {
		rename(value);
	    }
	});
    }

    /**
     * Rename.
     *
     * @param matched the matched
     */
    private void rename(FileMatched matched) {
	final Path subtitlePath = matched.getSubtitle().get();
	final File destFile = subtitlePath.resolve(matched.getNewName(suffix)).toFile();
	if (!destFile.exists()) {
	    copyToBackup(subtitlePath);
	    rename(subtitlePath.toFile(), destFile);
	} else if (overwrite) {
	    moveToBackup(destFile.toPath());
	    rename(subtitlePath.toFile(), destFile);
	} else {
	    System.err.println(
		    "[WARNING] The file already exists (use \"-Doverwrite=true\" is you want do overwrite it): "
			    + destFile.getPath());
	}
    }

    /**
     * Rename.
     *
     * @param srcFile the src file
     * @param destFile the dest file
     */
    private void rename(File srcFile, File destFile) {

	if (srcFile.renameTo(destFile)) {
	    System.out.println("[INFO] " + srcFile.getPath() + " => " + destFile.getPath());
	} else {
	    System.err.println("[ERROR] Fail to rename file: " + srcFile.toPath());
	}
    }

    /**
     * Copy to backup.
     *
     * @param filePath the file path
     */
    private void copyToBackup(final Path filePath) {

	try {
	    if (backupFiles) {
		FileUtils.copyFileToDirectory(filePath.toFile(), filePath.getParent().resolve(BKP_DIR_NAME).toFile(),
			true);
	    }
	} catch (IOException e) {
	    throw new RuntimeException("Fail to create backup file", e);
	}
    }

    /**
     * Move to backup.
     *
     * @param filePath the file path
     */
    private void moveToBackup(final Path filePath) {
	try {
	    FileUtils.moveFileToDirectory(filePath.toFile(), filePath.getParent().resolve(BKP_DIR_NAME).toFile(), true);
	} catch (IOException e) {
	    throw new RuntimeException("Fail to create backup file", e);
	}
    }

    /**
     * Map files.
     * @param path          the path
     *
     * @return the map
     */
    private Map<String, FileMatched> mapFiles(Path path) {

	System.out.println("[INFO] Starting to map files in " + path.getFileName() + "...");
	try {
	    final Map<String, FileMatched> files = new HashMap<>();
	    Files.newDirectoryStream(path).forEach(p -> {
		File file = p.toFile();
		if (file.isDirectory()) {
		    files.putAll(mapFiles(file.toPath()));
		} else {
		    Matcher m = episodeIdentifier.matcher(p.toString());
		    if (m.find()) {
			String key = m.group();
			FileMatched value = Optional.ofNullable(files.get(key)).orElse(new FileMatched());
			if (isSubtitleFile(p)) {
			    value.setSubtitle(p);
			} else if (isVideoFile(p)) {
			    value.setVideo(p);
			}
			files.put(key, value);
		    }
		}
	    });
	    
	    final Map<String, FileMatched> result = files.entrySet().stream()
		    .filter(e -> e.getValue().hasBothPaths() && !e.getValue().isSameName(suffix))
		    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	    
	    System.out.println("[INFO] Files in " + path.getFileName() + " are mapped!");
	    
	    return result;
	} catch (IOException e) {
	    throw new RuntimeException("Falha ao mapear arquivos: " + path, e);
	}
    }

    /**
     * Checks if is subtitle file.
     *
     * @param path the path
     * @return true, if is subtitle file
     */
    private boolean isSubtitleFile(Path path) {
	return ArrayUtils.contains(subtitleExtensions, extractExtension(path));
    }
    
    /**
     * Checks if is video file.
     *
     * @param path the path
     * @return true, if is video file
     */
    private boolean isVideoFile(Path path) {
	return ArrayUtils.contains(videoExtensions, extractExtension(path));
    }

    /**
     * Extract extension.
     *
     * @param path the path
     * @return the string
     */
    private String extractExtension(Path path) {
	return StringUtils.substringAfterLast(path.toString(), ".").toLowerCase();
    }

    /**
     * Process file.
     */
    private void processFile() {
	
//	if ()
	throw new UnsupportedOperationException();
    }
}
