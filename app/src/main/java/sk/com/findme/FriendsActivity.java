package sk.com.findme;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import sk.com.findme.Adapters.ListFriendsAdapter;
import sk.com.findme.Classes.People;


public class FriendsActivity extends ActionBarActivity {
    final int PICK_CONTACT = 2;
    ListView friendsList;
    Button addContactButton;
    Button showOnMapButton;
    ArrayList<People> peoples;
    ListFriendsAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_friends);
        addContactButton = (Button)findViewById(R.id.buttonPlus);
        peoples = new ArrayList<People>();
        TextView textView = new TextView(this);
        textView.setText("Ваш список друзей пуст, добавьте друзей для дальнейшей работы");

        customAdapter = new ListFriendsAdapter(this,peoples);
        friendsList = (ListView)findViewById(R.id.listViewFriends);
        friendsList.setEmptyView(textView);
        friendsList.setAdapter(customAdapter);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
        friendsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                People people = peoples.get(position);
                callAlert(people.getName());
            }
        });
        showOnMapButton = (Button)findViewById(R.id.buttonMap);
        showOnMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map();
            }
        });
    }
    public void map()
    {
        Intent mapIntent = new Intent(this,MapActivity.class);
        startActivity(mapIntent);
    }
    private void callAlert(String text)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Вы действительно хотите назначить встречу: "+ text);
        builder.setTitle(text);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToMap();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void goToMap()
    {
        Intent mapActivity = new Intent(this,MapActivity.class);
        startActivity(mapActivity);
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode,resultCode,data);
        switch (reqCode) {
            case (PICK_CONTACT) :
                if(resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        Long id = c.getLong(c.getColumnIndex(ContactsContract.Contacts._ID));
                        InputStream stream = openPhoto(id);
                        Bitmap element = BitmapFactory.decodeStream(stream);
                        peoples.add(new People(element,name));
                        customAdapter = new ListFriendsAdapter(this,peoples);
                        friendsList.setAdapter(customAdapter);
                    }
                }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friends, menu);
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

    public InputStream openPhoto(long contactId) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = getContentResolver().query(photoUri,
                new String[] {ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

}
