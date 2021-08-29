package net.opisek.unteruns.repositories;

import android.util.Pair;

import net.opisek.unteruns.models.LocationModel;
import net.opisek.unteruns.models.MorseModel;
import net.opisek.unteruns.models.MorseQrModel;
import net.opisek.unteruns.models.QrModel;
import net.opisek.unteruns.models.RightWrongQrModel;
import net.opisek.unteruns.models.RouteModel;
import net.opisek.unteruns.models.RouteQrModel;
import net.opisek.unteruns.models.WaypointModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MainRepository {
    //region                                                                        REPOSITORY
    // ==============================================================================================================================================================================================

    private static MainRepository instance;

    public static MainRepository getInstance() {
        if (instance == null) instance = new MainRepository();
        return instance;
    }
    private MainRepository() {
        initiateLocations();
        initializeRoutes();
        initializeMorseCodes();
        initializePostcardQuestions();
        initializeInputQuestions();
        initializeQrCodes();
    }

    // ==============================================================================================================================================================================================
    //endregion
    //region                                                                        LOCATIONS
    // ==============================================================================================================================================================================================

    private HashMap<String, LocationModel> locations;
    private void addLocation(LocationModel loc) {
        locations.put(loc.id, loc);
    }

    private void initiateLocations() {
        locations = new HashMap<>();
        addLocation(new LocationModel("eingang",    "Waldeingang",                      48.651969645503144d, 11.767538939916697d));
        addLocation(new LocationModel("kreuzung",   "Kreuzung",                         48.655620681852994d, 11.768300862382462d));
        addLocation(new LocationModel("koloman",    "Kapelle St. Koloman",              48.661686910045980d, 11.756360174609688d));
        addLocation(new LocationModel("see",        "Kleiner See",                      48.667600660245240d, 11.765604095755492d));
        addLocation(new LocationModel("kapellchen","Kleines Kapellchen",                48.660858476010720d, 11.771331706837545d));
        addLocation(new LocationModel("ggm",        "Gabelsberger Gymnasium Mainburg",  48.649638113113600d, 11.769270510916570d));

        addLocation(new LocationModel("eingang-kreuzung-1",48.655620681852994d, 11.768300862382462d));
    }

    public LocationModel getLocation(String id) {
        return locations.get(id);
    }

    // ==============================================================================================================================================================================================
    //endregion
    //region                                                                        ROUTES
    // ==============================================================================================================================================================================================

    private ArrayList<RouteModel> routes;
    private void addRoute(RouteModel route) {
        routes.add(route);
    }

    private void initializeRoutes() {
        routes = new ArrayList<>();
        addRoute(
                new RouteModel("Leicht", "4 Stunden",
                        new WaypointModel[]{
                                new WaypointModel(getLocation("ggm"), Riddle.RIGHTWRONG),
                                new WaypointModel(getLocation("eingang"), Riddle.INPUT),
                                new WaypointModel(getLocation("koloman"), Riddle.POSTCARDS),
                                new WaypointModel(getLocation("kapellchen")),
                                new WaypointModel(getLocation("ggm"), Riddle.FINAL)
                        }
                )
        );
        addRoute(
                new RouteModel("Schwer", "24 Stunden",
                        new WaypointModel[]{
                            new WaypointModel(getLocation("ggm")),
                            new WaypointModel(getLocation("eingang")),
                            new WaypointModel(getLocation("eingang-kreuzung-1")),
                            new WaypointModel(getLocation("kreuzung")),
                            new WaypointModel(getLocation("koloman")),
                            new WaypointModel(getLocation("see")),
                            new WaypointModel(getLocation("kapellchen"), Riddle.POSTCARDS),
                            new WaypointModel(getLocation("ggm"), Riddle.FINAL)
                        }
                )
        );
    }

    public ArrayList<Pair<String, String>> getRoutes() {
        ArrayList<Pair<String, String>> result = new ArrayList<>();
        for (int i = 0; i < routes.size(); i++) {
            RouteModel r = routes.get(i);
            result.add(new Pair<>(r.name, r.description));
        }
        return result;
    }

    private RouteModel route;
    private int routeProgress;

    public boolean pickRoute(int index) {
        if (index < 0 || index > routes.size()) return false;
        route = routes.get(index);
        routeProgress = -1;
        return true;
    }

    public WaypointModel nextWaypoint() {
        if (routeProgress >= route.waypoints.length-1) return null;
        return route.waypoints[routeProgress+1];
    }

    public WaypointModel nextStop() {
        WaypointModel result = null;
        for (int i = routeProgress+1; i < route.waypoints.length; i++) {
            if (!route.waypoints[i].intermediate) {
                result = route.waypoints[i];
                break;
            }
        }
        return result;
    }

    public WaypointModel currentStop() {
        if (routeProgress == -1) return null;
        //Log.v("repository", route.waypoints[routeProgress].location.name);
        return route.waypoints[routeProgress];
    }

    public boolean setStop(LocationModel loc) {
        boolean found = false;
        for (int i = 0; i < route.waypoints.length; i++) {
            if (route.waypoints[i].location == loc) {
                found = true;
                routeProgress = i;
                break;
            }
        }
        return found;
    }

    public void reachedWaypoint() {
        routeProgress++;
    }

    public void lostWaypoint() {
        routeProgress--;
    }

    // ==============================================================================================================================================================================================
    //endregion
    //region                                                                        MORSE
    // ==============================================================================================================================================================================================

    private HashMap<String, MorseModel> morseCodes;
    private void addMorseCode(MorseModel morse) { morseCodes.put(morse.id, morse); }

    private void initializeMorseCodes() {
        morseCodes = new HashMap<>();

        addMorseCode(new MorseModel("Frankfurt"));
        addMorseCode(new MorseModel("London"));
        addMorseCode(new MorseModel("Mainburg"));
        addMorseCode(new MorseModel("Moskau"));
        addMorseCode(new MorseModel("New York"));
        addMorseCode(new MorseModel("Paris"));
        addMorseCode(new MorseModel("Rio de Janeiro"));
        addMorseCode(new MorseModel("Venedig"));
        addMorseCode(new MorseModel("Zuerich"));

        //addMorseCode(new MorseModel("question1", "Was ist der vierte Planet in unserem Sonnensystem?"));
        addMorseCode(new MorseModel("question1", "Vierter Planet im Sonnensystem?"));
        addMorseCode(new MorseModel("answer1", "Mars"));
        //addMorseCode(new MorseModel("question2", "Was ist der hoechste Berg der Welt?"));
        addMorseCode(new MorseModel("question2", "Hoechster Berg der Welt?"));
        addMorseCode(new MorseModel("answer2", "Mount Everest"));
        addMorseCode(new MorseModel("question3", "Wer bringt am Weinachten Geschenke?"));
        addMorseCode(new MorseModel("answer3", "Nikolaus"));
    }

    public MorseModel getMorseCode(String id) { return morseCodes.get(id); }

    // ==============================================================================================================================================================================================
    //endregion
    //region                                                                        POSTCARD QUESTIONS
    // ==============================================================================================================================================================================================

    private MorseModel[] postcardQuestions;
    private boolean[] postcardQuestionDone;
    private MorseModel[] postcardAnswers;
    private void initializePostcardQuestions() {
        postcardQuestions = new MorseModel[]{
            getMorseCode("question1"),
            getMorseCode("question2"),
            getMorseCode("question3")
        };
        postcardQuestionDone = new boolean[] {
            false,
            false,
            false
        };
        postcardAnswers = new MorseModel[] {
                getMorseCode("answer1"),
                getMorseCode("answer2"),
                getMorseCode("answer3")
        };
    }

    public MorseModel[] getPostcardQuestions() { return postcardQuestions; }

    public MorseModel getPostcardQuestion(int index) { return postcardQuestions[index]; }

    PostcardQuestionDoneListener postcardQuestionDoneListener;
    public interface PostcardQuestionDoneListener {
        public void onDoneUpdated(int index);
    }
    public void setPostcardQuestionDoneListener(PostcardQuestionDoneListener listener) {
        postcardQuestionDoneListener = listener;
    }

    public boolean getPostcardQuestionDone(int index) { return postcardQuestionDone[index]; }

    public boolean getAllPostcardQuestionsDone() {
        boolean allDone = true;
        for (int i = 0; i < postcardQuestions.length; i++) {
            if (!postcardQuestionDone[i]) {
                allDone = false;
                break;
            }
        }
        return allDone;
    }

    public void setPostcardQuestionDone(int index) {
        postcardQuestionDone[index] = true;
        if (postcardQuestionDoneListener != null)postcardQuestionDoneListener.onDoneUpdated(index);
    }

    public MorseModel getPostcardAnswer(int index) { return postcardAnswers[index]; }


    // ==============================================================================================================================================================================================
    //endregion
    //region                                                                        INPUT QUESTIONS
    // ==============================================================================================================================================================================================

    public enum inputQuestionID {
        TEST
    };

    private HashMap<inputQuestionID, String> inputQuestionAnswers;

    private void initializeInputQuestions() {
        inputQuestionAnswers = new HashMap<>();
        inputQuestionAnswers.put(inputQuestionID.TEST, "test");
    }

    public String getInputQuestionAnswer(inputQuestionID id) { return inputQuestionAnswers.get(id); }

    // ==============================================================================================================================================================================================
    //endregion
    //region                                                                        QR CODES
    // ==============================================================================================================================================================================================

    private HashMap<UUID, QrModel> qrCodes;
    private void addQrCode(QrModel qr) { qrCodes.put(qr.id, qr); }

    private void initializeQrCodes() {
        qrCodes = new HashMap<>();

        addQrCode(new RouteQrModel("17064bd6-5ee2-4b52-b397-d4beac844307", getLocation("eingang")));
        addQrCode(new RouteQrModel("6e40c051-df1f-4964-b77d-647c1d02265e", getLocation("kreuzung")));
        addQrCode(new RouteQrModel("d47bac64-3020-4a25-a5d6-1b781ac07d8a", getLocation("koloman")));
        addQrCode(new RouteQrModel("11558407-bcd0-4dd0-9bf8-a2766f750a08", getLocation("see")));
        addQrCode(new RouteQrModel("a7eecb31-a434-4bdb-b1ea-615193db4921", getLocation("kapellchen")));
        addQrCode(new RouteQrModel("ba02ab0e-aa12-456a-b1d0-912341590b6d", getLocation("ggm")));

        addQrCode(new MorseQrModel("63b6c8e4-3ac2-4db0-bcd3-ac0dfd695142", getMorseCode("frankfurt")));
        addQrCode(new MorseQrModel("f0b390db-5ddc-4b00-8a1a-872011d9eb43", getMorseCode("london")));
        addQrCode(new MorseQrModel("92b2d14e-522c-49c9-9065-b8f694eb8722", getMorseCode("mainburg")));
        addQrCode(new MorseQrModel("9feba8b8-516f-4d63-a72a-d2b4ace98fa2", getMorseCode("moskau")));
        addQrCode(new MorseQrModel("1c572510-049b-41e4-aa87-06862bbd9adb", getMorseCode("new-york")));
        addQrCode(new MorseQrModel("a28d4e2d-e654-4257-bab2-dbb267694470", getMorseCode("paris")));
        addQrCode(new MorseQrModel("707de83f-b25b-4d8b-b545-25e80e8001e0", getMorseCode("rio-de-janeiro")));
        addQrCode(new MorseQrModel("96a2af77-8f53-44ca-ac3b-d657948e8ee8", getMorseCode("venedig")));
        addQrCode(new MorseQrModel("ab55b93c-a03e-46de-908e-eb0afd0f557c", getMorseCode("zuerich")));

        addQrCode(new RightWrongQrModel("7e2dac79-f588-406b-abfa-1d882fdb9102", true));
        addQrCode(new RightWrongQrModel("05dcdaaa-81cb-4911-b59d-528b20f7707c", false));
        addQrCode(new RightWrongQrModel("0ba3cbc2-6bf4-48fe-9621-9b1d199775c5", false));
    }

    public QrModel getQrCode(String id) {
        return qrCodes.get(UUID.fromString(id));
    }

    // ==============================================================================================================================================================================================
    //endregion
    //region                                                                        RIDDLES
    // ==============================================================================================================================================================================================

    public enum Riddle {
        NONE,
        POSTCARDS,
        INPUT,
        RIGHTWRONG,
        FINAL
    }

    // ==============================================================================================================================================================================================
    //endregion
}