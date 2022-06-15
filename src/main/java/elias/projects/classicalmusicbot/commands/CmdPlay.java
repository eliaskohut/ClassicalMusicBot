package elias.projects.classicalmusicbot.commands;

import ca.tristan.jdacommands.ExecuteArgs;
import ca.tristan.jdacommands.ICommand;
import elias.projects.classicalmusicbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CmdPlay implements ICommand {
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

        if(executeArgs.getArgs().length==1){
            executeArgs.getTextChannel().sendMessage("Enter \"!play\" and your link!").queue();
            return;
        }

        StringBuilder link = new StringBuilder();
        List<String> exArgsList = new ArrayList<>(Arrays.asList(executeArgs.getArgs()));
        for(int i = 1; i<exArgsList.size(); i++){
            link.append(exArgsList.get(i));
        }

        try {
            if(isUrl(link.toString()) && isClassical(link.toString())){
                    PlayerManager.getINSTANCE().loadAndPlay(executeArgs.getTextChannel(), "ytsearch: " + getTitle(link.toString()) + " audio");
            }else if(!isUrl(link.toString()) && isClassical(link.toString())){
                PlayerManager.getINSTANCE().loadAndPlay(executeArgs.getTextChannel(), "ytsearch: " + link + " audio");
            }else{
                executeArgs.getTextChannel().sendMessage("You can only play classical music!").queue();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isClassical(String initTitle) throws IOException {
        String title;
        if(isUrl(initTitle)){
            title = getTitle(initTitle);
        }else{
            title = initTitle;
        }
        if(title.contains("feat.") || title.contains("Feat.")){
            return false;
        }
        File f = new File("C:\\Spring Projects\\ClassicalMusicBot\\src\\main\\resources\\compossersforbot.txt");
        Scanner sc = new Scanner(f);
        while(sc.hasNextLine()){
            if(title.toLowerCase().contains(sc.nextLine())){
                return true;
            }
        }
        return false;
    }

    public String getTitle(String url) throws IOException {
        try{
            if(isUrl(url)){
                URL oracle = new URL(url);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(oracle.openStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    if(inputLine.contains("<title>")){
                        StringBuilder sb = new StringBuilder(inputLine);
                        return sb.substring(sb.indexOf("<title>") + 7, sb.indexOf(" - YouTube"));
                    }
                in.close();
            }
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }
    public boolean isUrl(String url){
        if(!url.contains("youtube")){
            return false;
        }
        UrlValidator defaultValidator = new UrlValidator();
        return defaultValidator.isValid(url);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String helpMessage() {
        return "To play classical music.";
    }

    @Override
    public boolean needOwner() {
        return false;
    }


}
