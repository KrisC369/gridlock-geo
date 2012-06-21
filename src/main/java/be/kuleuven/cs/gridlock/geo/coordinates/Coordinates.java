package be.kuleuven.cs.gridlock.geo.coordinates;

import be.kuleuven.cs.gridlock.geo.coordinates.calculations.DistanceCalculator;
import be.kuleuven.cs.gridlock.geo.coordinates.calculations.HaversineDistanceCalculator;
import java.util.List;

/**
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public class Coordinates {

    /**
     * Calculates the sum of all distances between subsequent coordinates in the list
     *
     * @param coordinates list of coordinates
     * @return sum of the distance
     */
    public static float calculateLength( List<Coordinates> coordinates ) {
        float distance = 0;
        Coordinates previous = null;

        for( Coordinates coordinate : coordinates ) {
            if( previous != null ) {
                distance += previous.distanceTo( coordinate );
            }

            previous = coordinate;
        }
        return distance;
    }

    static void changeDistanceCalculator( DistanceCalculator calculator ) {
        DISTANCE_CALCULATOR = calculator;
    }

    static void restoreDefaultDistanceCalculator() {
        changeDistanceCalculator( DEFAULT_DISTANCE_CALCULATOR );
    }

    public static final DistanceCalculator DEFAULT_DISTANCE_CALCULATOR = new HaversineDistanceCalculator();
    private static DistanceCalculator DISTANCE_CALCULATOR = DEFAULT_DISTANCE_CALCULATOR;

    public static Coordinates parseCoordinates( String coordinates ) {
        String[] pieces = coordinates.split( ",", 2 );
        return coordinatesAt( Float.parseFloat( pieces[0] ), Float.parseFloat( pieces[1] ) );
    }

    private final float latitude;
    private final float longitude;

    Coordinates( float latitude, float longitude ) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static Coordinates coordinatesAt( float latitude, float longitude ) {
        return new Coordinates( latitude, longitude );
    }

    public static Coordinates coordinatesAt( float... coordinates ) {
        return coordinatesAt( coordinates[0], coordinates[1] );
    }

    public static Coordinates coordinatesAt( double latitude, double longitude ) {
        return new Coordinates( (float)latitude, (float)longitude );
    }

    public static Coordinates coordinatesAt( double[] coordinates ) {
        return coordinatesAt( coordinates[0], coordinates[1] );
    }

    public static Coordinates coordinatesAt( double[] latitudeInDegrees, double[] longitudeInDegrees ) {
        double latitude = latitudeInDegrees[0] + latitudeInDegrees[1]/60.0 + latitudeInDegrees[2]/3600.0;
        double longitude = longitudeInDegrees[0] + longitudeInDegrees[1]/60.0 + longitudeInDegrees[2]/3600.0;
        return Coordinates.coordinatesAt( latitude, longitude );
    }

    public float getLatitude() {
        return this.latitude;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public double getLatitudeInRadians() {
        return this.latitude * Math.PI / 180.0;
    }

    public double getLongitudeInRadians() {
        return this.longitude * Math.PI / 180.0;
    }

    @Override
    public boolean equals(Object that) {
        if(this == that) return true;
        if(!(that instanceof Coordinates)) return false;
        Coordinates thatCoord = (Coordinates) that;

        return this.latitude == thatCoord.getLatitude() && this.longitude == thatCoord.getLongitude();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Float.floatToIntBits(this.latitude);
        hash = 67 * hash + Float.floatToIntBits(this.longitude);
        return hash;
    }

    public float distanceTo( Coordinates destination ) {
        if( destination == null ) {
            return Float.NaN;
        }

        return (float) DISTANCE_CALCULATOR.calculateDistance( this, destination );
    }
}