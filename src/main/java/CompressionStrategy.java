import java.io.IOException;
import java.io.OutputStream;

@FunctionalInterface
public interface CompressionStrategy {

    OutputStream compress(OutputStream data) throws IOException;
}