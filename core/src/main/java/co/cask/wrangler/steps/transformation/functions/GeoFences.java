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

package co.cask.wrangler.steps.transformation.functions;

import com.github.filosganga.geogson.gson.GeometryAdapterFactory;
import com.github.filosganga.geogson.model.Coordinates;
import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.github.filosganga.geogson.model.positions.LinearPositions;
import com.github.filosganga.geogson.model.positions.Positions;
import com.github.filosganga.geogson.model.positions.SinglePosition;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * GeoFencing check based on location and polygon
 */
public class GeoFences {

  public static final Boolean evaluate(double latitude, double longitude, String geofence) {
    Coordinates location = Coordinates.of(longitude, latitude);
    Boolean inzone = false;
    Gson gson = new GsonBuilder()
        .registerTypeAdapterFactory(new GeometryAdapterFactory())
        .create();

    FeatureCollection featureCollection = gson.fromJson(geofence, FeatureCollection.class);
    for (Feature feature : featureCollection.features()) {
      inzone = inzone || pointInside(getPolygonAsList(feature), location);

    }
    return inzone;
  }

  public static final Boolean evaluate(String latitude, String longitude, String geofence) {
    return evaluate(Double.parseDouble(latitude), Double.parseDouble(longitude), geofence);
  }

  private static List<Coordinates> getPolygonAsList(Feature feature) {
    List<Coordinates> points = new ArrayList<>();
    Iterable<? extends Positions> positions = feature.geometry().positions().children();
    Iterator positionsIterator = positions.iterator();
    while (positionsIterator.hasNext()) {
      LinearPositions position = (LinearPositions) positionsIterator.next();
      Iterator<SinglePosition> singlePositionIterator = position.children().iterator();
      while (singlePositionIterator.hasNext()) {
        Coordinates coordinates = singlePositionIterator.next().coordinates();
        points.add(coordinates);
      }
    }
    return points;
  }

  private static Boolean pointInside(List<Coordinates> polygon, Coordinates location) {
    if ((polygon == null) || (location == null)) {
      return false;
    }
    polygon = closePolygon(polygon);

    int wn = 0;                                                               // the winding number counter
    for (int i = 0; i < polygon.size() - 1; i++) {                            // edge from V[i] to V[i+1]
      if (polygon.get(i).getLat() <= location.getLat()) {                     // start y <= P.y
        if (polygon.get(i + 1).getLat() > location.getLat()) {                  // an upward crossing
          if (isLeft(polygon.get(i), polygon.get(i + 1), location) > 0.0) {    // P left of edge
            ++wn;                                                             // have a valid up intersect
          }
        }
      } else {                                                                // start y > P.y (no test needed)
        if (polygon.get(i + 1).getLat() <= location.getLat()) {                 // a downward crossing
          if (isLeft(polygon.get(i), polygon.get(i + 1), location) < 0.0) {    // P right of edge
            --wn;                                                             // have a valid down intersect
          }
        }
      }
    }
    return (wn != 0);
  }

  private static double isLeft(Coordinates gp0, Coordinates gp1, Coordinates gpC) {
    double val = (gp1.getLon() - gp0.getLon()) * (gpC.getLat() - gp0.getLat()) -
        (gpC.getLon() - gp0.getLon()) * (gp1.getLat() - gp0.getLat());
    return val;
  }

  private static List<Coordinates> closePolygon(List<Coordinates> polygon) {
    if (polygon.isEmpty()) {
      return polygon;
    } else if (polygon.size() < 3) {
      return polygon;
    } else {
      if (polygon.get(0).equals(polygon.get(polygon.size() - 1))) {
        return polygon;
      } else {
        polygon.add(polygon.get(0));
        return polygon;
      }
    }
  }
}
