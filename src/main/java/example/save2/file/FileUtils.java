package example.save2.file;

import example.save2.exceptions.FileValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    private FileUtils() {
        /* This utility class should not be instantiated */
    }

    public static void validateXmlFile(String filePath) throws FileValidationException {

        try {

            Path path = Paths.get(filePath);

            validateCommonFileProperties(path);

            validateXmlHeader(path);

        } catch (InvalidPathException | IOException e) {
            throw new FileValidationException("Reading error", e);
        }

    }

    public static void validateFitFile(String filePath) throws FileValidationException {

        try {

            Path path = Paths.get(filePath);

            validateCommonFileProperties(path);

        } catch (IOException e) {
            throw new FileValidationException("Reading error", e);
        }

    }

    public static void validateCommonFileProperties(Path path) throws IOException {

        if (!Files.exists(path)) {
            throw new FileValidationException("File does not exist: " + path);
        }

        if (!Files.isRegularFile(path)) {
            throw new FileValidationException("File " + path + " is not a regular file");
        }

        if (Files.size(path) > 10 * 1024 * 1024) {
            throw new FileValidationException("File " + path + " is too big");
        }

    }

    private static void validateXmlHeader(Path path) throws IOException {

        try (InputStream is = Files.newInputStream(path)) {
            byte[] header = new byte[5];
            if (is.read(header) != 5) {
                throw new FileValidationException("Reading error");
            }
            if (!new String(header).equals("<?xml")) {
                throw new FileValidationException("File is not XML-based");
            }
        }

    }

    public static byte[] readFileBytes(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        return Files.readAllBytes(path);
    }

    public static List<String> readAllFilesInDirectory(String pathString, String pattern) {

        Path dirPath = Paths.get(pathString);

        List<String> result = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath, pattern)) {
            for (Path entry : stream) {
                result.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }

}
