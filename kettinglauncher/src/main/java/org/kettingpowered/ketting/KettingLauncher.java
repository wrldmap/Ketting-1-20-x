package org.kettingpowered.ketting;

import org.kettingpowered.ketting.common.betterui.BetterUI;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.kettingpowered.ketting.common.KettingConstants.*;

public class KettingLauncher {

    private static List<String> args;
    public static boolean enableUpdate;

    public static void main(String[] args) throws IOException {
        KettingLauncher.args = Arrays.asList(args);
        Path eula = Paths.get("eula.txt");

        Libraries.downloadInternal();

        parseArgs(eula);

        BetterUI.printTitle(NAME, BRAND, System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")", VERSION, BUKKIT_VERSION, FORGE_VERSION);
        if(!BetterUI.checkEula(eula)) System.exit(0);

        Libraries.download();
        Patcher.init();
        launch();
    }

    private static void parseArgs(Path eula) throws IOException {
        if (containsArg("-noui"))
            BetterUI.setEnabled(false);

        if (containsArg("-nologo"))
            BetterUI.setEnableBigLogo(false);

        if (containsArg("-accepteula"))
            BetterUI.forceAcceptEULA(eula);

        enableUpdate = !containsArg("-dau");

        //TODO: help command?
    }

    private static boolean containsArg(String arg) {
        if (args.isEmpty()) return false;

        if (args.contains(arg)) {
            args.remove(arg);
            return true;
        }

        return false;
    }

    private static void launch() {
        System.out.println("Launching Ketting..."); //TODO
    }
}
