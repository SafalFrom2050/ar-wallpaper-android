package com.arwallpaper;

public class WallpaperStore {
    public static String[] wallpapers = {
            "wallpaper.png",
            "wallpaper2.png",
            "wallpaper3.png",
            "wallpaper4.png",
            "wallpaper5.png",
            "wallpaper6.png",
            "wallpaper7.png",
            "wallpaper8.png",
            "wallpaper9.png"
    };

    public static int selectedIndex = 0;


    public static void updateIndex(int diff) {
        int total = wallpapers.length;

        if (selectedIndex + diff >= total) {
            updateIndex(selectedIndex + diff - total - 1);
        } else if (selectedIndex + diff < 0) {
            updateIndex(total + diff - 1);
        }else{
            WallpaperStore.selectedIndex += diff;
        }
    }
}
