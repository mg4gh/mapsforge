/*
 * Copyright 2013-2014 Ludwig M Brinckmann
 * Copyright 2015-2016 devemux86
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.mapsforge.samples.android;

import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import org.mapsforge.core.graphics.Color;
import org.mapsforge.core.graphics.Filter;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.MapPosition;
import org.mapsforge.core.util.Parameters;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidPreferences;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.util.MapViewPositionObserver;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.cache.TileStore;
import org.mapsforge.map.layer.cache.TwoLevelTileCache;
import org.mapsforge.map.layer.debug.TileCoordinatesLayer;
import org.mapsforge.map.layer.debug.TileGridLayer;
import org.mapsforge.map.layer.overlay.Polyline;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.layer.tilestore.TileStoreLayer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.common.PreferencesFacade;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Viewer with tile grid and coordinates visible and frame counter displayed.
 */
public class DiagnosticsMapViewer extends DefaultTheme {

    protected PreferencesFacade preferencesFacade2;
    protected TileCache tileCache = null;
    private MapViewPositionObserver observer;
    private TileStoreLayer tileStoreLayer = null;
    private TileRendererLayer tileRendererLayer = null;

    @Override
    protected void createSharedPreferences() {
        super.createSharedPreferences();
        this.preferencesFacade2 = new AndroidPreferences(this.getSharedPreferences(getPersistableId()+"-2", MODE_PRIVATE));
    }

    @Override
    protected void createLayers() {
//        super.createLayers();
        tileRendererLayer = AndroidUtil.createTileRendererLayer(this.tileCaches.get(0),
                mapView.getModel().mapViewPosition, getMapFile(), getRenderTheme(), false, true, false,
                getHillsRenderConfig());
        tileRendererLayer.setAlpha(1.0f);
        this.mapView.getLayerManager().getLayers().add(tileRendererLayer);

        tileStoreLayer = new TileStoreLayer(tileCache,
                this.mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE, true);
        tileStoreLayer.setAlpha(0.5f);
        tileStoreLayer.setParentTilesRendering(Parameters.ParentTilesRendering.OFF);

        mapView.getLayerManager().getLayers().add(tileStoreLayer);
//        mapView.getModel().displayModel.setFixedTileSize(256);

//        // Add a grid layer and a layer showing tile coordinates
//        mapView.getLayerManager().getLayers().add(new TileGridLayer(AndroidGraphicFactory.INSTANCE, this.mapView.getModel().displayModel));
//        TileCoordinatesLayer tileCoordinatesLayer = new TileCoordinatesLayer(AndroidGraphicFactory.INSTANCE, this.mapView.getModel().displayModel);
//        tileCoordinatesLayer.setDrawSimple(true);
//        mapView.getLayerManager().getLayers().add(tileCoordinatesLayer);
//
//        // Enable frame counter
//        mapView.getFpsCounter().setVisible(true);
        // xx
    }


    @Override
    protected String getMapFileName() {
        return "bw.map";
    }

    @Override
    protected MapPosition getInitialPosition() {
        return new MapPosition(new LatLong(49.4, 8.7), (byte) 11);
    }

    @Override
    protected void createMapViews() {
        super.createMapViews();

        SeekBar sb1 = new SeekBar(this);
        sb1.setProgress(50);
        sb1.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tileStoreLayer.setAlpha(progress/100.0f);
                if (progress == 0){
                    tileStoreLayer.setVisible(false);
                } else {
                    tileStoreLayer.setVisible(true);
                }
                mapView.getModel().mapViewPosition.setCenter(mapView.getModel().mapViewPosition.getCenter());
                System.out.println(progress);
            }
        });
        SeekBar sb2 = new SeekBar(this);
        sb2.setProgress(100);
        sb2.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tileRendererLayer.setAlpha(progress/100.0f);
                if (progress == 0){
                    tileRendererLayer.setVisible(false);
                } else {
                    tileRendererLayer.setVisible(true);
                }
//                mapView.getModel().mapViewPosition.setCenter(mapView.getModel().mapViewPosition.getCenter());
                ((MapViewPosition)mapView.getModel().mapViewPosition).notifyObservers();
                System.out.println(progress);
            }
        });


        LinearLayout ll  = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams llParams=new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        RelativeLayout rl = (RelativeLayout)mapView.getParent();
        rl.addView(ll,llParams);


        LinearLayout.LayoutParams sb1Params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams sb2Params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);


        ll.addView(sb2,sb2Params);
        ll.addView(sb1,sb1Params);

    }

    @Override
    protected void createTileCaches() {
        super.createTileCaches();

        // to use a tile store you provide it as a cache (which is pre-filled and never purges any files.
        // additionally you should use a memory tile store for faster refresh.
        TileStore tileStore = new TileStore(new File(getExternalFilesDir(null), "tilestore2"), ".png", AndroidGraphicFactory.INSTANCE);
        InMemoryTileCache memoryTileCache = new InMemoryTileCache(AndroidUtil.getMinimumCacheSize(this,
                this.mapView.getModel().displayModel.getTileSize(),
                this.mapView.getModel().frameBufferModel.getOverdrawFactor(), this.getScreenRatio()));
        tileCache = new TwoLevelTileCache(memoryTileCache, tileStore);
        this.tileCaches.add( tileCache );
    }

}
