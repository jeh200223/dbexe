package com.example.dbexe;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {
    SQLiteDatabase database = null;
    DBHelper helper;
    String DBNAME = "TestDB";
    boolean dbflag, tableflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.textview);
        helper = new DBHelper(this, DBNAME, null, 1);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!dbflag) {
                    database = helper.getWritableDatabase();
                    if(database == null) {
                        textView.append("\n DB가 생성되지 않았습니다.");
                    } else {
                        dbflag = true;
                        textView.append("\n DB가 생성되었습니다.");
                    }
                } else {
                    textView.append("\n 이미 DB가 생성되었습니다.");
                }
            }
        });

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbflag && !tableflag) {
                    helper.onCreate(database);
                    tableflag = true;
                    textView.append("\n 테이블이 생성되었습니다.");
                } else if (!dbflag) {
                    textView.append("\n DB를 먼저 생성해주세요.");
                } else {
                    textView.append("\n 이미 테이블이 생성되었습니다.");
                }
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableflag){
                    helper.deleteTable();
                    tableflag = false;
                    textView.append("\n 테이블이 삭제되었습니다.");
                } else {
                    textView.append("\n 테이블이 없습니다.");
                }
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableflag) {
                    long result = helper.insertData("홍길동", 21, "010-2369-4112");
                    if(result == -1L) {
                        textView.append("\n 중복된 데이터입니다.");
                    } else {
                        textView.append("\n 한개의 데이터가 삽입되었습니다.");
                    }
                } else {
                    textView.append("\n 테이블이 없습니다.");
                }
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableflag) {
                    Cursor cursor = helper.searchData();
                    if(cursor.getCount() == 0) {
                        textView.append("\n 데이터가 없습니다.");
                    } else {
                        cursor.moveToFirst();
                        for(int i = 0; i < cursor.getCount(); i ++) {
                            int id = cursor.getInt(0);
                            String name = cursor.getString(1);
                            int age = cursor.getInt(2);
                            String phone = cursor.getString(3);
                            textView.append("\n id = " + id + ", 이름 = " + name + ", 나이 = " + age + ", 번호 = " + phone);
                            cursor.moveToNext();
                        }
                    }
                    cursor.close();
                } else {
                    textView.append("\n 테이블이 없습니다.");
                }
            }
        });

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase database = helper.getWritableDatabase();
                String sql = "SELECT * FROM " + helper.TBLName + ";";
                Cursor cursor = database.rawQuery(sql, null);
                if(cursor.getCount() == 0) {
                    textView.append("\n 데이터가 없습니다.");
                } else {
                    cursor.moveToFirst();
                    int id = cursor.getInt(0);
                    helper.deleteData(id);
                    textView.append("\n 하나의 데이터를 삭제하였습니다.");
                }
            }
        });
    }
}