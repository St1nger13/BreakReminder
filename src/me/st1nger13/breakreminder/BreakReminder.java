package me.st1nger13.breakreminder;

/**
 * Created by st1nger13 on 19.07.16.
 * Break Reminder
 */
public class BreakReminder {
    private int breakPeriod ;
    private int breakDuration ;
    private WorkRegime workRegime ;
    private volatile int timer ;
    private long lastUpdateTime ;
    private boolean isRunning ;


    private BreakReminder() {
    }

    public void start() {
        isRunning = true ;
        Thread timerThread = new Thread(() -> {
            while(isRunning) {
                if(System.currentTimeMillis() - lastUpdateTime > 1000) {
                    timer++ ;
                    lastUpdateTime = System.currentTimeMillis() ;
                }

                if(timer > breakPeriod) {
                    WindowReminder.start(breakDuration, workRegime) ;

                    if(timer > breakPeriod + breakDuration)
                        timer = 0 ;
                }

                try {
                    Thread.sleep(50) ;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }) ;
        timerThread.start() ;
    }

    public static BreakReminderBuilder newBuilder() {
        return new BreakReminder().new BreakReminderBuilder() ;
    }

    /*------------ BUILDER ------------*/

    public class BreakReminderBuilder {
        private BreakReminderBuilder() {}

        public BreakReminderBuilder setBreakPeriod(int breakPeriod) {
            BreakReminder.this.breakPeriod = breakPeriod ;
            return this ;
        }

        public BreakReminderBuilder setBreakDuration(int breakDuration) {
            BreakReminder.this.breakDuration = breakDuration ;
            return this ;
        }

        public BreakReminderBuilder setWorkRegime(WorkRegime workRegime) {
            BreakReminder.this.workRegime = workRegime ;
            return this ;
        }

        public BreakReminder build() {
            return BreakReminder.this ;
        }
    }
}
