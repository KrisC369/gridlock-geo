package be.kuleuven.cs.gridlock.geo.coordinates.calculations;

import be.kuleuven.cs.gridlock.geo.coordinates.Coordinates;

/**
 * Calculate the distance between two points using the HaverSine {@link http://en.wikipedia.org/wiki/Haversine_formula} formula
 * 
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public class HaversineDistanceCalculator implements DistanceCalculator {

    private static final int EARTH_RADIUS = 6371000;

    @Override
    public double calculateDistance( Coordinates origin, Coordinates destination ) {
        double lonA = origin.getLongitudeInRadians();
        double latA = origin.getLatitudeInRadians();
        double lonB = destination.getLongitudeInRadians();
        double latB = destination.getLatitudeInRadians();
        double dLon = lonB - lonA;
        double dLat = latB - latA;
        double a = Math.sin( dLat / 2 ) * Math.sin( dLat / 2 ) + Math.sin( dLon / 2 ) * Math.sin( dLon / 2 ) * Math.cos( latA ) * Math.cos( latB );
        double c = 2 * Math.atan2( Math.sqrt( a ), Math.sqrt( 1 - a ) );
        return EARTH_RADIUS * c;
    }

}