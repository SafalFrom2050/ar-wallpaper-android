package com.arwallpaper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WallpaperStoreTest {

    @Test
    public void updateIndex() {
        WallpaperStore.updateIndex(-1);
        assertEquals(WallpaperStore.selectedIndex, 7);
    }
}