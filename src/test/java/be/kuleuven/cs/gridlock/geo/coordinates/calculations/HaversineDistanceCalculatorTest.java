package be.kuleuven.cs.gridlock.geo.coordinates.calculations;

import be.kuleuven.cs.gridlock.geo.coordinates.Coordinates;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
// TODO extract general DistanceCalculatorTest superclass to facilitate testing of other future distance calculators
@RunWith( Parameterized.class )
public class HaversineDistanceCalculatorTest {

    private final Coordinates a;
    private final Coordinates b;
    private final double distance;
    private final HaversineDistanceCalculator calculator;
    private final double delta;

    public HaversineDistanceCalculatorTest( Coordinates a, Coordinates b, double distance ) {
        this.a = a;
        this.b = b;
        this.distance = distance;

        this.delta = distance * 0.01;

        this.calculator = new HaversineDistanceCalculator();
    }

    /**
     * Test of calculateDistance method, of class HaversineDistanceCalculator.
     */
    @Test
    public void testCalculateDistance() {
        assertEquals( distance, this.calculator.calculateDistance( a, b ), this.delta );
        assertEquals( distance, this.calculator.calculateDistance( b, a ), this.delta );
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
            { Coordinates.coordinatesAt( 51.1927735, 4.4350355 ), Coordinates.coordinatesAt( 51.1927735, 4.4350355 ), 0 },
            { Coordinates.coordinatesAt( 51.1921979, 4.4345034 ), Coordinates.coordinatesAt( 51.1921979, 4.4346034 ), 6.969 },
            { Coordinates.coordinatesAt( 51.1927735, 4.4350355 ), Coordinates.coordinatesAt( 51.1921979, 4.4345034 ), 73.97 },
            { Coordinates.coordinatesAt( 51.1927735, 4.4250355 ), Coordinates.coordinatesAt( 51.1921979, 4.4345034 ), 662.9 },
            { Coordinates.coordinatesAt( 51.2927735, 4.4250355 ), Coordinates.coordinatesAt( 51.1921979, 4.4345034 ), 11.20 * 1000 },
            { Coordinates.coordinatesAt( 51.2927735, 4.1150355 ), Coordinates.coordinatesAt( 51.1921979, 4.4345034 ), 24.89 * 1000 },
            { Coordinates.coordinatesAt( 53.4927735, 3.1150355 ), Coordinates.coordinatesAt( 51.1921979, 4.4345034 ), 271.0 * 1000 }
        };
        return Arrays.asList( data );
    }
}
