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

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * Utility class for GeoFencing
 */
public class GeoTools {

    /**
     * ** Returns true if the specified Object array is null/empty
     * ** @param A  The array instance
     * ** @return True if the specified array is null/empty
     **/
    public static <T> boolean isEmpty(T A[]) {
        return ((A == null) || (A.length == 0));
    }

    /**
     * ** Creates a new array with the specified object appended to the end of the specified array
     * ** @param list  The array to which the object will be appended
     * ** @param obj   The object to append to the specified array
     * ** @return The new Object array to which the specified object instance was appended
     **/
    public static <T> T[] add(T list[], T obj) {
        return GeoTools.insert(list, obj, -1);
    }

    /**
     * ** Copies the specified Collection to a new array of the specified Class type
     * ** @param list The Collection to copy to the new array
     * ** @param type The Class type of the return array
     * ** @return An array containing the elements from the Collection
     **/
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<?> list, Class<T> type) {
        if (type == null) {
            type = (Class<T>) Object.class;
        }
        if (list != null) {
            T array[] = (T[]) Array.newInstance(type, list.size());  // "unchecked cast"
            return list.toArray(array);
        } else {
            return (T[]) Array.newInstance(type, 0);  // "unchecked cast"
        }
    }

    /**
     * ** Creates a new array with the specified object inserted into specified array
     * ** @param list  The array to which the object will be inserted
     * ** @param obj   The object to insert into the specified array
     * ** @param index The location where the object will be inserted
     * ** @return The new Object array to which the specified object instance was inserted
     **/
    @SuppressWarnings("unchecked")
    public static <T> T[] insert(T list[], T obj, int index)
    // throws ArrayStoreException
    {
        if (list != null) {
            int ndx = ((index > list.length) || (index < 0)) ? list.length : index;
            Class type = list.getClass().getComponentType();
            int size = (list.length > ndx) ? (list.length + 1) : (ndx + 1);
            T array[] = (T[]) Array.newInstance(type, size);  // "unchecked cast"
            if (ndx > 0) {
                int maxLen = (list.length >= ndx) ? ndx : list.length;
                System.arraycopy(list, 0, array, 0, maxLen);
            }
            array[ndx] = obj; // <-- may throw ArrayStoreException
            if (ndx < list.length) {
                int maxLen = list.length - ndx;
                System.arraycopy(list, ndx, array, ndx + 1, maxLen);
            }
            return array;
        } else {
            return null;
        }
    }
}
