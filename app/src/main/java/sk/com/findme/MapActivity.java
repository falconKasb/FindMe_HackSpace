package sk.com.findme;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.drag.DragAndDropItem;
import ru.yandex.yandexmapkit.overlay.drag.DragAndDropOverlay;
import ru.yandex.yandexmapkit.utils.GeoPoint;


public class MapActivity extends ActionBarActivity {

    MapController mMapController;
    OverlayManager mOverlayManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_map);

        final MapView mapView = (MapView) findViewById(R.id.map);
        mMapController = mapView.getMapController();
        mOverlayManager = mMapController.getOverlayManager();
        showObject();
    }

    public void showObject(){
        Resources res = getResources();

        float density = getResources().getDisplayMetrics().density;
        int offsetX = (int)(-7 * density);
        int offsetY = (int)(20 * density);

        DragAndDropOverlay overlay = new DragAndDropOverlay(mMapController);

        DragAndDropItem drag1Item = new DragAndDropItem(new GeoPoint(55.752004 , 37.617017), res.getDrawable(R.drawable.image1));

        drag1Item.setOffsetX(offsetX);
        drag1Item.setOffsetY(offsetY);
        drag1Item.setDragable(true);

        BalloonItem balloonDrar1 = new BalloonItem(this,drag1Item.getGeoPoint());
        balloonDrar1.setText("Место встречи");
        balloonDrar1.setOffsetX(offsetX);
        drag1Item.setBalloonItem(balloonDrar1);
        overlay.addOverlayItem(drag1Item);

        mOverlayManager.addOverlay(overlay);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
