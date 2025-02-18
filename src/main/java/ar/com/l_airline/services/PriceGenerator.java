package ar.com.l_airline.services;

public class PriceGenerator {

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
        int earthRadio = 6371;
        //Parse to int the origin latitude data
        double originLatitudeToGrades = Integer.parseInt(originLatitude.substring(0,3));
        double originLatitudeToMinutes = Integer.parseInt(originLatitude.substring(4,6));
        double originLatitudeToSeconds = Integer.parseInt(originLatitude.substring(7,8));

        //Parse to int the origin longitude data
        double originLongitudeToGrades = Integer.parseInt(originLongitude.substring(0,3));
        double originLongitudeToMinutes = Integer.parseInt(originLongitude.substring(4,6));
        double originLongitudeToSeconds = Integer.parseInt(originLongitude.substring(7,8));

        //Parse to int the destiny latitude data
        double destinyLatitudeToGrades = Integer.parseInt(destinyLatitude.substring(0,3));
        double destinyLatitudeToMinutes = Integer.parseInt(destinyLatitude.substring(4,6));
        double destinyLatitudeToSeconds = Integer.parseInt(destinyLatitude.substring(7,8));

        //Parse to int the destiny longitude data
        double destinyLongitudeToGrades = Integer.parseInt(destinyLongitude.substring(0,3));
        double destinyLongitudeToMinutes = Integer.parseInt(destinyLongitude.substring(4,6));
        double destinyLongitudeToSeconds = Integer.parseInt(destinyLongitude.substring(7,8));

        //Parse to grades latitude and longitude received data
        double originLatitudeGrades = originLatitudeToGrades + originLatitudeToMinutes / 60 + originLatitudeToSeconds / (60*60);
        double originLongitudeGrades = originLongitudeToGrades + originLongitudeToMinutes / 60 + originLongitudeToSeconds / (60*60);
        double destinyLatitudeGrades = destinyLatitudeToGrades + destinyLatitudeToMinutes / 60 + destinyLatitudeToSeconds / (60*60);
        double destinyLongitudeGrades = destinyLongitudeToGrades + destinyLongitudeToMinutes / 60 + destinyLongitudeToSeconds / (60*60);

        //If any coordinate comes from South or West; set negative value
        if (originLatitude.endsWith("S")){
            originLatitudeGrades = originLatitudeGrades * (-1);
        }
        if (originLongitude.endsWith("O")){
            originLongitudeGrades = originLongitudeGrades * (-1);
        }
        if (destinyLatitude.endsWith("S")){
            destinyLatitudeGrades = destinyLatitudeGrades * (-1);
        }
        if (destinyLongitude.endsWith("O")){
            destinyLongitudeGrades = destinyLongitudeGrades * (-1);
        }

        //Parse from grades to radians
        double originLatitudeRadians = Math.toRadians(originLatitudeGrades);
        double originLongitudeRadians = Math.toRadians(originLongitudeGrades);
        double destinyLatitudeRadians = Math.toRadians(destinyLatitudeGrades);
        double destinyLongitudeRadians = Math.toRadians(destinyLongitudeGrades);

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

        double distance = 2 * earthRadio * squareRootArcsin;

        if (distance > 19000){
            return 3000;
        }
        if (distance > 17000){
            return 2800;
        }
        if (distance > 15000) {
            return 2500;
        }
        if (distance > 13000){
            return 2100;
        }
        if (distance > 11000){
            return 1900;
        }
        if (distance > 9000){
            return 1700;
        }
        if (distance > 7000){
            return 1400;
        }
        if (distance > 5000){
            return 1200;
        }
        if (distance > 3000){
            return 900;
        }
        if (distance > 1000){
            return 800;
        }

        //Return 500 if distance < 1000 km
        return 500;
    }

}
