package be.kuleuven.cs.gridlock.geo.coordinates;

import be.kuleuven.cs.gridlock.geo.coordinates.calculations.DistanceCalculator;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;

/**
 * @author Rutger Claes <rutger.claes@cs.kuleuven.be>
 */
@RunWith( Parameterized.class )
public class CoordinatesTest {

    private final float latitude;
    private final float longitude;
    private final Coordinates coordinate;

    public CoordinatesTest( float latitude, float longitude ) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.coordinate = new Coordinates( latitude, longitude );
    }

    private Coordinates createCopy() {
        return new Coordinates( this.latitude, this.longitude );
    }

    private Coordinates[] createVariations() {
        return new Coordinates[] {
            new Coordinates( latitude + 0.1f, longitude ),
            new Coordinates( latitude, longitude + 0.1f ),
            new Coordinates( latitude + 0.1f, longitude - 0.1f )
        };
    }

    @Before
    public void resetDistanceCalculator() {
        Coordinates.restoreDefaultDistanceCalculator();
    }

    @Test
    public void testEquals() {
        {
            assertFalse( this.coordinate.equals( null ) );
            assertFalse( this.coordinate.equals( "foo" ) );
            assertTrue( this.coordinate.equals( this.coordinate ) );
        }

        {
            Coordinates equalCoordinates = createCopy();
            assertTrue( this.coordinate.equals( equalCoordinates ) );
        }

        {
            for( Coordinates nonEqualCoordinates : this.createVariations() ) {
                assertFalse( this.coordinate.equals( nonEqualCoordinates ) );
            }
        }
    }

    @Test
    public void testHashCode() {
        {
            Coordinates equalCoordinates = createCopy();
            assertEquals( this.coordinate.hashCode(), equalCoordinates.hashCode() );
        }

        {
            for( Coordinates nonEqualCoordinates : this.createVariations() ) {
                assertFalse( this.coordinate.hashCode() ==  nonEqualCoordinates.hashCode() );
            }
        }
    }

    @Test
    public void testCoordinatesAsRadians() {
        final double latitudeInRadians  = this.latitude * Math.PI / 180.0;
        final double longitudeInRadians = this.longitude * Math.PI / 180.0;

        assertEquals( latitudeInRadians, this.coordinate.getLatitudeInRadians(), 0.000001 );
        assertEquals( longitudeInRadians, this.coordinate.getLongitudeInRadians(), 0.000001 );
    }

    @Test
    public void testCoordinateParsing() {
        StringBuilder builder = new StringBuilder();
        builder.append( this.latitude ).append( "," ).append(  this.longitude );
        String coordinateString = builder.toString();

        assertEquals( this.coordinate, Coordinates.parseCoordinates( coordinateString ) );
    }

    @Test
    public void testIllegalDistanceCalculation() {
        assertTrue( ((Float)coordinate.distanceTo( null )).isNaN() );
    }

    @Test
    public void testDistanceCalculationDelegation() {
        DistanceCalculator mockCalculator = Mockito.mock( DistanceCalculator.class );
        Coordinates mockCoordinate = Mockito.mock( Coordinates.class );
        Double distance = -1d;

        Mockito.when( mockCalculator.calculateDistance( coordinate, mockCoordinate ) ).thenReturn( distance );
        Coordinates.changeDistanceCalculator( mockCalculator );
        assertEquals( coordinate.distanceTo( mockCoordinate ), distance, 0 );

        Mockito.verify( mockCalculator ).calculateDistance( coordinate, mockCoordinate );
    }

    @Test
    public void testCoordinatesAtFloatArray() {
        Coordinates test = Coordinates.coordinatesAt( new float[] { this.latitude, this.longitude } );
        assertEquals( coordinate, test );
    }

    @Test
    public void testCoordinatesAtDoubleArray() {
        Coordinates test = Coordinates.coordinatesAt( new double[] { this.latitude, this.longitude } );
        assertEquals( coordinate, test );
    }

    @Test
    public void testIllegalCalculateLenght() {
        {
            assertEquals( 0, Coordinates.calculateLength( Collections.EMPTY_LIST ), 0 );
        }

        {
            assertEquals( 0, Coordinates.calculateLength( Arrays.asList( coordinate ) ), 0 );
        }
    }

    @Test
    public void testCalculateLength() {
        DistanceCalculator mockCalculator = Mockito.mock( DistanceCalculator.class );

        Coordinates.changeDistanceCalculator( mockCalculator );
        Coordinates mock1 = Mockito.mock( Coordinates.class );
        Coordinates mock2 = Mockito.mock( Coordinates.class );
        Coordinates mock3 = Mockito.mock( Coordinates.class );

        Mockito.when( mock1.distanceTo( mock2 ) ).thenCallRealMethod();
        Mockito.when( mock2.distanceTo( mock3 ) ).thenCallRealMethod();
        Mockito.when( mockCalculator.calculateDistance( mock1, mock2 ) ).thenReturn( 1d );
        Mockito.when( mockCalculator.calculateDistance( mock2, mock3 ) ).thenReturn( 2d );

        assertEquals( 3, Coordinates.calculateLength( Arrays.asList( mock1, mock2, mock3 ) ), 0 );

        Mockito.verify( mockCalculator ).calculateDistance( mock1, mock2 );
        Mockito.verify( mockCalculator ).calculateDistance( mock2, mock3 );
    }

    @Test
    public void testCoordinatesAtDegrees() {
        Coordinates alt = Coordinates.coordinatesAt( convertToDegrees( this.latitude ), convertToDegrees( this.longitude ) );
        assertEquals( this.latitude, alt.getLatitude(), 0.0000001 );
        assertEquals( this.longitude, alt.getLongitude(), 0.0000001 );
    }

    private double[] convertToDegrees( double decimal ) {
        double degrees = Math.floor( decimal );
        double minutesInDecimal = ( decimal - degrees ) * 60.0;
        double minutes = Math.floor( minutesInDecimal );
        double secondsInDecimal = ( minutesInDecimal - minutes ) * 60.0;

        return new double[] { degrees, minutes, secondsInDecimal };
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
            { 51.1927735f, 4.4350355f }
        };

        return Arrays.asList( data );
    }
}