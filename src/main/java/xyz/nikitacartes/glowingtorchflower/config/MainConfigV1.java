package xyz.nikitacartes.glowingtorchflower.config;

import com.google.common.io.Resources;
import org.apache.commons.text.StringSubstitutor;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.io.Resources.getResource;
import static java.nio.charset.StandardCharsets.UTF_8;

@ConfigSerializable
public class MainConfigV1 extends ConfigTemplate {
    public int torchflowerBrightness = 12;
    public int torchflowerPotBrightness = 14;
    public int torchflowerStage1Brightness = 3;
    public int torchflowerStage2Brightness = 7;
    public String configVersion = "1";

    public MainConfigV1() {
        super("main.conf");
    }

    public static MainConfigV1 load() {
        MainConfigV1 config = loadConfig(MainConfigV1.class, "main.conf");
        if (config == null) {
            config = new MainConfigV1();
            config.save();
        }
        return config;
    }

    protected String handleTemplate() throws IOException {
        Map<String, String> configValues = new HashMap<>();
        configValues.put("torchflowerBrightness", wrapIfNecessary(torchflowerBrightness));
        configValues.put("torchflowerPotBrightness", wrapIfNecessary(torchflowerPotBrightness));
        configValues.put("torchflowerStage1Brightness", wrapIfNecessary(torchflowerStage1Brightness));
        configValues.put("torchflowerStage2Brightness", wrapIfNecessary(torchflowerStage2Brightness));
        configValues.put("configVersion", wrapIfNecessary(configVersion));
        String configTemplate = Resources.toString(getResource(configResourcePath + configFilePath), UTF_8);
        return new StringSubstitutor(configValues).replace(configTemplate);
    }
}
