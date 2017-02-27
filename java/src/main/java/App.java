public class App {
    private enum RunExample {
        android,
        ios
    }

    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        RunExample runExample = RunExample.android;

        if (args.length == 0) {
            System.out.println("Did not specify android or ios. Will run " + runExample.toString());
        } else if (args[0].equalsIgnoreCase("android")) {
            System.out.println("Running android example");
            runExample = RunExample.android;
        } else if (args[0].equalsIgnoreCase("ios")) {
            System.out.println("Running ios example");
            runExample = RunExample.ios;
        } else {
            System.out.println("Invalid argument. Quitting");
            System.exit(1);
        }

        if (runExample == RunExample.android) {
            AndroidBasic androidBasic = new AndroidBasic();
            try {
                androidBasic.run();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        } else if (runExample == RunExample.ios) {
            IosBasic iosBasic = new IosBasic();
            try {
                iosBasic.run();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}