// Portions originated from https://github.com/docteurdiam/OpenGTS
/*
 *
 *  * Copyright © 2017 Cask Data, Inc.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  * use this file except in compliance with the License. You may obtain a copy of
 *  * the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  * License for the specific language governing permissions and limitations under
 *  * the License.
 *
 */

package co.cask.wrangler.utils.openGts;

/**
 * GeoPoint defines the coordinates. Every GeoPoint maps to actual location.
 */
public class GeoPoint implements Cloneable {

    private double latitude = 0.0;
    private double longitude = 0.0;

    /**
     * ** Constructor.
     * ** This creates a GeoPoint with latitude=0.0, and longitude=0.0
     **/
    public GeoPoint() {
        super();
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    /**
     * ** Copy Constructor.
     * ** This copies the specified argument GeoPoint to this constructed GeoPoint
     * ** @param gp  The GeoPoint to copy to this constructed GeoPoint
     **/
    public GeoPoint(GeoPoint gp) {
        this();
        this.setLatitude(gp.getLatitude());
        this.setLongitude(gp.getLongitude());
        // Note: does not clone "immutability"
    }

    /**
     * ** Constructor.
     * ** This creates a new GeoPoint with the specified latitude/longitude.
     * ** @param latitude  The latitude
     * ** @param longitude The longitude
     **/
    public GeoPoint(double latitude, double longitude) {
        this();
        this.setLatitude(latitude);
        this.setLongitude(longitude);
    }

    /**
     * ** Converts the specified degrees/minutes/seconds into degrees
     * ** @param deg  The degrees
     * ** @param min  The minutes
     * ** @param sec  The seconds
     * ** @return Decimal degrees
     **/
    public static double convertDmsToDec(double deg, double min, double sec) {
        double sign = (deg >= 0.0) ? 1.0 : -1.0;
        double d = Math.abs(deg);
        double m = Math.abs(min / 60.0);
        double s = Math.abs(sec / 3600.0);
        return sign * (d + m + s);
    }

    /**
     * ** Returns this GeoPoint
     * ** @return This GeoPoint
     **/
    public GeoPoint getGeoPoint() {
        return this;
    }

    /**
     * ** Gets the Latitude in degrees
     * ** @return The Latitude in degrees
     **/
    public double getLatitude() {
        return this.latitude;
    }

    /**
     * ** Sets the Latitude in degrees
     * ** @param lat  The Latitude
     **/
    public void setLatitude(double lat) {
        this.latitude = lat;
    }

    /**
     * ** Gets the 'Y' coordinate (same as Latitude)
     * ** @return The 'Y' coordinate
     **/
    public double getY() {
        return this.latitude;
    }

    /**
     * ** Gets the Longitude in degrees
     * ** @return The Longitude in degrees
     **/

    public double getLongitude() {
        return this.longitude;
    }

    /**
     * ** Sets the Longitude in degrees
     * ** @param lon  The Longitude
     **/
    public void setLongitude(double lon) {
        this.longitude = lon;
    }

    /**
     * ** Gets the 'X' coordinate (same as Longitude)
     * ** @return The 'X' coordinate
     **/
    public double getX() {
        return this.longitude;
    }

    /**
     * ** Sets the Latitude in degrees/minutes/seconds
     * ** @param deg  The degrees
     * ** @param min  The minutes
     * ** @param sec  The seconds
     **/
    public void setLatitude(double deg, double min, double sec) {
        this.setLatitude(GeoPoint.convertDmsToDec(deg, min, sec));
    }

    /**
     * ** Sets the Longitude in degrees/minutes/seconds
     * ** @param deg  The degrees
     * ** @param min  The minutes
     * ** @param sec  The seconds
     **/
    public void setLongitude(double deg, double min, double sec) {
        this.setLongitude(GeoPoint.convertDmsToDec(deg, min, sec));
    }
}
