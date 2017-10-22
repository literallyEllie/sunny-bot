package de.ellie.sunnybot.util;

import java.util.regex.Pattern;

/**
 * Created by Ellie on 03/05/2017 for Discord.
 * Affiliated with www.minevelop.com
 */
public class DiscordUtil {

    public static final String SELF = "309345192232615937";

    public static final String GENERAL = "309256299923767297";

    public static final String PATTERN_USER_ID = Pattern.compile("<!?@\\d{17,20}>").pattern();

    public static String getFinalArg(final String[] args, final int start) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i < args.length; i++) {
            if (i != start) {
                sb.append(" ");
            }
            sb.append(args[i]);
        }
        final String msg = sb.toString();
        sb.setLength(0);
        return msg;
    }

    public static String stripInputUser(String input){
        return input.replace("<@", "").replace("!", "").replace(">", "");
    }


}
