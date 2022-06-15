package elias.projects.classicalmusicbot.commands;

import ca.tristan.jdacommands.ExecuteArgs;
import ca.tristan.jdacommands.ICommand;
import elias.projects.classicalmusicbot.lavaplayer.GuildMusicManager;
import elias.projects.classicalmusicbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class CmdQuit implements ICommand {
    @Override
    public void execute(ExecuteArgs executeArgs) {
        if(!executeArgs.getMemberVoiceState().inAudioChannel()){
            executeArgs.getTextChannel().sendMessage("In order for me to leave, you need to be in the channel!").queue();
            return;
        }
        if(!executeArgs.getSelfVoiceState().inAudioChannel()){
            final AudioManager audioManager = executeArgs.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) executeArgs.getMemberVoiceState().getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        if(!Objects.equals(executeArgs.getMemberVoiceState().getChannel(), executeArgs.getSelfVoiceState().getChannel())){
            executeArgs.getTextChannel().sendMessage("In order for me to leave, you need to be in the same channel as me!").queue();
            return;
        }
        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(executeArgs.getGuild());
        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();
        AudioManager audioManager = executeArgs.getGuild().getAudioManager();
        audioManager.closeAudioConnection();

    }

    @Override
    public String getName() {
        return "quit";
    }

    @Override
    public String helpMessage() {
        return "To leave";
    }

    @Override
    public boolean needOwner() {
        return false;
    }
}
