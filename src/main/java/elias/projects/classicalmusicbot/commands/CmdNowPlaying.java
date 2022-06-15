package elias.projects.classicalmusicbot.commands;

import ca.tristan.jdacommands.ExecuteArgs;
import ca.tristan.jdacommands.ICommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import elias.projects.classicalmusicbot.lavaplayer.GuildMusicManager;
import elias.projects.classicalmusicbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class CmdNowPlaying implements ICommand {
    @Override
    public void execute(ExecuteArgs executeArgs) {
        if(!executeArgs.getMemberVoiceState().inAudioChannel()){
            executeArgs.getTextChannel().sendMessage("In order to show the current piece of classical music, you need to be in the channel!").queue();
            return;
        }
        if(!executeArgs.getSelfVoiceState().inAudioChannel()){
            final AudioManager audioManager = executeArgs.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) executeArgs.getMemberVoiceState().getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        if(!Objects.equals(executeArgs.getMemberVoiceState().getChannel(), executeArgs.getSelfVoiceState().getChannel())){
            executeArgs.getTextChannel().sendMessage("In order to show the current piece of classical music, you need to be in the same channel as me!").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(executeArgs.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();
        if(playingTrack == null){
            executeArgs.getTextChannel().sendMessage("You need to play something in order to show what is playing!").queue();
            return;
        }
        final AudioTrackInfo info = playingTrack.getInfo();

        executeArgs.getTextChannel().sendMessageFormat("%s by %s", info.title, info.author).queue();
    }

    @Override
    public String getName() {
        return "playing";
    }

    @Override
    public String helpMessage() {
        return "Show what's cracking";
    }

    @Override
    public boolean needOwner() {
        return false;
    }
}
