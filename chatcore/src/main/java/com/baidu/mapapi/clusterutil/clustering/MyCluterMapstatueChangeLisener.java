package com.baidu.mapapi.clusterutil.clustering;

import com.baidu.mapapi.map.MapStatus;

/**
 * Created by colin on 16/9/1.
 */
public interface MyCluterMapstatueChangeLisener {
    void onMapStatusChangeStart(MapStatus status);

    void onMapStatusChange(MapStatus status);

    void onMapStatusChangeFinish(MapStatus status);
}
