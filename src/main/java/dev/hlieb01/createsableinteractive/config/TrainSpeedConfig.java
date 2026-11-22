package dev.hlieb01.createsableinteractive.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.hlieb01.createsableinteractive.CreateSableInteractive;
import net.neoforged.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Small JSON config kept intentionally independent of NeoForge's TOML config format. */
public final class TrainSpeedConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FMLPaths.CONFIGDIR.get().resolve("createsableinteractive.json");
    private static volatile double maxTrainSpeedMps = -1.0;

    private TrainSpeedConfig() {}

    public static void load() {
        try {
            Files.createDirectories(FILE.getParent());
            if (!Files.exists(FILE)) {
                JsonObject defaults = new JsonObject();
                defaults.addProperty("maxTrainSpeed", -1.0);
                defaults.addProperty("_comment_ccd", "Sable/Rapier CCD is enabled by default; no public speed-toggle API is available.");
                Files.writeString(FILE, GSON.toJson(defaults));
            }
            JsonObject json = JsonParser.parseString(Files.readString(FILE)).getAsJsonObject();
            if (!json.has("_comment_ccd")) {
                json.addProperty("_comment_ccd", "Sable/Rapier CCD is enabled by default; no public speed-toggle API is available.");
                Files.writeString(FILE, GSON.toJson(json));
            }
            double configured = json.has("maxTrainSpeed") ? json.get("maxTrainSpeed").getAsDouble() : -1.0;
            if (!Double.isFinite(configured) || configured == 0 || configured < -1) {
                throw new IllegalArgumentException("maxTrainSpeed must be -1 or a positive number of m/s");
            }
            maxTrainSpeedMps = configured;
        } catch (Exception exception) {
            CreateSableInteractive.LOGGER.error("Could not read {}. Using maxTrainSpeed=-1 (unlimited).", FILE, exception);
            maxTrainSpeedMps = -1.0;
            try {
                JsonObject defaults = new JsonObject();
                defaults.addProperty("maxTrainSpeed", -1.0);
                defaults.addProperty("_comment_ccd", "Sable/Rapier CCD is enabled by default; no public speed-toggle API is available.");
                Files.writeString(FILE, GSON.toJson(defaults));
            } catch (IOException ignored) {
                // The in-memory default still works if the config directory is read-only.
            }
        }
    }

    /** Returns the configured limit in metres per second; -1 means unlimited. */
    public static double maxTrainSpeedMps() {
        return maxTrainSpeedMps;
    }
}
