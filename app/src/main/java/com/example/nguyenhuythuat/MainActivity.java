package com.example.nguyenhuythuat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nguyenhuythuat.database.AppDatabase;
import com.example.nguyenhuythuat.database.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    EditText edUser, edEmail, edComment;
    Spinner spinner;
    CheckBox ckAgree;
    Button btnSend;
    String type = "Gripe";
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = AppDatabase.getAppDatabase(this);

        ckAgree = findViewById(R.id.ck);
        edUser = findViewById(R.id.edUser);
        edEmail = findViewById(R.id.edEmail);
        edComment = findViewById(R.id.edComment);
        spinner = findViewById(R.id.spinner);
        btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        String[] listType = {"Gripe", "Spinner", "Unknown"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listType);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG", "onItemSelected: "+listType[i]);
                type = listType[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void onSendFeedbacks() {
        if (!validate()) {
            return;
        }
        User user = new User();
        user.username = edUser.getText().toString();
        user.comment = edComment.getText().toString();
        user.type = type;
        long id = db.userDao().insertUser(user);
        if (id > 0) {
            Toast.makeText(this, "You have "+id + "records", Toast.LENGTH_LONG).show();
        }
        goToListUser();
    }

    private void goToListUser() {
        Intent intent = new Intent(this, com.example.nguyenhuythuat.ListUserActivity.class);
        startActivity(intent);
    }

    private boolean validate() {
        String mes = null;
        if(edUser.getText().toString().trim().isEmpty()) {
            mes = "Do not input username yet!";
        } else if(edEmail.getText().toString().trim().isEmpty()) {
            mes = "Do not input email yet!";
        } else if(edComment.getText().toString().trim().isEmpty()) {
            mes = "Do not input introduction";
        }else if(!ckAgree.isChecked()) {
            mes = "You have to agree used condition";
        }
        if(mes != null) {
            Toast.makeText(this, mes, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                onSendFeedbacks();
                break;
            default:
                break;
        }
    }
}