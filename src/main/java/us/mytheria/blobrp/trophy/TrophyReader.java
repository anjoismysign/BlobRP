package us.mytheria.blobrp.trophy;

import us.mytheria.blobrp.BlobRPAPI;

import java.io.File;

/**
 * Use API instead.
 */
@Deprecated
public class TrophyReader {
    /**
     * Reads a Trophy from a file.
     *
     * @param file The file to read from
     * @return The Trophy
     * @deprecated Use {@link BlobRPAPI#readTrophy(File)} instead
     */
    @Deprecated
    public static Trophy read(File file) {
        return BlobRPAPI.getInstance().readTrophy(file);
    }
}
