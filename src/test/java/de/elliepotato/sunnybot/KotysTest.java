package de.elliepotato.sunnybot;

import de.arraying.kotys.JSON;
import org.junit.Test;

/**
 * @author Ellie in Discord
 * at 17/02/2018
 */
public class KotysTest {

    public static void main(String[] args) {
        parse();
    }

    @Test
    public static void parse() {
        JSON json = new JSON("{\"name\":\"Bob\",\"age\":25,\"lucky-numbers\":[19, 20, 21]}");
        System.out.println(json.marshal());
    }


}
