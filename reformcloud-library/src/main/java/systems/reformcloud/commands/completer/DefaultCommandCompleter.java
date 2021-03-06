/*
  Copyright © 2019 Pasqual K. | All rights reserved
 */

package systems.reformcloud.commands.completer;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import systems.reformcloud.commands.abstracts.AbstractCommandCompleter;
import systems.reformcloud.commands.abstracts.CommandMap;
import systems.reformcloud.commands.utility.Command;

/**
 * @author _Klaro | Pasqual K. / created on 18.06.2019
 */

public final class DefaultCommandCompleter extends AbstractCommandCompleter implements Serializable {

    @ConstructorProperties({"commandMap"})
    public DefaultCommandCompleter(CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    private CommandMap commandMap;

    @Override
    public int calculateCursorPosition(String buffer, int cursor) {
        int lastSpace = buffer.lastIndexOf(' ');
        return (lastSpace == -1) ? cursor - buffer.length() :
            cursor - (buffer.length() - lastSpace - 1);
    }

    @Override
    public int complete(String buffer, int cursor, List<CharSequence> candidates) {
        List<String> out = new ArrayList<>();
        if (buffer.isEmpty() || buffer.indexOf(' ') == -1) {
            List<String> commands = new ArrayList<>();
            commandMap.findAll(buffer).forEach(e -> commands.add(e.getName()));
            out.addAll(commands);
        } else {
            Command command = commandMap.fromFirstArgument(buffer);
            if (command != null) {
                String[] args = replace(buffer);
                List<String> returned = ((TabCompleter) command)
                    .complete(buffer, args);
                out.addAll(returned);
            }
        }

        Collections.sort(out);
        candidates.addAll(out);
        return calculateCursorPosition(buffer, cursor);
    }

    private String[] replace(String buffer) {
        List<String> args = new LinkedList<>(Arrays.asList(buffer.split(" ")));
        args.remove(buffer.split(" ")[0]);
        return args.toArray(new String[0]);
    }
}
