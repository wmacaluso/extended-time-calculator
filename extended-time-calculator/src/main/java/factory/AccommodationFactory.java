package factory;

import model.Accommodation;

/**
 * Created by will on 10/23/14.
 */
public class AccommodationFactory {
    public static Accommodation createReaderAccommodation(int studentTimerId) {
        return new Accommodation(Accommodation.READER, studentTimerId);
    }

    public static Accommodation createWordProcAccommodation(int studentTimerId) {
        return new Accommodation(Accommodation.WORD_PROCESSOR, studentTimerId);
    }

    public static Accommodation createVoiceRecAccommodation(int studentTimerId) {
        return new Accommodation(Accommodation.VOICE_RECOGNITION, studentTimerId);
    }

    public static Accommodation createScribeAccommodation(int studentTimerId) {
        return new Accommodation(Accommodation.SCRIBE, studentTimerId);
    }

    public static Accommodation createCalculatorAccommodation(int studentTimerId) {
        return new Accommodation(Accommodation.CALCULATOR, studentTimerId);
    }

    public static Accommodation createOtherAccommodation(String otherDesc, int studentTimerId) {
        return new Accommodation(Accommodation.OTHER, otherDesc, studentTimerId);
    }
}
