package indi.eiriksgata.rulateday;

import indi.eiriksgata.dice.message.handle.InstructHandle;
import indi.eiriksgata.dice.operation.DiceSet;
import indi.eiriksgata.rulateday.service.UserTempDataService;
import indi.eiriksgata.rulateday.service.impl.UserTempDataServiceImpl;
import net.mamoe.mirai.console.extension.PluginComponentStorage;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.Events;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Resource;

/**
 * @author: create by Keith
 * @version: v1.0
 * @description: indi.eiriksgata.rulateday
 * @date:2020/10/15
 **/

public class RulatedayCore extends JavaPlugin {

    public static final RulatedayCore INSTANCE = new RulatedayCore(); // 可以像 Kotlin 一样静态初始化单例

    private RulatedayCore() {
        super(new JvmPluginDescriptionBuilder(
                        "indi.eiriksgata.rulateday-dice", // name
                        "0.0.1" // version
                )
                        .author("Eirksgata")
                        .info("A TRPG dice plugin.")
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
        Events.registerEvents(INSTANCE, new DiceMessageEventHandle());
    }

    @Override
    public void onLoad(@NotNull PluginComponentStorage $this$onLoad) {
        getLogger().info("Rulateday-Dice loading...");
    }
}
