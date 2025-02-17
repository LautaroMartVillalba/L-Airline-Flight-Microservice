package ar.com.l_airline.services;

public class PriceGenerator {

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
        double originLatitudeGrades = originLatitudeToGrades + (double) originLatitudeToMinutes / 60 + (double) originLatitudeToSeconds / (60*60);
        double originLongitudeGrades = originLongitudeToGrades + (double) originLongitudeToMinutes / 60 + (double) originLongitudeToSeconds / (60*60);
        double destinyLatitudeGrades = destinyLatitudeToGrades + (double) destinyLatitudeToMinutes / 60 + (double) destinyLatitudeToSeconds / (60*60);
        double destinyLongitudeGrades = destinyLongitudeToGrades + (double) destinyLongitudeToMinutes / 60 + (double) destinyLongitudeToSeconds / (60*60);

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
