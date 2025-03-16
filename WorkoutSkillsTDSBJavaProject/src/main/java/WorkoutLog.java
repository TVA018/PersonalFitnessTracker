import java.util.HashMap;
import java.util.Map;

//This class keeps track of a certain workout, such as the Workout Log ID
public class WorkoutLog {
    final String id; //Workout Log ID
    final String name; //Name of the user
    final double weight; //Weight of the user
    final String type; //Type of workout (Cardio, strength training, yoga, etc.)
    final double duration; //Workout duration in minutes
    final String intensity; //May be changed to an Enum
    final double caloriesBurned;

    public enum WorkoutType {
        WALKING("Walking"),
        RUNNING("Running"),
        CYCLING("Cycling"),
        STRENGTH("Strength Training"),
        SWIMMING("Swimming"),
        YOGA("Yoga"),
        JUMP_ROPE("Jump Rope"),
        ROW("Rowing"),
        DANCE("Dancing"),
        HIIT("HIIT (High-Intensity Interval Training)"),
        CLIMB("Climbing/Stair Climbing"),
        BOXING("Boxing"),
        TENNIS("Tennis"),
        BASKETBALL("Basketball"),
        FOOTBALL("Football"),
        ELLIP_TRAINER("Elliptical Trainer"),
        CROSSFIT("Crossfit"),
        SKI_SNOWBOARD("Skiing/Snowboard"),
        GOLF("Golf"),
        HIKING("Hiking"),
        CIRCUIT("Circuit Training");

        final private String name;

        WorkoutType(String stringRepresentation){
            name = stringRepresentation;
        }

        static WorkoutType getEnum(String name){
            for(WorkoutType workoutType : WorkoutType.values()){
                if(workoutType.getName().equals(name)){
                    return workoutType;
                }
            }

            return null; //Enum was not found
        }

        String getName(){
            return this.name;
        }
    }

    public enum WorkoutIntensity {
        LOW("Low"),
        MED("Medium"),
        HIGH("High");

        final private String name;

        WorkoutIntensity(String stringRepresentation){
            name = stringRepresentation;
        }

        static WorkoutIntensity getEnum(String name){
            for(WorkoutIntensity workoutType : WorkoutIntensity.values()){
                if(workoutType.getName().equals(name)){
                    return workoutType;
                }
            }

            return null; //Enum was not found
        }

        String getName(){
            return this.name;
        }
    }

    static final public HashMap<WorkoutType, HashMap<WorkoutIntensity, Double>> MET_LOOKUP = new HashMap<WorkoutType, HashMap<WorkoutIntensity, Double>>(){{
        put(WorkoutType.WALKING, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 3.3);
            put(WorkoutIntensity.MED, 5.0);
            put(WorkoutIntensity.HIGH, 7.0);
        }});
        put(WorkoutType.RUNNING, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 8.0);
            put(WorkoutIntensity.MED, 9.8);
            put(WorkoutIntensity.HIGH, 12.8);
        }});
        put(WorkoutType.CYCLING, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 4.0);
            put(WorkoutIntensity.MED, 6.0);
            put(WorkoutIntensity.HIGH, 10.0);
        }});
        put(WorkoutType.STRENGTH, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 2.8);
            put(WorkoutIntensity.MED, 3.5);
            put(WorkoutIntensity.HIGH, 6.0);
        }});
        put(WorkoutType.SWIMMING, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 4.0);
            put(WorkoutIntensity.MED, 7.0);
            put(WorkoutIntensity.HIGH, 10.0);
        }});
        put(WorkoutType.YOGA, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 2.5);
            put(WorkoutIntensity.MED, 4.0);
            put(WorkoutIntensity.HIGH, 7.0);
        }});
        put(WorkoutType.JUMP_ROPE, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 12.0);
            put(WorkoutIntensity.MED, 14.0);
            put(WorkoutIntensity.HIGH, 16.0);
        }});
        put(WorkoutType.ROW, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 6.0);
            put(WorkoutIntensity.MED, 9.0);
            put(WorkoutIntensity.HIGH, 12.0);
        }});
        put(WorkoutType.DANCE, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 3.0);
            put(WorkoutIntensity.MED, 7.0);
            put(WorkoutIntensity.HIGH, 8.0);
        }});
        put(WorkoutType.HIIT, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 8.0);
            put(WorkoutIntensity.MED, 12.0);
            put(WorkoutIntensity.HIGH, 16.0);
        }});
        put(WorkoutType.CLIMB, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 8.0);
            put(WorkoutIntensity.MED, 10.0);
            put(WorkoutIntensity.HIGH, 12.0);
        }});
        put(WorkoutType.BOXING, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 7.0);
            put(WorkoutIntensity.MED, 9.5);
            put(WorkoutIntensity.HIGH, 12.0);
        }});
        put(WorkoutType.TENNIS, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 4.5);
            put(WorkoutIntensity.MED, 7.0);
            put(WorkoutIntensity.HIGH, 10.0);
        }});
        put(WorkoutType.BASKETBALL, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 4.5);
            put(WorkoutIntensity.MED, 8.0);
            put(WorkoutIntensity.HIGH, 11.5);
        }});
        put(WorkoutType.FOOTBALL, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 4.5);
            put(WorkoutIntensity.MED, 7.0);
            put(WorkoutIntensity.HIGH, 10.0);
        }});
        put(WorkoutType.ELLIP_TRAINER, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 5.5);
            put(WorkoutIntensity.MED, 8.0);
            put(WorkoutIntensity.HIGH, 10.5);
        }});
        put(WorkoutType.CROSSFIT, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 8.0);
            put(WorkoutIntensity.MED, 10.0);
            put(WorkoutIntensity.HIGH, 14.0);
        }});
        put(WorkoutType.SKI_SNOWBOARD, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 6.0);
            put(WorkoutIntensity.MED, 7.0);
            put(WorkoutIntensity.HIGH, 9.0);
        }});
        put(WorkoutType.GOLF, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 4.3);
            put(WorkoutIntensity.MED, 5.0);
            put(WorkoutIntensity.HIGH, 6.0);
        }});
        put(WorkoutType.HIKING, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 6.0);
            put(WorkoutIntensity.MED, 7.0);
            put(WorkoutIntensity.HIGH, 9.0);
        }});
        put(WorkoutType.CIRCUIT, new HashMap<WorkoutIntensity, Double>(){{
            put(WorkoutIntensity.LOW, 4.0);
            put(WorkoutIntensity.MED, 8.0);
            put(WorkoutIntensity.HIGH, 12.0);
        }});
    }};

    public WorkoutLog(String id, String name, double weight, String type, double duration, String intensity, double caloriesBurned){
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.type = type;
        this.duration = duration;
        this.intensity = intensity;
        this.caloriesBurned = caloriesBurned;
    }

    public static double GetMET(Object workoutName, Object intensity){
        return MET_LOOKUP.get(WorkoutType.getEnum((String) workoutName)).
                get(WorkoutIntensity.getEnum((String) intensity));
    }
}
