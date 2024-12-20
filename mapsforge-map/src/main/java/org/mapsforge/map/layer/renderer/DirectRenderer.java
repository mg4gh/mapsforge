/*
 * Copyright 2010, 2011, 2012, 2013 mapsforge.org
 * Copyright 2014-2015 Ludwig M Brinckmann
 * Copyright 2014-2016 devemux86
 * Copyright 2017 usrusr
 * Copyright 2018 Fabrice Fontaine
 * Copyright 2024 Sublimis
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
package org.mapsforge.map.layer.renderer;

import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.Tile;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.hills.HillsRenderConfig;
import org.mapsforge.map.layer.labels.TileBasedLabelStore;

/**
 * Note (2024): The DirectRenderer is deprecated, it is here just for compatibility.
 * It doesn't contain any new functionality not already covered by its superclass, DatabaseRenderer.
 * The deterministic labels also made calls to the {@link TileRefresher} interface obsolete.
 * <p>
 * The DirectRenderer renders map tiles by reading from a {@link MapDataStore}.
 * Just rendering the tiles without any memory of what happened before.
 *
 * @see <a href="https://github.com/mapsforge/mapsforge/issues/1085">mapsforge/mapsforge#1085</a>
 */
public class DirectRenderer extends DatabaseRenderer {

    /**
     * Constructs a new DirectRenderer.
     * There are three possible configurations:
     * 1) render labels directly onto tiles: renderLabels == true && tileCache != null
     * 2) do not render labels but cache them: renderLabels == false && labelStore != null
     * 3) do not render or cache labels: renderLabels == false && labelStore == null
     *
     * @param mapDataStore      the data source.
     * @param graphicFactory    the graphic factory.
     * @param tileCache         where tiles are cached (needed if labels are drawn directly onto tiles, otherwise null)
     * @param labelStore        where labels are cached.
     * @param renderLabels      if labels should be rendered.
     * @param cacheLabels       if labels should be cached.
     * @param hillsRenderConfig the hillshading setup to be used (can be null).
     */
    public DirectRenderer(MapDataStore mapDataStore, GraphicFactory graphicFactory, TileCache tileCache, TileBasedLabelStore labelStore, boolean renderLabels, boolean cacheLabels, HillsRenderConfig hillsRenderConfig) {
        super(mapDataStore, graphicFactory, tileCache, labelStore, renderLabels, cacheLabels, hillsRenderConfig);
    }

    /**
     * Note (2024): No-op, does nothing. Here just for compatibility.
     */
    public void addTileRefresher(TileRefresher tileRefresher) {
    }

    public interface TileRefresher {
        /**
         * Note (2024): Will not be called. Here just for compatibility.
         */
        void refresh(Tile tile);
    }
}
