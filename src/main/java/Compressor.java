import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Compressor {

    private final CompressionStrategy strategy;

    public Compressor(CompressionStrategy strategy) {
        this.strategy = strategy;
    }

    public static void main(String[] args) {
        try {
            System.out.println("Compression in " + args[0]);

            Path inFile = Paths.get(args[1]);

            if (args[0].equals("zip")) {
                File outFile = new File("frog.zip");
                Compressor zipCompressor = new Compressor(data -> {
                    ZipOutputStream zipOutputStream = new ZipOutputStream(data);
                    zipOutputStream.putNextEntry(new ZipEntry("frog.png"));
                    return zipOutputStream;
                });
                zipCompressor.compress(inFile, outFile);

            } else if (args[0].equals("gzip")) {
                File outFile = new File("frog.png.gz");
                Compressor gzipCompressor = new Compressor(GZIPOutputStream::new);
                gzipCompressor.compress(inFile, outFile);
            } else {
                System.out.println("Unknown format");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void compress(Path inFile, File outFile) throws IOException {
        try (OutputStream outStream = new FileOutputStream(outFile)) {
            Files.copy(inFile, strategy.compress(outStream));
        }
    }
}
