import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.validator.routines.UrlValidator;


public class Test {
    public static void main(String[] args) throws IOException {
        Testing testing = new Testing();
        System.out.println(testing.isClassical("https://www.youtube.com/watch?v=5N8O-iJ0uTw&ab_channel=PygmaLion"));
    }
}
class Testing{
    public boolean isClassical(String initTitle) throws IOException {
        String title= "";
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
        UrlValidator defaultValidator = new UrlValidator();
        return defaultValidator.isValid(url);
    }

    public String getId(String url){
        StringBuilder urlbuilder = new StringBuilder(url);
        return urlbuilder.substring(urlbuilder.indexOf("v=")+2, urlbuilder.indexOf("&"));
    }
}