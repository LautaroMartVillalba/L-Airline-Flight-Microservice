package ar.com.l_airline.services;

import ar.com.l_airline.exceptionHandler.custom_exceptions.InvalidFormatException;

public class PriceGenerator {

    private static final int EARTH_RADIO = 6371;

    /**
     * Calculates the flight price based on the geographical coordinates of the origin and destination.
     * The coordinates are expected to be in a specific format (e.g., "DDDMMSSX" where DDD is degrees,
     * MM is minutes, SS is seconds, and X is the direction (N, S, E, O)).
     *
     * @param originLatitude the latitude of the origin airport in the format "DDDMMSSX"
     * @param originLongitude the longitude of the origin airport in the format "DDDMMSSX"
     * @param destinyLatitude the latitude of the destination airport in the format "DDDMMSSX"
     * @param destinyLongitude the longitude of the destination airport in the format "DDDMMSSX"
     * @return the calculated flight price based on the distance between the origin and destination
     */
    public static double calc(String originLatitude, String originLongitude, String destinyLatitude, String destinyLongitude){
        //Parse from grades to radians
        double originLatitudeRadians = convertCoordinateToRadiansInDouble(originLatitude);
        double originLongitudeRadians = convertCoordinateToRadiansInDouble(originLongitude);
        double destinyLatitudeRadians = convertCoordinateToRadiansInDouble(destinyLatitude);
        double destinyLongitudeRadians = convertCoordinateToRadiansInDouble(destinyLongitude);

        //Latitude's and longitude's differences
        double latitudeDif = originLatitudeRadians - destinyLatitudeRadians;
        double longitudeDif = originLongitudeRadians - destinyLongitudeRadians;

        //sin²(latitudeDif)
        double sin2A = (1 - Math.cos(latitudeDif))/2;
        //sin²(longitudeDif)
        double sin2B = (1 - Math.cos(longitudeDif))/2;
        double cosOriginLatitude = Math.cos(originLatitudeRadians);
        double cosDestinyLatitude = Math.cos(destinyLatitudeRadians);

        //√(sin²(latitudeDif) + cos(originLatitudeRadians) * cos(destinyLatitudeRadians) * sin²(longitudeDif))
        double squareRoot = Math.sqrt(sin2A + cosOriginLatitude * cosDestinyLatitude * sin2B);

        double squareRootArcsin = Math.asin(squareRoot);

        double distance = 2 * EARTH_RADIO * squareRootArcsin;

        return setPrice(distance);
    }

    private static boolean validateFormat(String coordinate){
        if (coordinate == null || coordinate.length() != 8){
            return false;
        }

        for (int i = 0; i < 7; i ++){
            if (!Character.isDigit(coordinate.charAt(i))){
                return false;
            }
        }

        if (!coordinate.endsWith("S") || !coordinate.endsWith("N") || !coordinate.endsWith("E")|| !coordinate.endsWith("O")){
            return false;
        }
        
        return true;
    }

    private static double convertCoordinateToRadiansInDouble(String coordinate){

        if (validateFormat(coordinate)){
            throw new InvalidFormatException("The received coordinate is not matching with the expect format." +
                    " Please, follow the DDDMMSSX format, where DDD is degrees, MM es minutes, SS is seconds" +
                    " and X is orientation.");
        }

        double degrees = Integer.parseInt(coordinate.substring(0,3));
        double minutes = Integer.parseInt(coordinate.substring(4,6));
        double seconds = Integer.parseInt(coordinate.substring(7,8));

        double toDecimal = degrees + minutes / 60 + seconds / 3600;

        char direction = coordinate.charAt(coordinate.length() -1);
        if (direction == 'S' || direction == 'O'){
            toDecimal = (toDecimal * (-1));
        }

        toDecimal = Math.toRadians(toDecimal);

        return  toDecimal;
    }

    private static double setPrice(double distance){

        if (distance > 19000) return 3000;
        if (distance > 17000) return 2800;
        if (distance > 15000)  return 2500;
        if (distance > 13000) return 2100;
        if (distance > 11000) return 1900;
        if (distance > 9000) return 1700;
        if (distance > 7000) return 1400;
        if (distance > 5000) return 1200;
        if (distance > 3000) return 900;
        if (distance > 1000) return 800;
        //Return 500 if distance < 1000 km
        return 500;
    }
}
