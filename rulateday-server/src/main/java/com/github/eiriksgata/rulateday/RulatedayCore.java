package com.github.eiriksgata.rulateday;

import com.github.eiriksgata.rulateday.init.LoadDatabaseFile;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;

import java.io.File;
import java.io.IOException;

public class RulatedayCore extends JavaPlugin {

    public static final RulatedayCore INSTANCE = new RulatedayCore();

    private RulatedayCore() {
        super(new JvmPluginDescriptionBuilder(
                        "com.github.eiriksgata.rulateday-dice", // name
                        //TODO: update version
                        "0.5.1"
                )
                        .author("eiriksgata")
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
        File file = new File("");
        //输出程序路径
        try {
            getLogger().info("canonical:" + file.getCanonicalPath());
            getLogger().info("absolute:" + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        getLogger().info("configPath:" + getConfigFolderPath());
        getLogger().info("dataPath:" + getDataFolderPath());

        LoadDatabaseFile.init();
        getLogger().info("Rulateday-Dice is enable.Register Eventing...");
        GlobalEventChannel.INSTANCE.registerListenerHost(new DiceMessageEventHandle());
    }

}
