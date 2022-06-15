package elias.projects.classicalmusicbot.commands;

import ca.tristan.jdacommands.ExecuteArgs;
import ca.tristan.jdacommands.ICommand;
import elias.projects.classicalmusicbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Random;
import java.util.stream.Stream;

public class CmdRandomRadio implements ICommand {
    @Override
    public void execute(ExecuteArgs executeArgs) {
        if(!executeArgs.getMemberVoiceState().inAudioChannel()){
            executeArgs.getTextChannel().sendMessage("Enter a voice channel, so that I could play some classical music!").queue();
            return;
        }

        if(!executeArgs.getSelfVoiceState().inAudioChannel()){
            final AudioManager audioManager = executeArgs.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) executeArgs.getMemberVoiceState().getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        Random rand = new Random();
        int randomComp = rand.nextInt(914);
        String composer = "";
        try (Stream<String> lines = Files.lines(Paths.get("C:\\Spring Projects\\ClassicalMusicBot\\src\\main\\resources\\compossersforbot.txt"))) {
            composer = lines.skip(randomComp).findFirst().get();
        } catch (IOException e) {
            e.printStackTrace();

        }
        executeArgs.getTextChannel().sendMessage("We remember " + composer.substring(0,1).toUpperCase() + composer.substring(1) + ".").queue();
        PlayerManager.getINSTANCE().loadAndPlay(executeArgs.getTextChannel(), "ytsearch: " + composer + " playlist");
    }

    @Override
    public String getName() {
        return "radio";
    }

    @Override
    public String helpMessage() {
        return "To play random classical composer";
    }

    @Override
    public boolean needOwner() {
        return false;
    }
}
