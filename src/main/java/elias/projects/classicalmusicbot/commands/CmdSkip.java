package elias.projects.classicalmusicbot.commands;

import ca.tristan.jdacommands.ExecuteArgs;
import ca.tristan.jdacommands.ICommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import elias.projects.classicalmusicbot.lavaplayer.GuildMusicManager;
import elias.projects.classicalmusicbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class CmdSkip implements ICommand {
    @Override
    public void execute(ExecuteArgs executeArgs) {
        if(!executeArgs.getMemberVoiceState().inAudioChannel()){
            executeArgs.getTextChannel().sendMessage("In order to skip the current piece of classical music, you need to be in the channel!").queue();
            return;
        }
        if(!executeArgs.getSelfVoiceState().inAudioChannel()){
            final AudioManager audioManager = executeArgs.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) executeArgs.getMemberVoiceState().getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        if(!Objects.equals(executeArgs.getMemberVoiceState().getChannel(), executeArgs.getSelfVoiceState().getChannel())){
            executeArgs.getTextChannel().sendMessage("In order to skip the current piece of classical music, you need to be in the same channel as me!").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(executeArgs.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        if(audioPlayer.getPlayingTrack() == null){
            executeArgs.getTextChannel().sendMessage("You need to play something in order to skip!").queue();
            return;
        }
        musicManager.scheduler.nextTrack();
        executeArgs.getTextChannel().sendMessage("The current track has been skipped!").queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String helpMessage() {
        return "Skips the current piece of classical music!";
    }

    @Override
    public boolean needOwner() {
        return false;
    }
}
