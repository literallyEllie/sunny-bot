package de.elliepotato.sunnybot.util;

import java.util.regex.Pattern;

/**
 * Created by Ellie on 03/05/2017 for YT-er Sunny.
 */
public class DiscordUtil {

    public static final long BOT_SELF = 309345192232615937L;
    public static final long CHANNEL_GENERAL = 309256299923767297L;
    public static final long CHANNEL_STAFF = 309276723013484544L;

    public static final Pattern PATTERN_USER_ID = Pattern.compile("<!?@\\d{17,20}>");

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

    public static String stripInputUser(String input) {
        return input.replace("<@", "").replace("!", "").replace(">", "");
    }

    public static boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


}
