import lombok.AllArgsConstructor;

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
    static String outFileName;

    public Compressor(CompressionStrategy strategy) {
        this.strategy = strategy;
    }

    public static void main(String[] args) {
        try {
            System.out.println("Compression in " + args[0]);

            Path inFile = Paths.get(args[1]);
            outFileName = inFile.getFileName().toString();

            if (args[0].equals("zip")) {
                File outFile = new File(outFileName.replaceAll("[^.]+$","").concat("zip"));
                Compressor zipCompressor = new Compressor(ZipOutputStream::new);
                zipCompressor.compress(inFile, outFile);

            } else if (args[0].equals("gzip")) {
                File outFile = new File(outFileName.concat(".gz"));
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
            OutputStream finalOutputStream = strategy.compress(outStream);
            if (finalOutputStream instanceof ZipOutputStream) {
                ((ZipOutputStream) finalOutputStream).putNextEntry(new ZipEntry(outFileName));
            }
            Files.copy(inFile, finalOutputStream);
            finalOutputStream.close();
        }
    }
}
