package dev.rdchaves.subtitle.rename;

import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

public class FileMatched {
    private static final String DOT = ".";
    private Optional<Path> video = Optional.empty();
    private Optional<Path> subtitle = Optional.empty();

    public Optional<Path> getVideo() {
	return video;
    }

    public void setVideo(Path video) {
	this.video = Optional.ofNullable(video);
    }

    public Optional<Path> getSubtitle() {
	return subtitle;
    }

    public void setSubtitle(Path subtitle) {
	this.subtitle = Optional.ofNullable(subtitle);
    }

    public boolean hasBothPaths() {
	boolean has = video.isPresent() && subtitle.isPresent();
	return has;
    }

    public boolean isSameName(Optional<String> suffix) {
	
	return hasBothPaths() && StringUtils.equalsAny(substringBeforeLast(subtitle.get().getFileName().toString(), DOT),
    		substringBeforeLast(video.get().getFileName().toString(), DOT),
    		substringBeforeLast(video.get().getFileName().toString(), DOT) + suffix.orElse(StringUtils.EMPTY));
    }
    
    public String dryRun(Optional<String> suffix) {
	Objects.requireNonNull(video);
	Objects.requireNonNull(subtitle);
	
	return subtitle.get().toString() + " => " + getNewName(suffix);
    }

    public String getNewName(Optional<String> suffix) {
	return StringUtils.join(
		StringUtils.substringBeforeLast(video.get().toString(), DOT),
		suffix.orElse(StringUtils.EMPTY),
		DOT,
		StringUtils.substringAfterLast(subtitle.get().toString(), DOT));
    }

    public static String addSuffix(final String name, final String suffix) {
	return StringUtils.join(
		StringUtils.substringBeforeLast(name, DOT), 
		suffix, 
		DOT,
		StringUtils.substringAfterLast(name.toString(), DOT));
    }
    
    @Override
    public String toString() {
	return "Video: " + video.orElse(null) + "\nSubtitle: " + subtitle.orElse(null);
    }
}