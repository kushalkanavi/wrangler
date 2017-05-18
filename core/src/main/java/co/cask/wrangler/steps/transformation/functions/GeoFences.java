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

import co.cask.wrangler.utils.openGts.GeoPoint;
import co.cask.wrangler.utils.openGts.GeoPolygon;
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
 *
 */
public class GeoFences {

    public static final Boolean evaluate(double latitude, double longitude, String geofence){
        return evaluate(new Double(latitude).longValue(), new Double(longitude).longValue(), geofence);
    }

    public static final Boolean evaluate(String latitude, String longitude, String geofence){
        return evaluate(Long.parseLong(latitude), Long.parseLong(longitude), geofence);
    }

    public static final Boolean evaluate(long latitude, long longitude, String geofence){
        GeoPoint location = new GeoPoint(latitude, longitude);
        Boolean inzone = false;
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new GeometryAdapterFactory())
                .create();

        List<GeoPolygon> fences = new ArrayList<>();
        FeatureCollection featureCollection = gson.fromJson(geofence, FeatureCollection.class);

        for(Feature feature : featureCollection.features()){
            List<GeoPoint> points = new ArrayList<>();
            Iterable<? extends Positions> positions = feature.geometry().positions().children();
            Iterator i$1 = positions.iterator();
            while (i$1.hasNext()){
                LinearPositions position = (LinearPositions) i$1.next();
                Iterator<SinglePosition> i$2 = position.children().iterator();
                while(i$2.hasNext()){
                    Coordinates coordinates = i$2.next().coordinates();
                    points.add(new GeoPoint(coordinates.getLat(), coordinates.getLon()));
                }
            }
            fences.add(new GeoPolygon(points));
        }

        for (GeoPolygon fence: fences){
            inzone = inzone || fence.isPointInside(location);
        }
        return inzone;
    }
}
