package fill;

import rasterize.Raster;

public class SeedFiller implements Filler {
    private Raster raster;
    private int x, y;
    private int backgroundColor;

    public SeedFiller(Raster raster, int backgroundColor, int x, int y) {
        this.raster = raster;
        this.backgroundColor = backgroundColor;
        this.x = x;
        this.y = y;
    }

    @Override
    public void fill() {
        seedFill(x, y);
    }

    private void seedFill(int x, int y) {
        // 1. načtu barvu pixelu na souřadnici x, y

        // 2. podmínka: pokud se barva pozadá rovná načtené -> obarvuji

        if (x < 0 || x >= raster.getWidth() || y < 0 || y >= raster.getHeight())
            return;

        int pixelColor = raster.getPixel(x, y);

        if(pixelColor != backgroundColor)
            return;

        // 3. obarvim
        raster.setPixel(x, y, 0xffffff);

        // 4. zavolám pro 4 sousedy
        seedFill(x + 1, y);
        seedFill(x - 1, y);
        seedFill(x, y + 1);
        seedFill(x, y - 1);
    }
}
