package net.opisek.unteruns.repositories;

import android.util.Log;
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
        initializeVote();
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
        addLocation(new LocationModel("ggm",        "Gabelsberger Gymnasium Mainburg",  48.6496666666667d, 11.7693055555556d));
        addLocation(new LocationModel("eingang",    "Waldeingang",                      48.6519722222222d, 11.7675555555556d));
        addLocation(new LocationModel("kreuzung",   "Kreuzung",                         48.6556666666667d, 11.7682777777778d));
        addLocation(new LocationModel("koloman",    "Kapelle St. Koloman",              48.6615833333333d, 11.75625d));
        addLocation(new LocationModel("see",        "Kleiner See",                      48.6676388888889d, 11.7654722222222d));
        addLocation(new LocationModel("kapellchen","Kleines Kapellchen",                48.6608888888889d, 11.7709444444444d));
        addLocation(new LocationModel("ggm2",        "Gabelsberger Gymnasium Mainburg", 48.6502777777778d, 11.7689722222222d));

        addLocation(new LocationModel("w2",48.6519722222222d, 11.7675555555556d)); // eingang
        addLocation(new LocationModel("w3",48.6522222222222d, 11.7688333333333d));
        addLocation(new LocationModel("w4",48.6496666666667d, 11.7693055555556d)); // ggm
        addLocation(new LocationModel("w5",48.6529166666667d, 11.7705277777778d));
        addLocation(new LocationModel("w6",48.6493333333333d, 11.7700966666667d));
        addLocation(new LocationModel("w7",48.6571388888889d, 11.7679722222222d));
        addLocation(new LocationModel("w8",48.6554444444444d, 11.7696944444444d));
        addLocation(new LocationModel("w9",48.6514166666667d, 11.7680833333333d));
        addLocation(new LocationModel("w10",48.6537777777778d, 11.7718333333333d));
        addLocation(new LocationModel("w11",48.6565d, 11.7685d));
        addLocation(new LocationModel("w12",48.6589444444444d, 11.7664444444444d));
        addLocation(new LocationModel("w13",48.6513888888889d, 11.7701666666667d));
        addLocation(new LocationModel("w14",48.6556666666667d, 11.7682777777778d)); // kreuzung
        addLocation(new LocationModel("w15",48.6618888888889d, 11.7566111111111d));
        addLocation(new LocationModel("w16",48.65975d, 11.7604444444444d));
        addLocation(new LocationModel("w17",48.6615833333333d, 11.75625d)); // koloman
        addLocation(new LocationModel("w18",48.6595277777778d, 11.7635d));
        addLocation(new LocationModel("w19",48.6582777777778d, 11.7663611111111d));
        addLocation(new LocationModel("w20",48.6640555555556d, 11.7581666666667d));
        addLocation(new LocationModel("w21",48.65925d, 11.7658055555556d));
        addLocation(new LocationModel("w22",48.6640555555556d, 11.7575d));
        addLocation(new LocationModel("w23",48.661d, 11.7561666666667d));
        addLocation(new LocationModel("w24",48.6626111111111d, 11.7568055555556d));
        addLocation(new LocationModel("w25",48.6654166666667d, 11.7581944444444d));
        addLocation(new LocationModel("w26",48.6623888888889d, 11.7570277777778d));
        addLocation(new LocationModel("w27",48.6661388888889d, 11.7633055555556d));
        addLocation(new LocationModel("w28",48.6664722222222d, 11.7618333333333d));
        addLocation(new LocationModel("w29",48.6656666666667d, 11.7574166666667d));
        addLocation(new LocationModel("w30",48.66675d, 11.7576388888889d));
        addLocation(new LocationModel("w31",48.6666388888889d, 11.7597222222222d));
        addLocation(new LocationModel("w32",48.666d, 11.7640277777778d));
        addLocation(new LocationModel("w33",48.6670555555556d, 11.7649166666667d));
        addLocation(new LocationModel("w34",48.6676388888889d, 11.7654722222222d)); // see
        addLocation(new LocationModel("w35",48.6651111111111d, 11.7634722222222d));
        addLocation(new LocationModel("w36",48.6636388888889d, 11.7620833333333d));
        addLocation(new LocationModel("w37",48.6629166666667d, 11.7613888888889d));
        addLocation(new LocationModel("w38",48.6623888888889d, 11.7602222222222d));
        addLocation(new LocationModel("w39",48.6618055555556d, 11.75875d));
        addLocation(new LocationModel("w40",48.6605277777778d, 11.7583333333333d));
        addLocation(new LocationModel("w41",48.6502777777778d, 11.7689722222222d)); // tisch
        addLocation(new LocationModel("w42",48.661d, 11.76275d));
        addLocation(new LocationModel("w43",48.6636388888889d, 11.7623888888889d));
        addLocation(new LocationModel("w44",48.6633611111111d, 11.7629444444444d));
        addLocation(new LocationModel("w45",48.6630555555556d, 11.7624166666667d));
        addLocation(new LocationModel("w46",48.6628055555556d, 11.7622777777778d));
        addLocation(new LocationModel("w47",48.6626944444444d, 11.7631111111111d));
        addLocation(new LocationModel("w48",48.6626666666667d, 11.7650833333333d));
        addLocation(new LocationModel("w49",48.6618333333333d, 11.7659166666667d));
        addLocation(new LocationModel("w50",48.66125d, 11.7684166666667d));
        addLocation(new LocationModel("w51",48.6608888888889d, 11.7709444444444d));
        addLocation(new LocationModel("w52",48.6608888888889d, 11.7709444444444d)); // kappelchen
        addLocation(new LocationModel("w53",48.6631388888889d, 11.7649166666667d));
        addLocation(new LocationModel("w54",48.6644722222222d, 11.7635277777778d));
        addLocation(new LocationModel("w55",48.6673055555556d, 11.7654166666667d));
        addLocation(new LocationModel("w56",48.6662222222222d, 11.7661388888889d));
        addLocation(new LocationModel("w57",48.6656388888889d, 11.7674444444444d));
        addLocation(new LocationModel("w58",48.66475d, 11.7684722222222d));
        addLocation(new LocationModel("w59",48.6638333333333d, 11.769d));
        addLocation(new LocationModel("w60",48.66325d, 11.7691944444444d));
        addLocation(new LocationModel("w61",48.66325d, 11.7686111111111d));
        addLocation(new LocationModel("w62",48.6629444444444d, 11.7685277777778d));
        addLocation(new LocationModel("w63",48.6621111111111d, 11.7674722222222d));
        addLocation(new LocationModel("w64",48.6609722222222d, 11.7694166666667d));
        addLocation(new LocationModel("w65",48.6605277777778d, 11.7691944444444d));
        addLocation(new LocationModel("w66",48.6601944444444d, 11.7679444444444d));
        addLocation(new LocationModel("w67",48.6594166666667d, 11.7666111111111d));
        addLocation(new LocationModel("w68",48.6555d, 11.7692777777778d));
        addLocation(new LocationModel("w69",48.6550555555556d, 11.7689444444444d));
        addLocation(new LocationModel("w70",48.6547777777778d, 11.7671111111111d));
        addLocation(new LocationModel("w71",48.6545d, 11.7659444444444d));
        addLocation(new LocationModel("w72",48.6540555555556d, 11.7654166666667d));
        addLocation(new LocationModel("w73",48.6535277777778d, 11.7649444444444d));
        addLocation(new LocationModel("w74",48.6535277777778d, 11.7656388888889d));
        addLocation(new LocationModel("w75",48.6537222222222d, 11.7665555555556d));
        addLocation(new LocationModel("w76",48.654d, 11.76775d));
        addLocation(new LocationModel("w77",48.6534722222222d, 11.7683055555556d));
        addLocation(new LocationModel("w78",48.6531388888889d, 11.7684722222222d));
        addLocation(new LocationModel("w79",48.6528611111111d, 11.7680277777778d));
        addLocation(new LocationModel("w80",48.6525555555556d, 11.7678055555556d));
        addLocation(new LocationModel("w81",48.6523611111111d, 11.7678055555556d));
        addLocation(new LocationModel("w82",48.6508888888889d, 11.7690833333333d));


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
                                new WaypointModel(getLocation("ggm")),

                                    new WaypointModel(getLocation("w6")),
                                    new WaypointModel(getLocation("w13")),
                                    new WaypointModel(getLocation("w9")),

                                new WaypointModel(getLocation("eingang"), Riddle.RIGHTWRONG),

                                    new WaypointModel(getLocation("w3")),
                                    new WaypointModel(getLocation("w5")),
                                    new WaypointModel(getLocation("w10")),
                                    new WaypointModel(getLocation("w8")),
                                    new WaypointModel(getLocation("w14")),
                                    new WaypointModel(getLocation("w11")),
                                    new WaypointModel(getLocation("w7")),
                                    new WaypointModel(getLocation("w19")),
                                    new WaypointModel(getLocation("w12")),
                                    new WaypointModel(getLocation("w21")),
                                    new WaypointModel(getLocation("w18")),
                                    new WaypointModel(getLocation("w16")),
                                    new WaypointModel(getLocation("w40")),
                                    new WaypointModel(getLocation("w23")),

                                new WaypointModel(getLocation("koloman"), Riddle.POSTCARDS),

                                    new WaypointModel(getLocation("w23")),
                                    new WaypointModel(getLocation("w40")),
                                    new WaypointModel(getLocation("w39")),
                                    new WaypointModel(getLocation("w38")),
                                    new WaypointModel(getLocation("w37")),
                                    new WaypointModel(getLocation("w43")),
                                    new WaypointModel(getLocation("w54")),
                                    new WaypointModel(getLocation("w53")),
                                    new WaypointModel(getLocation("w48")),
                                    new WaypointModel(getLocation("w49")),
                                    new WaypointModel(getLocation("w50")),

                                new WaypointModel(getLocation("kapellchen"), Riddle.CROSSWORD),

                                    new WaypointModel(getLocation("w64")),
                                    new WaypointModel(getLocation("w65")),
                                    new WaypointModel(getLocation("w66")),
                                    new WaypointModel(getLocation("w67")),
                                    new WaypointModel(getLocation("w12")),
                                    new WaypointModel(getLocation("w19")),
                                    new WaypointModel(getLocation("w7")),
                                    new WaypointModel(getLocation("w11")),
                                    new WaypointModel(getLocation("w14")),
                                    new WaypointModel(getLocation("w8")),
                                    new WaypointModel(getLocation("w10")),
                                    new WaypointModel(getLocation("w5")),
                                    new WaypointModel(getLocation("w3")),
                                    new WaypointModel(getLocation("w2")),
                                    new WaypointModel(getLocation("w9")),
                                    new WaypointModel(getLocation("w82")),

                                new WaypointModel(getLocation("ggm2"), Riddle.FINAL)
                        }
                )
        );
        addRoute(
                new RouteModel("Schwer", "sehr langggg",
                        new WaypointModel[]{
                            new WaypointModel(getLocation("ggm")),

                                new WaypointModel(getLocation("w6")),
                                new WaypointModel(getLocation("w13")),
                                new WaypointModel(getLocation("w9")),

                            new WaypointModel(getLocation("eingang"), Riddle.RIGHTWRONG),

                                new WaypointModel(getLocation("w3")),
                                new WaypointModel(getLocation("w5")),
                                new WaypointModel(getLocation("w10")),
                                new WaypointModel(getLocation("w8")),

                            new WaypointModel(getLocation("kreuzung")),

                                new WaypointModel(getLocation("w11")),
                                new WaypointModel(getLocation("w7")),
                                new WaypointModel(getLocation("w19")),
                                new WaypointModel(getLocation("w12")),
                                new WaypointModel(getLocation("w21")),
                                new WaypointModel(getLocation("w18")),
                                new WaypointModel(getLocation("w16")),
                                new WaypointModel(getLocation("w40")),
                                new WaypointModel(getLocation("w23")),

                            new WaypointModel(getLocation("koloman"), Riddle.POSTCARDS),

                                new WaypointModel(getLocation("w15")),
                                new WaypointModel(getLocation("w26")),
                                new WaypointModel(getLocation("w24")),
                                new WaypointModel(getLocation("w22")),
                                new WaypointModel(getLocation("w20")),
                                new WaypointModel(getLocation("w25")),
                                new WaypointModel(getLocation("w29")),
                                new WaypointModel(getLocation("w30")),
                                new WaypointModel(getLocation("w31")),
                                new WaypointModel(getLocation("w28")),
                                new WaypointModel(getLocation("w27")),
                                new WaypointModel(getLocation("w32")),
                                new WaypointModel(getLocation("w33")),
                                new WaypointModel(getLocation("w55")),

                            new WaypointModel(getLocation("see"), Riddle.NAVIGATION),

                                new WaypointModel(getLocation("w56")),
                                new WaypointModel(getLocation("w57")),
                                new WaypointModel(getLocation("w58")),
                                new WaypointModel(getLocation("w59")),
                                new WaypointModel(getLocation("w60")),

                            new WaypointModel(getLocation("kapellchen"), Riddle.CROSSWORD),

                                new WaypointModel(getLocation("w64")),
                                new WaypointModel(getLocation("w65")),
                                new WaypointModel(getLocation("w66")),
                                new WaypointModel(getLocation("w67")),
                                new WaypointModel(getLocation("w12")),
                                new WaypointModel(getLocation("w19")),
                                new WaypointModel(getLocation("w7")),
                                new WaypointModel(getLocation("w11")),
                                new WaypointModel(getLocation("w14")),
                                new WaypointModel(getLocation("w8")),
                                new WaypointModel(getLocation("w10")),
                                new WaypointModel(getLocation("w5")),
                                new WaypointModel(getLocation("w3")),
                                new WaypointModel(getLocation("w2")),
                                new WaypointModel(getLocation("w9")),
                                new WaypointModel(getLocation("w82")),

                            new WaypointModel(getLocation("ggm2"), Riddle.FINAL)
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
    private int routeNumber;
    private int routeProgress;

    public boolean pickRoute(int index) {
        if (index < 0 || index > routes.size()) return false;
        routeNumber = index;
        route = routes.get(index);
        routeProgress = -1;
        return true;
    }

    public WaypointModel nextWaypoint() {
        if (routeProgress+1 >= route.waypoints.length) return null;
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

    public float getDistanceUntilNextStop() {
        float dist = 0f;
        for (int i = routeProgress+1; i < route.waypoints.length-1; i++) {
            if (!route.waypoints[i].intermediate) break;
            dist += route.waypoints[i].location.location.distanceTo(route.waypoints[i+1].location.location);
        }
        return dist;
    }

    public WaypointModel currentStop() {
        if (routeProgress == -1) return null;
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

    public int getRouteProgress() { return routeProgress; }

    public int getRouteNumber() { return routeNumber; }

    public void continueFromSave(int routeNumber, int routeProgress) {
        this.routeNumber = routeNumber;
        route = routes.get(routeNumber);
        this.routeProgress = routeProgress;
    }

    public float distanceWaypointFind() {
        return 15f;
    }
    public float distanceStopFind() {
        return 10f;
    }
    public float distanceStopLose() {
        return 50f;
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
        CROSSWORD,
        NAVIGATION
    };

    private HashMap<inputQuestionID, String> inputQuestionAnswers;

    private void initializeInputQuestions() {
        inputQuestionAnswers = new HashMap<>();
        inputQuestionAnswers.put(inputQuestionID.CROSSWORD, "Astronaut");
        inputQuestionAnswers.put(inputQuestionID.NAVIGATION, "4.2");
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

        addQrCode(new RouteQrModel("ba02ab0e-aa12-456a-b1d0-912341590b6d", getLocation("ggm")));
        addQrCode(new RouteQrModel("17064bd6-5ee2-4b52-b397-d4beac844307", getLocation("eingang")));
        addQrCode(new RouteQrModel("6e40c051-df1f-4964-b77d-647c1d02265e", getLocation("kreuzung")));
        addQrCode(new RouteQrModel("d47bac64-3020-4a25-a5d6-1b781ac07d8a", getLocation("koloman")));
        addQrCode(new RouteQrModel("11558407-bcd0-4dd0-9bf8-a2766f750a08", getLocation("see")));
        addQrCode(new RouteQrModel("a7eecb31-a434-4bdb-b1ea-615193db4921", getLocation("kapellchen")));
        addQrCode(new RouteQrModel("59f3796e-4790-40c7-b360-6e5e1d43ef2a", getLocation("ggm2")));

        addQrCode(new RouteQrModel("e6c99c55-7b73-45c5-b8d7-30a6ec6c3f07", getLocation("house1")));
        addQrCode(new RouteQrModel("81837642-f990-4d3b-b84c-1ca7d710ab5f", getLocation("house2")));

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
        addQrCode(new RightWrongQrModel("d2c8868f-30b7-44e0-869b-feba728af40d", false));
        addQrCode(new RightWrongQrModel("82d35c5a-821f-4e78-ac07-37161e0dfc90", false));
        addQrCode(new RightWrongQrModel("8be83841-b271-4368-8078-f86dcb0e6d6c", false));
        addQrCode(new RightWrongQrModel("356bf08d-9ebf-4020-bc08-433ccc173bd1", false));
        addQrCode(new RightWrongQrModel("880b68c3-fe55-4ac6-bffc-40bbf93d3730", false));
    }

    public QrModel getQrCode(UUID id) {
        return qrCodes.get(id);
    }

    // ==============================================================================================================================================================================================
    //endregion
    //region                                                                        RIDDLES
    // ==============================================================================================================================================================================================

    public enum Riddle {
        NONE,
        POSTCARDS,
        CROSSWORD,
        NAVIGATION,
        RIGHTWRONG,
        FINAL
    }

    private void initializeVote() {
        correctVote = new int[2];
        correctVote[0] = whiteVote;
        correctVote[1] = pinkVote;
    }

    public final int whiteVote = 1;
    public final int redVote = 2;
    public final int pinkVote = 3;
    public int[] correctVote;

    // ==============================================================================================================================================================================================
    //endregion
}