package com.example.a521androidpetukhova_internalstorage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText editLogin;
    EditText editReg;
   Map<String, String> dataReg = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setOnClickButton();

        findViewById(R.id.fab_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Здесь отправляется всё, что у вас на внутреннем хранилище. Если надо из внутреннего отправить,
                // то поменяйте на getExternalFilesDir(null) на getFilesDir()
                File filesDir = getFilesDir();
                if (filesDir != null) {
                    sendFiles(filesDir);
                }
            }
        });
    }


    public void init() {
        editLogin = findViewById(R.id.editLogin);
        editReg = findViewById(R.id.editReg);


//        String filename = "notes.txt";
//        File file = new File(getFilesDir(), filename);
//        try (FileWriter fw = new FileWriter(file, true)) {
//            fw.write("");
//
//
//        } catch (
//                IOException e) {
//            e.printStackTrace();
//        }

    }




    private void setOnClickButton() {
        findViewById(R.id.btnReg).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                File file = new File(getFilesDir(), "5.2.1.LoginAndPassword");
//                try (FileWriter fw = new FileWriter(file, true)) {
//                    fw.write("Login");
//                } catch (
//                        IOException e) {
//                    e.printStackTrace();
//                }

                try (FileOutputStream fileOutputStream = openFileOutput("5.2.1.LoginAndPassword", MODE_PRIVATE)) {
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    // Запишем текст в поток вывода данных, буферизуя символы так, чтобы обеспечить эффективную запись отдельных символов.
                    BufferedWriter bw = new BufferedWriter(outputStreamWriter);
                    // Осуществим запись данных

                    bw.write(editLogin.getText().toString());
                    bw.write(editReg.getText().toString());
                    Toast.makeText(MainActivity.this, "Логин и пароль сохранены", Toast.LENGTH_LONG).show();
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }

        });


        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try (FileInputStream fileInputStream = openFileInput("5.2.1.LoginAndPassword")) {// Получим входные байты из файла которых нужно прочесть.
                    // Декодируем байты в символы
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    // Читаем данные из потока ввода, буферизуя символы так, чтобы обеспечить эффективную запись отдельных символов.
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    reader.readLine();// чтение строки из консоли
                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
            }
            //собирать одну строку в несколько StringBuilder
        });
    }
    private void sendFiles(@NonNull File root) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
        intent.setType("*/*");
        ArrayList<Uri> files = new ArrayList<>();
        File[] listFiles = root.listFiles();
        if (listFiles == null) {
            Toast.makeText(this, "Нет файлов!", Toast.LENGTH_SHORT).show();
            return;
        }
        for (File file : listFiles) {
            Uri uri = FileProvider.getUriForFile(
                    MainActivity.this,
                    getString(R.string.file_provider_authority),
                    file);
            files.add(uri);
        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        startActivity(intent);
    }
}



