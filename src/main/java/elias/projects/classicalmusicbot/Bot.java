package elias.projects.classicalmusicbot;


import ca.tristan.jdacommands.JDACommands;
import elias.projects.classicalmusicbot.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Bot {
    public static GatewayIntent[] INTENTS = {
            GatewayIntent.DIRECT_MESSAGES,
            GatewayIntent.GUILD_MESSAGES,
            GatewayIntent.GUILD_MESSAGE_REACTIONS,
            GatewayIntent.GUILD_VOICE_STATES,
            GatewayIntent.GUILD_MEMBERS,
            GatewayIntent.GUILD_PRESENCES
    };
    public static void main(String[] args) throws LoginException, InterruptedException {
        JDACommands jdacommands = new JDACommands("!");
        jdacommands.registerCommand(new CmdPlay());
        jdacommands.registerCommand(new CmdStop());
        jdacommands.registerCommand(new CmdSkip());
        jdacommands.registerCommand(new CmdNowPlaying());
        jdacommands.registerCommand(new CmdQueue());
        jdacommands.registerCommand(new CmdQuit());
        jdacommands.registerCommand(new CmdRandomRadio());
        JDA jda = JDABuilder.create("OTgwMDY5NTI1MTc5NDkwMzQ0.Gqkijh.AHTFJZPwWoarb6vr_oHAg7A_NHz0PEqzVdlRw0", Arrays.asList(INTENTS))
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(Activity.listening("YouTube"))
                .setStatus(OnlineStatus.ONLINE)
                .addEventListeners(jdacommands)
                .build()
                .awaitReady();
    }
}
