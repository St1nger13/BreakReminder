package me.st1nger13.breakreminder;

public class Main {

    public static void main(String[] args) {
        int breakPeriod = 4 ;
        int breakDuration = 4 ;

        try {
            if(args.length > 0)
                breakPeriod = Integer.parseInt(args[0].trim()) ;
            if(args.length > 1)
                breakDuration = Integer.parseInt(args[1].trim()) ;
        } catch(NumberFormatException e) {
            Print.error("Incorrect time!") ;
        }

        Print.line("Set parameters:") ;
        Print.linet("Period: " + breakPeriod + " sec.") ;
        Print.linet("Duration: " + breakDuration + " sec.") ;

        BreakReminder breakReminder = BreakReminder.newBuilder()
                .setBreakPeriod(breakPeriod)
                .setBreakDuration(breakDuration)
                .setWorkRegime(WorkRegime.STRICT)
                .build() ;
        breakReminder.start() ;
    }
}
