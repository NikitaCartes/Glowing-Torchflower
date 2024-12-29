package xyz.nikitacartes.glowingtorchflower.config;

import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public abstract class ConfigTemplate {
    private transient final Pattern pattern = Pattern.compile("^[^$\"{}\\[\\]:=,+#`^?!@*&\\\\\\s/]+");
    transient final String configFilePath;
    transient final String configResourcePath = "xyz/nikitacartes/glowingtorchflower/config/";
    public static Path gameDirectory = FabricLoader.getInstance().getGameDir();
    private static String modName = "GlowingTorchflower";

    ConfigTemplate(String configFilePath) {
        this.configFilePath = configFilePath;
    }

    public static <Config extends ConfigTemplate> Config loadConfig(Class<Config> configClass, String configPath) {
        Path path = gameDirectory.resolve("config/" + modName).resolve(configPath);
        if (Files.exists(path)) {
            final HoconConfigurationLoader loader = HoconConfigurationLoader.builder().path(path).build();
            try {
                return loader.load().get(configClass);
            } catch (ConfigurateException e) {
                System.err.println("[" + modName + "] Failed to load config file" + e);
                return null;
            }
        } else {
            return null;
        }
    }

    public void save() {
        Path configDirectory = gameDirectory.resolve("config/" + modName);
        if (!Files.exists(configDirectory)) {
            try {
                Files.createDirectories(configDirectory);
            } catch (IOException e) {
                System.err.println("Failed to create config directory" + e);
            }
        }
        Path path = gameDirectory.resolve("config/" + modName + "/" + configFilePath);
        try {
            Files.writeString(path, handleTemplate());
        } catch (IOException e) {
            System.err.println("Failed to save config file" + e);
        }
    }

    private String escapeString(String string) {
        return string
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\"", "\\\"")
                .replace("'", "\\'");
    }

    protected <T> String wrapIfNecessary(T string) {
        String escapeString = escapeString(String.valueOf(string));
        if (!pattern.matcher(escapeString).matches()) {
            return "\"" + escapeString + "\"";
        } else {
            return escapeString;
        }
    }

    protected String wrapIfNecessary(double string) {
        return String.format(Locale.US, "%.4f", string);
    }

    protected String wrapIfNecessary(long string) {
        return String.valueOf(string);
    }

    protected <T extends List<String>> String wrapIfNecessary(T strings) {
        return "[" + strings
                .stream()
                .map(this::wrapIfNecessary)
                .collect(Collectors.joining(",\n  ")) + "]";
    }

    protected abstract String handleTemplate() throws IOException;



}
