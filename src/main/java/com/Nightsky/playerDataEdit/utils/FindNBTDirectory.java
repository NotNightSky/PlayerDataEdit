package com.Nightsky.playerDataEdit.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class FindNBTDirectory {

    private static final Pattern UUID_DAT_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\.dat$"
    );

    private FindNBTDirectory() {
    }

    public static boolean isValid(Path directory) {
        if (directory == null || !Files.isDirectory(directory)) {
            return false;
        }
        try (Stream<Path> stream = Files.list(directory)) {
            return stream
                    .filter(Files::isRegularFile)
                    .anyMatch(path -> UUID_DAT_PATTERN.matcher(path.getFileName().toString()).matches());
        } catch (IOException | UncheckedIOException e) {
            return false;
        }
    }

    public static Path find(Path startDirectory) {
        try (Stream<Path> stream = Files.walk(startDirectory)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> UUID_DAT_PATTERN.matcher(path.getFileName().toString()).matches())
                    .findFirst()
                    .map(Path::getParent)
                    .map(startDirectory::relativize)
                    .orElse(null);
        } catch (IOException | UncheckedIOException e) {
            return null;
        }
    }
}
