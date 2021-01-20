package indi.eiriksgata.rulateday;

import indi.eiriksgata.rulateday.utlis.LoadDatabaseFile;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.GlobalEventChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;

public class RulatedayCore extends JavaPlugin {

    public static final RulatedayCore INSTANCE = new RulatedayCore(); // 可以像 Kotlin 一样静态初始化单例

    private RulatedayCore() {
        super(new JvmPluginDescriptionBuilder(
                        "indi.eiriksgata.rulateday-dice", // name
                        "0.0.1" // version
                )
                        .author("Eirksgata")
                        .info("TRPG dice plugin.")
                        .build()
        );
    }


    @Override
    public void onDisable() {
        getLogger().info("Rulateday-Dice quiting...");
    }

    @Override
    public void onEnable() {
        getLogger().info("Rulateday-Dice is enable.Register Eventing...");
        GlobalEventChannel.INSTANCE.registerListenerHost(new DiceMessageEventHandle());
        LoadDatabaseFile.init();
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        getLogger().info("Rulateday-Dice loading...");
    }

    @Nullable
    @Override
    public String getResource(@NotNull String path) {
        return null;
    }

    @Nullable
    @Override
    public String getResource(@NotNull String path, @NotNull Charset charset) {
        return null;
    }

    @NotNull
    @Override
    public Path resolveDataPath(@NotNull Path relativePath) {
        return null;
    }

    @NotNull
    @Override
    public Path resolveDataPath(@NotNull String relativePath) {
        return null;
    }

    @NotNull
    @Override
    public File resolveDataFile(@NotNull String relativePath) {
        return null;
    }

    @NotNull
    @Override
    public File resolveDataFile(@NotNull Path relativePath) {
        return null;
    }

    @NotNull
    @Override
    public Path resolveConfigPath(@NotNull String relativePath) {
        return null;
    }

    @NotNull
    @Override
    public File resolveConfigFile(@NotNull Path relativePath) {
        return null;
    }

    @NotNull
    @Override
    public File resolveConfigFile(@NotNull String relativePath) {
        return null;
    }

    @NotNull
    @Override
    public Path resolveConfigPath(@NotNull Path relativePath) {
        return null;
    }
}
