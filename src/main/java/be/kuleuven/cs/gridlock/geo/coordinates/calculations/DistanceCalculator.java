package be.kuleuven.cs.gridlock.geo.coordinates.calculations;

import be.kuleuven.cs.gridlock.geo.coordinates.Coordinates;

/**
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
public interface DistanceCalculator {

    /**
     * Calculate the distance between origin and destination
     *
     * @param origin first coordinate
     * @param destination second coordinate
     * @return distance between first and second coordinate
     */
    public double calculateDistance( Coordinates origin, Coordinates destination );

}