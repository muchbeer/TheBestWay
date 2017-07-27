package com.example.muchbeer.thebestway.setting;

import com.example.muchbeer.thebestway.R;

/**
 * Created by muchbeer on 27/07/2017.
 */

public class Util {

    /**
     * Helper method to provide the icon resource id according to the weather condition id returned
     * by the OpenWeatherMap call.
     * @param weatherId from OpenWeatherMap API response
     * @return resource id for the corresponding icon. -1 if no relation is found.
     */
    public static int getIconResourceForWeatherCondition(int weatherId) {
        // Based on weather code data found at:
        // http://bugs.openweathermap.org/projects/api/wiki/Weather_Condition_Codes
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.recycler;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.recycler;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.recycler;
        } else if (weatherId == 511) {
            return R.drawable.recycler;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.recycler;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.recycler;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.recycler;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.recycler;
        } else if (weatherId == 800) {
            return R.drawable.recycler;
        } else if (weatherId == 801) {
            return R.drawable.recycler;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.recycler;
        }
        return -1;
    }
}
