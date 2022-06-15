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
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class CmdQueue implements ICommand {
    @Override
    public void execute(ExecuteArgs executeArgs) {
        if(!executeArgs.getMemberVoiceState().inAudioChannel()){
            executeArgs.getTextChannel().sendMessage("In order to see the current queue, you need to be in the channel!").queue();
            return;
        }
        if(!executeArgs.getSelfVoiceState().inAudioChannel()){
            final AudioManager audioManager = executeArgs.getGuild().getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) executeArgs.getMemberVoiceState().getChannel();
            audioManager.openAudioConnection(memberChannel);
        }
        if(!Objects.equals(executeArgs.getMemberVoiceState().getChannel(), executeArgs.getSelfVoiceState().getChannel())){
            executeArgs.getTextChannel().sendMessage("In order to see the current queue, you need to be in the same channel as me!").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(executeArgs.getGuild());
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
        if(queue.isEmpty()){
            executeArgs.getTextChannel().sendMessage("Current queue is empty!").queue();
            return;
        }
        final int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> trackList = new ArrayList<>(queue);
        MessageAction messageAction = executeArgs.getTextChannel().sendMessage("**Queue**\n");

        for(int i = 0; i<trackCount;i++){
            final AudioTrack track = trackList.get(i);
            AudioTrackInfo info = track.getInfo();
            messageAction.append('#')
                    .append(String.valueOf(i+1))
                    .append(" '")
                    .append(info.title)
                    .append(" by ")
                    .append(info.author)
                    .append("' {'")
                    .append(formatTime(track.getDuration()))
                    .append("'}\n");
        }

        if(trackList.size()>trackCount){
            messageAction.append("And")
                    .append(String.valueOf(trackList.size()-trackCount))
                    .append(" more!");
        }
        messageAction.queue();
    }

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String helpMessage() {
        return null;
    }

    @Override
    public boolean needOwner() {
        return false;
    }
}
