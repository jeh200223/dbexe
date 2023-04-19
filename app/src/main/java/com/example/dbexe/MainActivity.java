package com.example.dbexe;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase database = null;
    String DATABASE = "TestDB";
    boolean dbflag, tableflag = false;
    String TBLNAME = "member";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textview);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dbflag) {
                    database = openOrCreateDatabase(DATABASE, MODE_PRIVATE, null);
                    if (database == null) {
                        textView.append("\n 데이터베이스가 생성되지 않았습니다.");
                    } else {
                        textView.append("\n 데이터베이스가 생성되었습니다.");
                        dbflag = true;
                    }
                } else {
                    textView.append("\n 데이터베이스가 이미 생성되어있습니다.");
                }
            }
        });

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dbflag && !tableflag) {
                    String sql = "CREATE TABLE IF NOT EXISTS " + TBLNAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "name TEXT, age INTEGER, phone TEXT NOT NULL UNIQUE);";
                    database.execSQL(sql);
                    tableflag = true;
                    textView.append("\n 테이블이 생성되었습니다.");
                } else if (!dbflag){
                    textView.append("\n 먼저 DB부터 생성해주세요.");
                } else {
                    textView.append("\n 이미 테이블이 생성되어있습니다.");
                }
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableflag) {
                    String sql = "DROP TABLE IF EXISTS " + TBLNAME + ";";
                    database.execSQL(sql);
                    tableflag = false;
                    textView.append("\n 테이블이 제거되었습니다.");
                } else {
                    textView.append("\n 삭제할 테이블이 없습니다.");
                }
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableflag) {
                    String sql = "INSERT INTO " + TBLNAME + "(name, age, phone) VALUES" + "('홍길동', 21, '010-2369-4112');";
                    try {
                        database.execSQL(sql);
                        textView.append("\n 하나의 데이터가 삽입되었습니다.");
                    } catch (Exception e) {
                        textView.append("\n 데이터가 중복되었습니다.");
                    }
                } else {
                    textView.append("\n 테이블이 준비되지않았습니다.");
                }
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableflag) {
                    String sql = "SELECT * FROM " + TBLNAME + ";";
                    Cursor cursor = database.rawQuery(sql, null);
                    int count = cursor.getCount();
                    if(count == 0) {
                        textView.append("\n 테이블이 비어있습니다.");
                    } else {
                        cursor.moveToFirst();
                        for (int i = 0; i < count; i++) {
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
                    textView.append("\n 테이블이 준비되지않았습니다.");
                }
            }
        });

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tableflag) {
                    String sql = "SELECT * FROM " + TBLNAME + ";";
                    Cursor cursor = database.rawQuery(sql, null);
                    if (cursor.getCount() == 0) {
                        textView.append("\n 테이블이 비어있습니다.");
                    } else {
                        cursor.moveToFirst();
                        int id = cursor.getInt(0);
                        sql = "DELETE FROM " + TBLNAME + " WHERE _id = '" + id + "';";
                        database.execSQL(sql);
                        textView.append("\n 하나의 데이터를 삭제하였습니다.");
                    }
                    cursor.close();
                } else {
                    textView.append("\n 테이블이 준비되지않았습니다.");
                }
            }
        });
    }
}