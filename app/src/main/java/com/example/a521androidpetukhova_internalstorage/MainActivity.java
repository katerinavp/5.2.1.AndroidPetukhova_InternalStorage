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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editLogin;
    private EditText editPassword;
    private static final String fileNameLogin = "5.2.1.Login";
    private static final String fileNamePassword = "5.2.1.Password";

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
        editPassword = findViewById(R.id.editPassword);

    }


    public void checkData() {

        if (editLogin.getText().toString().length() == 0 && editPassword.getText().toString().length() == 0) {
            Toast.makeText(MainActivity.this, R.string.fill_in_Login_and_Password, Toast.LENGTH_LONG).show();
        } else if (!(editLogin.getText().toString().length() == 0) && editPassword.getText().toString().length() == 0) {
            Toast.makeText(MainActivity.this, R.string.fill_in_Password, Toast.LENGTH_LONG).show();
        } else if (editLogin.getText().toString().length() == 0 && !(editPassword.getText().toString().length() == 0)) {
            Toast.makeText(MainActivity.this, R.string.fill_in_Login, Toast.LENGTH_LONG).show();

        }

    }

    private void setOnClickButton() {
        findViewById(R.id.btnReg).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                checkData();
                if (!((editLogin.getText().toString().length()) == 0) && !(editPassword.getText().toString().length() == 0)) {
//
//                    1 способ для записи
//                    File file = new File(getFilesDir(), "Login and  password");
//                    try (FileWriter fw = new FileWriter(file, true)) {
//                    fw.write("Login and  password");
//                } catch (
//                        IOException e) {
//                    e.printStackTrace();
//                }
                    // 2 рекомендуемый метод для чтения из интернета данных
                    try (FileOutputStream fileOutputStreamLogin = openFileOutput(fileNameLogin, MODE_PRIVATE)) {
                        fileOutputStreamLogin.write(editLogin.getText().toString().getBytes());
                    } catch (
                            IOException e) {
                        e.printStackTrace();
                    }
                    try (FileOutputStream fileOutputStreamPassword = openFileOutput(fileNamePassword, MODE_PRIVATE)) {
                        fileOutputStreamPassword.write(editPassword.getText().toString().getBytes());

                    } catch (
                            IOException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MainActivity.this, R.string.Save_Login_and_Password, Toast.LENGTH_LONG).show();
                }
            }
        });


        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkData();
                if (!((editLogin.getText().toString().length()) == 0) && !(editPassword.getText().toString().length() == 0)) {

                    try (InputStreamReader inputStreamReaderLogin = new InputStreamReader((openFileInput(fileNameLogin)))) {
                        BufferedReader readerLogin = new BufferedReader(inputStreamReaderLogin);
                        String lineLogin = readerLogin.readLine();
                        if (lineLogin.equals(editLogin.getText().toString())) {
                            Toast.makeText(MainActivity.this, R.string.Login_is_correct, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.Login_is_incorrect, Toast.LENGTH_LONG).show();
                        }
                    } catch (
                            IOException e) {
                        e.printStackTrace();

                    }

                    try (InputStreamReader inputStreamReaderPassword = new InputStreamReader((openFileInput(fileNamePassword)))) {
                        BufferedReader readerPassword = new BufferedReader(inputStreamReaderPassword);
                        String linePassword = readerPassword.readLine();

                        if (linePassword.equals(editPassword.getText().toString())) {
                            Toast.makeText(MainActivity.this, R.string.Password_is_correct, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.Password_is_incorrect, Toast.LENGTH_LONG).show();
                        }
                    } catch (
                            IOException e) {
                        e.printStackTrace();

                    }
                }
            }

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
            Toast.makeText(this, R.string.file_is_not_exist, Toast.LENGTH_SHORT).show();
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



