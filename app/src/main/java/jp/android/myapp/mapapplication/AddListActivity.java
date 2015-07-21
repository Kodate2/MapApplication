package jp.android.myapp.mapapplication;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddListActivity extends Activity {
    private ListView myListView;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);
    }

    private void insertToDB(String[] regData) throws Exception{
        //登録日の取得
        final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date(System.currentTimeMillis());
        String time = df.format(date);

        db.execSQL("insert into myData("
                + MyDBHelper.REG_NAME + ", "
                + MyDBHelper.REG_ADDRESS + ", "
                + MyDBHelper.REG_OPENING + ", "
                + MyDBHelper.REG_PARKING + ", "
                + MyDBHelper.REG_DATE + " )values('" + regData[0] + "', '"
                                                       + regData[1] + "', '"
                                                       + regData[2] + "', '"
                                                       + regData[3] + "', '"
                                                       + time + "')");
    }

    private Cursor searchToDB() throws Exception {

        MyDBHelper dbHelper = new MyDBHelper(this);
        db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("select * from " + MyDBHelper.TABLE_NAME, null);
        return c;

    }

    @Override
    public void onStart(){
        super.onStart();

        myListView = (ListView)findViewById(R.id.listView);

        Button btn = (Button)findViewById(R.id.transRegButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddListActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        MyDBHelper dbHelper = new MyDBHelper(this);
        db = dbHelper.getWritableDatabase();

        String[] regData = new String[4];

        Intent intent = getIntent();

        if(intent != null) {

            regData[0] = intent.getStringExtra("name");
            regData[1] = intent.getStringExtra("address");
            regData[2] = intent.getStringExtra("opening");
            regData[3] = intent.getStringExtra("parking");

            if (db != null) {
                try {

                    insertToDB(regData);

                    Cursor c = searchToDB();

                    String[] from = {MyDBHelper.REG_NAME, MyDBHelper.REG_ADDRESS, MyDBHelper.REG_OPENING, MyDBHelper.REG_PARKING, MyDBHelper.REG_DATE};

                    int[] to = {R.id.regName, R.id.regAddress, R.id.regOpening, R.id.regParking, R.id.regDate};

                    SimpleCursorAdapter adapter =
                            new SimpleCursorAdapter(this, R.layout.listitem_layout, c, from, to, 0);

                    myListView.setAdapter(adapter);

                    myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Intent intent = new Intent(AddListActivity.this, MapsActivity.class);

                            String address = ((TextView)findViewById(R.id.regAddress)).getText().toString();
                            //String namae = ((TextView)findViewById(R.id.regName)).getText().toString();

                            intent.putExtra("address",address);
                            //intent.putExtra("name",namae);

                            startActivity(intent);

                        }
                    });

                } catch (Exception e) {

                    e.printStackTrace();

                } finally {
                    db.close();
                }
            }
        }
    }


}
