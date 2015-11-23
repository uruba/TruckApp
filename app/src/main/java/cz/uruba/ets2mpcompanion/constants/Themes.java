package cz.uruba.ets2mpcompanion.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import cz.uruba.ets2mpcompanion.R;

public class Themes {
    private static final Map<String, Integer> themeList;

    static {
        Map<String, Integer> themeMap = new HashMap<>();
        themeMap.put("RedTheme", R.style.RedTheme);
        themeMap.put("PinkTheme", R.style.PinkTheme);
        themeMap.put("PurpleTheme", R.style.PurpleTheme);
        themeMap.put("DeepPurpleTheme", R.style.DeepPurpleTheme);
        themeMap.put("IndigoTheme", R.style.IndigoTheme);
        themeMap.put("BlueTheme", R.style.BlueTheme);
        themeMap.put("LightBlueTheme", R.style.LightBlueTheme);
        themeMap.put("CyanTheme", R.style.CyanTheme);
        themeMap.put("TealTheme", R.style.TealTheme);
        themeMap.put("GreenTheme", R.style.GreenTheme);
        themeMap.put("LightGreenTheme", R.style.LightGreenTheme);
        themeMap.put("LimeTheme", R.style.LimeTheme);
        themeMap.put("YellowTheme", R.style.YellowTheme);
        themeMap.put("AmberTheme", R.style.AmberTheme);
        themeMap.put("DeepOrangeTheme", R.style.DeepOrangeTheme);

        themeList = Collections.unmodifiableMap(themeMap);
    }

    public static int getThemeStyle(String theme) {
        if (themeList.containsKey(theme)) {
            return themeList.get(theme);
        }

        return -1;
    }
}
