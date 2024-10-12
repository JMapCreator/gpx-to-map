package files;

/**
 * Holds extracted information from the GPX file
 *
 * @param gpxName   name of the GPX file
 * @param duration  formated duration of the activity (sum of each {@link io.jenetics.jpx.Track}'s duration)
 * @param distance total distance, in meters
 * @param elevation total elevation, in meters
 * @param speed average speed, in km/h
 */
public record ExtractedGpxResult(String gpxName, String duration, Integer distance, Integer elevation, float speed) {
}
