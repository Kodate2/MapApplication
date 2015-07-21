package jp.android.myapp.mapapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }



    @Override
    public void onStart(){
        super.onStart();

        Button btn = (Button)findViewById(R.id.regButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, AddListActivity.class);

                EditText name = (EditText)findViewById(R.id.editName);
                EditText address = (EditText)findViewById(R.id.editAddress);
                EditText opening = (EditText)findViewById(R.id.editOpening);
                EditText parking = (EditText)findViewById(R.id.editParking);

                intent.putExtra("name", name.getText().toString());
                intent.putExtra("address", address.getText().toString());
                intent.putExtra("opening", opening.getText().toString());
                intent.putExtra("parking", parking.getText().toString());

                startActivity(intent);

            }
        });
    }

}
