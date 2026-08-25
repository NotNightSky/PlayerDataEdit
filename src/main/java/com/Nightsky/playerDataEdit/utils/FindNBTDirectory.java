package com.Nightsky.playerDataEdit.utils;

import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class FindNBTDirectory {
    private static final Pattern UUID_DAT_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\\.dat$"
    );
    public static Path find(Path startDirectory) {
        try (Stream<Path> stream = Files.walk(startDirectory)) {
            Optional<Path> firstDatFile = stream
                    .filter(Files::isRegularFile)
                    .filter(path -> UUID_DAT_PATTERN.matcher(path.getFileName().toString()).matches())
                    .findFirst();
            Path relativePath = firstDatFile
                    .map(Path::getParent)
                    .map(startDirectory::relativize)
                    .orElseThrow(() -> new IOException("No .dat files found in the directory tree."));
            Bukkit.getLogger().info("Found .dat directory (relative to server root): " + relativePath);
            return relativePath;
        } catch (IOException | UncheckedIOException e) {
            System.err.println("Error scanning directory: " + e.getMessage());
            return null;
        }
    }
}
