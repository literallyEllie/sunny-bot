package de.elliepotato.sunnybot.backend.console;

import de.elliepotato.sunnybot.SunnyBot;
import de.elliepotato.sunnybot.util.DiscordUtil;
import net.dv8tion.jda.core.entities.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Ellie on <an unknown date> for YT-er Sunny.
 */
public class SunnyConsole extends Thread {

    private final SunnyBot sunnyBot;

    public SunnyConsole(SunnyBot sunnyBot) {
        this.sunnyBot = sunnyBot;
    }

    @Override
    public void run() {
        sunnyBot.getLogger().info("Console thread running...");
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        Thread.currentThread().setName("Sunny-Console");

        do {
            System.out.print("> ");
            try {

                final String input = bufferedReader.readLine();
                if (input != null && !input.isEmpty()) {
                    final String[] args = input.split(" ");

                    switch (args[0].toLowerCase()) {
                        case "shutdown":
                        case "stop":
                            System.out.println("Shutting down.");
                            sunnyBot.shutdown(0);
                            try {
                                join();
                            } catch (InterruptedException e) {
                                System.out.println("Error shutting down console thread!");
                                e.printStackTrace();
                            }
                            break;

                        case "listening":
                        case "playing":
                        case "watching":
                            if (args.length < 2) {
                                cu((args[0].toLowerCase() + " <to>"));
                                if (sunnyBot.getJda().getPresence().getGame() != null) {
                                    sunnyBot.getLogger().info("You're currently: " + sunnyBot.getJda().getPresence().getGame().getName());
                                }
                                break;
                            }
                            String to = DiscordUtil.getFinalArg(args, 1);
                            switch (args[0].toLowerCase()) {
                                case "listening":
                                    sunnyBot.getJda().getPresence().setGame(Game.listening(to));
                                    break;
                                case "playing":
                                    sunnyBot.getJda().getPresence().setGame(Game.playing(to));
                                    break;
                                case "watching":
                                    sunnyBot.getJda().getPresence().setGame(Game.watching(to));
                                    break;
                            }
                            sunnyBot.getLogger().info("Now " + args[0].toLowerCase() + " to: " + to);
                            break;
                        case "say":
                            if (args.length < 2) {
                                cu("say <channelId> <message>");
                            } else {
                                long channel = Long.parseLong(args[1]);
                                String message = DiscordUtil.getFinalArg(args, 2);
                                sunnyBot.getJda().getTextChannelById(channel).sendMessage(message).queue();
                                sunnyBot.getLogger().info("Sent message '" + message + "' to '" + channel + "'.");
                            }
                            break;

                        default:
                            System.out.println("Unknown command.");

                    }
                }

            } catch (Throwable e) {
                sunnyBot.getLogger().error("Failed to process console command!", e);
                e.printStackTrace();
            }

        } while (true);


    }

    private void cu(String usage) {
        sunnyBot.getLogger().info("Correct usage: " + usage);
    }

}
